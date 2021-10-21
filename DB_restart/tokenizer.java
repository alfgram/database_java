import queryExceptions.queryException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class tokenizer {
    int currentTokenIndex = 0;
    ArrayList<String> tokenArray = new ArrayList<>();

    public tokenizer(String query) throws queryException {
        StringTokenizer Token = new StringTokenizer(query, ",;'() ", true);
        removeWhiteSpace(Token);
        if (!tokenArray.get(tokenArray.size() - 1).equals(";")) throw new queryException();
        tokenArray.remove(tokenArray.size() - 1);
    }

    public Boolean hasMoreTokens() {
        return (currentTokenIndex < tokenArray.size() - 1);
    }

    private void removeWhiteSpace(StringTokenizer t) {
        String currentToken;
        do  {
            currentToken = t.nextToken();
            if (currentToken.equals("'")) {
                parseStringLiteral(t);
            }
            else if (!currentToken.equals(" ")) {
                tokenArray.add(currentToken);
            }
        } while (t.hasMoreElements());
    }

    private void parseStringLiteral(StringTokenizer t) {
        String stringLiteral = "";
        String currentToken = t.nextToken();
        while (!currentToken.equals("'") && t.hasMoreElements()) {
            stringLiteral += currentToken;
            currentToken = t.nextToken();
        }
        tokenArray.add(stringLiteral);
    }

    public String nextToken() throws queryException {
        if (!this.hasMoreTokens()) throw new queryException();
        currentTokenIndex++;
        return tokenArray.get(currentTokenIndex);

    }

    public String getFirstToken() {
        return tokenArray.get(0);
    }
}
