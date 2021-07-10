package Util;

import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class AreaUtil 
{
	public static int Coord(DecorationArea area, int x)
	{
		return area.getCenterX() - DecorationArea.DECORATION_RADIUS + x;
	}
}
