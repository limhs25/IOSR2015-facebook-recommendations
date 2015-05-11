package pl.recommendations.db.interest;

import com.google.common.collect.ImmutableSet;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import pl.recommendations.db.RelationshipType;
import pl.recommendations.db.interest.relationships.Contrast;
import pl.recommendations.db.interest.relationships.Similarity;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class InterestEntity {
    @GraphId
    private Long graphID;

    @Indexed(unique = true)
<<<<<<< HEAD:fb-war/src/main/java/pl/recommendations/db/interest/Interest.java
    private Long uuid;
=======
>>>>>>> 2970d6393f3fc719ea5af4dd6a1e5192a2a92647:fb-war/src/main/java/pl/recommendations/db/interest/InterestEntity.java
    private String name;

    @RelatedToVia(type = RelationshipType.SIMILARITY, direction = Direction.BOTH)
    private Set<Similarity> similarities = new HashSet<>();

    @RelatedToVia(type = RelationshipType.CONTRAST, direction = Direction.BOTH)
    private Set<Contrast> contrasts = new HashSet<>();

    public void addContrast(InterestEntity that, double v) {
        Contrast contrast = new Contrast();

        contrast.setFirstInterest(this);
        contrast.setSecondInterest(that);
        contrast.setValue(v);

        contrasts.add(contrast);
    }

    public void addSimilarity(InterestEntity that, double v) {
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

        InterestEntity that = (InterestEntity) o;

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

<<<<<<< HEAD:fb-war/src/main/java/pl/recommendations/db/interest/Interest.java
    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return graphID;
=======
    public void setName(String name) {
        this.name = name;
>>>>>>> 2970d6393f3fc719ea5af4dd6a1e5192a2a92647:fb-war/src/main/java/pl/recommendations/db/interest/InterestEntity.java
    }

    public Set<Contrast> getContrasts() {
        return ImmutableSet.copyOf(contrasts);
    }

    public Set<Similarity> getSimilarities() {
        return ImmutableSet.copyOf(similarities);
    }

}
