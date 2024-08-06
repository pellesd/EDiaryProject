namespace enaplo.Dtos;
public class LoginDto
{
    public string Username { get; }
    public string Password { get; }

    public LoginDto(string username, string password)
    {
        Username = username;
        Password = password;
    }
}