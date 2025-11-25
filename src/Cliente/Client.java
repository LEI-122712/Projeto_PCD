package Cliente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import Servidor.Server;


public class Client {
	
	private Socket connection;
	private Scanner in;
	private PrintWriter out;
	
	private String roomCode;
    private String teamName;
    private String username;
	
	

	public Client(String roomCode, String teamName, String username) {
		this.roomCode = roomCode;
		this.teamName = teamName;
		this.username = username;
	}

	
	
	public void runClient(){
		try{
			connectToServer();
			setStreams();
			processConnection();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			closeConnection();
		}
	}
	private void setStreams() throws IOException{
		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())), true);
		in = new Scanner(connection.getInputStream());
	}
	
	
	void connectToServer() throws IOException {
		InetAddress endereco = InetAddress.getByName(null);
		System.out.println("Endereco:" + endereco);
		connection = new Socket(endereco, Server.PORT);
		System.out.println("Socket:" + connection);
		
	}
	
	void processConnection() throws IOException {
		
		out.println(roomCode + " " + teamName + " " + username);
		if (!in.hasNextLine()) {
            System.out.println("Sem resposta do servidor.");
            return;
        }
		String response = in.nextLine();
		if(response.startsWith("ACCEPT")) {
	        System.out.println("Ligacao ao jogo estabelecida com sucesso.");
	    } else {
	        System.out.println(response);
	        return;
	    }
		
		 // aguarda sinal de START do servidor
        System.out.println("Aguardando o START do servidor...");
        while (in.hasNextLine()) {
            String line = in.nextLine();
            if (line.equals("START")) {
                System.out.println("Jogo iniciado!");
                break;
            } else {
            	//outra msg
                System.out.println("Servidor: " + line);
            }
        }
		
		//GUI??
		//out.println("FIM");
	}
	
	public void closeConnection(){
		try{
			if(connection!=null)
				connection.close();
			if(in != null)
				in.close();
			if(out != null)
				out.close();
			
		}catch (IOException e){
			e.printStackTrace();
			
		}
	}
	
	public static void main(String[] args) {
		
		if(args.length != 3) {
	        System.out.println("Insira os dados no formato <Jogo> <Equipa> <Username>");
	        return;
	    }

	    String roomCode = args[0];
	    String teamName = args[1];
	    String username = args[2];

	    new Client(roomCode, teamName, username).runClient();
	}

}

