package hw;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static AtomicInteger beautyWordsCounterFor3LettersLong = new AtomicInteger(); //счётчик 3
    public static AtomicInteger beautyWordsCounterFor4LettersLong = new AtomicInteger(); //счётчик 4
    public static AtomicInteger beautyWordsCounterFor5LettersLong = new AtomicInteger(); //счётчик 5

    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        executorService.submit(() -> {
            for (String text : texts) {
                if (isPalindrome(text)) {
                    AtomicInteger atomicInteger = getBeautyWordsCounter(text.length());
                    if (atomicInteger != null) {
                        atomicInteger.incrementAndGet();
                    }
                }
            }
        });

        executorService.submit(() -> {
            for (String text : texts) {
                if (isLettersAreSame(text)) {
                    AtomicInteger atomicInteger = getBeautyWordsCounter(text.length());
                    if (atomicInteger != null) {
                        atomicInteger.incrementAndGet();
                    }
                }
            }
        });

        executorService.submit(() -> {
            for (String text : texts) {
                if (isLettersInAscendingOrder(text)) {
                    AtomicInteger atomicInteger = getBeautyWordsCounter(text.length());
                    if (atomicInteger != null) {
                        atomicInteger.incrementAndGet();
                    }
                }
            }
        });

        executorService.awaitTermination(3, TimeUnit.SECONDS);

        System.out.println("Красивых слов с длиной 3: " + beautyWordsCounterFor3LettersLong.get() + " шт.");
        System.out.println("Красивых слов с длиной 4: " + beautyWordsCounterFor4LettersLong.get() + " шт.");
        System.out.println("Красивых слов с длиной 5: " + beautyWordsCounterFor5LettersLong.get() + " шт.");

        executorService.shutdown();
    }

    public static AtomicInteger getBeautyWordsCounter(int textLength) {
        switch (textLength) {
            case 3:
                return beautyWordsCounterFor3LettersLong;
            case 4:
                return beautyWordsCounterFor4LettersLong;
            case 5:
                return beautyWordsCounterFor5LettersLong;
            default:
                return null;
        }
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static boolean isPalindrome(String text) {
        return text.equals(new StringBuilder(text).reverse().toString());
    }

    public static boolean isLettersAreSame(String text) {
        String checkRow = String.valueOf(text.charAt(0)).repeat(text.length()).toLowerCase();
        if (text.toLowerCase().equals(checkRow)) {
            return true;
        }
        return false;
    }

    public static boolean isLettersInAscendingOrder(String text) {
        int prev = 0;
        for (char t : text.toCharArray()) {
            if ((int) t >= prev) {
                prev = t;
            } else {
                return false;
            }
        }
        return true;
    }

}