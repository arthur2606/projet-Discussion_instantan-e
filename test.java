package clientserveur2;

import java.io.IOException;

public class test {

	public static void main(String[] args) throws IOException {
		
	String 	host="127.0.0.1";
		 int port = 6001;
	        Serveur server = new Serveur(port);
	        server.run(); //on lance le serveur sur le port 5000
	       Client user1=new Client(host,port,"Arthur");
	       Client user2=new Client(host,port,"Roberto");
	      user2.sendmsg("Arthur", "bienvenu"); //il envoie un meesage à l'utilisateur 1
	      user1.logoff();  //utilisateur 1 se deconnecte
	      

	}

}
