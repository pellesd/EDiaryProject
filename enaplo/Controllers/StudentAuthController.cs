using System.Security.Claims;
using enaplo.Dtos;
using enaplo.Repositories;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace enaplo.Controllers;

[ApiController]
[Route("studentauth")]
public class StudentAuthController : ControllerBase
{
    private readonly IStudentAuthRepository repository;

    public StudentAuthController(IStudentAuthRepository _repository)
    {
        repository = _repository;
    }

    [AllowAnonymous]
    [HttpPost("login")]
    public async Task<IActionResult> LoginAsync(LoginDto user) 
    {
        var token = await repository.LoginAsync(user);
        if (token == null)
            return Unauthorized("Username or password is invalid!");
        return Ok(new StringDto(token));
    }
}