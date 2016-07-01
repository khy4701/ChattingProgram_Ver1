package Util;
 
import java.io.Serializable;
import java.util.ArrayList;

import Server.SocketThread;
/*
 * Message Type 
 * 
 * 
 * [10] : Client to Server (전체)메시지 전송    --  msg
 * [11] : Client to Server (귓속말)메시지       --  msg
 * 
 * [20] : Server -> Client 접속 알림                 --  Print#socket_name#total_userNum
 * [21] : Server -> Client 받은 메시지 전송       --  [socket_name] : ~
 * 들의 BroadCasting
 *  * 
 * [20] : Server에서 BroadCasting시
 * 
 * 
 */
public class Message implements Serializable{
	
	public static final int CS_TOTAL_MSG = 10;
	public static final int CS_SECRET_MSG = 11;
	public static final int SC_CONTROL_MSG = 20;
	public static final int SC_COMMON_MSG = 21;
	
	private int msgType;
	private String order_msg;
	
	private String sender_socketName;
	private String receiver_socketName;
	ArrayList<String> arrayName;
	
	
	public Message(int msgType ,String order_msg)
	{
		this.order_msg = order_msg;
		this.msgType = msgType;	
	}

	public Message(int msgType ,String order_msg , String sender, String receiver)
	{
		this(msgType, order_msg);
	
		sender_socketName = sender;
		receiver_socketName = receiver;	
	}

	public Message(int msgType, String order_msg, ArrayList<String> arrayName)
	{
		this(msgType, order_msg);
		this.arrayName = arrayName;
		
	}
	public String getMsg()
	{
		return order_msg;
	}
	
	public int getMsgType()
	{
		return msgType;
	}
	
	public String getSenderName()
	{
		return sender_socketName;
	}
	
	public String getReceiverName()
	{
		return receiver_socketName;
	}
	
	public ArrayList<String> getArrayName()
	{
		return arrayName;
	}
}
