package com.imambiplob.studentsapi.enums;

public enum SSCSubject {
    BANGLA1("Bangla 1st Paper"), BANGLA2("Bangla 2nd Paper"), ENGLISH1("English 1st Paper"), ENGLISH2("English 2nd Paper"), MATH("Mathematics"), HIGHERMATH("Higher Math"), PHYSICS("Physics"), CHEMISTRY("Chemistry"), BIOLOGY("Biology"), GLOBAL_STUDIES("Global Studies"), RELIGION("Religion");

    private final String label;

    SSCSubject(String label) {
        this.label = label;
    }

    public static SSCSubject getSubjectByLabel(String label) {
        for (SSCSubject subject : SSCSubject.values()) {
            if(subject.label.equals(label)) return subject;
        }
        return null;
    }

    public static String getLabelBySubject(SSCSubject subject) {
        return subject.label;
    }

}