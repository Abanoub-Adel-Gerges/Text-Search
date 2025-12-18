package suffixautomaton;
import java.util.ArrayList;
import java.util.HashMap;
public class SuffixAutomaton {
    private static class State {
        int len, link;
        HashMap<Character, Integer> next = new HashMap<>();
        ArrayList<Integer> endPositions = new ArrayList<>();
    }
    private final State[] states;
    private int size, last;
    public SuffixAutomaton(int maxLen) {
        states = new State[2 * maxLen];
        states[0] = new State();
        states[0].link = -1;
        size = 1;
        last = 0;
    }
    public void extend(char c, int position) {
        int cur = size++;
        states[cur] = new State();
        states[cur].len = states[last].len + 1;
        states[cur].endPositions.add(position);
        int p = last;
        while (p != -1 && !states[p].next.containsKey(c)) {
            states[p].next.put(c, cur);
            p = states[p].link;
        }
        if (p == -1) {
            states[cur].link = 0;
        } else {
            int q = states[p].next.get(c);
            if (states[p].len + 1 == states[q].len) {
                states[cur].link = q;
            } else {
                int clone = size++;
                states[clone] = new State();
                states[clone].len = states[p].len + 1;
                states[clone].next.putAll(states[q].next);
                states[clone].link = states[q].link;
                states[clone].endPositions.addAll(states[q].endPositions);
                while (p != -1 && states[p].next.get(c) == q) {
                    states[p].next.put(c, clone);
                    p = states[p].link;
                }
                states[q].link = states[cur].link = clone;
            }
        }
        last = cur;
    }
    public void build(String text) {
        for (int i = 0; i < text.length(); i++) {
            extend(text.charAt(i), i);
        }
        propagatePositions();
    }
    private void propagatePositions() {
        int maxLen = 0;
        for (int i = 0; i < size; i++){maxLen = Math.max(maxLen, states[i].len);}

        int[] cnt = new int[maxLen + 1];
        for (int i = 0; i < size; i++){cnt[states[i].len]++;}

        for (int i = 1; i <= maxLen; i++){cnt[i] += cnt[i - 1];}

        int[] order = new int[size];
        for (int i = size - 1; i >= 0; i--){order[--cnt[states[i].len]] = i;}

        for (int i = size - 1; i > 0; i--){
            int v = order[i];
            int parent = states[v].link;
            if (parent >= 0){states[parent].endPositions.addAll(states[v].endPositions);}
        }
    }
    public ArrayList<Integer> findOccurrences(String s) {
        int v = 0;
        for (char c : s.toCharArray()) {
            if (!states[v].next.containsKey(c)) return new ArrayList<>();
            v = states[v].next.get(c);
        }
        int k = s.length();
        ArrayList<Integer> res = new ArrayList<>();
        for (int end : states[v].endPositions){res.add(end - k + 1);}
        return res;
    }
}
