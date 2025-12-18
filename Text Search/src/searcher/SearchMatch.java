package searcher;

public class SearchMatch {
    public final int lineNumber;
    public final int startIndex;
    public final String matchedText;
    public SearchMatch(int lineNumber, int startIndex, String matchedText) {
        this.lineNumber = lineNumber;
        this.startIndex = startIndex;
        this.matchedText = matchedText;
    }
    @Override
    public String toString() {
        return "{Line: " + lineNumber + ", Index: " + startIndex + ", Match: \"" + matchedText + "\"}";
    }
}