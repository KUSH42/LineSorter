package kush.linesorter.gui.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import kush.linesorter.App;
import kush.linesorter.SortLogic;
import kush.linesorter.gui.MainView;

public class StartBtnListener implements EventHandler<ActionEvent> {

	private static final Logger LOGGER = Logger.getLogger(MainView.class.getName());

	private static final String NO_INPUT_FILE_SELECTED = App.ALERTS.getString("NO_INPUT_FILE_SELECTED");
	private static final String NO_OUTPUT_FILE_SELECTED = App.ALERTS.getString("NO_OUTPUT_FILE_SELECTED");

	private static final String PLEASE_SELECT_INPUT = App.ALERTS.getString("PLEASE_SELECT_INPUT");
	private static final String PLEASE_SELECT_OUTPUT = App.ALERTS.getString("PLEASE_SELECT_OUTPUT");

	private static final String IOEXCEPTION_SORT = App.ALERTS.getString("IOEXCEPTION_SORT");
	private static final String SORT_SUCCESS_TIME = App.ALERTS.getString("SORT_SUCCESS_TIME");
	
	//private static final MediaPlayer SUCCESS_SOUND = new MediaPlayer(new Media());

	private final MainView parent;

	public StartBtnListener(MainView parent) {
		this.parent = parent;

		Alert alert = new Alert(AlertType.ERROR, StartBtnListener.class.getClassLoader().getResource("sounds/success.ogg").toString(), ButtonType.OK);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		//SUCCESS_SOUND.setVolume(.15);
	}

	public void handle(ActionEvent event) {
		long start = System.currentTimeMillis();

		if (parent.getInputFiles() == null || parent.getInputFiles().isEmpty()) {
			LOGGER.info(NO_INPUT_FILE_SELECTED);
			Alert alert = new Alert(AlertType.WARNING, PLEASE_SELECT_INPUT, ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.show();
			return;
		}
		if (parent.getOutputFile() == null) {
			LOGGER.info(NO_OUTPUT_FILE_SELECTED);
			Alert alert = new Alert(AlertType.WARNING, PLEASE_SELECT_OUTPUT, ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.show();
			return;
		}

		try {
			SortLogic.sort(parent.getInputFiles(), parent.getOutputFile());
		} catch (IOException e) {
			LOGGER.severe(IOEXCEPTION_SORT);
			LOGGER.severe(Arrays.toString(e.getStackTrace()));
			Alert alert = new Alert(AlertType.ERROR, IOEXCEPTION_SORT, ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.show();
			return;
		}
		Alert alert = new Alert(AlertType.INFORMATION, String.format(SORT_SUCCESS_TIME,
				parent.getOutputFile().getAbsolutePath(), (System.currentTimeMillis() - start), ButtonType.OK));
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.show();
		//SUCCESS_SOUND.play();
		event.consume();
	}
}