package exp.search;

import exp.data.Spectrum;


/**
 * Store list of various feature values to one string with a specific delimiter 
 *
 *
 */
public class Feature {
	private PSM psm;
	private String delimiter;
//	public double calcXcorr(Spectrum spec);
//	public String getMassErr(Spectrum spec);
//	public double getAnnotatedTICRate(Spectrum spec);
//	public double getAnnotatedCountRate(Spectrum spec);
//	public int getSerialAnnotatedPeakLength(Spectrum spec);
	private double Xcorr;
	private String massErr;
	private double annotatedTICRate;
	private double annotatedCountRate;
	private int serialAnnotatedPeakLength;
	
	public Feature(PSM psm) {
		this.psm = psm;
		delimiter = "\t";
		Xcorr = 0;
		massErr = "";
		annotatedTICRate = 0;
		annotatedCountRate = 0;
		serialAnnotatedPeakLength = 0;
	}
	
	public void setAll(Spectrum spectrum) {
		Xcorr = psm.calcXcorr(spectrum);
		massErr = psm.getMassErr(spectrum);
		annotatedTICRate = psm.getAnnotatedTICRate(spectrum);
		annotatedCountRate = psm.getAnnotatedCountRate(spectrum);
		serialAnnotatedPeakLength = psm.getSerialAnnotatedPeakLength(spectrum);
	}
	
	/**
	 *  This method returns the string separated by delimiter
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(Xcorr);
		sb.append(delimiter);
		sb.append(massErr);
		sb.append(delimiter);
		sb.append(annotatedTICRate);
		sb.append(delimiter);
		sb.append(annotatedCountRate);
		sb.append(delimiter);
		sb.append(serialAnnotatedPeakLength);
		return sb.toString();
	}
}
