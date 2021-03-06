package pl.recommendations.db;

import com.google.common.collect.ImmutableSet;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import org.springframework.data.neo4j.core.GraphDatabase;
import pl.recommendations.db.relationships.Contrast;
import pl.recommendations.db.relationships.Similarity;

import javax.transaction.TransactionManager;
import java.util.HashSet;
import java.util.Set;

@NodeEntity
public abstract class Node {
    @GraphId
    protected Long graphID;

    @RelatedToVia(type = RelationshipType.SIMILARITY, direction = Direction.BOTH)
    private Set<Similarity> similarities = new HashSet<>();

    @RelatedToVia(type = RelationshipType.CONTRAST, direction = Direction.BOTH)
    private Set<Contrast> contrasts = new HashSet<>();

    public void addSimilarity(Node that, double v) {
        Similarity similarity = new Similarity();

        similarity.setFirstNode(this);
        similarity.setSecondNode(that);
        similarity.setValue(v);

        similarities.add(similarity);
    }

    public void addContrast(Node that, double v) {
        Contrast contrast = new Contrast();

        contrast.setFirstNode(this);
        contrast.setSecondNode(that);
        contrast.setValue(v);

        contrasts.add(contrast);
    }

    public Long getId() {
        return graphID;
    }

    public Set<Similarity> getSimilarities() {
        return ImmutableSet.copyOf(similarities);
    }

    public Set<Contrast> getContrasts() {
        return ImmutableSet.copyOf(contrasts);
    }
}
