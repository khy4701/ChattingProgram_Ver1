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
					// ArrayList 돌면서 변화를 감시해야하므로 조금의 Thread 일시 정지가 있어야 작동함.
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				
				
				if( !arrayList.isEmpty())
				{										
					for(SocketThread i : arrayList )
					{
						// 전송할 데이터가 있으면 
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
													"[" + msg.getSenderName() + "] 님 으로부터 귓속말 도착 :" + msg.getMsg());
											oos = j.getObjectStream();
											oos.writeObject(new Message(Message.SC_COMMON_MSG, send_msgToReceiver));
											oos.flush();
											break;
										}

									String send_msgToSender = new String(
											"[" + msg.getReceiverName() + "] 님 에게 귓속말 보냄 : " + msg.getMsg());
									
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
