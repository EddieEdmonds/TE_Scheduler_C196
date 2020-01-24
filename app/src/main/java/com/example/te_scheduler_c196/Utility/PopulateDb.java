package com.example.te_scheduler_c196.Utility;

import com.example.te_scheduler_c196.DB_Entities.Assessment;
import com.example.te_scheduler_c196.DB_Entities.Course;
import com.example.te_scheduler_c196.DB_Entities.Mentor;
import com.example.te_scheduler_c196.DB_Entities.Note;
import com.example.te_scheduler_c196.DB_Entities.Term;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class PopulateDb {
    private static final String SAMPLE_TITLE = "Sample Term ";
    private static final String SAMPLE_MENTOR = "Sameple Mentor ";
    private static final String SAMPLE_MENTOR_PHONE = "555-555-5555-s";
    private static final String SAMPLE_MENTOR_EMAIL = "SampleMentor@email.com";
    private static final String SAMPLE_COURSE_TITLE = "Sample Course ";
    private static final String SAMPLE_NOTE_TITLE = "Sample Note ";
    private static final String SAMPLE_ASSESSMENT_TITLE = "Sample Assessment ";

    private static Date getDate(int diff){
        GregorianCalendar date = new GregorianCalendar();
        date.add(Calendar.MILLISECOND, diff);
        return date.getTime();
    }

    public static List<Term> popTerms(){
        List<Term> terms = new ArrayList<>();
        terms.add(new Term(SAMPLE_TITLE +"1", getDate(0), getDate(100)));
        terms.add(new Term(SAMPLE_TITLE +"2", getDate(100), getDate(200)));
        System.out.println(terms);
        return terms;
    }

    public static List<Mentor> popMentors(){
        List<Mentor> mentors = new ArrayList<>();
        mentors.add(new Mentor(SAMPLE_MENTOR + "1", SAMPLE_MENTOR_PHONE, SAMPLE_MENTOR_EMAIL));
        mentors.add(new Mentor(SAMPLE_MENTOR + "2", SAMPLE_MENTOR_PHONE, SAMPLE_MENTOR_EMAIL));
        mentors.add(new Mentor(SAMPLE_MENTOR + "3", SAMPLE_MENTOR_PHONE, SAMPLE_MENTOR_EMAIL));

        return mentors;
    }

    public static List<Course> popCourses(){
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(SAMPLE_COURSE_TITLE +"1",getDate(0),getDate(10),"COMPLETE",1,1));
        courses.add(new Course(SAMPLE_COURSE_TITLE +"2",getDate(10),getDate(20),"IN PROGRESS",1,2));
        courses.add(new Course(SAMPLE_COURSE_TITLE +"3",getDate(100),getDate(110),"WAITING",2,2));
        courses.add(new Course(SAMPLE_COURSE_TITLE +"4",getDate(110),getDate(120),"WAITING",2,3));

        return courses;
    }

    public static List<Note> popNotes(){
        List<Note> notes = new ArrayList<>();
        notes.add(new Note(SAMPLE_NOTE_TITLE +"1", "Sample 1 Note Body", 1));
        notes.add(new Note(SAMPLE_NOTE_TITLE +"2", "Sample 2 Note Body", 2));
        notes.add(new Note(SAMPLE_NOTE_TITLE +"3", "Sample 3 Note Body", 2));
        notes.add(new Note(SAMPLE_NOTE_TITLE +"4", "Sample 4 Note Body", 3));
        notes.add(new Note(SAMPLE_NOTE_TITLE +"5", "Sample 5 Note Body", 4));

        return notes;
    }

    public static List<Assessment> popAssessments(){
        List<Assessment> assessments = new ArrayList<>();
        assessments.add(new Assessment("A",SAMPLE_ASSESSMENT_TITLE+"1",getDate(100),getDate(90),1));
        assessments.add(new Assessment("O",SAMPLE_ASSESSMENT_TITLE+"2",getDate(100),getDate(90),1));
        assessments.add(new Assessment("O",SAMPLE_ASSESSMENT_TITLE+"3",getDate(100),getDate(90),2));

        return assessments;
    }

}
