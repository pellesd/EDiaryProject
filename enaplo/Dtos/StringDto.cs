namespace enaplo.Dtos;
public class StringDto
{
    public string Value { get; set; }

    public StringDto() {
        Value = "";
    }
    public StringDto(string value)
    {
        Value = value;
    }
}