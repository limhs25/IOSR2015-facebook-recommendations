package pl.quatrofantastico.fb.db.model;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class FacebookContent {
    @GraphId
    private Long id;

    private String name;

    @Override
    public boolean equals(Object o) {
        if(this == o)return true;
        if(!(o instanceof FacebookContent)) return false;
        return ((FacebookContent) o).name.equals(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
