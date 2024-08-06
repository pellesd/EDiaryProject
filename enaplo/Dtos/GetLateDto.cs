namespace enaplo.Dtos;
public class GetLateDto
{
    public int Int { get; set;}
    public string String { get; set;}
    public int? Len { get; set;}

    public GetLateDto() 
    {
        Int = 0;
        String = "";
        Len = null;
    }

    public GetLateDto(int _int, string _string, int? _len)
    {
        Int = _int;
        String = _string;
        Len = _len;
    }
}