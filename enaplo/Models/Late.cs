using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("Keses")]
    public partial class Late
    {
		[Column("Az")]
        public int Id { get; set; }
		[Column("Idotartam")]
        public int Length { get; set; }
		[Column("TanuloAz")]
        public int StudentId { get; set; }
		[Column("TfeloAz")]
        public int DividendId { get; set; }
		[Column("Tanora")]
        public int Lesson { get; set; }
		[Column("Datum")]
        public DateTime Date { get; set; }
		[Column("Torolve")]
        public bool Deleted { get; set; }
		[Column("Elszamolva")]
        public bool? Counted { get; set; }
		[Column("ElszDatum")]
        public DateTime? CountDate { get; set; }
		[Column("Elszora")]
        public int? CountLesson { get; set; }

        public virtual RegisterLesson LessonNavigation { get; set; } = null!;
        public virtual Student StudentNavigation { get; set; } = null!;
        public virtual DividendLesson DividendNavigation { get; set; } = null!;
    }
}
