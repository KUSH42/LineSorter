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
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;
import kush.linesorter.App;
import kush.linesorter.gui.MainView;

public class DragOverOutputListener implements EventHandler<DragEvent> {

	private static final Logger LOGGER = Logger.getLogger(MainView.class.getName());
	
	private static final String TOO_MANY_OUTPUT_FILES_SELECTED = App.ALERTS.getString("TOO_MANY_OUTPUT_FILES_SELECTED");

	private final MainView parent;

	public DragOverOutputListener(MainView parent) {
		this.parent = parent;
	}

	@Override
	public void handle(DragEvent event) {
		Dragboard db = event.getDragboard();
		List<File> files = db.getFiles();
		boolean dragDropFlag = true;
		if (files.size() > 1) {
			LOGGER.info(TOO_MANY_OUTPUT_FILES_SELECTED);
			Alert alert = new Alert(AlertType.WARNING, TOO_MANY_OUTPUT_FILES_SELECTED, ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.show();
			return;
		}
		for (File file : files) {
			if (file != null) {
				String filename = file.getName();
				int i = filename.lastIndexOf('.');
				String ext = i > 0 ? filename.substring(i + 1) : "";
				if (!"txt".equals(ext)) {
					dragDropFlag = false;
					break;
				}
			}
		}
		if (dragDropFlag && !files.isEmpty()) {
			event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
			parent.setOutputFile(files.get(0));
		}
		event.consume();
	}

}
