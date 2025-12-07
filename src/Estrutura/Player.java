package Estrutura;

public class Player {

	private final String name;
	private int score = 0; // comeca a zero a ideia e ir atualizando a cada pergunta mas ns bem

	public Player(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
