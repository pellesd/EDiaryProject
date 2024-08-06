using enaplo.Dal;
using enaplo.Dtos;
using Microsoft.EntityFrameworkCore;

namespace enaplo.Repositories;
public class BasicRepository : IBasicRepository
{
    private readonly ENAPLOContext context;
    public BasicRepository(ENAPLOContext _context)
    {
        context = _context;
    }

    public async Task<List<FoodDto>> GetFoodAsync(DateTime date)
    {
        return await context.Canteens
        .Where(p => p.Date == date)
        .Select(p => new FoodDto(
            p.Id,
            p.Date,
            p.FirstMeal,
            p.SecondMeal,
            p.Extra
        ))
        .ToListAsync();
    }

    public async Task<FoodDto?> PostFoodAsync(FoodDto food)
    {
        if (food.Id == null) // ha új étel hozzáadjuk
        {
            var newFood = new Canteen{
                Date = food.Date,
                FirstMeal = food.FirstMeal,
                SecondMeal = food.SecondMeal,
                Extra = food.Extra
            };

            var newFoodDb = context.Canteens.Add(newFood).Entity;
            
            await context.SaveChangesAsync();
            
            food.Id = newFoodDb.Id;
            return food;
        } else { // már létezett az étel, csak frissítjük
            var foodDbTask = context.Canteens
                .Where(x => x.Id == food.Id)
                .SingleOrDefaultAsync();

            await foodDbTask;

            var foodDb = foodDbTask.Result;
            if (foodDb == null)
                return null; // nem találtuk meg az adatbázisban, hibás kérés
            // értékek frissítése
            foodDb.FirstMeal = food.FirstMeal;
            foodDb.SecondMeal = food.SecondMeal;
            foodDb.Extra = food.Extra;

            await context.SaveChangesAsync();

            return food;
        }
    }

    public async Task<bool> DeleteFoodAsync(int id)
    {
        Task<Canteen?> foodDbTask = context.Canteens
            .Where(x => x.Id == id)
            .SingleOrDefaultAsync();

        await foodDbTask;

        var foodDb = foodDbTask.Result;
        if (foodDb == null)
            return false; // nem találtuk meg az adatbázisban, akkor hát nincs benne

        // töröljük
        context.Canteens.Remove(foodDb);
        await context.SaveChangesAsync();

        return true;
    }
}