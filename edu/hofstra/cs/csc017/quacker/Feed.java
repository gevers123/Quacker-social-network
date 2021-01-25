package edu.hofstra.cs.csc017.quacker;

import java.util.ArrayList;
import java.util.List;

public class Feed implements Displayable
{
    private User        user;
    private String      feedType;
    private String      title;
    private List<Quack> quacks;

    public Feed(User user, String feedType, List<Quack> quacks)
    {
        this.user     = user;
        this.feedType = feedType;
        this.title    = user.getName() + "'s " + feedType + " feed:";
        this.quacks   = quacks;
    }

    public String getDisplayString()
    {
        List<String> quacksAsString = new ArrayList<String>();
        for(Quack quack:quacks)
        {
            quacksAsString.add(quack.getDisplayString());
        }
        return title + quacksAsString;
    }
    public User        getUser()          {return user;}
    public String      getFeedType()      {return feedType;}
    public String      getTitle()         {return title;}
    public List<Quack> getQuacks()        {return quacks;}

    public void setUser     (User newUser)          {user     = newUser;}
    public void setFeedType (String newfeedType)    {feedType = newfeedType;}
    public void setQuacks   (List<Quack> newQuacks) {quacks   = newQuacks;}
}
