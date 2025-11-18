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
		//TODO
		out.println("FIM");
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
		new Client().runClient();
	}

}

