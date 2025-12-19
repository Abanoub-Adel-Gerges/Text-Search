package searcher;
import suffixautomaton.SuffixAutomaton;
import printer.*;
import datastructures.ArrayList;
public class StringSearcher {
    private final String originalText;
    private final ArrayList<Integer> lineStarts;
    private final SuffixAutomaton samSensitive;
    private final SuffixAutomaton samInsensitive;
    public StringSearcher(String text) {
        this.originalText = text;
        this.lineStarts = computeLineStarts(text);

        // O(n) Build
        samSensitive = new SuffixAutomaton(text.length());
        samInsensitive = new SuffixAutomaton(text.length());

        samSensitive.build(text);
        samInsensitive.build(text.toLowerCase());
    }
    private ArrayList<Integer> computeLineStarts(String t) {
        ArrayList<Integer> res = new ArrayList<>();
        res.add(0);
        for (int i = 0; i < t.length(); i++) {
            if (t.charAt(i) == '\n') {
                res.add(i + 1);
            }
        }
        return res;
    }
    private int getLine(int index) {
        int l = 0, r = lineStarts.size() - 1, ans = 0;
        while (l <= r) {
            int m = (l + r) / 2;
            if (lineStarts.get(m) <= index) {
                ans = m;
                l = m + 1;
            } else r = m - 1;
        }
        return ans + 1;
    }
    public ArrayList<SearchMatch> search(SearchQuery sq) {
        if (sq.getKeyword() == null || sq.getKeyword().isEmpty()) {
            return new ArrayList<>();
        }
        ArrayList<SearchMatch> results = new ArrayList<>();
        String keyword = sq.getKeyword().trim();
        String lookupKey = sq.isCaseSensitive() ? keyword : keyword.toLowerCase();
        SuffixAutomaton sam = sq.isCaseSensitive() ? samSensitive : samInsensitive;
        // O(k) Find all raw occurrences
        ArrayList<Integer> rawPositions = sam.findOccurrences(lookupKey);
        // Ensure unique positions and sort them to maintain file order
        rawPositions.sort();
        rawPositions.removeDuplicates();

        for (int pos : rawPositions) {
            if (isValidMatch(pos, lookupKey.length(), sq.getMatchMode())) {
                int lineNum = getLine(pos);
                int colNum = pos - lineStarts.get(lineNum - 1);

                results.add(new SearchMatch(
                        lineNum,
                        colNum,
                        originalText.substring(pos, pos + lookupKey.length())
                ));
            }
        }
        return results;
    }
    private boolean isValidMatch(int pos, int len, String mode) {
        if (mode.equals(MatchMode.SUBSTRING)) return true;
        boolean startBoundary = (pos == 0 || !Character.isLetterOrDigit(originalText.charAt(pos - 1)));
        boolean endBoundary = (pos + len >= originalText.length() || !Character.isLetterOrDigit(originalText.charAt(pos + len)));
        if (mode.equals(MatchMode.PREFIX)) return startBoundary;
        if (mode.equals(MatchMode.WHOLE)) return startBoundary && endBoundary;

        return false;
    }
    public String getHighlightedText(ArrayList<SearchMatch> matches) {
        StringBuilder sb = new StringBuilder(originalText);
        // Work backwards to not invalidate indices
        for (int i = matches.size() - 1; i >= 0; i--) {
            SearchMatch m = matches.get(i);
            // We need the absolute index. Calculation: lineStart + column
            int absIdx = lineStarts.get(m.lineNumber - 1) + m.startIndex;
            sb.insert(absIdx + m.matchedText.length(), "$");
            sb.insert(absIdx, "$");
        }
        return sb.toString();
    }
    public void printHighlightedText(ArrayList<SearchMatch> matches) {
        if (matches == null || matches.isEmpty()) {
            Printer.print(originalText, ConsoleColors.PURPLE);
            return;
        }
        int lastIndex = 0;
        for (SearchMatch m : matches) {
            int matchStart = lineStarts.get(m.lineNumber - 1) + m.startIndex;
            int matchEnd = matchStart + m.matchedText.length();
            if (matchStart > lastIndex) {
                Printer.print(originalText.substring(lastIndex, matchStart), ConsoleColors.PURPLE);
            }
            Printer.print(originalText.substring(matchStart, matchEnd), ConsoleColors.BRIGHT_RED);
            lastIndex = matchEnd;
        }
        if (lastIndex < originalText.length()) {
            Printer.print(originalText.substring(lastIndex), ConsoleColors.PURPLE);
        }
        Printer.println();
    }
    public String getReplacedText(ArrayList<SearchMatch> matches, String replacement) {
        if (replacement == null || matches == null || matches.isEmpty()){return originalText;}
        StringBuilder sb = new StringBuilder(originalText.length());
        int lastIndex = 0;
        for (SearchMatch m : matches) {
            int matchStart = lineStarts.get(m.lineNumber - 1) + m.startIndex;
            sb.append(originalText, lastIndex, matchStart);
            sb.append(replacement);
            lastIndex = matchStart + m.matchedText.length();
        }
        if (lastIndex < originalText.length()) {
            sb.append(originalText, lastIndex, originalText.length());
        }
        return sb.toString();
    }
}
