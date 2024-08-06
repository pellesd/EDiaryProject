using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("Menza")]
    public partial class Canteen
    {
        [Column("Az")]
        public int Id { get; set; }
        [Column("Datum")]
        public DateTime Date { get; set; }
        [Column("ElsoFogas")]
        public string FirstMeal { get; set; } = null!;
        [Column("MasodikFogas")]
        public string? SecondMeal { get; set; }
        [Column("Extra")]
        public string? Extra { get; set; }
        
    }
}