# Runtime Analysis
For each method of the tasks give a runtime analysis in Big-O notation and a description of why it has this runtime.

**If you have implemented new methods not listed you must add these as well, e.g. any helper methods. You need to show how you analyzed any methods used by the methods listed below.**

The runtime should be expressed using these three parameters:
   * `n` - number of words in the list allWords
   * `m` - number of words in the list possibleWords
   * `k` - number of letters in the wordleWords


## Task 1 - matchWord
* `WordleAnswer::matchWord`: O(n)
    * *The method has a runtime of O(n) because it iterates through the answer string once, where 'n' is the length of the answer string. This main loop is the primary factor contributing to the linear time complexity. Other operations in the method are constant-time operations, making the overall time complexity O(n).*

## Task 2 - EliminateStrategy
* `WordleWordList::eliminateWords`: O(m)
    * *With 'k' always being a constant value of 5, the eliminateWords method has a simplified runtime of O(m). This simplification occurs because 'k' no longer affects the overall time complexity, making the complexity solely dependent on the size of the possibleAnswers list. Conequently making the runtime of the method linear*


## Task 3 - FrequencyStrategy
* `FrequencyStrategy::makeGuess`: O(m)
    * *The runtime is primarily determined by the number of words (N) in the input list due to an outer loop (O(N)), with each word being processed in constant time (O(1)) because their length is always 5 characters. Other operations like list initialization and variable updates are also constant time.

* `FrequencyStrategy::`countCommonLetters: O(n)
    *The countCommonLetters method has a runtime complexity of O(n). This simplification arises because 'k' is a constant value (fixed at 5) and the outer loop iterates a fixed number of times (5 times). Consequently, the method's overall time complexity depends solely on the size of the poss list and is not influenced by constant factors like 'k' or the fixed number of iterations in the loop, making it O(n).


# Task 4 - Make your own (better) AI
For this task you do not need to give a runtime analysis. 
Instead, you must explain your code. What was your idea for getting a better result? What is your strategy?

*My initial strategy was to eliminate words with green letters in the second guessing number; this way, I would receive more feedback on multiple letters. However, I never managed to accomplish this. I also considered instructing it not to guess words with duplicate letters on the second guessing. I've even explored the idea of using entropy and finding a word that eliminates the most possibilities instead of focusing on the word tith most green or yellow feedback. Unfortunately, I couldn't successfully implement any of these approaches, which led me to question the efficacy of the FrequencyStrategy. The FrequencyStrategy falls short when we know four of the letters, yet it suggests a less probable word. Consequently, I devised an alternative method for deriving a more plausible word.*
