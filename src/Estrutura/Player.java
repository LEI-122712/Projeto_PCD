package Estrutura;

public class Player {
	
	private final int id; //ainda ns se e preciso usar id eu so meti
	private final String name;
	private int score=0; //comeca a zero a ideia e ir atualizando a cada pergunta mas ns bem
	
	public Player(int id, String name) {
		this.id = id;
		this.name=name;
	}
	
	
	public int getId() {
		return id;
	}




	public int getScore() {
		return score;
	}


	public void setScore(int score) {
		this.score = score;
	}


	
	
	
	
	

}
