namespace enaplo.Dtos;
public class GradeSumDto
{
    public string Semester { get; set; }
    public string Subject { get; set; }
    public double? Average { get; set; }

    public GradeSumDto(string semester, string subject, double? average)
    {
        Semester = semester;
        Subject = subject;
        Average = average;
    }
}