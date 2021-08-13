package examples;

import java.io.IOException;

import exp.data.Spectra;
import exp.data.Spectrum;

public class ReadSpectrum {

	public static void main(String[] args) throws IOException {
		String spectrumFileName = "";
		
		Spectra spectra = new Spectra();
		spectra.readFiles(spectrumFileName);

		// access
		int size = spectra.getSize();
		for(int i=0; i<size; i++) {
			Spectrum spectrum = spectra.getSpectrumByIndex(i);
			
		}
	}
}
