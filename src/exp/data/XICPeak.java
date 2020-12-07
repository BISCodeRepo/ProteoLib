
package exp.data;

import java.util.List;
/**
 * Manage XIC peaks 
 */
public class XICPeak {
	private double mass;
	private double intensity;
	private List<Integer> scan_numbers;
	/**
	 * Get peak mass 
	 * @return mass 
	 */
	public double getMass() {
		return mass;
	}
	/**
	 * set peak mass 
	 * @param mass
	 */
	public void setMass(double mass) {
		this.mass = mass;
	}
	/**
	 * get peak intensity
	 * @return intensity
	 */
	public double getIntensity() {
		return intensity;
	}
	/**
	 * set peak intensity 
	 * @param intensity
	 */
	public void setIntensity(double intensity) {
		this.intensity = intensity;
	}
	/**
	 * Get list of scan numbers
	 * @return list of scan numbers 
	 */
	public List<Integer> getScan_numbers() {
		return scan_numbers;
	}
	/**
	 * Set list of scan numbers 
	 * @param scan_numbers
	 */
	
	public void setScan_numbers(List<Integer> scan_numbers) {
		this.scan_numbers = scan_numbers;
	}
}
