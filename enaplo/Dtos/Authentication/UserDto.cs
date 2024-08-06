namespace enaplo.Dtos;
public class UserDto
{
    public int UserId { get; }
    public string Role { get; }

    public UserDto(int userID, string role)
    {
        UserId = userID;
        Role = role;
    }
}