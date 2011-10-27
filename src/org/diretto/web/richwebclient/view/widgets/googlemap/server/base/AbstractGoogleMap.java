package org.diretto.web.richwebclient.view.widgets.googlemap.server.base;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.diretto.api.client.base.data.BoundingBox;
import org.diretto.api.client.base.data.TopographicPoint;
import org.diretto.web.richwebclient.view.widgets.googlemap.client.base.MapType;
import org.diretto.web.richwebclient.view.widgets.googlemap.server.event.GoogleMapListener;
import org.diretto.web.richwebclient.view.widgets.googlemap.server.event.MapSectionListener;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;

/**
 * This {@code abstract} class provides a default implementation and class
 * constraints for {@code GoogleMap} server side components.
 * 
 * @author Tobias Schlecht
 */
public abstract class AbstractGoogleMap extends AbstractComponent
{
	private static final long serialVersionUID = -9063410826820373961L;

	private final List<MapSectionListener> mapSectionListeners = new Vector<MapSectionListener>();

	protected int zoomLevel;
	protected double centerLatitude;
	protected double centerLongitude;
	protected MapType mapType;

	protected boolean scrollWheelZoomingEnabled;
	protected boolean draggingEnabled;

	protected double lowerLeftLatitude = 0.0d;
	protected double lowerLeftLongitude = 0.0d;
	protected double upperRightLatitude = 0.0d;
	protected double upperRightLongitude = 0.0d;

	/**
	 * Provides base implementation to construct a {@code GoogleMap}.
	 * 
	 * @param zoomLevel The initial zoom level
	 * @param centerLatitude The initial center latitude in degrees
	 * @param centerLongitude The initial center longitude in degrees
	 * @param mapType The initial {@code MapType}
	 */
	public AbstractGoogleMap(int zoomLevel, double centerLatitude, double centerLongitude, MapType mapType)
	{
		this(zoomLevel, centerLatitude, centerLongitude, mapType, true, true);
	}

	/**
	 * Provides base implementation to construct a {@code GoogleMap}.
	 * 
	 * @param zoomLevel The initial zoom level
	 * @param centerLatitude The initial center latitude in degrees
	 * @param centerLongitude The initial center longitude in degrees
	 * @param mapType The initial {@code MapType}
	 * @param scrollWheelZoomingEnabled {@code true} if scroll wheel zooming is
	 *        enabled; otherwise {@code false}
	 * @param draggingEnabled {@code true} if dragging is enabled; otherwise
	 *        {@code false}
	 */
	public AbstractGoogleMap(int zoomLevel, double centerLatitude, double centerLongitude, MapType mapType, boolean scrollWheelZoomingEnabled, boolean draggingEnabled)
	{
		this.zoomLevel = zoomLevel;
		this.centerLatitude = centerLatitude;
		this.centerLongitude = centerLongitude;
		this.mapType = mapType;

		this.scrollWheelZoomingEnabled = scrollWheelZoomingEnabled;
		this.draggingEnabled = draggingEnabled;
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException
	{
		super.paintContent(target);

		target.addVariable(this, "zoomLevel", zoomLevel);
		target.addVariable(this, "centerLatitude", centerLatitude);
		target.addVariable(this, "centerLongitude", centerLongitude);
		target.addVariable(this, "mapType", mapType.toString());

		target.addAttribute("applicationURL", getApplication().getURL().toString());
		target.addAttribute("scrollWheelZoomingEnabled", scrollWheelZoomingEnabled);
		target.addAttribute("draggingEnabled", draggingEnabled);
	}

	@Override
	public void changeVariables(Object source, Map<String, Object> variables)
	{
		super.changeVariables(source, variables);

		boolean mapSectionChanged = false;

		if(variables.containsKey("zoomLevel"))
		{
			zoomLevel = (Integer) variables.get("zoomLevel");
		}

		if(variables.containsKey("centerLatitude"))
		{
			centerLatitude = (Double) variables.get("centerLatitude");
		}

		if(variables.containsKey("centerLongitude"))
		{
			centerLongitude = (Double) variables.get("centerLongitude");
		}

		if(variables.containsKey("mapType"))
		{
			mapType = MapType.valueOf((String) variables.get("mapType"));
		}

		if(variables.containsKey("lowerLeftLatitude"))
		{
			double lowerLeftLatitude = (Double) variables.get("lowerLeftLatitude");

			if(this.lowerLeftLatitude != lowerLeftLatitude)
			{
				this.lowerLeftLatitude = lowerLeftLatitude;

				mapSectionChanged = true;
			}
		}

		if(variables.containsKey("lowerLeftLongitude"))
		{
			double lowerLeftLongitude = (Double) variables.get("lowerLeftLongitude");

			if(this.lowerLeftLongitude != lowerLeftLongitude)
			{
				this.lowerLeftLongitude = lowerLeftLongitude;

				mapSectionChanged = true;
			}
		}

		if(variables.containsKey("upperRightLatitude"))
		{
			double upperRightLatitude = (Double) variables.get("upperRightLatitude");

			if(this.upperRightLatitude != upperRightLatitude)
			{
				this.upperRightLatitude = upperRightLatitude;

				mapSectionChanged = true;
			}
		}

		if(variables.containsKey("upperRightLongitude"))
		{
			double upperRightLongitude = (Double) variables.get("upperRightLongitude");

			if(this.upperRightLongitude != upperRightLongitude)
			{
				this.upperRightLongitude = upperRightLongitude;

				mapSectionChanged = true;
			}
		}

		if(mapSectionChanged)
		{
			final BoundingBox boundingBox = new BoundingBox(lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude);

			for(final MapSectionListener mapSectionListener : mapSectionListeners)
			{
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						mapSectionListener.onMapSectionChanged(boundingBox);
					}
				}).start();
			}
		}
	}

	/**
	 * Returns the zoom level.
	 * 
	 * @return The zoom level
	 */
	public int getZoomLevel()
	{
		return zoomLevel;
	}

	/**
	 * Sets the zoom level.
	 * 
	 * @param zoomLevel The zoom level
	 */
	public void setZoomLevel(int zoomLevel)
	{
		this.zoomLevel = zoomLevel;

		requestRepaint();
	}

	/**
	 * Increases the zoom level.
	 */
	public void zoomIn()
	{
		zoomLevel++;

		requestRepaint();
	}

	/**
	 * Reduces the zoom level.
	 */
	public void zoomOut()
	{
		zoomLevel--;

		requestRepaint();
	}

	/**
	 * Returns the map focus.
	 * 
	 * @return The map focus
	 */
	public TopographicPoint getMapFocus()
	{
		return new TopographicPoint(centerLatitude, centerLongitude);
	}

	/**
	 * Sets the map focus to the given coordinates.
	 * 
	 * @param centerLatitude The center latitude in degrees
	 * @param centerLongitude The center longitude in degrees
	 */
	public void setMapFocus(double centerLatitude, double centerLongitude)
	{
		this.centerLatitude = centerLatitude;
		this.centerLongitude = centerLongitude;

		requestRepaint();
	}

	/**
	 * Returns the {@link MapType}.
	 * 
	 * @return The {@code MapType}
	 */
	public MapType getMapType()
	{
		return mapType;
	}

	/**
	 * Sets the {@link MapType}.
	 * 
	 * @param mapType The {@code MapType}
	 */
	public void setMapType(MapType mapType)
	{
		this.mapType = mapType;

		requestRepaint();
	}

	/**
	 * Returns the map section.
	 * 
	 * @return The map section
	 */
	public BoundingBox getMapSection()
	{
		return new BoundingBox(lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude);
	}

	/**
	 * Adds the given {@link MapSectionListener}.
	 * 
	 * @param mapSectionListener A {@code MapSectionListener}
	 */
	public synchronized void addMapSectionListener(MapSectionListener mapSectionListener)
	{
		if(!mapSectionListeners.contains(mapSectionListener))
		{
			mapSectionListeners.add(mapSectionListener);
		}
	}

	/**
	 * Removes the given {@link GoogleMapListener}.
	 * 
	 * @param googleMapListener A {@code GoogleMapListener}
	 */
	public synchronized void removeListener(GoogleMapListener googleMapListener)
	{
		if(googleMapListener instanceof MapSectionListener)
		{
			mapSectionListeners.remove(googleMapListener);
		}
	}
}
