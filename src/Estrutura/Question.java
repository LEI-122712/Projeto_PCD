package Estrutura;

public class Question {
	
	private final String question;
	private final String[] options;
	private final int correct;
	private final int points;
	private final boolean individualQuestion=false; //depois para ver se a pergunta e individual ou nao

	
	public Question(String quest, String[] options, int correct, int points /*, boolean individualQuestion */) {
		this.question = quest;
		this.options = options;
		this.correct = correct;
		this.points = points;
		/*this.individualQuestion=individualQuestion;*/
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
