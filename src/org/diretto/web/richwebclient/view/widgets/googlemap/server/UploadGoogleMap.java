package org.diretto.web.richwebclient.view.widgets.googlemap.server;

import java.util.Map;

import org.diretto.api.client.base.data.TopographicPoint;
import org.diretto.web.richwebclient.view.base.client.MediaType;
import org.diretto.web.richwebclient.view.widgets.googlemap.client.VUploadGoogleMap;
import org.diretto.web.richwebclient.view.widgets.googlemap.client.base.MapType;
import org.diretto.web.richwebclient.view.widgets.googlemap.client.markers.Marker;
import org.diretto.web.richwebclient.view.widgets.googlemap.server.base.AbstractGoogleMap;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;

/**
 * The server side component of the {@code UploadGoogleMap}.
 * 
 * @author Tobias Schlecht
 */
@ClientWidget(VUploadGoogleMap.class)
public final class UploadGoogleMap extends AbstractGoogleMap
{
	private static final long serialVersionUID = -7333138670233602219L;

	private MediaType mediaType;
	private double markerLatitude;
	private double markerLongitude;

	/**
	 * Constructs an {@link UploadGoogleMap}.
	 * 
	 * @param mediaType The initial {@code MediaType} of the {@code Marker}
	 * @param zoomLevel The initial zoom level
	 * @param mapType The initial {@code MapType}
	 */
	public UploadGoogleMap(MediaType mediaType, int zoomLevel, MapType mapType)
	{
		this(mediaType, zoomLevel, 0.0d, 0.0d, mapType);
	}

	/**
	 * Constructs an {@link UploadGoogleMap}.
	 * 
	 * @param mediaType The initial {@code MediaType} of the {@code Marker}
	 * @param zoomLevel The initial zoom level
	 * @param markerLatitude The initial {@code Marker} latitude in degrees
	 * @param markerLongitude The initial {@code Marker} longitude in degrees
	 * @param mapType The initial {@code MapType}
	 */
	public UploadGoogleMap(MediaType mediaType, int zoomLevel, double markerLatitude, double markerLongitude, MapType mapType)
	{
		this(mediaType, zoomLevel, markerLatitude, markerLongitude, mapType, true);
	}

	/**
	 * Constructs an {@link UploadGoogleMap}.
	 * 
	 * @param mediaType The initial {@code MediaType} of the {@code Marker}
	 * @param zoomLevel The initial zoom level
	 * @param markerLatitude The initial {@code Marker} latitude in degrees
	 * @param markerLongitude The initial {@code Marker} longitude in degrees
	 * @param mapType The initial {@code MapType}
	 * @param scrollWheelZoomingEnabled {@code true} if scroll wheel zooming is
	 *        enabled; otherwise {@code false}
	 */
	public UploadGoogleMap(MediaType mediaType, int zoomLevel, double markerLatitude, double markerLongitude, MapType mapType, boolean scrollWheelZoomingEnabled)
	{
		super(zoomLevel, markerLatitude, markerLongitude, mapType, scrollWheelZoomingEnabled, true);

		this.mediaType = mediaType;
		this.markerLatitude = markerLatitude;
		this.markerLongitude = markerLongitude;
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException
	{
		super.paintContent(target);

		target.addVariable(this, "mediaType", mediaType.toString());
		target.addVariable(this, "markerLatitude", markerLatitude);
		target.addVariable(this, "markerLongitude", markerLongitude);
	}

	@Override
	public void changeVariables(Object source, Map<String, Object> variables)
	{
		super.changeVariables(source, variables);

		if(variables.containsKey("markerLatitude"))
		{
			markerLatitude = (Double) variables.get("markerLatitude");
		}

		if(variables.containsKey("markerLongitude"))
		{
			markerLongitude = (Double) variables.get("markerLongitude");
		}
	}

	/**
	 * Returns the position of the {@link Marker}.
	 * 
	 * @return The position of the {@code Marker}
	 */
	public TopographicPoint getMarkerPosition()
	{
		return new TopographicPoint(markerLatitude, markerLongitude);
	}

	/**
	 * Sets the {@link Marker} position as well as the center of the map.
	 * 
	 * @param markerLatitude The {@code Marker} latitude in degrees
	 * @param markerLongitude The {@code Marker} longitude in degrees
	 */
	public void setMarkerPosition(double markerLatitude, double markerLongitude)
	{
		centerLatitude = markerLatitude;
		centerLongitude = markerLongitude;

		this.markerLatitude = markerLatitude;
		this.markerLongitude = markerLongitude;

		requestRepaint();
	}

	/**
	 * Returns the {@link MediaType} of the {@link Marker}.
	 * 
	 * @return The {@code MediaType} of the {@code Marker}
	 */
	public MediaType getMediaType()
	{
		return mediaType;
	}

	/**
	 * Sets the {@link MediaType} of the {@link Marker}.
	 * 
	 * @param mediaType The {@code MediaType} of the {@code Marker}
	 */
	public void setMediaType(MediaType mediaType)
	{
		this.mediaType = mediaType;

		requestRepaint();
	}
}
