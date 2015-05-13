package pl.recommendations.db.person;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;
import pl.recommendations.db.Edge;
import pl.recommendations.db.RelationshipType;

@RelationshipEntity(type = RelationshipType.SUGGESTION)
public class SuggestionEdge extends Edge {

    @StartNode
    private PersonNode personNode;

    @EndNode
    private PersonNode suggestion;

    public PersonNode getPersonNode(){
        return personNode;
    }

    public void setPersonNode(PersonNode personNode) {
        this.personNode = personNode;
    }

    public PersonNode getSuggestion(){
        return suggestion;
    }

    public void setSuggestion(PersonNode suggestion) {
        this.suggestion = suggestion;
    }

}
