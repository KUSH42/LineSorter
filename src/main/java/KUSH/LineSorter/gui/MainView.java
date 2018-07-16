package KUSH.LineSorter.gui;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import KUSH.LineSorter.SortLogic;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class MainView extends GridPane {

	private final static Logger LOGGER = initLogger();

	Button startBtn;
	Button fileInputSelectBtn;
	Button fileOutputSelectBtn;

	FileChooser fileInput;
	FileChooser fileOutput;

	TextField inputFilePathLabel;
	TextField outputFilePathLabel;

	List<File> inputFiles;
	File outputFile;

	public MainView() {
		super();
		initComponents();
	}

	private static Logger initLogger() {
		Logger logger = Logger.getLogger(MainView.class.getName());
		return logger;
	}

	private void initComponents() {
		setHgap(10);
		setVgap(10);
		setPadding(new Insets(10, 10, 10, 10));

		inputFilePathLabel = new TextField("Select input file(s)...");
		inputFilePathLabel.setEditable(false);
		inputFilePathLabel.setOnDragOver(new DragOverListener(IOEnum.INPUT));
		inputFilePathLabel.setOnDragDropped(new DragDroppedListener(IOEnum.INPUT));
		inputFilePathLabel.setMaxWidth(REMAINING);
		fileInput = new FileChooser();
		fileInput.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
		fileInputSelectBtn = new Button();
		fileInputSelectBtn.setText("...");
		fileInputSelectBtn.setOnAction(new FileSelectBtnListener(fileInput, IOEnum.INPUT));
		
		outputFilePathLabel = new TextField("Select output file...");
		outputFilePathLabel.setEditable(false);
		outputFilePathLabel.setOnDragOver(new DragOverListener(IOEnum.OUTPUT));
		outputFilePathLabel.setOnDragDropped(new DragDroppedListener(IOEnum.OUTPUT));
		outputFilePathLabel.setMaxWidth(REMAINING);
		fileOutput = new FileChooser();
		fileOutput.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"),
				new ExtensionFilter("All Files", "*.*"));
		fileOutputSelectBtn = new Button();
		fileOutputSelectBtn.setText("...");
		fileOutputSelectBtn.setOnAction(new FileSelectBtnListener(fileOutput, IOEnum.OUTPUT));

		startBtn = new Button();
		startBtn.setText("Sort!");
		startBtn.setOnAction(new StartBtnListener());
		startBtn.setPrefWidth(REMAINING);
		add(startBtn, 0, 1);
		
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
		add(borderpane, 0, 0);
		
		autosize();
	}

	private class DragOverListener implements EventHandler<DragEvent> {

		IOEnum MODE;

		public DragOverListener(IOEnum MODE) {
			this.MODE = MODE;
		}

		@Override
		public void handle(DragEvent event) {
			if (MODE == IOEnum.INPUT) {
				if (event.getGestureSource() != inputFilePathLabel && event.getDragboard().hasFiles()) {
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
				}
			}
			if (MODE == IOEnum.OUTPUT) {
				if (event.getGestureSource() != outputFilePathLabel && event.getDragboard().hasFiles()) {
					Dragboard db = event.getDragboard();
					List<File> files = db.getFiles();
					boolean dragDropFlag = true;
					if (files.size() > 1) {
						LOGGER.info("More than one output file was selected for the sort operation.");
						Alert alert = new Alert(AlertType.WARNING,
								"More than one output file was selected for the sort operation.", ButtonType.OK);
						alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
						alert.show();
						return;
					}
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
					if (dragDropFlag && files != null && files.size() != 0) {
						event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
						outputFile = files.get(0);
					}
				}
			}
			event.consume();
		}
	}

	private class DragDroppedListener implements EventHandler<DragEvent> {

		IOEnum MODE;

		public DragDroppedListener(IOEnum MODE) {
			this.MODE = MODE;
		}

		@Override
		public void handle(DragEvent event) {
			if (MODE == IOEnum.INPUT) {
				Dragboard db = event.getDragboard();
				List<File> selectedFiles = db.getFiles();
				if (selectedFiles != null && selectedFiles.size() != 0) {
					inputFiles = selectedFiles;
					String filepaths = "";
					int i = 0;
					for (File file : selectedFiles) {
						filepaths += file.getName();
						i++;
						if (i != selectedFiles.size()) {
							filepaths += "; ";
						}
					}
					inputFilePathLabel.setText(filepaths);
				}

			}
			if (MODE == IOEnum.OUTPUT) {
				Dragboard db = event.getDragboard();
				List<File> selectedFiles = db.getFiles();
				if (selectedFiles.size() > 1) {
					LOGGER.info("More than one output file was selected for the sort operation.");
					Alert alert = new Alert(AlertType.WARNING,
							"More than one output file was selected for the sort operation.", ButtonType.OK);
					alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
					alert.show();
					return;
				}
				if (selectedFiles != null && selectedFiles.size() != 0) {
					outputFile = selectedFiles.get(0);
					outputFilePathLabel.setText(outputFile.getName());
				}
			}
			event.consume();
		}
	}

	private class StartBtnListener implements EventHandler<ActionEvent> {

		public void handle(ActionEvent event) {
			long start = System.currentTimeMillis();

			if (inputFiles == null) {
				LOGGER.info("No input file was selected for the sort operation.");
				Alert alert = new Alert(AlertType.WARNING, "Please select an input file.", ButtonType.OK);
				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert.show();
				return;
			}
			if (outputFile == null) {
				LOGGER.info("No output file was selected for the sort operation.");
				Alert alert = new Alert(AlertType.WARNING, "Please select an output file.", ButtonType.OK);
				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert.show();
				return;
			}

			try {
				SortLogic.sort(inputFiles, outputFile);
			} catch (IOException e) {
				LOGGER.severe("IOException when trying to sort the input:\n".concat(e.getMessage()));
				Alert alert = new Alert(AlertType.ERROR, "IOException when trying to sort the input.", ButtonType.OK);
				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert.show();
				return;
			}
			Alert alert = new Alert(AlertType.INFORMATION,
					"Sort operation successfully completed.\nOutput file can be found at: "
							+ outputFile.getAbsolutePath() + "\nOperation completed in: "
							+ (System.currentTimeMillis() - start) + "ms",
					ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.show();
			return;
		}
	}

	private class FileSelectBtnListener implements EventHandler<ActionEvent> {

		private FileChooser input;
		private IOEnum MODE;

		FileSelectBtnListener(FileChooser input, IOEnum MODE) {
			this.input = input;
			this.MODE = MODE;
		}

		public void handle(ActionEvent event) {
			if (MODE == IOEnum.INPUT) {
				List<File> selectedFiles = input.showOpenMultipleDialog((Stage) getScene().getWindow());
				if (selectedFiles != null && selectedFiles.size() != 0) {
					inputFiles = selectedFiles;
					String filepaths = "";
					for (File file : selectedFiles) {
						filepaths += file.getName() + ";";
					}
					inputFilePathLabel.setText(filepaths);
				}
			} else if (MODE == IOEnum.OUTPUT) {
				File selectedFile = input.showSaveDialog((Stage) getScene().getWindow());
				if (selectedFile != null && selectedFile.length() != 0) {
					outputFile = selectedFile;
					outputFilePathLabel.setText(selectedFile.getAbsolutePath());
				}
			} else {
				throw new IllegalArgumentException("FileChooserBtn configured with invalid I/O mode.");
			}
		}
	}
}