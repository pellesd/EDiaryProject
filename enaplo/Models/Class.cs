using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("Osztaly")]
    public partial class Class
    {
        public Class()
        {
            StudentClasses = new HashSet<StudentClass>();
            Students = new HashSet<Student>();
        }

        [Column("OsztalyAz")]
        public int Id { get; set; }
        [Column("Nev")]
        public string Name { get; set; } = null!;
        [Column("Of1TanarAz")]
        public int? HeadTeacherId { get; set; }
        [Column("Of2TanarAz")]
        public int? SubHeadTeacherId { get; set; }
        [Column("Hetes1")]
        public int? SevenId1 { get; set; }
        [Column("Hetes2")]
        public int? SevenId2 { get; set; }

       public virtual User? HeadTeacherNavigation { get; set; }
        public virtual User? SubHeadTeacherNavigation { get; set; }
        public virtual ICollection<StudentClass> StudentClasses { get; set; }
        public virtual ICollection<Student> Students { get; set; }
    }
}
