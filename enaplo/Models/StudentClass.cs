using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("TanOszt")]
    public partial class StudentClass
    {
        [Column("TanosztAz")]
        public int Id { get; set; }
        [Column("TanAz")]
        public int StudentId { get; set; }
        [Column("OsztalyAz")]
        public int ClassId { get; set; }

        public virtual Class ClassNavigation { get; set; } = null!;
        public virtual Student StudentNavigation { get; set; } = null!;
    }
}
