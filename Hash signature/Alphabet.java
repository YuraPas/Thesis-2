package ru.fuzzysearch;

import java.io.Serializable;

/**
 * Алфавіт
 */
public interface Alphabet extends Serializable {

	/**
	 * Maps the ch character from the alphabet to an integer in the range from 0 до size() - 1
	 */
	public int mapChar(char ch);

	/**
	 * Returns an array of all alphabet characters
	 */
	public char[] chars();

	/**
	 * Returns the size of the alphabet.
	 */
	public int size();
}
