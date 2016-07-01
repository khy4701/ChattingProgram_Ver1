package Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.ArrayList;

import Util.Message;

public class ServerMainThread {
 
	private static ServerSocket server_socket = null;
	private static AcceptListen socket_listen = null;
	private static ArrayList<SocketThread> arrayList = null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			
		try {		
			// Server Socket Bind			
			server_socket = new ServerSocket();
			server_socket.bind(new InetSocketAddress("localhost", 5558));
						
			arrayList = new ArrayList<SocketThread>();
			
			socket_listen = new AcceptListen(server_socket, arrayList);
			socket_listen.start();
			
			
			while(true)
			{				
				try {
					// ArrayList ���鼭 ��ȭ�� �����ؾ��ϹǷ� ������ Thread �Ͻ� ������ �־�� �۵���.
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				
				
				if( !arrayList.isEmpty())
				{										
					for(SocketThread i : arrayList )
					{
						// ������ �����Ͱ� ������ 
						Message msg;
						if( (msg =i.getMsgClass()) != null)
						{
							switch(msg.getMsgType())
							{
							case Message.CS_TOTAL_MSG:								
								String send_msg =  new String("["+i.getSocket_name()+"]: " + msg.getMsg());
								for (SocketThread j : arrayList) {

									ObjectOutputStream oos = j.getObjectStream();
									try
									{									
									oos.writeObject(new Message(Message.SC_COMMON_MSG, send_msg));
									oos.flush();
									}catch(Exception e)
									{										
										oos.close();
										arrayList.remove(j);
									}

								}								
								break;
								
								
							case Message.CS_SECRET_MSG:
								
								if( !msg.getReceiverName().isEmpty())
								{
									ObjectOutputStream oos = null;

									for (SocketThread j : arrayList)
										if (j.getSocket_name().equals(msg.getReceiverName())) {
											String send_msgToReceiver = new String(
													"[" + msg.getSenderName() + "] �� ���κ��� �ӼӸ� ���� :" + msg.getMsg());
											oos = j.getObjectStream();
											oos.writeObject(new Message(Message.SC_COMMON_MSG, send_msgToReceiver));
											oos.flush();
											break;
										}

									String send_msgToSender = new String(
											"[" + msg.getReceiverName() + "] �� ���� �ӼӸ� ���� : " + msg.getMsg());
									
									try{
									
									oos = i.getObjectStream();
									oos.writeObject(new Message(Message.SC_COMMON_MSG, send_msgToSender));
									oos.flush();
									}catch(Exception e)
									{
										oos.close();
										
									}

								}								
								
								break;								
							}
							
							i.setInitStatus();							
						}
					}
				
				}					
			}
						
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
