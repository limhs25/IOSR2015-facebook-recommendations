package pl.recommendations.db;

import pl.recommendations.db.interest.InterestNode;
import pl.recommendations.db.person.PersonNode;

public abstract class EntityFactory {
    protected InterestNode createInterestNode(String name) {
        InterestNode interestEntity = new InterestNode();
        interestEntity.setName(name);

        return interestEntity;
    }

    protected PersonNode createUser(long uuid) {
        PersonNode person = new PersonNode();
        person.setUuid(uuid);

        return person;
    }
}