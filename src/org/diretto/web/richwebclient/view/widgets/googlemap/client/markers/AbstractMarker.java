package org.diretto.web.richwebclient.view.widgets.googlemap.client.markers;

import com.google.gwt.maps.client.HasMap;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.overlay.impl.MarkerImpl;

/**
 * This {@code abstract} class provides a default implementation and class
 * constraints for a {@link Marker}.
 * 
 * @author Tobias Schlecht
 */
abstract class AbstractMarker extends com.google.gwt.maps.client.overlay.Marker implements Marker
{
	/**
	 * Provides base implementation to construct a {@link Marker}.
	 * 
	 * @param map The corresponding map
	 * @param latitude The {@code Marker} latitude in degrees
	 * @param longitude The {@code Marker} longitude in degrees
	 */
	protected AbstractMarker(HasMap map, double latitude, double longitude)
	{
		super();

		setPosition(new LatLng(latitude, longitude));
		setMap(map);
	}

	@Override
	public void setMap(HasMap map)
	{
		if(map != null)
		{
			MarkerImpl.impl.setMap(super.getJso(), map.getJso());
		}
		else
		{
			MarkerImpl.impl.setMap(super.getJso(), null);
		}
	}
}
