package com.schibsted;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static Map<String, List<String>> filesAndWords = new HashMap<>();
    private static List<ItemRank> rankList = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            throw new IllegalArgumentException("No directory give to index");
        }
        final File indexableDirectory = new File(args[0]);

        try (Stream<Path> paths = Files.walk(Paths.get(indexableDirectory.getAbsolutePath()))) {
            paths.filter(Files::isRegularFile).forEach(item -> filesAndWords.put(item.toAbsolutePath().toString(), readAllWordsFromFile(item.toAbsolutePath().toString())));
        }

        System.out.println(filesAndWords.size() + " files read in directory " + indexableDirectory);

        Scanner keyboard = new Scanner(System.in);
        while (true) {
            System.out.print("search> ");
            final String line = keyboard.nextLine();
            List<String> words = Arrays.asList(line.split(" "));
            if (rankList.isEmpty()) {
                filesAndWords.entrySet().stream().forEach(item -> countOccurrences(item.getKey(), words));
            }
            displayRankList();
        }
    }

    public static int countOccurrences(String file, List<String> words) {
        int count = 0;
        for (String word : words) {
            if (wordIsPresentInsideFile(file, word)) {
                count++;
            }
        }
        double percentage = calculatePercentage(file, count);
        rankList.add(new ItemRank(file, percentage));
        return count;
    }

    public static void displayRankList() {
        int index = 0;
        for (ItemRank rank : rankList.stream()
                .sorted(Comparator.comparing(ItemRank::getPercentage)
                        .reversed().thenComparing(ItemRank::getFile))
                .limit(10).collect(Collectors.toList())) {
            if (rank.getPercentage() == 0 && index == 0) {
                System.out.println("no matches found");
                return;
            } else {
                index++;
                System.out.println(rank.getFile() + ": " + rank.getPercentage() + "%");
            }
        }
    }

    public static double calculatePercentage(String file, int numberOfOccurrences) {
        double percentage = 0;
        if (numberOfOccurrences > 0) {
            percentage = Math.round(100.0 * numberOfOccurrences / filesAndWords.get(file).size());
        }
        return percentage;
    }

    static Boolean wordIsPresentInsideFile(String file, String word) {
        return filesAndWords.get(file).stream().anyMatch(Main.matches(word));
    }

    public static Predicate<String> matches(String word) {
        return p -> p.compareTo(word) == 0;
    }

    static List<String> readAllWordsFromFile(String path) {
        String line;
        List<String> fileContent = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while ((line = br.readLine()) != null) {
                fileContent.addAll(Arrays.asList(line.split(" ")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }
}