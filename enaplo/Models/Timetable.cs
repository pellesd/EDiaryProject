using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    // Az adatbázisban logikailag össze van cserélve az Órarend az ÓrarendTörlések táblával,
    // Gondolkodtam hogy ezt kövessem-e, vagy visszacseréljem, de végül úgy döntöttem követem.
    // Így Timetable táblában a törölt órák, és TimetableDeletedben a megtartott órák találhatóak. :/
    [Table("Orarend")]
    public partial class Timetable
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

        public virtual DividendLesson DividendLessonNavigation { get; set; } = null!;
        public virtual ClassRoom? ClassRoomNavigation { get; set; }
    }
}
