using enaplo.Dtos;

namespace enaplo.Repositories;
public interface IStudentRepository
{
    Task<List<AbsenceDto>> GetAbsencesAsync(int studentId);
    Task<List<JudgementDto>> GetAdmonitoriesAsync(int studentId);
    Task<ClassDto?> GetClassAsync(int studentId);
    Task<List<ExamDto>> GetExamsAsync(int studentId);
    Task<List<GradeDto>> GetSubjectGradesAsync(int studentId, GetGradeDto grade);
    Task<List<string>> GetGroupMembersAsync(int studentId, string groupName);
    Task<List<LateDto>> GetLatesAsync(int studentId);
    Task<List<MessageDto>> GetMessagesAsync(int studentId);
    Task<List<JudgementDto>> GetPropitiousesAsync(int studentId);
    Task<List<GradeSumDto>> GetGradesSumAsync(int studentId);
    Task<List<TimetableDto>> GetTimetableAsync(int studentId, DateTime date);
    Task<string?> GetNameAsync(int userId);
}