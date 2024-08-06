using enaplo.Dtos;
using enaplo.Repositories;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace enaplo.Controllers;

[ApiController]
[Route("")]
public class BasicController : ControllerBase
{
    private readonly IBasicRepository repository;

    public BasicController(IBasicRepository _repository)
    {
        repository = _repository;
    }

    [HttpGet("canteen/{date}")]
    [Authorize]
    public async Task<IActionResult> GetFoodAsync([FromRoute]DateTime? date)
    {
        if (date == null)
            date = DateTime.Today;
        return Ok(await repository.GetFoodAsync(date!.Value));
    }

    [HttpPost("canteen")]
    [Authorize(Roles = "teacher,admin")]
    public async Task<IActionResult> PostFoodAsync(FoodDto food)
    {
        var result = await repository.PostFoodAsync(food);
        if (result == null)
            return BadRequest(new StringDto("Hibás kérés!"));
        return Ok(result);
    }

    [HttpDelete("canteen/{id}")]
    [Authorize(Roles = "teacher,admin")]
    public async Task<IActionResult> DeleteFoodAsync([FromRoute] int id)
    {
        var result = await repository.DeleteFoodAsync(id);
        if (result)
            return Ok(new StringDto("Menü sikeresen törölve!"));
        else
            return Ok(new StringDto("Hibás kérés!"));
    }

    
    [AllowAnonymous]
    [HttpGet("ping")]
    public IActionResult Ping() 
    {
        return Ok(new StringDto("pong"));
    }
}