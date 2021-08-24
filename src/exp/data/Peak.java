
package exp.data;

import exp.statics.IsobaricTag;

/**
 * Store one peak information of a spectrum
 */
public class Peak {
	public double mass;
	public double intensity;

	/**
	 * Get peak mass value 
	 * @return mass 
	 */
	//	public double getMass() {
	//		return mass;
	//	}

	//	protected void setMass(double mass) {
	//		this.mass = mass;
	//	}
	/**
	 * Get peak intensity value 
	 * @return intensity 
	 */
	//	public double getIntensity() {
	//		return intensity;
	//	}
	//	protected void setIntensity(double intensity) {
	//		this.intensity = intensity;
	//	}

	/**
	 * return whether this peak can be interpreted as Reporter ion or not
	 * label protocol : 0 = iTRAQ 4plex, 1 = iTRAQ 8plex, 2 = TMT 2plex, 3 = TMT 6plex, 4 = TMT 10plex 
	 * 
	 * @param label protocol index, fragment tolerance
	 * @return boolean (true = thought as reporter ion)
	 */
	public boolean checkReporterIon(int label_protocol, double frag_tol) {
		double reporterIonMasses[] = IsobaricTag.MASS_TABLE[label_protocol];
		for (int index = 1; index < reporterIonMasses.length; index++) {
			if (this.mass - frag_tol <= reporterIonMasses[index] && this.mass + frag_tol >= reporterIonMasses[index]) {
				return true;
			}
		}
		return false;
	}

}
