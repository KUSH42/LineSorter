package kush.linesorter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
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
import kush.linesorter.gui.MainView;

public class App extends Application {
	
	private static final Logger LOGGER = Logger.getLogger(App.class.getName());
	
	public static final String ALERTS_BUNDLE = "localization.AlertsBundle";
	public static final ResourceBundle ALERTS = ResourceBundle.getBundle(ALERTS_BUNDLE, Locale.getDefault());
	
	private static final String IMAGE_NOT_FOUND = ALERTS.getString("IMAGE_NOT_FOUND");
	
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
			LOGGER.severe(IMAGE_NOT_FOUND + e);
			Alert alert = new Alert(AlertType.ERROR, IMAGE_NOT_FOUND, ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.show();
			return;
		}
		
        primaryStage.show();
    }
}
