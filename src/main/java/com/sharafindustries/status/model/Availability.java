package com.sharafindustries.status.model;

public enum Availability
{
	Free, Away, Busy;
	
	public static boolean isValid(String availabilityString)
	{
		Availability[] availabilities = Availability.values();
		for (Availability availability : availabilities)
			if (Availability.valueOf(availabilityString) == availability)
				return true;
		return false;
	}

}
