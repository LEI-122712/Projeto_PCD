package Servidor;

import java.util.*;
import java.io.ObjectOutputStream;
import java.io.IOException;
import Estrutura.Message;
import Estrutura.Player;
import Estrutura.Question;
import Estrutura.Team;
import Servidor.ModifiedCountDownLatch;

public class GameState {

	private final String roomCode;
	private final List<Question> questions;
	private int currentQuestion = 0;
	private ModifiedCountDownLatch currentLatch;
	private final int numTeams;
	private final int numTeamPlayers;
	// private final int numQuestions; //a considerar

	// o mapa das teams e p facilitar a busca
	private final Map<String, Team> teams = new HashMap<>();
	private Map<Player, Integer> playersAnswers = new HashMap<>();
	private Map<String, Integer> scoreboard = new HashMap<>();
	private final List<ObjectOutputStream> outputStreams = new ArrayList<>(); //para registar os canais de escrita de todos os jogadores
	// cronometro?
	// a considerar
	private int connectedPlayers = 0;
	private final int totalPlayersExpected;


	public GameState(String roomCode, int numTeams, int numTeamPlayers, List<Question> questions) {

		this.roomCode = roomCode;
		this.numTeams = numTeams;
		this.numTeamPlayers = numTeamPlayers;
		this.questions = questions;
		// a considerar
		this.totalPlayersExpected = numTeams * numTeamPlayers;

	}

	public Map<String, Team> getTeams() {
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

	public boolean isTeamFull(Team team) {
		return team.getNumPlayers() == numTeamPlayers;
	}

	public boolean reachedTeamLimit() {
		return teams.size() == numTeams;
	}

	public void addConnectedPlayers() {
		connectedPlayers++;
	}

	public synchronized boolean areAllPlayersConnected() {
		return connectedPlayers == totalPlayersExpected;
	}
	
	public synchronized Player getPlayer(String username) {
	    for (Team t : teams.values()) {
	        for (Player p : t.getPlayers()) {
	            if (p.getName().equals(username)) {
	                return p;
	            }
	        }
	    }
	    return null;
	}


	public List<String> getAllUsernames() {
		List<String> list = new ArrayList<>();

		for (Team t : teams.values()) {
			for (Player p : t.getPlayers()) {
				list.add(p.getName());
			}
		}
		return list;
	}

	// Método para registar um novo canal de output
    public synchronized void addPlayerStream(ObjectOutputStream out) {
        outputStreams.add(out);
    }

    // Método para enviar mensagem a TODOS os jogadores deste jogo
    public synchronized void broadcast(Message msg) {
        for (ObjectOutputStream out : outputStreams) {
            try {
                out.writeObject(msg);
                out.reset(); // Importante para evitar cache de objetos repetidos
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean repeatedUsername(String username){
    	List<String> usernames= getAllUsernames();
    	for (String u : usernames) {
			if (u.equals(username))
				return true;
		}
    	return false;
    }
    
    
    public synchronized LoginResult addTeamAndPlayer(ObjectOutputStream out, String teamName, String username){
    	if (repeatedUsername(username)) {
            return LoginResult.USERNAME_EXISTS;
        }
		Team team = teams.get(teamName);
		if (team == null) {
			if (reachedTeamLimit()) {
				 return LoginResult.TEAM_LIMIT_REACHED;
			}
			team = new Team(teamName);
			teams.put(teamName, team);
		} else {
			if (isTeamFull(team)) {
				return LoginResult.TEAM_FULL;
			}
		}

		Player newPlayer = new Player(username);
		team.addPlayer(newPlayer);
		

		
		addPlayerStream(out);

		addConnectedPlayers();
		
		return LoginResult.OK;
		
    }

	public void runGame() {
        try {
            System.out.println("Início do Ciclo de Jogo [" + roomCode + "]");
            
            // Pausa inicial para garantir que clientes carregaram a GUI
            Thread.sleep(2000);

            for (Question q : questions) {
                currentQuestion = questions.indexOf(q);
                
                // 1. Enviar Pergunta a todos
                System.out.println("A enviar pergunta: " + q.getQuestion());
                broadcast(new Message(Message.Type.QUESTION, q, "Server"));

                // 2. Preparar Sincronização
                if (q.isIndividualQuestion()) {
                    System.out.println("Ronda Individual. A criar Latch...");
                    // Exemplo: Bónus x2 para os primeiros 2, 30 segundos, total de jogadores conectados
                    // Podes ajustar o '2' (bonusCount) conforme o número de jogadores
                    int bonusCount = Math.max(1, connectedPlayers / 2); 
                    currentLatch = new ModifiedCountDownLatch(2, bonusCount, 30, connectedPlayers);
                    
                    // 3. Bloquear a thread do jogo à espera das respostas ou do tempo
                    currentLatch.await(); 
                    
                } else {
                    // TODO: Implementar lógica da Barreira (Fase seguinte - Perguntas de Equipa)
                    System.out.println("Pergunta de equipa (Barreira). Saltando espera temporariamente...");
                    Thread.sleep(5000); 
                }

                // 4. Fim da Ronda: Enviar pontuações atualizadas
                // Vamos enviar o scoreboard (Map<String, Integer>)
                broadcast(new Message(Message.Type.SCORE_UPDATE, new HashMap<>(scoreboard), "Server"));
                
                // Pausa para os jogadores verem o resultado
                Thread.sleep(3000);
            }

            // Fim do Jogo
            broadcast(new Message(Message.Type.END_GAME, "O jogo terminou!", "Server"));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Processa a resposta de um jogador.
     * Chamado pela thread 'DealWithClient' quando recebe uma msg ANSWER.
     */
    public synchronized void submitAnswer(String username, int answerIndex) {
        // Encontra o jogador
        Player player = getPlayer(username);
        if (player == null) return;

        Question curQ = getCurrentQuestion();
        
        // Verifica se acertou
        if (answerIndex == curQ.getCorrect()) {
            int points = curQ.getPoints();
            
            // Se for individual, usa o Latch para ver se tem bónus
            if (curQ.isIndividualQuestion() && currentLatch != null) {
                int bonus = currentLatch.countDown(); // Decrementa e devolve o fator
                points *= bonus;
                if (bonus > 1) System.out.println("JOGADOR " + username + " GANHOU BÓNUS!");
            }
            
            // Atualiza pontuação do Jogador
            player.setScore(player.getScore() + points);
            
            // Atualiza scoreboard global (simplificado por agora: nome -> pontos)
            // Idealmente aqui somavas também à equipa
            scoreboard.put(username, player.getScore()); 
        } else {
            // Se errou, também precisamos de avisar o Latch que este jogador já respondeu?
            // Sim, para o jogo não ficar à espera dele até ao fim do tempo.
            // O countDown devolve 1 (sem bónus) mas decrementa o contador de respostas pendentes.
            if (curQ.isIndividualQuestion() && currentLatch != null) {
                currentLatch.countDown(); 
            }
        }
    }

}
