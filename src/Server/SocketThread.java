package Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import Util.Message;
 
public class SocketThread extends Thread{

	private String socket_name;
	private Socket socket;
	
	private OutputStream os;
	private ObjectOutputStream oos; 
	private SocketInputStreamListen s_stream_listen;
			
	public SocketThread(Socket socket, int socket_number)
	{
		this.socket = socket;
		socket_name = new String("Client"+socket_number);
	}	
	
	public void run()
	{		
		try {
			os = socket.getOutputStream();
			oos = new ObjectOutputStream(os);
			
			s_stream_listen = new SocketInputStreamListen(socket.getInputStream());
			s_stream_listen.start();			
		
			System.out.println(socket_name+": ServerThread 생성완료");
			while(true)
			{
				;
				
				// BroadCasting 서버에서 할 때 사용.
				
			}
//			
//			os.close();
//			bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Message getMsgClass()
	{
		return s_stream_listen.getMessage();
	}

	public void setInitStatus()
	{
		s_stream_listen.setInit();
	}
	
	public String getSocket_name()
	{
		return socket_name;
	}
	
	public ObjectOutputStream getObjectStream()
	{
		return oos;
	}
}
