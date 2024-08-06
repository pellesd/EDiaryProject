using enaplo.Dtos;
using enaplo.Repositories;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace enaplo.Controllers;

[ApiController]
[Route("teacherauth")]
public class TeacherAuthController : ControllerBase
{
    private readonly ITeacherAuthRepository repository;

    public TeacherAuthController(ITeacherAuthRepository _repository)
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

    // only for testing! Admin can set all User password except his into a custom value
    /*[Authorize(Roles = "admin")]
    [HttpGet("setTeacherPasswords")]
    public IActionResult setAllTeacherPassword(StringDto newPw) 
    {
        repository.setAllTeacherPassword(newPw.Value);
        return Ok(new StringDto("Jelszavak sikeresen beállítva!"));
    }*/
}