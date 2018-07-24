package kush.linesorter;

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
import java.util.logging.Level;
import java.util.logging.Logger;

public class SortLogic {

	private static final Logger LOGGER = Logger.getLogger(SortLogic.class.getName());

	private SortLogic() {
	}

	public static void sort(List<File> input, File output) throws IOException {
		long start = System.currentTimeMillis();
		String line;
		ArrayList<String> strList = new ArrayList<>();
		for (File currentFile : input) {
			try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
				line = reader.readLine();
				while (line != null) {
					if (!("").equals(line)) {
						strList.add(line);
					}
					line = reader.readLine();
				}
			}
		}
		Collections.sort(strList);
		if (!output.exists() && output.createNewFile()) {
			LOGGER.info("Created file: " + output.getAbsolutePath());
		}
		if (!output.canWrite()) {
			throw new AccessDeniedException(output.getAbsolutePath());
		}
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))) {
			int i = strList.size();
			for (String str : strList) {
				writer.write(str);
				if (--i > 0) {
					writer.newLine();
				}
			}
		}
		LOGGER.log(Level.INFO, "completed in: {0} ms", System.currentTimeMillis() - start);
	}
}