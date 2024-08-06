namespace enaplo.Dtos;
public class NewJudgementDto
{
    public int StudentId { get; set; }
    public string Text { get; set; } = null!;
    public int LevelId { get; set; }
}