package org.diretto.web.richwebclient.view.widgets.googlemap.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.diretto.web.richwebclient.view.base.client.MediaType;
import com.google.gwt.maps.client.base.InfoWindow;
import com.google.gwt.maps.client.base.InfoWindowOptions;
import com.google.gwt.maps.client.event.Event;
import com.google.gwt.maps.client.event.EventCallback;
import com.google.gwt.maps.client.event.HasMouseEvent;
import com.google.gwt.maps.client.event.MouseEventCallback;
import org.diretto.web.richwebclient.view.widgets.googlemap.client.base.AbstractVGoogleMap;
import org.diretto.web.richwebclient.view.widgets.googlemap.client.markers.Marker;
import org.diretto.web.richwebclient.view.widgets.googlemap.client.markers.RegularMarker;
import org.diretto.web.richwebclient.view.widgets.googlemap.client.markers.RegularMarker.RegularMarkerData;
import org.diretto.web.richwebclient.view.widgets.googlemap.server.ExploreGoogleMap;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

/**
 * The client side component of the {@link ExploreGoogleMap}.
 * 
 * @author Tobias Schlecht
 */
public final class VExploreGoogleMap extends AbstractVGoogleMap
{
	private static final int THUMBNAIL_SIZE = 60;
	private static final int POLLING_INTERVAL = 3000;

	private boolean initialized = false;
	private boolean polling = true;

	private Timer timer = null;

	private Map<String, RegularMarker> markers = new HashMap<String, RegularMarker>(1000);

	private RegularMarker selectedMarker = null;
	private InfoWindow openedInfoWindow = null;

	private static String selectedMarkerDocumentID = null;

	/**
	 * Constructs a {@link VExploreGoogleMap}.
	 */
	public VExploreGoogleMap()
	{
		super();
	}

	@Override
	public void updateFromUIDL(UIDL uidl, final ApplicationConnection applicationConnection)
	{
		super.updateFromUIDL(uidl, applicationConnection);

		if(!initialized)
		{
			timer = new Timer()
			{
				@Override
				public void run()
				{
					applicationConnection.sendPendingVariableChanges();
				}
			};

			timer.scheduleRepeating(POLLING_INTERVAL);

			exportJavaScriptMethods();
			addEventListeners();

			initialized = true;
		}
		else
		{
			UIDL uidlPolling = uidl.getChildByTagName("polling");

			boolean polling = uidlPolling.getBooleanAttribute("value");

			if(polling)
			{
				if(!this.polling)
				{
					timer.scheduleRepeating(POLLING_INTERVAL);

					this.polling = true;
				}
			}
			else
			{
				if(this.polling)
				{
					timer.cancel();

					this.polling = false;
				}
			}

			UIDL uidlClearMap = uidl.getChildByTagName("clearMap");

			boolean clearMap = uidlClearMap.getBooleanAttribute("value");

			if(clearMap)
			{
				for(RegularMarker marker : markers.values())
				{
					marker.setMap(null);
				}

				markers.clear();

				applicationConnection.updateVariable(paintableID, "cleared", true, true);
			}

			UIDL uidlDocuments = uidl.getChildByTagName("documents");

			for(Iterator<Object> iterator = uidlDocuments.getChildIterator(); iterator.hasNext();)
			{
				UIDL uidlDocument = (UIDL) iterator.next();

				String id = uidlDocument.getStringAttribute("id");
				String title = uidlDocument.getStringAttribute("title");
				String publisher = uidlDocument.getStringAttribute("publisher");
				double positionLatitude = uidlDocument.getDoubleAttribute("position-latitude");
				double positionLongitude = uidlDocument.getDoubleAttribute("position-longitude");
				long averageTime = uidlDocument.getLongAttribute("average-time");
				MediaType mediaType = MediaType.valueOf(uidlDocument.getStringAttribute("media-type"));
				int votesUP = uidlDocument.getIntAttribute("votes-up");
				int votesDOWN = uidlDocument.getIntAttribute("votes-down");
				String thumbnailURL = uidlDocument.getStringAttribute("thumbnail-url");

				RegularMarkerData regularMarkerData = new RegularMarkerData(id, title, publisher, averageTime, votesUP, votesDOWN, thumbnailURL);

				RegularMarker marker = new RegularMarker(map, positionLatitude, positionLongitude, mediaType, regularMarkerData);

				addMarkerEventListeners(marker);

				markers.put(id, marker);

				if(selectedMarker != null && id.equals(selectedMarkerDocumentID))
				{
					if(openedInfoWindow != null)
					{
						Event.trigger(marker, "mousedown");
					}
					else
					{
						selectedMarker = marker;

						marker.select();
					}
				}
				else
				{
					marker.setZIndex(-1);
				}
			}
		}
	}

	/**
	 * Exports the necessary JavaScript methods.
	 */
	private native void exportJavaScriptMethods()
	/*-{
		$wnd.loadDocumentView = $entry(this.@org.diretto.web.richwebclient.view.widgets.googlemap.client.VExploreGoogleMap::loadDocumentView());
	}-*/;

	/**
	 * Sets the content of the given {@link InfoWindow}.
	 * 
	 * @param infoWindow An {@code InfoWindow}
	 * @param regularMarkerData The corresponding {@code RegularMarkerData}
	 * @param positionLatitude The corresponding {@code Position} latitude in
	 *        degrees
	 * @param positionLongitude The corresponding {@code Position} longitude in
	 *        degrees
	 */
	private void setInfoWindowContent(InfoWindow infoWindow, RegularMarkerData regularMarkerData, double positionLatitude, double positionLongitude)
	{
		String content = "" +
				
		"<div id=\"googleMapsInfoWindow\" style=\"height:220px; font-size:11px; line-height:15px;\">" +
			"<div style=\"width:230px; height:1px;\" />" +
			"<div style=\"width:205px; margin-bottom:10px; overflow:hidden;\">" +
				"<a href=\"javascript:loadDocumentView()\" style=\"font-size:12px; font-weight:bold; color:#1B699F; text-decoration:none;\">" + SafeHtmlUtils.htmlEscape(regularMarkerData.getTitle()) + "</a>" +
			"</div>" +
			"<div style=\"margin-bottom:12px;\">" +
				"<a href=\"javascript:loadDocumentView()\">" +
					"<img src=\"" + regularMarkerData.getThumbnailURL() +"\" alt=\"\" style=\"width:" + THUMBNAIL_SIZE + "px; height:" + THUMBNAIL_SIZE + "px; border:1px solid #000000;\" />" +
				"</a>" +
			"</div>" +
			"<div>" +
				"<hr />" +
			"</div>" +
			"<div style=\"width:205px; overflow:hidden;\">" +
				"<span style=\"font-weight:bold\">Publisher:&nbsp;</span>" +
				"<span>" + SafeHtmlUtils.htmlEscape(regularMarkerData.getPublisher()) + "</span>" +
			"</div>" +
			"<div>" +
				"<hr />" +
			"</div>" +
			"<div style=\"white-space:nowrap;\">" +
				"<span style=\"font-weight:bold\">Position Latitude:&nbsp;</span>" +
				"<span>" + positionLatitude + "</span>" +
			"</div>" +
			"<div style=\"white-space:nowrap;\">" +
				"<span style=\"font-weight:bold\">Position Longitude:&nbsp;</span>" +
				"<span>" + positionLongitude + "</span>" +
			"</div>" +
			"<div style=\"white-space:nowrap;\">" +
				"<span style=\"font-weight:bold\">Time:&nbsp;</span>" +
				"<span>" +
					DateTimeFormat.getFormat("dd.MM.yyyy'&nbsp;'-'&nbsp;'hh:mm:ss' 'aa").format(new Date(regularMarkerData.getAverageTime())) +
				"</span>" +
			"</div>" +
			"<div>" +
				"<hr />" +
			"</div>" +
			"<div style=\"white-space:nowrap;\">" +
				"<span style=\"font-weight:bold\">UP Votes:&nbsp;</span>" +
				"<span>" + regularMarkerData.getVotesUP() + "</span>" +
				"<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>" +
				"<span style=\"font-weight:bold\">DOWN Votes:&nbsp;</span>" +
				"<span>" + regularMarkerData.getVotesDOWN() + "</span>" +
			"</div>" +
		"</div>";
		
		infoWindow.setContent(content);
	}

	/**
	 * Styles the opened {@link InfoWindow} and thereby resolves the problem
	 * with the unwanted scroll bars.
	 */
	private static native void styleInfoWindow()
	/*-{
		$wnd.document.getElementById('googleMapsInfoWindow').parentNode.style.overflow='';
		$wnd.document.getElementById('googleMapsInfoWindow').parentNode.style.width=$wnd.document.getElementById('googleMapsInfoWindow').parentNode.width;
		
		$wnd.document.getElementById('googleMapsInfoWindow').parentNode.parentNode.style.overflow='';                                                  
		$wnd.document.getElementById('googleMapsInfoWindow').parentNode.parentNode.style.overflowX='hidden';
		$wnd.document.getElementById('googleMapsInfoWindow').parentNode.parentNode.style.overflowY='auto';

		$wnd.document.getElementById('googleMapsInfoWindow').style.width=($wnd.document.getElementById('googleMapsInfoWindow').parentNode.parentNode.width - 25);
	}-*/;

	/**
	 * Initializes the loading process of the {@code DocumentView} for the
	 * {@code Document} corresponding to the currently selected {@link Marker}.
	 */
	private void loadDocumentView()
	{
		if(selectedMarkerDocumentID != null)
		{
			applicationConnection.updateVariable(paintableID, "documentView", selectedMarkerDocumentID, true);
		}
	}

	/**
	 * Adds {@code EventListener}s to the given {@link RegularMarker}.
	 * 
	 * @param marker A {@code RegularMarker}
	 */
	private void addMarkerEventListeners(final RegularMarker marker)
	{
		Event.addListener(marker, "mousedown", new MouseEventCallback()
		{
			@Override
			public void callback(HasMouseEvent event)
			{
				if(marker != selectedMarker)
				{
					if(openedInfoWindow != null)
					{
						openedInfoWindow.close();
					}

					if(selectedMarker != null)
					{
						selectedMarker.deselect();
					}

					InfoWindow infoWindow = new InfoWindow();
					InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
					infoWindowOptions.setDisableAutoPan(true);
					infoWindowOptions.setMaxWidth(300);
					infoWindow.setOptions(infoWindowOptions);
					addInfoWindowEventListeners(infoWindow);
					setInfoWindowContent(infoWindow, marker.getRegularMarkerData(), marker.getPosition().getLatitude(), marker.getPosition().getLongitude());
					infoWindow.open(map, marker);

					openedInfoWindow = infoWindow;
					selectedMarker = marker;
					selectedMarkerDocumentID = marker.getRegularMarkerData().getDocumentID();
				}
				else if(openedInfoWindow == null)
				{
					InfoWindow infoWindow = new InfoWindow();
					InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
					infoWindowOptions.setDisableAutoPan(true);
					infoWindowOptions.setMaxWidth(300);
					infoWindow.setOptions(infoWindowOptions);
					addInfoWindowEventListeners(infoWindow);
					setInfoWindowContent(infoWindow, marker.getRegularMarkerData(), marker.getPosition().getLatitude(), marker.getPosition().getLongitude());
					infoWindow.open(map, marker);

					openedInfoWindow = infoWindow;
				}
			}
		});
	}

	/**
	 * Adds {@code EventListener}s to the given {@link InfoWindow}.
	 * 
	 * @param infoWindow An {@code InfoWindow}
	 */
	private void addInfoWindowEventListeners(final InfoWindow infoWindow)
	{
		Event.addListener(infoWindow, "closeclick", new EventCallback()
		{
			@Override
			public void callback()
			{
				openedInfoWindow = null;
			}
		});

		Event.addListener(infoWindow, "domready", new EventCallback()
		{
			@Override
			public void callback()
			{
				styleInfoWindow();
			}
		});
	}

	/**
	 * Adds {@code EventListener}s to the map.
	 */
	private void addEventListeners()
	{
		Event.addListener(map, "rightclick", new MouseEventCallback()
		{
			@Override
			public void callback(HasMouseEvent event)
			{
				if(selectedMarker != null)
				{
					map.setCenter(selectedMarker.getPosition());
				}
			}
		});
	}
}
