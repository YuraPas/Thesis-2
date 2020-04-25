using System;
using System.Collections.Generic;
using System.Diagnostics;
using BK_Tree;


class Program
{
    public const string path = "words.txt";

    static void Main(string[] args)
    {
        string word = "necessaries";

        var tree = new BKTree.BKTree();
        Console.WriteLine("Loading words... ");
        tree.LoadDictionary(path);
        Console.WriteLine("Loaded");


        Stopwatch watch = new Stopwatch();
        watch.Start();
        var result = tree.GetSpellingSuggestions(word, 2);
        watch.Stop();

        PrintResult(result);
        Console.WriteLine(watch.ElapsedMilliseconds);

        


        //Brute force
        Console.WriteLine();
        Console.WriteLine("Brute force");
        var brute = new BruteForce();
        brute.LoadDictionary(path);
        watch.Restart();
        var bruteResult = brute.FindSuggestions(word, 2);
        watch.Stop();
        PrintResult(bruteResult);
        Console.WriteLine(watch.ElapsedMilliseconds);

        Console.WriteLine();
        
        Console.ReadKey();
    }

    private static void PrintResult(IEnumerable<string> list)
    {
        foreach(string s in list)
        {
            Console.Write(s + ", ");
        }

        Console.WriteLine();
    }
}

