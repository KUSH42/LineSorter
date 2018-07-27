package kush.linesorter.gui.controllers;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import kush.linesorter.App;
import kush.linesorter.gui.MainView;

public class DragDroppedOutputListener implements EventHandler<DragEvent> {

	private static final Logger LOGGER = Logger.getLogger(MainView.class.getName());

	private static final String TOO_MANY_OUTPUT_FILES_SELECTED = App.ALERTS.getString("TOO_MANY_OUTPUT_FILES_SELECTED");

	private final MainView parent;

	public DragDroppedOutputListener(final MainView parent) {
		this.parent = parent;
	}

	@Override
	public void handle(DragEvent event) {
		Dragboard db = event.getDragboard();
		List<File> selectedFiles = db.getFiles();
		if (selectedFiles.size() > 1) {
			LOGGER.info(TOO_MANY_OUTPUT_FILES_SELECTED);
			Alert alert = new Alert(AlertType.WARNING, TOO_MANY_OUTPUT_FILES_SELECTED, ButtonType.OK);
			alert.show();
			return;
		}
		if (!selectedFiles.isEmpty()) {
			parent.setOutputFile(selectedFiles.get(0));
			parent.getOutputFilePathLabel().setText(selectedFiles.get(0).getName());
		}
		event.consume();
	}

}
