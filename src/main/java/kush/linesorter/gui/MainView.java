package kush.linesorter.gui;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
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

	private final Stage stage;

	private GridPane mainPane;
	private GridPane gridPane;

	private TextArea inputFilePathLabel;
	private TextArea outputFilePathLabel;

	private TitleBar titleBar;
	private Button startBtn;

	private List<File> inputFiles;

	private File outputFile;
	private FileChooser fileInput;

	private FileChooser fileOutput;

	public MainView(final Stage stage) {
		this.stage = stage;
		initComponents();
		LOGGER.fine("MainView initialized");
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

	public TextArea getInputFilePathLabel() {
		return inputFilePathLabel;
	}

	public List<File> getInputFiles() {
		return inputFiles;
	}

	public GridPane getMainPane() {
		return mainPane;
	}

	public File getOutputFile() {
		return outputFile;
	}

	public TextArea getOutputFilePathLabel() {
		return outputFilePathLabel;
	}

	public Button getStartBtn() {
		return startBtn;
	}

	public TitleBar getTitleBar() {
		return titleBar;
	}

	private void initComponents() {

		// title-bar
		setTitleBar(new TitleBar(stage));

		// input
		inputFilePathLabel = new TextArea(SELECT_INPUT_FILES);
		inputFilePathLabel.setEditable(false);
		inputFilePathLabel.setOnDragOver(new DragOverInputListener());
		inputFilePathLabel.setOnDragDropped(new DragDroppedInputListener(this));
		inputFilePathLabel.setPrefSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
		inputFilePathLabel.setStyle("-fx-border-radius: 10 0 0 0;");
		inputFilePathLabel.setStyle("-fx-background-radius: 10 0 0 0;");

		fileInput = new FileChooser();
		fileInput.getExtensionFilters().add(new ExtensionFilter(TEXT_FILES, "*.txt"));
		final Button fileInputSelectBtn = new Button();
		fileInputSelectBtn.setText("...");
		fileInputSelectBtn.setPrefHeight(Integer.MAX_VALUE);
		fileInputSelectBtn.setOnAction(new FileSelectInputBtnListener(this));

		final HBox inputBox = new HBox(2);
		inputBox.getChildren().addAll(inputFilePathLabel, fileInputSelectBtn);

		// output
		outputFilePathLabel = new TextArea(SELECT_OUTPUT_FILE);
		outputFilePathLabel.setEditable(false);
		outputFilePathLabel.setOnDragOver(new DragOverOutputListener());
		outputFilePathLabel.setOnDragDropped(new DragDroppedOutputListener(this));
		outputFilePathLabel.setPrefSize(Integer.MAX_VALUE, Integer.MAX_VALUE);

		fileOutput = new FileChooser();
		fileOutput.getExtensionFilters().addAll(new ExtensionFilter(TEXT_FILES, "*.txt"),
				new ExtensionFilter(ALL_FILES, "*.*"));
		final Button fileOutputSelectBtn = new Button();
		fileOutputSelectBtn.setText("...");
		fileOutputSelectBtn.setPrefHeight(Integer.MAX_VALUE);
		fileOutputSelectBtn.setOnAction(new FileSelectOutputBtnListener(this));

		final HBox outputBox = new HBox(2);
		outputBox.getChildren().addAll(outputFilePathLabel, fileOutputSelectBtn);

		// sort button
		startBtn = new Button();
		startBtn.setText(SORT);
		startBtn.setOnAction(new StartBtnListener(this));
		startBtn.setPrefWidth(Integer.MAX_VALUE);

		// container panels
		gridPane = new GridPane();
		gridPane.setHgap(2);
		gridPane.setVgap(2);
		gridPane.setPadding(new Insets(3, 3, 3, 3));
		gridPane.addColumn(0, inputBox, outputBox, startBtn);

		mainPane = new GridPane();
		mainPane.addColumn(0, titleBar.getTitleBarPane(), gridPane);
		mainPane.setStyle("-fx-effect: innershadow(gaussian, #929292, 1, 3, 0, 0);");
	}

	public void setInputFiles(final List<File> inputFiles) {
		this.inputFiles = inputFiles;
	}

	public void setOutputFile(final File outputFile) {
		this.outputFile = outputFile;
	}

	public void setTitleBar(final TitleBar titleBar) {
		this.titleBar = titleBar;
	}
}