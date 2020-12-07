
package exp.data;

import exp.data.*;

/**
 * Store list of various feature values to one string with a specific delimiter 
 */
class Feature {
	
	private SpectralAnalyzer sa;
	
	private String delimiter;
	private double GDFR;
	private double isotopePeakRatio;
	private int maxSeqTagLength;
	private double sumPeakPairdiffRatio;
	private double sumInt;
	private double TICFrac;
	private double Xrea;
	
	Feature (SpectralAnalyzer specAnal) {
		sa = specAnal;
		delimiter = "\t";
		GDFR = 0;
		isotopePeakRatio = 0;
		maxSeqTagLength = 0;
		sumPeakPairdiffRatio = 0;
		sumInt = 0;
		TICFrac = 0;
		Xrea = 0;
	}
	
	void setAll(Spectrum spectrum, Spectra spectra, double tolerance, double charge) {
		GDFR = sa.extractGDFR(spectrum, tolerance, charge);
//		isotopePeakRatio = sa.extractIsotopePeakRatio(spectrum);
		maxSeqTagLength = sa.extractMaxSeqTagLength(spectrum);
//		sumPeakPairdiffRatio = sa.extractSumPeakPairdiffRatio(spectrum);
		sumInt = sa.extractSumInt(spectrum);
		TICFrac = sa.extractTICFrac(spectrum, spectra);
		Xrea = sa.extractXrea(spectrum);
	}
	
	/**
	 *  This method returns the string separated by delimiter
	 */
//	 String toString() {
//		StringBuilder sb = new StringBuilder();
//		sb.append(GDFR);
//		sb.append(delimiter);
////		sb.append(isotopePeakRatio);
//		sb.append(delimiter);
//		sb.append(maxSeqTagLength);
//		sb.append(delimiter);
////		sb.append(sumPeakPairdiffRatio);
//		sb.append(delimiter);
//		sb.append(sumInt);
//		sb.append(delimiter);
//		sb.append(TICFrac);
//		sb.append(delimiter);
//		sb.append(Xrea);
//		return sb.toString();
//	}
	 String toString(String delimiter) {
		StringBuilder sb = new StringBuilder();
		sb.append(GDFR);
		sb.append(delimiter);
//		sb.append(isotopePeakRatio);
		sb.append(delimiter);
		sb.append(maxSeqTagLength);
		sb.append(delimiter);
//		sb.append(sumPeakPairdiffRatio);
		sb.append(delimiter);
		sb.append(sumInt);
		sb.append(delimiter);
		sb.append(TICFrac);
		sb.append(delimiter);
		sb.append(Xrea);
		return sb.toString();
	}
}
