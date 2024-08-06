using enaplo.Dtos;

namespace enaplo.Repositories;
public interface IBasicRepository
{
    Task<List<FoodDto>> GetFoodAsync(DateTime date);
    Task<bool> DeleteFoodAsync(int id);
    Task<FoodDto?> PostFoodAsync(FoodDto food);
}