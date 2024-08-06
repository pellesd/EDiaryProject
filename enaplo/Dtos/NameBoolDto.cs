namespace enaplo.Dtos;
public class MissingDto
{
    public int Int { get; set;}
    public string String { get; set;}
    public bool Bool { get; set;}

    public MissingDto() 
    {
        Int = 0;
        String = "";
        Bool = false;
    }

    public MissingDto(int _int, string _string, bool _bool)
    {
        Int = _int;
        String = _string;
        Bool = _bool;
    }
}