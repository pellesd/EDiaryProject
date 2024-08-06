using enaplo.Dal;
using enaplo.Dtos;
using Microsoft.EntityFrameworkCore;

namespace enaplo.Repositories;
public class StudentRepository : IStudentRepository
{
    private readonly ENAPLOContext context;
    public StudentRepository(ENAPLOContext _context)
    {
        context = _context;
    }

    // Segédfüggvény: visszaadja a diák csoportjait
    async Task<List<int>> GetGroupsAsync(int studentId)
    {
        return await context.GroupMembers
        .Where(c => c.StudentId == studentId)
        .Select(c => c.GroupId)
        .ToListAsync();
    }

    public async Task<List<AbsenceDto>> GetAbsencesAsync(int studentId)
    {
        var absences = await context.Absences
        .Where(p => p.StudentId == studentId)
        .Include(p => p.LessonNavigation)
        .Include(p => p.LessonNavigation.SubjectNavigation)
        .ToListAsync();

        var result = absences
            .Select(p => new AbsenceDto(
                p.Date,
                p.LessonNavigation.Day,
                p.LessonNavigation.NumberOfLesson,
                p.LessonNavigation.SubjectNavigation.Name,
                p.NotPending,
                p.SchoolInterest, //iskolai erdekbol
                (p.NotPending == true && p.SchoolInterest == false && p.Unauthorized == false) ? true : false, //ha eldöntött és nem iglan/iskolai, akkor sima igazolt
                p.Unauthorized
        ))
        .OrderByDescending(p => p.Date).ThenBy(n => n.NumberOfLesson)
        .ToList();

        return result;
    }

    public async Task<List<JudgementDto>> GetAdmonitoriesAsync(int studentId)
    {
        var admonitories = await context.Admonitories
        .Where(p => p.StudentId == studentId)
        .Include(p => p.LevelNavigation)
        .Include(p => p.TeacherNavigation)
        .Include(p => p.StudentNavigation)
        .ToListAsync();

        var result = admonitories
            .Select(p => new JudgementDto(
                p.TeacherNavigation.Name,
                p.StudentNavigation.Name,
                p.Dated,
                p.LevelNavigation.Name,
                p.Text
            ))
            .OrderBy(p => p.Date)
            .ToList();

        return result;
    }

    public async Task<ClassDto?> GetClassAsync(int studentId)
    {
        var classes = await context.Students
            .Where(t => t.Id == studentId)
            .Include(t => t.ClassNavigation)
            .Include(o => o.ClassNavigation.HeadTeacherNavigation)
            .Include(o => o.ClassNavigation.SubHeadTeacherNavigation)
            .Select(t => t.ClassNavigation)
            .ToListAsync();

        Task<ClassDto>? resultTask = classes
            .Select(async o => new ClassDto(
                        o.Name,
                        null,
                        o.HeadTeacherNavigation == null ? null : o.HeadTeacherNavigation.Name,
                        o.SubHeadTeacherNavigation == null ? null : o.SubHeadTeacherNavigation.Name,
                        o.SevenId1,
                        await context.Students
                            .Where(x => x.Id == o.SevenId1)
                            .Select(x => x.Name)
                            .SingleOrDefaultAsync(),
                        o.SevenId2,
                        await context.Students
                            .Where(x => x.Id == o.SevenId2)
                            .Select(x => x.Name)
                            .SingleOrDefaultAsync()
            ))
            .SingleOrDefault();
        if (resultTask == null) {
            return null;
        } else {
            var result = resultTask.Result;
            return result;
        }
    }

    public async Task<List<ExamDto>> GetExamsAsync(int studentId)
    {
        var groupIds = await GetGroupsAsync(studentId);
        
        var dogaterv = await context.PlannedExams
            .Where(p => p.GroupId == null ? false : groupIds.Contains(p.GroupId.Value))
            .Where(p => p.Date > DateTime.Now)
            .ToListAsync();

        var result = dogaterv
                .Select(d => new ExamDto(
                d.Date,
                context.Users
                    .Where(p => p.Id == d.TeacherId)
                    .Select(p => p.Name)
                    .SingleOrDefault(),
                d.Text
            ))
            .OrderBy(p => p.Date)
            .ToList();

        return result;
    }

    public async Task<List<GradeSumDto>> GetGradesSumAsync(int studentId)
    {
        var gradesSum = await context.Grades
            .Where(j => j.StudentId == studentId)
            .Where(j => j.Close == false)
            .Include(j => j.PeriodNavigation)
            .Include(j => j.DividendNavigation.SubjectNavigation)
            .ToListAsync();

        var result = gradesSum
            .GroupBy(j => new {
                Semester = j.PeriodNavigation.Name,
                Subject = j.DividendNavigation.SubjectNavigation.Name})
            .Select(j => new GradeSumDto(
                j.Key.Semester,
                j.Key.Subject,
                j.Average(s => s.Value)
            ))
            .OrderBy(p => p.Semester)
            .ThenBy(p => p.Subject)
            .ToList();
        return result;
    }

    public async Task<List<string>> GetGroupMembersAsync(int studentId, string groupName)
    {
        var groups = await GetGroupsAsync(studentId);

        var groupMembers = await context.GroupMembers
            .Include(c => c.GroupNavigation)
            .Include(c => c.StudentNavigation)
            .Where(c => groups.Contains(c.GroupId))
            .Where(c => c.GroupNavigation.Name == groupName)
            .ToListAsync();

        var result = groupMembers
            .Select(o => o.StudentNavigation.Name)
            .OrderBy(q => q)
            .ToList();
        
        return result;
    }

    public async Task<List<LateDto>> GetLatesAsync(int studentId)
    {
        var lates = await context.Lates
        .Where(p => p.StudentId == studentId)
        .Include(p => p.LessonNavigation.SubjectNavigation)
        .ToListAsync();

        var result = lates
            .Select(p => new LateDto(
                p.Length,
                p.Date,
                p.LessonNavigation.Day,
                p.LessonNavigation.NumberOfLesson,
                p.LessonNavigation.SubjectNavigation.Name
            ))
            .OrderBy(p => p.Date)
            .ThenBy(n => n.NumberOfLesson)
            .ToList();
        
        return result;
    }

    public async Task<List<MessageDto>> GetMessagesAsync(int studentId)
    {
        var messages = await context.Students
        .Where(p => p.Id == studentId)
        .Select(p => p.WebMessages)
        .FirstOrDefaultAsync();

        if (messages == null) 
            return new List<MessageDto>();

        var result = messages
            .Where(x => x.ValidUntil > DateTime.Now)
            .Select(x => new MessageDto(
                x.Date,
                context.Users.Where(p => p.Id == x.SenderId).Select(p => p.Name).SingleOrDefault(),
                x.Text
            ))
            .ToList();
        
        return result;
    }

    public async Task<List<JudgementDto>> GetPropitiousesAsync(int studentId)
    {
        var propitiouses = await context.Propitiouses
        .Where(p => p.StudentId == studentId)
        .Include(p => p.LevelNavigation)
        .Include(p => p.TeacherNavigation)
        .Include(p => p.StudentNavigation)
        .ToListAsync();

        var result = propitiouses
            .Select(p => new JudgementDto(
                p.TeacherNavigation.Name,
                p.StudentNavigation.Name,
                p.Dated,
                p.LevelNavigation.Name,
                p.Text
            ))
            .OrderBy(p => p.Date)
            .ToList();

        return result;
    }

    public async Task<List<GradeDto>> GetSubjectGradesAsync(int studentId, GetGradeDto grade)
    {
        //get not close grades for specific subject
        var grades = await context.Grades
        .Where(j => j.StudentId == studentId)
        .Where(j => j.Close == false)
        .Include(j => j.TeacherNavigation)
        .Include(j => j.DividendNavigation.SubjectNavigation)
        .Include(j => j.PeriodNavigation)
        .Where(j => j.DividendNavigation.SubjectNavigation.Name == grade.subject)
        .ToListAsync();

        var result = grades
            .Where(j => j.PeriodNavigation.Name == grade.semester)
            .Select(p => new GradeDto(
                p.Value,
                p.GradeText,
                p.TeacherNavigation.Name,
                p.Dated,
                p.Text == null ? "" : p.Text,
                p.Close
        ))
        .OrderBy(p => p.Date)
        .ToList();

        return result;
    }

    // ha spéci nap van azt az órarendet, különben a simát adja vissza.
    public async Task<List<TimetableDto>> GetTimetableAsync(int studentId, DateTime date)
    {
        var groupIds = await GetGroupsAsync(studentId);

        if (groupIds.Count != 0) 
        {
            var dayString = (int)date.DayOfWeek;

            var timetableSpecial = await context
                .Timetables
                .Where(o => o.Start == date)
                .Include(o => o.DividendLessonNavigation.GroupNavigation)
                .Where(o => groupIds.Contains(o.DividendLessonNavigation.GroupNavigation.Id))
                .Include(o => o.ClassRoomNavigation)
                .Include(o => o.DividendLessonNavigation.SubjectNavigation)
                .Include(o => o.DividendLessonNavigation.TeacherNavigation)
                .ToListAsync();

            if (timetableSpecial.Count > 0) 
            {
                return timetableSpecial
                .Where(o => o.DayInt == dayString)
                .Select(t => new TimetableDto(
                    t.Id,
                    t.DividendId,
                    t.DividendLessonNavigation.SubjectNavigation.Name,
                    t.NumberOfLesson,
                    t.DividendLessonNavigation.TeacherNavigation != null ? t.DividendLessonNavigation.TeacherNavigation.Name : null,
                    t.DividendLessonNavigation.GroupId,
                    t.DividendLessonNavigation.GroupNavigation.Name,
                    t.ClassRoomNavigation != null ? t.ClassRoomNavigation.Name : null
                ))
                .OrderBy(t => t.NumberOfLesson)
                .ToList();
            } 
            else 
            {
                var timetable = await context
                .TimetableDeletes
                .Where(o => o.DayInt == dayString && o.Start <= date && o.End >= date)
                .Include(o => o.DividendNavigation.GroupNavigation)
                .Where(o => groupIds.Contains(o.DividendNavigation.GroupNavigation.Id))
                .Include(o => o.ClassRoomNavigation)
                .Include(o => o.DividendNavigation.SubjectNavigation)
                .Include(o => o.DividendNavigation.TeacherNavigation)
                .ToListAsync();

            return timetable
                .Select(t => new TimetableDto(
                    t.Id,
                    t.DividendId,
                    t.DividendNavigation.SubjectNavigation.Name,
                    t.NumberOfLesson,
                    t.DividendNavigation.TeacherNavigation != null ? t.DividendNavigation.TeacherNavigation.Name : null,
                    t.DividendNavigation.GroupId,
                    t.DividendNavigation.GroupNavigation.Name,
                    t.ClassRoomNavigation != null ? t.ClassRoomNavigation.Name : null
                ))
                .OrderBy(t => t.NumberOfLesson)
                .ToList();
            }
        }
        return new List<TimetableDto>();
    }

    public async Task<string?> GetNameAsync(int userId)
    {
        return await context.Students
            .Where(x => x.Id == userId)
            .Select(x => x.Name)
            .SingleAsync();
    }
}