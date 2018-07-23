package kush.linesorter.gui.controllers;

import java.io.File;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import kush.linesorter.gui.MainView;

public class DragDroppedInputListener implements EventHandler<DragEvent> {

	private final MainView parent;

	public DragDroppedInputListener(final MainView parent) {
		this.parent = parent;
	}

	@Override
	public void handle(DragEvent event) {
		Dragboard db = event.getDragboard();
		List<File> selectedFiles = db.getFiles();
		if (selectedFiles != null && !selectedFiles.isEmpty()) {
			parent.setInputFiles(selectedFiles);
			StringBuilder filepaths = new StringBuilder();
			int i = 0;
			for (File file : selectedFiles) {
				filepaths.append(file.getName());
				i++;
				if (i != selectedFiles.size()) {
					filepaths.append("; ");
				}
			}
			parent.getInputFilePathLabel().setText(filepaths.toString());
		}
		event.consume();
	}
}