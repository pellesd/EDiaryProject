using System.Text;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Options;

namespace enaplo.Dal
{
    public partial class ENAPLOContext : DbContext
    {
        public string Salt { get; } = null!;

        public ENAPLOContext(DbContextOptions<ENAPLOContext> options)
            : base(options)
        {
            // TODO appconfigból vegye ki 
            Salt = "cVIg98775ed65d65dT";//Encoding.ASCII.GetBytes("cVIg98775ed65d65dT");
        }

        public virtual DbSet<GroupClass> GroupClasses { get; set; } = null!;
        public virtual DbSet<Group> Groups { get; set; } = null!;
        public virtual DbSet<GroupMember> GroupMembers { get; set; } = null!;
        public virtual DbSet<Propitious> Propitiouses { get; set; } = null!;
        public virtual DbSet<PropitiousType> PropitiousTypes { get; set; } = null!;
        public virtual DbSet<PlannedExam> PlannedExams { get; set; } = null!;
        public virtual DbSet<Admonitory> Admonitories { get; set; } = null!;
        public virtual DbSet<AdmonitoryType> AdmonitoryTypes { get; set; } = null!;
        public virtual DbSet<LessonDescription> LessonDetails { get; set; } = null!;
        public virtual DbSet<Absence> Absences { get; set; } = null!;
        public virtual DbSet<Advertisement> Advertisements { get; set; } = null!;
        public virtual DbSet<Period> Periods { get; set; } = null!;
        public virtual DbSet<Grade> Grades { get; set; } = null!;
        public virtual DbSet<GradeType> GradeTypes { get; set; } = null!;
        public virtual DbSet<Late> Lates { get; set; } = null!;
        public virtual DbSet<Canteen> Canteens { get; set; } = null!;
        public virtual DbSet<Day> Days { get; set; } = null!;
        public virtual DbSet<Timetable> Timetables { get; set; } = null!;
        public virtual DbSet<TimetableDeleted> TimetableDeletes { get; set; } = null!;
        public virtual DbSet<Class> Classes { get; set; } = null!;
        public virtual DbSet<StudentClass> StudentClasses { get; set; } = null!;
        public virtual DbSet<DividendLesson> DividendLessons { get; set; } = null!;
        public virtual DbSet<RegisterLesson> RegisterLessons { get; set; } = null!;
        public virtual DbSet<Subject> Subjects { get; set; } = null!;
        public virtual DbSet<Student> Students { get; set; } = null!;
        public virtual DbSet<ClassRoom> ClassRooms { get; set; } = null!;
        public virtual DbSet<User> Users { get; set; } = null!;
        public virtual DbSet<MessageTeacher> MessageTeachers { get; set; } = null!;
        public virtual DbSet<MessageGroup> MessageGroups { get; set; } = null!;
        public virtual DbSet<Close> Closes { get; set; } = null!;

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.UseCollation("Hungarian_CI_AS");

            modelBuilder.Entity<GroupClass>(entity =>
            {
                entity.HasKey(e => e.Id);

                entity.ToTable("CsopOszt");

                entity.HasIndex(e => new { e.GroupId, e.ClassId }, "IX_CsopOszt")
                    .IsUnique();

                entity.HasOne(d => d.GroupNavigation)
                    .WithMany(p => p.GroupClasses)
                    .HasForeignKey(d => d.GroupId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_CsopOszt_Csoport");
            });

            modelBuilder.Entity<Group>(entity =>
            {
                entity.HasKey(e => e.Id)
                    .HasName("Ekulcs_Csoport");

                entity.ToTable("Csoport");

                entity.HasIndex(e => e.Name, "IX_Csoport_Csoport")
                    .HasFillFactor(80);

                entity.HasIndex(e => e.TypeId, "IX_Csoport_TipusAz")
                    .HasFillFactor(80);

                entity.Property(e => e.Name)
                    .HasMaxLength(100)
                    .HasColumnName("Csoport");
            });

            modelBuilder.Entity<GroupMember>(entity =>
            {
                entity.HasKey(e => e.Id)
                    .HasName("Ekulcs_CsoportTagok");

                entity.ToTable("CsoportTagok");

                entity.HasIndex(e => e.GroupId, "IX_CsoportTagok_CsopAz")
                    .HasFillFactor(80);

                entity.HasIndex(e => new { e.GroupId, e.StudentId }, "IX_CsoportTagok_CsopTagsag");

                entity.HasIndex(e => e.StudentId, "IX_CsoportTagok_TanuloAz")
                    .HasFillFactor(80);

                entity.HasIndex(e => new { e.StudentId, e.GroupId }, "Ukulcs_CsoportTagok")
                    .IsUnique();

                entity.HasOne(d => d.GroupNavigation)
                    .WithMany(p => p.GroupMembers)
                    .HasForeignKey(d => d.GroupId)
                    .HasConstraintName("FK_CsoportTagok_Csoport");

                entity.HasOne(d => d.StudentNavigation)
                    .WithMany(p => p.GroupMembers)
                    .HasForeignKey(d => d.StudentId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_CsoportTagok_Tanulo");
            });

            modelBuilder.Entity<Propitious>(entity =>
            {
                entity.HasKey(e => e.Id)
                    .HasName("Ekulcs_Dicseretek");

                entity.ToTable("Dicseretek");

                entity.HasIndex(e => e.LevelId, "IX_Dicseretek_FokAz")
                    .HasFillFactor(80);

                entity.HasIndex(e => e.TeacherId, "IX_Dicseretek_TanarAz")
                    .HasFillFactor(80);

                entity.HasIndex(e => e.StudentId, "IX_Dicseretek_TanuloAz")
                    .HasFillFactor(80);

                entity.Property(e => e.Dated)
                    .HasColumnType("smalldatetime")
                    .HasDefaultValueSql("(getdate())");

                entity.Property(e => e.Text).HasColumnType("text");

                entity.HasOne(d => d.LevelNavigation)
                    .WithMany(p => p.PropitiousTypes)
                    .HasForeignKey(d => d.LevelId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Dicseretek_Dicstipus");

                entity.HasOne(d => d.TeacherNavigation)
                    .WithMany(p => p.PropitiousesNavigations)
                    .HasForeignKey(d => d.TeacherId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Dicseretek_Users");

                entity.HasOne(d => d.StudentNavigation)
                    .WithMany(p => p.Propitiouses)
                    .HasForeignKey(d => d.StudentId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Dicseretek_Tanulo");
            });

            modelBuilder.Entity<PropitiousType>(entity =>
            {
                entity.HasKey(e => e.Id)
                    .HasName("Ekulcs_Dicstipus");

                entity.HasIndex(e => e.Name, "IX_Dicstipus_Nev")
                    .HasFillFactor(80);

                entity.Property(e => e.Name)
                    .HasMaxLength(50)
                    .IsUnicode(false)
                    .IsFixedLength();

                entity.Property(e => e.Point).HasColumnType("decimal(4, 2)");
            });

            modelBuilder.Entity<PlannedExam>(entity =>
            {
                entity.HasNoKey();

                entity.ToTable("Dogaterv");

                entity.Property(e => e.GroupId).HasColumnName("csopaz");

                entity.Property(e => e.Date).HasColumnType("datetime");

                entity.Property(e => e.Id).ValueGeneratedOnAdd();

                entity.Property(e => e.Text).HasMaxLength(100);

                entity.Property(e => e.TeacherId).HasColumnName("tanaraz");
            });

            modelBuilder.Entity<Admonitory>(entity =>
            {
                entity.HasKey(e => e.Id)
                    .HasName("Ekulcs_Figyelmeztetesek");

                entity.ToTable("Figyelmeztetesek");

                entity.HasIndex(e => e.LevelId, "IX_Figyelmeztetesek_FokAz")
                    .HasFillFactor(80);

                entity.HasIndex(e => e.TeacherId, "IX_Figyelmeztetesek_TanarAz")
                    .HasFillFactor(80);

                entity.HasIndex(e => e.StudentId, "IX_Figyelmeztetesek_TanuloAz")
                    .HasFillFactor(80);

                entity.Property(e => e.Dated)
                    .HasColumnType("smalldatetime")
                    .HasDefaultValueSql("(getdate())");

                entity.Property(e => e.Text).HasColumnType("text");

                entity.HasOne(d => d.LevelNavigation)
                    .WithMany(p => p.Admonitories)
                    .HasForeignKey(d => d.LevelId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Figyelmeztetesek_Figytipus");

                entity.HasOne(d => d.TeacherNavigation)
                    .WithMany(p => p.AdmonitoriesNavigations)
                    .HasForeignKey(d => d.TeacherId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Figyelmeztetesek_Users");

                entity.HasOne(d => d.StudentNavigation)
                    .WithMany(p => p.Admonitories)
                    .HasForeignKey(d => d.StudentId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Figyelmeztetesek_Tanulo");
            });

            modelBuilder.Entity<AdmonitoryType>(entity =>
            {
                entity.HasKey(e => e.Id)
                    .HasName("Ekulcs_Figytipus");

                entity.HasIndex(e => e.Name, "IX_Figytipus_Nev")
                    .HasFillFactor(80);

                entity.Property(e => e.Name)
                    .HasMaxLength(50)
                    .IsUnicode(false)
                    .IsFixedLength();

                entity.Property(e => e.Point).HasColumnType("decimal(4, 2)");
            });

            modelBuilder.Entity<LessonDescription>(entity =>
            {
                entity.HasKey(e => e.Id)
                    .HasName("Ekulcs_HaladasiNaplo");

                entity.ToTable("HaladasiNaplo");

                entity.Property(e => e.Title).HasColumnType("ntext");
            });

            modelBuilder.Entity<Absence>(entity =>
            {
                entity.HasKey(e => e.Id)
                    .HasName("Ekulcs_Hianyzasok");

                entity.ToTable("Hianyzasok");

                entity.HasIndex(e => e.Date, "IX_Hianyzasok_Datum")
                    .HasFillFactor(80);

                entity.HasIndex(e => e.NumberOfLesson, "IX_Hianyzasok_Ora")
                    .HasFillFactor(80);

                entity.HasIndex(e => e.StudentId, "IX_Hianyzasok_TanuloAz")
                    .HasFillFactor(80);

                entity.HasIndex(e => e.DividendId, "IX_Hianyzasok_TfeloAz")
                    .HasFillFactor(80);

                entity.HasIndex(e => new { e.StudentId, e.NumberOfLesson }, "Ukulcs_Hianyzasok")
                    .IsUnique();

                entity.Property(e => e.Dated)
                    .HasColumnType("smalldatetime")
                    .HasDefaultValueSql("(getdate())");

                entity.Property(e => e.Date).HasColumnType("smalldatetime");

                entity.Property(e => e.DeletedExercise).HasDefaultValueSql("((0))");

                entity.HasOne(d => d.LessonNavigation)
                    .WithMany(p => p.Absences)
                    .HasForeignKey(d => d.NumberOfLesson)
                    .HasConstraintName("FK_Hianyzasok_TanoraBeiras");

                entity.HasOne(d => d.TeacherNavigation)
                    .WithMany(p => p.AbsencesNavigations)
                    .HasForeignKey(d => d.TeacherId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Hianyzasok_Users");

                entity.HasOne(d => d.StudentNavigation)
                    .WithMany(p => p.Absences)
                    .HasForeignKey(d => d.StudentId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Hianyzasok_Tanulo");

                entity.HasOne(d => d.DividendNavigation)
                    .WithMany(p => p.Absences)
                    .HasForeignKey(d => d.DividendId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Hianyzasok_Tanfelo");
            });

            modelBuilder.Entity<Advertisement>(entity =>
            {
                entity.HasKey(e => e.Id)
                    .HasName("Ekulcs_Hirdetes");

                entity.HasIndex(e => e.ValidUntil, "IX_Hirdetes_Ervenyes")
                    .HasFillFactor(80);

                entity.Property(e => e.Dated)
                    .HasColumnType("smalldatetime")
                    .HasDefaultValueSql("(getdate())");

                entity.Property(e => e.ValidUntil).HasColumnType("smalldatetime");

                entity.Property(e => e.Text).HasColumnType("text");

                entity.HasOne(d => d.UserNavigation)
                    .WithMany(p => p.AdvertisementsNavigations)
                    .HasForeignKey(d => d.UserId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Hirdetes_Users");
            });

            modelBuilder.Entity<Period>(entity =>
            {
                entity.HasKey(e => e.Id)
                    .HasName("Ekulcs_Idoszak");

                entity.ToTable("Idoszak");

                entity.HasIndex(e => e.Start, "IX_Idoszak_Kezd")
                    .HasFillFactor(80);

                entity.Property(e => e.Start).HasColumnType("datetime");

                entity.Property(e => e.Name).HasMaxLength(50);

                entity.Property(e => e.End).HasColumnType("datetime");
            });

            modelBuilder.Entity<Grade>(entity =>
            {
                entity.HasKey(e => e.Id)
                    .HasName("Ekulcs_Jegyek");

                entity.ToTable("Jegyek");

                entity.HasIndex(e => e.PeriodId, "IX_Jegyek_IdoszakAz")
                    .HasFillFactor(80);

                entity.HasIndex(e => e.StudentId, "IX_Jegyek_TanuloAz")
                    .HasFillFactor(80);

                entity.HasIndex(e => e.DividendId, "IX_Jegyek_TfeloAz")
                    .HasFillFactor(80);

                entity.Property(e => e.GradeText).HasMaxLength(4);

                entity.Property(e => e.Dated)
                    .HasColumnType("datetime")
                    .HasDefaultValueSql("(getdate())");

                entity.Property(e => e.Date).HasColumnType("smalldatetime");

                entity.Property(e => e.Text)
                    .HasMaxLength(100)
                    .HasColumnName("kisinfo");

                /*entity.Property(e => e.Javajegy).HasMaxLength(4);

                entity.Property(e => e.Szininfo).HasColumnName("szininfo");

                entity.Property(e => e.Szinttantargy).HasDefaultValueSql("((0))");

                entity.Property(e => e.Szovert)
                    .HasColumnType("ntext")
                    .HasColumnName("szovert");*/

                entity.HasOne(d => d.PeriodNavigation)
                    .WithMany(p => p.Grades)
                    .HasForeignKey(d => d.PeriodId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Jegyek_Idoszak");

                entity.HasOne(d => d.TeacherNavigation)
                    .WithMany(p => p.GradesNavigations)
                    .HasForeignKey(d => d.TeacherId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Jegyek_Users");

                entity.HasOne(d => d.StudentNavigation)
                    .WithMany(p => p.Grades)
                    .HasForeignKey(d => d.StudentId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Jegyek_Tanulo");

                entity.HasOne(d => d.DividendNavigation)
                    .WithMany(p => p.Grades)
                    .HasForeignKey(d => d.DividendId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Jegyek_Tanfelo");
            });

            modelBuilder.Entity<GradeType>(entity =>
            {
                entity.HasKey(e => e.Id)
                    .HasName("Ekulcs_Jegytipusok");

                entity.ToTable("Jegytipusok");

                entity.HasIndex(e => e.Name, "IX_Jegytipusok_Megnevezes")
                    .HasFillFactor(80);

                entity.Property(e => e.Name).HasMaxLength(20);

                entity.Property(e => e.ShortName).HasMaxLength(3);
            });

            modelBuilder.Entity<Late>(entity =>
            {
                entity.HasKey(e => e.Id)
                    .HasName("Ekulcs_Keses");

                entity.HasIndex(e => e.Date, "IX_Keses_Datum")
                    .HasFillFactor(80);

                entity.HasIndex(e => e.Lesson, "IX_Keses_Tanora")
                    .HasFillFactor(80);

                entity.HasIndex(e => e.StudentId, "IX_Keses_TanuloAz")
                    .HasFillFactor(80);

                entity.HasIndex(e => e.DividendId, "IX_Keses_TfeloAz")
                    .HasFillFactor(80);

                entity.Property(e => e.Date).HasColumnType("smalldatetime");

                entity.Property(e => e.CountDate).HasColumnType("smalldatetime");

                entity.Property(e => e.Counted).HasDefaultValueSql("((0))");

                entity.HasOne(d => d.LessonNavigation)
                    .WithMany(p => p.Lates)
                    .HasForeignKey(d => d.Lesson)
                    .HasConstraintName("FK_Keses_TanoraBeiras");

                entity.HasOne(d => d.StudentNavigation)
                    .WithMany(p => p.Lates)
                    .HasForeignKey(d => d.StudentId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Keses_Tanulo");

                entity.HasOne(d => d.DividendNavigation)
                    .WithMany(p => p.Lates)
                    .HasForeignKey(d => d.DividendId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Keses_Tanfelo");
            });

            modelBuilder.Entity<Day>(entity =>
            {
                entity.HasKey(e => e.Number)
                    .HasName("Ekulcs_Napok");

                entity.ToTable("Napok");

                entity.Property(e => e.Number).ValueGeneratedNever();

                entity.Property(e => e.Name).HasMaxLength(15);
            });

            modelBuilder.Entity<Canteen>(entity =>
            {
                entity.HasKey(e => e.Id)
                    .HasName("Ekulcs_Menza");

                entity.ToTable("Menza");

                entity.Property(e => e.Date).HasColumnType("datetime");

                entity.Property(e => e.FirstMeal).HasMaxLength(80);
                
                entity.Property(e => e.SecondMeal).HasMaxLength(80);
                
                entity.Property(e => e.Extra).HasMaxLength(40);
            });

            modelBuilder.Entity<Timetable>(entity =>
            {
                entity.HasKey(e => e.Id)
                    .HasName("Ekulcs_Orarend");

                entity.ToTable("Orarend");

                entity.HasIndex(e => new { e.Day, e.NumberOfLesson, e.DividendId, e.PeriodId, e.Start }, "IX_Orarend")
                    .IsUnique()
                    .HasFillFactor(80);

                entity.HasIndex(e => e.DividendId, "IX_Orarend_TanfeloAz")
                    .HasFillFactor(80);

                entity.Property(e => e.DayInt).HasColumnName("ANap");

                entity.Property(e => e.Start).HasColumnType("smalldatetime");

                entity.Property(e => e.Day).HasMaxLength(15);

                entity.HasOne(d => d.DividendLessonNavigation)
                    .WithMany(p => p.Timetables)
                    .HasForeignKey(d => d.DividendId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Orarend_Tanfelo");

                entity.HasOne(d => d.ClassRoomNavigation)
                    .WithMany(p => p.Timetables)
                    .HasForeignKey(d => d.ClassRoomId)
                    .HasConstraintName("FK_Orarend_Terem");
            });

            modelBuilder.Entity<TimetableDeleted>(entity =>
            {
                entity.HasNoKey();

                entity.ToTable("OrarendTorlesek");

                entity.Property(e => e.DayInt).HasColumnName("ANap");

                entity.Property(e => e.Start).HasColumnType("smalldatetime");

                entity.Property(e => e.Day).HasMaxLength(15);

                entity.Property(e => e.End).HasColumnType("smalldatetime");

                entity.HasOne(d => d.DividendNavigation)
                    .WithMany()
                    .HasForeignKey(d => d.DividendId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_OrarendTorlesek_Tanfelo");

                entity.HasOne(d => d.ClassRoomNavigation)
                    .WithMany()
                    .HasForeignKey(d => d.ClassRoomId)
                    .HasConstraintName("FK_OrarendTorlesek_Terem");
            });

            modelBuilder.Entity<Class>(entity =>
            {
                entity.HasKey(e => e.Id)
                    .HasName("Ekulcs_Osztaly");

                entity.ToTable("Osztaly");

                entity.HasIndex(e => e.HeadTeacherId, "IX_Osztaly_Of1_TanarAz")
                    .HasFillFactor(80);

                entity.HasIndex(e => e.SubHeadTeacherId, "IX_Osztaly_Of2_TanarAz")
                    .HasFillFactor(80);

                entity.Property(e => e.SevenId1).HasColumnName("hetes1");

                entity.Property(e => e.SevenId2).HasColumnName("hetes2");

                entity.Property(e => e.Name).HasMaxLength(20);

                entity.Property(e => e.HeadTeacherId).HasColumnName("Of1_TanarAz");

                entity.Property(e => e.SubHeadTeacherId).HasColumnName("Of2_TanarAz");

                 entity.HasOne(d => d.HeadTeacherNavigation)
                    .WithMany(p => p.MainClassesNavigations)
                    .HasForeignKey(d => d.HeadTeacherId)
                    .HasConstraintName("FK_OsztalyOf1_Users"); 

                entity.HasOne(d => d.SubHeadTeacherNavigation)
                    .WithMany(p => p.SubMainClassesNavigations)
                    .HasForeignKey(d => d.SubHeadTeacherId)
                    .HasConstraintName("FK_OsztalyOf2_Users");
            });

            modelBuilder.Entity<StudentClass>(entity =>
            {
                entity.HasKey(e => e.Id);

                entity.ToTable("TanOszt");

                entity.HasOne(d => d.ClassNavigation)
                    .WithMany(p => p.StudentClasses)
                    .HasForeignKey(d => d.ClassId)
                    .HasConstraintName("FK_TanOszt_Osztaly");

                entity.HasOne(d => d.StudentNavigation)
                    .WithMany(p => p.StudentClasses)
                    .HasForeignKey(d => d.StudentId)
                    .HasConstraintName("FK_TanOszt_Tanulo");
            });

            modelBuilder.Entity<DividendLesson>(entity =>
            {
                entity.HasKey(e => e.Id)
                    .HasName("Ekulcs_Tanfelo");

                entity.ToTable("Tanfelo");

                entity.HasIndex(e => e.GroupId, "IX_Tanfelo_CsopAz")
                    .HasFillFactor(80);

                entity.HasIndex(e => e.PeriodId, "IX_Tanfelo_IdoszakAz")
                    .HasFillFactor(80);

                entity.HasIndex(e => e.TeacherId, "IX_Tanfelo_TanarAz")
                    .HasFillFactor(80);

                entity.HasIndex(e => e.SubjectId, "IX_Tanfelo_TantargyAz")
                    .HasFillFactor(80);

                entity.HasIndex(e => new { e.SubjectId, e.GroupId, e.PeriodId, e.TeacherId, e.Exercise }, "Ukulcs_Tanfelo")
                    .IsUnique();

                entity.Property(e => e.ShouldGrade).HasDefaultValueSql("((1))");

                entity.HasOne(d => d.GroupNavigation)
                    .WithMany(p => p.DividendLessons)
                    .HasForeignKey(d => d.GroupId)
                    .HasConstraintName("FK_Tanfelo_Csoport");

                entity.HasOne(d => d.PeriodNavigation)
                    .WithMany(p => p.DividendLessons)
                    .HasForeignKey(d => d.PeriodId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Tanfelo_Idoszak");

                entity.HasOne(d => d.TeacherNavigation)
                    .WithMany(p => p.DividendLessonsNavigations)
                    .HasForeignKey(d => d.TeacherId)
                    .HasConstraintName("FK_Tanfelo_Users");

                entity.HasOne(d => d.SubjectNavigation)
                    .WithMany(p => p.DividendLessons)
                    .HasForeignKey(d => d.SubjectId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Tanfelo_Tantargy");
            });

            modelBuilder.Entity<RegisterLesson>(entity =>
            {
                entity.HasKey(e => e.Id)
                    .HasName("Ekulcs_TanoraBeiras");

                entity.HasIndex(e => e.Date, "IX_TanoraBeiras_Datum")
                    .HasFillFactor(80);

                entity.HasIndex(e => e.SubstituteTeacher, "IX_TanoraBeiras_HelyettesitoTanAz")
                    .HasFillFactor(80);

                entity.HasIndex(e => e.TeacherId, "IX_TanoraBeiras_TanarAz")
                    .HasFillFactor(80);

                entity.HasIndex(e => e.DividendId, "IX_TanoraBeiras_TfeloAz")
                    .HasFillFactor(80);

                entity.HasIndex(e => new { e.Id, e.Day, e.NumberOfLesson, e.Date, e.DividendId, e.TeacherId, e.GroupId, e.SubjectId, e.LessonDescriptionId }, "TanoraBeiras11");

                entity.HasIndex(e => new { e.Date, e.DividendId, e.TeacherId, e.GroupId }, "TanoraBeiras19");

                entity.Property(e => e.Dated)
                    .HasColumnType("smalldatetime")
                    .HasDefaultValueSql("(getdate())");

                entity.Property(e => e.Date).HasColumnType("smalldatetime");

                entity.Property(e => e.SubstitutionType)
                    .HasMaxLength(50)
                    .HasDefaultValueSql("(N'Norm l')");

                entity.Property(e => e.Payed)
                    .IsRequired()
                    .HasDefaultValueSql("((1))");

                entity.Property(e => e.ActualDate).HasColumnType("smalldatetime");

                entity.Property(e => e.Day).HasMaxLength(15);

                entity.Property(e => e.LessonInfoId).HasDefaultValueSql("((1))");

                entity.Property(e => e.LessonPaymentMultiplier)
                    .HasColumnType("decimal(4, 2)")
                    .HasDefaultValueSql("((1.0))");

                entity.Property(e => e.ShouldGrade)
                    .IsRequired()
                    .HasDefaultValueSql("((1))");

                entity.HasOne(d => d.GroupNavigation)
                    .WithMany(p => p.RegisterLessons)
                    .HasForeignKey(d => d.GroupId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Tanorabeiras_Csoport");

                entity.HasOne(d => d.LessonDescriptionNavigation)
                    .WithMany(p => p.RegisterLessons)
                    .HasForeignKey(d => d.LessonDescriptionId)
                    .HasConstraintName("FK_Tanorabeiras_HaladasiNaplo");

                entity.HasOne(d => d.SubstituteTeacherNavigation)
                    .WithMany(p => p.RegisterLessonSubstituateNavigations)
                    .HasForeignKey(d => d.SubstituteTeacher)
                    .HasConstraintName("FK_Tanorabeiras_Users1");

                entity.HasOne(d => d.TeacherNavigation)
                    .WithMany(p => p.RegisterLessonsNavigations)
                    .HasForeignKey(d => d.TeacherId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Tanorabeiras_Users");

                entity.HasOne(d => d.SubjectNavigation)
                    .WithMany(p => p.RegisterLessons)
                    .HasForeignKey(d => d.SubjectId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Tanorabeiras_Tantargy");

                entity.HasOne(d => d.DividendLessonNavigation)
                    .WithMany(p => p.RegisteredLessons)
                    .HasForeignKey(d => d.DividendId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Tanorabeiras_Tanfelo");
            });

            modelBuilder.Entity<Subject>(entity =>
            {
                entity.HasKey(e => e.Id)
                    .HasName("Ekulcs_Tantargy");

                entity.ToTable("Tantargy");

                entity.Property(e => e.IsDivideGroup)
                    .IsRequired()
                    .HasDefaultValueSql("((1))");

                entity.Property(e => e.ShouldGrade)
                    .IsRequired()
                    .HasDefaultValueSql("((1))");

                entity.Property(e => e.Name)
                    .HasMaxLength(140)
                    .HasColumnName("Tantargy");

                entity.Property(e => e.SubjectTypeId).HasDefaultValueSql("((1))");

                entity.HasOne(d => d.MainSubjectNavigation)
                    .WithMany(p => p.InverseMainSubjectNavigation)
                    .HasForeignKey(d => d.BaseSubjectId)
                    .HasConstraintName("FK_Tantargy_Tantargy");
            });

            modelBuilder.Entity<Student>(entity =>
            {
                entity.HasKey(e => e.Id)
                    .HasName("Ekulcs_Tanulo");

                entity.ToTable("Tanulo");

                entity.HasIndex(e => e.Name, "IX_Tanulo_Nev")
                    .HasFillFactor(80);

                entity.HasIndex(e => e.ClassId, "IX_Tanulo_OsztalyAz")
                    .HasFillFactor(80);


                entity.Property(e => e.Name).HasMaxLength(50);

                entity.Property(e => e.UserId).HasMaxLength(11);
                
                entity.HasOne(d => d.ClassNavigation)
                    .WithMany(p => p.Students)
                    .HasForeignKey(d => d.ClassId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Tanulo_Osztaly");
            });

            modelBuilder.Entity<ClassRoom>(entity =>
            {
                entity.HasKey(e => e.Id)
                    .HasName("Ekulcs_Terem");

                entity.ToTable("Terem");

                entity.HasIndex(e => e.Name, "IX_Megnevezes")
                    .IsUnique()
                    .HasFillFactor(90);

                entity.Property(e => e.Name).HasMaxLength(30);
            });
            
            modelBuilder.Entity<User>(entity =>
            {
                entity.HasKey(e => e.Id)
                    .HasName("Ekulcs_Users");

                entity.HasIndex(e => e.Name, "IX_Users_Nev")
                    .HasFillFactor(80);

                entity.HasIndex(e => e.Username, "Ukulcs_FelhasznaloNev")
                    .IsUnique();

                entity.Property(e => e.Username).HasMaxLength(15);

                entity.Property(e => e.Role)
                    .HasMaxLength(20)
                    .HasDefaultValueSql("('tanár')");

                entity.Property(e => e.PasswordHash)
                    .HasMaxLength(128)
                    .HasColumnName("kodszouj")
                    .HasDefaultValueSql("('')");

                entity.Property(e => e.Name).HasMaxLength(50);
            });

            modelBuilder.Entity<MessageTeacher>(entity =>
            {
                entity.HasKey(e => e.Id)
                    .HasName("Ekulcs_Uzenetek");

                entity.ToTable("Uzenetek");

                entity.HasIndex(e => e.ReceiverId, "IX_Uzenetek_FAz")
                    .HasFillFactor(80);

                entity.HasIndex(e => e.SenderId, "IX_Uzenetek_KAz")
                    .HasFillFactor(80);

                entity.Property(e => e.Date)
                    .HasColumnType("smalldatetime")
                    .HasDefaultValueSql("(getdate())");

                entity.Property(e => e.ReceiverId).HasColumnName("FAz");

                entity.Property(e => e.SenderId).HasColumnName("KAz");

                entity.Property(e => e.Seen).HasColumnType("smalldatetime");

                entity.Property(e => e.Text).HasColumnType("text");

                entity.HasOne(d => d.ReceiverNavigation)
                    .WithMany(p => p.MessageReceivedNavigations)
                    .HasForeignKey(d => d.ReceiverId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_UzenetekFAz_Users");

                entity.HasOne(d => d.SenderNavigation)
                    .WithMany(p => p.MessageSendNavigations)
                    .HasForeignKey(d => d.SenderId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_UzenetekKAz_Users");
            });

            modelBuilder.Entity<MessageGroup>(entity =>
            {
                entity.HasKey(e => e.Id)
                    .HasName("PK_Euzenetek");

                entity.ToTable("Webuzenetek");

                entity.Property(e => e.Date)
                    .HasColumnType("datetime")
                    .HasDefaultValueSql("(getdate())");

                entity.Property(e => e.ValidUntil).HasColumnType("datetime");

                entity.Property(e => e.Text).HasColumnType("text");

                entity.HasOne(d => d.SenderIdNavigation)
                    .WithMany(p => p.MessageGroups)
                    .HasForeignKey(d => d.SenderId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Webuzenetek_Users");

                entity.HasMany(d => d.Students)
                    .WithMany(p => p.WebMessages)
                    .UsingEntity<Dictionary<string, object>>(
                        "Wcimzettek",
                        l => l.HasOne<Student>().WithMany().HasForeignKey("Cimzettaz").OnDelete(DeleteBehavior.ClientSetNull).HasConstraintName("FK_Wcimzettek_Tanulo"),
                        r => r.HasOne<MessageGroup>().WithMany().HasForeignKey("Wuzenetaz").HasConstraintName("FK_Wcimzettek_Webuzenetek"),
                        j =>
                        {
                            j.HasKey("Wuzenetaz", "Cimzettaz");

                            j.ToTable("Wcimzettek");

                            j.IndexerProperty<int>("Wuzenetaz").HasColumnName("wuzenetaz");

                            j.IndexerProperty<int>("Cimzettaz").HasColumnName("cimzettaz");
                        });
            });

            modelBuilder.Entity<Close>(entity =>
            {
                entity.HasNoKey();

                entity.HasIndex(e => e.Start, "IX_Zaras")
                    .IsClustered();

                entity.Property(e => e.Start).HasColumnType("smalldatetime");

                entity.Property(e => e.Closed).HasColumnType("smalldatetime");

                entity.Property(e => e.Name).HasMaxLength(30);

                entity.Property(e => e.End).HasColumnType("smalldatetime");
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}
