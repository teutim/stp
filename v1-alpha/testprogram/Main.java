import de.teutim.stp.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static Expression tokenize(String source) {
        return Tokenizer.INSTANCE.tokenizeToExpr(source);
    }

    public static Expression parse(String source) {
        var result = Parser.Companion.getDEFAULT().parse(source);
        return result.hasLeft() ? result.getLeft() : result.getRight();
    }

    public static Expression evaluateHosted(String source) {
        return Expression.Companion.from("{?(parse (bytes-to-string-utf8 (io-read-bytes \"stdlib.stp\"))) " + source + "}").evaluate(new Context().setVersionProperty(1).inheritPrimitives());
    }

    public static Expression evaluateFreestanding(String source) {
        return Expression.Companion.from(source).evaluate(new Context().setVersionProperty(1).inheritPrimitives());
    }

    public static void main(String[] args) throws IOException {
        Context.Companion.registerDefaultPrimitives();
        Context.Companion.setScriptsPath("");
        Context.Companion.setOnlinePath("");
        Context.Companion.setEXPERIMENTAL_ALLOW_OVERWRITES_ONLY_ON_SYMBOLS(true);

        var br = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        while (!"exit".equals(input)) {
            if (input.startsWith("tokenize ")) {
                System.out.println("= " + tokenize(input.substring("tokenize ".length())));
            } else if (input.startsWith("parse ")) {
                System.out.println("= " + parse(input.substring("parse ".length())));
            } else if (input.startsWith("hosted ")) {
                System.out.println("= " + evaluateHosted(input.substring("hosted ".length())));
            } else if (input.startsWith("free ")) {
                System.out.println("= " + evaluateFreestanding(input.substring("free ".length())));
            } else if (!"".equals(input)) {
                try {
                    System.out.println("= " + evaluateHosted(input));
                } catch (ExitRequest seq) {
                    System.out.println("Script exited");
                }
            }
            System.out.print("> ");
            input = br.readLine();
        }
    }
}
