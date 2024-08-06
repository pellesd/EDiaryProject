using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("Idoszak")]
    public partial class Period
    {
        public Period()
        {
            Grades = new HashSet<Grade>();
            DividendLessons = new HashSet<DividendLesson>();
        }

        [Column("IdoszakAz")]
        public int Id { get; set; }
        [Column("Nev")]
        public string Name { get; set; } = null!;
        [Column("Kezd")]
        public DateTime? Start { get; set; }
        [Column("Veg")]
        public DateTime? End { get; set; }

        public virtual ICollection<Grade> Grades { get; set; }
        public virtual ICollection<DividendLesson> DividendLessons { get; set; }
    }
}
