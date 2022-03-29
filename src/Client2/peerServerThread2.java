package Client2;
import Server.RequestType;
import Server.cRequest;
import Server.cResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.nio.file.Path;

public class peerServerThread2 extends Thread
{
	Socket requestClientSoc;
	String clientName;
	cRequest peerRequest;
	cResponse response;
	ObjectInputStream ois=null;
	FileInputStream fi=null;
	BufferedInputStream BS=null;
	OutputStream OS=null;
	Path currentRelativePath = Paths.get("");
	String cpath = currentRelativePath.toAbsolutePath().toString();
	public peerServerThread2(Socket requestClientSocket, String ClientName)
	{
		this.requestClientSoc=requestClientSocket;
		this.clientName=ClientName;
	}
	
	public void run()
	{
		try 
		{
			readRequestObject();
			if(peerRequest.getRequestType() == RequestType.IsAlive){
                            requestClientSoc.close();
                        }else{
                            String FileName=peerRequest.getFileName();
                            System.out.println("FileName:-"+FileName);
                            File clientFolder= new File("C:\\Users\\ducke\\Documents\\NetBeansProjects\\TestLTM\\src\\Client2\\input_files");
                            File[] clientFiles=clientFolder.listFiles();
                            for(File file:clientFiles){
				if( file.isFile()&& file.getName().equals( FileName ))
				{
					if( peerRequest.getRequestType() == RequestType.RequestFile)   // client request to download file
					{
						sendFile( file );
						requestClientSoc.close();
						break;
					}
				}
                            }
                        }	
		}
	

		catch (UnknownHostException e) 
		{
			e.printStackTrace();
			System.out.println("No host found");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			System.out.println("Something went wrong, check IP and Port");
		}
	}
	//This function return the file to the requesting client.
	private void sendFile(File file) throws IOException
	{
		byte[] by= new byte[512000];		
		fi=new FileInputStream(file);
		BS=new BufferedInputStream(fi);
		OS=requestClientSoc.getOutputStream();
		int bytesRead=0;
		while((bytesRead=BS.read(by, 0, by.length))!=-1)
		{
			OS.write(by, 0, bytesRead);
		}
		OS.close(); //have to close this port to end the outputstream, else will through 
					// stack exception
		System.out.println("File Sent");
		return;
	}
	//This function read the object related to the communication between the requesting client and processing client.

	private void readRequestObject()
	{
		try 
		{
				//System.out.println("came to reading the object");
				ois=new ObjectInputStream(requestClientSoc.getInputStream());
				peerRequest=(cRequest) ois.readObject();
				//System.out.println("read the request first");	
		} 
		catch (IOException | ClassNotFoundException e) 
		{
			
			e.printStackTrace();
		}
		
	}
}

