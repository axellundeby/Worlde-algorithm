package no.uib.inf102.wordle.controller.AI;

import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

import java.util.List;
import java.util.HashMap;
import java.util.Random;

public class MyAiStrategy implements IStrategy {
    private WordleWordList guesses;
    private WordleWordList copyGuessesList;

    public MyAiStrategy() {
        reset();
        new Random(11);
    }

    @Override
    public String makeGuess(WordleWord feedback) {

        if (feedback != null) {
            guesses.eliminateWords(feedback);
        }

        if (guesses.size() < 4) {
            String bestWord = findBestWordByProbability();
            guesses.remove(bestWord);

            return bestWord;
        } else {
            return guesses.bestword();
        }
    }

    @Override
    public void reset() {
        guesses = new WordleWordList();
        copyGuessesList = new WordleWordList(guesses.possibleAnswers());
    }

    private String findBestWordByProbability() {
        List<HashMap<Character, Integer>> originalCommonLetterCounts = guesses
                .countCommonLetters(copyGuessesList.possibleAnswers());
        List<String> possibleAnswers = guesses.possibleAnswers();

        String bestWord = null;
        double maxProbability = Double.NEGATIVE_INFINITY;

        for (String candidateWord : possibleAnswers) {
            double wordProbability = calculateWordProbability(candidateWord, possibleAnswers);

            for (int i = 0; i < originalCommonLetterCounts.size(); i++) {
                int commonCount = originalCommonLetterCounts.get(i).getOrDefault(candidateWord.charAt(i), 0);
                wordProbability *= Math.pow(commonCount / (double) possibleAnswers.size(), 2);
            }

            if (wordProbability > maxProbability) {
                bestWord = candidateWord;
                maxProbability = wordProbability;
            }
        }

        return (bestWord != null) ? bestWord : guesses.bestword();
    }

    private double calculateWordProbability(String word, List<String> possibleAnswers) {
        int wordCount = (int) possibleAnswers.stream().filter(possibleWord -> possibleWord.equals(word)).count();
        int totalWordCount = possibleAnswers.size();
        return wordCount / (double) totalWordCount;
    }

}