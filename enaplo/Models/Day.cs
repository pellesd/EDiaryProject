using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("Napok")]
    public partial class Day
    {
        [Column("Anap")]
        public int Number { get; set; }
        [Column("Nap")]
        public string Name { get; set; } = null!;
    }
}
