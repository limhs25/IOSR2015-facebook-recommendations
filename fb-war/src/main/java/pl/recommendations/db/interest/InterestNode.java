package pl.recommendations.db.interest;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import pl.recommendations.db.Node;
import pl.recommendations.db.person.InterestEdge;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class InterestNode extends Node {

    @Indexed(unique = true)
    private String name;

    @RelatedToVia(direction = Direction.INCOMING)
    private Set<InterestEdge> interestEdges = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InterestNode that = (InterestNode) o;

        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
