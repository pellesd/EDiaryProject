namespace enaplo.Dtos;
public class MessageTeacherDto 
{
    public DateTime Date { get; set; }
    public string? From { get; set; }
    public string? To { get; set; }
    public string? Message { get; set; }

    public MessageTeacherDto(DateTime date, string? from, string? to, string? message)
    {
        Date = date;
        From = from;
        To = to;
        Message = message;
    }
}