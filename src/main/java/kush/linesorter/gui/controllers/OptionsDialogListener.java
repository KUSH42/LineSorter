package kush.linesorter.gui.controllers;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class OptionsDialogListener implements EventHandler<KeyEvent> {

	private static final KeyCombination keyComb = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_ANY);
	private final Stage stage;

	public OptionsDialogListener(final Stage stage) {
		this.stage = stage;
	}

	@Override
	public void handle(final KeyEvent event) {
		if (keyComb.match(event)) {
			final Alert alert = new Alert(Alert.AlertType.INFORMATION, "Works!", ButtonType.OK);
			alert.initOwner(stage);
			alert.show();
			event.consume();
		}
	}

}
