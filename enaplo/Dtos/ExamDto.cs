namespace enaplo.Dtos;
public class ExamDto
{
    public DateTime? Date { get; set; }
    public string? Teacher { get; set; }
    public string? Text { get; set; }

    public ExamDto(DateTime? date, string? teacher, string? text)
    {
        Date = date;
        Teacher = teacher;
        Text = text;
    }
}