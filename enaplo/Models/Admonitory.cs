using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("Figyelmeztetesek")]
    public partial class Admonitory
    {
        [Column("Az")]
        public int Id { get; set; }
        [Column("TanuloAz")]
        public int StudentId { get; set; }
        [Column("TanarAz")]
        public int TeacherId { get; set; }
        [Column("Bevitel")]
        public DateTime? Dated { get; set; }
        [Column("Szoveg")]
        public string Text { get; set; } = null!;
        [Column("FokAz")]
        public int LevelId { get; set; }

        public virtual AdmonitoryType LevelNavigation { get; set; } = null!;
        public virtual User TeacherNavigation { get; set; } = null!;
        public virtual Student StudentNavigation { get; set; } = null!;
    }
}
