package kush.linesorter.gui.controllers;

import java.io.File;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import kush.linesorter.gui.MainView;

public class FileSelectInputBtnListener implements EventHandler<ActionEvent> {

	private final MainView parent;

	public FileSelectInputBtnListener(final MainView parent) {
		this.parent = parent;
	}

	@Override
	public void handle(ActionEvent event) {
		List<File> selectedFiles = parent.getFileInput().showOpenMultipleDialog((Stage) parent.getScene().getWindow());
		if (selectedFiles != null && !selectedFiles.isEmpty()) {
			parent.setInputFiles(selectedFiles);
			StringBuilder filepaths = new StringBuilder();
			int i = 0;
			for (File file : selectedFiles) {
				filepaths.append(file.getName());
				if (i < 0) {
					filepaths.append(";");
				}
				i++;
			}
			parent.getInputFilePathLabel().setText(filepaths.toString());
		}
		event.consume();
	}
}