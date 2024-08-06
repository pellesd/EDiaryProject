namespace enaplo.Dtos;
public class FoodDto
{
    public int? Id { get; set; }
    public DateTime Date { get; set; }
    public string FirstMeal { get; set; }
    public string? SecondMeal { get; set; }
    public string? Extra { get; set; }

    public FoodDto(
        int? id,
        DateTime date, 
        string firstMeal, 
        string? secondMeal, 
        string? extra)
    {
        Id = id;
        Date = date;
        FirstMeal = firstMeal;
        SecondMeal = secondMeal;
        Extra = extra;
    }
}