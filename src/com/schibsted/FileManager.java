package com.schibsted;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FileManager {

    private RankCalculator rankCalculator;
    private File baseDirectory;
    private Map<String, List<String>> filesAndWords;

    public FileManager(File rootDirectory) {
        this.filesAndWords = new HashMap<>();
        this.rankCalculator = new RankCalculator();
        this.baseDirectory = rootDirectory;
        this.loadFileContents(rootDirectory);
    }

    public void resetFileBuffer() {
        this.loadFileContents(baseDirectory);
    }

    public Map<String, List<String>> getFilesAndWords() {
        return this.filesAndWords;
    }

    public void loadFileContents(File rootDirectory) {
        try (Stream<Path> paths = Files.walk(Paths.get(rootDirectory.getAbsolutePath()))) {
            paths.filter(Files::isRegularFile)
                    .forEach(item ->
                            filesAndWords.put(item.getFileName().toString(), readAllWordsFromFile(item.toAbsolutePath().toString())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> readAllWordsFromFile(String path) {
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

    public int countOccurrences(String file, List<String> words) {
        int count = 0;
        for (String word : words) {
            if (isWordPresentInsideFile(file, word)) {
                count++;
            }
        }
        return count;
    }

    public boolean isWordPresentInsideFile(String file, String word) {
        return filesAndWords.get(file).stream().anyMatch(containsWord(word));
    }

    public Predicate<String> containsWord(String word) {
        return p -> p.compareTo(word) == 0;
    }
}
