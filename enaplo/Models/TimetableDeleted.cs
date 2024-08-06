using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("OrarendTorlesek")]
    public partial class TimetableDeleted
    {
        [Column("OrarendAz")]
        public int Id { get; set; }
        [Column("Nap")]
        public string Day { get; set; } = null!;
        [Column("Oraszam")]
        public short NumberOfLesson { get; set; }
        [Column("TanfeloAz")]
        public int DividendId { get; set; }
        [Column("Tkf")]
        public bool Tkf { get; set; }
        [Column("TeremAz")]
        public int? ClassRoomId { get; set; }
        [Column("CiklusAz")]
        public int PeriodId { get; set; }
        [Column("KezdDatum")]
        public DateTime Start { get; set; }
        [Column("Anap")]
        public int? DayInt { get; set; }
        [Column("VegeDatum")]
        public DateTime End { get; set; }

        public virtual DividendLesson DividendNavigation { get; set; } = null!;
        public virtual ClassRoom? ClassRoomNavigation { get; set; }
    }
}
