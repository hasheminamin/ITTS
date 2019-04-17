package enums;

public enum POS
{	
	N,
	V,
	ADJ,
	ADV,
	ADR,
	CONJ,
	NUM,
	POSTP,
	PR,
	PSUS,          
	SUBR,          
	PREP,          
	PUNC,          
	LATIN,          
	ARAB,
	UNKNOWN;				
	
	public static POS fromString(String posTag){
		if (posTag != null)
			for (POS pos : POS.values()) 
				if (posTag.equalsIgnoreCase(pos.name())) 
					return pos;
       return UNKNOWN;
	}
}
