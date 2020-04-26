package ru.fuzzysearch;

/**
 * Метод Хешировання по сигнатурі.
 */
public class SignHashIndexer implements Indexer {

	public SignHashIndexer(Alphabet alphabet) {
		this.alphabet = alphabet;
		alphabetMap = makeAlphabetMap(alphabet);
	}

	public Index createIndex(String[] dictionary) {
		int maxLength = 0;

		// Counting the number of words for each hash group (to reduce memory consumption)
		int[] hashCountTable = new int[1 << HASH_SIZE];
		for (String word : dictionary) {
			int hash = makeHash(alphabet, alphabetMap, word);
			++hashCountTable[hash];
			if (word.length() > maxLength) maxLength = word.length();
		}

		// Fill the hash table. Each element is an array of indexes of words in the dictionary corresponding to the hash.
		int[][] hashTable = new int[1 << HASH_SIZE][];
		for (int i = 0; i < dictionary.length; ++i) {
			int hash = makeHash(alphabet, alphabetMap, dictionary[i]);
			if (hashTable[hash] == null) hashTable[hash] = new int[hashCountTable[hash]];
			hashTable[hash][--hashCountTable[hash]] = i;
		}

		return new SignHashIndex(dictionary, alphabet, hashTable, HASH_SIZE, alphabetMap, maxLength);
	}

	/**
	 *Computes the signature hash for a word.
	 */
	public static int makeHash(Alphabet alphabet, int[] alphabetMap, String word) {
		int result = 0;
		for (int i = 0; i < word.length(); ++i) {
			int group = alphabetMap[alphabet.mapChar(word.charAt(i))];
			result |= 1 << group;
		}
		return result;
	}

	/**
	 * Produces an even distribution of alphabet characters among hash groups.
	 */
	private static int[] makeAlphabetMap(Alphabet alphabet) {
		int[] result = new int[alphabet.size()];
		double sourceAspect = (double) result.length / HASH_SIZE;
		double aspect = sourceAspect;
		int[] map = new int[HASH_SIZE];
		for (int i = 0; i < HASH_SIZE; ++i) {
			int step = (int) Math.round(aspect);
			double diff = aspect - step;
			map[i] = step;
			aspect = sourceAspect + diff;
		}
		int resultIndex = 0;
		for (int i = 0; i < map.length; ++i)
			for (int j = 0; j < map[i]; ++j)
				if (resultIndex < result.length) result[resultIndex++] = i;
		return result;
	}

	private static final int HASH_SIZE = 16;
	private final Alphabet alphabet;
	private final int[] alphabetMap;
}
