package Servidor;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;

import Estrutura.Question;
import Estrutura.QuestionLoader;

import java.util.Map;
import java.util.Random;
import java.util.HashMap;
import java.util.List;


public class Server {
	public static final int PORT=2025;
	
	private ServerSocket server; //server
	
	private Map<String, GameState> games = new HashMap<>();

	
	
	public void runServer(){
		try{
			server = new ServerSocket(PORT);
			new Thread(new Runnable(){
				
				
				
				@Override
				public void run() {
					Scanner sc = new Scanner(System.in);
				    while (true) {
				        System.out.print("> ");
				        String cmd = sc.nextLine();
				        processCommand(cmd);
					
				}
				
			}}).start();
			while(true){
				waitForConnection();
				
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(server != null){
				try{
					server.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	private void waitForConnection() throws IOException{
		Socket connection = server.accept();
		DealWithClient handler= new DealWithClient(connection);
		handler.start();
		System.out.println("Started new connection...");
	}
	

	
	// connection handler é so para o servidor?
	private class DealWithClient extends Thread{
		private Socket connection; //connection
		private Scanner in; //stream reader
		private PrintWriter out; //stream writer
		
		public DealWithClient(Socket connection){
			this.connection=connection;
		}
		
		@Override
		public void run(){
			try{
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
		
		private void processConnection(){
			while(true){
				//TODO
			}
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
		
		
		
	}
	
	public String createCode(){
		Random random= new Random();
		String code="";
		while(true){ //gera codigos ate obter um original que nao exista ja
			int codeSize=(int)(Math.random()*(9-3+1)+3); //numero entre 3 e 9
			for(int i=0; i<codeSize; i++){
				int n=(int)(Math.random()*10);
				code+=n;
			}
			if(!games.containsKey(code)) break;
		}
		
		return code;
		
	}
	
	public void processCommand(String cmd){
		String[] s=cmd.split(" ");
		if(s[0].equals("new")){
			int numTeams=Integer.parseInt(s[1]);
			int numTeamPlayers=Integer.parseInt(s[2]);
			int numQuestions=Integer.parseInt(s[3]);
			
			String code= createCode();
			List<Question> questions = null;
	        try {
	            questions = QuestionLoader.load("dados/quizzes.json", numQuestions); 
	        } catch (Exception e) {
	            e.printStackTrace();
	            return;
	        }
			GameState g=new GameState(code, numTeams, numTeamPlayers, questions);
			games.put(code, g);
			
			System.out.println("Nova sala criada com o código "+ code);
		}
		
		
	}
	
	public static void main(String[] args){
		
		new Server().runServer();
		
	}
}