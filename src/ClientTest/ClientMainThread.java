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
			
			// 컨트롤러에서  Stage을 넘겨주어 컨트롤러에서 새로운 Dialog 생성 가능.
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
