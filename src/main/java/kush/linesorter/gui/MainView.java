package kush.linesorter.gui;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import kush.linesorter.App;
import kush.linesorter.gui.controllers.DragDroppedInputListener;
import kush.linesorter.gui.controllers.DragDroppedOutputListener;
import kush.linesorter.gui.controllers.DragOverInputListener;
import kush.linesorter.gui.controllers.DragOverOutputListener;
import kush.linesorter.gui.controllers.FileSelectInputBtnListener;
import kush.linesorter.gui.controllers.FileSelectOutputBtnListener;
import kush.linesorter.gui.controllers.StartBtnListener;

public class MainView {

	private static final Logger LOGGER = Logger.getLogger(MainView.class.getName());

	private static final String SELECT_INPUT_FILES = App.ALERTS.getString("SELECT_INPUT_FILES");
	private static final String SELECT_OUTPUT_FILE = App.ALERTS.getString("SELECT_OUTPUT_FILE");

	private static final String ALL_FILES = App.ALERTS.getString("SELECT_OUTPUT_FILE");
	private static final String TEXT_FILES = App.ALERTS.getString("TEXT_FILES");

	private static final String SORT = App.ALERTS.getString("SORT");

	private GridPane gridPane;

	private TextArea inputFilePathLabel;
	private TextArea outputFilePathLabel;

	private List<File> inputFiles;
	private File outputFile;

	private FileChooser fileInput;
	private FileChooser fileOutput;

	public MainView() {
		initComponents();
		LOGGER.fine("MainView initialized");
	}

	private void initComponents() {
		gridPane = new GridPane();
		gridPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("#D0D0D0"), null, null)));
		gridPane.setHgap(2);
		gridPane.setVgap(2);
		gridPane.setPadding(new Insets(3, 3, 3, 3));

		inputFilePathLabel = new TextArea(SELECT_INPUT_FILES);
		inputFilePathLabel.setEditable(false);
		inputFilePathLabel.setOnDragOver(new DragOverInputListener());
		inputFilePathLabel.setOnDragDropped(new DragDroppedInputListener(this));
		inputFilePathLabel.setMinSize(TextArea.USE_COMPUTED_SIZE, TextArea.USE_COMPUTED_SIZE);
		inputFilePathLabel.setPrefSize(Integer.MAX_VALUE, Control.USE_COMPUTED_SIZE);

		fileInput = new FileChooser();
		fileInput.getExtensionFilters().add(new ExtensionFilter(TEXT_FILES, "*.txt"));
		Button fileInputSelectBtn = new Button();
		fileInputSelectBtn.setText("...");
		fileInputSelectBtn.setPrefHeight(Integer.MAX_VALUE);
		fileInputSelectBtn.setOnAction(new FileSelectInputBtnListener(this));

		outputFilePathLabel = new TextArea(SELECT_OUTPUT_FILE);
		outputFilePathLabel.setEditable(false);
		outputFilePathLabel.setOnDragOver(new DragOverOutputListener());
		outputFilePathLabel.setOnDragDropped(new DragDroppedOutputListener(this));
		outputFilePathLabel.setMinSize(TextArea.USE_COMPUTED_SIZE, TextArea.USE_COMPUTED_SIZE);
		outputFilePathLabel.setPrefSize(Integer.MAX_VALUE, Control.USE_COMPUTED_SIZE);

		fileOutput = new FileChooser();
		fileOutput.getExtensionFilters().addAll(new ExtensionFilter(TEXT_FILES, "*.txt"),
				new ExtensionFilter(ALL_FILES, "*.*"));
		Button fileOutputSelectBtn = new Button();
		fileOutputSelectBtn.setText("...");
		fileOutputSelectBtn.setPrefHeight(Integer.MAX_VALUE);
		fileOutputSelectBtn.setOnAction(new FileSelectOutputBtnListener(this));

		Button startBtn = new Button();
		startBtn.setText(SORT);
		startBtn.setOnAction(new StartBtnListener(this));
		startBtn.setPrefWidth(Integer.MAX_VALUE);
		startBtn.setMinHeight(Button.USE_PREF_SIZE);

		HBox inputBox = new HBox(2);
		inputBox.getChildren().addAll(inputFilePathLabel, fileInputSelectBtn);
		inputBox.setAlignment(Pos.BOTTOM_CENTER);
		HBox outputBox = new HBox(2);
		outputBox.getChildren().addAll(outputFilePathLabel, fileOutputSelectBtn);
		outputBox.setAlignment(Pos.BOTTOM_RIGHT);

		gridPane.addColumn(0, inputBox, outputBox, startBtn);
	}

	public List<File> getInputFiles() {
		return inputFiles;
	}

	public void setInputFiles(List<File> inputFiles) {
		this.inputFiles = inputFiles;
	}

	public TextArea getInputFilePathLabel() {
		return inputFilePathLabel;
	}

	public TextArea getOutputFilePathLabel() {
		return outputFilePathLabel;
	}

	public File getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	public FileChooser getFileInput() {
		return fileInput;
	}

	public FileChooser getFileOutput() {
		return fileOutput;
	}

	public GridPane getGridPane() {
		return gridPane;
	}
}