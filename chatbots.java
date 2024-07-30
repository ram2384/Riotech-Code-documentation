import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.Properties;
import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.StanfordCoreNLPProperties;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;

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
public class AdvancedChatBot {
    private static final String JOKE_API_URL = "https://v2.jokeapi.dev/joke/Any";
    private static StanfordCoreNLP pipeline;

    public static void main(String[] args) {
        initializeNLP();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello! I am your advanced chatbot. How can I assist you today?");
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Goodbye!");
                break;
            } else if (input.toLowerCase().contains("joke")) {
                handleJokeRequest();
            } else {
                handleGeneralQuery(input);
            }
        }
        scanner.close();
    }

    private static void initializeNLP() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);
    }

    private static void handleJokeRequest() {
        try {
            URL url = new URL(JOKE_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONObject json = new JSONObject(new JSONTokener(response.toString()));
            String joke;
            if (json.has("joke")) {
                joke = json.getString("joke");
            } else {
                joke = json.getJSONObject("setup") + " " + json.getJSONObject("delivery");
            }
            System.out.println("Here's a joke for you: " + joke);
        } catch (Exception e) {
            System.out.println("Error fetching joke.");
        }
    }

    private static void handleGeneralQuery(String input) {
        String sentiment = analyzeSentiment(input);
        System.out.println("You said: " + input);
        System.out.println("Sentiment: " + sentiment);
    }

    private static String analyzeSentiment(String text) {
        Annotation document = new Annotation(text);
        pipeline.annotate(document);
        String sentiment = "Unknown";
        for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
            sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
        }
        return sentiment;
    }
}

