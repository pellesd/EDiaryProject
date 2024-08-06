namespace enaplo.Dtos;
public class LateDto
{
    public int Minute { get; set; }
    public DateTime Date { get; set; }
    public string Day { get; set; }
    public short NumberOfLesson { get; set; }
    public string Subject { get; set; }

    public LateDto(int minute, DateTime date, string day, short numberOfLesson, string subject)
    {
        Minute = minute;
        Date = date;
        Day = day;
        NumberOfLesson = numberOfLesson;
        Subject = subject;
    }
}