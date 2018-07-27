package kush.linesorter.gui.controllers;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

public class OptionsDialogListener implements EventHandler<KeyEvent> {

	final KeyCombination keyComb = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_ANY);

	public void handle(KeyEvent event) {
		if (keyComb.match(event)) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION, "Works!", ButtonType.OK);
			alert.show();
			event.consume();
		}
	}

}
