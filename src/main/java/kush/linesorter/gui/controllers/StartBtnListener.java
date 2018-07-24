package kush.linesorter.gui.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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

	private static final MediaPlayer SUCCESS_SOUND_PLAYER = new MediaPlayer(new Media(
			StartBtnListener.class.getClassLoader().getResource("sounds/success.mp3").toString()));

	private final MainView parent;

	public StartBtnListener(MainView parent) {
		this.parent = parent;

		SUCCESS_SOUND_PLAYER.setVolume(.15);
	}

	public void handle(ActionEvent event) {
		long start = System.currentTimeMillis();

		if (parent.getInputFiles() == null || parent.getInputFiles().isEmpty()) {
			LOGGER.info(NO_INPUT_FILE_SELECTED);
			Alert alert = new Alert(AlertType.WARNING, PLEASE_SELECT_INPUT, ButtonType.OK);
			alert.show();
			return;
		}
		if (parent.getOutputFile() == null) {
			LOGGER.info(NO_OUTPUT_FILE_SELECTED);
			Alert alert = new Alert(AlertType.WARNING, PLEASE_SELECT_OUTPUT, ButtonType.OK);
			alert.show();
			return;
		}

		try {
			SortLogic.sort(parent.getInputFiles(), parent.getOutputFile());
		} catch (IOException e) {
			LOGGER.severe(IOEXCEPTION_SORT);
			LOGGER.severe(Arrays.toString(e.getStackTrace()));
			Alert alert = new Alert(AlertType.ERROR, IOEXCEPTION_SORT, ButtonType.OK);
			alert.show();
			return;
		}
		Alert alert = new Alert(AlertType.INFORMATION, String.format(SORT_SUCCESS_TIME,
				parent.getOutputFile().getAbsolutePath(), (System.currentTimeMillis() - start), ButtonType.OK));
		SUCCESS_SOUND_PLAYER.stop();
		SUCCESS_SOUND_PLAYER.play();
		alert.show();
		event.consume();
	}
}