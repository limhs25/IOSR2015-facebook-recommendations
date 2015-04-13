package pl.recommendations.db;

import pl.recommendations.db.interest.Interest;
import pl.recommendations.db.user.User;

public abstract class EntityFactory {
    protected Interest createInterest(long uuid) {
        Interest interest = new Interest();
        interest.setUuid(uuid);

        return interest;
    }

    protected User createUser(long uuid) {
        User user = new User();
        user.setUuid(uuid);

        return user;
    }
}
