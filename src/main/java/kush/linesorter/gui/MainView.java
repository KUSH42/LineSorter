package kush.linesorter.gui;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
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
	
	private TextField inputFilePathLabel;
	private TextField outputFilePathLabel;

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
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(10, 10, 10, 10));

		inputFilePathLabel = new TextField(SELECT_INPUT_FILES);
		inputFilePathLabel.setEditable(false);
		inputFilePathLabel.setOnDragOver(new DragOverInputListener());
		inputFilePathLabel.setOnDragDropped(new DragDroppedInputListener(this));
		inputFilePathLabel.setMaxWidth(Integer.MAX_VALUE);
		fileInput = new FileChooser();
		fileInput.getExtensionFilters().add(new ExtensionFilter(TEXT_FILES, "*.txt"));
		Button fileInputSelectBtn = new Button();
		fileInputSelectBtn.setText("...");
		fileInputSelectBtn.setOnAction(new FileSelectInputBtnListener(this));

		outputFilePathLabel = new TextField(SELECT_OUTPUT_FILE);
		outputFilePathLabel.setEditable(false);
		outputFilePathLabel.setOnDragOver(new DragOverOutputListener(this));
		outputFilePathLabel.setOnDragDropped(new DragDroppedOutputListener(this));
		outputFilePathLabel.setMaxWidth(Integer.MAX_VALUE);
		fileOutput = new FileChooser();
		fileOutput.getExtensionFilters().addAll(new ExtensionFilter(TEXT_FILES, "*.txt"),
				new ExtensionFilter(ALL_FILES, "*.*"));
		Button fileOutputSelectBtn = new Button();
		fileOutputSelectBtn.setText("...");
		fileOutputSelectBtn.setOnAction(new FileSelectOutputBtnListener(this));

		Button startBtn = new Button();
		startBtn.setText(SORT);
		startBtn.setOnAction(new StartBtnListener(this));
		startBtn.setPrefWidth(Integer.MAX_VALUE);
		gridPane.add(startBtn, 0, 1);

		VBox btnVbox = new VBox(10);
		btnVbox.getChildren().addAll(fileInputSelectBtn, fileOutputSelectBtn);

		VBox fileVbox = new VBox(10);
		VBox.setVgrow(inputFilePathLabel, Priority.ALWAYS);
		VBox.setVgrow(outputFilePathLabel, Priority.ALWAYS);
		fileVbox.getChildren().addAll(inputFilePathLabel, outputFilePathLabel);

		BorderPane borderpane = new BorderPane();
		BorderPane.setMargin(btnVbox, new Insets(0, 0, 0, 10));
		borderpane.setCenter(fileVbox);
		borderpane.setRight(btnVbox);
		gridPane.add(borderpane, 0, 0);

		gridPane.autosize();
	}

	public List<File> getInputFiles() {
		return inputFiles;
	}

	public void setInputFiles(List<File> inputFiles) {
		this.inputFiles = inputFiles;
	}

	public TextField getInputFilePathLabel() {
		return inputFilePathLabel;
	}

	public TextField getOutputFilePathLabel() {
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