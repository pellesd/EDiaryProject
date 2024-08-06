using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{

    [Table("Zaras")]
    public partial class Close
    {
        [Column("Megnevezes")]
        public string Name { get; set; } = null!;
        [Column("Kezd")]
        public DateTime? Start { get; set; }
        [Column("Veg")]
        public DateTime? End { get; set; }
        [Column("Lezarva")]
        public DateTime? Closed { get; set; }
    }
}
