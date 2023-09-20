package no.uib.inf102.wordle.model.word;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import no.uib.inf102.wordle.resources.GetWords;

/**
 * This class describes a structure of two lists for a game of Wordle: The list
 * of words that can be used as guesses and the list of words that can be
 * possible answers.
 */
public class WordleWordList {

	/**
	 * All words in the game. These words can be used as guesses.
	 */
	private List<String> allWords;

	/**
	 * A subset of <code>allWords</code>. <br>
	 * </br>
	 * These words can be the answer to a wordle game.
	 */
	private List<String> possibleAnswers;

	/**
	 * Create a WordleWordList that uses the full words and limited answers of the
	 * GetWords class.
	 */
	public WordleWordList() {
		this(GetWords.ALL_WORDS_LIST, GetWords.ANSWER_WORDS_LIST);
	}

	/**
	 * Create a WordleWordList with the given <code>words</code> as both guesses and
	 * answers.
	 * 
	 * @param words
	 */
	public WordleWordList(List<String> words) {
		this(words, words);
	}

	/**
	 * Create a WordleWordList with the given lists as guessing words and answers.
	 * <code>answers</code> must be a subset of <code>words</code>.
	 * 
	 * @param words   The list of words to be used as guesses
	 * @param answers The list of words to be used as answers
	 * @throws IllegalArgumentException if the given <code>answers</code> were not a
	 *                                  subset of <code>words</code>.
	 */
	public WordleWordList(List<String> words, List<String> answers) {
		HashSet<String> wordsSet = new HashSet<String>(words);
		if (!wordsSet.containsAll(answers))
			throw new IllegalArgumentException("The given answers were not a subset of the valid words.");

		this.allWords = new ArrayList<>(words);
		this.possibleAnswers = new ArrayList<>(answers);
	}

	/**
	 * Get the list of all guessing words.
	 * 
	 * @return all words
	 */
	public List<String> getAllWords() {
		return allWords;
	}

	/**
	 * Returns the list of possible answers.
	 * 
	 * @return
	 */
	public List<String> possibleAnswers() {
		return possibleAnswers;
	}

	/**
	 * Eliminates words from the possible answers list using the given
	 * <code>feedback</code>
	 * 
	 * @param feedback
	 */
	public void eliminateWords(WordleWord feedback) {
		List<String> posslist = new ArrayList<>();
		for (String possWord : possibleAnswers) {
			if (WordleWord.isPossibleWord(possWord, feedback)) {
				posslist.add(possWord);
			}
		}
		possibleAnswers = posslist;
	}

	/**
	 * Returns the amount of possible answers in this WordleWordList
	 * 
	 * @return size of
	 */
	public int size() {
		return possibleAnswers.size();
	}

	/**
	 * Removes the given <code>answer</code> from the list of possible answers.
	 * 
	 * @param answer
	 */
	public void remove(String answer) {
		possibleAnswers.remove(answer);

	}

	/**
	 * Returns the word length in the list of valid guesses.
	 * 
	 * @return
	 */
	public int wordLength() {
		return allWords.get(0).length();
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

	/**
	 * Finds the best word from a list of possible answers based on common letter
	 * counts.
	 *
	 * This method calculates a score for each word in the list of possible answers
	 * by comparing the letters in the word to the common letter counts provided by
	 * the
	 * countCommonLetters method. The word with the highest score is considered the
	 * best word.
	 *
	 * @return The best word among the list of possible answers.
	 */
	public String bestword(boolean diff) {
		List<String> poss = possibleAnswers();
		List<HashMap<Character, Integer>> hashmapList = countCommonLetters(poss);
		List<String> words = getAllWords();
		int highscore = 0;
		String bestword = "";
		for (String word : words) {// O(n) siden word er bare 5 bokstaver
			if (diff && !allCharsDifferent(word)) {
				continue;
			}
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

	private boolean allCharsDifferent(String word) {
		HashMap<Character, Integer> dupMap = new HashMap<>();
		char[] chars = word.toCharArray();
		for (int i = 0; i < word.length(); i++) {
			dupMap.put(word.charAt(i), dupMap.getOrDefault(word.charAt(i), 0) + 1);
		}

		for (Character c : chars) {
			if (dupMap.get(c) > 1) {
				return false;
			}
		}
		return true;

	}

}
