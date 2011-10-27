package org.diretto.web.richwebclient.view.widgets.googlemap.client.base;

import com.google.gwt.maps.client.MapTypeId;

/**
 * This type represents a {@code MapType} of a {@code GoogleMap}.
 * 
 * @author Tobias Schlecht
 */
public enum MapType
{
	/**
	 * This type represents the {@code HYBRID} {@link MapType} of a
	 * {@code GoogleMap}.
	 */
	HYBRID("Hybrid"),

	/**
	 * This type represents the {@code ROADMAP} {@link MapType} of a
	 * {@code GoogleMap}.
	 */
	ROADMAP("Roadmap"),

	/**
	 * This type represents the {@code SATELLITE} {@link MapType} of a
	 * {@code GoogleMap}.
	 */
	SATELLITE("Satellite"),

	/**
	 * This type represents the {@code TERRAIN} {@link MapType} of a
	 * {@code GoogleMap}.
	 */
	TERRAIN("Terrain");

	private final String name;

	/**
	 * Constructs a {@link MapType}.
	 * 
	 * @param name The name
	 */
	private MapType(String name)
	{
		this.name = name;
	}

	/**
	 * Returns the Google Maps API value.
	 * 
	 * @return The Google Maps API value
	 */
	public String getAPIValue()
	{
		return getAPIValue(this);
	}

	/**
	 * Returns the name.
	 * 
	 * @return The name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Returns the corresponding {@link MapType} for the given Google Maps API
	 * value.
	 * 
	 * @param apiValue A Google Maps API value
	 * @return The corresponding {@code MapType}
	 */
	public static MapType getValue(String apiValue)
	{
		if(apiValue.equals(HYBRID.getAPIValue()))
		{
			return HYBRID;
		}
		else if(apiValue.equals(ROADMAP.getAPIValue()))
		{
			return ROADMAP;
		}
		else if(apiValue.equals(SATELLITE.getAPIValue()))
		{
			return SATELLITE;
		}
		else
		{
			return TERRAIN;
		}
	}

	/**
	 * Returns the corresponding Google Maps API value for the given
	 * {@link MapType}.
	 * 
	 * @param mapType A {@code MapType}
	 * @return The Google Maps API value
	 */
	public static String getAPIValue(MapType mapType)
	{
		if(mapType == HYBRID)
		{
			return new MapTypeId().getHybrid();
		}
		else if(mapType == ROADMAP)
		{
			return new MapTypeId().getRoadmap();
		}
		else if(mapType == SATELLITE)
		{
			return new MapTypeId().getSatellite();
		}
		else
		{
			return new MapTypeId().getTerrain();
		}
	}
}
