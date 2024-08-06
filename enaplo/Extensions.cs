using System.Text;
using enaplo.Repositories;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;
using Microsoft.OpenApi.Models;

namespace enaplo.Extensions;

public static class ServiceExtensions
{
    public static void RegisterRepos(this IServiceCollection collection)
    {
        collection.AddTransient<IStudentRepository, StudentRepository>();
        collection.AddTransient<ITeacherRepository, TeacherRepository>();
        collection.AddTransient<IBasicRepository, BasicRepository>();
        collection.AddTransient<IStudentAuthRepository, StudentAuthRepository>();
        collection.AddTransient<ITeacherAuthRepository, TeacherAuthRepository>();
    }
    public static void RegisterAuthSwagger(this IServiceCollection collection)
    {
        collection.AddSwaggerGen(c => {
            c.SwaggerDoc("v1", new OpenApiInfo {
                Title = "Enaplo", Version = "v1"
            });
            c.AddSecurityDefinition("Bearer", new OpenApiSecurityScheme() {
                Name = "Authorization",
                    Type = SecuritySchemeType.ApiKey,
                    Scheme = "Bearer",
                    BearerFormat = "JWT",
                    In = ParameterLocation.Header,
                    Description = "JWT Authorization header using the Bearer scheme. \r\n\r\n Enter 'Bearer' [space] and then your token in the text input below.\r\n\r\nExample: \"Bearer 1safsfsdfdfd\"",
            });
            c.AddSecurityRequirement(new OpenApiSecurityRequirement {
                {
                    new OpenApiSecurityScheme {
                        Reference = new OpenApiReference {
                            Type = ReferenceType.SecurityScheme,
                                Id = "Bearer"
                        }
                    },
                    new string[] {}
                }
            });
        });
    }
}

public static class WebApplicationBuilderExtensions
{
    public static void RegisterAuth(this WebApplicationBuilder builder)
    {
        builder.Services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
        .AddJwtBearer(options => {
            {
                    options.TokenValidationParameters = new TokenValidationParameters
                    {
                        ValidateIssuer = true,
                        ValidateAudience = true,
                        ValidateLifetime = true,
                        ValidateIssuerSigningKey = true,
                        ValidIssuer = builder.Configuration["Jwt:Issuer"],
                        ValidAudience = builder.Configuration["Jwt:Audience"],
                        IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(builder.Configuration["Jwt:Key"]))
                    };
            }
        });
    }
}