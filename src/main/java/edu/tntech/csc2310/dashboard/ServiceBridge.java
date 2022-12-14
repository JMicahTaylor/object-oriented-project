package edu.tntech.csc2310.dashboard;

import edu.tntech.csc2310.dashboard.data.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@RestController
public class ServiceBridge {


    private static final String apiKey = "44603498-EE17-4271-ADAC-2CC86FFDC2F3";
    private static final String urlString = "https://portapit.tntech.edu/express/api/unprotected/getCourseInfoByAPIKey.php?Subject=%s&Term=%s&Key=%s";

    private CourseInstance[] courses;

    public ServiceBridge() {
    }

    /**
     *
     * @param subject STRING - Abbreviated title for subject of course
     * @param term STRING - 6-digit code for the semester, year + "10" or "50" or "80"
     *
     */
    public ServiceBridge(String term, String subject) {

        Gson gson = new Gson();

        try {
            String urlFmt = String.format(urlString, subject.toUpperCase(), term, apiKey);
            URL url = new URL(urlFmt);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            JsonReader jr = gson.newJsonReader(in);
            courses = gson.fromJson(jr, CourseInstance[].class);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param bgt - last 2 digits of 6-digit code for the semester, should be "10" or "50" or "80"
     * @return bgt - Incremented integer. "10" becomes "50", "50" becomes "80" and "80" becomes "10"
     *
     */
    public int termmonthadder(int bgt) {
        int bgt2 = bgt;
        if (bgt == 10) {
            bgt2 = bgt2 + 40;
        } else if (bgt == 50) {
            bgt2 = bgt2 + 30;
        } else if (bgt == 80) {
            bgt2 = bgt2 + 30;
        } else {
            return bgt;
        }

        return Integer.parseInt(String.valueOf(bgt2).substring(String.valueOf(bgt2).length() - 2));
    }

    /**
     *
     * @param subject STRING - Abbreviated title for subject of course
     * @param term STRING - 6-digit code for the semester, year + "10" or "50" or "80"
     * @return courses - array of CourseInstances generated by the JSon parser reading data from the API.
     *
     */
    public CourseInstance[] ServiceB(String term, String subject) {
        Gson gson = new Gson();

        try {
            String urlFmt = String.format(urlString, subject.toUpperCase(), term, apiKey);
            URL url = new URL(urlFmt);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            JsonReader jr = gson.newJsonReader(in);
            courses = gson.fromJson(jr, CourseInstance[].class);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return courses;
    }

    public CourseInstance[] getCourses() {
        return courses;
    }

    /**
     *
     * @param term STRING - 6-digit code for the semester, year + "10" or "50" or "80"
     * @return s - SemesterSchedule object displaying both an array of CourseInstances that show
     * all information of courses, along with the subject and term, of all of these courses
     * based upon the specified term.
     *
     */
    @GetMapping("/allcourses")
    public SemesterSchedule allcourses(
            @RequestParam(value = "term", defaultValue = "na") String term) {
        CourseInstance[] schedule = ServiceB(term, "");
        SubjectTerm subjectTerm = new SubjectTerm(term, "ALL");
        SemesterSchedule s = new SemesterSchedule(subjectTerm, schedule);
        return s;
    }

    /**
     *
     * @param subject STRING - Abbreviated title for subject of course
     * @param term STRING - 6-digit code for the semester, year + "10" or "50" or "80"
     * @return s - SemesterSchedule object displaying both an array of CourseInstances that show
     * all information of courses, along with the subject and term, of all of these courses
     * based upon the specified term and subject.
     *
     */
    @GetMapping("/coursesbysubject")
    public SemesterSchedule coursesbysubject(
            @RequestParam(value = "subject", defaultValue = "na") String subject,
            @RequestParam(value = "term", defaultValue = "na") String term) {
        CourseInstance[] schedule = ServiceB(term, subject.toUpperCase());
        SubjectTerm subjectTerm = new SubjectTerm(term, subject.toUpperCase());
        SemesterSchedule s = new SemesterSchedule(subjectTerm, schedule);
        return s;
    }

    /**
     *
     * @param subject STRING - Abbreviated title for subject of course
     * @param term STRING - 6-digit code for the semester, year + "10" or "50" or "80"
     * @param lastname STRING - Last name of professor teaching course
     * @param firstname STRING - First name and middle name(s) of professor teaching course
     * @return array - Array of CourseInstance objects displaying all possible information
     * of any courses taught by a specific professor during a specified term, such as
     * room number, times, etc...
     */
    @GetMapping("/coursesbyfaculty")
    public CourseInstance[] coursesbyfaculty(
            @RequestParam(value = "subject", defaultValue = "na") String subject,
            @RequestParam(value = "term", defaultValue = "na") String term,
            @RequestParam(value = "lastname", defaultValue = "na") String lastname,
            @RequestParam(value = "firstname", defaultValue = "na") String firstname) {

        if (firstname.charAt(firstname.length() - 1) == ' ') {
            firstname = firstname.substring(0, firstname.length() - 1);
        }
        String[] s2 = firstname.split(" ");
        String s3 = "";
        for (int i = 0; i < s2.length; i++) {
            s2[i] = s2[i].substring(0, 1).toUpperCase() + s2[i].substring(1).toLowerCase();
            if (s3.equals("")) {
                s3 = s2[i];
            } else {
                s3 = s3 + " " + s2[i];
            }
        }

        String[] s4 = lastname.split(" ");
        String s5 = "";
        for (int i = 0; i < s4.length; i++) {
            s4[i] = s4[i].substring(0, 1).toUpperCase() + s4[i].substring(1).toLowerCase();
            if (s5.equals("")) {
                s5 = s4[i];
            } else {
                s5 = s5 + " " + s4[i];
            }
        }


        CourseInstance[] schedule = ServiceB(term, subject.toUpperCase());
        SubjectTerm subjectTerm = new SubjectTerm(term, subject.toUpperCase());
        int al = 0;

        for (int i = 0; i < schedule.length; i++) {
            schedule[i].setSubjectTerm(subjectTerm);
            schedule[i].setFaculty();

            if (s3.equals(String.valueOf(schedule[i].getFaculty().getFirstname())) && s5.equals(String.valueOf(schedule[i].getFaculty().getLastname()))) {
                al++;
            }
        }

        CourseInstance[] array = new CourseInstance[al];

        int nal = 0;
        for (int i = 0; i < schedule.length; i++) {
            if (s3.equals(String.valueOf(schedule[i].getFaculty().getFirstname())) && s5.equals(String.valueOf(schedule[i].getFaculty().getLastname()))) {
                array[nal] = schedule[i];
                nal++;
            }
        }

        return array;

    }

    /**
     *
     * @param subject STRING - Abbreviated title for subject of course
     * @param term STRING - 6-digit code for the semester, year + "10" or "50" or "80"
     * @param section STRING - section number, i.e. "001" dependent upon the course number
     * @param course STRING - Course number, i.e., "2310."
     * @return c - CourseInstance object displaying information about the course based on the section,
     * such as room, professor, times, capacity, etc...
     */
    @GetMapping("/coursebysection")
    public CourseInstance coursebysection(
            @RequestParam(value = "subject", defaultValue = "na") String subject,
            @RequestParam(value = "term", defaultValue = "na") String term,
            @RequestParam(value = "section", defaultValue = "na") String section,
            @RequestParam(value = "course", defaultValue = "na") String course) {

        CourseInstance[] schedule = ServiceB(term, subject.toUpperCase());
        SubjectTerm subjectTerm = new SubjectTerm(subject.toUpperCase(), term);
        CourseInstance c = null;
        for (int i = 0; i < schedule.length; i++) {
            schedule[i].setSubjectTerm(subjectTerm);
            schedule[i].setFaculty();

            if (section.equals(String.valueOf(schedule[i].getSECTION())) && course.equals(String.valueOf(schedule[i].getCOURSE()))) {
                c = schedule[i];
            }
        }
        return c;
    }
    /**
     *
     * @param subject STRING - Abbreviated title for subject of course
     * @param term STRING - 6-digit code for the semester, year + "10" or "50" or "80"
     * @return subjectCreditHours - Number of credit hours generated per department, along with term
     * and course name.
     */
    @GetMapping("/schbydepartment")
    public SubjectCreditHours schbydepartment(
            @RequestParam(value = "subject", defaultValue = "na") String subject,
            @RequestParam(value = "term", defaultValue = "na") String term) {
        CourseInstance[] schedule = ServiceB(term, subject.toUpperCase());

        if(schedule.length == 0){
            return new SubjectCreditHours(0, null);
        }

        SubjectTerm subjectTerm = new SubjectTerm(term, subject.toUpperCase());
        int chg = 0;
        for (int i = 0; i < schedule.length; i++) {
            chg = chg + schedule[i].getCREDITS() * schedule[i].getSTUDENTCOUNT();
        }

        SubjectCreditHours subjectCreditHours = new SubjectCreditHours(chg, subjectTerm);
        return subjectCreditHours;
    }


    /**
     *
     * @param subject STRING - Abbreviated title for subject of course
     * @param term STRING - 6-digit code for the semester, year + "10" or "50" or "80"
     * @param lastname STRING - Last name of professor teaching course
     * @param firstname STRING - First name and middle name(s) of professor teaching course
     * @return facultyCreditHours - faculty credithours object displaying amount of credit hours generated,
     * along with professor's full name and term.
     */
    @GetMapping("/schbyfaculty")
    public FacultyCreditHours schbyfaculty(
            @RequestParam(value = "subject", defaultValue = "na") String subject,
            @RequestParam(value = "term", defaultValue = "na") String term,
            @RequestParam(value = "lastname", defaultValue = "na") String lastname,
            @RequestParam(value = "firstname", defaultValue = "na") String firstname) {

        SubjectTerm subjectTerm;
        CourseInstance[] schedule;
        subjectTerm = new SubjectTerm(term, subject.toUpperCase());
        schedule = ServiceB(term, subject.toUpperCase());

        String[] s2 = firstname.split(" ");
        String s3 = "";
        for (int i = 0; i < s2.length; i++) {
            s2[i] = s2[i].substring(0, 1).toUpperCase() + s2[i].substring(1).toLowerCase();
            if (s3.equals("")) {
                s3 = s2[i];
            } else {
                s3 = s3 + " " + s2[i];
            }
        }

        String[] s4 = lastname.split(" ");
        String s5 = "";
        for (int i = 0; i < s4.length; i++) {
            s4[i] = s4[i].substring(0, 1).toUpperCase() + s4[i].substring(1).toLowerCase();
            if (s5.equals("")) {
                s5 = s4[i];
            } else {
                s5 = s5 + " " + s4[i];
            }
        }

        if (s3.charAt(firstname.length() - 1) == ' ') {
            s3 = s3.substring(0, s3.length() - 1);
        }

        Faculty faculty = new Faculty(s3, s5);

        FacultyCreditHours facultyCreditHours;

        if (!facultybysubject(term, subject).contains(faculty)) {
            facultyCreditHours = new FacultyCreditHours(null, 0, subjectTerm);
            return facultyCreditHours;
        } else {

            int chg = 0;
            for (int i = 0; i < schedule.length; i++) {

                schedule[i].setSubjectTerm(subjectTerm);

                schedule[i].setFaculty();

                if (s3.equals(String.valueOf(schedule[i].getFaculty().getFirstname())) && s5.equals(String.valueOf(schedule[i].getFaculty().getLastname()))) {
                    chg = chg + schedule[i].getCREDITS() * schedule[i].getSTUDENTCOUNT();
                }
            }
            facultyCreditHours = new FacultyCreditHours(faculty, chg, subjectTerm);
            //

            return facultyCreditHours;
        }
    }

    /**
     *
     * @param subject STRING - Abbreviated title for subject of course
     * @param term STRING - 6-digit code for the semester, year + "10" or "50" or "80"
     * @param beginterm STRING - The earliest 6-digit semester specified by user
     * @param endterm STRING - The latest 6-digit semester specified by user.
     * @return bdt - arrayList of SubjectCreditHour objects that display the credit hours generated
     * by a specific subject that occured between (inclusively) the semesters specified by the
     * user in beginterm and endterm.
     *
     */
    @GetMapping("/schbydepartmentandterms")
    public ArrayList<SubjectCreditHours> schbydepartmentandterms (
            @RequestParam(value = "subject", defaultValue = "na") String subject,
            @RequestParam(value = "term", defaultValue = "na") String term,
            @RequestParam(value = "beginterm", defaultValue = "na") String beginterm,
            @RequestParam(value = "endterm", defaultValue = "na") String endterm){
        int btm;
        int bty;
        int etm;
        int ety;
        // Parse beginterm and endterm into integers
        if (beginterm.length() < 6 || endterm.length() < 6) {
            btm = 0;
            bty = 0;
            etm = 0;
            ety = 0;
        } else {
            try {
                btm = Integer.parseInt(beginterm.substring(beginterm.length() - 2));
                bty = Integer.parseInt(beginterm.substring(0, 4));

                etm = Integer.parseInt(endterm.substring(endterm.length() - 2));
                ety = Integer.parseInt(endterm.substring(0, 4));
            } catch (NumberFormatException e) {
                btm = 0;
                bty = 0;
                etm = 0;
                ety = 0;
            }
        }


        ArrayList<SubjectCreditHours> bdt = new ArrayList<>();

        int yi = bty;
        int mi = btm;
        if (bty > 2008 && ety > 2008) {
            if (etm == 10 || etm == 50 || etm == 80) {
                while (yi <= ety && !(String.valueOf(yi) + String.valueOf(mi)).equals(endterm)) {
                    bdt.add(schbydepartment(subject, String.valueOf(yi) + String.valueOf(mi)));
                    if (termmonthadder(mi) == 10)
                        yi = yi + 1;

                    mi = termmonthadder(mi);

                }

                bdt.add(schbydepartment(subject, String.valueOf(ety) + String.valueOf(etm)));
            }
        }

        return bdt;
    }

    /**
     *
     * @param subject STRING - Abbreviated title for subject of course
     * @param term STRING - 6-digit code for the semester, year + "10" or "50" or "80"
     * @param termlist STRING - list of 6-digit semester codes delimited by a ",".
     * @return bdt - arrayList of SubjectCreditHour objects that display the credit hours generated
     * by a specific subject that occured during the semesters specified by the user in termlist.
     *
     */
    @GetMapping("/schbydepartmentandtermlist")
    public ArrayList<SubjectCreditHours> schbydepartmentandtermlist (
            @RequestParam(value = "subject", defaultValue = "na") String subject,
            @RequestParam(value = "term", defaultValue = "na") String term,
            @RequestParam(value = "termlist", defaultValue = "na") String termlist){

        ArrayList<SubjectCreditHours> bdt = new ArrayList<>();

        // ArrayList<String> list = new ArrayList<>();
        String[] list2 = termlist.split(",");
        for (int i = 0; i < list2.length; i++) {
            if (list2[i].length() == 6) {
                if (list2[i].substring(4).equals("10") || list2[i].substring(4).equals("50") || list2[i].substring(4).equals("80")) {
                    try {
                        if (Integer.parseInt(list2[i].substring(0, 4)) > 2008)
                            bdt.add(schbydepartment(subject, list2[i]));
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }

        return bdt;
    }

    /**
     *
     * @param subject STRING - Abbreviated title for subject of course
     * @param term STRING - 6-digit code for the semester, year + "10" or "50" or "80"
     * @param lastname STRING - Last name of professor teaching course
     * @param firstname STRING - First name and middle name(s) of professor teaching course
     * @param beginterm STRING - The earliest 6-digit semester specified by user
     * @param endterm STRING - The latest 6-digit semester specified by user.
     * @return bdt - arrayList of SubjectCreditHour objects that display the credit hours generated
     * by a specific subject and faculty member (specified by firstname and lastname) that
     * occurred between (inclusively) the semesters specified by the user in beginterm and endterm.
     *
     */
    @GetMapping("/schbyfacultyandterms")
    public ArrayList<SubjectCreditHours> schbyfacultyandterms (
            @RequestParam(value = "subject", defaultValue = "na") String subject,
            @RequestParam(value = "term", defaultValue = "na") String term,
            @RequestParam(value = "lastname", defaultValue = "na") String lastname,
            @RequestParam(value = "firstname", defaultValue = "na") String firstname,
            @RequestParam(value = "beginterm", defaultValue = "na") String beginterm,
            @RequestParam(value = "endterm", defaultValue = "na") String endterm)
            throws NumberFormatException {

        {
            int btm;
            int bty;
            int etm;
            int ety;
            // Parse beginterm and endterm into integers
            if (beginterm.length() < 6 || endterm.length() < 6) {
                btm = 0;
                bty = 0;
                etm = 0;
                ety = 0;
            } else {
                try {
                    btm = Integer.parseInt(beginterm.substring(4));
                    bty = Integer.parseInt(beginterm.substring(0, 4));

                    etm = Integer.parseInt(endterm.substring(4));
                    ety = Integer.parseInt(endterm.substring(0, 4));
                } catch (NumberFormatException e) {
                    btm = 0;
                    bty = 0;
                    etm = 0;
                    ety = 0;
                }
            }

            ArrayList<SubjectCreditHours> bdt = new ArrayList<>();

            int yi = bty;
            int mi = btm;
            if (bty > 2008 && ety > 2008) {
                if (etm == 10 || etm == 50 || etm == 80) {
                    while (yi <= ety && !(String.valueOf(yi) + String.valueOf(mi)).equals(endterm)) {
                        bdt.add(schbyfaculty(subject, String.valueOf(yi) + String.valueOf(mi), lastname, firstname));
                        if (termmonthadder(mi) == 10)
                            yi = yi + 1;

                        mi = termmonthadder(mi);

                    }

                    bdt.add(schbyfaculty(subject, String.valueOf(ety) + String.valueOf(etm), lastname, firstname));
                }
            }
            return bdt;
        }
    }

    /**
     *
     * @param subject STRING - Abbreviated title for subject of course
     * @param term STRING - 6-digit code for the semester, year + "10" or "50" or "80"
     * @param lastname STRING - Last name of professor teaching course
     * @param firstname STRING - First name and middle name(s) of professor teaching course
     * @return bdt - arrayList of SubjectCreditHour objects that display the credit hours generated
     * by a specific subject and faculty member (specified by firstname and lastname)
     * that occurred during the semesters specified by the user in termlist.
     *
     */
    @GetMapping("/schbyfacultyandtermlist")
    public ArrayList<SubjectCreditHours> schbyfacultyandtermlist (
            @RequestParam(value = "subject", defaultValue = "na") String subject,
            @RequestParam(value = "term", defaultValue = "na") String term,
            @RequestParam(value = "lastname", defaultValue = "na") String lastname,
            @RequestParam(value = "firstname", defaultValue = "na") String firstname,
            @RequestParam(value = "termlist", defaultValue = "na") String termlist){

        ArrayList<SubjectCreditHours> bdt = new ArrayList<>();

        // ArrayList<String> list = new ArrayList<>();

        String[] list2 = termlist.split(",");
        // List<String> al = new ArrayList<String>(Arrays.asList(list2));
        // list.addAll(al);

        if (firstname.charAt(firstname.length() - 1) == ' ') {
            firstname = firstname.substring(0, firstname.length() - 1);
        }
        String[] s2 = firstname.split(" ");
        String s3 = "";
        for (int i = 0; i < s2.length; i++) {
            s2[i] = s2[i].substring(0, 1).toUpperCase() + s2[i].substring(1).toLowerCase();
            if (s3.equals("")) {
                s3 = s2[i];
            } else {
                s3 = s3 + " " + s2[i];
            }
        }

        String[] s4 = lastname.split(" ");
        String s5 = "";
        for (int i = 0; i < s4.length; i++) {
            s4[i] = s4[i].substring(0, 1).toUpperCase() + s4[i].substring(1).toLowerCase();
            if (s5.equals("")) {
                s5 = s4[i];
            } else {
                s5 = s5 + " " + s4[i];
            }
        }


        for (int i = 0; i < list2.length; i++) {
            if (list2[i].length() == 6) {
                if (list2[i].substring(4).equals("10") || list2[i].substring(4).equals("50") || list2[i].substring(4).equals("80")) {
                    try {
                        Faculty faculty = new Faculty(s3, s5);
                        if (Integer.parseInt(list2[i].substring(0, 4)) > 2008 && facultybysubject(list2[i], subject).contains(faculty))
                            bdt.add(schbyfaculty(subject, list2[i], s5, s3));
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }

        return bdt;
    }

    /**
     *
     * @param term STRING - 6-digit code for the semester, year + "10" or "50" or "80"
     * @param crnlist STRING - list of CRN numbers (course numbers) used to specify courses, delimited by ","
     * @return bdt - ArrayList of CourseInstance objects that display all course information based on the
     * specified CRN, such as faculty teaching the course, building, course schedule, etc...
     *
     */
    @GetMapping("/coursesbycrnlist")
    public ArrayList<CourseInstance> coursesbycrnlist (
            @RequestParam(value = "term", defaultValue = "na") String term,
            @RequestParam(value = "crnlist", defaultValue = "na") String crnlist){

        ArrayList<CourseInstance> bdt = new ArrayList<>();

        // ArrayList<String> list = new ArrayList<>();

        String[] list2 = crnlist.split(",");
        // List<String> al = new ArrayList<String>(Arrays.asList(list2));
        // list.addAll(al);

        int l = allcourses(term).getSchedule().length;
        SemesterSchedule s = allcourses(term);

        //for (int i = 0; i < list2.length; i++) {
        for (int i = 0; i < list2.length; i++) {
            for (int j = 0; j < l; j++) {
                if (list2[i].equals(String.valueOf(s.getSchedule()[j].getCRN()))) {
                    SubjectTerm st = new SubjectTerm(String.valueOf(s.getSchedule()[j].getDEPARTMENT()), term);
                    s.getSchedule()[j].setSubjectTerm(st);
                    bdt.add(s.getSchedule()[j]);
                }
            }
        }

        return bdt;
    }

    /**
     *
     * @param term STRING - 6-digit code for the semester, year + "10" or "50" or "80"
     * @param subject STRING - Abbreviated title for subject of course
     * @return ts - TreeSet of Faculty objects that displays the faculty members teaching
     * a specific subject during a specific semester. List is alphabetized and duplicates are removed.
     *
     */
    @GetMapping("/facultybysubject")
    public TreeSet<Faculty> facultybysubject (
            @RequestParam(value = "term", defaultValue = "na") String term,
            @RequestParam(value = "subject", defaultValue = "na") String subject){
        TreeSet<Faculty> ts = new TreeSet<Faculty>();

        SemesterSchedule s = coursesbysubject(subject, term);
        CourseInstance[] c = s.getSchedule();
        for (int i = 0; i < s.getSchedule().length; i++) {
            ts.add(c[i].getFaculty());
        }

        return ts;
    }


    /**
     *
     * @param term STRING - 6-digit code for the semester, year + "10" or "50" or "80"
     * @return ts - TreeSet of String objects consisting of all the departments
     * of the University during a specific semester. List is alphabetized and duplicates
     * are removed.
     *
     */
    @GetMapping("/getallsubjects")
    public TreeSet<String> getallsubjects (
            @RequestParam(value = "term", defaultValue = "na") String term){
        SemesterSchedule s = allcourses(term);
        CourseInstance[] c = s.getSchedule();
        TreeSet<String> ts = new TreeSet<String>();

        for (int i = 0; i < s.getSchedule().length; i++) {
            ts.add(c[i].getDEPARTMENT());
        }

        return ts;
    }


}