package Server;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;


public class cRequest implements Serializable { //uses JAVA Serializable package

	public final InetAddress getIpAddr() { //Returns IP-Address InetAddress object which is set by void setIpAddr(InetAddress ipAddr)  function.
		return ipAddr;
	}
	public final void setIpAddr(InetAddress ipAddr) { //et InetAddress object IP-Address using the passed parameter ipAddr.
		this.ipAddr = ipAddr;
	}
	public final int getPort() {  //Returns the port number.
		return port;
	}
	public final void setPort(int port) {  //Sets the port number to the port value passed to the function.
		this.port = port;
	}
	public final String getHostName() {  //Return HostName as string value.
		return hostName;
	}
	public final void setHostName(String hostName) {  //Set HostName value with the parameter string value hostName.
		this.hostName = hostName;
	}
	public final String getFileName() {  //Return the name of the filename set by void setFileName(String fileName) function.
		return fileName;
	}
	public final void setFileName(String fileName) {  //Set fileName
		this.fileName = fileName;
	}
	public final RequestType getRequestType() { //Return RequestType enum value identifying the type of request made by peers.
		return requestType;
	}
	public final void setRequestType(RequestType requestType) { //Set the RequestType enum value identifying the type of request made by peers.
		this.requestType = requestType;
	}
	public final boolean isDownload() {  // Return boolean value related to whether Peer wants to download the requested file.
		return download;
	}
	public final void setDownload(boolean download) {  // Set boolean value for download variable.
		this.download = download;
	}
	public final ArrayList<String> getFilenames() { //Return ArrayList of all the filenames for processing
		return filenames;
	}
	public final void setfilenames(ArrayList<String> filenames) {  // Set filenames into a ArrayList.
		this.filenames=filenames;
	}
	
	private InetAddress ipAddr;
	private int port;
	private String hostName;
	private ArrayList<String> filenames;  // contains all filenames of client
	private String fileName;
	private RequestType requestType;
	private boolean download;
	
}

