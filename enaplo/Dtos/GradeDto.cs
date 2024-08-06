namespace enaplo.Dtos;
public class GradeDto
{
    public short? Grade { get; set; } = null!;
    public string? GradeString { get; set; } = null!;
    public string Teacher { get; set; } = null!;
    public DateTime? Date { get; set; }
    public string Text { get; set; } = null!;
    public bool Closed { get; set; }

    public GradeDto(
        short? grade, string? gradeString, string teacher, 
        DateTime? date, string text, bool closed)
    {
        Grade = grade;
        GradeString = gradeString;
        Teacher = teacher;
        Date = date;
        Text = text;
        Closed = closed;
    }
}
