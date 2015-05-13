package pl.recommendations.db.relationships;

import com.google.common.collect.ImmutableSet;
import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;
import pl.recommendations.db.Edge;
import pl.recommendations.db.Node;

import java.util.Set;

@RelationshipEntity
public class NodeRelationship extends Edge {

    @StartNode
    private Node firstNode;

    @EndNode
    private Node secondNode;

    private double value;

    public Set<Node> getNodes() {
        return ImmutableSet.of(firstNode, secondNode);
    }

    public double getValue() {
        return value;
    }

    public void setFirstNode(Node firstNode) {
        this.firstNode = firstNode;
    }

    public void setSecondNode(Node secondNode) {
        this.secondNode = secondNode;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
