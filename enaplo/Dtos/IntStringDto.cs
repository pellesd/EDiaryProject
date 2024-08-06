namespace enaplo.Dtos;
public class IntStringDto
{
    public int Int { get; set;}
    public string String { get; set;}

    public IntStringDto() 
    {
        String = "";
    }

    public IntStringDto(int integer, string str)
    {
        Int = integer;
        String = str;
    }
}