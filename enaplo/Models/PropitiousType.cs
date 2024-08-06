using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("Dicstipus")]
    public partial class PropitiousType
    {
        public PropitiousType()
        {
            PropitiousTypes = new HashSet<Propitious>();
        }
        [Column("Az")]

        public int Id { get; set; }
        [Column("Nev")]
        public string Name { get; set; } = null!;
        [Column("Pont")]
        public decimal? Point { get; set; }

        public virtual ICollection<Propitious> PropitiousTypes { get; set; }
    }
}
