using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("Terem")]
    public partial class ClassRoom
    {
        public ClassRoom()
        {
            Timetables = new HashSet<Timetable>();
        }

        [Column("TeremAz")]
        public int Id { get; set; }
        [Column("Megnevezes")]
        public string Name { get; set; } = null!;

        public virtual ICollection<Timetable> Timetables { get; set; }
    }
}
