package ro.usv.rf;

import java.util.Arrays;
import java.util.HashMap;

public class Classificators {

	/**
	 * This class is responsible for storing the distance to unknown class and the
	 * class name that the pattern from learning set
	 * 
	 * @author vasyl
	 *
	 */
	private static class DataElem implements Comparable<DataElem> {
		/**
		 * Distance to unknown class pattern
		 */
		private double distanceToUnknownClassPattern;
		/**
		 * Pattern class name from learning set
		 */
		private String patternClassName;

		/**
		 * Constructor
		 * 
		 * @param distanceToUnknownClassPattern - distance from unknown class pattern to known pattern from learning set
		 * @param patternClassName - the pattern class name from learning set
		 */
		public DataElem(int dataIndex, double distance, String dataClass) {
			this.distanceToUnknownClassPattern = distance;
			this.patternClassName = dataClass;
		}

		/**
		 * The compareTo method for sorting the collection of this data class elements
		 */
		@Override
		public int compareTo(DataElem o) {
			if (this == o) {
				return 0;
			}

			if (this.distanceToUnknownClassPattern > o.distanceToUnknownClassPattern) {
				return 1;
			} else if (this.distanceToUnknownClassPattern < o.distanceToUnknownClassPattern) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	/**
	 * Perform KNN classification
	 * 
	 * @param unknownPattern - unknown class pattern
	 * @param learningSet    - learning set patterns
	 * @param kFactor        - K coef for KNN
	 */
	protected static String performKNNClassification(String[] unknownPattern, String[][] learningSet, double[] distances, int kFactor) {
		int learningSetLength = learningSet.length;
		// index and distances array
		DataElem[] indexAndDistance = new DataElem[learningSetLength];
		// calculate the distances from each pattern to the unknown class pattern
		for (int i = 0; i < learningSetLength; ++i) {
			String dataClass = learningSet[i][learningSet[i].length - 1];

			DataElem currentData = new DataElem(i, distances[i], dataClass);

			indexAndDistance[i] = currentData;
		}
		// sort the array ascending
		Arrays.sort(indexAndDistance);
		// the classes map that contains the classes that are found near the unknown
		// pattern
		HashMap<String, Integer> classesMap = new HashMap<String, Integer>(1);
		for (int k = 0; k < kFactor; k++) {
			String dataClass = indexAndDistance[k].patternClassName;
			// store the occurence amount in the hash map
			if (!classesMap.containsKey(dataClass)) {
				classesMap.put(dataClass, 1);
			} else {
				int nr = classesMap.get(dataClass);
				classesMap.put(dataClass, ++nr);
			}
		}
		// find the most occurent class pattern
		int maxCount = Integer.MIN_VALUE;
		String foundClassName = "";
		for (String className : classesMap.keySet()) {
			int classCount = classesMap.get(className);
			if (classCount > maxCount) {
				maxCount = classCount;
				foundClassName = className;
			}
		}
		// return the found class name
		return foundClassName;
	}
}
