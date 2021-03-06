package ro.usv.rf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class VMainClass {
	public static void main(String[] args) {
		String[][] learningSet;
		try {
			learningSet = FileUtils.readLearningSetFromFile("iris.csv");
			int numberOfPatterns = learningSet.length;
			int numberOfFeatures = learningSet[0].length - 1;

			Map<String, Integer> classesMap = new HashMap<String, Integer>();

			// create map with distinct classes and number of occurence for each class
			for (int i = 0; i < numberOfPatterns; i++) {
				String clazz = learningSet[i][learningSet[i].length - 1];
				if (classesMap.containsKey(clazz)) {
					Integer nrOfClassPatterns = classesMap.get(clazz);
					classesMap.put(clazz, nrOfClassPatterns + 1);
				} else {
					classesMap.put(clazz, 1);
				}
			}
			Random random = new Random();
			// map that keeps for each class the random patterns selected for evaluation set
			Map<String, List<Integer>> classesEvaluationPatterns = new HashMap<String, List<Integer>>();
			Integer evaluationSetSize = 0;
			for (Map.Entry<String, Integer> entry : classesMap.entrySet()) {
				String className = entry.getKey();
				Integer classMembers = entry.getValue();
				Integer evaluationPatternsNr = Math.round(classMembers * 15 / 100);
				evaluationSetSize += evaluationPatternsNr;
				List<Integer> selectedPatternsForEvaluation = new ArrayList<Integer>();
				for (int i = 0; i < evaluationPatternsNr; i++) {
					Integer patternNr = random.nextInt(classMembers) + 1;
					while (selectedPatternsForEvaluation.contains(patternNr)) {
						patternNr = random.nextInt(classMembers) + 1;
					}
					selectedPatternsForEvaluation.add(patternNr);
				}
				classesEvaluationPatterns.put(className, selectedPatternsForEvaluation);
			}

			String[][] evaluationSet = new String[evaluationSetSize][numberOfPatterns];
			String[][] trainingSet = new String[numberOfPatterns - evaluationSetSize][numberOfPatterns];
			int evaluationSetIndex = 0;
			int trainingSetIndex = 0;
			Map<String, Integer> classCurrentIndex = new HashMap<String, Integer>();
			for (int i = 0; i < numberOfPatterns; i++) {
				String className = learningSet[i][numberOfFeatures];
				if (classCurrentIndex.containsKey(className)) {
					int currentIndex = classCurrentIndex.get(className);
					classCurrentIndex.put(className, currentIndex + 1);
				} else {
					classCurrentIndex.put(className, 1);
				}
				if (classesEvaluationPatterns.get(className).contains(classCurrentIndex.get(className))) {
					evaluationSet[evaluationSetIndex] = learningSet[i];
					evaluationSetIndex++;
				} else {
					trainingSet[trainingSetIndex] = learningSet[i];
					trainingSetIndex++;
				}
			}
			
			// calculate distances
						double[][] distances = new double[evaluationSet.length][trainingSet.length];
						for (int i = 0; i < evaluationSet.length; i++) {
							for (int j = 0; j < trainingSet.length; j++) {
								distances[i][j] = DistanceUtils.euclidianDistance(evaluationSet[i], trainingSet[j], 1);
							}
						}
			
			FileUtils.writeLearningSetToFile("eval.csv", evaluationSet);
			FileUtils.writeLearningSetToFile("train.csv", trainingSet);
			
			String[][] newEvalSet = new String[evaluationSet.length][evaluationSet[0].length + 1];
			
			for(int i = 0; i < evaluationSet.length; i++) {
				for(int j = 0; j < trainingSet[i].length; j++) {
					newEvalSet[i][j] = evaluationSet[i][j];
				}
				
				newEvalSet[i][newEvalSet[0].length - 1] = KNNClassifier.performKNNClassification(evaluationSet[i], trainingSet, distances[i], 3);
			}
			
			FileUtils.writeLearningSetToFile("KNNLearningSet.txt", newEvalSet);

		} catch (USVInputFileCustomException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Finished learning set operations");
		}

	}
}
