package Servidor;
import java.util.*;

import Estrutura.Player;
import Estrutura.Question;
import Estrutura.Team;

public class GameState {
	
	private final String roomCode;
    private final List<Question> questions;
    private int currentQuestion = 0;

    

	//o mapa das teams e p facilitar a busca
    private final Map<String, Team> teams = new HashMap<>();
    private Map<Player, Integer> playersAnswers = new HashMap<>();
    private Map<String, Integer> scoreboard = new HashMap<>();
    
	public GameState(String roomCode, List<Team> teams, List<Question> questions) {
		
		this.roomCode = roomCode;
		this.questions = questions;
		
		for (Team t : teams) {
            this.teams.put(t.getTeamName(), t);
            scoreboard.put(t.getTeamName(), 0);
        }
		
	}
	
	public Map<String,Team> getTeams() {
        return teams;
    }
	
	public List<Question> getQuestions() {
		return questions;
	}

	public Map<Player, Integer> getPlayersAnswers() {
		return playersAnswers;
	}

	public Map<String, Integer> getScoreboard() {
		return scoreboard;
	}

    public String getRoomCode() {
        return roomCode;
    }
	
	public Question getCurrentQuestion() {
        return questions.get(currentQuestion);
    }
	
	public void nextQuestion() {
        if (currentQuestion < questions.size() - 1) {
            currentQuestion++;
        }
    }
	
	//nao tenho a certeza!!
	public void submit(Player player, int option) {
	    playersAnswers.put(player, option);
	    //para testar
	    System.out.println("Jogador " + player.getId() + " respondeu com opção: " + option);
	}
	
	
	
	
	
	
	
    
    
    
	
	
    
    
	
	

}
