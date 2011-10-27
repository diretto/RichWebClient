package org.diretto.web.richwebclient.view.widgets.googlemap.client.markers;

import java.util.Map;
import java.util.HashMap;

import org.diretto.web.richwebclient.view.base.client.MediaType;
import com.google.gwt.maps.client.base.Point;
import com.google.gwt.maps.client.base.Size;
import com.google.gwt.maps.client.overlay.MarkerImage;
import org.diretto.web.richwebclient.view.util.client.ResourceUtils;
import org.diretto.web.richwebclient.view.widgets.googlemap.client.markers.Marker.Action;
import org.diretto.web.richwebclient.view.widgets.googlemap.client.markers.Marker.Colored;

/**
 * The {@code IconFactory} is responsible for creating and managing the
 * {@link Marker} icons. <br/><br/>
 * 
 * <i>Annotation:</i> <u>Singleton Pattern</u> (Enum Singleton)
 * 
 * @author Tobias Schlecht
 */
public enum IconFactory
{
	INSTANCE;

	private Map<String, MarkerImage> icons = new HashMap<String, MarkerImage>();

	private final String markersPath;
	private final int iconWidth;
	private final int iconHeight;

	private String applicationURL = null;

	/**
	 * Constructs the sole instance of the {@link IconFactory}. <br/><br/>
	 * 
	 * <i>Annotation:</i> <u>Singleton Pattern</u> (Enum Singleton)
	 */
	IconFactory()
	{
		markersPath = ResourceUtils.GOOGLE_MAP_MARKERS_PATH;
		iconWidth = ResourceUtils.GOOGLE_MAP_MARKER_ICON_WIDTH;
		iconHeight = ResourceUtils.GOOGLE_MAP_MARKER_ICON_HEIGHT;
	}

	/**
	 * Returns the corresponding application {@code URL}.
	 * 
	 * @return The corresponding application {@code URL}
	 */
	public String getApplicationURL()
	{
		return applicationURL;
	}

	/**
	 * Sets the corresponding application {@code URL}.
	 * 
	 * @param applicationURL The corresponding application {@code URL}
	 */
	public void setApplicationURL(String applicationURL)
	{
		this.applicationURL = applicationURL;
	}

	/**
	 * Returns the requested {@link Marker} icon.
	 * 
	 * @param mediaType The {@code MediaType}
	 * @return The {@code Marker} icon
	 */
	MarkerImage getMarkerIcon(MediaType mediaType)
	{
		return getMarkerIcon(mediaType, Colored.COLORED);
	}

	/**
	 * Returns the requested {@link Marker} icon.
	 * 
	 * @param mediaType The {@code MediaType}
	 * @param colored The {@code Colored} type
	 * @return The {@code Marker} icon
	 */
	MarkerImage getMarkerIcon(MediaType mediaType, Colored colored)
	{
		return getMarkerIcon(mediaType, colored, Action.REGULAR);
	}

	/**
	 * Returns the requested {@link Marker} icon.
	 * 
	 * @param mediaType The {@code MediaType}
	 * @param colored The {@code Colored} type
	 * @param action The {@code Action} type
	 * @return The {@code Marker} icon
	 */
	MarkerImage getMarkerIcon(MediaType mediaType, Colored colored, Action action)
	{
		String iconName = mediaType.toString().toLowerCase() + "-" + colored.toString().toLowerCase() + "-" + action.toString().toLowerCase();

		return getMarkerIcon(iconName, iconWidth, iconHeight, iconWidth / 2, iconHeight);
	}

	/**
	 * Returns the requested {@link Marker} icon.
	 * 
	 * @param iconName The icon name
	 * @param width The icon width
	 * @param height The icon height
	 * @param anchorX The x-coordinate value of the anchor
	 * @param anchorY The y-coordinate value of the anchor
	 * @return The {@code Marker} icon
	 */
	private MarkerImage getMarkerIcon(String iconName, int width, int height, int anchorX, int anchorY)
	{
		if(icons.containsKey(iconName))
		{
			return icons.get(iconName);
		}
		else
		{
			MarkerImage markerImage = new MarkerImage.Builder(applicationURL + markersPath + iconName + ".png").setSize(new Size(width, height)).setAnchor(new Point(anchorX, anchorY)).build();

			icons.put(iconName, markerImage);

			return markerImage;
		}
	}
}
