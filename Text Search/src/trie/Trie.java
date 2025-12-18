package trie;
// Prefix tree
public class Trie {
    private TrieNode root;
    public Trie() {
        root = new TrieNode();
    }
    public TrieNode getRoot(){
        return root;
    }
    public void insert(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            if(Character.isLowerCase(c)) {
                int index = c - 'a';
                if (current.childrenLowerCase[index] == null) {
                    current.childrenLowerCase[index] = new TrieNode();
                }
                current = current.childrenLowerCase[index];
            } else {
                int index = c - 'A';
                if (current.childrenUpperCase[index] == null) {
                    current.childrenUpperCase[index] = new TrieNode();
                }
                current = current.childrenUpperCase[index];
            }
        }
        current.isEndOfWord = true;
    }
    public boolean searchWholeWord(String word) {
        TrieNode node = getNode(word);
        return node != null && node.isEndOfWord;
    }
    public boolean startsWith(String prefix) {
        TrieNode node = getNode(prefix);
        return node != null;
    }
    private TrieNode getNode(String str) {
        TrieNode current = root;
        for (char c : str.toCharArray()) {
            if(Character.isLowerCase(c)) {
                int index = c - 'a';
                if (current.childrenLowerCase[index] == null){return null;}
                current = current.childrenLowerCase[index];
            } else {
                int index = c - 'A';
                if (current.childrenUpperCase[index] == null){return null;}
                current = current.childrenUpperCase[index];
            }
        }
        return current;
    }
}