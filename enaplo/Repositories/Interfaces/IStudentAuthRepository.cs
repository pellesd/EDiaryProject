using enaplo.Dtos;

namespace enaplo.Repositories;
public interface IStudentAuthRepository
{
    Task<string?> LoginAsync(LoginDto user);
}