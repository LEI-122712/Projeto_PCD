package projeto;
import java.util.*;



public class Main {
	
	
	public static void main(String[] args){
		// Cria uma pergunta
        String qtext = "Qual é o nível de gentileza do João?";
        String[] answers = {"Muito", "Pouco", "Mais ou Menos", "Não quero responder"};
        Question q = new Question(qtext, answers, 0, 10);

        // Cria um jogador e equipa de teste
        Player p1 = new Player(1,"Joao");
        Team team1 = new Team(1, "Team 1", List.of(p1), 0);
        List<Team> teams = List.of(team1);
        List<Question> questions = List.of(q);

        // Cria o GameState
        GameState gs = new GameState("ABC123", teams, questions);

        // Cria a GUI com GameState e jogador atual
        GUI window = new GUI(gs, p1);
        window.open();

        // Mostra a pergunta
        window.addQuestionFrame(q);

        // Espera 10 segundos (simulação)
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Mostra o placar
        window.addStatsFrame();
    }
		
		
	

}
