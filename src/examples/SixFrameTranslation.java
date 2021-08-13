package examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import exp.util.Translator;

/**
 * Example of six-frame translation <br>
 * 
 * @author progi
 *
 */
public class SixFrameTranslation {

	public static void main(String[] args) throws IOException {
		/******** Description *********/
		/**
		 * INPUT: a folder including fasta files, and each fasta file contains a single header.
		 * OUTPUT: sixframe-translated peptide sequences.
		 * 
		 */

		// 25개의 fasta 파일이 있는 폴더
		// INPUT PATH
		String inputPath = "D:\\GBM\\FASTA\\FASTA";
		// OUTPUT FILE NAME
		String outputPath = "";
		
		/******** RUN ALGORITHME **********/

		File inputFile = new File(inputPath);
		File[] fileList = inputFile.listFiles();
		FileWriter forDB = new FileWriter(new File(outputPath), true);

		for (File file : fileList) {
			if (file.isFile()) {
				String fileName = file.getName();
				String chromo = fileName.split("\\.")[0];
				System.out.println("fileName : " + fileName);

				try {
					FileReader fasta = new FileReader(file);
					BufferedReader bufReaderForfasta = new BufferedReader(fasta);
					String lineForFasta = "";
					StringBuffer sb = new StringBuffer();
					String gSeq = "";
					int countForFasta = 0;
					while ((lineForFasta = bufReaderForfasta.readLine()) != null) {
						if (countForFasta != 0) {
							sb.append(lineForFasta.replaceAll("\n", ""));
						}
						countForFasta++;
					}
					System.out.println(chromo + ".fa read done");
					gSeq = sb.toString();
					for (int frame = 0; frame < 3; frame++) {
						String longSequence = Translator.translation(gSeq, frame, false);
						long startPosition = 1;
						long endPosition = 1;
						startPosition = startPosition + frame;
						String[] peptides = longSequence.split("X");

						for (int i = 0; i < peptides.length; i++) {
							if (peptides[i].length() < 8) {
								startPosition = startPosition + peptides[i].length() * 3 + 3;
								continue;
							}
							endPosition = startPosition + peptides[i].length() * 3 - 1;
							forDB.write(">chr" + chromo + ":" + startPosition + "-" + endPosition + "_" + frame + "+" + "\n");
							forDB.write(peptides[i] + "\n");
							startPosition = startPosition + peptides[i].length() * 3 + 3;
						}
					}

					for (int frame = 0; frame < 3; frame++) {
						String longSequence = Translator.reverseComplementTranslation(gSeq, frame, false);
						long startPosition = gSeq.length();
						long endPosition = 1;
						startPosition = startPosition - frame;
						String[] peptides = longSequence.split("X");

						for (int i = 0; i < peptides.length; i++) {

							if (peptides[i].length() < 8) {
								startPosition = startPosition - peptides[i].length() * 3 - 3;
								continue;
							}
							endPosition = startPosition - peptides[i].length() * 3 + 1;
							forDB.write(">chr" + chromo + ":" + startPosition + "-" + endPosition + "_" + frame + "-" + "\n");
							forDB.write(peptides[i] + "\n");
							startPosition = startPosition - peptides[i].length() * 3 - 3;
						}
					}
					bufReaderForfasta.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		forDB.close();
	}
}
