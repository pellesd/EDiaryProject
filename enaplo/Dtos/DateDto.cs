namespace enaplo.Dtos;
public class DateDto
{
    public DateTime date { get; set; }

    public DateDto() 
    {
        date = DateTime.Today;
    }
    public DateDto(DateTime _date)
    {
        date = _date;
    }
}