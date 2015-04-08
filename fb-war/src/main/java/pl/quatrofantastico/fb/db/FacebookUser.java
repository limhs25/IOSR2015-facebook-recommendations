package pl.quatrofantastico.fb.db;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class FacebookUser {
    @GraphId
    private Long id;

    private String name;

    @Override
    public boolean equals(Object o) {
        if(this == o)return true;
        if(!(o instanceof FacebookUser)) return false;
        return ((FacebookUser) o).name.equals(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
