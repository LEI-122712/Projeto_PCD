package Estrutura;

import com.google.gson.*;
import java.io.*;
import java.util.*;

public class QuestionLoader {

    public static List<Question> load(String filePath, int numQuestions) throws IOException {
		

        Reader reader = new InputStreamReader(new FileInputStream(filePath), java.nio.charset.StandardCharsets.UTF_8);
        Gson gson = new Gson();
        JsonObject root = gson.fromJson(reader, JsonObject.class);
        reader.close();

        List<Question> questionList = new ArrayList<>();
        JsonArray questionsArray = root.getAsJsonArray("questions");
        
        int count = 0;
        for (JsonElement elem : questionsArray) {
            if (count >= numQuestions) {
                break;
            }
            
            JsonObject qObj = elem.getAsJsonObject();
            
            String text = qObj.get("question").getAsString();
            int correct = qObj.get("correct").getAsInt();
            int points = qObj.get("points").getAsInt();
            
            JsonArray optsJson = qObj.getAsJsonArray("options");
            String[] options = new String[optsJson.size()];
            for(int k = 0; k < optsJson.size(); k++) {
                options[k] = optsJson.get(k).getAsString();
            }
            
            boolean isIndividual = (count % 2 == 0); 

            Question qst = new Question(text, options, correct, points, isIndividual);
            questionList.add(qst);
            
            count++;
        }

        return questionList;
    }
}