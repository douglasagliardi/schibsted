package com.schibsted;

public class ItemRank {
    private String file;
    private double percentage;

    public ItemRank(String file, double percentage) {
        this.file = file;
        this.percentage = percentage;
    }

    public String getFile() {
        return this.file;
    }

    public double getPercentage() {
        return percentage;
    }
}
