namespace enaplo.Dtos;
public class TeacherGetGradeDto
{
    
    public string semester { get; set; } = null!;
    public string subject { get; set; } = null!;
    public int studentid { get; set; }
}