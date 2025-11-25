package Servidor;
import java.util.*;

import Estrutura.Player;
import Estrutura.Question;
import Estrutura.Team;

public class GameState {
	
	private final String roomCode;
    private final List<Question> questions;
    private int currentQuestion = 0;
    
    private final int numTeams;
    private final int numTeamPlayers;
    //private final int numQuestions; //a considerar

    

	//o mapa das teams e p facilitar a busca
    private final Map<String, Team> teams = new HashMap<>();
    private Map<Player, Integer> playersAnswers = new HashMap<>();
    private Map<String, Integer> scoreboard = new HashMap<>();
    //cronometro?
    //a considerar
    private int connectedPlayers = 0;
    private final int totalPlayersExpected;

    
	public GameState(String roomCode, int numTeams, int numTeamPlayers, List<Question> questions) {
		
		this.roomCode = roomCode;
		this.numTeams=numTeams;
		this.numTeamPlayers=numTeamPlayers;
		this.questions = questions;
		// a considerar
		this.totalPlayersExpected = numTeams * numTeamPlayers;
		
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
