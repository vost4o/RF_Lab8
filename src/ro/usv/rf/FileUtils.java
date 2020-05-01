package ro.usv.rf;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class FileUtils {
	private static final String IN_OUT_SEPARATOR = ",";

	protected static String[][] readLearningSetFromFile(String filePath) throws USVInputFileCustomException {
		ArrayList<String[]> learningSet = new ArrayList<String[]>(1);

		try {
			learningSet = (ArrayList<String[]>) Files.lines(Paths.get(filePath)).map(FileUtils::readLineAsString)
					.collect(Collectors.toList());
		} catch (Throwable thr) {
			thr.printStackTrace();
		}

		String[][] lSetArray = new String[learningSet.size()][learningSet.get(0).length];

		for (int i = 0; i < lSetArray.length; ++i) {
			lSetArray[i] = learningSet.get(i);
		}

		return lSetArray;
	}

	private static String[] readLineAsString(String line) {
		return line.split(IN_OUT_SEPARATOR);
	}

	/**
	 * Write the learning set to a file
	 * @param filePath - path to file
	 * @param learningSet - the learning set
	 * @throws IOException - Input/Output Exception
	 */
	protected static void writeLearningSetToFile(String filePath, String[][] learningSet) throws IOException {
		// Create a new file
		File file = new File(filePath);
		
		if(!file.exists()) {
			file.createNewFile();
		}
		// use a file writer
		FileWriter fWriter = new FileWriter(file);
		
		for(int x = 0; x < learningSet.length; ++x) {
			for(int y = 0; y < learningSet[x].length; ++y) {
				fWriter.write(learningSet[x][y] + (y == learningSet[x].length - 1 ? "" : IN_OUT_SEPARATOR));
			}
			fWriter.write("\n");
		}
		
		fWriter.close();
	}

}
