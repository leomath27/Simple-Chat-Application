// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  private String loginId;
  public ChatClient(String loginId, String host, int port, ChatIF clientUI) throws IOException {
	    super(host, port); // Call the superclass constructor
	    this.loginId = loginId;
	    this.clientUI = clientUI;
	    openConnection();
	}

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
      if(message.startsWith("#")){
    		  handleCommand(message);
      }
      else
    	  sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  private void handleCommand (String command) throws IOException {
	  if(command.equals("#quit")) {
		  quit();
	  }
	  else if(command.equals("#logoff")) {
		  closeConnection();
	  }
	  else if(command.equals("#sethost")) {
		  if(isConnected()) {
  			System.out.println("Error: Client is not logged off");
  		} else {
  			setHost(command);
  		}
	  }
	  else if(command.equals("#setport")) {
		  if(isConnected()) {
  			System.out.println("Error: Client is not logged off");
  		} else {
  			setPort(Integer.parseInt(command));
  		}
	  }
	  else if(command.equals("#login")) {
		  if(isConnected()) {
  			System.out.println("Error: Client is already connected to server");
  		} else {
  			openConnection();
  		}
	  }
	  
	  else if(command.equals("#gethost")) {
		  clientUI.display("The current host is: " + getHost());
	  }
	  else if(command.equals("#getport")) {
		  clientUI.display("The current port is: " + getPort());
	  }
  }
  private void display(String string) {
	// TODO Auto-generated method stub
	
}


/**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  
  @Override
  protected void connectionEstablished() {
      try {
          // Send the login message to the server
          sendToServer("#login " + loginId);
      } catch (IOException e) {
          clientUI.display("Could not send login message to server. Terminating client.");
          quit();
      }
  }
  /**
	 * Implements the hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
  	
  	@Override 
	protected void connectionException(Exception exception) {
  		clientUI.display("The server is shut down");
  		System.exit(0);
  	}
  	
  	
  	/**
	 * Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
  	
  	@Override
	protected void connectionClosed() {
  		clientUI.display("Connection is closed");
	}
}
//End of ChatClient class

