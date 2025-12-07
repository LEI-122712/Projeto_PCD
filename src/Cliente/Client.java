package Cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import Servidor.Server;
import Estrutura.Message;

public class Client {

	private Socket connection;
	private ObjectInputStream in;   // MUDOU
    private ObjectOutputStream out; // MUDOU

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
                    waitForStart(); // Cria este método ou mete a lógica aqui
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

	void waitForStart() throws IOException, ClassNotFoundException {
        System.out.println("A aguardar início do jogo...");
        while (true) {
        Object obj = in.readObject(); // Fica bloqueado aqui à espera de msg
        if (obj instanceof Message) {
            Message msg = (Message) obj;
            if (msg.getType() == Message.Type.START_GAME) {
                System.out.println("O JOGO COMEÇOU!");
                // Aqui eventualmente será chamada a função para iniciar o ciclo de perguntas
                break; 
            }
            // Podemos adicionar else if para tratar de outras mensagens se for preciso
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
