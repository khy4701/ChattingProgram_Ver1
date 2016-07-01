package Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Util.Message;

public class AcceptListen extends Thread{
 
	ServerSocket server_socket;
	ArrayList<SocketThread> arrayList;
	
	public AcceptListen(ServerSocket server_socket, ArrayList<SocketThread> arrayList)
	{
		this.server_socket = server_socket;
		this.arrayList = arrayList;
	}
	
	
	public void run()
	{
		Socket socket = null;
		int socket_number = 0;
		
		try {
			while( ( socket = server_socket.accept()) != null)
			{
				socket_number++;
				SocketThread s_th =  new SocketThread(socket,socket_number);
				arrayList.add(s_th);
				s_th.start();				
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				ArrayList<String> arrayName = new ArrayList<String>();
				for ( SocketThread i : arrayList )
				{
					arrayName.add(i.getSocket_name());
				}
								
				for ( SocketThread i : arrayList )
				{
					ObjectOutputStream oos = i.getObjectStream();
					
					String control_msg =  new String(socket_number+"#"+ arrayList.size());
					
					try{
						oos.writeObject(new Message( Message.SC_CONTROL_MSG , control_msg, arrayName));					
						oos.flush();
					}catch(Exception e)
					{
						oos.close();
						arrayList.remove(i);
						break;
					}
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
