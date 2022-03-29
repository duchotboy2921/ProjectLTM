package Server;
import java.io.Serializable;
import java.util.ArrayList;


public class cResponse implements Serializable{  //uses JAVA Serializable package

	public final String getMessage() {  //This function return a message related to response.
		return message;
	}
	public final void setMessage(String message) { //This function set the message to some string value passed as a parameter.
		this.message = message;
	}
	public final boolean isSuccess() { //this function return true if a communication between Client and server is a successful one else it return false.
		return success;
	}
	public final void setSuccess(boolean success) { //This function set the success boolean variable equals to the boolean value passed to the function as the parameter confirming successful communication or not.
		this.success = success;
	}
	public final ArrayList<cRequest> getPeerList() {  //This function returns a string array list with all the peer information in it.
		return peerList;
	}
	public final void setPeerList(ArrayList<cRequest> peerList) {  //This function set a string array list with peer information.
		this.peerList = peerList;
	}
	
	public final boolean isfileExists() {  //This function checks whether a particular file exist or not.
		return fileExists;
	}
	public final void setfileExits(boolean fileExists) { //This function set boolean fileExists variable to true if a particular file exists else it set the variable to false.
		this.fileExists = fileExists;
	}
	private String message;
	private boolean success;
	private ArrayList<cRequest> peerList;  // list of clients info
	private boolean fileExists;
}
