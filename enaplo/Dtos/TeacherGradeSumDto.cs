namespace enaplo.Dtos;
public class TeacherGradeSumDto
{
    public string Semester { get; set; }
    public int StudentId { get; set; }
    public string Student { get; set; }
    public double? Average { get; set; }

    public TeacherGradeSumDto(
        string semester, int studentId, 
        string student, double? average)
    {
        Semester = semester;
        StudentId = studentId;
        Student = student;
        Average = average;
    }
}