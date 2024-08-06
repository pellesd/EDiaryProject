namespace enaplo.Dtos;
public class ToGroupsMessageDto
{
    public int Id { get; set; }
    public DateTime Date { get; set; }
    public string? Message { get; set; }
    public DateTime ValidUntil { get; set; }

    public ToGroupsMessageDto(
        int id, 
        DateTime date, 
        string? message, 
        DateTime valid)
    {
        Id = id;
        Date = date;
        Message = message;
        ValidUntil = valid;
    }
}