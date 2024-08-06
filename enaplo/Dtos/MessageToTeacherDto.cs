namespace enaplo.Dtos;
public class MessageToTeacherDto 
{
    public int groupid { get; set; }
    public string text { get; set; } = null!;
    public DateTime valid { get; set; }
}