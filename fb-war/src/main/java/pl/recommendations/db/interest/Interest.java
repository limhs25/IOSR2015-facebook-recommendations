package pl.recommendations.db.interest;

import com.google.common.collect.ImmutableSet;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import pl.recommendations.db.RelationshipType;
import pl.recommendations.db.interest.relationships.Contrast;
import pl.recommendations.db.interest.relationships.Similarity;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Interest {
    @GraphId
    private Long graphID;

    private String name;

    @RelatedToVia(type = RelationshipType.SIMILARITY, direction = Direction.BOTH)
    private Set<Similarity> similarities = new HashSet<>();

    @RelatedToVia(type = RelationshipType.CONTRAST, direction = Direction.BOTH)
    private Set<Contrast> contrasts = new HashSet<>();

    public void addContrast(Interest that, double v) {
        Contrast contrast = new Contrast();

        contrast.setFirstInterest(this);
        contrast.setSecondInterest(that);
        contrast.setValue(v);

        contrasts.add(contrast);
    }

    public void addSimilarity(Interest that, double v) {
        Similarity similarity = new Similarity();

        similarity.setFirstInterest(this);
        similarity.setSecondInterest(that);
        similarity.setValue(v);

        similarities.add(similarity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Interest interest = (Interest) o;

        if (name != null ? !name.equals(interest.name) : interest.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public Long getId() {
        return graphID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Contrast> getContrasts() {
        return ImmutableSet.copyOf(contrasts);
    }

    public Set<Similarity> getSimilarities() {
        return ImmutableSet.copyOf(similarities);
    }

}
