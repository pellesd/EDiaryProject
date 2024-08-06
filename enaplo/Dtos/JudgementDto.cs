namespace enaplo.Dtos;
public class JudgementDto
{
    public string Teacher { get; set; }
    public string Student { get; set; }
    public DateTime? Date { get; set; }
    public string Type { get; set; }
    public string Text { get; set; }

    public JudgementDto(string teacher, string student, DateTime? date, string type, string text)
    {
        Teacher = teacher;
        Student = student;
        Date = date;
        Type = type;
        Text = text;
    }
}