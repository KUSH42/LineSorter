package kush.linesorter.gui.controllers;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ResizeHelper {

	public static void addResizeListener(final Stage stage) {
		final ResizeListener resizeListener = new ResizeListener(stage);
		stage.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeListener);
		stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED, resizeListener);
		stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, resizeListener);
		stage.getScene().addEventHandler(MouseEvent.MOUSE_MOVED, resizeListener);
		stage.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED, resizeListener);
		final ObservableList<Node> children = stage.getScene().getRoot().getChildrenUnmodifiable();
		for (final Node child : children) {
			addListenersRecursively(child, resizeListener);
		}
	}

	private static void addListenersRecursively(final Node node, final EventHandler<MouseEvent> listener) {
		node.addEventHandler(MouseEvent.MOUSE_DRAGGED, listener);
		node.addEventHandler(MouseEvent.MOUSE_EXITED, listener);
		node.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, listener);
		node.addEventHandler(MouseEvent.MOUSE_MOVED, listener);
		node.addEventHandler(MouseEvent.MOUSE_PRESSED, listener);
		if (node instanceof Parent) {
			final Parent parent = (Parent) node;
			final ObservableList<Node> children = parent.getChildrenUnmodifiable();
			for (final Node child : children) {
				addListenersRecursively(child, listener);
			}
		}
	}

	private ResizeHelper() {
	}
}
