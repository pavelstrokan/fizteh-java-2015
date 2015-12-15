package ru.fizteh.fivt.students.StrokanPavel.Collections;

import ru.fizteh.fivt.students.StrokanPavel.Collections.impl.Tuple;

import java.lang.reflect.InvocationTargetException;
//import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.fizteh.fivt.students.StrokanPavel.Collections.impl.FromStmt.from;
import static ru.fizteh.fivt.students.StrokanPavel.Collections.Sources.list;
import static ru.fizteh.fivt.students.StrokanPavel.Collections.CollectionQuery.Student.student;


public class CollectionQuery {

    /**
     * Make this code work!
     *
     * @param args
     */
    public static void main(String[] args) throws NoSuchMethodException,
            InstantiationException, IllegalAccessException, InvocationTargetException {
//        Iterable<Statistics> statistics =
//                from(list(
//                        student("ivanov", LocalDate.parse("1016-08-06"), "124"),
//                        student("ivadasdnv", LocalDate.parse("2008-02-23"), "125"),
//                        student("dadov", LocalDate.parse("1976-08-06"), "194"),
//                        student("dasadov", LocalDate.parse("1986-08-06"), "194")))
//
//        .select(Statistics.class, Student::getGroup, Student::age)
////                                .where(p -> p.age() > 5 && p.getDateOfBirth().getDayOfWeek() == DayOfWeek.SATURDAY)
//                        .where(rlike(Student::getName, ".*ov").and(s -> s.age() > 20))
////                        .groupBy(Student::getGroup)
////                        .having(s -> s.getCount() > 0)
//                        .orderBy(asc(Student::getGroup), desc(count(Student::getGroup)))
//                        .limit(2)
//                        .union()
//                        .from(list(student("ivanov", LocalDate.parse("1905-08-06"), "494"), student("dasdasdwqe",
// LocalDate.parse("1905-08-06"), "414")))
//                        .select(Statistics.class, Student::getGroup, Student::age)
//                        .execute();
//
// Iterable<Statistics> statistics =
//                from(list(
//                        student("iglina", LocalDate.parse("1986-08-06"), "494"),
//                        student("kargaltsev", LocalDate.parse("1986-08-06"), "495"),
//                        student("zertsalov", LocalDate.parse("1986-08-06"), "495"),
//                        student("ivanov", LocalDate.parse("1986-08-06"), "494")))
//                        .select(Statistics.class, Student::getGroup, count(Student::getGroup), avg(Student::age))
//                        .where(rlike(Student::getName, ".*ov").and(s -> s.age() > 20))
//                        .groupBy(Student::getName)
//                        .having(s -> s.getCount() > 0)
//                        .orderBy(asc(Statistics::getGroup), desc(count(Statistics::getGroup)))
//                        .limit(100)
//                        .union()
//                        .from(list(student("ivanov", LocalDate.parse("1985-08-06"), "494")))
//                        .selectDistinct(Statistics.class, s -> "all", count(s -> 1), avg(Student::age))
//                        .execute();
        Iterable<Tuple<String, String>> mentorsByStudent =
                from(list(student("ivanov", LocalDate.parse("1985-08-06"), "494"),
                        student("iglina", LocalDate.parse("1986-08-06"), "494"),
                        student("kargaltsev", LocalDate.parse("1986-08-06"), "495"),
                        student("zertsalov", LocalDate.parse("1986-08-06"), "495")))
                        .join(list(Group.group("494", "mr.sidorov")))
                        .on((s, g) -> Objects.equals(s.getGroup(), g.getGroup()))
                        .select(sg -> sg.getFirst().getName(), sg -> sg.getSecond().getMentor())
                        .where(s -> Objects.equals(s.getFirst().getName(), "iglina"))
                        .union()
                        .from(list(student("ivanov", LocalDate.parse("1985-08-06"), "494"),
                                student("iglina", LocalDate.parse("1986-08-06"), "494"),
                                student("kargaltsev", LocalDate.parse("1986-08-06"), "495"),
                                student("zertsalov", LocalDate.parse("1986-08-06"), "495")))
                        .join(list(Group.group("495", "mr.kormushin")))
                        .on((s, g) -> Objects.equals(s.getGroup(), g.getGroup()))
                        .select(sg -> sg.getFirst().getName(), sg -> sg.getSecond().getMentor())
                        .execute();
        System.out.println(mentorsByStudent);
    }


    public static class Student {
        private final String name;

        private final LocalDate dateOfBirth;

        private final String group;

        public Student(String name, LocalDate dateOfBirth, String group) {
            this.name = name;
            this.dateOfBirth = dateOfBirth;
            this.group = group;
        }

//        public  Student(String name, String group) {
//            this.name = name;
//            this.group = group;
//        }

        public String getName() {
            return name;
        }

        public LocalDate getDateOfBirth() {
            return dateOfBirth;
        }

        public String getGroup() {
            return group;
        }

        public Double age() {
            return ((double) ChronoUnit.YEARS.between(getDateOfBirth(), LocalDateTime.now()));
        }

        public static Student student(String name, LocalDate dateOfBirth, String group) {
            return new Student(name, dateOfBirth, group);
        }

        @Override
        public String toString() {
            StringBuilder toReturn = new StringBuilder("Students{");
            if (!group.equals(0)) {
                toReturn.append("group= ").append(this.group).append('\'');
            }
            if (!name.equals(0)) {
                toReturn.append("name= ").append(this.name).append('\'');
            }
            if (!dateOfBirth.equals(0)) {
                toReturn.append("dateOfBirth= ").append(this.dateOfBirth).append('\'');
            }
            toReturn.append("}");
            return toReturn.toString();
        }
    }


    public static class Statistics {

        private final String group;
        private final Long count;
        private final Double age;

        public String getGroup() {
            return group;
        }

        public Long getCount() {
            return count;
        }

        public Double getAge() {
            return age;
        }

        public Statistics(String group, Long count, Double age) {
            this.group = group;
            this.count = count;
            this.age = age;
        }

        public Statistics(String group, Double age) {
            this(group, 1L, age);
        }

        public Statistics(String group) {
            this(group, 1L, 2D);
        }

        public Statistics(Double age) {
            this("sad", 0L, age);
        }

        @Override
        public String toString() {
            StringBuilder toReturn = new StringBuilder("Statistics{");
            if (!group.equals(0)) {
                toReturn.append("group= ").append(this.group).append('\'');
            }
            if (!count.equals(0)) {
                toReturn.append("count= ").append(this.count).append('\'');
            }
            if (!age.equals(0)) {
                toReturn.append("age= ").append(this.age).append('\'');
            }
            toReturn.append("}");
            return toReturn.toString();
        }
    }

    public static class Group {
        private final String group;
        private final String mentor;

        public Group(String myGroup, String myMentor) {
            this.group = myGroup;
            this.mentor = myMentor;
        }

        public String getGroup() {
            return group;
        }

        public String getMentor() {
            return mentor;
        }
        public static Group group(String myGroup, String myMentor) {
            return new Group(myGroup, myMentor);
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder().append("Student{");
            if (group != null) {
                result.append("group='").append(group).append('\'');
            }
            if (mentor != null) {
                result.append(", name=").append(mentor);
            }
            result.append("}\n");
            return result.toString();
        }
    }

}
