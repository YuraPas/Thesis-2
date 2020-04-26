package ru.fuzzysearch;

import java.util.HashSet;
import java.util.Set;

/**
 * Signature hashing method search module.
 * 
 * @see SignHashIndexer
 */
public class SignHashSearcher extends WordSearcher {

	public SignHashSearcher(SignHashIndex index, Metric metric, int maxDistance) {
		super(index);
		this.metric = metric;
		this.maxDistance = maxDistance;
		dictionary = index.getDictionary();
		alphabet = index.getAlphabet();
		hashTable = index.getHashTable();
		hashSize = index.getHashSize();
		alphabetMap = index.getAlphabetMap();
	}

	/**
	 * Searches for all words from the dictionary, whose signature hash differs from the original one no more than in
	 * maxDistance бітів.
	 */
	public Set<Integer> search(String string) {
		Set<Integer> result = new HashSet<Integer>();

		int stringHash = SignHashIndexer.makeHash(alphabet, alphabetMap, string);
		populate(string, stringHash, result);
		if (maxDistance > 0) hashPopulate(string, stringHash, 0, hashSize, result, maxDistance - 1);
		return result;
	}

	/**
* Introduces iterates over all hashes that differ from the original one by 1 bit at any position. When adding or removing
* a character from a word in a signature hash changes 0 or 1 bit, when replacing a character, from 0 to 2 bits
	 */
	private void hashPopulate(String query, int hash, int start, int hashSize, Set<Integer> set, int depth) {
		for (int i = start; i < hashSize; ++i) {
			int queryHash = hash ^ (1 << i);
			populate(query, queryHash, set);
			if (depth > 0) hashPopulate(query, queryHash, i + 1, hashSize, set, depth - 1);
		}
	}

	/**
* Iterates over all the words in a dictionary with a given hash, recording only words satisfying the restriction in the result
* with this metric.
	 */
	private void populate(String query, int queryHash, Set<Integer> set) {
		int[] hashBucket = hashTable[queryHash];
		if (hashBucket != null) for (int dictionaryIndex : hashBucket) {
			String word = dictionary[dictionaryIndex];
			if (metric.getDistance(query, word, maxDistance) <= maxDistance) set.add(new Integer(dictionaryIndex));
		}
	}

	private final Metric metric;
	private final int maxDistance;
	private final String[] dictionary;
	private final Alphabet alphabet;
	private final int[][] hashTable;
	private final int hashSize;
	private final int[] alphabetMap;
}
