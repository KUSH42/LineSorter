package kush.linesorter.gui.controllers;

import java.io.File;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class DragOverInputListener implements EventHandler<DragEvent> {

	@Override
	public void handle(DragEvent event) {
		Dragboard db = event.getDragboard();
		List<File> files = db.getFiles();
		boolean dragDropFlag = true;
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
		if (dragDropFlag) {
			event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
		}
		event.consume();
	}

}
