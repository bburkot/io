package pl.edu.agh.io.pojo;

public enum DayType {
	SPECIAL('B'), // 'B' if this trip started on a holiday or any other special day
	DAY_BEFOR_SPECIAL('C'), // 'C' if the trip started on a day before a type-B day;
	OTHERWISE('A')	// 'A' otherwise (i.e. a normal day, workday or weekend).
	;
	
	private char c;
	private DayType(char c){
		this.c = c;
	}
	
	public static DayType fromChar(char call_type) {
		for (DayType dt : values())
			if (dt.c == call_type)
				return dt;
		throw new IllegalArgumentException();
	}
}
