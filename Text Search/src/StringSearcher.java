import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class StringSearcher {
    private final String originalText;
    private final String normalizedText; // Lowercase
    private final List<Integer> lineStartIndices;
    public StringSearcher(String text) {
        this.originalText = text;
        this.normalizedText = text.toLowerCase();
        this.lineStartIndices = new ArrayList<>();
        lineStartIndices.add(0);
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\n' || text.charAt(i) == '\r') {
                if (i + 1 < text.length() && text.charAt(i + 1) != '\n' && text.charAt(i + 1) != '\r') {
                    lineStartIndices.add(i + 1);
                } else if (i + 1 < text.length() && text.charAt(i) == '\r' && text.charAt(i+1) == '\n') {
                    lineStartIndices.add(i + 2);
                    i++;
                }
            }
        }
    }
    private int getLineNumber(int textIndex) {
        for (int i = lineStartIndices.size() - 1; i >= 0; i--) {
            if (textIndex >= lineStartIndices.get(i)) {
                return i + 1;
            }
        }
        return 1;
    }
    private int[] computeLPSArray(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int length = 0;
        int i = 1;
        lps[0] = 0;
        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(length)) {
                length++;
                lps[i] = length;
                i++;
            } else {
                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }
    public List<Integer> KMPsearch(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        int N = text.length();
        int M = pattern.length();
        if (M == 0) return occurrences;
        if (N == 0) return occurrences;
        int[] lps = computeLPSArray(pattern);
        int i = 0;
        int j = 0;
        while (i < N) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }
            if (j == M) {
                occurrences.add(i - j);
                j = lps[j - 1];
            } else if (i < N && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return occurrences;
    }
    public List<SearchMatch> search(SearchQuery query) {
        List<SearchMatch> results = new ArrayList<>();
        String keyword = query.getKeyword();
        if (keyword == null || keyword.isEmpty()) {
            return results;
        }
        String searchText = (query.isCaseSensitive()) ? originalText : normalizedText;
        String searchPattern = (query.isCaseSensitive()) ? keyword : keyword.toLowerCase();
        if (query.getMatchMode().equals(MatchMode.SUBSTRING)) {
            // Use KMP for O(N+M) substring search
            List<Integer> indices = KMPsearch(searchText, searchPattern);
            for (int index : indices) {
                String matchedText = originalText.substring(index, index + keyword.length());
                int lineNumber = getLineNumber(index);
                results.add(new SearchMatch(lineNumber, index, matchedText));
            }
        } else if (query.getMatchMode().equals(MatchMode.WHOLE) || query.getMatchMode().equals(MatchMode.PREFIX)) {
            String patternString;
            if (query.getMatchMode().equals(MatchMode.WHOLE)) {
                patternString = "\\b" + Pattern.quote(keyword) + "\\b";
            } else { // PREFIX
                patternString = "\\b" + Pattern.quote(keyword);
            }
            Pattern pattern = Pattern.compile(patternString, query.isCaseSensitive() ? 0 : Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(originalText);
            while (matcher.find()) {
                int index = matcher.start();
                String matchedText = matcher.group();
                int lineNumber = getLineNumber(index);
                results.add(new SearchMatch(lineNumber, index, matchedText));
            }

        }
        return results;
    }
    public String replaceAndHighlight(List<SearchMatch> matches, String replaceTarget, String highlightStart, String highlightEnd) {
        if (matches.isEmpty() && replaceTarget == null) {
            return originalText;
        }
        StringBuilder result = new StringBuilder(originalText);
        matches.sort((m1, m2) -> Integer.compare(m2.startIndex, m1.startIndex));

        for (SearchMatch match : matches) {
            int start = match.startIndex;
            int end = match.startIndex + match.matchedText.length();
            String highlightedText = highlightStart + match.matchedText + highlightEnd;
            result.replace(start, end, highlightedText);
        }
        return result.toString();
    }
    public String performReplacement(List<SearchMatch> matches, String replaceTarget) {
        if (replaceTarget == null || matches.isEmpty()) {
            return originalText;
        }
        StringBuilder result = new StringBuilder(originalText);
        matches.sort((m1, m2) -> Integer.compare(m2.startIndex, m1.startIndex));
        for (SearchMatch match : matches) {
            int start = match.startIndex;
            int end = match.startIndex + match.matchedText.length();
            result.replace(start, end, replaceTarget);
        }
        return result.toString();
    }
}