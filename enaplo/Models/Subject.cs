using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("Tantargy")]
    public partial class Subject
    {
        public Subject()
        {
            InverseMainSubjectNavigation = new HashSet<Subject>();
            DividendLessons = new HashSet<DividendLesson>();
            RegisterLessons = new HashSet<RegisterLesson>();
        }

        [Column("TantargyAz")]
        public int Id { get; set; }
        [Column("Tantargy1")]
        public string Name { get; set; } = null!;
        [Column("Csoportbontas")]
        public bool? IsDivideGroup { get; set; }
        [Column("Osztalyozando")]
        public bool? ShouldGrade { get; set; }
        [Column("AlapTantargyAz")]
        public int? BaseSubjectId { get; set; }
        [Column("Fotantargye")]
        public bool IsMainSubject { get; set; }
        [Column("TargytipusAz")]
        public int SubjectTypeId { get; set; }
        [Column("IdegenNyelv")]
        public bool ForeignLanguage { get; set; }
        [Column("Gyakorlat")]
        public bool Exercise { get; set; }
        [Column("Valaszthato")]
        public bool Optional { get; set; }
        [Column("Tkf")]
        public bool Tkf { get; set; }
        [Column("TkfAz")]
        public int? TkfId { get; set; }

        public virtual Subject? MainSubjectNavigation { get; set; }
        public virtual ICollection<Subject> InverseMainSubjectNavigation { get; set; }
        public virtual ICollection<DividendLesson> DividendLessons { get; set; }
        public virtual ICollection<RegisterLesson> RegisterLessons { get; set; }
    }
}
