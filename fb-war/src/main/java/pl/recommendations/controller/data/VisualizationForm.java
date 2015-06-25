package pl.recommendations.controller.data;

public class VisualizationForm {
    private String count = "2";
    private String maxNodes = "50";

    public Long getCountLong() {
        return Long.parseLong(count);
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Long getMaxNodesLong() {
        return Long.parseLong(maxNodes);
    }

    public void setMaxNodes(String maxNodes) {
        this.maxNodes = maxNodes;
    }

    public String getCount() {
        return count;
    }

    public String getMaxNodes() {
        return maxNodes;
    }

    @Override
    public String toString() {
        return count + " " + maxNodes;
    }
}
