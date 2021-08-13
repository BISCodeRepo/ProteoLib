package exp.util;

import exp.statics.Codon;

public class Translator {
	/**
	 * arguments: <br>
	 * nucleotides: DNA sequences such as 'A', 'C', 'T', 'G' <br>
	 * frame: start position only allowing 0, 1, 2 <br> 
	 * isTerminatedAtStopCodon: ends with TGA, TAG, TAA <br>
	 * 
	 * output: translated peptide sequence <br>
	 * 
	 * @param nucleotides
	 * @param frame
	 * @param isTerminatedAtStopCodon
	 * @return
	 */
	public static String translation (String nucleotides, int frame, boolean isTerminatedAtStopCodon) {
		StringBuilder peptides = new StringBuilder();
		for(int position=frame; position<nucleotides.length()-2; position+=3) {
			char aa = Codon.nuclToAmino(nucleotides.substring(position,position+3));
			if(isTerminatedAtStopCodon && aa == 'X') break;
			peptides.append(aa);
		}
		return peptides.toString();
	}
	
	/**
	 * arguments: <br>
	 * nucleotides: DNA sequences such as 'A', 'C', 'T', 'G' <br>
	 * frame: start position only allowing 0, 1, 2 <br> 
	 * isTerminatedAtStopCodon: ends with TGA, TAG, TAA <br>
	 * 
	 * output: reverse-complement translated peptide sequence <br>
	 * 
	 * @param nucleotides
	 * @param frame
	 * @param isTerminatedAtStopCodon
	 * @return
	 */
	public static String reverseComplementTranslation (String nucleotides, int frame, boolean isTerminatedAtStopCodon) {
		StringBuilder peptides = new StringBuilder();
		StringBuilder reverseComplementNTs = new StringBuilder(nucleotides);
		for(int i=0; i<nucleotides.length(); i++) {
			switch(reverseComplementNTs.charAt(i)) {
				case 'A': reverseComplementNTs.setCharAt(i, 'T'); break;
				case 'C': reverseComplementNTs.setCharAt(i, 'G'); break;
				case 'T': reverseComplementNTs.setCharAt(i, 'A'); break;
				case 'G': reverseComplementNTs.setCharAt(i, 'C'); break;
				default : break;
			}
		}
		
		nucleotides = reverseComplementNTs.reverse().toString();
		for(int position=frame; position<nucleotides.length()-2; position+=3) {
			char aa = Codon.nuclToAmino(nucleotides.substring(position,position+3));
			if(isTerminatedAtStopCodon && aa == 'X') break;
			peptides.append(aa);
		}
		return peptides.toString();
	}
	
	/**
	 * 
	 * arguments: <br>
	 * nucleotides: DNA sequences such as 'A', 'C', 'T', 'G' <br>
	 * frame: start position only allowing 0, 1, 2 <br> 
	 * isTerminatedAtStopCodon: ends with TGA, TAG, TAA <br>
	 * 
	 * output: complement translated peptide sequence <br>
	 * 
	 * @param nucleotides
	 * @param frame
	 * @return
	 */
	public static String complementTranslation (String nucleotides, int frame) {
		StringBuilder peptides = new StringBuilder();
		StringBuilder reverseComplementNTs = new StringBuilder(nucleotides);
		for(int i=0; i<nucleotides.length(); i++) {
			switch(reverseComplementNTs.charAt(i)) {
				case 'A': reverseComplementNTs.setCharAt(i, 'T'); break;
				case 'C': reverseComplementNTs.setCharAt(i, 'G'); break;
				case 'T': reverseComplementNTs.setCharAt(i, 'A'); break;
				case 'G': reverseComplementNTs.setCharAt(i, 'C'); break;
				default : break;
			}
		}
		
		nucleotides = reverseComplementNTs.toString();
		for(int position=frame; position<nucleotides.length()-2; position+=3) {
			peptides.append(Codon.nuclToAmino(nucleotides.substring(position,position+3)));
		}
		return peptides.toString();
	}
}
