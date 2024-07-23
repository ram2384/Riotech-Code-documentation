import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class SimpleChatbot {

    private static Map<Pattern, String> patterns = new HashMap<>();

    static {
        patterns.put(Pattern.compile(".*\\b(hi|hello|hey)\\b.*", Pattern.CASE_INSENSITIVE), "Hello! How can I help you?");
        patterns.put(Pattern.compile(".*\\b(how are you|how's it going)\\b.*", Pattern.CASE_INSENSITIVE), "I'm just a bunch of code, but I'm doing great! How about you?");
        patterns.put(Pattern.compile(".*\\b(what is your name|who are you)\\b.*", Pattern.CASE_INSENSITIVE), "I'm SimpleChatbot, your virtual assistant.");
        patterns.put(Pattern.compile(".*\\b(bye|exit|goodbye)\\b.*", Pattern.CASE_INSENSITIVE), "Goodbye! Have a great day!");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean keepChatting = true;

        System.out.println("Hello! I'm a simple chatbot. How can I assist you today?");

        while (keepChatting) {
            System.out.print("You: ");
            String userInput = scanner.nextLine();

            boolean matched = false;
            for (Map.Entry<Pattern, String> entry : patterns.entrySet()) {
                if (entry.getKey().matcher(userInput).find()) {
                    System.out.println("Chatbot: " + entry.getValue());
                    matched = true;
                    if (entry.getValue().equals("Goodbye! Have a great day!")) {
                        keepChatting = false;
                    }
                    break;
                }
            }

            if (!matched) {
                System.out.println("Chatbot: I'm sorry, I don't understand that. Can you rephrase?");
            }
        }

        scanner.close();
    }
}
