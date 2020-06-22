package clientserveur2;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;




public class Client {
	
private String login;
private	 String password;
private Socket socket;
private OutputStream serverOut;
private BufferedReader bufferedIn;
private final String serverName;
private final int serverPort;
private ArrayList<Client> userStatut = new ArrayList<>();  //table des utilisateurs avec leurn statut
private ArrayList<Client> messageRecu = new ArrayList<>();    //table des messages recus
private String[] type_communication =new String[1];
 
public Client(String host, int port,String login) {
	
	
	 this.serverName = host;
     this.serverPort = port;
     this.login=login;
	
}

 
public void online(String login) {
            System.out.println("ONLINE: " + login);
}

        
 public void offline(String login) {
        System.out.println("OFFLINE: " + login);
      }
   

       //methode qui permet d'alerter de la reception d'un message
public void alerteMessage(String login, String msg) {
	
        System.out.println("Vous venez de recevoir un message de " + login + " ===>" + msg);
}

//methode permettant de se connecter

public boolean connect() {
	 try {
          socket = new Socket(InetAddress.getByName(serverName),serverPort);
         return true;
         
      } catch (UnknownHostException e) {
         e.printStackTrace();
         return false;
      }catch (IOException e) {
         e.printStackTrace();
         return false;
      }
   }

//methode permettant de verifier la connexion
   
public static void verifieconnexion(Client client) {
	
    if (!client.connect()) {
    	
        System.err.println("Connect failed.");
    } else {
        System.out.println("Connect successful");
    }
}

//methode permettant d'envoyer un message

public void sendmsg(String sendTo, String msg) throws IOException {
	
    String msgsend="msg"+sendTo +   " : "+msg;
    serverOut.write(msgsend.getBytes());
 
}

public void login(String login, String password) throws IOException {  //methode permettant de se deconnecter avec serveur avec un login et son mon de passe
    String msg = "login "+ login + " " + password + "\n";
    serverOut.write(msg.getBytes());
  
    String response = bufferedIn.readLine();
    System.out.println("Reponse du serveur:" + response);

    if ("ok login".equalsIgnoreCase(response)) {
    	 System.out.println("connection acceptée");
       
    } else {
    	System.out.println("connection Refusée");
    }
}


public void logoff() throws IOException {  //methode permettant de se deconnecter avec serveur
    
	String messagelogoff="deconnection";
    serverOut.write(messagelogoff.getBytes());
    
}


private void readMessage() {  //methode qui lit dans le terminal
	try {
		
       String line;
       
        while ((line=bufferedIn.readLine()) != null) {
            String[] tokens = StringUtils.split(line);
            if (tokens != null && tokens.length > 0) {
                String cmd = tokens[0];
                if ("online".equalsIgnoreCase(cmd)) {
                    handleOnline(tokens);
                } else if ("offline".equalsIgnoreCase(cmd)) {
                    handleOffline(tokens);
                } else if ("msg".equalsIgnoreCase(cmd)) {
                    String[] tokensMsg = StringUtils.split(line, null, 3);
                    handleMessage(tokensMsg);
                }
            }
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
//methode qui permet de recuperer le message lorsque c'est un messag esimple et non un mesage de communication
private void handleMessage(String[] tokensMsg) {
    String login = tokensMsg[1];
    String msgBody = tokensMsg[2];

    for(Client client : messageRecu) {
    	client.alerteMessage(login, msgBody);
    }
}

//Cette methode nous permet de nous deconnecter en envoyany un offlog au serveur
//elle permet aussi  de modifier le staut de l'utilisateur en le passant en offline

private void handleOffline(String[] tokens) {
    String login = tokens[1];
    for(Client client : userStatut) {
    	client.offline(login);
    }
}

//methode qui va nous permettre de modifier le staut d'un utilisateur et de le mettre en online

private void handleOnline(String[] tokens) {
    String login = tokens[1];
    for(Client client : userStatut) {
    	client.online(login);
    }
}


}


	
	
