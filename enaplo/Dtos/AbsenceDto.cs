namespace enaplo.Dtos;
public class AbsenceDto
{
    public DateTime Date { get; set; }
    public string Day { get; set; }
    public short NumberOfLesson { get; set; }
    public string Subject { get; set; }
    public bool NotPending { get; set; }
    public bool SchoolInterest { get; set; }
    public bool NormalAuthorizedAbsence { get; set; }
    public bool UnauthorizedAbsence { get; set; }

    public AbsenceDto(
        DateTime date, string day, short numberOfLesson, 
        string subject, bool notPending, bool schoolIntrest, 
        bool normalAuthorizedAbsence, bool unauthorizedAbsence)
    {
        Date = date;
        Day = day;
        NumberOfLesson = numberOfLesson;
        Subject = subject;
        NotPending = notPending;
        SchoolInterest = schoolIntrest;
        NormalAuthorizedAbsence = normalAuthorizedAbsence;
        UnauthorizedAbsence = unauthorizedAbsence;
    }
}