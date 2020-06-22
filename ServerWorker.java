package clientserveur2;
import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class ServerWorker extends Thread{
	

	    private final Socket clientSocket;
	    private final Serveur serveur;
	    private String login = null;
	    public String getLogin() {
			return login;
		}

		private OutputStream outputStream;
	    private HashSet<String> topicSet = new HashSet<>();  //liste des login ou utilisateur presents

	    public ServerWorker(Serveur serveur, Socket clientSocket) {
	        this.serveur = serveur;
	        this.clientSocket = clientSocket;
	    }


	    public void run() {
	        try {
	            handleClientSocket();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }

	    private void handleClientSocket() throws IOException, InterruptedException {
	    	
	    	String line;
	    	 InputStream inputStream = clientSocket.getInputStream();
	         this.outputStream = clientSocket.getOutputStream();
	    	 try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
				while ( (line = reader.readLine()) != null) {
				    String[] tokens = StringUtils.split(line);
				    if (tokens != null && tokens.length > 0) {
				        String cmd = tokens[0];
				        if ("logoff".equals(cmd) || "quit".equalsIgnoreCase(cmd)) {
				        	turnoff();
				            break;
				        }  else if ("msg".equalsIgnoreCase(cmd)) {
				            String[] tokensMsg = StringUtils.split(line, null, 3);
				            Message(tokensMsg);
				        } else if ("join".equalsIgnoreCase(cmd)) {
				            Join(tokens);
				        }else {
				            System.out.println("error");
				        }
				    }
				}
			}
	  }

	//methode permettant d'ajouter un utilisateur
	    
	    private void Join(String[] tokens) {
	       
	    	 if (tokens.length > 1) {
	             String topic = tokens[1];
	             topicSet.add(topic);
	         }
	        }
	    

	    //cette nous permet d'envoyer un message
	    private void Message(String[] tokens) throws IOException {
	    	//le tokens[0] =msg pour preciser qu'il enverra un message
	        String sendTo = tokens[1];
	        String body = tokens[2];

	        List<ServerWorker> workerList = serveur.getWorkerList();
	        for(ServerWorker worker : workerList) {
	            if (worker.login==sendTo) {
	                    String outMsg = "msg " + sendTo + ":" + login + " " + body + "\n";
	                    worker.send(outMsg);
	                }
	            
	        }
	    }

	    private void turnoff() throws IOException {
	        serveur.removeWorker(this);
	        List<ServerWorker> workerList = serveur.getWorkerList();

	        // afficher le statut des autres utilisateurs
	        
	        String onlineMsg = "offline " + login + "\n";
	        for(ServerWorker worker : workerList) {
	            if (!login.equals(worker.getLogin())) {
	                worker.send(onlineMsg);
	            }
	        }
	        clientSocket.close();
	    }
	    
	    //methode pour envoyer un messsage
	  
	    private void send(String msg) throws IOException {
	        if (login != null) {
	            try {
	                outputStream.write(msg.getBytes());
	            } catch(Exception ex) {
	                ex.printStackTrace();
	            }
	        }
	    }
	}



