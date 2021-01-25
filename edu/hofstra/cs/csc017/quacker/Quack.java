package edu.hofstra.cs.csc017.quacker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Quack implements Displayable   // Quack = Tweet
{
    private User   user;
    private String message;
    private Date   date;

    public Quack(User user, String message, Date date)
    {
        this.user    = user;
        this.message = message;
        this.date    = date;
    }

    public User   getUser()          {return user;}
    public String getMessage()       {return message;}
    public Date   getDateQuacked()   {return date;}
    public String getDisplayString() {return "\n" + user.getName() + " quacked:\n" + message + "\non: " + this.dateToString() + "\n";}

    public void editMessage(String newMessage)
    {
        message = newMessage;
        System.out.println("Message changed to: " + message);
    }

    public String dateToString()
    {
        DateFormat usFormat     = new SimpleDateFormat("MM/dd/yyyy");
        String     dateAsString = usFormat.format(date);
        return dateAsString;
    }
}