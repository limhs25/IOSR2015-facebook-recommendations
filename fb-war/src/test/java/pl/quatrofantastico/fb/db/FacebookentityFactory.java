package pl.quatrofantastico.fb.db;

import pl.quatrofantastico.fb.db.model.nodes.FacebookContent;
import pl.quatrofantastico.fb.db.model.nodes.FacebookUser;

public abstract class FacebookentityFactory {
    protected FacebookContent createFacebookContent(String name) {
        FacebookContent user = new FacebookContent();
        user.setName(name);

        return user;
    }

    protected FacebookUser createFacebookUser(String name) {
        FacebookUser user = new FacebookUser();
        user.setName(name);

        return user;
    }
}
