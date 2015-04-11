package pl.recommendations.db;

import pl.recommendations.db.interest.Interest;
import pl.recommendations.db.user.User;

public abstract class FacebookentityFactory {
    protected Interest createInterest(String name) {
        Interest interest = new Interest();
        interest.setName(name);

        return interest;
    }

    protected User createUser(String name) {
        User user = new User();
        user.setName(name);

        return user;
    }
}
