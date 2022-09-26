package edu.tntech.csc2310.dashboard;
import edu.tntech.csc2310.dashboard.data.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ServiceBridgeTest {

    private ServiceBridge bridge;

    @Test
    public void coursesExist() {
        ServiceBridge bridge = new ServiceBridge("202210", "CSC");
        assertTrue( "courses exist",bridge.getCourses().length > 0);
        bridge = new ServiceBridge("202210", "csc");
        assertTrue( "courses exist",bridge.getCourses().length > 0);
        bridge = new ServiceBridge("202210", "cSc");
        assertTrue( "courses exist", bridge.getCourses().length > 0);
    }

    @Test
    public void coursesDontExist(){
        ServiceBridge bridge = new ServiceBridge("202210", "Jerry");
        CourseInstance[] ci = bridge.getCourses();
        assertEquals("course does not exist", 0, ci.length);
    }

    @Test
    public void courseFound(){
        ServiceBridge bridge = new ServiceBridge("202180", "UNIV");
        CourseInstance[] ci = bridge.getCourses();
        boolean found = false;
        for (CourseInstance co: ci){
            if (co.getCOURSE().contentEquals("1030"))
                found = true;
        }
        assertTrue("course found", found);
    }

    @Test
    public void termDoesNotExist(){
        ServiceBridge bridge = new ServiceBridge("202380", "UNIV");
        CourseInstance[] ci = bridge.getCourses();
        assertEquals("term does not exist",0, ci.length);
    }

    @Before
    public void setUp() throws Exception {
        bridge = new ServiceBridge("202210", "CSC");
    }

    @Test
    public void serviceB() {
        CourseInstance[] result = bridge.ServiceB("202210", "CSC");
        assertTrue("Course instance array created", result.length > 0);

        result = bridge.ServiceB("202210", "csC");
        assertTrue("Course instance array created", result.length > 0);

        result = bridge.ServiceB("202210", "csc");
        assertTrue("Course instance array created", result.length > 0);
    }

    @Test
    public void noServiceB(){
        CourseInstance [] result = bridge.ServiceB("195508", "JAZZ");
        assertTrue("Unhappy path", result.length == 0);

    }

    @Test
    public void getCourses() {
        CourseInstance[] schedule = bridge.ServiceB("202180", "");
        SubjectTerm subjectTerm = new SubjectTerm("202180", "ALL");
        SemesterSchedule s = new SemesterSchedule(subjectTerm, schedule);
    }

    @Test
    public void allcourses() {
        CourseInstance[] schedule = bridge.ServiceB("202180", "");
        SubjectTerm subjectTerm = new SubjectTerm("202180", "ALL");
        SemesterSchedule s = new SemesterSchedule(subjectTerm, schedule);
        assertTrue("Allcourses is proper number of courses.", s.getSchedule().length > 3700 && s.getSchedule().length < 4000);

        schedule = bridge.ServiceB("201310", "");
        subjectTerm = new SubjectTerm("201310", "ALL");
        s = new SemesterSchedule(subjectTerm, schedule);
        assertTrue("Allcourses is proper number of courses.", s.getSchedule().length > 3250 && s.getSchedule().length < 3400);
    }

    @Test
    public void wrongallcourses(){
        CourseInstance[] schedule = bridge.ServiceB("Megadeth", "Slayer");
        SubjectTerm subjectTerm = new SubjectTerm("Megadeth", "Slayer");
        SemesterSchedule s = new SemesterSchedule(subjectTerm, schedule);
        assertTrue("Unhappy path.", s.getSchedule().length == 0);
    }

    @Test
    public void coursesbysubject() {
        CourseInstance[] schedule = bridge.ServiceB("202210", "csc");
        SubjectTerm subjectTerm = new SubjectTerm("202210", "csc");
        SemesterSchedule s = new SemesterSchedule(subjectTerm, schedule);
        assertTrue("Terms match.", s.getSubjectTerm().getSubject().equalsIgnoreCase("csc"));
        assertTrue("Courses by subject has adequate length.", s.getSchedule().length > 110 && s.getSchedule().length < 140);
    }

    @Test
    public void baddatacoursesbysubject(){
        assertTrue(bridge.coursesbysubject("Metallica", "Judas_Priest").getSchedule().length == 0);
    }

    @Test
    public void coursesbyfaculty() {
        assertTrue(bridge.coursesbyfaculty("CSC", "202210", "Gannod", "Gerald C").length == 8);
        assertTrue(bridge.coursesbyfaculty("CSc", "202080", "bruMMett", "Travis s").length == 10);
    }

    @Test
    public void baddatacoursesbyfaculty(){
        assertTrue("Unhappy Path", bridge.coursesbyfaculty("Black_Sabbath", "Travis_Tritt", "Manson", "Charles").length == 0);
    }

    @Test
    public void testcoursebysection() {
        assertTrue(bridge.coursebysection("CSC", "202210", "003", "3710").getFaculty().getLastname().equals("Elizandro"));
        assertTrue(bridge.coursebysection("MatH", "202210", "001", "1010").getFaculty().getLastname().equals("Bryant"));
    }

    @Test
    public void nocoursesbysection(){
        assertNull("Unhappy Path.", bridge.coursebysection("Bluegrass", "195110", "501", "Monroe"));
    }


    @Test
    public void testschbydepartment() {
        assertTrue(bridge.schbydepartment("CSC", "201910").getCreditHoursGenerated() >= 4000 && bridge.schbydepartment("CSC", "201910").getCreditHoursGenerated() <= 5000);
        assertTrue(bridge.schbydepartment("CSC", "201750").getCreditHoursGenerated() >= 150 && bridge.schbydepartment("CSC", "201750").getCreditHoursGenerated() <= 200);
    }

    @Test
    public void noschbydepartment(){
        assertTrue(bridge.schbydepartment("Fender", "Epiphone").getCreditHoursGenerated() == 0);
        assertTrue("Get subject term,", bridge.schbydepartment("Fender", "Epiphone").getSubjectTerm() == null);
    }

    @Test
    public void schbyfaculty() {
        assertTrue(bridge.schbyfaculty("CSC", "202210", "Gannod", "Gerald c").getCreditHoursGenerated() >= 330 && bridge.schbyfaculty("CSC", "202210", "Gannod", "Gerald").getCreditHoursGenerated() <= 360);
        assertTrue(bridge.schbyfaculty("CSC", "202180", "ElizandrO", "daviD w").getCreditHoursGenerated() >= 165 && bridge.schbyfaculty("CSC", "202180", "Elizandro", "David").getCreditHoursGenerated() <= 200);
    }

    @Test
    public void noschbyfaculty(){
        assertTrue(bridge.schbyfaculty("Waylon", "Carter", "Vern", "George").getCreditHoursGenerated() == 0);
    }

    @Test
    public void termmonthadder() {
        assertTrue(bridge.termmonthadder(10) == 50);
        assertTrue(bridge.termmonthadder(50) == 80);
        assertTrue(bridge.termmonthadder(80) == 10);
        assertTrue(bridge.termmonthadder(90) == 90);
    }

    @Test
    public void testcoursesbycrnlist() {
        assertTrue(bridge.coursesbycrnlist("202210", "10986").size() == 1);
        assertTrue(bridge.coursesbycrnlist("201980", "84180,84703,81276,81118,82365,82369").size() == 6);
    }

    @Test
    public void nocoursesbycrnlist(){
        assertTrue("Somewhat unhappy path", bridge.coursesbycrnlist("202210", "10986,Miles").size() == 1);
        assertTrue("Sad path :(", bridge.coursesbycrnlist("Coltrane", "Rollins,Miles").size() == 0);
    }

    @Test
    public void testfacultybysubject() {

        assertTrue(bridge.facultybysubject("202210", "CSC").size() > 22 && bridge.facultybysubject("202210", "CSC").size() < 27);
        assertTrue(bridge.facultybysubject("200910", "hist").size() > 15 && bridge.facultybysubject("200910", "hist").size() < 20);
    }

    @Test
    public void nofacultybysubject(){
        assertTrue(bridge.facultybysubject("d", "d").size() == 0);
        assertTrue(bridge.facultybysubject("d", "CSC").size() == 0);}

    @Test
    public void testSchbydepartmentandterms() {
        assertTrue(bridge.schbydepartmentandterms("csc", "20", "201510", "202080").size() == 18);
        assertTrue(bridge.schbydepartmentandterms("csc", "d", "201480", "202080").get(4).getCreditHoursGenerated() > 3000);
    }

    @Test
    public void noSchbydepartmentandterms(){
        assertTrue("Unhappy Path", bridge.schbydepartmentandterms("Giant", "Steps", "Kind", "ofBlue").size() == 0);
        assertTrue("Unhappy Path, but proper terms", bridge.schbydepartmentandterms("Giant", "Steps", "201550", "201950").size() == 13);
    }

    @Test
    public void schbyfacultyandterms() {
        assertTrue(bridge.schbyfacultyandterms("CSC", "202210", "Gannod", "Gerald c", "201680", "202180").get(6).getCreditHoursGenerated() > 280);
        assertTrue(bridge.schbyfacultyandterms("CSC", "202210", "Gannod", "Gerald c", "201780", "202180").size() > 7);
    }

    @Test
    public void noschbyfacultyandterms(){
        assertTrue("Wrong subject", bridge.schbyfacultyandterms("math", "202210", "Gannod", "Gerald c", "201680", "202180").get(15).getCreditHoursGenerated() == 0);
        assertTrue("Unhappy Path", bridge.schbyfacultyandterms("He", "stopped", "loving", "her", "today", ".").size() == 0);
    }

    @Test
    public void schbydepartmentandtermlist() {
        assertTrue(bridge.schbydepartmentandtermlist("CSC", "201980", "201910,201580,201650,201750,201680").size() == 5);
        assertTrue(bridge.schbydepartmentandtermlist("CSC", "201980", "201910,201580,201650,201750,201680").get(4).getCreditHoursGenerated() > 3000 && bridge.schbydepartmentandtermlist("CSC", "201980", "201910,201580,201650,201750,201680").get(4).getCreditHoursGenerated() < 3500);
    }

    @Test
    public void noschbydepartmentandtermlist() {
        assertTrue("Error Path; only proper semester terms in list specified.",bridge.schbydepartmentandtermlist("d", "d", "200850,202210,201980").size() == 2);
        assertTrue("Unhappy Path,",bridge.schbydepartmentandtermlist("d", "d", "d,2d").size() == 0);
    }


    @Test
    public void getallsubjects() {
        assertTrue(bridge.getallsubjects("201910").size() > 150 && bridge.getallsubjects("201910").size() < 160);
    }

    @Test
    public void nogetallsubjects(){
        assertTrue(bridge.getallsubjects("m").size() == 0);
    }

    @Test
    public void schbyfacultyandtermlist() {
        assertTrue("Happy path.", bridge.schbyfacultyandtermlist("csc", "d", "Gannod", "Gerald C", "202050,202180,201950,201850,201750,196090").size() == 5);
        assertTrue("Happy path, but with erroneous term for last in list.", bridge.schbyfacultyandtermlist("csc", "d", "Gannod", "Gerald C", "202050,202180,201950,201850,201750,196090").size() == 5);
    }

    @Test
    public void noschbyfacultyandtermlist(){
        assertTrue(bridge.schbyfacultyandtermlist("Hank Williams, Sr", "", "Gannod", "Gerald C", "194750, 195310").size() == 0);
        assertTrue(bridge.schbyfacultyandtermlist("Blues", "", "Gannod", "Gerald c", "201510, 201550").size() == 0);
    }
}