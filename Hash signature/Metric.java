package ru.fuzzysearch;

/**
 * Метрика
 */
public abstract class Metric {


	public int getDistance(CharSequence first, CharSequence second) {
		return getDistance(first, second, -1);
	}

	public abstract int getDistance(CharSequence first, CharSequence second, int max);


	public int getPrefixDistance(CharSequence string, CharSequence prefix) {
		return getPrefixDistance(string, prefix, -1);
	}


	public abstract int getPrefixDistance(CharSequence string, CharSequence prefix, int max);

	
	public int getDistance(CharSequence first, CharSequence second, boolean prefix) {
		return prefix ? getPrefixDistance(first, second) : getDistance(first, second);
	}

	
	public int getDistance(CharSequence first, CharSequence second, int max, boolean prefix) {
		return prefix ? getPrefixDistance(first, second, max) : getDistance(first, second, max);
	}
}
