package pl.recommendations.controller.data;

import org.springframework.web.multipart.MultipartFile;
import pl.recommendations.exceptions.GettingInputStreamException;

import java.io.IOException;
import java.io.InputStream;

public class SingleDatabaseFile {
    private MultipartFile edges;

    public InputStream getPeopleEdgesStream()  {
        try {
            return edges.getInputStream();
        }catch (IOException e){
            throw new GettingInputStreamException(e);
        }
    }

    public MultipartFile getEdges() {
        return edges;
    }

    public void setEdges(MultipartFile edges) {
        this.edges = edges;
    }
}
