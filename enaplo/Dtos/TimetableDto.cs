namespace enaplo.Dtos;
public class TimetableDto
{
    public DateTime? Date { get; set; }
    public int Id { get; set; }
    public int DividendId { get; set; }
    public string Lesson { get; set; }
    public int NumberOfLesson { get; set; }
    public string? Teacher { get; set; }
    public int? GroupId { get; set; }
    public string? Group { get; set; }
    public string? Room { get; set; }

    public TimetableDto(int _id, int _dividendId, string _lesson, int _numberOfLesson,
                        string? _teacher, int? _groupId, string? _group, string? _room)
    {
        Id = _id;
        DividendId = _dividendId;
        Lesson = _lesson;
        NumberOfLesson = _numberOfLesson;
        Teacher = _teacher;
        GroupId = _groupId;
        Group = _group;
        Room = _room;
    }

    public TimetableDto(DateTime _date, int _id, int _dividendId, string _lesson, int _numberOfLesson,
                        string? _teacher, int? _groupId, string? _group, string? _room)
    {
        Date = _date;
        Id = _id;
        DividendId = _dividendId;
        Lesson = _lesson;
        NumberOfLesson = _numberOfLesson;
        Teacher = _teacher;
        GroupId = _groupId;
        Group = _group;
        Room = _room;
    }
}
