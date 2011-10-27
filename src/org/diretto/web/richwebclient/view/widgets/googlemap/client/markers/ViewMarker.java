package org.diretto.web.richwebclient.view.widgets.googlemap.client.markers;

import org.diretto.web.richwebclient.view.base.client.MediaType;
import com.google.gwt.maps.client.HasMap;

/**
 * This class represents a <i>View</i> {@link Marker}.
 * 
 * @author Tobias Schlecht
 */
public class ViewMarker extends DocumentMarker
{
	private final String positionID;

	/**
	 * Constructs a {@link ViewMarker}.
	 * 
	 * @param positionID The corresponding {@code PositionID} ({@code String}
	 *        representation)
	 * @param map The corresponding map
	 * @param latitude The {@code Marker} latitude in degrees
	 * @param longitude The {@code Marker} longitude in degrees
	 * @param mediaType The {@code MediaType}
	 */
	public ViewMarker(String positionID, HasMap map, double latitude, double longitude, MediaType mediaType)
	{
		this(positionID, map, latitude, longitude, mediaType, Colored.GRAY, Action.REGULAR);
	}

	/**
	 * Constructs a {@link ViewMarker}.
	 * 
	 * @param positionID The corresponding {@code PositionID} ({@code String}
	 *        representation)
	 * @param map The corresponding map
	 * @param latitude The {@code Marker} latitude in degrees
	 * @param longitude The {@code Marker} longitude in degrees
	 * @param mediaType The {@code MediaType}
	 * @param colored The {@code Colored} type
	 * @param action The {@code Action} type
	 */
	public ViewMarker(String positionID, HasMap map, double latitude, double longitude, MediaType mediaType, Colored colored, Action action)
	{
		super(map, latitude, longitude, mediaType, colored, action);

		this.positionID = positionID;
	}

	/**
	 * Returns the corresponding {@code PositionID} ({@code String}
	 * representation).
	 * 
	 * @return The corresponding {@code PositionID}
	 */
	public String getPositionID()
	{
		return positionID;
	}
}
