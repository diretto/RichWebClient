package org.diretto.web.richwebclient.view.widgets.googlemap.client;

import org.diretto.web.richwebclient.view.base.client.MediaType;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.event.Event;
import com.google.gwt.maps.client.event.HasMouseEvent;
import com.google.gwt.maps.client.event.MouseEventCallback;
import org.diretto.web.richwebclient.view.widgets.googlemap.client.base.AbstractVGoogleMap;
import org.diretto.web.richwebclient.view.widgets.googlemap.client.markers.DraggableMarker;
import org.diretto.web.richwebclient.view.widgets.googlemap.client.markers.Marker;
import org.diretto.web.richwebclient.view.widgets.googlemap.server.UploadGoogleMap;

import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

/**
 * The client side component of the {@link UploadGoogleMap}.
 * 
 * @author Tobias Schlecht
 */
public final class VUploadGoogleMap extends AbstractVGoogleMap
{
	private boolean initialized = false;

	private DraggableMarker marker;

	/**
	 * Constructs a {@link VUploadGoogleMap}.
	 */
	public VUploadGoogleMap()
	{
		super();
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection applicationConnection)
	{
		super.updateFromUIDL(uidl, applicationConnection);

		if(!initialized)
		{
			marker = new DraggableMarker(map, map.getCenter().getLatitude(), map.getCenter().getLongitude(), MediaType.valueOf(uidl.getStringVariable("mediaType")));

			addEventListeners();

			initialized = true;
		}
		else
		{
			MediaType uidlMediaType = MediaType.valueOf(uidl.getStringVariable("mediaType"));

			if(marker.getMediaType() != uidlMediaType)
			{
				marker.changeMediaType(uidlMediaType);
			}

			if(marker.getPosition().getLatitude() != uidl.getDoubleVariable("markerLatitude") || marker.getPosition().getLongitude() != uidl.getDoubleVariable("markerLongitude"))
			{
				marker.setPosition(new LatLng(uidl.getDoubleVariable("markerLatitude"), uidl.getDoubleVariable("markerLongitude")));
			}
		}
	}

	/**
	 * Sends the variables to the server side component.
	 */
	private void updateVariables()
	{
		applicationConnection.updateVariable(paintableID, "markerLatitude", marker.getPosition().getLatitude(), false);
		applicationConnection.updateVariable(paintableID, "markerLongitude", marker.getPosition().getLongitude(), true);
	}

	/**
	 * Adds {@code EventListener}s to the {@link Marker} and the map.
	 */
	private void addEventListeners()
	{
		Event.addListener(marker, "position_changed", new MouseEventCallback()
		{
			@Override
			public void callback(HasMouseEvent event)
			{
				updateVariables();
			}
		});

		Event.addListener(map, "dblclick", new MouseEventCallback()
		{
			@Override
			public void callback(HasMouseEvent event)
			{
				marker.setPosition(event.getLatLng());

				updateVariables();
			}
		});

		Event.addListener(map, "rightclick", new MouseEventCallback()
		{
			@Override
			public void callback(HasMouseEvent event)
			{
				map.setCenter(marker.getPosition());
			}
		});
	}
}
