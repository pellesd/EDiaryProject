namespace enaplo.Dtos;

public class PostLateDto {

    public int LessonId { get; set; }
    public int DividendId { get; set; }
    public DateTime Date { get; set; }
    public List<GetLateDto> Lates { get; set; }

    public PostLateDto() {
        Lates = new List<GetLateDto>();
    }

    public PostLateDto(
        int lessonId, int dividendId, 
        DateTime date, List<GetLateDto> lates) 
    {
        LessonId = lessonId;
        DividendId = dividendId;
        Date = date;
        Lates = lates;
    }
}