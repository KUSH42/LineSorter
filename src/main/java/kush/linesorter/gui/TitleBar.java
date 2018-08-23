package kush.linesorter.gui;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TitleBar {

	private static final Logger LOGGER = Logger.getLogger(TitleBar.class.getName());

	private StackPane titleBarPane;
	private GridPane titleControlsPane;
	private Stage stage;

	private Button titleMin;
	private Button titleMax;
	private Button titleClose;

	private double posX;
	private double posY;

	public TitleBar(final Stage stage) {

		this.stage = stage;

		titleMin = new Button("🗕");
		titleMin.setScaleX(.75);
		titleMin.setScaleY(.75);
		titleMin.setOnAction(value -> {
			LOGGER.log(Level.FINE, "Window minimized: {0}", value);
			getStage().setIconified(true);
		});
		final Group titleMinGroup = new Group(titleMin);
		titleMax = new Button("🗖");
		titleMax.setScaleX(.75);
		titleMax.setScaleY(.75);
		final Group titleMaxGroup = new Group(titleMax);
		titleMax.setOnAction(value -> {
			if (!getStage().isMaximized()) {
				LOGGER.log(Level.FINE, "Window maximized: {0}", value);
				getStage().setMaximized(true);
				getTitleMax().setText("☒");
			} else {
				LOGGER.log(Level.FINE, "Window restored: {0}", value);
				getStage().setMaximized(false);
				getTitleMax().setText("☐");
			}
		});
		titleClose = new Button("✖");
		titleClose.setScaleX(.75);
		titleClose.setScaleY(.75);
		final Group titleCloseGroup = new Group(titleClose);
		titleClose.setOnAction(value -> {
			LOGGER.log(Level.FINE, "Shutdown: {0}", value);
			getStage().close();
		});

		titleControlsPane = new GridPane();
		titleControlsPane.addRow(0, titleMinGroup, titleMaxGroup, titleCloseGroup);
		titleControlsPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FF00FF"), null, null)));
		titleControlsPane.setAlignment(Pos.CENTER_RIGHT);

		final HBox appTitleBox = new HBox();

		GridPane.setConstraints(appTitleBox, 2, 1, 2, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER,
				new Insets(2, 8, 2, 8));
		GridPane.setConstraints(titleControlsPane, 4, 1, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.NEVER,
				new Insets(0, 0, 0, 2));

		final Image titleImage = new Image(getClass().getClassLoader().getResourceAsStream("images/titleIcon.png"), 16,
				16, true, true);
		final Label titleLabel = new Label("LineSorter");
		titleLabel.setFont(Font.font(14));
		titleLabel.setGraphic(new ImageView(titleImage));
		titleLabel.setPadding(new Insets(0, 16, 0, 0));
		appTitleBox.getChildren().add(titleLabel);
		appTitleBox.setAlignment(Pos.CENTER);

		titleBarPane = new StackPane();
		titleBarPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("#DDDDDD"), null, null)));
		titleBarPane.getChildren().addAll(appTitleBox, titleControlsPane);

		titleBarPane.setOnMousePressed(event -> {
			posX = event.getSceneX();
			posY = event.getSceneY();
		});
		titleBarPane.setOnMouseDragged((event -> {
			getStage().setX(event.getScreenX() - posX);
			getStage().setY(event.getScreenY() - posY);
		}));
	}

	public Stage getStage() {
		return stage;
	}

	public StackPane getTitleBarPane() {
		return titleBarPane;
	}

	public Button getTitleClose() {
		return titleClose;
	}

	public Node getTitleControlsPane() {
		return this.titleControlsPane;
	}

	public Button getTitleMax() {
		return titleMax;
	}

	public Button getTitleMin() {
		return titleMin;
	}

	public void setStage(final Stage stage) {
		this.stage = stage;
	}

	public void setTitleBarPane(final StackPane titleBarPane) {
		this.titleBarPane = titleBarPane;
	}

	public void setTitleClose(final Button titleClose) {
		this.titleClose = titleClose;
	}

	public void setTitleControlsPane(final GridPane titleControlsPane) {
		this.titleControlsPane = titleControlsPane;
	}

	public void setTitleMax(final Button titleMax) {
		this.titleMax = titleMax;
	}

	public void setTitleMin(final Button titleMin) {
		this.titleMin = titleMin;
	}
}