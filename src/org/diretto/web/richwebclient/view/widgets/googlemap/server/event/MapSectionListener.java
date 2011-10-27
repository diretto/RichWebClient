package org.diretto.web.richwebclient.view.widgets.googlemap.server.event;

import org.diretto.api.client.base.data.BoundingBox;

/**
 * This interface represents a {@link GoogleMapListener} for {@code GoogleMap}
 * events in respect of map section changes.
 * 
 * @author Tobias Schlecht
 */
public interface MapSectionListener extends GoogleMapListener
{
	/**
	 * Called when the map section of the corresponding {@code GoogleMap} has
	 * been changed.
	 * 
	 * @param boundingBox The {@code BoundingBox} of the new map section
	 */
	void onMapSectionChanged(BoundingBox boundingBox);
}
