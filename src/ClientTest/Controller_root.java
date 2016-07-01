 package ClientTest;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import Util.Message;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class Controller_root implements Initializable {

	@FXML
	private ToggleGroup listgroup;
	@FXML
	private Button connect;
	@FXML
	private Button disconnect;
	@FXML
	private TextField txtIp;
	@FXML
	private ListView<String> txtUserList;
	@FXML
	private TextField txtChatField;
	@FXML
	private TextArea txtChatArea;
	@FXML
	private TextField txtPort;
	@FXML
	private RadioButton RadioWhisper;
	@FXML
	private Label clientName;
	
	
	private Stage primaryStage;
	private InputStreamListen c_stream_listen;
	private OutputStream os;
	private ObjectOutputStream oos;
	private boolean connect_status = false;
	private Socket socket;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		connect.setOnAction(event -> ConnectAction());
		disconnect.setOnAction(event->DisconnectAction());
		
		txtIp.setText("localhost");
		txtPort.setText("5558");

		txtUserList.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				System.out.println("changed");
				System.out.println(txtUserList.getSelectionModel().getSelectedItem());
	
			}
		});

		listgroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				// TODO Auto-generated method stub
				if (listgroup.getSelectedToggle() != null) {
					System.out.println(listgroup.getSelectedToggle().getUserData().toString());
				}
				
			}
		});

		txtChatField.setOnAction(event -> {

			// TextArea에 추가.
			String str = txtChatField.getText();

			if (connect_status) {
				try {										
					
					String toggle = listgroup.getSelectedToggle().getUserData().toString();
					
					if( toggle.equals("BroadCast") )
					{
						oos.writeObject(new Message(Message.CS_TOTAL_MSG, str));
						oos.flush();
						
						
						
					}
					else if( toggle.equals("Whisper"))
					{
						String listText = txtUserList.getSelectionModel().getSelectedItem();
						// 자기 자신에게는 Whisper하지 않는다.
						// 선택이 되어 있을 경우
						if (listText != null ) {
							
							if (!listText.equals(c_stream_listen.getClientName())) {
								// MessageType, Sender, Receiver
								System.out.println(c_stream_listen.getClientName() + " to " + listText);
								oos.writeObject(new Message(Message.CS_SECRET_MSG, str, c_stream_listen.getClientName(),
										listText));
								oos.flush();
								
							}else
								txtChatArea.appendText("자기 자신에겐 귓속말을 보낼 수 없습니다.\n");
						}else
							txtChatArea.appendText("귓속말할 대상을 선택하세요.\n");

					}					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			txtChatField.clear();
		});
		
		

	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void ConnectAction() {
		if (!connect_status) {
			// Get Ip Info
			String str_ipInfo;
			str_ipInfo = txtIp.getText();

			if (str_ipInfo.isEmpty())
				str_ipInfo = "localhost";

			// Get Port Info

			String str_portInfo = txtPort.getText();
			int portInfo = 0;
			if (!str_portInfo.isEmpty()) {
				portInfo = Integer.parseInt(str_portInfo);
			} else
				portInfo = 5555;

			// Socket 연결
			try {
				socket = new Socket();
				socket.connect(new InetSocketAddress(str_ipInfo, portInfo));
				System.out.println("연결 성공");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (socket != null) {
				try {
					c_stream_listen = new InputStreamListen(socket.getInputStream(), primaryStage.getScene().getRoot());
					c_stream_listen.start();

					os = socket.getOutputStream();
					oos = new ObjectOutputStream(os);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			connect.setDisable(true);
			disconnect.setDisable(false);
			connect_status = true;
		} 
	}
	
	public void DisconnectAction() {
	
		connect.setDisable(false);
		disconnect.setDisable(true);
		
		try {
			os.close();
			oos.close();
			socket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
