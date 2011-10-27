package org.diretto.web.richwebclient.view.widgets.googlemap.client.markers;

import org.diretto.web.richwebclient.view.base.client.MediaType;
import com.google.gwt.maps.client.HasMap;

/**
 * This class represents a <i>draggable</i> {@link Marker}.
 * 
 * @author Tobias Schlecht
 */
public class DraggableMarker extends DocumentMarker
{
	/**
	 * Constructs a {@link DraggableMarker}.
	 * 
	 * @param map The corresponding map
	 * @param latitude The {@code Marker} latitude in degrees
	 * @param longitude The {@code Marker} longitude in degrees
	 * @param mediaType The {@code MediaType}
	 */
	public DraggableMarker(HasMap map, double latitude, double longitude, MediaType mediaType)
	{
		super(map, latitude, longitude, mediaType, Colored.COLORED, Action.SELECTED);

		setDraggable(true);
	}
}
