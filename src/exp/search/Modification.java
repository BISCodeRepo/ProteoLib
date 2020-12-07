package exp.search;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import res.*;
/**
 * Manage modification information 
 *
 */
public class Modification {
	
	private static HashMap<String, Modification> map = new HashMap<String, Modification>();
	
	/**
	 * Get modification information
	 * Modification key: deltaMass_site_position.
	 * 
	 * @param site modification site
	 * @param position position in a sequence 
	 * @param deltaMass delta mass 
	 * @return modification
	 */
	public static Modification getMod (String site, String position, String deltaMass) {
		String key = deltaMass +"_" + site + "_" + position;
		if(map.size() == 0)
			try {
				initMap();
			} catch (IOException e) {
				e.printStackTrace();
			}
		Modification modification = map.get(key);
		
		if(modification == null) {
			System.err.println("There is no matched modification: " + key);
		}
		
		return modification;
	}
	
	public static Modification getMod (String key) {
		
		if(map.size() == 0)
			try {
				initMap();
			} catch (IOException e) {
				e.printStackTrace();
			}
		Modification modification = map.get(key);
		
		if(modification == null) {
			System.err.println("There is no matched modification: " + key);
		}
		
		return modification;
	}
	/**
	 * Read modification informations from the file which contains all unimod 
	 * @throws IOException
	 */
	public static void initMap() throws IOException {
		String currentPath = System.getProperty("user.dir");
		BufferedReader br = new BufferedReader(new FileReader(currentPath+"//res//unimod.xml")); // 다른 패키지에 있는 파일을 어떻게 읽는지?		
		String key;
		String line;
		String[] lineSplit;
		String title;
		String site;
		String position;
		String deltaM = "";
		String chemicalComposition = "";
		String temp;
		ArrayList<String> ar = new ArrayList<String>();

		
		while ((line=br.readLine()) != null) {
			if (line.contains("umod:mod ")) {
				title = line.split("=")[1].split(" ")[0].split("\"")[1];
				
				line=br.readLine();
				while(!line.contains("/umod:mod")) {
					line=br.readLine();
					if (line.contains("umod:specificity ")) {
						lineSplit =line.split("=");
						site = lineSplit[2].split("\"")[1];
						if (site.length()>1) {
							site = "X";
						}
						position = lineSplit[3].split("\"")[1];
						if (position.contains("N-term")) {
							position = "Nterm";
						}
						else if (position.contains("C-term")) {
							position = "Cterm";
						}
						else {
							position = "any";
						}
						ar.add(site + "_" + position);
					}
					if (line.contains("umod:delta ")) {
						lineSplit =line.split("=");
						deltaM = lineSplit[1].split(" ")[0].split("\"")[1];
						deltaM = String.valueOf(Math.round(Double.parseDouble(deltaM)*1000)/1000.0);
						if (line.contains("composition")) {
							chemicalComposition = lineSplit[3].split(">")[0];
						}
						else {
							line = br.readLine();
							temp = line.substring(line.indexOf("comp"));
							chemicalComposition = temp.split("=")[1].split(">")[0];
						}
						
					}

				}
				for (int i=0 ; i<ar.size();i++) {
					site = ar.get(i).split("_")[0];
					position = ar.get(i).split("_")[1];
					key = deltaM + "_" + ar.get(i);
					map.put(key, new Modification(title, Double.parseDouble(deltaM), site.charAt(0), position, chemicalComposition  ));
				}
				ar.clear();

				
			}

		}
		
		br.close();
	}
	
	private String name;
	private double deltaMass;
	private String chemicalComposition;
	private String position;	//	Nterm, Cterm, Side-chain
	private char site;
	

/**
 *  
 * @param name modification name 
 * @param deltaMass delta mass
 * @param site modification site
 * @param position modification position 
 * @param chemicalComposition chemical composition
 */
	public Modification(String name, double deltaMass, char site, String position, String chemicalComposition) {
		this.name = name;
		this.deltaMass = deltaMass;
		this.site = site;
		this.position = position;
		this.chemicalComposition = chemicalComposition;


	}
	/**
	 * Get modification name
	 * @return name
	 */
	public String getName() {
		return name;
	}


/**
 * Set modification name 
 * @param name modification name 
 */
	public void setName(String name) {
		this.name = name;
	}



	public double getDeltaMass() {
		return deltaMass;
	}

/**
 * Set delta mass 
 * @param deltaMass delta mass 
 */

	public void setDeltaMass(double deltaMass) {
		this.deltaMass = deltaMass;
	}

/**
 * Get chemical composition
 * @return chemical composition
 */

	public String getChemicalComposition() {
		return chemicalComposition;
	}


/**
 * Set chemical composition 
 * @param chemicalComposition chemical composition 
 */
	public void setChemicalComposition(String chemicalComposition) {
		this.chemicalComposition = chemicalComposition;
	}

/**
 * Get position 
 * @return position
 */

	public String getPosition() {
		return position;
	}



	public void setPosition(String position) {
		this.position = position;
	}


/**
 * Get modification site 
 * @return site
 */
	public char getSite() {
		return site;
	}

/**
 * Set modification site 
 * @param site
 */

	public void setSite(char site) {
		this.site = site;
	}


	

	
//	public String toString() {
//		String modString = "";
//		if( name.startsWith(aminoAcid+"") )
//			modString =  "" + (aminoAcid=='*'?positionConstraint:aminoAcid) + (deltaMass>0?"+"+deltaMass:deltaMass);
//		else
//			modString =  name;
//		if( positionConstraint.endsWith("term") )
//			modString += "/" + positionConstraint;
//		return modString + "(" + aminoAcid + position + ")";
//	}

}
