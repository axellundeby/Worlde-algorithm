package no.uib.inf102.wordle.controller.AI;

import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MyAiStrategy1 implements IStrategy {
    private WordleWordList guesses;
    private Random random;

    public MyAiStrategy1() {
        reset();
        random = new Random(11);
    }

    @Override
    public String makeGuess(WordleWord feedback) {

        if (feedback != null) {
            guesses.eliminateWords(feedback);
        }
        if (guesses.size() < 4) {
            String bestWord = calculateBestWordByProbability();
            guesses.remove(bestWord);

            return bestWord;
        } else {
            return guesses.bestword();
        }
    }

    @Override
    public void reset() {
        guesses = new WordleWordList();
    }

    private String calculateBestWordByProbability() {
        List<String> possibleAnswers = guesses.possibleAnswers();

        Map<String, Double> wordProbabilities = new HashMap<>();

        int totalLetterCount = 0;
        for (String word : possibleAnswers) {
            totalLetterCount += word.length();
        }

        for (String word : possibleAnswers) {
            double wordProbability = calculateWordProbability(word, totalLetterCount);
            wordProbabilities.put(word, wordProbability);
        }

        double randomNumber = random.nextDouble();
        double cumulativeProbability = 0.0;

        for (Map.Entry<String, Double> entry : wordProbabilities.entrySet()) {
            cumulativeProbability += entry.getValue();
            if (randomNumber <= cumulativeProbability) {
                return entry.getKey();
            }
        }
        return guesses.bestword();
    }

    private double calculateWordProbability(String word, int totalLetterCount) {
        return (double) word.length() / totalLetterCount;
    }
}