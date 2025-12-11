package Cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Map;

import Servidor.Server;
import Estrutura.Message;
import Estrutura.Question;

public class Client {

	private Socket connection;
	private ObjectInputStream in;   // MUDOU
    private ObjectOutputStream out; // MUDOU
	private GUI gui;
	private String roomCode;
	private String teamName;
	private String username;

	public Client(String roomCode, String teamName, String username) {
		this.roomCode = roomCode;
		this.teamName = teamName;
		this.username = username;
	}

	public void runClient() {
		try {
			this.gui = new GUI(this);
			connectToServer();
			setStreams();
			processConnection();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}

	private void setStreams() throws IOException {
        // Tal como no servidor: Output primeiro!
        out = new ObjectOutputStream(connection.getOutputStream());
        out.flush();
        in = new ObjectInputStream(connection.getInputStream());
    }

	void connectToServer() throws IOException {
		InetAddress endereco = InetAddress.getByName(null);
		System.out.println("Endereco:" + endereco);
		connection = new Socket(endereco, Server.PORT);
		System.out.println("Socket:" + connection);

	}

	void processConnection() throws IOException {
        try {
            // 1. Enviar pedido de Login
            String loginData = roomCode + " " + teamName + " " + username;
            out.writeObject(new Message(Message.Type.LOGIN, loginData, username));
            
            // 2. Aguardar resposta
            Object responseObj = in.readObject();
            if (responseObj instanceof Message) {
                Message response = (Message) responseObj;
                
                if (response.getType() == Message.Type.LOGIN_SUCCESS) {
                    System.out.println("Ligação ao jogo estabelecida!");
                    waitForStart();
                } else {
                    System.out.println("Erro: " + response.getContent());
                    return;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

		// GUI??
		// out.println("FIM");
	}

	public void sendAnswer(int index) {
        try {
            // Envia o índice da resposta como Integer dentro da mensagem
            out.writeObject(new Message(Message.Type.ANSWER, index, username));
            out.flush(); // Importante para seguir imediatamente
        } catch (IOException e) {
            System.out.println("Erro ao enviar resposta: " + e.getMessage());
        }
    }

	void waitForStart() throws IOException, ClassNotFoundException {
        System.out.println("A aguardar início do jogo...");
        
        // 2. ABRIR A JANELA DE ESPERA
        gui.open(); 

        while (true) {
            Object obj = in.readObject();
            if (obj instanceof Message) {
                Message msg = (Message) obj;
                
                switch (msg.getType()) {
                    case START_GAME:
                        System.out.println("O JOGO COMEÇOU!");
                        break;
                        
                    case QUESTION:
                        Question q = (Question) msg.getContent();
                        System.out.println("Recebi pergunta: " + q.getQuestion());
                        // 3. ATUALIZAR A GUI COM A PERGUNTA
                        gui.addQuestionFrame(q); 
                        break;
                        
                    case SCORE_UPDATE:
                        Map<String, Integer> scores = (Map<String, Integer>) msg.getContent();
                        // 4. ATUALIZAR A GUI COM O PLACAR
                        gui.addStatsFrame(scores);
                        break;
                        
                    case END_GAME:
                        System.out.println("Fim do jogo!");
                        gui.endOfGame();
                        return; // Sai e fecha
                        
                    default:
                        System.out.println("Msg desconhecida: " + msg.getType());
                }
            }
        }
    }
	public void closeConnection() {
		try {
			if (connection != null)
				connection.close();
			if (in != null)
				in.close();
			if (out != null)
				out.close();

		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	public static void main(String[] args) {

		if (args.length != 3) {
			System.out.println("Insira os dados no formato <Jogo> <Equipa> <Username>");
			return;
		}

		String roomCode = args[0];
		String teamName = args[1];
		String username = args[2];

		new Client(roomCode, teamName, username).runClient();
	}

}
