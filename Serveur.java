package clientserveur2;
import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;




public class Serveur {

	 private final int serverPort;
	    private ArrayList<ServerWorker> Listserveur = new ArrayList<>();   //liste des communications actives

	    public Serveur(int serverPort) {
	        this.serverPort = serverPort;
	    }

		public ArrayList<ServerWorker> getWorkerList() {
			return Listserveur;
		}


		public void run() {
	        try {
	        	
	        	//on crée un socket coté Serveur qui repondra au socket du client sur un port 'serverport'
	        	
	            ServerSocket serverSocket = new ServerSocket(serverPort);
	            while(true) {
	                System.out.println("connection etablie...");
	                Socket clientSocket = serverSocket.accept(); //accepte la communication du client
	                System.out.println("connecté à " + clientSocket);
	                ServerWorker worker = new ServerWorker(this, clientSocket); 
	                Listserveur.add(worker);  //on ajoute une communication client-serveur
	                worker.start();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    public void removeWorker(ServerWorker serverWorker) {
	        Listserveur.remove(serverWorker);
	    }
}
