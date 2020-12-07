package exp.search;
/**
 * Store one annotated peak 
 *
 */
public class AnnoPeak {
	private double mass; 
	private double intensity;
	private String ion_type;
	private String charge;
	private double mass_error;
	private String annotation;	//	y1, b1, y1-H2O, ...
	/**
	 * Get annotated peak mass 
	 * @return mass 
	 */
	public double getMass() {
		return mass;
	}
	/**
	 * Set annotated peak mass 
	 * 
	 */
	public void setMass(double mass) {
		this.mass = mass;
	}
	/**
	 * Get annotated peak intensity 
	 * 
	 */
	public double getIntensity() {
		return intensity;
	}
	/**
	 * Set annotated peak intensity 
	 * 
	 */
	public void setIntensity(double intensity) {
		this.intensity = intensity;
	}
	/**
	 * Get ion type of the annotated peak
	 * @return ion_type
	 */
	public String getIon_type() {
		return ion_type;
	}
	/**
	 * Set ion type of the annotated peak
	 * 
	 */
	public void setIon_type(String ion_type) {
		this.ion_type = ion_type;
	}
	/**
	 * Get fragment ion charge
	 * @return charge
	 */
	public String getCharge() {
		return charge;
	}
	/**
	 * Set fragment ion charge
	 */
	public void setCharge(String charge) {
		this.charge = charge;
	}
	/**
	 * Get mass error between observed and theoretical peaks
	 */
	public double getMass_error() {
		return mass_error;
	}
	/**
	 * Set mass error between observed and theoretical peaks
	 */
	public void setMass_error(double mass_error) {
		this.mass_error = mass_error;
	}
	/**
	 * Get annotation
	 * 
	 */
	public String getAnnotation() {
		return annotation;
	}
	/**
	 * Set annotation
	 * 
	 */
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
}	
