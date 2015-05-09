package pl.recommendations.crawling.embedded;

import java.io.File;

/**
 * Created by marekmagik on 2015-05-06.
 */
public interface FileRepositoryCrawler extends Runnable {

    public void init();

    public void setDbDir(File file);

    void setSeparator(String separator);
}
