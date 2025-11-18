package Estrutura;
import java.util.List;
import java.util.ArrayList;

public class Team {
	
	private final int id;
	private final String teamName;
	private final List<Player> players;
	private int totalScore=0; //comeca a zero
	
	public Team(int id, String teamName, List<Player> players, int totalScore) {
		this.id = id;
		this.teamName = teamName;
		this.players = players;
	}

	

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}



	public int getId() {
		return id;
	}



	public String getTeamName() {
		return teamName;
	}



	public List<Player> getPlayers() {
		return players;
	}



	public int getTotalScore() {
		return totalScore;
	}
	
	
	
	
	
	

}
