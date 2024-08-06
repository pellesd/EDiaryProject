using enaplo.Dtos;

namespace enaplo.Repositories;
public interface ITeacherRepository
{
    Task<List<TimetableDto>> GetTimetableAsync(int teacherId, DateTime date);
    Task<List<PreTeacherMessageDto>> GetMessagesSentToTeacherAsync(int teacherId);
    Task<List<ToGroupsMessageDto>> GetMessagesSentToGroupAsync(int teacherId);
    Task<MessageTeacherDto?> GetMessageFromTeacherAsync(int teacherId, int messageId);
    Task<List<IntStringDto>> GetOtherTeachersAsync(int myId);
    Task PostSendMessageToTeacherAsync(int mySenderId, PostMessageDto message);
    Task<List<PreTeacherMessageDto>> GetMessagesFromTeacherAsync(int teacherId);
    Task<List<IntStringDto>> GetTaughtGroupsAsync(int teacherId);
    Task PostSendMessageToGroupAsync(int teacherId, MessageToTeacherDto message);
    Task<List<IntStringDto>> GetPropitiousTypesAsync();
    Task<List<IntStringDto>> GetAdmonitoryTypesAsync();
    Task<List<IntStringDto>?> GetGroupMembersAsync(int groupId);
    Task PostProptiousToStudentAsync(int teacherId, NewJudgementDto judgement);
    Task PostAdmonitoryToStudentAsync(int teacherId, NewJudgementDto judgement);
    Task<RegisterLessonDto?> GetRegisterLessonAsync(int taughtId, RegisterLessonKeysDto keys);
    Task<bool> PostRegisterLessonAsync(PostRegisterLessonDto lesson);
    Task<List<string>> GetGroupMembersByMessageAsync(int userId, int messageId);
    Task<List<MissingDto>> GetMissingStudentsAsync(int teacherId, int lessonId, int dividendId);
    Task PostMissingStudentsAsync(int teacherId, PostMissingDto postMissingDto);
    Task<List<GetLateDto>> GetLateAsync(int teacherId, int lessonId, int dividendId);
    Task PostLateAsync(int teacherId, PostLateDto postLateDto);
    Task<string> GetNameAsync(int userId);
    Task<List<GradeTypeDto>> GetGradeTypesAsync();
    Task<bool> PostGradeAsync(int userId, PostGradeDto grade);
    Task<bool> PostGradesAsync(int userId, List<PostGradeDto> grades);
    Task<List<JudgementDto>> GetAdmonitoriesAsync(int userId);
    Task<List<JudgementDto>> GetPropitiousesAsync(int userId);
    Task<ClassDto?> GetClassAsync(int userId);
    Task<bool> PostClassAsync(int userId, ClassDto myClass);
    Task<List<IntStringDto>> GetSubjectsAsync(int userId, int groupId);
    Task<List<TeacherGradeSumDto>> GetGradesSumAsync(int userId, int dividendId);
    Task<List<GradeDto>> GetSubjectGradesAsync(int studentId, TeacherGetGradeDto grade);
}