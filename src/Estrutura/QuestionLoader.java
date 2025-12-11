package Estrutura;

import com.google.gson.*;
import java.io.*;
import java.util.*;

public class QuestionLoader {

    public static List<Question> load(String filePath, int numQuestions) throws IOException {
		// mudei aqui em vez de ele receber o nome do quizz recebe o numero de perguntas
		// a incluir no jogo (ta no enunciado que na criacao do novo jogo ele recebe o
		// nr de perguntas) - joana

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
            
            // 1. Extrair os dados básicos do JSON
            String text = qObj.get("question").getAsString();
            int correct = qObj.get("correct").getAsInt();
            int points = qObj.get("points").getAsInt();
            
            // 2. Converter o array de opções JSON para String[]
            JsonArray optsJson = qObj.getAsJsonArray("options");
            String[] options = new String[optsJson.size()];
            for(int k = 0; k < optsJson.size(); k++) {
                options[k] = optsJson.get(k).getAsString();
            }
            
            // 3. Determinar se é individual ou equipa (Alternadamente)
            boolean isIndividual = (count % 2 == 0); 

            // 4. Criar o objeto manualmente
            Question qst = new Question(text, options, correct, points, isIndividual);
            questionList.add(qst);
            
            count++;
        }

        return questionList;
    }
}