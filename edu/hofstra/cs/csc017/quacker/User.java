package edu.hofstra.cs.csc017.quacker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User implements Displayable
{
    private String      first;
    private String      last;
    private String      name;
    private int         age;
    private Date        birthday;
    private String      job;
    private List<User>  follows;
    private List<Quack> quacks;
    
    public User(String first, String last, int age, Date birthday, String job)
    {
        this.first    = first;
        this.last     = last;
        this.name     = first + " " + last;
        this.age      = age;
        this.birthday = birthday;
        this.job      = job;
        this.follows  = new ArrayList<User>();
        this.quacks   = new ArrayList<Quack>();
    }

    public String      getName()          {return name;}
    public int         getAge()           {return age;}
    public Date        getBirthday()      {return birthday;}
    public String      getJob()           {return job;}
    public String      getDisplayString() {return this.getName();}
    public List<User>  getFollowed()      {return follows;}
    public List<Quack> getQuacks()        {return quacks;}
    public void        follow(User user)  {this.follows.add(user);}
    
    public List<String> getFollowedAsString() 
    {
        List<String> followedAsString = new ArrayList<String>();
        for(User followed:follows)
        {
            followedAsString.add(followed.getName());
        }
        return followedAsString;
    }

    public void setName(String newFirst, String newLast)
    {
        first = newFirst;
        last  = newLast;
        System.out.println("Name set to: " + first + " " + last);
    }

    public void setAge(int newAge)
    {
        age = newAge;
        System.out.println("Age set to: " + age);
    }

    public void setBirthday(Date newBirthday)
    {
        birthday = newBirthday;
        System.out.println("Birthday set to: " + birthday);
    }

    public void setJob(String newJob)
    {
        job = newJob;
        System.out.println("Job set to: " + job);
    }

    public void quack(Quack newQuack)
    {
        quacks.add(newQuack);
    }
}