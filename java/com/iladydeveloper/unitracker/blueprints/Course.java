package com.iladydeveloper.unitracker.blueprints;


/**
 * 5. Include the following details for each  course:
 *
 * •  the course title
 *
 * •  the start date
 *
 * •  the anticipated end date
 *
 * •  the status (in progress, completed, dropped, plan to take)
 *
 * •  the course mentor names, phone numbers, and e-mail addresses*/
//perhaps create mentor class and alert class and import mentors and alerts ??

// Courses have Mentors
public class Course {

    private long id;
    private int termID;
    private String title;
    private String start;
    private String end;
    private String status;
    private String mentor1;
    private String phone1;
    private String email1;
    private String mentor2;
    private String phone2;
    private String email2;
    private String optionalNotes;


    // If user checks box, set alert for 24 hours before time in set field.
//    private String course[] = {title, termID, start, end, status, inst1, inst2,
//            o_assess, o_goal_date, p_assess, p_goal_date};//TODO should course be a concatenated String or an array?

    //id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    //title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //associatedTermID
    public int getTermID() {
        return termID;
    }

    public void setTermID(int termID) {
        this.termID = termID;
    }

    //start
    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    //end
    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    //status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getMentor1() {
        return mentor1;
    }

    public void setMentor1(String mentor1) {
        this.mentor1 = mentor1;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getMentor2() {
        return mentor2;
    }

    public void setMentor2(String mentor2) {
        this.mentor2 = mentor2;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getOptionalNotes() {
        return optionalNotes;
    }

    public void setOptionalNotes(String optionalNotes) {
        this.optionalNotes = optionalNotes;
    }


    @Override
    public String toString(){

        return "Course Name: " + getTitle()
                + "\n Ass. Term: " + getTermID()
                + "\n Start: " + getStart()
                + "\n End: " + getEnd()
                + "\n Status: " + getStatus()
                + "\n Mentor 1: " + getMentor1()
                + "\n Phone 1: " + getPhone1()
                + "\n Email 1: " + getEmail1()
                + "\n Mentor 2: " + getMentor2()
                + "\n Phone 2: " + getPhone2()
                + "\n Email 2: " + getEmail2()
                + "\n Optional Notes: " + getOptionalNotes();
        // return String.valueOf( course );
    }

}










