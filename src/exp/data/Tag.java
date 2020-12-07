

package exp.data;

import java.util.ArrayList;
/**
 * Calculate sequence tags among the list of given peaks 
 *
 */
public class Tag {

	private ArrayList<Peak> peakList = null;
	private double tolerance = 0;
	/**
	 * constructor 
	 * @param pealist 
	 * @param tolerance - fragment tolerance
	 */
	public Tag (ArrayList<Peak> peakList, double tolerance) {this.peakList = peakList; this.tolerance = tolerance;}
	/**
	 * Get all possible sequences among the list of peaks 
	 * @return list of sequences 
	 */
	public ArrayList<String> getPossibleSequences () {
		ArrayList<String> possibleSequences = new ArrayList<String>();
		getPossibleSequences(possibleSequences, 0, new StringBuilder());		
		return possibleSequences;
	}
	
	private void getPossibleSequences(ArrayList<String> sequences, int index, StringBuilder sb) {
		int nextIndex = index+1;
		if(nextIndex >= this.getSizeOfPeaks()) {
			sequences.add(sb.toString());
			return;
		}
		
		char[] pAAs = SpectralAnalyzer.explainableAAfromTwoPeaks(peakList.get(index).mass, peakList.get(nextIndex).mass, tolerance, 1);
		
		for(char aa : pAAs) {
			sb.setLength(nextIndex);
			sb.append(aa);
			getPossibleSequences(sequences, nextIndex, sb);
		}
	}
	/**
	 * Get the number of peaks 
	 * @return peak count 
	 */
	public int getSizeOfPeaks () {
		return this.peakList.size();
	}
	/**
	 * Get the number of tags contained in the list of peaks 
	 * @return tag count 
	 */
	public int getTagLength () {
		return this.peakList.size() - 1;
	}
}
