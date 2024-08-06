using enaplo.Dtos;

namespace enaplo.Repositories;
public interface ITeacherAuthRepository
{
    Task<string?> LoginAsync(LoginDto user);
    void setAllTeacherPassword(string newPw);
}