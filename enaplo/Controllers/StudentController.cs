using System.Security.Claims;
using enaplo.Dtos;
using enaplo.Repositories;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace enaplo.Controllers;

[ApiController]
[Route("student")]
[Authorize(Roles = "student")]
public class StudentController : ControllerBase
{
    private readonly IStudentRepository repository;

    public StudentController(IStudentRepository _repository)
    {
        repository = _repository;
    }

    private UserDto? GetCurrentUser()
    {
        var identity = HttpContext.User.Identity as ClaimsIdentity;

        if (identity != null)
        {
            var userClaims = identity.Claims;

            var userId = userClaims.FirstOrDefault(o => o.Type == ClaimTypes.NameIdentifier)?.Value;
            var role = userClaims.FirstOrDefault(o => o.Type == ClaimTypes.Role)?.Value;
            if (userId != null && role != null)
                return new UserDto
                (
                    Int16.Parse(userId),
                    role
                );
        }
        return null;
    }

    // Hianyzás
    [HttpGet("absence")]
    public async Task<IActionResult> GetStudentAbsencesAsync() 
    {
        var user = GetCurrentUser();
        
        if (user != null)
            return Ok(await repository.GetAbsencesAsync(user.UserId));
        return BadRequest();
    }

    // Megrovás
    [HttpGet("admonitory")]
    public async Task<IActionResult> GetStudentAdmonitoriesAsync()
    {
        var user = GetCurrentUser();
        
        if (user != null)
            return Ok(await repository.GetAdmonitoriesAsync(user.UserId));
        return BadRequest();
    }

    // Osztály adatai, hetessel
    [HttpGet("class")]
    public async Task<IActionResult> GetStudentClassAsync() 
    {
        var user = GetCurrentUser();
        
        if (user != null)
            return Ok(await repository.GetClassAsync(user.UserId));
        return BadRequest();
    }

    // Tervezett dolgozatok
    [HttpGet("exams")]
    public async Task<IActionResult> GetStudentExamsAsync()
    {
        var user = GetCurrentUser();
        
        if (user != null)
            return Ok(await repository.GetExamsAsync(user.UserId));
        return BadRequest();
    }

    // Jegyek Tantargyhoz nem speciálisaknál
    [HttpGet("grades")]
    public async Task<IActionResult> GetStudentSubjectGradesAsync([FromQuery]GetGradeDto grade)
    {
        var user = GetCurrentUser();
        
        if (user != null)
           return Ok(await repository.GetSubjectGradesAsync(user.UserId, grade));
        return BadRequest();
    }

    // Csoport tagjainak lekérése
    [HttpGet("groupmembers/{group}")]
    public async Task<IActionResult> GetStudentGroupMembersAsync([FromRoute]string group) 
    {
        var user = GetCurrentUser();
        
        if (user != null)
           return Ok(await repository.GetGroupMembersAsync(user.UserId, group));
        return BadRequest();
    }

    // Késés
    [HttpGet("late")]
    public async Task<IActionResult> GetStudentLatesAsync() 
    {
        var user = GetCurrentUser();
        
        if (user != null)
           return Ok(await repository.GetLatesAsync(user.UserId));
        return BadRequest();
    }

    // Üzenetek
    [HttpGet("messages")]
    public async Task<IActionResult> GetStudentMessagesAsync()
    {
        var user = GetCurrentUser();
        
        if (user != null)
           return Ok(await repository.GetMessagesAsync(user.UserId));
        return BadRequest();
    }

    // Dicséret
    [HttpGet("propitious")]
    public async Task<IActionResult> GetStudentPropitiousesAsync()
    {
        var user = GetCurrentUser();
        
        if (user != null)
           return Ok(await repository.GetPropitiousesAsync(user.UserId));
        return BadRequest();
    }

    // Jegyek Összegző Speciálisak nélkül
    [HttpGet("sumgrades")]
    public async Task<IActionResult> GetStudentGradesSumAsync()
    {
        var user = GetCurrentUser();
        
        if (user != null)
           return Ok(await repository.GetGradesSumAsync(user.UserId));
        return BadRequest();
    }

    // Órarend
    [HttpGet("timetable/{date}")]
    public async Task<IActionResult> GetStudentTimetableAsync([FromRoute]DateTime date)
    {
        var user = GetCurrentUser();
        
        if (user != null)
           return Ok(await repository.GetTimetableAsync(user.UserId, date));
        return BadRequest();
    }

    // MyName
    [HttpGet("name")]
    public async Task<IActionResult> NameAsync()
    {
        var user = GetCurrentUser();
        
        if (user != null) {
            var result = await repository.GetNameAsync(user.UserId);
            if (result != null)
                return Ok(new StringDto(result));
        }
        return BadRequest();
    }
}