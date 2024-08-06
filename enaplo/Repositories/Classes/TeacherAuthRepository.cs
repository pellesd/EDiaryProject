using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;
using enaplo.Dal;
using enaplo.Dtos;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Cryptography.KeyDerivation;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;

namespace enaplo.Repositories;
public class TeacherAuthRepository : AuthRepository, ITeacherAuthRepository
{
    public TeacherAuthRepository(ENAPLOContext context, IConfiguration config)
    : base(context, config)
    {
    }

    private async Task<string> CreatePasswordHashAsync(LoginDto user) 
    {
        var salt = await GetUserSaltAsync(user.Username);
        var pass = user.Password;

        string hashed = Convert.ToBase64String(KeyDerivation.Pbkdf2(
            password: pass,
            salt: salt,
            prf: KeyDerivationPrf.HMACSHA256,
            iterationCount: 100000,
            numBytesRequested: 64));
        
        return hashed;
    }

    public async void setAllTeacherPassword(string newPw) 
    {
        var users = await context.Users.ToListAsync();
        foreach (var user in users) 
        {   
            if (user.Username != "admin")
                user.PasswordHash = await CreatePasswordHashAsync(
                    new LoginDto(user!.Username!, newPw));
        }
        await context.SaveChangesAsync();
    }

    //segedfuggveny UserId és salt felhasználásával visszaadja a személy salt-ot byte[]-ként
    private async Task<byte[]> GetUserSaltAsync(string username)
    {
        // Visszaadja felhasznalo nev alapjan a UserId-t
        var userId = await context
            .Users
            .Where(u => u.Username == username)
            .Select(u => u.Id)
            .SingleOrDefaultAsync();
        
        var salt = config["Salt"] + userId.ToString();

        return Encoding.ASCII.GetBytes(salt);
    }
    
    public async Task<string?> LoginAsync(LoginDto user)
    {
        var hashedPassword = await CreatePasswordHashAsync(user);
        var userDb = await context
            .Users
            .Where(u => u.Username == user.Username)
            .SingleOrDefaultAsync();
        if (userDb == null)
        {   
            // username is not exist
            return null;
        }
        var hashedPasswordDb = userDb.PasswordHash;

        var userId = userDb.Id;

        if (String.Compare(hashedPassword, hashedPasswordDb) == 0) {
            UserDto newUser; 

            if (userDb.Username == "admin")
                newUser = new UserDto(userId, "admin");
            else
                newUser = new UserDto(userId, "teacher");

            var token = GenerateJwtToken(newUser);
            return token;
        }
        // Wrong password
        return null;
    }
}