namespace enaplo.Dtos;
public class MessageDto
{
    public DateTime Date { get; set; }
    public string? Sender { get; set; }
    public string? Message { get; set; }

    public MessageDto(DateTime date, string? sender, string? message)
    {
        Date = date;
        Sender = sender;
        Message = message;
    }
}