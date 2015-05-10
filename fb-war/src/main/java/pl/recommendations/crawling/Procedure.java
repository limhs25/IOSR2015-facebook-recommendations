package pl.recommendations.crawling;

@FunctionalInterface
interface Procedure {
    void apply() throws InterruptedException;
}