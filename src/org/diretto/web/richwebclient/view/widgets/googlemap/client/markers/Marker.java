package org.diretto.web.richwebclient.view.widgets.googlemap.client.markers;

/**
 * This interface represents a {@code Marker} of a {@code GoogleMap}.
 * 
 * @author Tobias Schlecht
 */
public interface Marker
{
	/**
	 * A {@code Colored} type represents the type how a {@link Marker} is
	 * colored.
	 */
	public enum Colored
	{
		COLORED, GRAY;
	}

	/**
	 * An {@code Action} type represents the status of a {@link Marker}.
	 */
	public enum Action
	{
		REGULAR, HOVERED, SELECTED;
	}
}
