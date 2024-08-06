using System;
using System.Security.Claims;
using enaplo.Dtos;
using enaplo.Repositories;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace enaplo.Controllers;

// TODO saveChanges() nem szabadna h repositoryban legyen, de csomo.
// TODO a jegyek jelenleg nem annyiszorozodnak ahányszor kéne a típus miatt
// TODO string == összehasonlításokat ki kéne szedni, és utánanézni, hogy miért is?
// TODO adott tanuló hiányzásainak, késéseinek, figy és dicseinek lekérése?
[ApiController]
[Route("teacher")]
[Authorize(Roles = "teacher,admin")]
public class TeacherController : ControllerBase
{
    private readonly ITeacherRepository repository;

    public TeacherController(ITeacherRepository _repository)
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

    // Adott napi órarend lekérdezése
    [HttpGet("timetable/{date}")]
    public async Task<IActionResult> GetTeacherTimetableAsync([FromRoute] DateTime date)
    {
        var user = GetCurrentUser();

        if (user != null)
        {
            return Ok(await repository.GetTimetableAsync(user.UserId, date));
        }
        return BadRequest();
    }

    // Elküldött üzenetek előtöltése
    [HttpGet("outmessages/teacher")]
    public async Task<IActionResult> GetTeacherMessagesSentToTeacherAsync()
    {
        var user = GetCurrentUser();

        if (user != null)
            return Ok(await repository.GetMessagesSentToTeacherAsync(user.UserId));
        return BadRequest();
    }

    // Elküldött üzenetek előtöltése
    [HttpGet("outmessages/group")]
    public async Task<IActionResult> GetTeacherMessagesSentToGroupAsync()
    {
        var user = GetCurrentUser();

        if (user != null)
            return Ok(await repository.GetMessagesSentToGroupAsync(user.UserId));
        return BadRequest();
    }

    // Elküldött üzenetek előtöltése
    [HttpGet("outmessages/group/{messageid}")]
    public async Task<IActionResult> GetTeacherMessageSentToGroupDetailsAsync([FromRoute]int messageid)
    {
        var user = GetCurrentUser();

        if (user != null)
            return Ok(await repository.GetGroupMembersByMessageAsync(user.UserId, messageid));
        return BadRequest();
    }

    // Specifikus üzenet megtekintése, olvasattnak jelölése
    [HttpGet("messages/{messageid}")]
    public async Task<IActionResult> GetTeacherMessageFromTeacherAsync([FromRoute] int messageid)
    {
        var user = GetCurrentUser();
        if (user != null)
        {
            var message = await repository.GetMessageFromTeacherAsync(user.UserId, messageid);
            if (message == null)
                return NotFound("Üzenet nem található.");
            return Ok(message);
        }
        return BadRequest();
    }

    // Tanártársaim lekérése
    [HttpGet("teachers")]
    public async Task<IActionResult> GetTeacherOtherTeachersAsync()
    {
        var user = GetCurrentUser();

        if (user != null)
            return Ok(await repository.GetOtherTeachersAsync(user.UserId));
        return BadRequest();
    }

    // Üzenet küldése tanártársnak
    [HttpPost("message/teacher")]
    public async Task<IActionResult> PostTeacherSendMessageToTeacherAsync(PostMessageDto message)
    {
        var user = GetCurrentUser();

        if (user != null)
        {
            await repository.PostSendMessageToTeacherAsync(user.UserId, message);
            return Ok(new StringDto("Üzenet sikeresen elküldve!"));
        }
        return BadRequest();
    }

    // Kapott üzenetek előtöltése
    [HttpGet("messages")]
    public async Task<IActionResult> GetTeacherMessagesFromTeacherAsync()
    {
        var user = GetCurrentUser();

        if (user != null)
            return Ok(await repository.GetMessagesFromTeacherAsync(user.UserId));
        return BadRequest();
    }

    // Tanított csoportok lekérése
    [HttpGet("groups")]
    public async Task<IActionResult> GetTeacherTaughtGroupsAsync()
    {
        var user = GetCurrentUser();

        if (user != null)
            return Ok(await repository.GetTaughtGroupsAsync(user.UserId));
        return BadRequest();
    }

    // Üzenet küldése a csoportnak
    [HttpPost("message/group")]
    public async Task<IActionResult> PostTeacherSendMessageToGroupAsync(MessageToTeacherDto message)
    {
        var user = GetCurrentUser();
        if (user != null)
        {
            await repository.PostSendMessageToGroupAsync(user.UserId, message);
            return Ok(new StringDto("Üzenet a csoportnak sikeresen elküldve!"));
        }
        return BadRequest();
    }

    // DicseretTipusok lekerese
    [HttpGet("propitious/types")]
    public async Task<IActionResult> GetPropitiousTypesAsync()
    {
        return Ok(await repository.GetPropitiousTypesAsync());
    }

    // FigyelmeztetesTipusok
    [HttpGet("admonitory/types")]
    public async Task<IActionResult> GetAdmonitoryTypesAsync()
    {
        return Ok(await repository.GetAdmonitoryTypesAsync());
    }

    // Egy tanított csoportom tagjainak lekérése
    [HttpGet("groupmembers/{groupid}")]
    public async Task<IActionResult> GetGroupMembersAsync([FromRoute] int groupid)
    {
        var members = await repository.GetGroupMembersAsync(groupid);
        if (members != null)
        {
            return Ok(members);
        }
        return BadRequest("Üres vagy nem létező csoport!");
    }

    // Dicseret beirasa diaknak
    [HttpPost("propitious")]
    public async Task<IActionResult> PostProptiousToStudentAsync(NewJudgementDto judgement)
    {
        var user = GetCurrentUser();
        if (user != null)
        {
            await repository.PostProptiousToStudentAsync(user.UserId, judgement);
            return Ok(new StringDto("A dicséret rögzítése sikeresen megtörtént!"));
        }
        return BadRequest();
    }

    // Elmarasztalas beirasa diaknak
    [HttpPost("admonitory")]
    public async Task<IActionResult> PostAdmonitoryToStudentAsync(NewJudgementDto judgement)
    {
        var user = GetCurrentUser();
        if (user != null)
        {
            await repository.PostAdmonitoryToStudentAsync(user.UserId, judgement);
            return Ok(new StringDto("A figyelmeztetés rögzítése sikeresen megtörtént!"));
        }
        return BadRequest();
    }

    // Adott tanorabeiras lekerese
    [HttpGet("registerlesson")]
    public async Task<IActionResult> GetRegisterLessonAsync([FromQuery] RegisterLessonKeysDto keys)
    {
        var user = GetCurrentUser();

        if (user != null)
        {
            var register = await repository.GetRegisterLessonAsync(user.UserId, keys);
            if (register == null)
                return BadRequest("A keresett óra nem található");
            return Ok(register);
        }
        return BadRequest();
    }

    // Adott tanorabeiras frissitese
    [HttpPost("registerlesson")]
    public async Task<IActionResult> PostRegisterLessonAsync(PostRegisterLessonDto lesson)
    {
        var register_success = await repository.PostRegisterLessonAsync(lesson);
        if (register_success)
            return Ok(new StringDto("A tanóra regisztrálása/frissítése sikeres volt"));
        return BadRequest("Hiba történt a tanóra rögzítése során.");
    }

    [HttpGet("absence")]
    public async Task<IActionResult> GetMissingStudentsAsync([FromQuery] int lessonid, [FromQuery] int dividendid)
    {
        var user = GetCurrentUser();
        if (user != null)
        {
            return Ok(await repository.GetMissingStudentsAsync(
                user.UserId, lessonid, dividendid));
        }
        return BadRequest();
    }

    [HttpPost("absence")]
    public async Task<IActionResult> PostMissingStudentsAsync(PostMissingDto postMissingDto)
    {
        var user = GetCurrentUser();
        if (user != null)
        {
            await repository.PostMissingStudentsAsync(user.UserId, postMissingDto);
            return Ok(new StringDto("Hiányzások rögzítése sikeres volt!"));
        }
        return BadRequest();
    }


    [HttpGet("late")]
    public async Task<IActionResult> GetLateAsync([FromQuery] int lessonid,[FromQuery] int dividendid)
    {
        var user = GetCurrentUser();
        if (user != null)
        {
            return Ok(await repository.GetLateAsync(
                user.UserId, lessonid, dividendid));
        }
        return BadRequest();
    }

    [HttpPost("late")]
    public async Task<IActionResult> PostLateAsync(PostLateDto postLateDto)
    {
        var user = GetCurrentUser();
        if (user != null)
        {
            await repository.PostLateAsync(user.UserId, postLateDto);
            return Ok(new StringDto("Késések rögzítése sikeres volt!"));
        }
        return BadRequest();
    }

    // MyName
    [HttpGet("name")]
    public async Task<IActionResult> NameAsync()
    {
        var user = GetCurrentUser();
        
        if (user != null)
           return Ok(new StringDto(await repository.GetNameAsync(user.UserId)));
        return BadRequest();
    }

    [HttpGet("grade/types")]
    public async Task<IActionResult> GetGradeTypesAsync()
    {
        return Ok(await repository.GetGradeTypesAsync());
    }

    [HttpPost("grade")]
    public async Task<IActionResult> PostGradeAsync(PostGradeDto grade)
    {
        var user = GetCurrentUser();

        if (user != null && await repository.PostGradeAsync(user.UserId, grade))
            return Ok(new StringDto("A Jegy mentése sikeres volt!"));
        return BadRequest(new StringDto("Hiba! Sikertelen mentés!"));
    }

    [HttpPost("grades")]
    public async Task<IActionResult> PostGradesAsync(List<PostGradeDto> grades)
    {
        var user = GetCurrentUser();

        if (user != null && await repository.PostGradesAsync(user.UserId, grades))
            return Ok(new StringDto("A Jegyek mentése sikeres volt!"));
        return BadRequest(new StringDto("Hiba! Sikertelen mentés!"));
    }

    [HttpGet("admonitory")]
    public async Task<IActionResult> GetAdmonitoryAsync()
    {
        var user = GetCurrentUser();
        
        if (user != null)
           return Ok(await repository.GetAdmonitoriesAsync(user.UserId));
        return BadRequest();
    }

    [HttpGet("propitious")]
    public async Task<IActionResult> GetPropitiousesAsync()
    {
        var user = GetCurrentUser();
        
        if (user != null)
           return Ok(await repository.GetPropitiousesAsync(user.UserId));
        return BadRequest();
    }

    // Osztályom adatai, hetessel
    [HttpGet("class")]
    public async Task<IActionResult> GetClassAsync() 
    {
        var user = GetCurrentUser();
        
        if (user != null)
            return Ok(await repository.GetClassAsync(user.UserId));
        return BadRequest();
    }

     [HttpPost("class")]
    public async Task<IActionResult> PostClassAsync(ClassDto myClass)
    {
        var user = GetCurrentUser();

        if (user != null && await repository.PostClassAsync(user.UserId, myClass))
            return Ok(new StringDto("A hetesek frissítése sikeres volt!"));
        return BadRequest(new StringDto("Hiba! Sikertelen mentés!"));
    }
    
    [HttpGet("subjects/{groupid}")]
    public async Task<IActionResult> GetSubjectsAsync([FromRoute]int groupid) 
    {
        var user = GetCurrentUser();
        
        if (user != null)
            return Ok(await repository.GetSubjectsAsync(user.UserId, groupid));
        return BadRequest();
    }
    
    [HttpGet("sumgrades/{dividendid}")]
    public async Task<IActionResult> GetGradesSumAsync([FromRoute]int dividendid) 
    {
        var user = GetCurrentUser();
        
        if (user != null)
            return Ok(await repository.GetGradesSumAsync(user.UserId, dividendid));
        return BadRequest();
    }

    [HttpGet("grades")]
    public async Task<IActionResult> GetStudentSubjectGradesAsync([FromQuery]TeacherGetGradeDto grade)
    {
        var user = GetCurrentUser();
        
        if (user != null)
           return Ok(await repository.GetSubjectGradesAsync(user.UserId, grade));
        return BadRequest();
    }
}