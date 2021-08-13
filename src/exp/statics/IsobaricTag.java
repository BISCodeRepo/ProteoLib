package exp.statics;

public class IsobaricTag {

	public static final double[][] MASS_TABLE =  {
			//index 0: reagent mass, others: reporters are sorted
		    {144.102063D, 114.11123D, 115.10826D, 116.11162D, 117.11497D }, //iTRAQ 4plex 
		    {304.205363D, 113.107873D, 114.111228D, 115.108263D, 116.111618D, 117.114973D, 118.112008D, 119.115363D, 121.122072D }, //iTRAQ 8plex 
		    {225.155833D, 126.127725D, 127.131079D }, // TMT 2plex 
		    {229.162932D, 126.127725D, 127.12476D, 128.134433D, 129.131468D, 130.141141D, 131.138176D }, // TMT 6plex 
			{229.162932D, 126.127725D, 127.12476D, 127.131079D, 128.128114D, 128.134433D, 129.131468D, 129.137787D, 130.134822D, 130.141141D, 131.138176D}  // TMT 10plex
		};
}
