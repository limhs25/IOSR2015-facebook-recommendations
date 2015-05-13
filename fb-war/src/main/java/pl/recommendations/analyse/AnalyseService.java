package pl.recommendations.analyse;

import org.springframework.stereotype.Component;

@Component("analyseService")
public interface AnalyseService {
     void scheduleForAnalyse(Long userId);
     void analyse(Long uuid);
     void updateDatabase();
}
