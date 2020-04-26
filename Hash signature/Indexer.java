package ru.fuzzysearch;

/**
 * Search algorithm indexer.
 */
public interface Indexer {

	/**
	 * Creates an index on a given dictionary.
	 * 
	 * @param dictionary
	 *            
	 * @return индекс {@link Index}
	 */
	public Index createIndex(String[] dictionary);
}
