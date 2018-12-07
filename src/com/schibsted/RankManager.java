package com.schibsted;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class RankManager {

    private List<ItemRank> rankList;
    private RankCalculator rankCalculator;

    public RankManager() {
        this.rankList = new LinkedList<>();
        this.rankCalculator = new RankCalculator();
    }

    public void addItemOnRankList(ItemRank item) {
        this.rankList.add(item);
    }

    public List<ItemRank> getRankList() {
        return rankList;
    }

    public void processRankList(final String file, final int occurrences, final int totalNumberOfWords) {
        double percentage = rankCalculator.calculatePercentage(occurrences, totalNumberOfWords);
        addItemOnRankList(new ItemRank(file, percentage));
    }

    public void displayRankList(final List<ItemRank> rankList) {
        int index = 0;
        for (ItemRank rank : getSortedAndLimitedRankList(rankList)) {
            if (rank.getPercentage() == 0.0 && index == 0) {
                System.out.println("no matches found");
                return;
            } else {
                index++;
                System.out.println(rank.getFile() + ": " + rank.getPercentage() + "%");
            }
        }
    }

    public void resetRankList() {
        this.rankList.clear();
    }

    public List<ItemRank> getSortedAndLimitedRankList(List<ItemRank> rankList) {
        return rankList.stream()
                .sorted(Comparator.comparing(ItemRank::getPercentage)
                        .reversed().thenComparing(ItemRank::getFile))
                .limit(10).collect(Collectors.toList());
    }
}
