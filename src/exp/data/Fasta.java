package exp.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
/**
 * Manage database file and make decoys 
 *
 */
public class Fasta {

	public class FastaEntry {
		private String header = null;
		private String sequence = null;
		
		public FastaEntry (String header, String sequence) {
			this.header = header;
			this.sequence = sequence;
		}
		
		public String getHeader () {
			return this.header;
		}
		
		public String getSequence () {
			return this.sequence;
		}
		
		public void setHeader (String header) {
			this.header = header;
		}
		
		public void setSeqeucne (String sequence) {
			this.sequence = sequence;
		}
	}

	
	private ArrayList<FastaEntry> entries = null;
	private Hashtable<String, FastaEntry> headerMapper = null;
	
	/**
	 * Read fasta database file and store them to list 
	 * @param fileName file name
	 */
	public Fasta (String fileName) {
		readFasta(fileName);
	}
	
	public Fasta () {
		this.entries = new ArrayList<FastaEntry>();
		this.headerMapper = new Hashtable<String, FastaEntry>();
	}
	
	private void readFasta (String fileName) {
		try {
			BufferedReader BR = new BufferedReader(new FileReader(fileName));
			String line = null;
			
			entries = new ArrayList<FastaEntry>();
			this.headerMapper = new Hashtable<String, FastaEntry>();
			
			StringBuilder sequence = new StringBuilder();
			String header = null;
			while((line = BR.readLine()) != null) {
				
				if(line.startsWith(">")) {
					if(header != null) {
						FastaEntry fastaEntry = new FastaEntry(header, sequence.toString());
						this.entries.add(fastaEntry);
						this.headerMapper.put(fastaEntry.getHeader(), fastaEntry);
					}
					sequence.setLength(0);
					header = line;
				}else if(line.length() != 0) {
					sequence.append(line);
				}
			}
			
			if(header != null) {
				FastaEntry fastaEntry = new FastaEntry(header, sequence.toString());
				this.entries.add(fastaEntry);
				this.headerMapper.put(fastaEntry.getHeader(), fastaEntry);
			}
			
			BR.close();
		} catch (IOException ioe) {
			
		}
	}
	/**
	 * Get the number of entries in the database file
	 * @param null
	 * @return the number of entries
	 */
	public int sizeOfEntries () {
		return this.entries.size();
	}
	/**
	 * Get entry based on index number
	 * @param index index number 
	 * @return FastaEntry object
	 */
	public FastaEntry getFastaEntryByIndex (int index) {
		return this.entries.get(index);
	}

	/**
	 * Get entry based on header
	 * The header should be contain ">" annotation. <br>
	 * 
	 * @param header
	 * @return entry object 
	 */
	public FastaEntry getFastaEntryByHeader (String header) {
		return this.headerMapper.get(header);
	}
	/**
	 * Make decoy sequences 
	 * @param reverseMark prefix used for distinguish targets and decoys 
	 *
	 */
	public void makeReverseSequence (String reverseMark) {
		int sizeOfEntries = this.sizeOfEntries();
		for(int i=0; i<sizeOfEntries; i++) {
			FastaEntry reverseEntry = new FastaEntry(">"+reverseMark+this.entries.get(i).header.substring(1), new StringBuilder(this.entries.get(i).getSequence()).reverse().toString());
			this.entries.add(reverseEntry);
		}
	}
	/**
	 * Remove one entry by given index
	 * @param index index number
	 */
	public void removeEntry (int index) {
		if(index >= this.sizeOfEntries()) return;
		this.entries.remove(index);
	}
	
	/**
	 * Add one entry
	 * Header format: ">HEADER".<br>
	 * If header does not start with '>', it automatically adds '>' in front of given header.
	 * 
	 * @param header
	 * @param sequence
	 */
	public void addEntry (String header, String sequence) {
		if(!header.startsWith(">")) header = ">" + header;
		FastaEntry fastaEntry = new FastaEntry(header, sequence);
		this.entries.add(fastaEntry);
		this.headerMapper.put(fastaEntry.getHeader(), fastaEntry);
	}
	/**
	 * Get list of entries 
	 * @return list of entries 
	 */
	public ArrayList<FastaEntry> getFastaEntries () {
		return this.entries;
	}
	
}
