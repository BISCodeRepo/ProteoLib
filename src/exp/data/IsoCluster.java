package exp.data;

import java.util.List;

/**
 * Manage Isotope cluster information
 *
 */

public class IsoCluster {
	private List<Peak> peakList;
	private double mono;
//	private int size;
	private double RT;
	
	public IsoCluster() {
		peakList = null;
		mono = 0.0;
//		size = -1;
		RT = 0;
	}
	
	public IsoCluster(double mono, double RT, List<Peak> peakList) {
		this.peakList = peakList;
		this.mono = mono;
//		setSize();
		this.RT = RT;
	}
	/**
	 * Get peak list 
	 * @param null
	 * @return peak list
	 */
	public List<Peak> getPeakList() {
		return peakList;
	}
	
	void setPeakList(List<Peak> peakList) {
		this.peakList = peakList;
	}
	/**
	 * Get mono peak 
	 * @param null
	 * @return mono peak
	 */
	public double getMono() {
		return mono;
	}
	void setMono(double mono) {
		this.mono = mono;
	}
	/**
	 * Get the number of peaks 
	 * @param null
	 * @return peak count
	 */
	public int getSize() {
		return peakList.size();
	}
//	public void setSize() {
//		this.size = peakList.size();
//	}
	/**
	 * Get retention time
	 * @param null
	 * @return retention time
	 */
	public double getRT() {
		return RT;
	}
	void setRT(double rT) {
		RT = rT;
	}
}
