package Estrutura;

import java.io.Serializable;

public class Question implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String question;
	private final String[] options;
	private final int correct;
	private final int points;
	private final boolean individualQuestion;	

	
	public Question(String quest, String[] options, int correct, int points, boolean individualQuestion) {
		this.question = quest;
		this.options = options;
		this.correct = correct;
		this.points = points;
		this.individualQuestion = individualQuestion;
	}

	public boolean isIndividualQuestion() {
		return individualQuestion;
	}

	public String getQuestion() {
		return question;
	}

	public String[] getOptions() {
		return options;
	}

	public int getCorrect() {
		return correct;
	}

	public int getPoints() {
		return points;
	}

}
