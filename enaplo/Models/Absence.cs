using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("Hianyzasok")]
    public partial class Absence
    {
        [Column("Az")]
        public int Id { get; set; }
        [Column("TanuloAz")]
        public int StudentId { get; set; }
        [Column("TfeloAz")]
        public int DividendId { get; set; }
        [Column("Datum")]
        public DateTime Date { get; set; }
        [Column("Ora")]
        public int NumberOfLesson { get; set; }
        [Column("Gyak")]
        public bool Exercise { get; set; }
        [Column("TanarAz")]
        public int TeacherId { get; set; }
        [Column("Bevitel")]
        public DateTime Dated { get; set; }
        [Column("Dontott")]
        public bool NotPending { get; set; }
        [Column("Iglan")]
        public bool Unauthorized { get; set; }
        [Column("Iskolai")]
        public bool SchoolInterest { get; set; }
        [Column("Elmgyak")]
        public bool? DeletedExercise { get; set; }
        [Column("Idoszakos")]
        public bool Periodical { get; set; }
        [Column("Automata")]
        public bool Automata { get; set; }

        public virtual RegisterLesson LessonNavigation { get; set; } = null!;
        public virtual User TeacherNavigation { get; set; } = null!;
        public virtual Student StudentNavigation { get; set; } = null!;
        public virtual DividendLesson DividendNavigation { get; set; } = null!;
    }
}
