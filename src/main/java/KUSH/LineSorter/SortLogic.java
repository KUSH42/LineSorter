package KUSH.LineSorter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import KUSH.LineSorter.gui.MainView;

public class SortLogic {

	private final static Logger LOGGER = initLogger();

	private static Logger initLogger() {
		Logger logger = Logger.getLogger(MainView.class.getName());
		return logger;
	}

	public static void sort(List<File> input, File output) throws IOException, AccessDeniedException {
		long start = System.currentTimeMillis();
		String line;
		ArrayList<String> strList = new ArrayList<>();
		for (File currentFile : input) {
			BufferedReader reader = new BufferedReader(new FileReader(currentFile));
			line = reader.readLine();
			while (line != null) {
				strList.add(line);
				line = reader.readLine();
			}
			reader.close();
		}
		Collections.sort(strList);
		if (!output.exists()) {
			output.createNewFile();
		}
		if (!output.canWrite()) {
			throw new AccessDeniedException(output.getAbsolutePath());
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(output));
		int i = strList.size();
		for (String str : strList) {
			writer.write(str);
			i--;
			if (!(i <= 0)) {
				writer.append('\n');
			}
		}
		writer.close();
		LOGGER.info("completed in: " + Long.toString((System.currentTimeMillis() - start)) + " ms");
	}
}