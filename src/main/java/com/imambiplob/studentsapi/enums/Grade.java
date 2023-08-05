package com.imambiplob.studentsapi.enums;

public enum Grade {
    A_PLUS("A+"), A("A"), A_MINUS("A-"), B("B"), B_MINUS("B-"), C("C"), C_MINUS("C-"), D("D"), D_MINUS("D-"), F("F");

    private final String label;

    Grade(String label) {
        this.label = label;
    }

    public static Grade getSubjectByLabel(String label) {
        for (Grade grade : Grade.values()) {
            if(grade.label.equals(label)) return grade;
        }
        return null;
    }

    public static String getLabelByGrade(Grade grade) {
        return grade.label;
    }

}
