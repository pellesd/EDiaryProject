using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("Tanulo")]
    public partial class Student
    {
        public Student()
        {
            GroupMembers = new HashSet<GroupMember>();
            Propitiouses = new HashSet<Propitious>();
            Admonitories = new HashSet<Admonitory>();
            Absences = new HashSet<Absence>();
            Grades = new HashSet<Grade>();
            Lates = new HashSet<Late>();
            StudentClasses = new HashSet<StudentClass>();
            WebMessages = new HashSet<MessageGroup>();
        }
        [Column("TanAz")]
        public int Id { get; set; }
        [Column("Nev")]
        public string Name { get; set; } = null!;
        [Column("OsztalyAz")]
        public int ClassId { get; set; }
        [Column("Tanuloazonosito")]
        public string UserId { get; set; } = null!;

        public virtual Class ClassNavigation { get; set; } = null!;
        public virtual ICollection<GroupMember> GroupMembers { get; set; }
        public virtual ICollection<Propitious> Propitiouses { get; set; }
        public virtual ICollection<Admonitory> Admonitories { get; set; }
        public virtual ICollection<Absence> Absences { get; set; }
        public virtual ICollection<Grade> Grades { get; set; }
        public virtual ICollection<Late> Lates { get; set; }
        public virtual ICollection<StudentClass> StudentClasses { get; set; }
        public virtual ICollection<MessageGroup> WebMessages { get; set; }
    }
}
