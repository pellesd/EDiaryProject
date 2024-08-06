using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("Jegyek")]
    public partial class Grade
    {
        [Column("Az")]
        public int Id { get; set; }
        [Column("TfeloAz")]
        public int DividendId { get; set; }
        [Column("Jegy")]
        public short? Value { get; set; }
        [Column("Ajegy")]
        public string GradeText { get; set; } = null!;
        [Column("TanuloAz")]
        public int StudentId { get; set; }
        [Column("Datum")]
        public DateTime Date { get; set; }
        [Column("TanarAz")]
        public int TeacherId { get; set; }
        [Column("IdoszakAz")]
        public int PeriodId { get; set; }
        [Column("Zaro")]
        public bool Close { get; set; }
        [Column("Bevitel")]
        public DateTime Dated { get; set; }

        [Column("Kisinfo")]
        public string? Text { get; set; }

        public virtual Period PeriodNavigation { get; set; } = null!;
        public virtual User TeacherNavigation { get; set; } = null!;
        public virtual Student StudentNavigation { get; set; } = null!;
        public virtual DividendLesson DividendNavigation { get; set; } = null!;
    }
}
