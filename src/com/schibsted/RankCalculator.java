package com.schibsted;

public class RankCalculator {

    public double calculatePercentage(final int numberOfOccurrences, final int totalWords) {
        double percentage = 0;
        if (numberOfOccurrences > 0) {
            percentage = Math.round(100.0 * numberOfOccurrences / totalWords);
        }
        return percentage;
    }
}
