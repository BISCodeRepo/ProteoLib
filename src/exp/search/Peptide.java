package exp.search;

import java.util.List;

import exp.data.Peak;
import exp.util.Parameters;
/**
 * 
 * Manage peptide information
 *
 */
public class  Peptide {
//	private String sequence;
//	private int pep_length;
	
	/**
	 * Generated theoretical peaks of given sequences
	 * @param seq peptide sequence 
	 * @param binWidth - bin width
	 * @param modificationList - modification list 
	 * @return mass array
	 */
	public int[] generateThreoPeak(String seq, double binWidth, List<Modification> modificationList) {

		int fragmentationSiteSize = seq.length() - 1;
		int threopeak[] = new int[fragmentationSiteSize];

		double mass = 0;
		int index = 0;

    if (modificationList.size() != 0) {
	// if it has n-term modification, the n-term mass should be added.
	if (modificationList.get(0).getPosition().equals("Nterm")) { // meaning n-term modification.
	    mass += modificationList.get(index).getDeltaMass();
	    index++;
	}
    }
		for (int aminoIndex = 0; aminoIndex < fragmentationSiteSize; aminoIndex++) {
			char aa = seq.charAt(aminoIndex);

			mass += Parameters.aminoacid_masss[aa - 'A'];
			if (index < modificationList.size() && (!modificationList.get(index).getPosition().equals("Nterm"))) { // if
				// it's
				// not
				// a
				// N-term
				// modification,
				// location
				// - 1
				// >= 0
				mass += modificationList.get(index).getDeltaMass();
				index++;
			}
			int ma = mass2bin(mass + Parameters.Proton, 1, binWidth);
			threopeak[aminoIndex] = ma;
		}

		return threopeak;
	}
	private int mass2bin(double mass, double charge, double binWidth) {
		return (int) Math.floor(
				((mass + (charge - 1) * Parameters.Proton) / (charge * binWidth)) + 1.0 - Parameters.binOffset); // REVIEW
		// what
		// is
		// binOffset?
		// the
		// value
		// is
		// always
		// zero.
	}
}
