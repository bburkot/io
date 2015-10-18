package pl.edu.agh.io.pojo;

public enum CallType {
	CENTRAL('A'),
	STREET('B'),
	OTHER('C')
	;
	
	private char c;
	private CallType(char c){
		this.c = c;
	}
	
	public static CallType fromChar(char call_type) {
		for (CallType ct : values())
			if (ct.c == call_type)
				return ct;
		throw new IllegalArgumentException();
	}
}
