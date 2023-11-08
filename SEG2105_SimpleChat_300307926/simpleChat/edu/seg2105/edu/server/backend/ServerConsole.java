package edu.seg2105.edu.server.backend;
import java.io.*;
import edu.seg2105.client.common.*;

public class ServerConsole implements ChatIF {
	
EchoServer server;
	
	/**
	 * Constructs an instance of the ServerConsole UI.
	 *
	 * @param port The port to listen too.
	 */
	public ServerConsole(int port) {
		server = new EchoServer(port, this);
	    try {
	    	//Added a instance of ChatIF Changed for E50 RX
	    	server.listen(); //Start listening for connections
	    } catch (Exception ex) {
	    	display("ERROR - Could not listen for clients!");
	    }
	}
	
	/**
	 * This method overrides the method in the ChatIF interface.  It
	 * displays a message onto the screen.
	 *
	 * @param message The string to be displayed.
	 */
	@Override
	public void display(String message) {
		if(message.contains("SERVER MSG>")) {
			System.out.println(message);
		} else {
			System.out.println("> " + message);
		}	  
	}
	
	/**
	 * This method waits for input from the console.
	 */
	public void accept() {
		try
	    {
	      BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
	      String message;
	      Boolean loop = true;

	      while (loop) {
	        message = fromConsole.readLine(); //Reads console input
	        
	        if(message.charAt(0)=='#' && !message.equals(null)) { //Detects if a command is entered and executes it Changed for E50 RX
	        	String[] command = message.split(" ");
	        	switch(command[0]) { //Checks which command was entered
	        	case "#quit": //Quits the server
	        		server.close();
	        		loop = false;
	        		break;
	        	case "#login":
                    // Send the #login command with the login id to the server
                    server.sendToAllClients(message);
                    display("SERVER MSG> " + message);
                    break;
	        	case "#stop": //Stops listening for new clients
	        		server.stopListening();
	        		break;
	        	case "#close": //Closes the server
	        		server.close();
	        		break;
	        	case "#setport": //sets the server port if closed
	        		if(server.isListening() && server.getNumberOfClients()>0) {
	        			System.out.println("Error: Server is not closed");
	        		} else {
	        			server.setPort(Integer.parseInt(command[1]));
	        		}
	        		break;
	        	case "#start": //Starts the server if stopped
	        		if(server.isListening()) {
	        			System.out.println("Error: Server is not stopped");
	        		} else {
	        			server.listen();
	        		}
	        		break;
	        	case "#getport": //Gets the port number
	        		display("The current port is: " + server.getPort());
	        		break;
	        	default: //If command is not recognized
	        		display("Invalid Command!");
	        	}
	        } else { //Will only send to client when input is not a command Changed for E50 RX
	        	server.sendToAllClients("SERVER MSG> " + message);
	        	display("SERVER MSG> " + message);
	        }
	      }
	      
	    } catch (Exception ex) {
	        System.out.println("Unexpected error while reading from console!");
	    }
	}

	/**
	 * This method is responsible for the creation of the Server UI.
	 *
	 * @param args[0] The host to connect to.
	 */
	public static void main(String[] args) {
		int port = 0; //Port to listen on

	    try {
	      port = Integer.valueOf(args[0]); //Get port from command line
	    } catch(Throwable t) {
	      port = EchoServer.DEFAULT_PORT; //Set port to 5555
	    }
		
	    ServerConsole chat = new ServerConsole(port);
	    chat.accept();

	}
}

