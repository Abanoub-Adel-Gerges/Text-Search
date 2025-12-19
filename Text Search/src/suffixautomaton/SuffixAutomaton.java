package suffixautomaton;
import datastructures.ArrayList;
public class SuffixAutomaton {
    private final int[] len;
    private final int[] link;
    private final int[] firstPos;
    private final boolean[] isClone;
    private final int[] head;
    private final int[] nextEdge;
    private final int[] edgeTo;
    private final char[] edgeChar;

    private final int[] treeHead;
    private final int[] treeNext;
    private final int[] treeTo;

    private int size, last, edgeCount, treeEdgeCount;

    public SuffixAutomaton(int maxLen) {
        int nodes = 2 * maxLen + 1;
        len = new int[nodes];
        link = new int[nodes];
        firstPos = new int[nodes];
        isClone = new boolean[nodes];
        head = new int[nodes];
        java.util.Arrays.fill(head, -1);

        int maxEdges = 3 * maxLen + 5;
        nextEdge = new int[maxEdges];
        edgeTo = new int[maxEdges];
        edgeChar = new char[maxEdges];

        treeHead = new int[nodes];
        java.util.Arrays.fill(treeHead, -1);
        treeNext = new int[nodes];
        treeTo = new int[nodes];

        link[0] = -1;
        firstPos[0] = -1;
        size = 1;
        last = 0;
    }

    private void addTransition(int u, char c, int v) {
        int e = findEdge(u, c);
        if (e != -1) {
            edgeTo[e] = v;
            return;
        }
        // Add new
        edgeTo[edgeCount] = v;
        edgeChar[edgeCount] = c;
        nextEdge[edgeCount] = head[u];
        head[u] = edgeCount++;
    }
    private int getTransition(int u, char c) {
        int e = findEdge(u, c);
        return (e == -1) ? -1 : edgeTo[e];
    }
    private int findEdge(int u, char c) {
        for (int i = head[u]; i != -1; i = nextEdge[i]) {
            if (edgeChar[i] == c) return i;
        }
        return -1;
    }
    public void extend(char c, int position) {
        int cur = size++;
        len[cur] = len[last] + 1;
        firstPos[cur] = position;
        isClone[cur] = false;

        int p = last;
        while (p != -1 && getTransition(p, c) == -1) {
            addTransition(p, c, cur);
            p = link[p];
        }

        if (p == -1) {
            link[cur] = 0;
        } else {
            int q = getTransition(p, c);
            if (len[p] + 1 == len[q]) {
                link[cur] = q;
            } else {
                int clone = size++;
                len[clone] = len[p] + 1;
                link[clone] = link[q];
                firstPos[clone] = firstPos[q];
                isClone[clone] = true;

                // Copy transitions from q to clone
                for (int i = head[q]; i != -1; i = nextEdge[i]) {
                    addTransition(clone, edgeChar[i], edgeTo[i]);
                }

                while (p != -1 && getTransition(p, c) == q) {
                    addTransition(p, c, clone);
                    p = link[p];
                }
                link[q] = link[cur] = clone;
            }
        }
        last = cur;
    }
    public void build(String text) {
        for (int i = 0; i < text.length(); i++) {
            extend(text.charAt(i), i);
        }
        // Build Link Tree for DFS
        for (int i = 1; i < size; i++) {
            int p = link[i];
            treeTo[treeEdgeCount] = i;
            treeNext[treeEdgeCount] = treeHead[p];
            treeHead[p] = treeEdgeCount++;
        }
    }
    public ArrayList<Integer> findOccurrences(String s) {
        int v = 0;
        for (int i = 0; i < s.length(); i++) {
            v = getTransition(v, s.charAt(i));
            if (v == -1) return new ArrayList<>();
        }
        ArrayList<Integer> res = new ArrayList<>();
        collectPositions(v, s.length(), res);
        return res;
    }
    private void collectPositions(int v, int patternLen, ArrayList<Integer> res) {
        if (v != 0 && !isClone[v]) {
            res.add(firstPos[v] - patternLen + 1);
        }
        for (int i = treeHead[v]; i != -1; i = treeNext[i]) {
            collectPositions(treeTo[i], patternLen, res);
        }
    }
}