

import javax.mail.internet.*;
import javax.mail.*
import javax.activation.*

import com.urbancode.air.AirPluginTool;
import com.urbancode.ud.client.UDRestClient;

def apTool = new AirPluginTool(this.args[0], this.args[1]);


// get the step properties
def props = apTool.getStepProperties();

// get the user, password, and weburl needed to create a rest client
def udUser = apTool.getAuthTokenUsername();
def udPass = apTool.getAuthToken();
def weburl = apTool.getWebUrl();



// get the properties from the step definition
def toAddress = props['mongodumpPath'];
def toAddress1 = props['backupfile_path'];


//def file1 = new File('result.txt')

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


//def Path = build.getEnvironment(listener).get('Path')
//def file1 = new File('result.txt')

Backup(toAddress, toAddress1);


def Backup(String toAddress, String toAddress1) {
DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		String date1 = dateFormat.format(date);
		 File file = new File(toAddress1+"\\MongoDbDump-"+date1);
	        if (!file.exists()) {
	            if (file.mkdir()) {
	            	System.out.println("Directory is created!");
	            } else {
	            	System.out.println("Failed to create directory!");
	            }
	        }
		
	    boolean status = false;
	  
	   
	      String command=toAddress+"\\mongodump.exe --out "+file;
	   
	    try {
	        Process runtimeProcess = Runtime.getRuntime().exec(command);
	      int processComplete = runtimeProcess.waitFor();
	        
	        if (processComplete == 0) {
	          System.out.println("backup: Backup Successfull");
	            status = true;
	       } else 
	        {
	    	  System.out.println("backup: Backup Failure!");
	        }
	    
	    } catch (IOException ioe) {
	    	System.out.println("Exception IO");
	        ioe.printStackTrace();
	    } catch (Exception e) {
	    	System.out.println("Exception");
	        e.printStackTrace();
	    }
	    return status;
	}




