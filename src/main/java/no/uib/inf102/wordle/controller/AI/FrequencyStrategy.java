package no.uib.inf102.wordle.controller.AI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.uib.inf102.wordle.model.word.WordleCharacter;
import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

/**
 * This strategy finds the word within the possible words which has the highest
 * expected
 * number of green matches.
 */
public class FrequencyStrategy implements IStrategy {

    private WordleWordList guesses;

    public FrequencyStrategy() {
        reset();
    }

    @Override
    public String makeGuess(WordleWord feedback) {
        if (feedback != null) {
            guesses.eliminateWords(feedback);
        }
        List<String> poss = guesses.possibleAnswers();
        List<HashMap<Character, Integer>> hashmapList = countCommonLetters(poss);

        List<String> words = guesses.getAllWords();
        int highscore = 0;
        String bestword = "";
        for (String word : words) {// O(n) siden word er bare 5 bokstaver
            int wordPoints = 0;
            for (int i = 0; i < word.length(); i++) {
                int charPoints = 0;
                HashMap<Character, Integer> currentMap = hashmapList.get(i);
                charPoints += currentMap.getOrDefault(word.charAt(i), 0);
                wordPoints += charPoints;
            }
            if (wordPoints > highscore) {
                highscore = wordPoints;
                bestword = word;
            }
            wordPoints = 0;
        }
        return bestword;

    }

    private List<HashMap<Character, Integer>> countCommonLetters(List<String> poss) {

        HashMap<Character, Integer> firstLetter = new HashMap<>();
        HashMap<Character, Integer> secondLetter = new HashMap<>();
        HashMap<Character, Integer> thirdLetter = new HashMap<>();
        HashMap<Character, Integer> fourthLetter = new HashMap<>();
        HashMap<Character, Integer> fifthLetter = new HashMap<>();

        for (String word : poss) {
            firstLetter.put(word.charAt(0), firstLetter.getOrDefault(word.charAt(0), 0) + 1);
            secondLetter.put(word.charAt(1), secondLetter.getOrDefault(word.charAt(1), 0) + 1);
            thirdLetter.put(word.charAt(2), thirdLetter.getOrDefault(word.charAt(2), 0) + 1);
            fourthLetter.put(word.charAt(3), fourthLetter.getOrDefault(word.charAt(3), 0) + 1);
            fifthLetter.put(word.charAt(4), fifthLetter.getOrDefault(word.charAt(4), 0) + 1);
        }
        List<HashMap<Character, Integer>> hashMapList = new ArrayList<>();

        hashMapList.add(firstLetter);
        hashMapList.add(secondLetter);
        hashMapList.add(thirdLetter);
        hashMapList.add(fourthLetter);
        hashMapList.add(fifthLetter);
        return hashMapList;

    }

    @Override
    public void reset() {
        guesses = new WordleWordList();
    }
}