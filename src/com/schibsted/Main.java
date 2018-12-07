package com.schibsted;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        if (args.length == 0) {
            throw new IllegalArgumentException("No directory give to index");
        }
        final File rootDirectory = new File(args[0]);
        FileManager fileManager = new FileManager(rootDirectory);
        RankManager rankManager = new RankManager();

        System.out.println(fileManager.getFilesAndWords().size() + " files read in directory " + rootDirectory);

        Scanner keyboard = new Scanner(System.in);
        while (true) {
            System.out.print("search> ");
            final String line = keyboard.nextLine();
            List<String> words = Arrays.asList(line.split(" "));
            fileManager.getFilesAndWords().entrySet().stream().forEach(item -> {
                int occurrences = fileManager.countOccurrences(item.getKey(), words);
                rankManager.processRankList(item.getKey(), occurrences, item.getValue().size());
            });
            rankManager.displayRankList(rankManager.getRankList());
            rankManager.resetRankList();
            fileManager.resetFileBuffer();
        }
    }
}