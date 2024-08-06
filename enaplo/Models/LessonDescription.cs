using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("HaladasiNaplo")]
    public partial class LessonDescription
    {
        public LessonDescription()
        {
            RegisterLessons = new HashSet<RegisterLesson>();
        }

        [Column("HaladasiAz")]
        public int Id { get; set; }
        [Column("Tartalom")]
        public string? Title { get; set; }

        public virtual ICollection<RegisterLesson> RegisterLessons { get; set; }
    }
}
