package no.uib.inf102.wordle.controller.AI;

import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

public class MyAiStrategy implements IStrategy {
    private WordleWordList guesses;

    @Override
    public String makeGuess(WordleWord feedback) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'makeGuess'");
    }

    @Override
    public void reset() {
        guesses = new WordleWordList();
    }

}
