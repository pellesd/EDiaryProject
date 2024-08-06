namespace enaplo.Dtos;
public class PostMessageDto
{
    public int teacherid { get; set;}
    public string message { get; set;}

    public PostMessageDto() 
    {
        message = "";
    }

    public PostMessageDto(int _teacherid, string _message)
    {
        teacherid = teacherid;
        message = _message;
    }
}