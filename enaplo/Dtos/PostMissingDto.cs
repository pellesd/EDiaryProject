namespace enaplo.Dtos;

public class PostMissingDto {

    public int LessonId { get; set; }
    public int DividendId { get; set; }
    public DateTime Date { get; set; }
    public List<MissingDto> Absences { get; set; } = null!;
}