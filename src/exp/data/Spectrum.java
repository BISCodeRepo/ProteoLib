package exp.data; 
import java.util.*;

/**
 * Store spectrum information, extract features, and pre-process peaks 
 * 
 * @author daewook
 *
 */
public class Spectrum {
	/**
	 * scan number 
	 */
	private int scanID;
	/**
	 * list of peaks in a spectrum
	 */
	private List<Peak> peakinfo;
	/**
	 * total ion current  
	 */
	private double TIC;
	/**
	 * retention time  
	 */
	private double RT;
	/**
	 * ms level 
	 */
	private int msLevel;
	/**
	 * parent information
	 * -1: no information for parent(MS1) scan number (eg. *mgf input file) 
	 */
	private int parentInfo;
	/**
	 * file index
	 */
	private int fileID; 	
	
	/**
	 * get the maximum intensity peak among all peaks 
	 * @param null 
	 * @return Peak 
	 */
	
	public Peak getMaxIntensityPeak() {
		Peak maxPeak = null;
		double maxInt = Double.MIN_VALUE;
		int idx = 0;

		for (int i = 0; i < peakinfo.size(); i++) {
			if (maxInt < peakinfo.get(i).intensity) {
				idx = i;
				maxInt = peakinfo.get(i).intensity;
			}
		}
		maxPeak = peakinfo.get(idx);
		return maxPeak;
	}

	
	/**
	 * Get the number of peaks
	 * @param null 
	 * @return peakinfo(list) size.
	 */
	
	public int getPeakCount() {
		return peakinfo.size();
	}

	/**
	 * Filter noise peaks under the user-defined threshold
	 * @param threshold  threshold   
	 * @return List<Peak> including peaks bigger than threshold.
	 */
	
	public List<Peak> filterNoisePeaks(double threshold) {
		List<Peak> filteredList = new ArrayList<>();

		for (int i = 0; i < peakinfo.size(); i++) {
			if (peakinfo.get(i).intensity >= threshold) {
				filteredList.add(peakinfo.get(i));
			}
		}
		return filteredList;
	}

	/**
	 * Extract top N peak List.  (not considering isotope) 
	 * @param n - number of top N 
	 * @return List<Peak> including top N Peaks. (sorted by descending order)
	 */
	
	
	public List<Peak> topN(int n) {
		List<Peak> topList = new ArrayList<>();
		int num = 0;
		
		List<Peak> temp = peakinfo;
		//sorting using comparator.  descending sort by intensity
		CompareInt comp = new CompareInt();
		Collections.sort(temp, comp);

		for(int i=0; i<n; i++) {
			topList.add(temp.get(i));
		}

		return topList;
	}
	
	/**
	 * Predict Isotope Cluster - Not implemented yet.
	 * - Under study
	 * @param double error_tolerance 
	 * 
	 */	
	private List<IsoCluster> predictIsotopeCluster(double error_tolerance) {
		System.out.println("Not implemented yet.");
		return null;
	}

		
//	public IsoCluster getIsotopeCluster(double mass, int charge, double error_tolerance) {
//		System.out.println("Not implemented yet.");
//		return null;
//	}
	/**
	 * Get scan number 
	 * @param null
	 * @return int scan number 
	 */
	public int getScanID() {
		return scanID;
	}


	protected void setScanID(int scanID) {
		this.scanID = scanID;
	}

	/**
	 * Get list of peaks  
	 * @param null
	 * @return list of peaks  
	 */
	public List<Peak> getPeakinfo() {
		return peakinfo;
	}

	
	protected void setPeakinfo(List<Peak> peakinfo) {
		this.peakinfo = peakinfo;
	}

	/**
	 * Get total ion current (TIC)  
	 * @param null
	 * @return double TIC 
	 */
	
	public double getTIC() {
		return TIC;
	}


	protected void setTIC(double tIC) {
		TIC = tIC;
	}

	/**
	 * Get retention time   
	 * @param null
	 * @return double retention time   
	 */
	
	public double getRT() {
		return RT;
	}


	protected void setRT(double rT) {
		RT = rT;
	}

	/**
	 * Get ms level   
	 * @param null
	 * @return int ms level   
	 */
	
	public int getMSLevel() {
		return msLevel;
	}


	protected void setMSLevel(int msLevel) {
		this.msLevel = msLevel;
	}


	public int getParentInfo() {
		return parentInfo;
	}


	protected void setParentInfo(int parentInfo) {
		this.parentInfo = parentInfo;
	}
	
	protected void setFileID(int fileID){
		this.fileID=fileID;
	}
	
	public int getFileID()
	{
		return this.fileID;
	}
	/** 
	 * Calculate Total Ion Current(TIC).
	 * @param List<peak> list - all peak list in a spectrum 
	 * @return int TIC - Sum of all peak intensities 
	 */
	protected double TICSum(List<Peak> list) {
		double sum = 0;
		for (int i = 0; i < list.size(); i++) {
			sum = sum + list.get(i).intensity;
		}
		return sum;
	}
	
}
/**
 * sort peak list based on intensity 
 */
class CompareInt implements Comparator<Peak> {
	
	public int compare(Peak first, Peak second) {
		double firstInt = first.intensity;
		double secondInt = second.intensity;

		// order by descending
		if (firstInt > secondInt) {
			return -1;
		} else if (firstInt < secondInt) {
			return 1;
		} else {
			return 0;
		}
	}
}
