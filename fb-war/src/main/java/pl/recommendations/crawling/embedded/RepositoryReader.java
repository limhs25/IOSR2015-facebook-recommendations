package pl.recommendations.crawling.embedded;

import pl.recommendations.db.person.FriendshipEdge;
import pl.recommendations.db.person.FriendshipType;
import pl.recommendations.db.person.PersonNode;

import java.util.Random;

public class RepositoryReader {
    public static double dropRatio = 0.33;
    private static Random random = new Random();;

    public static FriendshipEdge createFriendship(PersonNode person, PersonNode friend) {
        FriendshipEdge friendship = new FriendshipEdge();
        friendship.setPersonNode(person);
        friendship.setFriend(friend);

        if(random.nextDouble() < dropRatio){
            friendship.setType(FriendshipType.RETAINED);
        }else{
            friendship.setType(FriendshipType.COMMON);

        }

        return friendship;
    }

    public static void setDropRation(String value) {
        dropRatio = Double.parseDouble(value);
    }

}
