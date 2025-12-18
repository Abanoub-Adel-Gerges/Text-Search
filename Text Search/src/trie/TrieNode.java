package trie;
import java.util.ArrayList;
public class TrieNode {
    public TrieNode[] childrenLowerCase = new TrieNode[26];
    public TrieNode[] childrenUpperCase = new TrieNode[26];
    public boolean isEndOfWord = false;
}
