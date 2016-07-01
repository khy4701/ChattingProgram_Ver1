package ClientTest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMainThread extends Application{

	public void start(Stage primaryStage) {
		try {
			
			FXMLLoader loader =  new FXMLLoader(getClass().getResource("root.fxml"));	
			Parent root = loader.load();
			 
			Controller_root control = loader.getController();
			
			// ��Ʈ�ѷ�����  Stage�� �Ѱ��־� ��Ʈ�ѷ����� ���ο� Dialog ���� ����.
			control.setPrimaryStage(primaryStage);
			
			
			primaryStage.setScene(new Scene(root));
			primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}