package projeto;
import com.google.gson.*;
import java.io.*;
import java.util.*;


public class QuestionLoader {
	public static List<Question> load(String filePath, String quizName) throws FileNotFoundException{
		Reader reader = new FileReader("dados/perguntas.json");
		Gson gson = new Gson();
		JsonObject root = gson.fromJson(reader, JsonObject.class);
		JsonArray quizzes = root.getAsJsonArray("quizzes");
		return new ArrayList<Question>();

		

	}

}
