namespace enaplo.Dtos;
public class ClassDto
{
    public string Name { get; set; }
    public int? GroupId { get; set; }
    public string? HeadTeacher { get; set; }
    public string? SubHeadTeacher { get; set; }
    public int? SevenId1 { get; set; }
    public string? Seven1 { get; set; }
    public int? SevenId2 { get; set; }
    public string? Seven2 { get; set; }

    public ClassDto(
        string name, int? groupId, 
        string? headTeacher, string? subHeadTeacher, 
        int? sevenId1, string? seven1, 
        int? sevenId2, string? seven2)
    {
        Name = name;
        GroupId = groupId;
        HeadTeacher = headTeacher;
        SubHeadTeacher = subHeadTeacher;
        Seven1 = seven1;
        SevenId1 = sevenId1;
        Seven2 = seven2;
        SevenId2 = sevenId2;
    }
}