package ru.fizteh.fivt.students.StrokanPavel.Collections;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CollectionQuery {

    public static void main(String[] args) {
        System.out.println("CQL");
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

        public Student(String name, String group) {
            this.name = name;
            this.group = group;
            this.dateOfBirth = null;
        }

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
            if (group != null) {
                toReturn.append("group= ").append(this.group).append('\'');
            }
            if (name != null) {
                toReturn.append("name= ").append(this.name).append('\'');
            }
            if (dateOfBirth != null) {
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

        public Statistics(String group, Long count) {
            this.group = group;
            this.count = count;
            this.age = null;
        }
        public Statistics(String group, Long count, Double age) {
            this.group = group;
            this.count = count;
            this.age = age;
        }
        public Statistics(String group) {
            this.group = group;
            this.count = null;
            this.age = null;
        }
        @Override
        public String toString() {
            StringBuilder result = new StringBuilder().append("Statistics{");
            if (group != null) {
                result.append("group ='").append(group).append('\'');
            }
            if (count != null) {
                result.append(", count =").append(count);
            }
            if (age != null) {
                result.append(", age =").append(age);
            }
            result.append("}\n");
            return result.toString();
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
