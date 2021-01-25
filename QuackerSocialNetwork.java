// Gillian Evers

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Date;
import java.util.HashSet;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import edu.hofstra.cs.csc017.quacker.Displayable;
import edu.hofstra.cs.csc017.quacker.Feed;
import edu.hofstra.cs.csc017.quacker.User;
import edu.hofstra.cs.csc017.quacker.Quack;

class AgeToIntException extends Exception
{
    static final long serialVersionUID = 1;

    public AgeToIntException(String ageAsString)
    {
        super(ageAsString + " is not an age in digits. Couldn't create user on line: ");
    }
}

class WrongDateFormatException extends Exception
{
    static final long serialVersionUID = 1;

    public WrongDateFormatException(String dateAsString)
    {
        super(dateAsString + " is not in the correct date format: MM/dd/yyyy. Couldn't use content from line: ");
    }
}

public class QuackerSocialNetwork   // twitter is bird-themed, quacker is duck-themed
{
    public static void display(Displayable toBeDisplayed)
    {
        System.out.println(toBeDisplayed.getDisplayString());
    }
    
    public static int stringToInt(String ageAsString) throws AgeToIntException
    {
        try
        {
            int age = Integer.parseInt(ageAsString);
            return age;
        }
        catch(NumberFormatException ageToIntException)
        {
            throw new AgeToIntException(ageAsString);
        }
    }

    public static Date stringToDate(String dateAsString) throws WrongDateFormatException
    {
        try
        {
            SimpleDateFormat usFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date             date     = usFormat.parse(dateAsString);
            return date;
        }
        catch(ParseException wrongDateFormatException)
        {
            throw new WrongDateFormatException(dateAsString);
        }
    }

    public static Path getPathToFile(String filename)
    {
        FileSystem defaultFS = FileSystems.getDefault();
        return defaultFS.getPath(filename);
    }

    public static List<String> readAllLinesFromFile(String filename) throws IOException
    {
        Path path = getPathToFile(filename);
        return Files.readAllLines(path);
    }

    public static void displayWrongLengthErrorMessage(String[] info, int correctLength, String objectType, int lineNumber, String correctFormat)
    {
        if(info.length < correctLength)
        {
            System.out.println("Too little information to create " + objectType + " on line: " + lineNumber + "\nFormat to create " + objectType + " is: " + correctFormat);
        }
        else if(info.length > correctLength)
        {
            System.out.println("Too much information provided to create " + objectType + " on line: " + lineNumber + "\nFormat to create " + objectType + " is: " + correctFormat);
        }
    }

    public static List<User> createUserListFromFile(String filename) throws IOException
    {
        List<User>   userList       = new ArrayList<User>();
        List<String> users          = readAllLinesFromFile(filename);
        int          userLineNumber = 1;
        for(String userAsString:users)
        {
            String[] userInfo = userAsString.split("\\|");
            if(userInfo.length != 5)
            {
                displayWrongLengthErrorMessage(userInfo, 5, "user", userLineNumber, "FirstName|LastName|Age|Birthday|Job");
            }
            else
            {
                try
                {
                    String firstName = userInfo[0];
                    String lastName  = userInfo[1];
                    
                    String ageAsString = userInfo[2];
                    int    age         = stringToInt(ageAsString);
                    
                    String birthdayAsString = userInfo[3];
                    Date   birthday         = stringToDate(birthdayAsString);
                    
                    String job = userInfo[4];

                    User user = new User(firstName, lastName, age, birthday, job);
                    userList.add(user);
                }
                catch(AgeToIntException ageToIntException)
                {
                    System.out.println(ageToIntException.getMessage() + userLineNumber);
                }
                catch(WrongDateFormatException wrongDateFormatException)
                {
                    System.out.println("From " +  filename + ": " + wrongDateFormatException.getMessage() + userLineNumber);
                }
            }
            userLineNumber++;
        }
        return userList;
    }

    public static User findUserInUserList(String userName, List<User> userList)
    {
        User correctUser = null;
        for(User user:userList)
        {
            if(user.getName().equals(userName))
            {
                correctUser = user;
            }
        }  
        return correctUser;
    } 

    public static void displayCannotFindUserFromFileErrorMessage(String filename, String userName, int lineNumber)
    {
        System.out.println("From " + filename + ": Cannot find user to match: " + userName + " on line: " + lineNumber);
    }

    public static void createRelationshipsFromFileAndUserList(String filename, List<User> userList) throws IOException
    {
        List<String> relationships          = readAllLinesFromFile(filename);
        int          relationshipLineNumber = 1;
        for(String relation:relationships)
        {
            String[] relationshipInfo = relation.split("\\|");
            if(relationshipInfo.length != 4)
            {
                displayWrongLengthErrorMessage(relationshipInfo, 4, "relationship", relationshipLineNumber, "FollowerFirstName|FollowerLastName|ToBeFollowedFirstName|ToBeFollowedLastName");
            }
            else
            {
                String followerFirstName     = relationshipInfo[0];
                String followerLastName      = relationshipInfo[1];
                String followerName          = followerFirstName + " " + followerLastName;
                String toBeFollowedFirstName = relationshipInfo[2];
                String toBeFollowedLastName  = relationshipInfo[3];
                String toBeFollowedName      = toBeFollowedFirstName + " " + toBeFollowedLastName;

                String correctFollower = followerFirstName + " " + followerLastName;
                User   follower        = findUserInUserList(correctFollower, userList);
                
                String correctToBeFollowed = toBeFollowedFirstName + " " + toBeFollowedLastName;
                User   toBeFollowed        = findUserInUserList(correctToBeFollowed, userList);
                
                if(follower == null)
                {
                    displayCannotFindUserFromFileErrorMessage(filename, followerName, relationshipLineNumber);
                }
                else if(toBeFollowed == null)
                {
                    displayCannotFindUserFromFileErrorMessage(filename, toBeFollowedName, relationshipLineNumber);
                }
                else
                {
                    follower.follow(toBeFollowed);
                }  
            }
            relationshipLineNumber++;
        }
    }

    public static List<Quack> createQuackListFromFileAndUserList(String filename, List<User> userList) throws IOException
    {
        List<String> quacks          = readAllLinesFromFile(filename);
        int          quackLineNumber = 1;
        List<Quack>  quackList       = new ArrayList<Quack>();
        for(String quackAsString:quacks)
        {
            String[] quackInfo = quackAsString.split("\\|");
            if(quackInfo.length != 3)
            {
                displayWrongLengthErrorMessage(quackInfo, 3, "quack", quackLineNumber, "User|Message|Date");
            }
            else
            {
                try
                {
                    String userName = quackInfo[0];
                    String message  = quackInfo[1];
                    
                    String dateAsString = quackInfo[2];
                    Date   date         = stringToDate(dateAsString);
                    
                    User correctUser = findUserInUserList(userName, userList);   
                    if(correctUser == null)
                    {
                        displayCannotFindUserFromFileErrorMessage(filename, userName, quackLineNumber);
                    }
                    else
                    {
                        Quack quack = new Quack(correctUser, message, date);
                        correctUser.quack(quack);
                        quackList.add(quack);
                    }
                }
                catch(WrongDateFormatException wrongDateFormatException)
                {
                    System.out.println("From " + filename + ": " + wrongDateFormatException.getMessage() + quackLineNumber);
                }
            }
            quackLineNumber++;
        }
        return quackList;
    }

    public static List<Quack> createQuackListForFeed(List<User> usersInFeed, List<Quack> quackList)
    {
        List<Quack> feedQuackList = new ArrayList<Quack>();
        int         counter       = 0;
        for(Quack quack:quackList)
        {
            for(User userInFeed:usersInFeed)
            {
                if(quack.getUser().equals(userInFeed) && counter < 10)
                {
                    feedQuackList.add(quack);
                    counter++;
                }
            }
        }
        return feedQuackList;
    }

    public static void createMainFeedForUser(User user, List<Quack> quackList) 
    {
        List<Quack> mainQuackList = createQuackListForFeed(user.getFollowed(), quackList);
        Feed        mainFeed      = new Feed(user, "main", mainQuackList);
        display(mainFeed);
    }

    public static Set<User> createAllFollowedUsersOfFollowedUserSet(List<User> followedList)
    {
        Set<User> allFollowedUsersOfFollowedUsers = new HashSet<User>();
        for(User followedUser:followedList)
        {
            List<User> followedUsersOfFollowedUser = followedUser.getFollowed();
            for(User followedUserOfFollowedUser:followedUsersOfFollowedUser)
            {
                allFollowedUsersOfFollowedUsers.add(followedUserOfFollowedUser);
            }
        }
        return allFollowedUsersOfFollowedUsers;
    }

    public static List<User> filterToRecommendedUsers(User user, List<User> followedList, Set<User> allFollowedUsersOfFollowedUsers)
    {
        Set<User>  recommendedUsersAsSet  = allFollowedUsersOfFollowedUsers.stream().filter(followedUserOfFollowedUser -> !(followedList.contains(followedUserOfFollowedUser)||followedUserOfFollowedUser == user)).collect(Collectors.toSet());
        List<User> recommendedUsersAsList = new ArrayList<>(recommendedUsersAsSet);
        return recommendedUsersAsList;
    }

    public static void createRecommendedFeedForUser(User user, List<Quack> quackList)
    {
        List<User>  followedList                    = user.getFollowed();
        Set<User>   allFollowedUsersOfFollowedUsers = createAllFollowedUsersOfFollowedUserSet(followedList);
        List<User>  recommendedUsers                = filterToRecommendedUsers(user, followedList, allFollowedUsersOfFollowedUsers);
        List<Quack> recommendedQuackList            = createQuackListForFeed(recommendedUsers, quackList);
        Feed        recommendedFeed                 = new Feed(user, "recommended", recommendedQuackList);
        display(recommendedFeed);
    }

    public static User getRandomUserFromUserList(List<User> userList)
    {
        Random random     = new Random();
        User   randomUser = userList.get(random.nextInt(userList.size()));
        return randomUser;
    }
    public static void main(String[] arguments)
    {
        try
        {
            List<User> userList = createUserListFromFile("users.txt");
            createRelationshipsFromFileAndUserList("relationships.txt", userList);
            List<Quack> quackList = createQuackListFromFileAndUserList("quacks.txt", userList);

            User loggedInUser = getRandomUserFromUserList(userList);
            createMainFeedForUser(loggedInUser, quackList);
            createRecommendedFeedForUser(loggedInUser, quackList);
        }  
        catch(NoSuchFileException noSuchFileException)
        {
            System.out.println("Could not find the file: " + noSuchFileException.getFile());
            System.exit(1);
        }
        catch(IOException ioException)
        {
            System.out.println(ioException.getMessage());
        }
    }
}