using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace enaplo.Models
{
    public partial class User
    {
        public User()
        {
            PropitiousesNavigations = new HashSet<Propitious>();
            AdmonitoriesNavigations = new HashSet<Admonitory>();
            AbsencesNavigations = new HashSet<Absence>();
            AdvertisementsNavigations = new HashSet<Advertisement>();
            GradesNavigations = new HashSet<Grade>();
            MainClassesNavigations = new HashSet<Class>();
            SubMainClassesNavigations = new HashSet<Class>();
            DividendLessonsNavigations = new HashSet<DividendLesson>();
            RegisterLessonSubstituateNavigations = new HashSet<RegisterLesson>();
            RegisterLessonsNavigations = new HashSet<RegisterLesson>();
            MessageReceivedNavigations = new HashSet<MessageTeacher>();
            MessageSendNavigations = new HashSet<MessageTeacher>();
            MessageGroups = new HashSet<MessageGroup>();
        }

        [Column("UserAz")]
        public int Id { get; set; }
        [Column("Nev")]
        public string Name { get; set; } = null!;
        [Column("Jog")]
        public string? Role { get; set; }
        [Column("FelhasznaloNev")]
        public string? Username { get; set; }
        [Column("Kodszouj")]
        public string PasswordHash { get; set; } = null!;

        // TODO adott tanár által beírt figyelmeztetesek, dicseretek listázása 
        public virtual ICollection<Propitious> PropitiousesNavigations { get; set; }
        public virtual ICollection<Admonitory> AdmonitoriesNavigations { get; set; }
        public virtual ICollection<Absence> AbsencesNavigations { get; set; }
        public virtual ICollection<Advertisement> AdvertisementsNavigations { get; set; }
        public virtual ICollection<Grade> GradesNavigations { get; set; }
        // TODO ofő és helyettes bejelölheti a heteseket, esetleg számolás h ki jön?
        public virtual ICollection<Class> MainClassesNavigations { get; set; }
        public virtual ICollection<Class> SubMainClassesNavigations { get; set; }
        public virtual ICollection<DividendLesson> DividendLessonsNavigations { get; set; }
        public virtual ICollection<RegisterLesson> RegisterLessonSubstituateNavigations { get; set; }
        public virtual ICollection<RegisterLesson> RegisterLessonsNavigations { get; set; }
        
        public virtual ICollection<MessageTeacher> MessageReceivedNavigations { get; set; }
        public virtual ICollection<MessageTeacher> MessageSendNavigations { get; set; }
        public virtual ICollection<MessageGroup> MessageGroups { get; set; }
    }
}
