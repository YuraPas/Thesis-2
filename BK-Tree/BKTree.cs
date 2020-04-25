using System.Collections.Generic;
using System.IO;
using System.Linq;

namespace BKTree
{
    public class BKTree
    {
        private BKNode root;

        public void AddWord(string word)
        {
            if (root == null)
            {
                root = new BKNode(word);
            }
            else
            {
                root.AddNode(word);
            }
        }

        public bool WordExists(string word)
        {
            return root.FindWord(word);
        }

        public IEnumerable<string> GetSpellingSuggestions(string word, int tolerance = 2)
        {
            if (root == null)
                return null;

            var suggestions = new List<string>();
            suggestions.AddRange(root.FindSuggestions(word, tolerance));
            return suggestions;
        }

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



        private class BKNode
        {
            private string nodeWord;
            /// <summary>
            /// int - Levenshtein distance
            /// Node - tree node with word
            /// </summary>
            private Dictionary<int, BKNode> children;

            public BKNode(string nodeWord)
            {
                this.nodeWord = nodeWord;
                children = new Dictionary<int, BKNode>();
            }

            public void AddNode(string word)
            {
                var dist = Levenshtein.Compute(word, nodeWord);

                // The word is already in dictionary
                if (dist == 0)
                    return;

                BKNode child;
                if (children.TryGetValue(dist, out child))
                {
                    // We already have another node of equal distance, so add this word to that node's subtree
                    child.AddNode(word);
                }
                else
                {
                    // We don't have a child of that distance, so a new child is added
                    children.Add(dist, new BKNode(word));
                }
            }

            public bool FindWord(string word)
            {
                int dist = Levenshtein.Compute(word, this.nodeWord);

                // The word's already in the dictionary!
                if (dist == 0) return true;

                BKNode child;
                if (children.TryGetValue(dist, out child))
                {
                    // We already have another node of equal distance, so add this word to that node's subtree
                    return child.FindWord(word);
                }

                return false;
            }

            public IEnumerable<string> FindSuggestions(string word, int tolerance)
            {
                var suggestions = new List<string>();

                int dist = Levenshtein.Compute(word, nodeWord);
                if (dist < tolerance)
                {
                    suggestions.Add(nodeWord);
                }

                foreach(int key in children.Keys)
                {
                    if (key >= dist - tolerance && key <= dist + tolerance)
                    {
                        suggestions.AddRange(children[key].FindSuggestions(word, tolerance));
                    }
                }

                return suggestions;
            }
        }
    }
}
