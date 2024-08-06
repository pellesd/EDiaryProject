using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("Figytipus")]
    public partial class AdmonitoryType
    {
        public AdmonitoryType()
        {
            Admonitories = new HashSet<Admonitory>();
        }
        
        [Column("Az")]
        public int Id { get; set; }
        [Column("Nev")]
        public string Name { get; set; } = null!;
        [Column("Pont")]
        public decimal? Point { get; set; }

        public virtual ICollection<Admonitory> Admonitories { get; set; }
    }
}
