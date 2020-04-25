using System;

namespace BKTree
{
   
    static class Levenshtein
    {
        // Compute the Levenshtein Distance between two strings
        // number of operations that must be performed to convert one into the other

        public static int Compute(string word, string compareToWord)
        {
            int wordLength = word.Length;
            int compareWordLength = compareToWord.Length;

            int[,] d = new int[wordLength + 1, compareWordLength + 1];

            if (wordLength == 0)
            {
                return compareWordLength;
            }

            if (compareWordLength == 0)
            {
                return wordLength;
            }


            for (int i = 0; i <= wordLength; d[i, 0] = i++) ;

            for (int j = 0; j <= compareWordLength; d[0, j] = j++) ;

            for (int i = 1; i <= wordLength; ++i)
            {
                for (int j = 1; j <= compareWordLength; ++j)
                {
                    int cost = (compareToWord[j - 1] == word[i - 1]) ? 0 : 1;

                    d[i, j] = Math.Min(
                        Math.Min(d[i - 1, j] + 1, d[i, j - 1] + 1),
                        d[i - 1, j - 1] + cost);
                }
            }

            return d[wordLength, compareWordLength];
        }
    }
}
