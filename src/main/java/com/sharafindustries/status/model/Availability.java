package com.sharafindustries.status.model;

public enum Availability
{
	Available, Away, Busy;
	
	public static boolean isValid(String availabilityString)
	{
		try
		{
			Availability.valueOf(availabilityString);
			return true;
		}
		catch (IllegalArgumentException e)
		{
			return false;
		}
	}

}
