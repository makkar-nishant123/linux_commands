package POC.POC_chart;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;


/**
 * Hello world!
 *
 */
public class Helper  {
	public static com.sshtools.j2ssh.SshClient ssh;
	static Session session;
	static Channel channel;
	public static void main(String[] args) {
		try {
			connect_server("24.33.45.198", 22, "orafmw", "()r1fmw");	
			run_command("mkdir /home/orafmw/test");
			read_from_console();
			sftp_file("d:\\testftp.txt", "/home/orafmw/test");
			run_command("ls -aF ~/test");
			read_from_console();
			disconnect_channel();
			disconnect_session();


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static boolean disconnect_session() {
		// TODO Auto-generated method stub
		session.disconnect();
		System.out.println(session.isConnected());
		return session.isConnected();
	}

	private static boolean disconnect_channel() {
		// TODO Auto-generated method stub
		channel.disconnect();
		System.out.println(channel.isConnected());
		return channel.isConnected();
	}

	private static void read_from_console() throws IOException, JSchException {
		// TODO Auto-generated method stub
	    InputStream in=channel.getInputStream();
        channel.connect();
        byte[] tmp=new byte[1024];
        while(true){
          while(in.available()>0){
            int i=in.read(tmp, 0, 1024);
            if(i<0)break;
            System.out.print(new String(tmp, 0, i));
          }
          if(channel.isClosed()){
            System.out.println("exit-status: "+channel.getExitStatus());
            break;
          }
          try{Thread.sleep(1000);}catch(Exception ee){}
        }
	}

	private static void run_command(String command ) throws JSchException, IOException {
		
		channel=session.openChannel("exec");
        ((ChannelExec)channel).setCommand(command);
        channel.setInputStream(null);
        ((ChannelExec)channel).setErrStream(System.err);
        
    
       
	}

	private static void sftp_file(String source , String destination) throws SftpException, FileNotFoundException, JSchException {
		// TODO Auto-generated method stub
		ChannelSftp channel = null;
		channel = (ChannelSftp)session.openChannel("sftp");
		channel.connect();
		    File localFile = new File(source);
		    channel.cd(destination);
		   // channel.cd(destination);
		    
		channel.put(new FileInputStream(localFile),localFile.getName());
	}

	private static boolean connect_server(String host, int port, String user, String password) throws IOException {
		java.util.Properties config = new java.util.Properties(); 
    	config.put("StrictHostKeyChecking", "no");
    	JSch jsch = new JSch();
    	
		try {
			session = jsch.getSession(user, host, 22);
		} catch (JSchException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
    	session.setPassword(password);
    	session.setConfig(config);
    	try {
			session.connect();
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    	System.out.println("Connected");
return true;
	}

	
}
