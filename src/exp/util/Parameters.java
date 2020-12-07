package exp.util;

import java.io.IOException;
import java.util.Map;

import exp.search.Modification;
/**
 * Store parameters used in this library
 */
public class Parameters {

	private static Map<String, Modification> modMap;
	
	public static final double UNIT_MASS = 1.;

	public static final double Electron = 0.000549;
	public static final double Hydrogen = 1.007825035;
	public static final double Oxygen = 15.99491463;
	public static final double Nitrogen = 14.003074;
	public static final double Proton = Hydrogen - Electron;
	public static final double HO = Hydrogen + Oxygen;
	public static final double H2O = Hydrogen * 2 + Oxygen;
	public static final double NH3 = Hydrogen * 3 + Nitrogen;
	public static final double IsotopeSpace = 1.00235;

	public static final double NTERM_FIX_MOD = 0;
	public static final double CTERM_FIX_MOD = 0;
	public static final double B_ION_OFFSET = Proton;
	public static final double Y_ION_OFFSET = H2O + Proton;
	public static final double A_ION_OFFSET = Oxygen + 12.;
	/*
	 * Used for xcorr calculation, 0.4 is the default value with low resolution MS2
	 * with high resolution, 0.0 should be fine.
	 * https://groups.google.com/forum/#!topic/crux-users/xv7kx75zp1s
	 */
	public static final double binOffset = 0.4;
	/*
	 * In tide search, for low resolution, the default is 1.0005079. for
	 * high-resolution data, 0.02 is recommended.
	 */
	public static final double binWidth = 1.0005079;
	public static final int weightScaling = 20;

	
	private static int ROUNDING = 3;
	
	public static int getROUNDING() {
		return ROUNDING;
	}
	public static void setROUNDING(int rOUNDING) {
		ROUNDING = rOUNDING;
	}

	/**
	 * The array for residue mass of amino acid. It can be used by "array['#Some amino acid character'-'A']"
	 */
	public static final double aminoacid_masss[]={
			71.03711,	0,			103.00919, 115.02694,	129.04259, 
			147.06841,	57.02146,	137.05891, 113.08406,	0, 
			128.09496,	113.08406,	131.04049, 114.04293,	0, 
			97.05276,	128.05858,	156.10111, 87.03203,	101.04768,
			0,			99.06841,	186.07931, 0,			163.06333,	0};
	/**
	 * 
	 * If you want to get modification object, you should enter the proper modification expression.
	 * Examples:  	When the value of "ROUNDING" is 3,
	 * 				getModification("S-18.010565", 3)
	 * 				-> return Modification "Dehydrated(S3)"
	 * 				getModification("Nterm+42.010565", 0)
	 * 				-> return Modification "Acetyl/Nterm(*0)"	
	 * @param	modification expression, the position of this modified amino acid in the peptide
	 * @return	the instance of Modification.
	 * @throws IOException
	 */
	public static Modification getModification(String site, String position, String deltaMass) {
		return Modification.getMod(site, position, deltaMass);
	}
	
	
//	public static void main(String[] args) {
//		Modification mod =getModification("Nterm+42.010565", 0);
//		System.out.println(mod);
//		System.out.println(Character.isAlphabetic('C'));
//	}
}
