package com.iladydeveloper.unitracker.blueprints;


/**
 * 1. Include the following information for each  term:
 *
 * •  the term title (e.g., Term 1, Term 2, Spring Term)
 *
 * •  the start date
 *
 * •  the end date
 */
//Terms have Courses
public class Term {

    //variables
    private String id;

    private String termTitle;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTermTitle() {
        return termTitle;
    }

    public void setTermTitle(String termTitle) {
        this.termTitle = termTitle;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String[] getTerm() {
        return term;
    }

    public void setTerm(String[] term) {
        this.term = term;
    }

    private String startDate;
    private String endDate;

    //  private String term;
    String term[] = {termTitle, startDate, endDate };
    // public String term = termTitle + " " + startDate + " " + endDate;//TODO should course be a concatenated String or an array?







    @Override
    public String toString(){
      return String.valueOf( term );
    }

}


