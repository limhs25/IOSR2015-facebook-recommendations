package pl.recommendations.controller.data;

public abstract class DatabaseFile {
    protected String dropRate;

    public String getDropRate() {
        return dropRate;
    }

    public void setDropRate(String dropRate) {
        this.dropRate = dropRate;
    }

    public double getNumericDropRate(){
        return Double.parseDouble(dropRate);
    }
}
