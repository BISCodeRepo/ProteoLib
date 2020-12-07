
package exp.data;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import exp.util.Parameters;

/**
 * Calculate additional features of spectra which cannot get values directly from a spectrum 
 */

public class SpectralAnalyzer{

	/**
	 * calculate XIC peaks from spectrum list 
	 * @param spectra spectra object store spectrum list 
	 * @return XICpeak XIC peak object 
	 */
	public List<XICPeak> calcXIC(Spectra spectra) {
		// Dummy
		// Step
		List<XICPeak> XICPeaks = new ArrayList<XICPeak>();
		
		// 1. Range of XIC
		int startIndex = 0;
		int endIndex = 0;
		
		// 2. Sum
		for(int index=startIndex; index<endIndex; index++) {
			// call each spectrum.
			// aggregate into the XICPeaks.
			Spectrum eachSpectrum = null;
			// TODO: if spectrum is ms1
			LinkedList<Peak> peakList = null;
			Iterator<Peak> peaks = (Iterator<Peak>)peakList.iterator();
			while(peaks.hasNext()) {
				Peak peak = peaks.next();
				XICPeak xicPeak = new XICPeak();
				xicPeak.setIntensity(peak.intensity);
				xicPeak.setMass(peak.mass);
				
				// I do not understand why this information is needed?
				// scannum???
				
				XICPeaks.add(xicPeak);
			}
		}
		
		// return the XICPeaks
		return XICPeaks;
	}

	
//	public double clacPIP(Spectra spectra) {
//		// TODO Auto-generated method stub
//		return 0;
//	}

	/**
	 * Extract GDFR value = sum of the difference between fragmentation peak and annotated peaks / TIC  
	 * @param spectrum spectrum object 
	 * @param tolerance fragment tolerance
	 * @param maxCharge maximum charge 
	 * @return GDFR value 
	 */
	public double extractGDFR(Spectrum spectrum, double tolerance, double maxCharge) {
		// Dummy
		double[] aaMasses = Parameters.aminoacid_masss;
		int sizeOfAA = 0;
		// estimate the size of AAs.
		for(int i=0; i<aaMasses.length; i++) if(aaMasses[i] != 0) sizeOfAA++;
		aaMasses = new double[sizeOfAA];
		int index = 0;
		for(int i=0; i<Parameters.aminoacid_masss.length; i++) if(Parameters.aminoacid_masss[i] != 0) aaMasses[index++] = Parameters.aminoacid_masss[i]; 
		
		// Pre: find max and min diff (abs).
		double maxAAMass = 0;
		double minAAMass = 1000;
		for(int i=0; i<aaMasses.length; i++) {
			maxAAMass = maxAAMass < aaMasses[i] ? aaMasses[i] : maxAAMass; 
			minAAMass = minAAMass > aaMasses[i] ? aaMasses[i] : minAAMass;
		}
		
		// Step
		// 1. Count number of each aa.
		List<Peak> peakList = spectrum.getPeakinfo();
		Peak[] arrayPeak = (Peak[])peakList.toArray();
		int sizeOfPeakList = arrayPeak.length;
		double TIC = spectrum.getTIC();
		
		// Supposed that peaks are sorted by ascending order of m/z.
		// We do not want a peak to count multiple times.
		Hashtable<Peak, Boolean> GDFRPeaks = new Hashtable<Peak, Boolean>();
		for(int charge = 1; charge<=maxCharge; charge++) {
			for(int i=0; i<sizeOfPeakList; i++) {
				Peak pivotPeak = arrayPeak[i];
				for(int j=i+1; j<sizeOfPeakList; j++) {
					Peak targetPeak = arrayPeak[j];
					// GDFR Condition
					double diff = Math.abs(pivotPeak.mass - targetPeak.mass);
					
					if(diff > (minAAMass - tolerance)/charge && diff < (maxAAMass + tolerance)/charge) {
						// Find fitness
						char[] pAAs = explainableAAfromTwoPeaks(pivotPeak.mass, targetPeak.mass, tolerance, charge);
						if(pAAs != null ) {
							GDFRPeaks.put(pivotPeak, true);
							GDFRPeaks.put(targetPeak, true);
						}
					}
				}
			}
		}
		
		// there is no matched peak.
		if(GDFRPeaks.size() == 0) return .0;
		
		double effectiveIntensity = 0;
		Iterator<Peak> GDFRPeakIter = (Iterator<Peak>) GDFRPeaks.keys();
		while(GDFRPeakIter.hasNext()) {
			effectiveIntensity += GDFRPeakIter.next().intensity;
		}
		
		double GDFR = effectiveIntensity / TIC;
		return GDFR;
	}

//	// TODO: this needs a PSM information. Therefore, it will implement after that PSM is completed.
//	public double extractIsotopePeakRatio(Spectrum spectrum) {
//		// TODO Auto-generated method stub
//		// TODO Need a PSM information.
//		// kinds of peptide, modification information.
//		double isotopeSpace = 1.0;
//		
//		return .0;
//	}

	
/**
 * Extract maximal number of sequence tags in the spectrum 
 * @param spectrum spectrum object 
 * @return maximal tag length
 */
	public int extractMaxSeqTagLength(Spectrum spectrum) {
		double[] aaMasses = Parameters.aminoacid_masss;
		double tolerance = 0.025; // TODO: it must be declared in other class.
		// Pre: find max and min diff (abs).
		
		double maxAAMass = 0;
		double minAAMass = 1000;
		for(int i=0; i<aaMasses.length; i++) {
			if(aaMasses[i] == 0) continue;
			maxAAMass = maxAAMass < aaMasses[i] ? aaMasses[i] : maxAAMass; 
			minAAMass = minAAMass > aaMasses[i] ? aaMasses[i] : minAAMass;
		}
		
		// Step
		// 1. Recursive way to obtain the max tag.
		// TODO: Supposed that a peak list of the spectrum was got.
		List<Peak> peakList = null; // TODO: calling peak list from spectrum is needed.
		Peak[] arrayPeak = (Peak[])peakList.toArray();
		int sizeOfPeakList = arrayPeak.length;
		
		ArrayList<Tag> totalConsecutiveTags = new ArrayList<Tag>();
		
		// Supposed that peaks are sorted by ascending order of m/z.
		for(int i=0; i<sizeOfPeakList; i++) {
			Peak pivotPeak = arrayPeak[i];
			
			for(int j=i+1; j<sizeOfPeakList; j++) {
				double massDiff = arrayPeak[j].mass - pivotPeak.mass;
				if(massDiff < minAAMass - tolerance) continue;
				if(massDiff > maxAAMass + tolerance) break;
				char[] pAAs = explainableAAfromTwoPeaks(pivotPeak.mass, arrayPeak[j].mass, tolerance, 1);
				if(pAAs != null) {
					ArrayList<Peak> consecutivePeaks = new ArrayList<Peak>();
					consecutivePeaks.add(pivotPeak);
					extractMaxSeqTagLength(arrayPeak, j, consecutivePeaks, totalConsecutiveTags, minAAMass, maxAAMass, tolerance);
				}
			}
		}
		
		int maxLength = 0;
		for(Tag tag : totalConsecutiveTags) maxLength = maxLength < tag.getTagLength() ? tag.getTagLength() : maxLength;
		
		return maxLength;
	}
	
	private void extractMaxSeqTagLength (Peak[] arrayPeak, int index, ArrayList<Peak> consecutivePeaks, ArrayList<Tag> totalConsecutiveTags, double minAAMass, double maxAAMass, double tolerance) {
		Peak pivotPeak = arrayPeak[index];
		int sizeOfPeaks = arrayPeak.length;
		boolean moreTag = false;
		int pivotIndex = consecutivePeaks.size();
		consecutivePeaks.add(pivotPeak);
		
		for(int i=index+1; i<sizeOfPeaks; i++) {
			double massDiff = arrayPeak[i].mass - pivotPeak.mass;
			if(massDiff < minAAMass - tolerance) continue;
			if(massDiff > maxAAMass + tolerance) break;
			char[] pAAs = explainableAAfromTwoPeaks(pivotPeak.mass, arrayPeak[i].mass, tolerance, 1);
			if(pAAs != null) {
				extractMaxSeqTagLength(arrayPeak, i, consecutivePeaks, totalConsecutiveTags, minAAMass, maxAAMass, tolerance);
				while(pivotIndex < consecutivePeaks.size()) {
					consecutivePeaks.remove(consecutivePeaks.size()-1);
				}
				moreTag = true;
			}
		}
		
		if(!moreTag) {
			ArrayList<Peak> tagPeaks = new ArrayList<Peak>(consecutivePeaks);
			Tag tag = new Tag(tagPeaks, tolerance);
			totalConsecutiveTags.add(tag);
		}
	}


	
//	// This function is duplicated with GDFR.
//	// DO NOT IMPLEMENT.
//	public double extractSumPeakPairdiffRatio(Spectrum_pre spectrum) {
//		// TODO Auto-generated method stub
//		return .0;
//	}

	/**
	 * Extract sum of peak intensities 
	 * @param spectrum spectrum object
	 * @return sum of peak intensities 
	 */
	public double extractSumInt(Spectrum spectrum) {
		// TODO Auto-generated method stub
		LinkedList<Peak> peaks = null;
		Iterator<Peak> iter = peaks.iterator();
		
		double sumOfInts = 0;
		while(iter.hasNext()) sumOfInts += iter.next().intensity;
		
		// return ssomeOfInts
		return sumOfInts;
	}

	/**
	 * Extract the fraction of spectrum TIC among all spectra 
	 * @param spectrum spectrum object 
	 * @param spectra spectra object 
	 * @return fraction  
	 * 
	 */
	public double extractTICFrac(Spectrum spectrum, Spectra spectra) {
		int sizeOfSpectra = spectra.getSize();
		ArrayList<Spectrum> listOfSpectrum = null; // TODO: this list must be retrieved from the given spectra.
		
		double totalOfTICs = .0;
		for(Spectrum spec : listOfSpectrum) {
			totalOfTICs += this.extractSumInt(spec);
		}
		
		
		return this.extractSumInt(spectrum) / totalOfTICs;
	}

	/**
	 * Extract Xrea (signal to noise score proposed by S. Na and E. Paek) value: http://doi.org/10.1021/pr0603248;
	 * @param spectrum spectrum object
	 * @return Xrea 
	 */
	public double extractXrea(Spectrum spectrum) {
		// TODO Auto-generated method stub
		ArrayList<Peak> peaks = (ArrayList<Peak>)spectrum.getPeakinfo();
		// sort by ascending order of intensity.
		Collections.sort(peaks, new Comparator<Peak>() {
			@Override
			public int compare(Peak p1, Peak p2) {
				if(p1.intensity > p2.intensity) {
					return 1;
				}else if(p1.intensity < p2.intensity) {
					return -1;
				}
				return 0;
			}
			
		});
		
		double[] cumulativeHistogram = new double[peaks.size()];
		cumulativeHistogram[0] = peaks.get(0).intensity;
		for(int i=1; i<cumulativeHistogram.length; i++)	cumulativeHistogram[i] += cumulativeHistogram[i-1] + peaks.get(i).intensity;
		// nomalization
		for(int i=0; i<cumulativeHistogram.length; i++) cumulativeHistogram[i] /= cumulativeHistogram[cumulativeHistogram.length-1];
		// the area of lower triangle
		double lowerTriangle = .0;
		for(int i=0; i<cumulativeHistogram.length; i++) lowerTriangle += cumulativeHistogram[i];
		double XXArea = ((cumulativeHistogram.length+1) / 2 ) -lowerTriangle;
		
		// Alpha
		// The more intense the magnitude of the most abundant peak is, the larger the are of XX
		// and thus, the spectrum will be regarded as having better quality.
		// To prevent this tendency, alpha is employed, and its value is the most intense RIbyTIC.
		// RIbyTIC is the difference between the most and the second most intense cumulative intensity.
		double alpha = cumulativeHistogram[cumulativeHistogram.length-1] - cumulativeHistogram[cumulativeHistogram.length-2];
		double XreaScore = (XXArea)/(lowerTriangle+alpha);
		
		// sort by ascending order of mz (recover to the original one).
		Collections.sort(peaks, new Comparator<Peak>() {
			@Override
			public int compare(Peak p1, Peak p2) {
				if(p1.mass > p2.mass) {
					return 1;
				}else if(p1.mass < p2.mass) {
					return -1;
				}
				return 0;
			}
		
		});
		
		return XreaScore;
	}

	/**
	 * Extract all calculated features from a spectrum 
	 * @param spectrum interested spectrum object 
	 * @param spectra spectra object contains list of spectrum 
	 * @param toleran fragment tolerance
	 * @param charge max charge 
	 * @param delimiter used to list the values 
	 * @return a string contains various feature values with delimiter
	 */
	public String extractAllFeature(Spectrum spectrum, Spectra spectra, double tolerance, double charge, String delimiter) {
		Feature feature = new Feature(this);
		feature.setAll(spectrum, spectra, tolerance, charge);
		return feature.toString(delimiter);
	}

	/**
	 * Return some of amino acids of which mass is almost equal to a difference between peak1 and peak2.
	 * Notice that the return type is an array of char.
	 * 
	 * 
	 * @param peak1
	 * @param peak2
	 * @param tolerance
	 * @param charge
	 * @return 
	 * If there is at least one AA, then return an array of char.
	 * else return null. 
	 */
	protected static char[] explainableAAfromTwoPeaks (double peak1, double peak2, double tolerance, int charge) {
		int magicNumber = 26;
		char[] pAAs = new char[magicNumber];
		double[] aaMasses = Parameters.aminoacid_masss;
		int index = 0;
		
		double diff = Math.abs(peak1 - peak2);
		for(int i=0; i<aaMasses.length; i++) {
			if(aaMasses[i] == 0) continue;
			// within a tolerance.
			if(diff > (aaMasses[i] - tolerance)/charge && diff < (aaMasses[i] + tolerance)/charge) {
				// find possible aa.
				pAAs[index++] = (char) (i+'A');
			}
		}
		
		char[] returnAAs = null;
		if(index != 0) returnAAs = new char[index];
		for(int i=0; i<index; i++) returnAAs[i] = pAAs[i];
		
		return returnAAs;
		
	}
	

}
