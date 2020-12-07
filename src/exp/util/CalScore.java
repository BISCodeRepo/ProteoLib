package exp.util;

import java.text.DecimalFormat;
import java.util.ArrayList;

import exp.data.Peak;
/**
 * Calculate similarity scores between two spectra (peak lists) 
 *
 */

public class CalScore {

	/**
	 * Extract SCA value: similarity of two lists of peaks 
	 * @param expPeaks - observed peak list 
	 * @param theorPeaks - theoretical peak list
	 * @param tolerance - fragment tolerance
	 * @return
	 */
	public double extractSCA (ArrayList<Peak> expPeaks, ArrayList<Peak> theorPeaks, double tolerance) {
		
		// Each peak is deeply copied to avoid modification of an original peak.
		double oneMaxInt = 0; double anotherMaxInt = 0;
		double oneNorm2 = 0; double anotherNorm2 = 0;
		ArrayList<Peak> onePeaks = new ArrayList<Peak>();
		ArrayList<Peak> anotherPeaks = new ArrayList<Peak>();
		int expIndex = 0;
		
		// extracted a matched experiment peaks to a theoretical peak from expPeaks.
		// To consider only experimental peaks of interest. 
		for(Peak peak : theorPeaks) {
			anotherPeaks.add(deepCopyOfPeak(peak));
			anotherMaxInt = anotherMaxInt < peak.intensity ? peak.intensity : anotherMaxInt;
			anotherNorm2 += peak.intensity;
			
			for(; expIndex<expPeaks.size(); expIndex++) {
				Peak expPeak = expPeaks.get(expIndex);
				double diff = Math.abs(expPeak.mass - peak.mass);
				if(diff < tolerance) {
					int sizeOfOnePeaks = onePeaks.size();
					Peak newPeak = null;
					if(sizeOfOnePeaks != 0 && onePeaks.get(sizeOfOnePeaks-1).mass == peak.mass) newPeak = onePeaks.get(sizeOfOnePeaks-1);
					else {
						newPeak = new Peak();
						newPeak.intensity = 0;
					}
					
					newPeak.mass = peak.mass;
					newPeak.intensity += expPeak.intensity;
					
					oneMaxInt = oneMaxInt < newPeak.intensity ? newPeak.intensity : oneMaxInt;
					oneNorm2 += expPeak.intensity;
				} else if(diff >= tolerance )break;
			}
		}
		
		// calculate norm2
		oneNorm2 /= oneMaxInt; anotherNorm2 /= anotherMaxInt;
		oneNorm2 = Math.sqrt(oneNorm2); anotherNorm2 = Math.sqrt(anotherNorm2);
		
		// SQRT Normalization
		oneMaxInt = Math.sqrt(oneMaxInt); anotherMaxInt = Math.sqrt(anotherMaxInt);
		for(Peak peak : onePeaks) peak.intensity=(Math.sqrt(peak.intensity) / oneMaxInt);
		for(Peak peak : anotherPeaks) peak.intensity=(Math.sqrt(peak.intensity) / anotherMaxInt);
		
		// calculate SCA
		expIndex = 0;
		double innerProduct = 0;
		for(Peak peak : theorPeaks) {
			for(; expIndex < anotherPeaks.size(); expIndex++) {
				Peak expPeak = anotherPeaks.get(expIndex);
				innerProduct += peak.intensity * expPeak.intensity;
			}
		}
		
		
		double cos = innerProduct/(oneNorm2*anotherNorm2);
		DecimalFormat decimalFormat = new DecimalFormat("#.####");
		cos = Double.parseDouble(decimalFormat.format(cos));
		
		// adjustment
		if(cos > 1) cos = 1.0;
		else if(cos < -1) cos = -1.0;
		
		double sca = 1 - 2 * (Math.acos(cos) / Math.PI);
		return sca;
	}
	private Peak deepCopyOfPeak (Peak peak) {
		Peak cPeak = new Peak();
		cPeak.intensity = peak.intensity;
		cPeak.mass = peak.mass;
		return cPeak;
}
	
}

