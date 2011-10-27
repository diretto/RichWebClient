package org.diretto.web.richwebclient.view.widgets.googlemap.client.markers;

import org.diretto.web.richwebclient.view.base.client.MediaType;
import com.google.gwt.maps.client.HasMap;
import com.google.gwt.maps.client.event.HasMouseEvent;
import com.google.gwt.maps.client.event.MouseEventCallback;
import org.diretto.web.richwebclient.view.widgets.googlemap.client.markers.Marker.Action;
import org.diretto.web.richwebclient.view.widgets.googlemap.client.markers.Marker.Colored;

/**
 * This {@code abstract} class provides a default implementation and class
 * constraints for a {@code DocumentMarker}.
 * 
 * @author Tobias Schlecht
 */
abstract class DocumentMarker extends AbstractMarker
{
	protected MediaType mediaType;
	protected Colored colored;
	protected Action action;

	private boolean isDragging = false;

	/**
	 * Provides base implementation to construct a {@link DocumentMarker}.
	 * 
	 * @param map The corresponding map
	 * @param latitude The {@code Marker} latitude in degrees
	 * @param longitude The {@code Marker} longitude in degrees
	 * @param mediaType The {@code MediaType}
	 */
	protected DocumentMarker(HasMap map, double latitude, double longitude, MediaType mediaType)
	{
		this(map, latitude, longitude, mediaType, Colored.COLORED);
	}

	/**
	 * Provides base implementation to construct a {@link DocumentMarker}.
	 * 
	 * @param map The corresponding map
	 * @param latitude The {@code Marker} latitude in degrees
	 * @param longitude The {@code Marker} longitude in degrees
	 * @param mediaType The {@code MediaType}
	 * @param colored The {@code Colored} type
	 */
	protected DocumentMarker(HasMap map, double latitude, double longitude, MediaType mediaType, Colored colored)
	{
		this(map, latitude, longitude, mediaType, colored, Action.REGULAR);
	}

	/**
	 * Provides base implementation to construct a {@link DocumentMarker}.
	 * 
	 * @param map The corresponding map
	 * @param latitude The {@code Marker} latitude in degrees
	 * @param longitude The {@code Marker} longitude in degrees
	 * @param mediaType The {@code MediaType}
	 * @param colored The {@code Colored} type
	 * @param action The {@code Action} type
	 */
	protected DocumentMarker(HasMap map, double latitude, double longitude, MediaType mediaType, Colored colored, Action action)
	{
		super(map, latitude, longitude);

		this.mediaType = mediaType;
		this.colored = colored;
		this.action = action;

		setIcon(IconFactory.INSTANCE.getMarkerIcon(mediaType, colored, action));

		addEventListeners();
	}

	/**
	 * Returns the {@link MediaType}.
	 * 
	 * @return The {@code MediaType}
	 */
	public MediaType getMediaType()
	{
		return mediaType;
	}

	/**
	 * Returns the {@link Colored} type.
	 * 
	 * @return The {@code Colored} type
	 */
	public Colored getColored()
	{
		return colored;
	}

	/**
	 * Returns the {@link Action} type.
	 * 
	 * @return The {@code Action} type
	 */
	public Action getAction()
	{
		return action;
	}

	/**
	 * Changes the {@link MediaType}.
	 * 
	 * @param mediaType The new {@code MediaType}
	 */
	public void changeMediaType(MediaType mediaType)
	{
		this.mediaType = mediaType;

		setIcon(IconFactory.INSTANCE.getMarkerIcon(mediaType, colored, action));
	}

	/**
	 * Selects this {@link Marker}.
	 */
	public void select()
	{
		action = Action.SELECTED;

		setIcon(IconFactory.INSTANCE.getMarkerIcon(mediaType, colored, Action.SELECTED));
	}

	/**
	 * Deselects this {@link Marker}.
	 */
	public void deselect()
	{
		action = Action.REGULAR;

		setIcon(IconFactory.INSTANCE.getMarkerIcon(mediaType, colored, Action.REGULAR));
	}

	/**
	 * Adds {@code EventListener}s to this {@link Marker}.
	 */
	private void addEventListeners()
	{
		com.google.gwt.maps.client.event.Event.addListener(this, "mousedown", new MouseEventCallback()
		{
			@Override
			public void callback(HasMouseEvent event)
			{
				action = Action.SELECTED;

				setIcon(IconFactory.INSTANCE.getMarkerIcon(mediaType, colored, Action.SELECTED));
			}
		});

		com.google.gwt.maps.client.event.Event.addListener(this, "dragstart", new MouseEventCallback()
		{
			@Override
			public void callback(HasMouseEvent event)
			{
				isDragging = true;
			}
		});

		com.google.gwt.maps.client.event.Event.addListener(this, "dragend", new MouseEventCallback()
		{
			@Override
			public void callback(HasMouseEvent event)
			{
				isDragging = false;

				setIcon(IconFactory.INSTANCE.getMarkerIcon(mediaType, colored, Action.HOVERED));
			}
		});

		com.google.gwt.maps.client.event.Event.addListener(this, "mouseover", new MouseEventCallback()
		{
			@Override
			public void callback(HasMouseEvent event)
			{
				if(!isDragging)
				{
					setIcon(IconFactory.INSTANCE.getMarkerIcon(mediaType, colored, Action.HOVERED));
				}
			}
		});

		com.google.gwt.maps.client.event.Event.addListener(this, "mouseout", new MouseEventCallback()
		{
			@Override
			public void callback(HasMouseEvent event)
			{
				setIcon(IconFactory.INSTANCE.getMarkerIcon(mediaType, colored, action));
			}
		});
	}
}
