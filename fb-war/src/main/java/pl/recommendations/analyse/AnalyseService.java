package pl.recommendations.analyse;

public interface AnalyseService {

    int PREFFERED_SUGGESTION_SIZE = 10;

    void analyse(Long uuid, int suggestionSize);
}
