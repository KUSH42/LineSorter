package kush.linesorter.gui.controllers;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ResizeListener implements EventHandler<MouseEvent> {
	private static final int BORDER = 12;
	private final Stage stage;
	private Cursor cursorEvent = Cursor.DEFAULT;
	private double startX = 0;
	private double startY = 0;
	private double startScreenX = 0;
	private double startScreenY = 0;

	public ResizeListener(final Stage stage) {
		this.stage = stage;
	}

	@Override
	public void handle(final MouseEvent mouseEvent) {
		if (stage.isFullScreen() || stage.isMaximized()) {
			return;
		}

		final EventType<? extends MouseEvent> mouseEventType = mouseEvent.getEventType();
		final Scene scene = stage.getScene();

		final double mouseEventX = mouseEvent.getSceneX();
		final double mouseEventY = mouseEvent.getSceneY();

		if (MouseEvent.MOUSE_MOVED.equals(mouseEventType)) {
			mouseMoved(mouseEvent);
		} else if (MouseEvent.MOUSE_EXITED.equals(mouseEventType)
				|| MouseEvent.MOUSE_EXITED_TARGET.equals(mouseEventType)) {
			scene.setCursor(Cursor.DEFAULT);
		} else if (MouseEvent.MOUSE_PRESSED.equals(mouseEventType)) {
			startX = stage.getWidth() - mouseEventX;
			startY = stage.getHeight() - mouseEventY;
		} else if (MouseEvent.MOUSE_DRAGGED.equals(mouseEventType) && !Cursor.DEFAULT.equals(cursorEvent)) {
			resizeDrag(mouseEvent);
		}

		if (MouseEvent.MOUSE_PRESSED.equals(mouseEventType)) {
			startScreenX = mouseEvent.getScreenX();
			startScreenY = mouseEvent.getScreenY();
		} else if (MouseEvent.MOUSE_DRAGGED.equals(mouseEventType) && Cursor.DEFAULT.equals(cursorEvent)) {
			moveStage(mouseEvent);
		}
	}

	/**
	 * For resizing the window to the left or right
	 *
	 * @param mouseEvent The {@link MouseEvent} that triggered the resize
	 */
	private void leftRightResize(final MouseEvent mouseEvent) {
		final double minWidth = stage.getMinWidth() > (BORDER * 2) ? stage.getMinWidth() : (BORDER * 2);
		final double maxWidth = stage.getMaxWidth();
		if (Cursor.W_RESIZE.equals(cursorEvent) || Cursor.SW_RESIZE.equals(cursorEvent)) {
			double newWidth = stage.getWidth() - (mouseEvent.getScreenX() - stage.getX());
			if (newWidth >= minWidth && newWidth <= maxWidth) {
				stage.setWidth(newWidth);
				stage.setX(mouseEvent.getScreenX());
			} else {
				newWidth = Math.min(Math.max(newWidth, minWidth), maxWidth);
				// x1 + w1 = x2 + w2
				// x1 = x2 + w2 - w1
				stage.setX(stage.getX() + stage.getWidth() - newWidth);
				stage.setWidth(newWidth);
			}
		} else {
			stage.setWidth(Math.min(Math.max(mouseEvent.getSceneX() + startX, minWidth), maxWidth));
		}
	}

	private void mouseMoved(final MouseEvent mouseEvent) {
		final Scene scene = stage.getScene();
		final double sceneWidth = scene.getWidth();
		final double sceneHeight = scene.getHeight();
		final double mouseEventX = mouseEvent.getSceneX();
		final double mouseEventY = mouseEvent.getSceneY();

		if (mouseEventX < BORDER && mouseEventY < BORDER) {
			cursorEvent = Cursor.NW_RESIZE;
		} else if (mouseEventX < BORDER && mouseEventY > sceneHeight - BORDER) {
			cursorEvent = Cursor.SW_RESIZE;
		} else if (mouseEventX > sceneWidth - BORDER && mouseEventY < BORDER) {
			cursorEvent = Cursor.NE_RESIZE;
		} else if (mouseEventX > sceneWidth - BORDER && mouseEventY > sceneHeight - BORDER) {
			cursorEvent = Cursor.SE_RESIZE;
		} else if (mouseEventX < BORDER) {
			cursorEvent = Cursor.W_RESIZE;
		} else if (mouseEventX > sceneWidth - BORDER) {
			cursorEvent = Cursor.E_RESIZE;
		} else if (mouseEventY < BORDER) {
			cursorEvent = Cursor.N_RESIZE;
		} else if (mouseEventY > sceneHeight - BORDER) {
			cursorEvent = Cursor.S_RESIZE;
		} else {
			cursorEvent = Cursor.DEFAULT;
		}
		scene.setCursor(cursorEvent);
	}

	private void moveStage(final MouseEvent mouseEvent) {
		stage.setX(stage.getX() + mouseEvent.getScreenX() - startScreenX);
		startScreenX = mouseEvent.getScreenX();
		stage.setY(stage.getY() + mouseEvent.getScreenY() - startScreenY);
		startScreenY = mouseEvent.getScreenY();
	}

	private void resizeDrag(final MouseEvent mouseEvent) {
		if (!Cursor.W_RESIZE.equals(cursorEvent) && !Cursor.E_RESIZE.equals(cursorEvent)) {
			final double minHeight = stage.getMinHeight() > (BORDER * 2) ? stage.getMinHeight() : (BORDER * 2);
			final double maxHeight = stage.getMaxHeight();
			if (Cursor.NW_RESIZE.equals(cursorEvent) || Cursor.N_RESIZE.equals(cursorEvent)
					|| Cursor.NE_RESIZE.equals(cursorEvent)) {
				upResize(mouseEvent);
			} else {
				// downwards resize
				stage.setHeight(Math.min(Math.max(mouseEvent.getSceneY() + startY, minHeight), maxHeight));
			}
		}
		if (!Cursor.N_RESIZE.equals(cursorEvent) && !Cursor.S_RESIZE.equals(cursorEvent)) {
			leftRightResize(mouseEvent);
		}
	}

	/**
	 * For resizing the window upwards
	 *
	 * @param mouseEvent The {@link MouseEvent} that triggered the resize
	 */
	private void upResize(final MouseEvent mouseEvent) {
		double newHeight = stage.getHeight() - (mouseEvent.getScreenY() - stage.getY());
		if (newHeight >= stage.getMinHeight() && newHeight <= stage.getMaxHeight()) {
			stage.setHeight(newHeight);
			stage.setY(mouseEvent.getScreenY());
		} else {
			newHeight = Math.min(Math.max(newHeight, stage.getMinHeight()), stage.getMaxHeight());
			// y1 + h1 = y2 + h2
			// y1 = y2 + h2 - h1
			stage.setY(stage.getY() + stage.getHeight() - newHeight);
			stage.setHeight(newHeight);
		}
	}
}