package no.uib.inf102.wordle.model.word;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	public void eliminateWords(WordleWord feedback) {// O(n)
		List<String> posslist = new ArrayList<>();
		for (String possWord : possibleAnswers) { // O(n)
			if (WordleWord.isPossibleWord(possWord, feedback)) { // O(k)
				posslist.add(possWord); // O(1)
			}
		}

		// Assigning 'posslist' to 'possibleAnswers' takes O(m) time.
		possibleAnswers = posslist; // O(m)
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

	/**
	 * Counts the number of times each letter appears in each position of the
	 * possible answers.
	 *
	 * This method returns a list of hashmaps, where each hashmap contains the
	 * number of times each letter appears in a given position of the possible
	 * answers. For example, if the possible answers are "horse", "stars", "house",
	 * "hopes", and "hoses", then the returned list of hashmaps would be:
	 *
	 * <pre>
	 * [{h=5, s=0}, {o=2, t=1, o=1, p=1, o=1}, {r=1, a=1, u=1, p=1, s=1}, {s=2, e=2, s=1, e=1, s=1}, {e=2, s=2, e=1, s=1, e=1}]
	 * </pre>
	 *
	 * @param poss The list of possible answers.
	 * @return A list of hashmaps, where each hashmap contains the number of times
	 *         each letter appears in a given position of the possible answers.
	 */

	public List<HashMap<Character, Integer>> countCommonLetters(List<String> poss) {// O(n)
		List<HashMap<Character, Integer>> hashMapList = new ArrayList<>(); // O(1)

		// This loop iterates 5 times, which is a constant factor (O(5) = O(1)).
		for (int i = 0; i < 5; i++) { // O(1)
			HashMap<Character, Integer> letterCount = new HashMap<>(); // O(1)
			for (String word : poss) { // O(n)
				char letter = word.charAt(i); // O(1)
				letterCount.put(letter, letterCount.getOrDefault(letter, 0) + 1); // O(1)
			}

			hashMapList.add(letterCount); // O(1)
		}

		return hashMapList; // O(1)
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
	public String bestword() {
		List<String> words = possibleAnswers(); // O(1)
		List<HashMap<Character, Integer>> hashmapList = countCommonLetters(words); // O(n)
		int highscore = 0; // O(1)
		String bestword = ""; // O(1)

		for (String word : words) { // O(N)
			int wordPoints = 0; // O(1)

			for (int i = 0; i < word.length(); i++) { // O(5) Word length is always 5
				int charPoints = 0; // O(1)
				HashMap<Character, Integer> currentMap = hashmapList.get(i); // O(1)
				charPoints += currentMap.getOrDefault(word.charAt(i), 0); // O(1)
				wordPoints += charPoints; // O(1)
			}

			if (wordPoints > highscore) { // O(1)
				highscore = wordPoints; // O(1)
				bestword = word; // O(1)
			}

			wordPoints = 0; // O(1)
		}

		return bestword; // O(1)
	}

}