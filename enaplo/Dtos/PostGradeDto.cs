public class PostGradeDto {
    public int DividendId { get; set; }
    public short Grade { get; set; }
    public string GradeString { get; set; } = null!;
    public int StudentId { get; set; }
    public DateTime Date { get; set; }
    public string? Text { get; set;} = null!;
    public int Multiplier { get; set; }
}