package exp.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Pattern;

import org.systemsbiology.jrap.stax.MSXMLParser;
import org.systemsbiology.jrap.stax.Scan;
import org.systemsbiology.jrap.stax.ScanHeader;

/**
 * Read MS spectra from one or more *mgf(or *mzXML) files and store them to list
 * 
 * <p>
 * 
 * @author sangho
 * @date 28-08-2020
 * @version 1.1
 *
 */

public class Spectra {

	/**
	 * list of file names
	 */
	private String[] fileNames = null; // unuse the first index.
	/**
	 * the number of files
	 */
	private int sizeOfFiles = 0;
	/**
	 * the file format of the processed files
	 */
	private String fileformat;
	/**
	 * list of spectrum
	 */
	private List<Spectrum> specList = new ArrayList<Spectrum>();
	private Hashtable<String, ArrayList<Spectrum>> spectraByFileScan = new Hashtable<String, ArrayList<Spectrum>>();
	private Hashtable<String, Spectrum> spectrumByTitle = new Hashtable<String, Spectrum>();

	/**
	 * get spectrum by index. <br>
	 * 
	 * outOfIndexBound: return null <br>
	 * 
	 * @param index
	 * @return
	 */
	public Spectrum getSpectrumByIndex(int index) {
		// check index bound.
		if (index < 0 || this.specList.size() < index)
			return null;

		Spectrum spectrum = this.specList.get(index);
		return spectrum;
	}

	/**
	 * Get the number of spectra in the list of MS scan files
	 * 
	 * @param null
	 * @return spectrum count
	 */

	public int getSize() {
		return specList.size();
	}

	/**
	 * Read MS scan files and store them to the list of spectrum
	 * 
	 * @param path directory path or a specific file path
	 * @return void
	 */

	public void readFiles(String path) {
		String readpath = path;

		File[] files = new File(readpath).listFiles();
		if (files == null) {
			files = new File[1];
			files[0] = new File(readpath);
		}
		fileNames = new String[files.length + 1];

		sizeOfFiles = 0;
		for (File file : files) {
			sizeOfFiles++;
			String fileName = file.getName();
			fileNames[sizeOfFiles] = fileName;
			if (fileName.toLowerCase().endsWith(".mgf")) {
				setFileformat("mgf");
				try {
					readMGF(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (fileName.toLowerCase().endsWith(".mzxml")) {
				setFileformat("mzxml");
				try {
					readMZXML(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {

			}

		}
	}

	/**
	 * Parse a mgf file and save the spectra to the list of spectrum. MGF file
	 * format grammar http://kirchnerlab.github.io/libmgf/doc/html/index.html
	 * 
	 * @param File file
	 * @return void
	 */
	private void readMGF(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		Spectrum spectrum = null;
		Pattern p = Pattern.compile("(^[0-9]$)");
		boolean scanCheck = false;

		while ((line = br.readLine()) != null) {
			if (line.contains("BEGIN IONS")) {
				spectrum = new Spectrum();
				spectrum.setFileID(sizeOfFiles);
				spectrumClear(spectrum, scanCheck);
			} else if (line.contains("END IONS")) {
				spectrum.setMSLevel(2);
				spectrum.setParentInfo(-1);
				spectrum.setTIC(spectrum.TICSum(spectrum.getPeakinfo()));

				String key = fileNames[sizeOfFiles] + "_" + spectrum.getScanID();
				ArrayList<Spectrum> mappedSpectra = spectraByFileScan.get(key);
				if (mappedSpectra == null)
					mappedSpectra = new ArrayList<Spectrum>();
				mappedSpectra.add(spectrum);
				spectraByFileScan.put(key, mappedSpectra);

				specList.add(spectrum);
			} else if (line.contains("TITLE=")) {
				String title = "";
				if (line.contains("File:"))
					title = line.split(" ")[0].split("=")[1];
				else
					title = line.split("=")[1];
				spectrum.setTitle(title);
			} else if (line.contains("PEPMASS=")) {
				spectrum.setPepMassInfo(line.split("=")[1]);
			} else if (line.contains("RTINSECONDS")) {
				String[] tok = line.split("=");
				spectrum.setRT(Double.parseDouble(tok[1]));
			} else if (p.matcher(line.substring(0, 1)).find()) {
				Peak peak = new Peak();
				String[] tok = line.split(" ");
				peak.mass = (Double.parseDouble(tok[0]));
				peak.intensity = (Double.parseDouble(tok[1]));
				spectrum.getPeakinfo().add(peak);
			} else {
				findScanNum(spectrum, line, scanCheck);
			}
		}
		br.close();
	}

	/**
	 * Parse a *mzXML file and save the spectra to the list of spectrum. Using jrap
	 * http://javaprotlib.sourceforge.net/javadoc/jrap/rel1.2/
	 * http://maltcms.de/staging/maltcms/maltcms-io-mzxml/apidocs/org/systemsbiology/jrap/staxnxt/ScanHeader.html
	 * 
	 * @param File file
	 * @return void
	 */
	private void readMZXML(File file) throws IOException {
		MSXMLParser parser = new MSXMLParser(file.getAbsolutePath());
		Spectrum spectrum = null;

		for (int scanNum = 1; scanNum < parser.getScanCount() + 1; scanNum++) {
			spectrum = new Spectrum();
			spectrum.setFileID(sizeOfFiles);
			spectrumClear(spectrum);

			Scan scan = parser.rap(scanNum);
			ScanHeader shead = scan.getHeader();

			spectrum.setScanID(scanNum);

			double[][] massAndintencity = scan.getMassIntensityList();
			for (int i = 0; i < massAndintencity[0].length; i++) {
				Peak peak = new Peak();
				peak.mass = (massAndintencity[0][i]);
				peak.intensity = (massAndintencity[1][i]);
				spectrum.getPeakinfo().add(peak);
			}

			spectrum.setTIC(spectrum.TICSum(spectrum.getPeakinfo()));

			spectrum.setRT(shead.getDoubleRetentionTime());

			spectrum.setMSLevel(shead.getMsLevel());

			if (spectrum.getMSLevel() == 1) {
				spectrum.setParentInfo(-1);
			} else {
				spectrum.setParentInfo(shead.getPrecursorScanNum());
			}

			String key = fileNames[sizeOfFiles] + "_" + spectrum.getScanID();
			ArrayList<Spectrum> mappedSpectra = spectraByFileScan.get(key);
			if (mappedSpectra == null)
				mappedSpectra = new ArrayList<Spectrum>();
			mappedSpectra.add(spectrum);
			spectraByFileScan.put(key, mappedSpectra);

			specList.add(spectrum);
		}

	}

	/**
	 * How the scan number are read on file by file basis.
	 * 
	 * @param spectrum, String line - file in line
	 * @param boolean   scanCheck - describe scan number within spectrum
	 * @return void
	 */
	private void findScanNum(Spectrum spectrum, String line, boolean scanCheck) {

		if (line.startsWith("TITLE=")) {
			String title = line.split("=")[1];
			spectrumByTitle.put(title, spectrum);
		}

		if (line.contains("SCANS") && !line.substring(0, 5).equals("TITLE") && scanCheck == false) { // SCANS=2366
			String[] tok = line.split("=");
			spectrum.setScanID(Integer.parseInt(tok[1]));
			scanCheck = true;
		} else if (line.substring(0, 5).equals("TITLE") && scanCheck == false) {
			String[] tok = line.split(" ");
			for (int i = 0; i < tok.length; i++) {
				if (tok[i].contains("scans:")) { // TITLE=File66 Spectrum74 scans: 2166
					spectrum.setScanID(Integer.parseInt(tok[i + 1]));
					scanCheck = true;
					break;
				} else if (tok[i].contains("scan=")) { // TITLE=170628_H1299_HPH_F01.4.4.1
														// File:"170628_H1299_HPH_F01.raw", NativeID:"controllerType=0
														// controllerNumber=1 scan=4"
					String[] str = tok[i].split("=");
					spectrum.setScanID(Integer.parseInt(str[1].substring(0, str[1].length() - 1)));
					scanCheck = true;
					break;
				}
			}
		}
	}

	/**
	 * Initialize spectrum object.
	 * 
	 * @param spectrum object
	 * @param boolean  scanCheck - describe scan number within spectrum
	 * @return void
	 */
	private void spectrumClear(Spectrum spectrum, boolean scanCheck) {
		ArrayList<Peak> peakinfo = new ArrayList<Peak>();
		spectrum.setMSLevel(0);
		spectrum.setParentInfo(0);
		spectrum.setPeakinfo(peakinfo);
		spectrum.setRT(0);
		spectrum.setScanID(0);
		spectrum.setTIC(0);
		scanCheck = false;
	}

	/**
	 * Initialize spectrum object.
	 * 
	 * @param spectrum object
	 * @return void
	 */

	private void spectrumClear(Spectrum spectrum) {
		ArrayList<Peak> peakinfo = new ArrayList<Peak>();
		spectrum.setMSLevel(0);
		spectrum.setParentInfo(0);
		spectrum.setPeakinfo(peakinfo);
		spectrum.setRT(0);
		spectrum.setScanID(0);
		spectrum.setTIC(0);

	}

	/**
	 * Return the spectrum with the corresponding scan number among all spectra
	 * included in the file
	 * 
	 * @param fileName file name
	 * @param scanNum  scan number
	 * @return spectrum spectrum object
	 */

	public ArrayList<Spectrum> getSpectraByFileScan(String fileName, int scanNum) {
		String key = fileName + "_" + scanNum;
		return spectraByFileScan.get(key);
	}

	/**
	 * Get the list of processed file names
	 * 
	 * @param null
	 * @return file names
	 */

	public String[] getFileNames() {
		return fileNames;
	}

	/**
	 * Set the list of file names
	 * 
	 * @return void
	 */

	private void setFileNames(String[] fileNames) {
		this.fileNames = fileNames;
	}

	/**
	 * Get file format of processed file names
	 * 
	 * @param null
	 * @return processed file format
	 */

	public String getFileformat() {

		return fileformat;
	}

	private void setFileformat(String fileformat) {
		this.fileformat = fileformat;
	}

	//	private void setSpecList(List<Spectrum> specList) {
	//		this.specList = specList;
	//	}

	/**
	 * Get the list of spectrum which contains all the spectra in the files
	 * 
	 * @return list of spectrum
	 */

	//	public List<Spectrum> getSpecList() {
	//		return specList;
	//	}

	/**
	 * Return the spectrum with the corresponding scan title among all spectra
	 * included in the file
	 * 
	 * @param title scan title
	 * @return spectrum object
	 */

	public Spectrum getSpectrumByTitle(String title) {
		return this.spectrumByTitle.get(title);
	}

}