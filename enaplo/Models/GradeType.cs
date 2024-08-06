using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("Jegytipusok")]
    public partial class GradeType
    {
        [Column("Az")]
        public int Id { get; set; }
        [Column("Rovidites")]
        public string ShortName { get; set; } = null!;
        [Column("Megnevezes")]
        public string Name { get; set; } = null!;
    }
}
