using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("CsopOszt")]
    public partial class GroupClass
    {
        [Column("Az")]
        public int Id { get; set; }
        [Column("CsoportAz")]
        public int GroupId { get; set; }
        [Column("OsztalyAz")]
        public int ClassId { get; set; }
        [Column("Bontasszint")]
        public int? DividendLevel { get; set; }

        public virtual Group GroupNavigation { get; set; } = null!;
    }
}
