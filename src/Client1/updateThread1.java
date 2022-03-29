package Client1;
import Server.RequestType;
import Server.cRequest;
import java.io.*;
import java.net.*;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;



public class updateThread1 {
	Timer timer;
	public static ArrayList<String> filenames =new ArrayList<String>();
	public static ArrayList<String> tfilenames =new ArrayList<String>();
	public static Path currentRelativePath = Paths.get("");
	public static String cpath = currentRelativePath.toAbsolutePath().toString();
    
	public updateThread1(int seconds) {    //in constructor make list of files in input folder
		File folder = new File("C:\\Users\\ducke\\Documents\\NetBeansProjects\\TestLTM\\src\\Client1\\input_files");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
        	if (listOfFiles[i].isFile()) {
        		filenames.add((String)listOfFiles[i].getName());
            }
        }
        Set<String> hs = new HashSet<>();    //sort and remove duplicates using hashset class
        hs.addAll(filenames);
        filenames.clear();
        filenames.addAll(hs);
        timer = new Timer();
        timer.schedule(new RemindTask(),0, seconds*1000);
	}

	// this class runs in every second set by client
    class RemindTask extends TimerTask {
    	public void run() {
    		try{
    			tfilenames.clear();
                cRequest request = new cRequest();                           // create temporary file list 
                File folder = new File("C:\\Users\\ducke\\Documents\\NetBeansProjects\\TestLTM\\src\\Client1\\input_files");
                File[] listOfFiles = folder.listFiles();
                for (int i = 0; i < listOfFiles.length; i++) {
                	if (listOfFiles[i].isFile()) {
                		tfilenames.add((String)listOfFiles[i].getName());
                    }
                }
                Set<String> hs = new HashSet<>();  //remove duplicate and sort 
                hs.addAll(tfilenames);
                tfilenames.clear();
                tfilenames.addAll(hs);
                int flag=0;
                if(filenames.size()!= tfilenames.size())    //match files 
                	flag=1;
                if(flag!=1)
                	for(int i =0;i<filenames.size();i++)
                	{
                		if(!filenames.get(i).equals(tfilenames.get(i)))
                		{ 
                			flag=1;
                			break;
                		}
                	}
                if(flag==1)                 // if any changes done in folder then send update request to server
                {
                	try{
                		request.setIpAddr(InetAddress.getLocalHost());
                	}
                	catch (Exception e)
                	{
                        e.printStackTrace();
                	}
                	request.setPort(Client1.ClientPort);
                	request.setHostName(Client1.ClientName);
                	request.setRequestType(RequestType.RequestUpdate);
                	request.setfilenames(tfilenames);
                	try
                	{
                        Socket socket = new Socket(Client1.ServerIpAddress,Client1.ServerPort);
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(request);
                	}
                	catch(Exception e)
                	{
                        System.out.println(e.getMessage());
                	}
                	System.out.println("Update");
                	filenames = tfilenames;	
                }
    		}
            catch (Exception e)
            {
            	e.printStackTrace();
            }
        }
    }
}
