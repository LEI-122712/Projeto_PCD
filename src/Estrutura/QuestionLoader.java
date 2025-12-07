package Estrutura;

import com.google.gson.*;
import java.io.*;
import java.util.*;

public class QuestionLoader {
	// mudei aqui em vez de ele receber o nome do quizz recebe o numero de perguntas
	// a incluir no jogo (ta no enunciado que na criacao do novo jogo ele recebe o
	// nr de perguntas)

	public static List<Question> load(String filePath, int numQuestions) throws IOException {
		// para nao desformatar
		Reader reader = new InputStreamReader(new FileInputStream(filePath), java.nio.charset.StandardCharsets.UTF_8);
		Gson gson = new Gson();
		JsonObject root = gson.fromJson(reader, JsonObject.class);
		reader.close();

		List<Question> questionList = new ArrayList<>();
		JsonArray questions = root.getAsJsonArray("questions");
		int i = 1;
		for (JsonElement elem : questions) {
			if (i > numQuestions) {
				break;
			}
			JsonObject question = elem.getAsJsonObject();
			Question qst = gson.fromJson(question, Question.class);
			questionList.add(qst);
			i++;
		}

		return questionList;
	}

}
