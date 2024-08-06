using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using enaplo.Dtos;

namespace enaplo.Models
{
    [Table("Tanorabeiras")]
    public partial class RegisterLesson
    {
        public RegisterLesson()
        {
            Absences = new HashSet<Absence>();
            Lates = new HashSet<Late>();
        }

        [Column("Az")]
        public int Id { get; set; }
        [Column("Nap")]
        public string Day { get; set; } = null!;
        [Column("Oraszam")]
        public short NumberOfLesson { get; set; }
        [Column("Datum")]
        public DateTime? Date { get; set; }
        [Column("Megtartva")]
        public DateTime? ActualDate { get; set; }
        [Column("Megtartvaoraszam")]
        public int? ActualNumberOfLesson { get; set; }
        [Column("TanarAz")]
        public int TeacherId { get; set; }
        [Column("CsopAz")]
        public int GroupId { get; set; }
        [Column("TfeloAz")]
        public int DividendId { get; set; }
        [Column("TantargyAz")]
        public int SubjectId { get; set; }
        [Column("HaladasiNaploAz")]
        public int? LessonDescriptionId { get; set; }
        [Column("Gyakorlat")]
        public bool Exercise { get; set; }
        [Column("Helyettesites")]
        public bool Substitution { get; set; }
        [Column("Helyettesitestipus")]
        public string? SubstitutionType { get; set; }
        [Column("Elszamolando")]
        public bool ShouldCount { get; set; }
        [Column("Osszevont")]
        public bool KeptTogether { get; set; }
        [Column("HelyettesitoTanAz")]
        public int? SubstituteTeacher { get; set; }
        [Column("Helyettesitesjegyadas")]
        public bool SubstituteGraded { get; set; }
        [Column("Elmaradt")]
        public bool Deleted { get; set; }
        [Column("BevitelDatum")]
        public DateTime Dated { get; set; }
        [Column("Osztalyozando")]
        public bool? ShouldGrade { get; set; }
        [Column("Elmgyak")]
        public bool DeletedExercise { get; set; }
        [Column("Jovairt")]
        public bool? Payed { get; set; }
        [Column("Oradijszorzo")]
        public decimal LessonPaymentMultiplier { get; set; }
        [Column("OraInfoAz")]
        public int LessonInfoId { get; set; }
        [Column("Tulora")]
        public bool PlussClass { get; set; }

        public virtual Group GroupNavigation { get; set; } = null!;
        public virtual LessonDescription? LessonDescriptionNavigation { get; set; }
        public virtual User? SubstituteTeacherNavigation { get; set; }
        public virtual User TeacherNavigation { get; set; } = null!;
        public virtual Subject SubjectNavigation { get; set; } = null!;
        public virtual DividendLesson DividendLessonNavigation { get; set; } = null!;
        public virtual ICollection<Absence> Absences { get; set; }
        public virtual ICollection<Late> Lates { get; set; }

        internal void UpdateByRegisterLessonDto(PostRegisterLessonDto lesson)
        {
            this.Day = lesson.Day;
            this.NumberOfLesson = lesson.NumberOfLesson;
            this.TeacherId = lesson.TeacherId;
            this.GroupId = lesson.GroupId;
            this.DividendId = lesson.DividendId;
            this.SubjectId = lesson.SubjectId;
            this.Deleted = lesson.Deleted;
            this.Dated = DateTime.Now;
            this.ShouldGrade = lesson.ShouldGrade;
        }
    }
}
