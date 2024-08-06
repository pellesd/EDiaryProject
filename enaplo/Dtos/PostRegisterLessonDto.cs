using System;
using System.Collections.Generic;

namespace enaplo.Dtos
{
    public partial class PostRegisterLessonDto
    {
        public int LessonId { get; set; }
        public string Day { get; set; } = null!;
        public short NumberOfLesson { get; set; }
        public DateTime Date { get; set; }
        public int TeacherId { get; set; }
        public int GroupId { get; set; }
        public int DividendId { get; set; }
        public int SubjectId { get; set; }
        public int? LessonDescriptionId { get; set; }
        public string? LessonDescription { get; set; }
        public bool Deleted { get; set; }
        public DateTime Dated { get; set; }
        public bool? ShouldGrade { get; set; }
    }
}
