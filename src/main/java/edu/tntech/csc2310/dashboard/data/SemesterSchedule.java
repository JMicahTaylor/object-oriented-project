package edu.tntech.csc2310.dashboard.data;

public class SemesterSchedule {

    private SubjectTerm subjectTerm;
    private CourseInstance[] schedule;

    public SemesterSchedule(SubjectTerm subjectTerm, CourseInstance[] schedule){
        this.subjectTerm = subjectTerm;
        this.schedule = schedule;

        for (int i = 0; i < schedule.length; i++){
            schedule[i].setSubjectTerm(subjectTerm);
            schedule[i].setFaculty();

        }

    }

    public SubjectTerm getSubjectTerm(){
        return subjectTerm;
    }

    public CourseInstance[] getSchedule(){
        return schedule;
    }

}
