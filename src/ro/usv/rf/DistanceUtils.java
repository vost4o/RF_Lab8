package ro.usv.rf;

public class DistanceUtils {

	/**
	 * Calculate the euclidian distance between two patterns
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	protected static double euclidianDistance(String[] p1, String[] p2, int patternValuesCount) {
		double distance = 0;
		for (int i = 0; i < patternValuesCount; ++i) {
			double pOneValue = Double.valueOf(p1[i]);
			double pTwoValue = Double.valueOf(p2[i]);
			distance += Math.pow(pOneValue - pTwoValue, 2);
		}
		distance = Math.sqrt(distance);

		return distance;
	}
}
