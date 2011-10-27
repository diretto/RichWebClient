package org.diretto.web.richwebclient.view.widgets.googlemap.client.base;

import com.google.gwt.maps.client.HasMap;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeControlOptions;
import com.google.gwt.maps.client.MapTypeControlStyle;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.NavigationControlOptions;
import com.google.gwt.maps.client.NavigationControlStyle;
import com.google.gwt.maps.client.base.HasLatLng;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.event.Event;
import com.google.gwt.maps.client.event.HasMouseEvent;
import com.google.gwt.maps.client.event.MouseEventCallback;
import com.google.gwt.maps.client.impl.ControlPositionImpl;
import com.google.gwt.maps.client.impl.MapTypeControlStyleImpl;
import com.google.gwt.maps.client.impl.NavigationControlStyleImpl;
import org.diretto.web.richwebclient.view.widgets.googlemap.client.markers.IconFactory;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

/**
 * This {@code abstract} class provides a default implementation and class
 * constraints for {@code GoogleMap} client side components.
 * 
 * @author Tobias Schlecht
 */
public abstract class AbstractVGoogleMap extends Composite implements Paintable
{
	protected SimplePanel wrapperPanel = null;
	protected MapWidget mapWidget = null;
	protected HasMap map = null;
	protected MapOptions mapOptions = null;

	protected static String paintableID;
	protected static ApplicationConnection applicationConnection;

	private boolean zoomChanged = false;
	private boolean dragging = false;

	private HasLatLng center = null;
	private int zoomLevel;
	private double lowerLeftLongitude;
	private double upperRightLongitude;

	private int absoluteLeft = -1;
	private int absoluteRight;
	private int absoluteTop;
	private int absoluteBottom;

	/**
	 * Provides base implementation to construct a {@code VGoogleMap}.
	 */
	public AbstractVGoogleMap()
	{
		wrapperPanel = new SimplePanel();

		initWidget(wrapperPanel);
	}

	/**
	 * Initializes the {@link MapWidget}.
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
	private void initMap(int zoomLevel, double centerLatitude, double centerLongitude, MapType mapType, boolean scrollWheelZoomingEnabled, boolean draggingEnabled)
	{
		mapOptions = new MapOptions();

		mapOptions.setZoom(zoomLevel);
		mapOptions.setCenter(new LatLng(centerLatitude, centerLongitude));
		mapOptions.setMapTypeId(mapType.getAPIValue());
		mapOptions.setScrollwheel(scrollWheelZoomingEnabled);
		mapOptions.setDraggable(draggingEnabled);

		mapOptions.setDisableDoubleClickZoom(true);
		mapOptions.setMapTypeControl(true);
		mapOptions.setNavigationControl(true);

		NavigationControlOptions navigationControlOptions = new NavigationControlOptions(new ControlPositionImpl(), new NavigationControlStyleImpl());
		navigationControlOptions.setStyle(NavigationControlStyle.SMALL);
		mapOptions.setNavigationControlOptions(navigationControlOptions);

		MapTypeControlOptions mapTypeControlOptions = new MapTypeControlOptions(new ControlPositionImpl(), new MapTypeControlStyleImpl());
		mapTypeControlOptions.setStyle(MapTypeControlStyle.HORIZONTAL_BAR);
		mapOptions.setMapTypeControlOptions(mapTypeControlOptions);

		mapWidget = new MapWidget(mapOptions);

		map = mapWidget.getMap();

		mapWidget.setWidth("100%");
		mapWidget.setHeight("100%");

		wrapperPanel.add(mapWidget);

		triggerResizeMapEvent();
	}

	@Override
	public void setWidth(String width)
	{
		super.setWidth(width);

		wrapperPanel.setWidth(width);

		if(map != null && isMapDivResized())
		{
			triggerResizeMapEvent();
		}
	}

	@Override
	public void setHeight(String height)
	{
		super.setHeight(height);

		wrapperPanel.setHeight(height);

		if(map != null && isMapDivResized())
		{
			triggerResizeMapEvent();
		}
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection applicationConnection)
	{
		if(applicationConnection.updateComponent(this, uidl, true))
		{
			return;
		}

		paintableID = uidl.getId();

		AbstractVGoogleMap.applicationConnection = applicationConnection;

		if(mapWidget == null)
		{
			if(IconFactory.INSTANCE.getApplicationURL() == null)
			{
				IconFactory.INSTANCE.setApplicationURL(uidl.getStringAttribute("applicationURL"));
			}

			initMap(uidl.getIntVariable("zoomLevel"), uidl.getDoubleVariable("centerLatitude"), uidl.getDoubleVariable("centerLongitude"), MapType.valueOf(uidl.getStringVariable("mapType")), uidl.getBooleanAttribute("scrollWheelZoomingEnabled"), uidl.getBooleanAttribute("draggingEnabled"));

			addEventListeners();
		}
		else
		{
			setMapValues(uidl.getIntVariable("zoomLevel"), uidl.getDoubleVariable("centerLatitude"), uidl.getDoubleVariable("centerLongitude"), MapType.valueOf(uidl.getStringVariable("mapType")));
		}
	}

	/**
	 * Sets the given values to the map.
	 * 
	 * @param zoomLevel The zoom level
	 * @param centerLatitude The center latitude in degrees
	 * @param centerLongitude The center longitude in degrees
	 * @param mapType The {@code MapType}
	 */
	private void setMapValues(int zoomLevel, double centerLatitude, double centerLongitude, MapType mapType)
	{
		if(map.getZoom() != zoomLevel)
		{
			map.setZoom(zoomLevel);
		}

		if(map.getCenter().getLatitude() != centerLatitude || map.getCenter().getLongitude() != centerLongitude)
		{
			map.setCenter(new LatLng(centerLatitude, centerLongitude));
		}

		if(!map.getMapTypeId().equals(mapType.getAPIValue()))
		{
			map.setMapTypeId(mapType.getAPIValue());
		}
	}

	/**
	 * Sends the variables to the server side component.
	 */
	private void updateVariables()
	{
		double lowerLeftLongitude = map.getBounds().getSouthWest().getLongitude();
		double upperRightLongitude = map.getBounds().getNorthEast().getLongitude();

		if(lowerLeftLongitude > upperRightLongitude)
		{
			lowerLeftLongitude = this.lowerLeftLongitude;
			upperRightLongitude = this.upperRightLongitude;
		}

		applicationConnection.updateVariable(paintableID, "zoomLevel", map.getZoom(), false);
		applicationConnection.updateVariable(paintableID, "centerLatitude", map.getCenter().getLatitude(), false);
		applicationConnection.updateVariable(paintableID, "centerLongitude", map.getCenter().getLongitude(), false);
		applicationConnection.updateVariable(paintableID, "mapType", MapType.getValue(map.getMapTypeId()).toString(), false);
		applicationConnection.updateVariable(paintableID, "lowerLeftLatitude", map.getBounds().getSouthWest().getLatitude(), false);
		applicationConnection.updateVariable(paintableID, "lowerLeftLongitude", lowerLeftLongitude, false);
		applicationConnection.updateVariable(paintableID, "upperRightLatitude", map.getBounds().getNorthEast().getLatitude(), false);
		applicationConnection.updateVariable(paintableID, "upperRightLongitude", upperRightLongitude, true);
	}

	/**
	 * Adds {@code EventListener}s to the map.
	 */
	private void addEventListeners()
	{

		Event.addListener(map, "idle", new MouseEventCallback()
		{
			@Override
			public void callback(HasMouseEvent event)
			{
				updateVariables();
			}
		});

		Event.addListener(map, "maptypeid_changed", new MouseEventCallback()
		{
			@Override
			public void callback(HasMouseEvent event)
			{
				updateVariables();
			}
		});

		Event.addListener(map, "zoom_changed", new MouseEventCallback()
		{
			@Override
			public void callback(HasMouseEvent event)
			{
				zoomChanged = true;
			}
		});

		Event.addListener(map, "dragstart", new MouseEventCallback()
		{
			@Override
			public void callback(HasMouseEvent event)
			{
				dragging = true;
			}
		});

		Event.addListener(map, "dragend", new MouseEventCallback()
		{
			@Override
			public void callback(HasMouseEvent event)
			{
				dragging = false;
			}
		});

		Event.addListener(map, "bounds_changed", new MouseEventCallback()
		{
			@Override
			public void callback(HasMouseEvent event)
			{
				if(center == null)
				{
					updateGlobalMapVariables();
				}

				int newZoomLevel = map.getZoom();
				double newLowerLeftLongitude = map.getBounds().getSouthWest().getLongitude();
				double newUpperRightLongitude = map.getBounds().getNorthEast().getLongitude();

				if(zoomChanged)
				{
					zoomChanged = false;

					if(newZoomLevel < zoomLevel)
					{
						if(newLowerLeftLongitude > lowerLeftLongitude || newUpperRightLongitude < upperRightLongitude)
						{
							map.setZoom(zoomLevel);

							updateVariables();
						}
						else
						{
							updateGlobalMapVariables();
						}
					}
					else
					{
						updateGlobalMapVariables();
					}
				}
				else if(dragging)
				{
					if(newLowerLeftLongitude > newUpperRightLongitude || (newLowerLeftLongitude > lowerLeftLongitude && newUpperRightLongitude < upperRightLongitude) || (newLowerLeftLongitude < lowerLeftLongitude && newUpperRightLongitude > upperRightLongitude))
					{
						map.setCenter(center);

						updateVariables();
					}
					else
					{
						updateGlobalMapVariables();
					}
				}
			}
		});

		Event.addListener(map, "resize", new MouseEventCallback()
		{
			@Override
			public void callback(HasMouseEvent event)
			{
				if(center == null)
				{
					updateGlobalMapVariables();
				}

				double newLowerLeftLongitude = map.getBounds().getSouthWest().getLongitude();
				double newUpperRightLongitude = map.getBounds().getNorthEast().getLongitude();

				if(Math.abs(newLowerLeftLongitude - lowerLeftLongitude) > 100 || Math.abs(newUpperRightLongitude - upperRightLongitude) > 100)
				{
					map.setZoom(zoomLevel + 1);

					updateVariables();
				}
				else
				{
					updateGlobalMapVariables();
				}
			}
		});
	}

	/**
	 * Determines whether the DIV block of the map has been resized.
	 * 
	 * @return {@code true} if the DIV block has been resized; otherwise
	 *         {@code false}
	 */
	private boolean isMapDivResized()
	{
		Element div = map.getDiv();

		if(absoluteLeft == -1)
		{
			updateGlobalDivVariables();

			return false;
		}

		if(absoluteLeft != div.getAbsoluteLeft() || absoluteRight != div.getAbsoluteRight() || absoluteTop != div.getAbsoluteTop() || absoluteBottom != div.getAbsoluteBottom())
		{
			updateGlobalDivVariables();

			return true;
		}

		return false;
	}

	/**
	 * Triggers a {@code resize} map event.
	 */
	private void triggerResizeMapEvent()
	{
		HasLatLng center = map.getCenter();
		Event.trigger(map, "resize");
		map.setCenter(center);
	}

	/**
	 * Updates the global variables of the map.
	 */
	private void updateGlobalMapVariables()
	{
		center = map.getCenter();
		zoomLevel = map.getZoom();

		double lowerLeftLongitude = map.getBounds().getSouthWest().getLongitude();
		double upperRightLongitude = map.getBounds().getNorthEast().getLongitude();

		if(lowerLeftLongitude < upperRightLongitude)
		{
			this.lowerLeftLongitude = lowerLeftLongitude;
			this.upperRightLongitude = upperRightLongitude;
		}
	}

	/**
	 * Updates the global variables of the DIV block of the map.
	 */
	private void updateGlobalDivVariables()
	{
		Element div = map.getDiv();

		absoluteLeft = div.getAbsoluteLeft();
		absoluteRight = div.getAbsoluteRight();
		absoluteTop = div.getAbsoluteTop();
		absoluteBottom = div.getAbsoluteBottom();
	}
}
