package com.imambiplob.studentsapi.enums;

import java.util.ArrayList;
import java.util.List;

public enum HSCSubject {
    BANGLA1("Bangla 1st Paper"), BANGLA2("Bangla 2nd Paper"), ENGLISH1("English 1st Paper"), ENGLISH2("English 2nd Paper"), HIGHERMATH1("Higher Math 1st Paper"), HIGHERMATH2("Higher Math 2nd Paper"), PHYSICS1("Physics 1st Paper"), PHYSICS2("Physics 2st Paper"), CHEMISTRY1("Chemistry 1st Paper"), CHEMISTRY2("Chemistry 2nd Paper"), BIOLOGY1("Biology 1st Paper"), BIOLOGY2("Biology 2nd Paper"), ICT("Information and Communication Technology");

    private final String label;

    HSCSubject(String label) {
        this.label = label;
    }

    public static HSCSubject getSubjectByLabel(String label) {
        for (HSCSubject subject : HSCSubject.values()) {
            if(subject.label.equals(label)) return subject;
        }
        return null;
    }

    public static String getLabelBySubject(HSCSubject subject) {
        return subject.label;
    }

    public static List<String> getAllLabels() {
        List<String> hscSubjectList = new ArrayList<>();
        for(HSCSubject subject: HSCSubject.values()) {
            hscSubjectList.add(HSCSubject.getLabelBySubject(subject));
        }
        return hscSubjectList;
    }
}
