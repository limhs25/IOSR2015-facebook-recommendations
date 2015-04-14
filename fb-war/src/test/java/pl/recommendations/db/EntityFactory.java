package pl.recommendations.db;

import pl.recommendations.db.interest.Interest;
import pl.recommendations.db.person.Person;

public abstract class EntityFactory {
    protected Interest createInterest(long uuid) {
        Interest interest = new Interest();
        interest.setUuid(uuid);

        return interest;
    }

    protected Person createUser(long uuid) {
        Person person = new Person();
        person.setUuid(uuid);

        return person;
    }
}
