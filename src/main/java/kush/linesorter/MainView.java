package kush.linesorter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

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

	private static final Logger LOGGER = initLogger();

	Button startBtn;
	Button fileInputSelectBtn;
	Button fileOutputSelectBtn;

	FileChooser fileInput;
	FileChooser fileOutput;

	TextField inputFilePathLabel;
	TextField outputFilePathLabel;

	List<File> inputFiles;
	File outputFile;

	private static final String MORE_THAN_ONE_OUTPUT_FILE_WAS_SELECTED_FOR_THE_SORT_OPERATION = "More than one output file was selected for the sort operation.";

	public MainView() {
		super();
		initComponents();
	}

	private static Logger initLogger() {
		return Logger.getLogger(MainView.class.getName());
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

		private final IOEnum mode;

		public DragOverListener(IOEnum mode) {
			this.mode = mode;
		}

		@Override
		public void handle(DragEvent event) {
			if (mode == IOEnum.INPUT
					|| event.getGestureSource() != inputFilePathLabel && event.getDragboard().hasFiles()) {
				handleInput(event);
			}
			if (mode == IOEnum.OUTPUT
					&& (event.getGestureSource() != outputFilePathLabel && event.getDragboard().hasFiles())) {
				handleOutput(event);
			}
			event.consume();
		}

		public void handleInput(DragEvent event) {
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

		public void handleOutput(DragEvent event) {
			Dragboard db = event.getDragboard();
			List<File> files = db.getFiles();
			boolean dragDropFlag = true;
			if (files.size() > 1) {
				LOGGER.info(MainView.MORE_THAN_ONE_OUTPUT_FILE_WAS_SELECTED_FOR_THE_SORT_OPERATION);
				Alert alert = new Alert(AlertType.WARNING,
						MainView.MORE_THAN_ONE_OUTPUT_FILE_WAS_SELECTED_FOR_THE_SORT_OPERATION, ButtonType.OK);
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
			if (dragDropFlag && !files.isEmpty()) {
				event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
				outputFile = files.get(0);
			}
		}
	}

	private class DragDroppedListener implements EventHandler<DragEvent> {

		private final IOEnum mode;

		public DragDroppedListener(final IOEnum mode) {
			this.mode = mode;
		}

		@Override
		public void handle(DragEvent event) {
			if (mode == IOEnum.INPUT) {
				handleInput(event);
			}
			if (mode == IOEnum.OUTPUT) {
				handleOutput(event);
			}
			event.consume();
		}

		private void handleInput(DragEvent event) {
			Dragboard db = event.getDragboard();
			List<File> selectedFiles = db.getFiles();
			if (selectedFiles != null && !selectedFiles.isEmpty()) {
				inputFiles = selectedFiles;
				StringBuilder filepaths = new StringBuilder();
				int i = 0;
				for (File file : selectedFiles) {
					filepaths.append(file.getName());
					i++;
					if (i != selectedFiles.size()) {
						filepaths.append("; ");
					}
				}
				inputFilePathLabel.setText(filepaths.toString());
			}
		}

		private void handleOutput(DragEvent event) {
			Dragboard db = event.getDragboard();
			List<File> selectedFiles = db.getFiles();
			if (selectedFiles.size() > 1) {
				LOGGER.info(MORE_THAN_ONE_OUTPUT_FILE_WAS_SELECTED_FOR_THE_SORT_OPERATION);
				Alert alert = new Alert(AlertType.WARNING,
						MORE_THAN_ONE_OUTPUT_FILE_WAS_SELECTED_FOR_THE_SORT_OPERATION, ButtonType.OK);
				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert.show();
				return;
			}
			if (!selectedFiles.isEmpty()) {
				outputFile = selectedFiles.get(0);
				outputFilePathLabel.setText(outputFile.getName());
			}
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
		}
	}

	private class FileSelectBtnListener implements EventHandler<ActionEvent> {

		private final FileChooser input;
		private final IOEnum mode;

		FileSelectBtnListener(final FileChooser input, final IOEnum mode) {
			this.input = input;
			this.mode = mode;
		}

		public void handle(ActionEvent event) {
			if (mode == IOEnum.INPUT) {
				List<File> selectedFiles = input.showOpenMultipleDialog((Stage) getScene().getWindow());
				if (selectedFiles != null && selectedFiles.isEmpty()) {
					inputFiles = selectedFiles;
					StringBuilder filepaths = new StringBuilder();
					for (File file : selectedFiles) {
						filepaths.append(file.getName());
						filepaths.append(";");
					}
					inputFilePathLabel.setText(filepaths.toString());
				}
			} else if (mode == IOEnum.OUTPUT) {
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