package kush.linesorter.gui.controllers;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import kush.linesorter.gui.MainView;

public class FileSelectOutputBtnListener implements EventHandler<ActionEvent> {

	private final MainView parent;

	public FileSelectOutputBtnListener(final MainView parent) {
		this.parent = parent;
	}

	@Override
	public void handle(ActionEvent event) {
		File selectedFile = parent.getFileOutput().showSaveDialog(parent.getGridPane().getScene().getWindow());
		if (selectedFile != null) {
			parent.setOutputFile(selectedFile);
			parent.getOutputFilePathLabel().setText(selectedFile.getAbsolutePath());
		}
		event.consume();
	}
}