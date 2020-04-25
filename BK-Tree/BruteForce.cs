using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using BKTree;

namespace BK_Tree
{
    public class BruteForce
    {
        public List<string> WordList { get; set; }= new List<string>();
        public List<string> Suggestion { get; set; } = new List<string>();


        public void LoadDictionary(string path)
        {
            StreamReader file = File.OpenText(path);

            string line;

            while ((line = file.ReadLine()) != null)
            {
                line = line.Trim();

                AddWord(line);
            }
        }

        private void AddWord(string line)
        {
            WordList.Add(line);
        }

        public IEnumerable<string> FindSuggestions(string word, int tolerance = 2)
        {

            foreach (var item in WordList)
            {
                int dist = Levenshtein.Compute(word, item);

                if (dist < tolerance)
                {
                    Suggestion.Add(item);
                }
            }


            return Suggestion;
        }
    }
}
