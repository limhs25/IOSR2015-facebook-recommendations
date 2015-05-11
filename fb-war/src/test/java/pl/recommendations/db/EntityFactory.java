package pl.recommendations.db;

import pl.recommendations.db.interest.InterestEntity;
import pl.recommendations.db.person.Person;

public abstract class EntityFactory {
    protected InterestEntity createInterest(String name) {
        InterestEntity interestEntity = new InterestEntity();
        interestEntity.setName(name);

        return interestEntity;
    }

    protected Person createUser(long uuid) {
        Person person = new Person();
        person.setUuid(uuid);

        return person;
    }
}
