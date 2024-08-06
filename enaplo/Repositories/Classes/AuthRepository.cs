using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;
using enaplo.Dal;
using enaplo.Dtos;
using Microsoft.AspNetCore.Cryptography.KeyDerivation;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;

namespace enaplo.Repositories;
public class AuthRepository
{
    protected readonly ENAPLOContext context;
    protected readonly IConfiguration config;
    public AuthRepository(ENAPLOContext _context, IConfiguration _config)
    {
        this.context = _context;
        this.config = _config;
    }

    protected string GenerateJwtToken(UserDto user)
    {
        var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(config["Jwt:Key"]));
            var credentials = new SigningCredentials(securityKey, SecurityAlgorithms.HmacSha256);

            var claims = new[]
            {
                new Claim(ClaimTypes.NameIdentifier, user.UserId.ToString()),
                new Claim(ClaimTypes.Role, user.Role)
            };

            var token = new JwtSecurityToken(config["Jwt:Issuer"],
              config["Jwt:Audience"],
              claims,
              expires: DateTime.Now.AddDays(14),
              signingCredentials: credentials);

            return new JwtSecurityTokenHandler().WriteToken(token);
    }
}