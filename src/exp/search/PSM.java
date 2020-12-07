
package exp.search;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exp.data.Peak;
import exp.data.Spectrum;
import exp.util.Parameters;

/**
 * Extract PSM features 
 * @author kyoon3
 *
 */
public class PSM implements Comparable <PSM> {
	private String scanID;
	private String index;
	private String charge;
	private String title;
	private String seq;

	private String stripPepSeq;
	private List<Modification> mods;
	private Score score;
	private Double observedMass;
	private Double calculatedMass;
	private Double deltaM;
	private Double precursorMZ;
	private boolean isTarget;
	private String proteinID;
	private String NTT;
	private String posInProt;
	private List<AnnoPeak> AnnotatedPeakList;
	private List<PeakWithInfo> TheoPeakList;

	/**
	 * private class peakwithInfo with ions, charge
	 * 
	 * @author kyoon3
	 * 
	 */
	private class PeakWithInfo extends Peak {
		public String getIon_type() {
			return ion_type;
		}

		public void setIon_type(String ion_type) {
			this.ion_type = ion_type;
		}

		public double getCharge() {
			return charge;
		}

		public void setCharge(double charge) {
			this.charge = charge;
		}

		String ion_type;
		double charge;

	}

	/**
	 * Get scan ID 
	 * @param null
	 * @return scanID of this psm in string format.
	 */

	public String getScanID() {
		return this.scanID;
	}

	/**
	 * Get scan index
	 * @param null
	 * @return index of this psm in string format.
	 */
	
	public String getIndex() {
		return this.index;
	}

	/**
	 * Get charge of precursor in string format
	 * @param null
	 * @return charge
	 */

	public String getCharge() {
		return this.charge;
	}

	/**
	 * Get title of this PSM in string format
	 * @param null
	 * @return title 
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Get peptide sequence without modification in string format
	 * @param null
	 * @return peptide sequence without modification
	 */
	
	public String getStripPepSeq() {
		return this.stripPepSeq;
	}

	/**
	 * Get modifications of this PSM in List<Modification> format
	 * @param null
	 * @return modifications.
	 */

	public List<Modification> getMods() {
		return this.mods;
	}

	/**
	 * Get scores of this PSM in the list<Score> format
	 * @param null
	 * @return scores of this PSM
	 */

	public Score getScore() {
		return this.score;
	}

	/**
	 * Get observed mass of this PSM in Double format
	 * @param null
	 * @return observed mass
	 */

	public Double getObservedMass() {
		return this.observedMass;
	}

	/**
	 * Get calculated mass based on peptide sequence 
	 * @param null
	 * @return CaculatedMass
	 */
	
	public Double getCalculatedMass() {
		return this.calculatedMass;
	}

	/**
	 * Get delta mass of this psm in Double format
	 * @param null
	 * @return delta mass 
	 */

	public Double getDeltaM() {
		return this.deltaM;
	}

	/**
	 * Get m/z of this PSM's precursor in Double format.
	 * @param null
	 * @return precursor m/z
	 */

	public Double getPrecursorMZ() {
		return this.precursorMZ;
	}

	/**
	 * Is the PSM target PSM or not?
	 * @param null
	 * @return "target" or "decoy" 
	 */

	public boolean isTarget() {
		return this.isTarget;
	}

	/**
	 * Get protein identification of this PSM in String format
	 * @param null
	 * @return protein IDs 
	 */

	public String getProteinID() {
		return this.proteinID;
	}

	/**
	 * Get Number of Tolerable Termini in String Format E.g. For
	 *         trypsin, 0: non-tryptic, 1: semi-tryptic, 2: fully-tryptic peptides
	 *         only.
	 * @param null
	 * @return the Number of Tolerable Termini 
	 * 
	 */

	public String getNTT() {
		return this.NTT;
	}

/**
 * Get Position of the PSM in the corresponding protein 
 * @param null
 * @return position
 */
	public String getPosInProt() {
		return this.posInProt;
	}
	
	
	/**
	 * Set scan ID 
	 * @param scanID scan ID
	 * 
	 */

	public void setScanID(String scanID) {
		this.scanID = scanID;
	}

	/**
	 * Set index 
	 * @param index PSM index 
	 * @return null.
	 */

	public void setIndex(String index) {
		this.index = index;
	}

	/**
	 * Set charge
	 * @param charge charge 
	 * @return null.
	 */

	public void setCharge(String charge) {
		this.charge = charge;
	}

	/**
	 * Set title 
	 * @param title title
	 * 
	 */

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Set peptide sequence without modification information
	 * @param strippedpeptideSeq to set in String Format
	 * @return null.
	 */

	public void setStripPepSeq(String stripPepSeq) {
		this.stripPepSeq = stripPepSeq;
		// TODO Auto-generated method stub

	}

	/**
	 * Set list of modifications in the sequece 
	 * @param Modifications list of modifications 
	 * @return null.
	 */

	public void setMods(List<Modification> mods) {
		this.mods = mods;

	}

	/**
	 * Set PSM score for FDR calculation
	 * @param score score used for FDR calculation
	 * @return null.
	 */
	
	public void setScore(Score score) {
		this.score = score;
	}

	/**
	 * Set observed mass of PSM
	 * @param observedMass observed mass 
	 * @return null.
	 */
	
	public void setObservedMass(Double observedMass) {
		this.observedMass = observedMass;
	}

	/**
	 * Set calculated mass 
	 * @param calculatedMass calculated mass 
	 * @return null.
	 */

	public void setCalculatedMass(Double calculatedMass) {
		this.calculatedMass = calculatedMass;
	}

	/**
	 * Set delta mass 
	 * @param detla mass 
	 * @return null.
	 */

	public void setDeltaM(Double deltaM) {
		this.deltaM = deltaM;
	}

	/**
	 * Set precursor m/z 
	 * @param precursorMZ precursor m/z
	 * @return null.
	 */

	public void setPrecursorMZ(Double precursorMZ) {
		this.precursorMZ = precursorMZ;
	}

	/**
	 * Set the condition of target/decoy 
	 * @param target or decoy to set in String Format
	 * @return null.
	 */

	public void setTarget(boolean isTarget) {
		this.isTarget = isTarget;
	}

	/**
	 * Set protein identification information 
	 * @param proteinID protein identification
	 * @return null.
	 */
	
	public void setProteinID(String proteinID) {
		this.proteinID = proteinID;
	}

	/**
	 * Set NTT(Number of Tolerable Termini)
	 * @param set the NTT in String Format E.g. For trypsin,
	 *            0: non-tryptic, 1: semi-tryptic, 2: fully-tryptic peptides only.
	 * @return null
	 * 
	 */
	
	public void setNTT(String nTT) {
		this.NTT = nTT;
	}

	/**
	 * Set the position in protein sequence 
	 * @param posInProt set the position in Protein in this psm
	 */

	public void setPosInProt(String posInProt) {
		this.posInProt = posInProt;
	}

	/**
	 * Set peptide sequence 
	 * @param seq peptide sequence 
	 */
	public void setseq(String seq) {
		this.seq = seq;
	}
	/**
	 * Extract annotated peaks of PSM
	 * @param spec  Spectrum to get Annotated Peaks
	 * @param tol   size of the tolerance
	 * @param unit  tolerance unit to consider. if "ppm" then consider ppm
	 *              tolerance, if "da" then consider da tolerance
	 * @param maxCS maximum fragment charge state to consider. Current maximum
	 *              possible fragment charge is 2.
	 * @return List of AnnoPeak object
	 */
	
	public List<AnnoPeak> extractAnnotatedPeaks(Spectrum spec, double tol, String unit, int maxCS) {
		// TODO Auto-generated method stub
		List<Peak> CurrentPeakInfo = spec.getPeakinfo();
		String seq = this.getStripPepSeq();
		List<PeakWithInfo> TheoPeak = new ArrayList<PeakWithInfo>();// calculate the Theopeak using peptide interface
		List<AnnoPeak> AnnotatedPeakList = new ArrayList<AnnoPeak>();
		TheoPeak = TheoPeakCalculator(seq, maxCS);
		AnnotatedPeakList = GetAnnotatedPeakList(TheoPeak, CurrentPeakInfo, tol, unit);
		this.AnnotatedPeakList = AnnotatedPeakList;
		return AnnotatedPeakList;
	}

	/**
	 * private theoretical peak builder
	 * 
	 * @param seq   sequence to make theoretical peak list
	 * @param maxCS currently up to 2
	 * 
	 * @return List peak of theoretical peak.
	 */
	private List<PeakWithInfo> TheoPeakCalculator(String seq, int maxCS) {
		List<PeakWithInfo> TheoPeak = new ArrayList<PeakWithInfo>();
		if (maxCS >= 1) {
			double TotalMass = 0;
			for (int i = 0; i < seq.length(); i++) {// add b-ion
				double CurrentMass = Parameters.aminoacid_masss[seq.charAt(i) - 'A'];
				TotalMass += CurrentMass;
				PeakWithInfo peak = new PeakWithInfo();
				peak.mass=(TotalMass + (1 * Parameters.Proton));// PROTON mass
				peak.ion_type = "b";
				peak.charge = 1;
				TheoPeak.add(peak);
			}
			TotalMass = 0;
			for (int i = seq.length() - 1; i >= 0; i--) {// add y-ion
				double CurrentMass = Parameters.aminoacid_masss[seq.charAt(i) - 'A'];
				PeakWithInfo peak = new PeakWithInfo();
				TotalMass += CurrentMass;
				peak.mass=(TotalMass + Parameters.Proton + Parameters.H2O);// add Hydrogen + H2O mass
				peak.ion_type = "y";
				peak.charge = 1;
				TheoPeak.add(peak);
			}
			if (maxCS >= 2) {
				TotalMass = 0;
				for (int i = 0; i < seq.length(); i++) {// add b-iom
					double CurrentMass = Parameters.aminoacid_masss[seq.charAt(i) - 'A'];
					TotalMass += CurrentMass;
					PeakWithInfo peak = new PeakWithInfo();
					peak.mass=((TotalMass + Parameters.Proton * 2) / 2);
					peak.ion_type = "b";
					peak.charge = 2;
					TheoPeak.add(peak);
				}
				TotalMass = 0;
				for (int i = seq.length() - 1; i >= 0; i--) {// add y-ion
					double CurrentMass = Parameters.aminoacid_masss[seq.charAt(i) - 'A'];
					TotalMass += CurrentMass;
					PeakWithInfo peak = new PeakWithInfo();
					peak.mass=((TotalMass + Parameters.Proton * 2 + Parameters.H2O) / 2);// add Hydrogen + H2O
					peak.ion_type = "y";
					peak.charge = 2;
					TheoPeak.add(peak);
				}
			}
		} else {
			System.out.println("Wrong charge state. Current Fragment Charge maximum is 2");
			return null;
		}
		this.TheoPeakList = TheoPeak;
		return TheoPeak;
	}

	/**
	 * GetAnnotatedPeak 
	 * 
	 * @param TheoPeaks   List PeakWithInfo of theoretical peak
	 * @param CurrentPeak List peak of current spm peak.
	 * @param tol         tolerance amount
	 * @param unit        "ppm" or "da" in String format
	 * @return List of annotated Peak
	 */
	private List<AnnoPeak> GetAnnotatedPeakList(List<PeakWithInfo> TheoPeakList, List<Peak> CurrentPeakInfo, double tol,
			String unit) {
		List<AnnoPeak> AnnotatedPeakList = new ArrayList<AnnoPeak>();
		for (PeakWithInfo TheoPeak : TheoPeakList) {
			for (Peak CurrentPeak : CurrentPeakInfo) {
				double mass_diff = 0; // mass diff between theopeak and currentpeak.
				if (unit.equals("ppm")) {
					mass_diff = 1e6 * ((TheoPeak.mass - CurrentPeak.mass) / TheoPeak.mass);
				} else if (unit.equals("da")) {
					mass_diff = TheoPeak.mass - CurrentPeak.mass;
				} else {// if unit is not ppm or da
					System.out.println("Wrong unit. Use ppm or da");
					return null;
				}
				if (Math.abs(mass_diff) <= tol) {// if peak matches Within tolerance Created Annotated Peak
					AnnoPeak AnnotatedPeak = new AnnoPeak();
					AnnotatedPeak.setMass(CurrentPeak.mass);
					AnnotatedPeak.setCharge(String.valueOf(TheoPeak.charge));
					AnnotatedPeak.setIntensity(CurrentPeak.intensity);
					AnnotatedPeak.setMass_error(mass_diff);
					AnnotatedPeak.setIon_type(TheoPeak.ion_type);
					AnnotatedPeak.setAnnotation(TheoPeak.ion_type + charge);
					AnnotatedPeakList.add(AnnotatedPeak);
				}
			}
		}
		return AnnotatedPeakList;

	}

	/**
	 * Calculate Xcorr With given spectrum
	 * @param spec Spectrum to get xcorr
	 * @return Calculated Xcorr value
	 */
	public double calcXcorr(Spectrum spec) {
		// TODO Auto-generated method stub
		double obMW = (precursorMZ - Parameters.Proton) * Double.parseDouble(this.charge);
		XCorr xcorr = new XCorr();
		Peptide pep=new Peptide();
		int threoPeak[] = pep.generateThreoPeak(this.stripPepSeq, Parameters.binWidth, this.mods);
		double peptideMass = xcorr.getPeptideMass(this.stripPepSeq, this.mods);
		int evidence[] = xcorr.CreateEvidenceVector(spec.getPeakinfo(), peptideMass, obMW, Parameters.binWidth,
				Integer.valueOf(this.charge));
		int refactoredXcorrValue = 0;
		for (int i = 0; i < threoPeak.length; i++) {
			if (threoPeak[i] >= evidence.length) {
				break;
			}
			refactoredXcorrValue += evidence[threoPeak[i]];
		}

		return (double) refactoredXcorrValue / Parameters.weightScaling;
	}

	/**
	 * Get mass error 
	 * @param spec Spectrum to get mass error
	 * @return Total Error mass value between annotated and theoretical peak
	 */
	public String getMassErr(Spectrum spec) {
		double totalError = 0;
		
		for (AnnoPeak AnnotatedPeak : this.AnnotatedPeakList) {
			totalError += AnnotatedPeak.getMass_error();
		}
		return String.valueOf(totalError);
	}

	/**
	 * get TIC rate of Annotated PeakList
	 * @param spec spectrum 
	 * @return TIC rate of Annotated PeakList
	 */
	public double getAnnotatedTICRate(Spectrum spec) {
		// TODO Auto-generated method stub
		double TIC = spec.getTIC();
		double AnnotatedTIC = 0;
		for (AnnoPeak AnnotatedPeak : this.AnnotatedPeakList) {
			AnnotatedTIC += AnnotatedPeak.getIntensity();
		}
		return AnnotatedTIC / TIC;
	}

	/**
	 * Get annotated peak ratio compared to TheoPeak
	 * @param spec Spectrum to get Annotated Count Rate
	 * @return fraction
	 */
	public double getAnnotatedCountRate(Spectrum spec) {
		// TODO Auto-generated method stub
		return (double) this.AnnotatedPeakList.size() / this.TheoPeakList.size();
	}

	/**
	 * Get the longest serial annotated peak length regardless of ion
	 *         type in int format
	 * @param spec spectrum to get serial annotated peak length;
	 * @return return the longest serial annotated peak length regardless of ion
	 *         type in int format
	 */
	public int getSerialAnnotatedPeakLength(Spectrum spec) {
		// extract b, y ions from Annotated Peak
		Map<String, List<AnnoPeak>> APListMap = new HashMap<String, List<AnnoPeak>>();
		for (AnnoPeak AP : this.AnnotatedPeakList) {
			String ion_type = AP.getIon_type();
			List<AnnoPeak> APList = new ArrayList<AnnoPeak>();

			if (APListMap.get(ion_type) == null) {// if this ion is the first ion
				APList.add(AP);
				APListMap.put(ion_type, APList);
			} else {
				APList = APListMap.get(ion_type);
				APList.add(AP);
				APListMap.put(ion_type, APList);
			}
		}
		// extract b, y ions from Theo Peak
		Map<String, List<PeakWithInfo>> PWIListMap = new HashMap<String, List<PeakWithInfo>>();
		for (PeakWithInfo PWI : this.TheoPeakList) {
			if (PWI.charge == 1) {
				String ion_type = PWI.getIon_type();
				List<PeakWithInfo> PWIList = new ArrayList<PeakWithInfo>();

				if (PWIListMap.get(ion_type) == null) {// if this ion is the first ion
					PWIList.add(PWI);
					PWIListMap.put(ion_type, PWIList);
				} else {
					PWIList = PWIListMap.get(ion_type);
					PWIList.add(PWI);
					PWIListMap.put(ion_type, PWIList);
				}
			}
		}
		/// ??? 1과 2과
		Comparator<AnnoPeak> APCompare = Comparator.comparingDouble((o -> o.getMass()));
		// sort by weight
		int maxSerialCount = 0;
		for (String ion_type : PWIListMap.keySet()) {
			List<PeakWithInfo> PWIList = PWIListMap.get(ion_type);
			int serialCount = 0; // Count sequential number of ions
			double PreviousTheoMass = 0;
			double PreviousAPMass = 0;

			if (APListMap.get(ion_type) != null) {// if there was a match in this type ions

				for (AnnoPeak AP : APListMap.get(ion_type)) {
					for (PeakWithInfo PWI : PWIList) {
						double MassToConsider = (AP.getMass() + AP.getMass_error())
								* Double.parseDouble(AP.getCharge());
						if (PWI.mass == MassToConsider) {
							if (PreviousAPMass != PreviousTheoMass) {
								serialCount = 0;
							}
							PreviousAPMass = MassToConsider;
							serialCount++;
						}
						PreviousTheoMass = PWI.mass;

					}
				}
			}
			maxSerialCount = Math.max(maxSerialCount, serialCount);
		}
		// TODO Auto-generated method stub
		return maxSerialCount;
	}

	/**
	 * @param mass   mass to consider
	 * @param ion    ion type to consider
	 * @param charge charge to consider
	 * @return Calculated ion
	 */

	private double CalcionTypeMass(double mass, String ion_type, int charge) {
		if (ion_type.equals("b")) {
			mass = (mass + 1.00727646688 * (charge - 1)) / charge;
		} else if (ion_type.equals("y")) {
			mass = (mass + 1.0078250321 + 18.0106 + ((charge - 1) * 1.00727646688)) / charge;
		} else {
			System.out.println("invalid ion_type");
		}
		return mass;
	}



	/**
	 * Return Features of current Spectrum
	 * Features include Xcorr, Annotated TICRate, SerialAnnotatedPeakLength, 
	 * MassErr, AnnotatedPeakList	
	 * @param spec Spectrum To Compare

	 * @param psm current psm

	 * @return feature
	 */
	public Feature getAllFeatures(Spectrum spec, PSM psm) {
		Feature feature = new Feature(psm);
		feature.setAll(spec);

		// TODO Auto-generated method stub
		return feature;
	}

	private class XCorr {
		private int[] CreateEvidenceVector(List<Peak> peak, double peptideMass, double obMW, double binWidth,
				int charge) {

			int MAX_XCORR_OFFSET = 75;
			int maxPrecurMass = mass2bin(peptideMass, 1, binWidth);
			double pepMassMonoMean = (maxPrecurMass - 0.5 + Parameters.binOffset) * binWidth;
			maxPrecurMass = mass2bin(obMW + MAX_XCORR_OFFSET + 30, 1, binWidth) + 50;

			double evidenceIntScale = 500.0; // original value is 500
			int nRegion = 10;
			double maxIntensPerRegion = 50.0;
			double precursorMZExclude = 15.0;
			double massNH3Mono = 17.02655; // mass of NH3 (monoisotopic)
			double massCOMono = 27.9949; // mass of CO (monoisotopic)
			double massH2OMono = 18.010564684; // mass of water (monoisotopic)
			double massHMono = 1.0078246; // mass of hydrogen (monoisotopic)
			double BYHeight = 50.0;
			double NH3LossHeight = 10.0;
			double COLossHeight = 10.0; // for creating a ions on the fly from b ions
			double H2OLossHeight = 10.0;

			int ma;
			int pc;
			int ionBin;
			double bIonMass;
			double yIonMass;
			double ionMZMultiCharge;
			double ionMassNH3Loss;
			double ionMassCOLoss;
			double ionMassH2OLoss;

			double[] evidence = new double[maxPrecurMass];
			double[] intensArrayObs = new double[maxPrecurMass];
			int[] intensRegion = new int[maxPrecurMass];
			int[] evidenceInt = new int[maxPrecurMass];

			for (ma = 0; ma < maxPrecurMass; ma++) {
				evidence[ma] = 0.0;
				evidenceInt[ma] = 0;
				intensArrayObs[ma] = 0.0;
				intensRegion[ma] = -1;
			}

			double precurMz = obMW / charge + Parameters.Proton;
			;
			int nIon = peak.size();
			int precurCharge = charge;
			double experimentalMassCutoff = precurMz * precurCharge + 50.0;

			double maxIonMass = 0.0;
			double maxIonIntens = 0.0;
			for (int ion = 0; ion < nIon; ion++) {
				double ionMass = peak.get(ion).mass;
				double ionIntens = peak.get(ion).intensity;
				if (ionMass >= experimentalMassCutoff) {
					continue;
				}
				if (maxIonMass < ionMass) {
					maxIonMass = ionMass;
				}
				if (maxIonIntens < ionIntens) {
					maxIonIntens = ionIntens;
				}
			}
			int regionSelector = (int) Math.floor(
					(int) Math.floor(((double) maxIonMass / binWidth) + 1.0 - Parameters.binOffset) / (double) nRegion);
			for (int ion = 0; ion < nIon; ion++) {
				double ionMass = peak.get(ion).mass;
				double ionIntens = peak.get(ion).intensity;
				if (ionMass >= experimentalMassCutoff) {
					continue;
				}
				if (ionMass > precurMz - precursorMZExclude && ionMass < precurMz + precursorMZExclude) {
					continue;
				}
				ionBin = (int) Math.floor((ionMass / binWidth) + 1.0 - Parameters.binOffset); // REVIEW same as mass2bin
				// method
				int region = (int) Math.floor((double) (ionBin) / (double) regionSelector);
				if (region >= nRegion) {
					region = nRegion - 1;
				}
				intensRegion[ionBin] = region;
				if (intensArrayObs[ionBin] < ionIntens) {
					intensArrayObs[ionBin] = ionIntens;
				}
			}

			maxIonIntens = Math.sqrt(maxIonIntens);
			for (ma = 0; ma < maxPrecurMass; ma++) {
				intensArrayObs[ma] = Math.sqrt(intensArrayObs[ma]);
				if (intensArrayObs[ma] <= 0.05 * maxIonIntens) {
					intensArrayObs[ma] = 0.0;
				}
			}

			double[] maxRegion = new double[nRegion];
			for (int re = 0; re < nRegion; re++) {
				maxRegion[re] = 0.0;
			}
			for (ma = 0; ma < maxPrecurMass; ma++) {
				int reg = intensRegion[ma];
				if (reg >= 0 && maxRegion[reg] < intensArrayObs[ma]) {
					maxRegion[reg] = intensArrayObs[ma];
				}
			}
			for (ma = 0; ma < maxPrecurMass; ma++) {
				int reg = intensRegion[ma];
				if (reg >= 0 && maxRegion[reg] > 0.0) {
					intensArrayObs[ma] *= (maxIntensPerRegion / maxRegion[reg]);
				}
			}
			// delete [] maxRegion;

			// ***** Adapted from tide/spectrum_preprocess2.cc.
			// TODO replace, if possible, with call to
			// static void SubtractBackground(double* observed, int end).
			// Note numerous small changes from Tide code.
			double multiplier = 1.0 / (MAX_XCORR_OFFSET * 2.0 + 1.0);
			double total = 0.0;
			double[] partial_sums = new double[maxPrecurMass];
			for (int i = 0; i < maxPrecurMass; ++i) {
				partial_sums[i] = (total += intensArrayObs[i]); // cumulative sum
			}
			for (int i = 0; i < maxPrecurMass; ++i) {
				int right_index = Math.min(maxPrecurMass - 1, i + MAX_XCORR_OFFSET);
				int left_index = Math.max(0, i - MAX_XCORR_OFFSET - 1);
				intensArrayObs[i] -= multiplier * (partial_sums[right_index] - partial_sums[left_index]); // REVIEW just
				// diminish
				// the value a
				// little
			}
			// delete [] partial_sums;
			// *****
			int binFirst = mass2bin(30, 1, binWidth);
			int binLast = mass2bin(pepMassMonoMean - 47, 1, binWidth);

			for (ma = binFirst; ma <= binLast; ma++) {
				// b ion
				bIonMass = (ma - 0.5 + Parameters.binOffset) * binWidth;
				ionBin = (int) Math.floor(bIonMass / binWidth + 1.0 - Parameters.binOffset);
				evidence[ma] = evidence[ma] + intensArrayObs[ionBin] * BYHeight;
				for (pc = 3; pc <= precurCharge; pc++) {
					ionBin = mass2bin(bIonMass, pc - 1, binWidth);
					evidence[ma] = evidence[ma] + intensArrayObs[ionBin] * BYHeight;
				}
				// y ion
				yIonMass = pepMassMonoMean + 2 * massHMono - bIonMass;
				ionBin = (int) Math.floor(yIonMass / binWidth + 1.0 - Parameters.binOffset);
				evidence[ma] = evidence[ma] + intensArrayObs[ionBin] * BYHeight;
				for (pc = 3; pc <= precurCharge; pc++) {
					ionBin = mass2bin(yIonMass, pc - 1, binWidth);
					evidence[ma] = evidence[ma] + intensArrayObs[ionBin] * BYHeight;
				}
				// NH3 loss from b ion
				ionMassNH3Loss = bIonMass - massNH3Mono;
				ionBin = (int) Math.floor(ionMassNH3Loss / binWidth + 1.0 - Parameters.binOffset);
				evidence[ma] = evidence[ma] + intensArrayObs[ionBin] * NH3LossHeight;
				for (pc = 3; pc <= precurCharge; pc++) {
					ionBin = mass2bin(ionMassNH3Loss, pc - 1, binWidth);
					evidence[ma] = evidence[ma] + intensArrayObs[ionBin] * NH3LossHeight;
				}
				// NH3 loss from y ion
				ionMassNH3Loss = yIonMass - massNH3Mono;
				ionBin = (int) Math.floor(ionMassNH3Loss / binWidth + 1.0 - Parameters.binOffset);
				evidence[ma] = evidence[ma] + intensArrayObs[ionBin] * NH3LossHeight;
				for (pc = 3; pc <= precurCharge; pc++) {
					ionBin = mass2bin(ionMassNH3Loss, pc - 1, binWidth);
					evidence[ma] = evidence[ma] + intensArrayObs[ionBin] * NH3LossHeight;
				}
				// CO and H2O loss from b ion
				ionMassCOLoss = bIonMass - massCOMono;
				ionMassH2OLoss = bIonMass - massH2OMono;
				ionBin = (int) Math.floor(ionMassCOLoss / binWidth + 1.0 - Parameters.binOffset);
				evidence[ma] = evidence[ma] + intensArrayObs[ionBin] * COLossHeight;
				ionBin = (int) Math.floor(ionMassH2OLoss / binWidth + 1.0 - Parameters.binOffset);
				evidence[ma] = evidence[ma] + intensArrayObs[ionBin] * H2OLossHeight;
				for (pc = 3; pc <= precurCharge; pc++) {
					ionBin = mass2bin(ionMassCOLoss, pc - 1, binWidth);
					evidence[ma] = evidence[ma] + intensArrayObs[ionBin] * COLossHeight;
					ionBin = mass2bin(ionMassH2OLoss, pc - 1, binWidth);
					evidence[ma] = evidence[ma] + intensArrayObs[ionBin] * H2OLossHeight;
				}
				// H2O loss from y ion
				ionMassH2OLoss = yIonMass - massH2OMono;
				ionBin = (int) Math.floor(ionMassH2OLoss / binWidth + 1.0 - Parameters.binOffset);
				evidence[ma] = evidence[ma] + intensArrayObs[ionBin] * H2OLossHeight;
				for (pc = 3; pc <= precurCharge; pc++) {
					ionBin = mass2bin(ionMassH2OLoss, pc - 1, binWidth);
					evidence[ma] = evidence[ma] + intensArrayObs[ionBin] * H2OLossHeight;
				}
			}

			// discretize evidence array
			for (ma = 0; ma < maxPrecurMass; ma++) {
				evidenceInt[ma] = (int) Math.floor(evidence[ma] / evidenceIntScale + 0.5);
			}

			return evidenceInt;
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
		

		/**
		 * Calculate peptide mass 
		 * @param strippedPeptideSequence peptide sequence without modificaiton
		 * @param modList list of modifications in the sequence 
		 * return mass
		 * 
		 */
		private double getPeptideMass(String strippedPeptideSequence, List<Modification> modList) {

			double pMass = 0;
			// Add all modification mass
			for (Modification modification : modList) {
				pMass += modification.getDeltaMass();//// >이거 맞는 지 확인.
			}
			// Add all residue mass
			for (char residue : strippedPeptideSequence.toCharArray()) {
				pMass += Parameters.aminoacid_masss[residue - 'A'];
				;
			}
			pMass += Parameters.H2O;

			return pMass;
		}
	}

	/**
	 * Get peptide sequence 
	 * @return the seq in String format
	 */

	public String getSeq() {
		// TODO Auto-generated method stub
		return this.seq;
	}

	/**
	 * Set sequence 
	 * @param seq set the sequence
	 */

	public void setSeq(String seq) {
		// TODO Auto-generated method stub
		
	}

/**
 * Sort PSMs by a specific sore 
 * @param o PSMs
 * @return int value 
 */
	public int compareTo(PSM o) {
		if(this.score.getFDRScore() > o.score.getFDRScore()) {
			return 1;
		}else if (this.score.getFDRScore() < o.score.getFDRScore()) {
			return -1;
		}
		return 0;
	}
}
