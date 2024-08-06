using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("Dogaterv")]
    public partial class PlannedExam
    {
        [Column("Dkod")]
        public int Id { get; set; }
        [Column("Osztalyaz")]
        public int? ClassId { get; set; }
        [Column("Datum")]
        public DateTime? Date { get; set; }
        [Column("Szoveg")]
        public string? Text { get; set; }
        [Column("Tanaraz")]
        public int? TeacherId { get; set; }
        [Column("Csopaz")]
        public int? GroupId { get; set; }
    }
}
