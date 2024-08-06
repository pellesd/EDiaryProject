using enaplo.Dal;
using enaplo.Dtos;
using enaplo.Repositories;
using Microsoft.EntityFrameworkCore;

public class TeacherRepository : ITeacherRepository
{
    private readonly ENAPLOContext context;
    public TeacherRepository(ENAPLOContext _context)
    {
        context = _context;
    }

    public async Task<MessageTeacherDto?> GetMessageFromTeacherAsync(int teacherId, int messageId)
    {
        var message = await context.MessageTeachers
                .Where(u => u.Id == messageId)
                .Where(u => u.ReceiverId == teacherId || u.SenderId == teacherId)
                .SingleOrDefaultAsync();
        
        if (message != null) { // csak akkor legyen megtekintve, ha az kérte le, akinek szólt, nem a küldő.
            if (message.Seen == null && message.ReceiverId == teacherId) 
            {
                message.Seen = DateTime.Now;
                await context.SaveChangesAsync();
            }
             return new MessageTeacherDto(
                    message.Date,
                    await context.Users.Where(u => u.Id == message.SenderId).Select(u => u.Name).SingleOrDefaultAsync(),
                    await context.Users.Where(u => u.Id == message.ReceiverId).Select(u => u.Name).SingleOrDefaultAsync(),
                    message.Text
                );
        }
        return null;
    }

    public async Task<List<PreTeacherMessageDto>> GetMessagesFromTeacherAsync(int teacherId)
    {
        return (await context.MessageTeachers
                .Where(u => u.ReceiverId == teacherId)
                .Include(u => u.SenderNavigation)
                .ToListAsync())
                .Select(x => new PreTeacherMessageDto(
                    x.Id,
                    x.Date,
                    x.SenderNavigation.Name,
                    x.Text != null && x.Text.ToString().Length >= 13 // elso max 13 karakter
                                ? (x.Text.Substring(0, 13) + "...")
                                : x.Text,
                    x.Seen == null ? false : true
                ))
                .OrderBy(x => x.Seen == false ? 0 : 1)
                .ThenByDescending(x => x.Date)
                .ToList();
    }

    public async Task<List<PreTeacherMessageDto>> GetMessagesSentToTeacherAsync(int teacherId)
    {
        return (await context.MessageTeachers
                .Where(u => u.SenderId == teacherId)
                .Include(u => u.ReceiverNavigation)
                .ToListAsync())
                .Select(x => new PreTeacherMessageDto(
                    x.Id,
                    x.Date,
                    x.ReceiverNavigation.Name,
                    (x.Text != null && x.Text.ToString().Length >= 13) // elso max 13 karakter
                                                    ? (x.Text.Substring(0, 13) + "...")
                                                    : x.Text,
                    x.Seen == null ? false : true
                ))
                .OrderByDescending(x => x.Date)
                .ToList();
    }

    public async Task<List<ToGroupsMessageDto>> GetMessagesSentToGroupAsync(int teacherId)
    {
        return (await context.MessageGroups
                .Where(u => u.SenderId == teacherId)
                .Select(u => new ToGroupsMessageDto(
                    u.Id,
                    u.Date,
                    u.Text,
                    u.ValidUntil
                ))
                .ToListAsync())
                .OrderByDescending(x => x.Date)
                .ToList();
        
    }

    public async Task<List<IntStringDto>> GetOtherTeachersAsync(int myId)
    {
        return await context
                .Users
                .Where(u => u.Id != myId)
                .Select(u => new IntStringDto(
                    u.Id,
                    u.Name))
                .ToListAsync();
    }

    public async Task<List<IntStringDto>> GetTaughtGroupsAsync(int teacherId)
    {
        var group = await context
            .DividendLessons
            .Where(u => u.TeacherId == teacherId)
            .Include(u => u.GroupNavigation)
            .ToListAsync();

        return group
            .Select(u => new IntStringDto(
                u.GroupId,
                u.GroupNavigation.Name!))
            .GroupBy (u => u.Int.ToString() + u.String )
            .Select (u => u.First())
            .OrderBy(u => u.String)
            .ToList();
    }

    // ha spéci nap van azt az órarendet, különben a simát adja vissza.
    public async Task<List<TimetableDto>> GetTimetableAsync(int teacherId, DateTime date)
    {
        var dayInt = (int)date.DayOfWeek;

        var timetableSpecial = await context
            .Timetables
            .Where(o => o.Start == date)
            .Include(o => o.DividendLessonNavigation)
            .Where(o => o.DividendLessonNavigation.TeacherId == teacherId)
            .Include(o => o.ClassRoomNavigation)
            .Include(o => o.DividendLessonNavigation.SubjectNavigation)
            .Include(o => o.DividendLessonNavigation.TeacherNavigation)
            .Include(o => o.DividendLessonNavigation.GroupNavigation)
            .ToListAsync();

        if (timetableSpecial.Count > 0) 
        {
            return timetableSpecial
            .Where(o => o.DayInt == dayInt)
            .Select(t => new TimetableDto(
                date,
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
                .Where(o => o.DayInt == dayInt && o.Start <= date && o.End >= date)
                .Include(o => o.DividendNavigation)
                .Where(o => o.DividendNavigation.TeacherId == teacherId)
                .Include(o => o.ClassRoomNavigation)
                .Include(o => o.DividendNavigation.SubjectNavigation)
                .Include(o => o.DividendNavigation.TeacherNavigation)
                .Include(o => o.DividendNavigation.GroupNavigation)
                .ToListAsync();

        return timetable
            .Select(t => new TimetableDto(
                date,
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

    public async Task<List<IntStringDto>?> GetGroupMembersAsync(int groupId)
    {
        
        var students = await context
            .GroupMembers
            .Where(g => g.GroupId == groupId)
            .Include(g => g.StudentNavigation)
            .ToListAsync();
        
        return students
            .Select(g => new IntStringDto(
                g.StudentId,
                g.StudentNavigation.Name
            ))
            .OrderBy(g => g.String)
            .ToList();
    }

    public async Task<List<GradeTypeDto>> GetGradeTypesAsync() {
        return await context.GradeTypes
                .Select(g => new GradeTypeDto{
                    Id = g.Id,
                    ShortName = g.ShortName,
                    Name = g.Name
                })
                .ToListAsync();
    }

    public async Task PostSendMessageToGroupAsync(int teacherId, MessageToTeacherDto message)
    {
        var newMessage = new MessageGroup{
            SenderId = teacherId,
            Date = DateTime.Now,
            ValidUntil = message.valid,
            Text = message.text,
            Students = (context
                    .GroupMembers
                    .Where(c => c.GroupId == message.groupid)
                    .Include(c => c.StudentNavigation))
                    .ToList()
                    .Select(c => c.StudentNavigation)
                    .ToList()
        };
        
        context.MessageGroups.Add(newMessage);
        await context.SaveChangesAsync();
    }

    public async Task PostSendMessageToTeacherAsync(int mySenderId, PostMessageDto message)
    {
        var newMessage = new MessageTeacher{
            SenderId = mySenderId,
            ReceiverId = message.teacherid,
            Date = DateTime.Now,
            Seen = null,
            Text = message.message
        };
        
        context.MessageTeachers.Add(newMessage);
        await context.SaveChangesAsync();
    }

    // dicseretTipusok
    public async Task<List<IntStringDto>> GetPropitiousTypesAsync()
    {
        return await context.PropitiousTypes
            .Select(d => new IntStringDto(
                d.Id,
                d.Name
            ))
            .ToListAsync();
    }

    // figyelmeztetesTipusok
    public async Task<List<IntStringDto>> GetAdmonitoryTypesAsync()
    {
        return await context.AdmonitoryTypes
            .Select(d => new IntStringDto(
                d.Id,
                d.Name
            ))
            .ToListAsync();
    }

    public async Task PostProptiousToStudentAsync(int teacherId, NewJudgementDto judgement)
    {
        var promptious = new Propitious{
        StudentId = judgement.StudentId,
        TeacherId = teacherId,
        Dated = DateTime.Now,
        Text = judgement.Text,
        LevelId = judgement.LevelId
        };
        
        context.Propitiouses.Add(promptious);
        await context.SaveChangesAsync();
    }

    public async Task PostAdmonitoryToStudentAsync(int teacherId, NewJudgementDto judgement)
    {
        var admonitory = new Admonitory{
        StudentId = judgement.StudentId,
        TeacherId = teacherId,
        Dated = DateTime.Now,
        Text = judgement.Text,
        LevelId = judgement.LevelId
        };
        
        context.Admonitories.Add(admonitory);
        await context.SaveChangesAsync();
    }

    public async Task<RegisterLessonDto?> GetRegisterLessonAsync(int teacherId, RegisterLessonKeysDto keys) 
    {
        var dividendLesson = await context
            .DividendLessons
            .Where(t => t.Id == keys.dividendid)
            .SingleOrDefaultAsync();
        if (dividendLesson == null || dividendLesson.TeacherId != teacherId)
            return null;

        var dayInt = (int)keys.date.DayOfWeek;

        var lesson = await context
            .Timetables
            .Where(o => o.DividendId == keys.dividendid 
                        && o.Start == keys.date 
                        && o.NumberOfLesson == keys.numberoflesson 
                        && o.DayInt == dayInt)
            .Select(o => new IntStringDto(
                o.Id,
                o.Day
            ))
            .SingleOrDefaultAsync();

        if (lesson == null) 
        {
            lesson = await context
            .TimetableDeletes
            .Where(o => o.DividendId == keys.dividendid 
                        && o.Start <= keys.date 
                        && o.End >= keys.date
                        && o.NumberOfLesson == keys.numberoflesson 
                        && o.DayInt == dayInt)
            .Select(o => new IntStringDto(
                o.Id,
                o.Day
            ))
            .SingleOrDefaultAsync();
        } 
        
        var register = await context
            .RegisterLessons
            .Where(t => t.Date == keys.date
                        && t.TeacherId == dividendLesson.TeacherId
                        && t.NumberOfLesson == keys.numberoflesson)
            .SingleOrDefaultAsync();
        
        if (register == null) 
        {
            register = new RegisterLesson{
                Day = lesson!.String!,
                Date = keys.date,
                DividendId = keys.dividendid,
                TeacherId = (int)dividendLesson!.TeacherId!,
                GroupId = (int)dividendLesson!.GroupId!,
                SubjectId = (int)dividendLesson!.SubjectId!,
                Exercise = false,
                Substitution = false,
                SubstitutionType = "Norm l",
                ShouldCount = false,
                KeptTogether = false,
                SubstituteGraded = false,
                Deleted = false,
                Dated = DateTime.Now,
                ShouldGrade = true,
                DeletedExercise = false,
                Payed = true,
                LessonPaymentMultiplier = 1,
                LessonInfoId = 1,
                PlussClass = false
            };

            context.RegisterLessons.Add(register);
            
            await context.SaveChangesAsync();
        }

        var newRegister = await context.RegisterLessons
                .Include(x => x.TeacherNavigation)
                .Include(x => x.GroupNavigation)
                .Include(x => x.SubjectNavigation)
                .Include(x => x.LessonDescriptionNavigation)
                .Where(t => t.Date == keys.date
                        && t.TeacherId == dividendLesson.TeacherId
                        && t.NumberOfLesson == keys.numberoflesson)
                .SingleOrDefaultAsync();

        return new RegisterLessonDto{
                RegisterLessonId = newRegister!.Id,
                LessonId = lesson!.Int,
                Day = newRegister!.Day,
                NumberOfLesson = newRegister!.NumberOfLesson,
                Date = newRegister!.Date!.Value,
                TeacherId = newRegister!.TeacherId,
                Teacher = newRegister!.TeacherNavigation?.Name,
                GroupId = newRegister!.GroupId,
                Group = newRegister?.GroupNavigation?.Name,
                DividendId = newRegister!.DividendId,
                SubjectId = newRegister!.SubjectId,
                Subject = newRegister!.SubjectNavigation?.Name,
                LessonDescriptionId = newRegister!.LessonDescriptionId,
                LessonDescription = newRegister!.LessonDescriptionNavigation?.Title,
                Deleted = newRegister!.Deleted,
                Dated = newRegister!.Dated,
                ShouldGrade = newRegister!.ShouldGrade
            };
    }

    public async Task<bool> PostRegisterLessonAsync(PostRegisterLessonDto lesson) 
    {
        var dayInt = (int)lesson.Date.DayOfWeek;

        var registerDb = await context
            .RegisterLessons
            .Include(t => t.LessonDescriptionNavigation)
            .Where(t => t.Date == lesson.Date
                        && t.TeacherId == lesson.TeacherId
                        && t.NumberOfLesson == lesson.NumberOfLesson)
            .SingleOrDefaultAsync();

        if (registerDb == null)
            return false;
        
        if (lesson.LessonDescription != null) {
            if (registerDb.LessonDescriptionId == null) {
                LessonDescription lessonDescription = context.LessonDetails.Add(
                    new LessonDescription {Title = lesson.LessonDescription}
                ).Entity;
                await context.SaveChangesAsync();
                registerDb.LessonDescriptionId = lessonDescription.Id;
                await context.SaveChangesAsync();
            } else if (registerDb.LessonDescriptionNavigation != null) 
                registerDb.LessonDescriptionNavigation.Title = lesson.LessonDescription;
        }

        registerDb.UpdateByRegisterLessonDto(lesson);
                await context.SaveChangesAsync();

        return true;
    }

    public async Task<List<string>> GetGroupMembersByMessageAsync(int userId, int messageId)
    {
        var result = await context.MessageGroups
            .Where(x => x.Id == messageId)
            .Select(x => x.Students
                            .Select(t => t.Name)
                            .OrderBy(t => t)
                            .ToList())
            .FirstOrDefaultAsync();
        if (result == null)
            return new List<string>();
        return result;
    }

    public async Task<List<MissingDto>> GetMissingStudentsAsync(
        int teacherId, int lessonId, int dividendId) 
    {
        var missing = await context
            .Absences
            .Where(h => h.NumberOfLesson == lessonId)
            .Select(h => h.StudentId)
            .ToListAsync();

        var result = await context
            .GroupMembers
            .Include(t => t.StudentNavigation)
            .Where(c => context
                            .DividendLessons
                            .Where(t => t.Id == dividendId)
                            .Select(t => t.GroupId)
                            .ToList().Contains(c.GroupId))
            .Select(c => new MissingDto(
                c.StudentId,
                c.StudentNavigation.Name,
                false
            ))
            .ToListAsync();

        foreach(var item in result) 
        {
            if (missing.Contains(item.Int))
                item.Bool = true;
        }
        return result;
    }

    public async Task PostMissingStudentsAsync(int teacherId, PostMissingDto postMissingDto) {
        var registredMissing = await context
            .Absences
            .Where(h => h.NumberOfLesson == postMissingDto.LessonId)
            .Select(h => h.StudentId)
            .ToListAsync();

        foreach(var item in postMissingDto.Absences) {
            // ha az adott diák még nincs regisztálva hiányzónak, de az
            if (item.Bool && !registredMissing.Contains(item.Int)) {
                var missing = new Absence {
                    StudentId = item.Int,
                    DividendId = postMissingDto.DividendId,
                    Date = postMissingDto.Date,
                    NumberOfLesson = postMissingDto.LessonId,
                    Exercise = false,
                    TeacherId = teacherId,
                    Dated = DateTime.Now,
                    NotPending = false,
                    Unauthorized = false,
                    SchoolInterest = false,
                    DeletedExercise = false,
                    Periodical = false,
                    Automata = false
                };
                context.Absences.Add(missing);
                await context.SaveChangesAsync();
            } else if (!item.Bool && registredMissing.Contains(item.Int)) {
                var missing = await context
                    .Absences
                    .Where(h => h.NumberOfLesson == postMissingDto.LessonId)
                    .Where(h => h.StudentId == item.Int)
                    .SingleOrDefaultAsync();
                if (missing != null)
                    context.Absences.Remove(missing);
                    await context.SaveChangesAsync();
            }
        }
        await context.SaveChangesAsync();
    }

    public async Task<List<GetLateDto>> GetLateAsync(int teacherId, int lessonId, int dividendId)
    {
        var lates = await context
            .Lates
            .Where(h => h.Lesson == lessonId)
            .Select(h => new IdLength{ 
                Id = h.StudentId,
                Length = h.Length
                })
            .ToListAsync();

        var result = await context
            .GroupMembers
            .Include(t => t.StudentNavigation)
            .Where(c => context
                            .DividendLessons
                            .Where(t => t.Id == dividendId)
                            .Select(t => t.GroupId)
                            .ToList().Contains(c.GroupId))
            .Select(c => new GetLateDto(
                c.StudentId,
                c.StudentNavigation.Name,
                null
            ))
            .ToListAsync();

        foreach(var item in result) 
        {
            var late = contains(lates, item.Int);
            if (late != null)
                item.Len = late.Length;
        }
        return result;
    }

    public async Task PostLateAsync(int teacherId, PostLateDto postLateDto)
    {
        var registredLate = await context
            .Lates
            .Where(h => h.Lesson == postLateDto.LessonId)
            .Select(h => new IdLength{ 
                Id = h.StudentId,
                Length = h.Length
                })
            .ToListAsync();

        foreach(var item in postLateDto.Lates) {
            // ha az adott diák még nincs regisztálva későnek, de az
            var late = contains(registredLate, item.Int);
            if (item.Len != null && item.Len > 0 && late == null) {
                var newLate = new Late {
                    Length = item.Len.Value,
                    StudentId = item.Int,
                    DividendId = postLateDto.DividendId,
                    Lesson = postLateDto.LessonId,
                    Date = postLateDto.Date,
                    Deleted = false,
                    Counted = false
                };
                context.Lates.Add(newLate);
            // ha már regisztrálva van de mégsem az
            } else if ((item.Len == null || item.Len <= 0) && late != null) {
                var deletableLate = await context
                    .Lates
                    .Where(h => h.Lesson == postLateDto.LessonId)
                    .Where(h => h.StudentId == item.Int)
                    .SingleOrDefaultAsync();
                if (deletableLate != null)
                    context.Lates.Remove(deletableLate);
            // ha már regisztrálva van és továbbra is az, akkor frissítem az értéket
            } else if (item.Len != null && item.Len > 0 && late != null) {
                var updateableLate = await context
                    .Lates
                    .Where(h => h.Lesson == postLateDto.LessonId)
                    .Where(h => h.StudentId == item.Int)
                    .SingleOrDefaultAsync();
                if (updateableLate != null)
                    updateableLate.Length = item.Len.Value;
                    await context.SaveChangesAsync();
            }
        }
        await context.SaveChangesAsync();
    }

    private class IdLength
    {
        public int Id { get; set; }
        public int Length { get; set; }
    }

    private IdLength? contains(List<IdLength> lates, int id) { 
        foreach(var item in lates) 
        {
            if (item.Id.Equals(id))
                return item;
        }
        return null;
    }

    public async Task<string> GetNameAsync(int userId)
    {
        return await context.Users
            .Where(x => x.Id == userId)
            .Select(x => x.Name)
            .SingleAsync();
    }

    public async Task<bool> PostGradeAsync(int userId, PostGradeDto grade)
    {
        var periodId = await context.Periods
            .Where(x => x.Start <= grade.Date)
            .Where(x => x.End >= grade.Date)
            .Where(x => x.Name.Contains("félév"))
            .Select(x => x.Id)
            .SingleOrDefaultAsync();

        if (periodId == 0)
            return false;

        for(int i = 0; i < grade.Multiplier; i++) {
            var newGrade = new Grade{
                DividendId = grade.DividendId,
                Value = grade.Grade,
                GradeText = grade.GradeString,
                StudentId = grade.StudentId,
                TeacherId = userId,
                Date = grade.Date,
                Dated = DateTime.Now,
                Text = grade.Text,
                PeriodId = periodId
            };

            context.Grades.Add(newGrade);

            await context.SaveChangesAsync();
        }

        return true;
    }

    public async Task<bool> PostGradesAsync(int userId, List<PostGradeDto> grades)
    {
        var periodId = await context.Periods
            .Where(x => x.Start <= grades[0].Date)
            .Where(x => x.End >= grades[0].Date)
            .Where(x => x.Name.Contains("félév"))
            .Select(x => x.Id)
            .SingleOrDefaultAsync();

        if (periodId == 0) // not found, default 
            return false;

        var dated = DateTime.Now;

        foreach(var grade in grades)
        {
            for(int i = 0; i < grade.Multiplier; i++) {
                var newGrade = new Grade{
                    DividendId = grade.DividendId,
                    Value = grade.Grade,
                    GradeText = grade.GradeString,
                    StudentId = grade.StudentId,
                    TeacherId = userId,
                    Date = grade.Date,
                    Dated = dated,
                    Text = grade.Text,
                    PeriodId = periodId
                };

                context.Grades.Add(newGrade);
            }
        }
        
        await context.SaveChangesAsync();
        return true;
    }

    public async Task<List<JudgementDto>> GetAdmonitoriesAsync(int userId)
    {
        return (await context.Admonitories
            .Where(p => p.TeacherId == userId)
            .Include(p => p.LevelNavigation)
            .Include(p => p.StudentNavigation)
            .Select(p => new JudgementDto(
                    p.TeacherNavigation.Name,
                    p.StudentNavigation.Name,
                    p.Dated,
                    p.LevelNavigation.Name,
                    p.Text
                ))
            .ToListAsync())
            .OrderByDescending(p => p.Date)
            .ToList();
    }

    public async Task<List<JudgementDto>> GetPropitiousesAsync(int userId)
    {
        return (await context.Propitiouses
            .Where(p => p.TeacherId == userId)
            .Include(p => p.LevelNavigation)
            .Include(p => p.StudentNavigation)
            .Select(p => new JudgementDto(
                    p.TeacherNavigation.Name,
                    p.StudentNavigation.Name,
                    p.Dated,
                    p.LevelNavigation.Name,
                    p.Text
                ))
            .ToListAsync())
            .OrderByDescending(p => p.Date)
            .ToList();
    }

    public async Task<ClassDto?> GetClassAsync(int userId)
    {
        var myClass = await context.Classes
            .Where(t => t.HeadTeacherId == userId || t.SubHeadTeacherId == userId)
            .Include(o => o.HeadTeacherNavigation)
            .Include(o => o.SubHeadTeacherNavigation)
            .SingleOrDefaultAsync();

        if (myClass != null) {
            return new ClassDto(
                myClass.Name,
                await context.Groups
                    .Where(x => x.Name == myClass.Name)
                    .Select(x => x.Id).SingleOrDefaultAsync(),
                myClass.HeadTeacherNavigation == null ? null : myClass.HeadTeacherNavigation.Name,
                myClass.SubHeadTeacherNavigation == null ? null : myClass.SubHeadTeacherNavigation.Name,
                myClass.SevenId1,
                await context.Students
                    .Where(x => x.Id == myClass.SevenId1)
                    .Select(x => x.Name)
                    .SingleOrDefaultAsync(),
                myClass.SevenId2,
                await context.Students
                    .Where(x => x.Id == myClass.SevenId2)
                    .Select(x => x.Name)
                    .SingleOrDefaultAsync()
            );
        }
        return null;
    }

    public async Task<bool> PostClassAsync(int userId, ClassDto myClass)
    {
        var myClassDb = await context.Classes
            .Where(t => t.HeadTeacherId == userId || t.SubHeadTeacherId == userId)
            .Where(t => t.Name == myClass.Name)
            .SingleOrDefaultAsync();
        
        if (myClassDb == null)
            return false;

        myClassDb.SevenId1 = myClass.SevenId1;
        myClassDb.SevenId2 = myClass.SevenId2;

        await context.SaveChangesAsync();

        return true;
    }

    public async Task<List<IntStringDto>> GetSubjectsAsync(int userId, int groupId)
    {
        return await context.DividendLessons
            .Where(x => x.GroupId == groupId && x.TeacherId == userId)
            .Select(x => new IntStringDto{
                Int = x.Id,
                String = x.SubjectNavigation.Name
            })
            .ToListAsync();
    }

    public async Task<List<TeacherGradeSumDto>> GetGradesSumAsync(int userId, int dividendId)
    {
        var gradesSum = (await context.Grades
            .Where(j => j.DividendId == dividendId)
            .Where(j => j.Close == false)
            .Include(j => j.PeriodNavigation)
            .Include(j => j.StudentNavigation)
            .Include(j => j.DividendNavigation)
            .ToListAsync())
            .Where(j => j.DividendNavigation.TeacherId == userId)
            .ToList();

        var result = gradesSum
            .GroupBy(j => new {
                Semester = j.PeriodNavigation.Name,
                StudentId = j.StudentId,
                Student = j.StudentNavigation.Name})
            .Select(j => new TeacherGradeSumDto(
                j.Key.Semester,
                j.Key.StudentId,
                j.Key.Student,
                j.Average(s => s.Value)
            ))
            .OrderBy(p => p.Semester)
            .ThenBy(p => p.StudentId)
            .ToList();
        return result;
    }

    public async Task<List<GradeDto>> GetSubjectGradesAsync(int teacherId, TeacherGetGradeDto grade)
    {
        //get not close grades for specific subject
        var grades = (await context.Grades
        .Where(j => j.StudentId == grade.studentid)
        .Where(j => j.Close == false)
        .Include(j => j.TeacherNavigation)
        .Include(j => j.DividendNavigation.SubjectNavigation)
        .Include(j => j.PeriodNavigation)
        .Where(j => j.DividendNavigation.SubjectNavigation.Name == grade.subject)
        .ToListAsync())
        .Where(j => j.DividendNavigation.TeacherId == teacherId)
        .ToList();

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
}