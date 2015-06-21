package pl.recommendations.controller.data;

import org.springframework.web.multipart.MultipartFile;
import pl.recommendations.exceptions.GettingInputStreamException;

import java.io.IOException;
import java.io.InputStream;

public class CustomDatabaseFiles  extends DatabaseFile{
    private MultipartFile peopleNodes;
    private MultipartFile peopleEdges;

    public InputStream getPeopleNodesStream()  {
        try {
            return peopleNodes.getInputStream();
        }catch (IOException e){
            throw new GettingInputStreamException(e);
        }
    }

   public InputStream getPeopleEdgesStream()  {
        try {
            return peopleEdges.getInputStream();
        }catch (IOException e){
            throw new GettingInputStreamException(e);
        }
    }

       public MultipartFile getPeopleNodes() {
        return peopleNodes;
    }

    public void setPeopleNodes(MultipartFile peopleNodes) {
        this.peopleNodes = peopleNodes;
    }


    public MultipartFile getPeopleEdges() {
        return peopleEdges;
    }

    public void setPeopleEdges(MultipartFile peopleEdges) {
        this.peopleEdges = peopleEdges;
    }

}
