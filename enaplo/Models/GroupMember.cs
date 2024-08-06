using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("CsoportTagok")]
    public partial class GroupMember
    {
        [Column("Az")]
        public int Id { get; set; }
        [Column("TanuloAz")]
        public int StudentId { get; set; }
        [Column("CsopAz")]
        public int GroupId { get; set; }
        [Column("Egyeni")]
        public bool Custom { get; set; }

        public virtual Group GroupNavigation { get; set; } = null!;
        public virtual Student StudentNavigation { get; set; } = null!;
    }
}