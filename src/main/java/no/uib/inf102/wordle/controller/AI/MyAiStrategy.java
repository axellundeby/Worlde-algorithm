package no.uib.inf102.wordle.controller.AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import no.uib.inf102.wordle.model.word.AnswerType;
import no.uib.inf102.wordle.model.word.WordleCharacter;
import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

public class MyAiStrategy implements IStrategy {

    private WordleWordList guesses;
    private WordleWordList copyList;
    private Integer guessCount;

    private HashMap<Character, Integer> confirmedGreen;
    private HashMap<Character, Integer> confirmedYellow;
    private HashMap<Character, Integer> confirmedGray;

    public MyAiStrategy() {
        reset();
    }

    @Override
    public String makeGuess(WordleWord feedback) {
        if (feedback != null) {
            guesses.eliminateWords(feedback);
            copyList = eliminateGreenAsGrey(copyList, feedback);
            confirmedPositions(feedback);
            removeGreenFromCopyList();
            removeGrayFromCopyList();
        }

        if (guessCount == 0) {
            guessCount++;
            return guesses.bestword();
        }

        if (guessCount >= 1) {
            dontGuessYellowOnSameIndex();
            removeGreenFromCopyList();
            guessCount++;
            return guesses.bestword();
        }

        if (guessCount == 1) {
            eliminateWordsWithDuplicateLetters();
        }

        if (guessCount >= 1 && (confirmedGreen.size() <= 1 && confirmedYellow.size() <= 1)// endre case
                || (confirmedGray.size() <= 3) || confirmedGreen.size() <= 3) {
            if (copyList.size() != 0) {
                guessCount++;
                return copyList.bestword();
            }
        }

        guessCount++;
        return guesses.bestword();
    }

    @Override
    public void reset() {
        guesses = new WordleWordList();
        copyList = new WordleWordList(guesses.possibleAnswers());
        confirmedGreen = new HashMap<>();
        confirmedYellow = new HashMap<>();
        confirmedGray = new HashMap<>();
        guessCount = 0;
    }

    private void confirmedPositions(WordleWord feedback) {
        int index = 0;
        for (WordleCharacter wc : feedback) {
            if (wc.answerType == AnswerType.CORRECT) {
                confirmedGreen.put(wc.letter, index);
            }
            if (wc.answerType == AnswerType.WRONG_POSITION) {
                confirmedYellow.put(wc.letter, index);
            }
            if (wc.answerType == AnswerType.WRONG) {
                confirmedGray.put(wc.letter, index);
            }
            index++;
        }
        // System.out.println("Green: " + confirmedGreen);
        // System.out.println("Yellow: " + confirmedYellow);
        // System.out.println("Gray: " + confirmedGray);
    }

    private WordleWordList eliminateGreenAsGrey(WordleWordList wordList, WordleWord feedback) {
        if (feedback == null) {
            return wordList;
        }

        List<String> filteredPossibleAnswers = new ArrayList<>();

        for (String currentGuess : wordList.possibleAnswers()) {
            boolean validWord = true;

            for (Map.Entry<Character, Integer> entry : confirmedGreen.entrySet()) {
                char greenLetter = entry.getKey();
                int greenPosition = entry.getValue();

                int firstOccurrence = currentGuess.indexOf(greenLetter);
                int lastOccurrence = currentGuess.lastIndexOf(greenLetter);

                if (currentGuess.length() <= greenPosition ||
                        currentGuess.charAt(greenPosition) != greenLetter ||
                        (firstOccurrence != lastOccurrence && firstOccurrence != greenPosition)) {
                    validWord = false;
                    break;
                }
            }

            if (validWord) {
                filteredPossibleAnswers.add(currentGuess);
            }
        }

        return new WordleWordList(filteredPossibleAnswers);
    }

    private void removeGreenFromCopyList() {
        List<String> wordsToRemove = new ArrayList<>();

        for (String word : copyList.possibleAnswers()) {
            boolean containsGreen = false;

            for (char c : word.toCharArray()) {
                if (confirmedGreen.containsKey(c)) {
                    containsGreen = true;
                    break;
                }
            }

            if (containsGreen) {
                wordsToRemove.add(word);
            }
        }

        for (String wordToRemove : wordsToRemove) {
            copyList.remove(wordToRemove);
        }
    }

    private void removeGrayFromCopyList() {
        List<String> wordsToRemove = new ArrayList<>();

        for (String word : copyList.possibleAnswers()) {
            boolean containsGray = false;

            for (char c : word.toCharArray()) {
                if (confirmedGray.containsKey(c)) {
                    containsGray = true;
                    break;
                }
            }

            if (containsGray) {
                wordsToRemove.add(word);
            }
        }

        for (String wordToRemove : wordsToRemove) {
            copyList.remove(wordToRemove);
        }
    }

    private void dontGuessYellowOnSameIndex() {
        List<String> wordsToRemove = new ArrayList<>();

        for (String word : copyList.possibleAnswers()) {
            boolean containsYellow = false;

            for (int i = 0; i < word.length(); i++) {
                if (confirmedYellow.containsKey(word.charAt(i)) && confirmedYellow.get(word.charAt(i)) == i) {
                    containsYellow = true;
                    break;
                }
            }
            if (containsYellow) {
                wordsToRemove.add(word);
            }
        }

        for (String wordToRemove : wordsToRemove) {
            copyList.remove(wordToRemove);
        }
    }

    // make a mathod that dont guess words with duplicate letters

    private WordleWordList eliminateWordsWithDuplicateLetters() {
        List<String> filteredWords = new ArrayList<>();

        for (String word : copyList.possibleAnswers()) {
            boolean hasDuplicate = false;
            Set<Character> uniqueLetters = new HashSet<>();

            for (char c : word.toCharArray()) {
                if (uniqueLetters.contains(c)) {
                    hasDuplicate = true;
                    break;
                }
                uniqueLetters.add(c);
            }

            if (!hasDuplicate) {
                filteredWords.add(word);
            }
        }

        return new WordleWordList(filteredWords);
    }

}
