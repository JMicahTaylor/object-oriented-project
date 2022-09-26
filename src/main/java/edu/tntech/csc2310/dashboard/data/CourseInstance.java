package edu.tntech.csc2310.dashboard.data;

public class CourseInstance {

    private String DEPARTMENT;
    private int STUDENTCOUNT;
    private int CREDITS;
    private String CRN;
    private String COURSE;
    private String SECTION;
    private String ISTIMEDETERMINED;
    private String STARTTIME;
    private String STARTAM_PM;
    private String ENDTIME;
    private String ENDAM_PM;
    private String CLASSDAYS;
    private String ISLOCDETERMINED;
    private String BUILDING;
    private String ROOM;
    private String ISONLINE;
    private String PROF;
    private String MAXIMUMSTUDENTS;
    private String ISOPEN;
    private String TITLE;
    private SubjectTerm subjectTerm;
    private Faculty faculty;

    public CourseInstance(SubjectTerm subjectTerm, Faculty faculty){
        this.subjectTerm = subjectTerm;
        this.faculty = faculty;

    }

    public void setSubjectTerm(SubjectTerm newSubjectTerm){
        this.subjectTerm = newSubjectTerm;
    }
    public void setFaculty(){
        if(String.valueOf(PROF).contains(", ")) {
            String[] s = String.valueOf(PROF).split(", ");
            if(s[1].charAt(s[1].length() - 1) == ' '){
                s[1] = s[1].substring(0, s[1].length() - 1);
            }
            String[] s2 = s[1].split(" ");
            String s3 = "";
            for(int i = 0; i < s2.length; i++){
                s2[i] = s2[i].substring(0, 1).toUpperCase() + s2[i].substring(1).toLowerCase();
                if (s3.equals("")){
                    s3 = s2[i];
                }else{
                    s3 = s3 + " " + s2[i];
                }
            }

            String[] s4 = s[0].split(" ");
            String s5 = "";
            for(int i = 0; i < s4.length; i++){
                s4[i] = s4[i].substring(0, 1).toUpperCase() + s4[i].substring(1).toLowerCase();
                if (s5.equals("")){
                    s5 = s4[i];
                }else{
                    s5 = s5 + " " + s4[i];
                }
            }

            this.faculty = new Faculty(s3, s5);
        }else /*if(String.valueOf(PROF).equals(null))*/{
            this.faculty = new Faculty("na", "na");
        }/*else
        {
            this.faculty = new Faculty("na", "na");
        }*/
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public SubjectTerm getSubjectTerm() {
        return subjectTerm;
    }

    public String getDEPARTMENT() {
        return DEPARTMENT;
    }

    public int getSTUDENTCOUNT() {
        return STUDENTCOUNT;
    }

    public int getCREDITS() {
        return CREDITS;
    }

    public String getCRN() {
        return CRN;
    }

    public String getCOURSE() {
        return COURSE;
    }

    public String getSECTION() {
        return SECTION;
    }

    public String getISTIMEDETERMINED() {
        return ISTIMEDETERMINED;
    }

    public String getSTARTTIME() {
        return STARTTIME;
    }

    public String getSTARTAM_PM() {
        return STARTAM_PM;
    }

    public String getENDTIME() {
        return ENDTIME;
    }

    public String getENDAM_PM() {
        return ENDAM_PM;
    }

    public String getCLASSDAYS() {
        return CLASSDAYS;
    }

    public String getISLOCDETERMINED() {
        return ISLOCDETERMINED;
    }

    public String getBUILDING() {
        return BUILDING;
    }

    public String getROOM() {
        return ROOM;
    }

    public String getISONLINE() {
        return ISONLINE;
    }

    public String getPROF() {
        return PROF;
    }

    public String getMAXIMUMSTUDENTS() {
        return MAXIMUMSTUDENTS;
    }

    public String getISOPEN() {
        return ISOPEN;
    }

    public String getTITLE() {
        return TITLE;
    }

    public String toString(){
        return DEPARTMENT + " " + COURSE + "-" + SECTION + " (" + PROF + ") " +
                CLASSDAYS + "\t\t" + STARTTIME + STARTAM_PM + " - " + ENDTIME + ENDAM_PM + "\t" + STUDENTCOUNT + "/" + MAXIMUMSTUDENTS;
    }
}
