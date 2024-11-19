package agh.ii.prinjava.lab07.lst07_08;

import agh.ii.prinjava.lab07.dal.Alice;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Playing with the text of "Alice's Adventures in Wonderland"
 */


public class Main {

    /**
     * First 10 lines of the text
     */
    private static void demo1() {
        System.out.println("demo1...");
        final List<String> lines = Alice.lines().orElseThrow().stream()
                .limit(10)
                .toList();
        lines.forEach(System.out::println);
    }
    /** Ex 8_1**/

    private static void countTotalWords() {
        System.out.println("\nCounting total words in the text...");

        final Optional<List<String>> optLines = Alice.lines();

        if (optLines.isPresent()) {
            long totalWords = optLines.get().stream()
                    .flatMap(line -> Arrays.stream(line.split("\\s+")))
                    .filter(word -> !word.isEmpty())
                    .count();

            System.out.println("Total number of words in the text: " + totalWords);
        } else {
            System.out.println("Failed to load the text");
        }
    }
    /**
     * Lines of each chapter
     */
    private static void demo2() {
        System.out.println("\ndemo2...");
        final Optional<Map<Integer, List<String>>> optChapterLines = Alice.chapterLines();
        optChapterLines.ifPresent(integerListMap -> {
            integerListMap.forEach((key, value) -> System.out.println("Chapter " + key + " => " + value));
            System.out.println("---");

            integerListMap.entrySet().stream()
                    .flatMap(entry -> entry.getValue().stream())
                    .toList()
                    .forEach(System.out::println);
            System.out.println("---");

            integerListMap.values().stream()
                    .toList()
                    .forEach(System.out::println);
        });
    }

    /**
     * Checking if the division of the text to chapters is correct
     */
    private static void demo3() {
        System.out.println("\ndemo3...");
        final Optional<List<String>> optLines1 = Alice.lines();
        final Optional<Map<Integer, List<String>>> optChapterLines = Alice.chapterLines();

        if (optLines1.isPresent() && optChapterLines.isPresent()) {
            long lines1Count = optLines1.get().stream()
                    .skip(1)
                    .filter(line -> !Pattern.matches("^[IV]+--.+", line))
                    .count();

            long lines2Count = optChapterLines.get().values().stream()
                    .mapToLong(List::size)
                    .sum();

            System.out.println("lines1Count: " + lines1Count + ", lines2Count: " + lines2Count);
        }
    }

    /**
     * Frequency table of letters in the text (without the chapter titles)
     */
    private static void demo4() {
        System.out.println("\ndemo4...");
        final Optional<Map<Integer, List<String>>> optChapterLines = Alice.chapterLines();

        optChapterLines.ifPresent(integerListMap -> integerListMap.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream())
                .flatMap(line -> Alice.expand(line.toLowerCase()).stream())
                .filter(s -> !"'\"?!., ():;-_".contains(s))
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()))
                .forEach((k, v) -> System.out.println(k + " => " + v)));
    }

    public static void main(String[] args) {
        demo1();
        demo2();
        demo3();
        demo4();
        countTotalWords();
    }
}
