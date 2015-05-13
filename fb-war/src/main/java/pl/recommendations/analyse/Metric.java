package pl.recommendations.analyse;


import org.springframework.stereotype.Component;

@Component
public interface Metric {
    double getDistance(Long firstUUID, Long secondUUID);
}
