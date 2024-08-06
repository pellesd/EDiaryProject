using System.Text;
using enaplo.Dal;
using enaplo.Dtos;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;

namespace enaplo.Repositories;
public class StudentAuthRepository : AuthRepository, IStudentAuthRepository
{
    
    public StudentAuthRepository(ENAPLOContext context, IConfiguration config)
    : base(context, config)
    {
    }

    public async Task<string?> LoginAsync(LoginDto user)
    {
        int password;
        try { // if pw is not an int, than it can't be good
            password = Int32.Parse(user.Password);
        } catch { 
            return null;
        }
        var studentDb = await context
            .Students
            .Where(t => t.UserId == user.Username)
            .Where(t => t.Id == password)
            .SingleOrDefaultAsync();
        
        if (studentDb != null) 
        {
            var newUser = new UserDto(
                studentDb.Id,
                "student"
            );

            var token = GenerateJwtToken(newUser);

            return token;
        }
        return null;
    }
}