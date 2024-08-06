namespace enaplo.Dtos;
public class PreTeacherMessageDto
{
    public int Id { get; set; }
    public DateTime Date { get; set; }
    public string Teacher { get; set; }
    public string? Message { get; set; }
    public bool Seen { get; set; }

    public PreTeacherMessageDto(int id, DateTime date, string teacher, string? message, bool seen)
    {
        Id = id;
        Date = date;
        Teacher = teacher;
        Message = message;
        Seen = seen;
    }
}