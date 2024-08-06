using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("Webuzenetek")]
    public partial class MessageGroup
    {
        public MessageGroup()
        {
            Students = new HashSet<Student>();
        }

        [Column("Wuzenetaz")]
        public int Id { get; set; }
        [Column("Kaz")]
        public int SenderId { get; set; }
        [Column("Datum")]
        public DateTime Date { get; set; }
        [Column("Tartalom")]
        public string? Text { get; set; }
        [Column("Ervenyes")]
        public DateTime ValidUntil { get; set; }

        public virtual User SenderIdNavigation { get; set; } = null!;
        public virtual ICollection<Student> Students { get; set; }
    }
}
