using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    [Table("Uzenetek")]
    public partial class MessageTeacher
    {
        [Column("UzenetAz")]
		public int Id { get; set; }
		[Column("Kaz")]
        public int SenderId { get; set; }
		[Column("Faz")]
        public int ReceiverId { get; set; }
		[Column("Datum")]
        public DateTime Date { get; set; }
		[Column("Megtekint")]
        public DateTime? Seen { get; set; }
		[Column("Szoveg")]
        public string? Text { get; set; }

        public virtual User ReceiverNavigation { get; set; } = null!;
        public virtual User SenderNavigation { get; set; } = null!;
    }
}
