
package exp.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import exp.data.Fasta;

 /**
 * 
 * Read PSM files and calculate FDRs 
 *
 */
public class PSMset{
	
	 private String filename; 
	 private String fragMethod;
	 private Fasta database;
	 private String decoy_prefix;
	 private List<Modification> mods;
	 private List<Modification> PsmSetMods; //msgf파일 하나에 있는 모든 modification을 저장. 
	 private Double fragTol;
	 private String fragTolUnit;
	 private List<PSM> psms;
	
	/**
	 * 
	 * Read files and put PSMs to list 
	 * @param filePath PSM result files with *tsv format
	 * @param decoyToken prefix of decoy header
	 * @throws IOException
	 */
	public PSMset(String filePath, String decoyToken) throws IOException { //decoyToken으로 어떤 펩타이드가 디코이인지 확인한다.
		this.readFile(filePath, decoyToken);
	}
	public PSMset() {
		
	}
	/**
	 * Read files and put PSMs to list 
	 * @param filePath PSM result files with *tsv format
	 * @param decoyToken prefix of decoy header
	 * @throws IOException
	 */
	public void readFile(String filePath, String decoyToken) throws IOException {
	
		Modification.initMap(); //Modification의 해쉬맵을 먼저 만들어 놓는다.
		
		File[] files = new File(filePath).listFiles();
		if(files == null) {
			files = new File[1];
			files[0] = new File(filePath);
		}
		
		for(File file : files) {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine(); // 태그읽기
			line = line.toLowerCase();
			String[] lineSplit = line.split("\t");
			ArrayList<String> al = new ArrayList<>(Arrays.asList(lineSplit));
			int i_specFile = al.indexOf("#specfile");
			int i_specID = al.indexOf("specid");
			int i_scanNum = al.indexOf("scannum");
			int i_title = al.indexOf("title");
			int i_fragMethod = al.indexOf("fragmethod");
			int i_precursorMZ = al.indexOf("precursor");
			int i_isotopeError = al.indexOf("isotopeerror");
			int i_preError = al.indexOf("precursorerror(ppm)");
			int i_charge = al.indexOf("charge");
			int i_seq = al.indexOf("peptide");
			int i_protein = al.indexOf("protein");
			int i_denovo = al.indexOf("denovoscore");
			int i_msgf = al.indexOf("msgfscore");
			int i_spec = al.indexOf("specevalue");
			int i_e = al.indexOf("evalue");
			
			line = br.readLine();
			lineSplit = line.split("\t");
			
			this.filename = filePath; //MSGF파일의 경로+파일이름
			this.psms = new ArrayList<PSM>();
			String SpecFile = lineSplit[i_specFile]; //mgf 파일
			String specID = lineSplit[i_specID];
			String scanNum = lineSplit[i_scanNum];
			String title = lineSplit[i_title];
			this.fragMethod = lineSplit[i_fragMethod];
			double precursorMZ = Double.parseDouble(lineSplit[i_precursorMZ]);				
			String isotopeError = lineSplit[i_isotopeError];
			double deltaM = Double.parseDouble(lineSplit[i_preError]); // ppm
			String charge = lineSplit[i_charge];
			

			String pepSeq = lineSplit[i_seq];
			String peptide = getStripSeq(lineSplit[i_seq]); 
			String proteinID = lineSplit[i_protein];
			
			double observedM = Double.parseDouble(lineSplit[i_precursorMZ]) * Double.parseDouble(charge);
			double calculatedM = observedM + deltaM*precursorMZ*Double.parseDouble(charge)/1000000.0 ;
			boolean isTarget = (proteinID.contains(decoyToken)) ? false : true; //proteinID에 decoyToken이 서브스트링으로 포함되어잇으면 decoy, 그렇지않으면 target
			
			// default FDR score is 14
			Score score = new Score();
			score.addScore(Double.parseDouble(lineSplit[i_denovo]));//DenovoScore
			score.addScore(Double.parseDouble(lineSplit[i_msgf]));//MSGFScore
			score.addScore(Double.parseDouble(lineSplit[i_spec]));//SpecEValue
			score.addScore(Double.parseDouble(lineSplit[i_e]));// EValue
			score.setTargetScore(3);
			score.setFDROrder(true);
			
			PSM psm = new PSM(); 
			psm.setScanID(scanNum);
			psm.setIndex(specID);
			psm.setCharge(charge);
			psm.setTitle(title);
			psm.setSeq(pepSeq);
			psm.setStripPepSeq(peptide);
			psm.setPrecursorMZ(precursorMZ);
			psm.setProteinID(proteinID);
			psm.setDeltaM(deltaM);
			psm.setObservedMass(observedM);
			psm.setCalculatedMass(calculatedM);
			psm.setTarget(isTarget);
			psm.setScore(score);
			
			this.psms.add(psm);
			

			
			this.mods = getModList(pepSeq); //Modification의 argument로 각 psm의 modification된 부분을 파싱해서 만든 List<Modification>을 받는다.
			psm.setMods(this.mods);
			
			

			
			while ((line=br.readLine()) != null) {
				lineSplit = line.split("\t");
				
				
				
				SpecFile = lineSplit[0]; //mgf 파일
				specID = lineSplit[1];
				scanNum = lineSplit[2];
				title = lineSplit[3];
				precursorMZ = Double.parseDouble(lineSplit[5]);
				isotopeError = lineSplit[6];
				deltaM = Double.parseDouble(lineSplit[7]);
				charge = lineSplit[8];
				pepSeq = lineSplit[9];
				peptide = getStripSeq(lineSplit[9]); 
				proteinID = lineSplit[10];
				
				observedM = Double.parseDouble(lineSplit[5]) * Double.parseDouble(charge);
				calculatedM = observedM + deltaM*precursorMZ*Double.parseDouble(charge)/1000000.0 ;
				isTarget = (proteinID.contains(decoyToken)) ? false : true; 
				
				// default FDR score is 14
				score = new Score();
				score.addScore(Double.parseDouble(lineSplit[11]));//DenovoScore
				score.addScore(Double.parseDouble(lineSplit[12]));//MSGFScore
				score.addScore(Double.parseDouble(lineSplit[13]));//SpecEValue
				score.addScore(Double.parseDouble(lineSplit[14]));// EValue
				score.setTargetScore(3);
				score.setFDROrder(true);
				
				psm = new PSM();
				psm.setScanID(scanNum);
				psm.setIndex(specID);
				psm.setCharge(charge);
				psm.setTitle(title);
				psm.setStripPepSeq(peptide);
				psm.setSeq(pepSeq);
				psm.setPrecursorMZ(precursorMZ);
				psm.setProteinID(proteinID);
				psm.setDeltaM(deltaM);
				psm.setObservedMass(observedM);
				psm.setCalculatedMass(calculatedM);
				psm.setTarget(isTarget);
				psm.setScore(score);
				
				this.psms.add(psm);
				
				this.mods = getModList(pepSeq);
				psm.setMods(this.mods);
				
				
			
				
			}
			br.close();
			
			
		}
		
		
	}
	/**
	 * Get peptide sequence without modification 
	  * @param s peptide sequence with modification delta mass, for example, +144.102IYAES+79.966DEEDFK+144.102EQTR
	  * @return peptide sequence without modification delta mass, for example, IYAESDEEDFKEQTR
	 */
	public String getStripSeq(String s) {
		
		String output = "";
		
		for(char c : s.toCharArray()) {
			if (c>='A' && c<='Z') {
				output += String.valueOf(c);
			}
		}
		
		return output;
	}
	
	/**
	 * Get list of modifications in the sequences  
	 * @param seq  peptide sequence with modification delta mass, for example, +144.102IYAES+79.966DEEDFK+144.102EQTR
	 * @return modification List of input sequence
	 */
	
	public List<Modification> getModList(String seq) {
		
		List<Modification> output = new ArrayList<Modification>();
		seq = seq+"X"; // X means end of sequence
		String AA = "";
		
		double tempDouble;
		
		for (char c : seq.toCharArray()) {
			if (c<'A' || c>'Z') {
				AA += String.valueOf(c);
			}
			else {
				if (AA.length() >1) {
					if (AA.charAt(0) == '+' ) {
						tempDouble = Double.parseDouble(AA.substring(1));
						tempDouble = Math.round(tempDouble*1000)/1000.0;
						AA = String.valueOf(tempDouble) + "_" +  String.valueOf(c) +  "_Nterm";
						if (Modification.getMod(AA) == null) {
							AA = String.valueOf(tempDouble) + "_" +  "X" +  "_Nterm";
						}
						output.add(Modification.getMod(AA));
					}
					else {
						tempDouble = Double.parseDouble(AA.substring(2));
						tempDouble = Math.round(tempDouble*1000)/1000.0;
						AA = String.valueOf(tempDouble) + "_" +  AA.substring(0, 1) + "_any";
						if (Modification.getMod(AA) == null) {
							AA = String.valueOf(tempDouble) + "_" +  "X" +  "_Nterm";
						}
						output.add(Modification.getMod(AA)); // ModificationHash 는 그냥 임의로 일단 써둔거임. 나중에 진짜 함수이름으로 바꿔서 하기.
					}					
				}
				AA = String.valueOf(c);
			}
		}

		
		return output;
	}
	/**
	 * Get the number of PSMs
	 * @param null 
	 * @return PSM count
	 */
	public int getSize() {
		return this.psms.size();
	};
	/**
	 * extract PSMs which have only one protein ID
	 * @return psm list 
	 */
	public List<PSM> extractUniquePSMs(){
		
		List<PSM> output = new ArrayList<PSM>();
		PSM tempPsm;
		String tempProteinID;
		String[] split;
		
		int size = this.psms.size();
		for(int i = 0; i<size; i++) {
			tempPsm = psms.get(i);
			tempProteinID = tempPsm.getProteinID();
			split = tempProteinID.split(";");
			if(split.length == 1) {
				output.add(tempPsm);
			}
			
			
		}
		
		return output;
	};
	
//	public List<PSM> extractNonoverlappingPep(){//seq뿐만 아니라 modification도 같아야 동일한 것으로 고쳐야함, 또한 동일한 것 중에 스코어가 높은 것을 뽑도록 해야함.
//		/**
//		 * 
//		 * 
//		 * @return List of unique PSMs which of peptide sequences are different each other
//		 */
//		List<PSM> output = new ArrayList<PSM>();
//		HashMap<String,PSM> unique = new HashMap<String,PSM>();// key는 sequence(strip sequence말고), value는 스코어값.
//		String seq;
//		PSM temp;
//		int size = this.psms.size();
//		
//		for(int i=0; i<size; i++) {
//			temp = psms.get(i);
//			seq = temp.getSeq();
//			if(unique.containsKey(seq)) {
//				if (unique.get(seq).getScore().getFDRScore() < temp.getScore().getFDRScore() && !temp.getScore().getFDROrder()  ) { // msgf 스코어로 비교
//					unique.remove(seq);
//					unique.put(seq, temp);
//				}
//				
//			}
//			else {
//				unique.put(seq, temp); //temp.getScores().get(1)
//			}
//		}
//		for (PSM value : unique.values()) {
//			output.add(value);
//		}
//		
//		return output;
//	};
	
	private PSMset fdr (ArrayList<PSM> psms, double fdrRatio) {
		if(fdrRatio < 0 || fdrRatio > 1) {
			System.err.println("fdrRatio must be from 0 to 1 !!");
			return null;
		}
		boolean smallerIsBetter = this.getPsms().get(0).getScore().getFDROrder();
		Collections.sort(psms);
		if(smallerIsBetter) Collections.reverse(psms);
		
		PSMset newSet = new PSMset();
		newSet.database = this.database;
		newSet.decoy_prefix = this.decoy_prefix;
		newSet.filename = this.filename;
		newSet.fragMethod = this.fragMethod;
		newSet.fragTol = this.fragTol;
		newSet.fragTolUnit = this.fragTolUnit;
		newSet.mods = this.mods;
		newSet.psms = new ArrayList<PSM>();
		newSet.PsmSetMods = this.PsmSetMods;
		
		int threshold = 0;
		int size = psms.size();
		double targets = 0;
		double decoys = 0;
		if(size < 1000) {
			System.err.println("Warning: The number of psms is too small! (" + size +")");
		}
		
		for(int i=0; i<size; i++) {
			PSM psm = psms.get(i);
			if(psm.isTarget()) targets++;
			else decoys++;
			// if it passes the FDR threshold.
			if(targets/(targets+decoys) <= threshold) threshold = i;
		}
		ArrayList<PSM> newPsms = new ArrayList<PSM>();
		for(int i=0; i<=threshold; i++) {
			PSM psm = psms.get(i);
			if(psm.isTarget()) newPsms.add(psm);
		}
		
		return newSet;
	}
	
	/**
	 * Estimate local FDRs (NTT&Charge)
	 * @param fdrRatio fdrRatio must be from 0 to 1. 
	 * @return PSMset PSMset object 
	 */
	public PSMset estimateLocalFDR (PSMset psmset, double fdrRatio) {
		PSMset newSet = new PSMset();
		newSet.database = this.database;
		newSet.decoy_prefix = this.decoy_prefix;
		newSet.filename = this.filename;
		newSet.fragMethod = this.fragMethod;
		newSet.fragTol = this.fragTol;
		newSet.fragTolUnit = this.fragTolUnit;
		newSet.mods = this.mods;
		newSet.psms = new ArrayList<PSM>();
		newSet.PsmSetMods = this.PsmSetMods;
		
		// charge and NTT
		int maxNTT = 2;
		int maxCharge = 4;
		ArrayList[][] psms = new ArrayList[maxNTT+1][maxCharge+1];
		int size = this.psms.size();
		for(int i=0; i<size; i++) {
			PSM psm = this.psms.get(i);
			int ntt = Integer.parseInt(psm.getNTT());
			int charge = Integer.parseInt(psm.getCharge());
			if(charge > 4) charge = 4;
			if(psms[ntt][charge] == null) psms[ntt][charge] = new ArrayList<PSM>();
			psms[ntt][charge].add(psm);
		}
		
	    
		for(int ntt=0; ntt<=maxNTT; ntt++) {
			for(int charge=1; charge<=maxCharge; charge++) {
				if(psms[ntt][charge] == null) continue;
				PSMset tempSet = (PSMset) fdr((ArrayList<PSM>)psms[ntt][charge], fdrRatio);
				newSet.psms.addAll(tempSet.getPsms());
			}
		}
		
		return newSet;
	}
	/**
	 * Estimate global FDRs 
	 * @param fdrRatio fdrRatio must be from 0 to 1. 
	 * @return PSMset PSMset object 
	 */
	public PSMset estimateGlobalFDR (double fdrRatio) {
		return fdr((ArrayList<PSM>)this.psms, fdrRatio);
	}
	/**
	 * Get result result file name 
	 * @param null
	 * @return file name 
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * Set result result file name 
	 * @param filename file name
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/**
	 * Get fragmentation method information
	 * @param null
	 * @return fragmentation method information
	 */
	public String getFragMethod() {
		return fragMethod;
	}
	/**
	 * Set fragmentation method information
	 * @param fragMethod fragmentation method information
	 */
	public void setFragMethod(String fragMethod) {
		this.fragMethod = fragMethod;
	}

	/**
	 * Get database information 
	 * @param null
	 * @return database information 
	 */
	public Fasta getDatabase() {
		return database;
	}
	/**
	 * Set database information 
	 * @param null
	 * @return database information 
	 */
	public void setDatabase(Fasta database) {
		this.database = database;
	}
	/**
	 * Get decoy prefix 
	 * @param null
	 */
	public String getDecoy_prefix() {
		return decoy_prefix;
	}
/**
 * Set decoy prefix
 * @param decoy_prefix decoy prefix
 */
	public void setDecoy_prefix(String decoy_prefix) {
		this.decoy_prefix = decoy_prefix;
	}
/**
 *Get Modifications 
 *@param null
 *@return modification list 
 */
	public List<Modification> getMods() {
		return mods;
	}
/**
 * Set Modifications 
 * @param modification list
  */
	public void setMods(List<Modification> mods) {
		this.mods = mods;
	}
	/**
	 * Get fragment tolerance 
	 * @param null 
	 * @return fragment tolerance
	 */
	public Double getFragTol() {
		return fragTol;
	}
/**
 * Set fragment tolerance 
 * @param fragTol fragment tolerance 
 */
	public void setFragTol(Double fragTol) {
		this.fragTol = fragTol;
	}
/**
 * Get the unit of fragment tolerance 
 * @return fragment tolerance unit 
 */
	public String getFragTolUnit() {
		return fragTolUnit;
	}
	/**
	 * Set the unit of fragment tolerance 
	 * @param fragTolUnit fragment tolerance unit 
	 */
	public void setFragTolUnit(String fragTolUnit) {
		this.fragTolUnit = fragTolUnit;
	}
/**
 * Get PSMs 
 * @param null
 * @return list of PSMs
 */
	public List<PSM> getPsms() {
		return psms;
	}
/**
 * Set PSM list 
 * @param psms PSM list
 */
	public void setPsms(List<PSM> psms) {
		this.psms = psms;
	}
	
	

}
