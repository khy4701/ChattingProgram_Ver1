package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import Util.Message;

public class SocketInputStreamListen extends Thread{
 
	private InputStream is = null;
	private ObjectInputStream os = null;
	private String stream_msg;
	private Message readMsg;
	
	public SocketInputStreamListen(InputStream is)
	{
		this.is = is;
		stream_msg = new String("");
	}

	public void run()
	{
		try {
			os = new ObjectInputStream(is);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("ServerInputStreamListen 생성완료");

		while(true)
		{			
			try {
				readMsg = (Message)os.readObject();

				stream_msg = readMsg.getMsg();
				
//				switch(readMsg.getMsgType())
//				{
//				case Message.CS_TOTAL_MSG:
//					
//					break;
//					
//				case Message.CS_SECRET_MSG:
//					
//					break;
//				}
				
				

			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();

				try {
					is.close();
					os.close();
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			
		}
	}	
	
	public Message getMessage()
	{
		return readMsg;
	}
	
	public void setInit()
	{
		readMsg = null;
	}
	

}
