global using enaplo.Models;
using enaplo.Dal;
using enaplo.Extensions;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddDbContext<ENAPLOContext>(options => options.UseSqlServer(builder.Configuration.GetConnectionString("EnaploDatabase")));

//builder.Services.RegisterAuth();
builder.Services.RegisterRepos();
builder.RegisterAuth(); // builder extension because on IConfig
builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.RegisterAuthSwagger();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (true)//(app.Environment.IsDevelopment() || app.Environment.IsProduction())
{
    app.UseSwagger();
    app.UseSwaggerUI(options =>
    {
        options.SwaggerEndpoint("/swagger/v1/swagger.json", "v1");
        options.RoutePrefix = string.Empty;
    });
}

app.UseHttpsRedirection();
app.UseAuthentication();
app.MapControllers();
app.UseAuthorization();
app.Run();
