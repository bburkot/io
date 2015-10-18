package pl.edu.agh.io.pojo;

public enum DayType {
	SPECIAL, // 'B' if this trip started on a holiday or any other special day
	DAY_BEFOR_SPECIAL, // 'C' if the trip started on a day before a type-B day;
	OTHERWISE	// 'A' otherwise (i.e. a normal day, workday or weekend).
	;
}
