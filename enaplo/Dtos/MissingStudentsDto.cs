namespace enaplo.Dtos;

public class MissingStudentsDto
{
    public int LessonId { get; set; }
    public int DividendId { get; set; }
    public DateTime Date { get; set; }
    public List<int> StudentIds { get; set; } = null!;
}