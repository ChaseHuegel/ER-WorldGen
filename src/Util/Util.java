package Util;

public class Util 
{
	public static float 	clamp(float val, float min, float max) { return Math.max(min, Math.min(max, val)); }
	public static double 	clamp(double val, double min, double max) { return Math.max(min, Math.min(max, val)); }
	public static Integer 	clamp(Integer val, Integer min, Integer max) { return Math.max(min, Math.min(max, val)); }
	public static int 		clamp(int val, int min, int max) { return Math.max(min, Math.min(max, val)); }
	
	public static float 	mapToRange(float val, float oldMin, float oldMax, float newMin, float newMax) { return (val - oldMin)/(oldMax - oldMin) * (newMax - newMin) + newMin; }
	public static double	mapToRange(double val, double oldMin, double oldMax, double newMin, double newMax) { return (val - oldMin)/(oldMax - oldMin) * (newMax - newMin) + newMin; }
	public static int 		mapToRange(int val, int oldMin, int oldMax, int newMin, int newMax) { return (int)( (val - oldMin)/(oldMax - oldMin) * (newMax - newMin) + newMin ); }
	
	public static String removeExtention(String file) 
	{
	    final int lastPeriodPos = file.lastIndexOf('.');
	    if (lastPeriodPos <= 0)
	    {
	        // No period after first character - return name as it was passed in
	        return file;
	    }
	    else
	    {
	        // Remove the last period and everything after it
	    	return file.substring(0, lastPeriodPos);
	    }
	}
	
	public static String getSubFolder(String file) 
	{
	    int lastPos = file.lastIndexOf('/');
	    if (lastPos <= 0)
	    {
	        return file;
	    }
	    else
	    {
	    	file = file.substring(0, lastPos);
	    	lastPos = file.lastIndexOf('/');
	    	
	    	if (lastPos <= 0) { return file; }
	    	else return file.substring(lastPos, file.length());
	    }
	}
}
