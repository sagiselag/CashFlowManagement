package il.ac.hit.CashFlowManagement.model;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  The Expense class
 */
public class Expense
{
    private Date date;
    private String classification, details;
    private double cost;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Expense C'tor
     * @param iDateStr Date of new expense, can't be null
     * @param iClassification Classification of new expense, can't be null
     * @param iDetails New expense details, can't be null
     * @param iCost New expense cost, can't be null
     */
    public Expense(@NotNull String iDateStr, @NotNull String iClassification, @NotNull String iDetails, @NotNull double iCost) {
        try{
            setDate(simpleDateFormat.parse(iDateStr));
            setClassification(iClassification);
            setDetails(iDetails);
            setCost(iCost);
        } catch(ParseException pe) {
            throw new IllegalArgumentException(pe);
        }
    }

    /**
     * getter for expense classification
     * @return string which describes the expense classification
     */
    public String getClassification() {
        return classification;
    }

    /**
     * setter for expense classification
     * @param classification classification to set
     */
    public void setClassification(String classification) {
        this.classification = classification;
    }

    /**
     * getter for expense details
     * @return string which describes the expense details
     */
    public String getDetails() {
        return details;
    }

    /**
     * setter for expense details
     * @param details details to set
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * getter for expense cost
     * @return the expense cost as a float number
     */
    public double getCost() {
        return cost;
    }

    /**
     * setter for expense cost
     * @param cost cost to set
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * getter for expense date
     * @return the expense date
     */
    public Date getDate() {
        return date;
    }

    /**
     * setter for expense date
     * @param date date to set (as a Date)
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * setter for expense date
     * @param date date to set  (as a String)
     */
    public void setDate(String date) {
        try {
            setDate(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
