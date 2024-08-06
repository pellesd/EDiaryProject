using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("Tanfelo")]
    public partial class DividendLesson
    {
        public DividendLesson()
        {
            Absences = new HashSet<Absence>();
            //InverseFotantargyNavigation = new HashSet<Tanfelo>();
            Grades = new HashSet<Grade>();
            Lates = new HashSet<Late>();
            Timetables = new HashSet<Timetable>();
            RegisteredLessons = new HashSet<RegisterLesson>();
        }
        [Column("TanfeloAz")]
        public int Id { get; set; }
        [Column("CsopAz")]
        public int GroupId { get; set; }
        [Column("TanarAz")]
        public int? TeacherId { get; set; }
        [Column("TantargyAz")]
        public int SubjectId { get; set; }
        [Column("Gyakorlat")]
        public bool Exercise { get; set; }
        [Column("IdoszakAz")]
        public int PeriodId { get; set; }
        [Column("Tkf")]
        public bool Tkf { get; set; }
        [Column("Fotantargy")]
        public int? MainSubject { get; set; }
        [Column("Tervezettoraszam")]
        public double? PlannedLessonNumber { get; set; }
        [Column("Osztalyozando")]
        public bool? ShouldGrade { get; set; }
        [Column("Fotantargye")]
        public bool IsMainLesson { get; set; }
        [Column("Elmgyak")]
        public bool DeletedExercise { get; set; }
        [Column("ElszamolasAz")]
        public int? CountedId { get; set; }
        [Column("TkfAz")]
        public int? TkfId { get; set; }
        [Column("Valaszthato")]
        public bool Optional { get; set; }
        [Column("Csopbontas")]
        public bool GroupDivide { get; set; }

        public virtual Group GroupNavigation { get; set; } = null!;
        public virtual Period PeriodNavigation { get; set; } = null!;
        public virtual User? TeacherNavigation { get; set; }
        public virtual Subject SubjectNavigation { get; set; } = null!;
        public virtual ICollection<Absence> Absences { get; set; }
        public virtual ICollection<Grade> Grades { get; set; }
        public virtual ICollection<Late> Lates { get; set; }
        public virtual ICollection<Timetable> Timetables { get; set; }
        public virtual ICollection<RegisterLesson> RegisteredLessons { get; set; }
    }
}
