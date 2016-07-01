package ClientTest;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import Util.Message;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class InputStreamListen extends Thread{
 
	private AnchorPane root_container;
	private String stream_message = null;
	private InputStream is = null;
	private ObjectInputStream ois = null;
	private String client_name = null;
	
	ObservableList<String> list = FXCollections.observableArrayList("");

	public InputStreamListen(InputStream is ,Parent root_container)
	{
		this.is = is;
		this.root_container = (AnchorPane)root_container;
		stream_message = new String("");
	}
	
	public void run()
	{	

		try {
			ois = new ObjectInputStream(is);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ListView<String> txtUserList = (ListView<String>) root_container.lookup("#txtUserList");
		RadioButton RadioWhisper = (RadioButton) root_container.lookup("#RadioWhisper");
		TextArea txtArea = (TextArea) root_container.lookup("#txtChatArea");
		Label totalNum = (Label) root_container.lookup("#totalNum");
		Label clientName =  (Label) root_container.lookup("#clientName");

		txtUserList.setItems(list);

		while (true) {
			try {

				Message msg = (Message) ois.readObject();
				stream_message = msg.getMsg();
							
				switch (msg.getMsgType()) {
				
				case Message.SC_CONTROL_MSG:

					String[] split_msg = stream_message.split("#");
					
					if( client_name == null)
					{
						client_name = "Client" + Integer.parseInt(split_msg[0]);
						txtArea.appendText(client_name + "가 입장하였습니다.\n");
						
						// ClientName GUI 변경
						Platform.runLater(() -> {
						clientName.setText(client_name);
						});
						
					}else
						txtArea.appendText("Client" + Integer.parseInt(split_msg[0]) + "가 입장하였습니다.\n");
					
					// ListView 변경.
					ArrayList<String> userName =msg.getArrayName();
					Platform.runLater(() -> {
						
						list.clear();				  // 기존 List 삭제		
						for(String i : userName)     // Client 이름 ArrayList 추가
						{
							list.add(i);
						}
					});
					
					
					// Total 인원 변경
					Platform.runLater(() -> {
						totalNum.setText(split_msg[1]);
						
						if(Integer.parseInt(split_msg[1]) >= 2)
						{
							RadioWhisper.setDisable(false);
						}else RadioWhisper.setDisable(true);
					});
					
					break;
					
					
				case Message.SC_COMMON_MSG:
					
					txtArea.appendText(stream_message + "\n");
					break;					
				}
				
				stream_message = new String("");
				Thread.sleep(100);

			} catch (Exception e) {
				// TODO Auto-generated catch block				
					e.printStackTrace();
			}
		}

	}
	
	public String getStreamMsg()
	{		
		return stream_message;
	}
	
	public String getClientName()
	{
		return client_name;
	}

}
