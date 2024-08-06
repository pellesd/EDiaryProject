using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("Csoport")]
    public partial class Group
    {
        public Group()
        {
            GroupClasses = new HashSet<GroupClass>();
            GroupMembers = new HashSet<GroupMember>();
            DividendLessons = new HashSet<DividendLesson>();
            RegisterLessons = new HashSet<RegisterLesson>();
        }
        [Column("CsoportAz")]
        public int Id { get; set; }
        [Column("Csoport1")]
        public string Name { get; set; } = null!;
        [Column("TipusAz")]
        public int? TypeId { get; set; }

        public virtual ICollection<GroupClass> GroupClasses { get; set; }
        public virtual ICollection<GroupMember> GroupMembers { get; set; }
        public virtual ICollection<DividendLesson> DividendLessons { get; set; }
        public virtual ICollection<RegisterLesson> RegisterLessons { get; set; }
    }
}
