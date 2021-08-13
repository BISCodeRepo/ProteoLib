package examples;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import exp.data.Peak;
import exp.data.Spectra;
import exp.data.Spectrum;
import exp.statics.IsobaricTag;

/**
 * Example of erasing reporter ions <br>
 * 
 * @author jihun cha
 *
 */

public class EraseReporterIons {
	/******** Description *********/
	/**
	 * INPUT: a folder including MGF files, labeling protocol
	 * OUTPUT: a folder including Reporter ion erased MGFs
	 * 
	 **/

	// input mgf dir path, output mgf dir path
	public static final String INPUTMGFPATH = "D:\\Data\\ProteomeTools\\MGF\\1\\";
	public static final String OUTPUTMGFPATH = "D:\\Data\\ProteomeTools\\MGF\\RPErasedMGF";

	// label protocol : 0 = iTRAQ 4plex, 1 = iTRAQ 8plex, 2 = TMT 2plex, 3 = TMT 6plex, 4 = TMT 10plex 
	public static final int LABELPROTOCOL = 4;
	// Fragment tolerance
	public static final double FRAG_TOL = 0.025;

	public static void main(String[] args) throws IOException {
		File input_dir = new File(INPUTMGFPATH);
		File output_dir = new File(OUTPUTMGFPATH);
		if (!output_dir.exists())
			output_dir.mkdir();

		// reading mgf file one by one
		File input_mgfs[] = input_dir.listFiles();
		for (File input_mgf : input_mgfs) {
			//saving all spectra
			Spectra spectra = new Spectra();
			spectra.readFiles(input_mgf.getAbsolutePath());

			// get new mgf path
			String title = input_mgf.getName().replace(".mgf", "");
			File newmgf = new File(OUTPUTMGFPATH + File.separatorChar + title + "_RIonErased.mgf");
			System.out.println(newmgf.getAbsolutePath());
			BufferedWriter newmgf_bw = new BufferedWriter(new FileWriter(newmgf));

			// write new mgf
			for (int i = 0; i < spectra.getSize(); i++) {
				Spectrum ions = spectra.getSpectrumByIndex(i);
				newmgf_bw.write("BEGIN IONS\n");
				newmgf_bw.write("SCANS=" + ions.getScanID() + "\n");
				newmgf_bw.write("TITLE=" + ions.getTitle() + "\n");
				newmgf_bw.write("RTINSECONDS=" + ions.getRT() + "\n");
				newmgf_bw.write("PEPMASS=" + ions.getPepMassInfo() + "\n");
				newmgf_bw.write("CHARGE=" + ions.getTitle().split("\\.")[3] + "+\n");

				//erase reporter ion mass w/ peak info and reporterion mass table
				List<Peak> peaks = ions.getPeakinfo();
				for (Peak peak : peaks) {
					boolean checkion = CheckReporterIon(peak.mass);
					if (!checkion)
						newmgf_bw.write(peak.mass + " " + peak.intensity + "\n");
					else
						continue;
				}
				newmgf_bw.write("END IONS\n");
			}
			newmgf_bw.close();
		}
	}

	/** 
	 * Check if the mass is within reporter ion mass +- fragment tol
	 * @param mass - peak mass
	 * @return boolean 
	 */
	private static boolean CheckReporterIon(double mass) {
		double reporterIonMasses[] = IsobaricTag.MASS_TABLE[LABELPROTOCOL];
		for (int index = 1; index < reporterIonMasses.length; index++) {
			if (mass - FRAG_TOL <= reporterIonMasses[index] && mass + FRAG_TOL >= reporterIonMasses[index]) {
				return true;
			}
		}
		return false;
	}
}
