package fxTuote;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


/**
 * @author Miro
 * @version 20.1.2021
 *
 */
public class TuoteMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("TuoteGUIView.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("tuote.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Tuotehaku");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		launch(args);

	}
}
