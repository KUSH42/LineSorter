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
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kush.linesorter.gui.MainView;
import kush.linesorter.gui.controllers.OptionsDialogListener;
import kush.linesorter.gui.controllers.ResizeHelper;

public class App extends Application {

	private static final Logger LOGGER = Logger.getLogger(App.class.getName());

	public static final String ALERTS_BUNDLE = "localization.AlertsBundle";
	public static final ResourceBundle ALERTS = ResourceBundle.getBundle(ALERTS_BUNDLE, Locale.getDefault());

	private static final String IMAGE_NOT_FOUND = ALERTS.getString("IMAGE_NOT_FOUND");

	public static void main(final String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage primaryStage) {
		final MainView root = new MainView(primaryStage);
		loadTitleIcon(primaryStage);
		primaryStage.setTitle("Line Sorter");

		final Scene scene = new Scene(root.getMainPane(), 280, 140);
		scene.addEventFilter(KeyEvent.KEY_PRESSED, new OptionsDialogListener(primaryStage));
		initializeStyles(primaryStage, scene, root);

		primaryStage.setScene(scene);
		primaryStage.show();
		ResizeHelper.addResizeListener(primaryStage);
		primaryStage.setMinHeight(
				primaryStage.getHeight() + root.getGridPane().getVgap() * root.getGridPane().getChildren().size() - 1
						+ root.getMainPane().getHeight());
		primaryStage.setMinWidth(
				primaryStage.getWidth() + root.getGridPane().getHgap() * root.getGridPane().getChildren().size() - 1
						+ root.getMainPane().getWidth());
	}

	private void initializeStyles(final Stage primaryStage, final Scene scene, final MainView root) {
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		scene.setFill(Color.TRANSPARENT);
		scene.getStylesheets().add("stylesheet.css");
		root.getTitleBar().getTitleBarPane().getStyleClass().add("main-view");
		root.getTitleBar().getTitleControlsPane().getStyleClass().add("main-view");
		root.getGridPane().getStyleClass().add("main-view");
		root.getMainPane().getStyleClass().add("main-view");
		root.getShadowPane().getStyleClass().add("main-view");
		root.getTitleBar().getTitleClose().getStyleClass().add("title-close-button");
		root.getTitleBar().getTitleMin().getStyleClass().add("title-min-button");
		root.getTitleBar().getTitleMax().getStyleClass().add("title-max-button");
		root.getTitleBar().getTitleClose().setTextFill(Paint.valueOf("#111111"));
		root.getStartBtn().getStyleClass().add("main-view-button");
		root.getMainPane().getStyleClass().add("mainpane");
		root.getShadowPane().getStyleClass().add("shadowpane");
	}

	private void loadTitleIcon(final Stage primaryStage) {
		try {
			final BufferedImage titleIcon = ImageIO
					.read(getClass().getClassLoader().getResourceAsStream("images/titleIcon.png"));
			primaryStage.getIcons().add(SwingFXUtils.toFXImage(titleIcon, null));
		} catch (final IOException e) {
			LOGGER.severe(IMAGE_NOT_FOUND + e);
			final Alert alert = new Alert(AlertType.ERROR, IMAGE_NOT_FOUND, ButtonType.OK);
			alert.initOwner(primaryStage);
			alert.show();
		}
	}
}
