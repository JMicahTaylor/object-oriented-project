package edu.tntech.csc2310.dashboard.data;

public class SubjectTerm {

    private String term;
    private String subject;

    public SubjectTerm(String term, String subject){
        this.subject = subject;
        this.term = term;
    }

    public String getSubject(){
        return subject;
    }

    public String getTerm(){
        return term;
    }


}
