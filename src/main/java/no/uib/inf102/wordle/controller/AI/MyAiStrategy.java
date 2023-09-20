package no.uib.inf102.wordle.controller.AI;

import java.util.List;

import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

public class MyAiStrategy implements IStrategy {
    private WordleWordList guesses;
    List<WordleWord> feedbackList;

    public MyAiStrategy() {
        reset();
    }

    @Override
    public String makeGuess(WordleWord feedback) {
        if (feedback != null) {
            guesses.eliminateWords(feedback);
        }
        for (WordleWord feedbackelement : feedbackList) {

        }
        return null;
    }

    @Override
    public void reset() {
        guesses = new WordleWordList();
    }

}

// gjett karakter om riktig, gjett noe annet
// bruker feedback fra tidligere, lagre feedback.
// google wordle løsninger.
