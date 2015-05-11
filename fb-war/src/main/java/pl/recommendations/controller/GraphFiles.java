package pl.recommendations.controller;

import org.springframework.web.multipart.MultipartFile;
import pl.recommendations.exceptions.GettingInputStreamException;

import java.io.IOException;
import java.io.InputStream;

public class GraphFiles {
    private MultipartFile peopleNodes;
    private MultipartFile interestNodes;
    private MultipartFile peopleEdges;
    private MultipartFile interestEdges;

    private String separator;

    public InputStream getPeopleNodesStream()  {
        try {
            return peopleNodes.getInputStream();
        }catch (IOException e){
            throw new GettingInputStreamException(e);
        }
    }

    public InputStream getInterestNodesStream()  {
        try {
            return interestNodes.getInputStream();
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

    public InputStream getInterestEdgesStream()  {
        try {
            return interestEdges.getInputStream();
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

    public MultipartFile getInterestNodes() {
        return interestNodes;
    }

    public void setInterestNodes(MultipartFile interestNodes) {
        this.interestNodes = interestNodes;
    }

    public MultipartFile getPeopleEdges() {
        return peopleEdges;
    }

    public void setPeopleEdges(MultipartFile peopleEdges) {
        this.peopleEdges = peopleEdges;
    }

    public MultipartFile getInterestEdges() {
        return interestEdges;
    }

    public void setInterestEdges(MultipartFile interestEdges) {
        this.interestEdges = interestEdges;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }
}
