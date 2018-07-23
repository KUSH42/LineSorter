package kush.linesorter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class App extends Application {
	
	private static final Logger LOGGER = initLogger();
	
	public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
    	
    	MainView root = new MainView();
        primaryStage.setTitle("Line Sorter");
        primaryStage.setScene(new Scene(root, 230, 105));
        primaryStage.setResizable(false);

        BufferedImage titleIcon;
		try {
			titleIcon = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/titleIcon.png"));
			primaryStage.getIcons().add(SwingFXUtils.toFXImage(titleIcon, null));
		} catch (IOException e) {
			LOGGER.severe("Unable to load icon image resource!" + e);
			Alert alert = new Alert(AlertType.ERROR, "Unable to load icon image resource!", ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.show();
			return;
		}
		
        primaryStage.show();
    }
    
    private static Logger initLogger() {
    	return Logger.getLogger(MainView.class.getName());
    }
}
