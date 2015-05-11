package pl.recommendations.controller;

import java.io.File;

public class GraphFiles {
    private File peopleNodes;
    private File interestNodes;
    private File peopleRelations;
    private File interestRelations;

    public File getPeopleNodes() {
        return peopleNodes;
    }

    public void setPeopleNodes(File peopleNodes) {
        this.peopleNodes = peopleNodes;
    }

    public File getInterestNodes() {
        return interestNodes;
    }

    public void setInterestNodes(File interestNodes) {
        this.interestNodes = interestNodes;
    }

    public File getPeopleRelations() {
        return peopleRelations;
    }

    public void setPeopleRelations(File peopleRelations) {
        this.peopleRelations = peopleRelations;
    }

    public File getInterestRelations() {
        return interestRelations;
    }

    public void setInterestRelations(File interestRelations) {
        this.interestRelations = interestRelations;
    }
}
