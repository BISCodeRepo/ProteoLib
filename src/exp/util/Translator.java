package exp.util;

import java.util.ArrayList;

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
			String inputCodon = nucleotides.substring(position,position+3);
			peptides.append(Codon.nuclToAmino(inputCodon));
		}
		return peptides.toString();
	}
	
	/**
	 * 
	 * arguments: <br>
	 * fastaSeq: DNA sequences of one fasta file <br>
	 * Notice message will print only when there are other sequences other than 'A', 'T', 'C', 'G', 'N' <br>
	 * 
	 * @param fastaSeq
	 */
	public static void checkOnlyACTGN(String fastaSeq) {
		String checkList[] = new String[] {"a","c","t","g","n","A","C","T","G","N"};
		String error = fastaSeq;
		ArrayList<String> errorList = new ArrayList<>();
		for(String i :checkList) {
			error = error.replaceAll(i, "");
			}
		if(error.length()!=0) {
			String[] errors = error.split("");
			for(String errorSeq : errors) {
				if (!errorList.contains(errorSeq)) {
					errorList.add(errorSeq);
				}
			}
			System.out.println("Wrong fasta sequence is in your fasta file");
			System.out.print("Wrong sequence : ");
			for(String errorSeq: errorList) {
				System.out.print(errorSeq+" ");
			}
			System.out.println();
			System.out.println("We treated codon that contains error sequence as 'X'");
		}
		}
}
