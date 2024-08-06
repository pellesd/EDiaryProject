using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("Hirdetes")]
    public partial class Advertisement
    {
        [Column("HirdetesAz")]
        public int Id { get; set; }
        [Column("Bevitel")]
        public DateTime Dated { get; set; }
        [Column("UserAz")]
        public int UserId { get; set; }
        [Column("Ervenyes")]
        public DateTime ValidUntil { get; set; }
        [Column("Szoveg")]
        public string Text { get; set; } = null!;

        public virtual User UserNavigation { get; set; } = null!;
    }
}
