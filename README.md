# ProteoLib
Proteomic data analysis library 

The library supports the analysis of mass spectrometry (MS)-based proteomic data, which provides not only the number of features of spectra, PSM, and peptides among published proteomic libraries, but also proteomic data preprocessing functions. From MS spectra stored in “mgf” or “mzZXML” files, it can extract both MS1 and MS2 features users might find useful, such as extracted ion chromatogram (XIC), total ion count (TIC), peak information and so on. Moreover, it also provides pre-preprocessing procedures of peak filtering. For the PSMs, various features (annotated peak information, AA compositions in the peptide sequence etc) of PSMs can be extracted but also feature statistics can be provided, in order to facilitate data analysis and post-processing such as PSM rescoring. To identify high confidence PSMs and peptides, both global and local false discovery rate (FDR) can be applied with user specified parameter settings (score types, peptide length etc) based on target-decoy (TD) approach.

*Details of the library structure and functions can be found in the javadoc file.  
