package com.thereseparks.familyintouch.Model.Models;

/**The Relation Table needs to have pre-set data with four choices with set id's
 * 1: Immediate Family 2. Extended Family 3. Family_In_Law 4. Other Family */
public class Relation_Status {


    //variables
    private long id; //db var is called id //PK
    private int relationStatus_Id;
    private String relationTitle;
    private String relationNumber; // string because we're only using for searching, no manipulation

    public String getRelationNumber() {
        return relationNumber;
    }

    public void setRelationNumber(String relationNumber) {
        this.relationNumber = relationNumber;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRelationStatus_Id() {
        return relationStatus_Id;
    }

    public void setRelationStatus_Id(int relationStatus_Id) {
        this.relationStatus_Id = relationStatus_Id;
    }

    public String getRelationTitle() {
        return relationTitle;
    }

    public void setRelationTitle(String relationTitle) {
        this.relationTitle = relationTitle;
    }


    @Override
    public String toString() {
        return "Relation_Status{" +
                "id=" + id +
                ", relationStatus_Id=" + relationStatus_Id +
                ", relationTitle='" + relationTitle + '\'' +
                ", relationNumber='" + relationNumber + '\'' +
                '}';
    }



}
