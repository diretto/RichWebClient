package org.diretto.web.richwebclient.view.widgets.googlemap.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.diretto.api.client.base.data.ResultSet;
import org.diretto.api.client.base.data.ResultSetImpl;
import org.diretto.api.client.external.processing.ProcessingService;
import org.diretto.api.client.main.core.CoreService;
import org.diretto.api.client.main.core.entities.Attachment;
import org.diretto.api.client.main.core.entities.CoreServiceEntityIDFactory;
import org.diretto.api.client.main.core.entities.Document;
import org.diretto.api.client.main.core.entities.DocumentID;
import org.diretto.api.client.main.core.entities.Position;
import org.diretto.api.client.main.core.entities.Time;
import org.diretto.api.client.user.UserID;
import org.diretto.api.client.user.UserInfo;
import org.diretto.web.richwebclient.RichWebClientApplication;
import org.diretto.web.richwebclient.management.AuthenticationRegistry;
import org.diretto.web.richwebclient.view.util.MediaTypeUtils;
import org.diretto.web.richwebclient.view.widgets.googlemap.client.VExploreGoogleMap;
import org.diretto.web.richwebclient.view.widgets.googlemap.client.base.MapType;
import org.diretto.web.richwebclient.view.widgets.googlemap.server.base.AbstractGoogleMap;
import org.diretto.web.richwebclient.view.widgets.googlemap.server.event.GoogleMapListener;
import org.diretto.web.richwebclient.view.widgets.googlemap.server.event.LoadingProcessListener;
import org.diretto.web.richwebclient.view.windows.DocumentViewWindow;
import org.diretto.web.richwebclient.view.windows.MainWindow;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;

/**
 * The server side component of the {@code ExploreGoogleMap}.
 * 
 * @author Tobias Schlecht
 */
@ClientWidget(VExploreGoogleMap.class)
public final class ExploreGoogleMap extends AbstractGoogleMap
{
	private static final long serialVersionUID = -5921741171570925121L;

	private final List<LoadingProcessListener> loadingProcessListeners = new Vector<LoadingProcessListener>();

	private static final int THUMBNAIL_SIZE = 60;

	private final CoreService coreService;
	private final ProcessingService processingService;
	private final MainWindow mainWindow;
	private final AuthenticationRegistry authenticationRegistry;
	private final int paginationSize;

	private final Map<DocumentID, Document> documents = new ConcurrentHashMap<DocumentID, Document>();
	private final Map<DocumentID, String> userNames = new ConcurrentHashMap<DocumentID, String>();
	private final Map<DocumentID, String> thumbnailURLs = new ConcurrentHashMap<DocumentID, String>();

	private volatile Thread currentThread = null;

	private boolean polling = true;
	private boolean clearMap = false;

	/**
	 * Constructs an {@link ExploreGoogleMap}.
	 * 
	 * @param application The corresponding {@code RichWebClientApplication}
	 * @param zoomLevel The initial zoom level
	 * @param mapType The initial {@code MapType}
	 */
	public ExploreGoogleMap(RichWebClientApplication application, int zoomLevel, MapType mapType)
	{
		this(application, zoomLevel, 0.0d, 0.0d, mapType);
	}

	/**
	 * Constructs an {@link ExploreGoogleMap}.
	 * 
	 * @param application The corresponding {@code RichWebClientApplication}
	 * @param zoomLevel The initial zoom level
	 * @param centerLatitude The initial center latitude in degrees
	 * @param centerLongitude The initial center longitude in degrees
	 * @param mapType The initial {@code MapType}
	 */
	public ExploreGoogleMap(RichWebClientApplication application, int zoomLevel, double centerLatitude, double centerLongitude, MapType mapType)
	{
		this(application, zoomLevel, centerLatitude, centerLongitude, mapType, true);
	}

	/**
	 * Constructs an {@link ExploreGoogleMap}.
	 * 
	 * @param application The corresponding {@code RichWebClientApplication}
	 * @param zoomLevel The initial zoom level
	 * @param centerLatitude The initial center latitude in degrees
	 * @param centerLongitude The initial center longitude in degrees
	 * @param mapType The initial {@code MapType}
	 * @param scrollWheelZoomingEnabled {@code true} if scroll wheel zooming is
	 *        enabled; otherwise {@code false}
	 */
	public ExploreGoogleMap(RichWebClientApplication application, int zoomLevel, double centerLatitude, double centerLongitude, MapType mapType, boolean scrollWheelZoomingEnabled)
	{
		super(zoomLevel, centerLatitude, centerLongitude, mapType, scrollWheelZoomingEnabled, true);

		coreService = application.getCoreService();
		processingService = application.getProcessingService();
		mainWindow = (MainWindow) application.getMainWindow();
		authenticationRegistry = application.getAuthenticationRegistry();
		paginationSize = coreService.getPaginationSize();
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException
	{
		super.paintContent(target);

		target.startTag("polling");

		target.addAttribute("value", polling);

		target.endTag("polling");

		target.startTag("clearMap");

		target.addAttribute("value", clearMap);

		target.endTag("clearMap");

		target.startTag("documents");

		for(DocumentID documentID : documents.keySet())
		{
			Document document = documents.remove(documentID);
			String publisher = userNames.remove(documentID);
			String thumbnailURL = thumbnailURLs.remove(documentID);

			if(document != null && publisher != null && thumbnailURL != null)
			{
				Attachment attachment = document.getInitialAttachment();
				Position position = document.getBestVotedPosition();
				Time time = document.getBestVotedTime();

				target.startTag("document");

				target.addAttribute("id", documentID.toString());
				target.addAttribute("title", document.getTitle());
				target.addAttribute("publisher", publisher);
				target.addAttribute("position-latitude", position.getLatitude());
				target.addAttribute("position-longitude", position.getLongitude());
				target.addAttribute("average-time", time.getAverageTime().getMillis());
				target.addAttribute("media-type", MediaTypeUtils.getMediaType(document.getMediaMainType()).toString());
				target.addAttribute("votes-up", attachment.getVotes().UP);
				target.addAttribute("votes-down", attachment.getVotes().DOWN);
				target.addAttribute("thumbnail-url", thumbnailURL);

				target.endTag("document");
			}
		}

		target.endTag("documents");
	}

	@Override
	public void changeVariables(Object source, Map<String, Object> variables)
	{
		super.changeVariables(source, variables);

		if(variables.containsKey("cleared") && (Boolean) variables.get("cleared"))
		{
			clearMap = false;
		}

		if(variables.containsKey("documentView"))
		{
			String documentIDString = (String) variables.get("documentView");

			DocumentID documentID = CoreServiceEntityIDFactory.getDocumentIDInstance(documentIDString);

			DocumentViewWindow documentViewWindow = new DocumentViewWindow(mainWindow, coreService, processingService, authenticationRegistry, documentID);

			mainWindow.addWindow(documentViewWindow);
			documentViewWindow.focus();
		}
	}

	/**
	 * Adds the given {@link Document} to this {@link ExploreGoogleMap}.
	 * 
	 * @param document A {@code Document}
	 */
	public void addDocument(Document document)
	{
		DocumentID documentID = document.getID();

		userNames.put(documentID, coreService.getUserInfo(document.getPublisher()).getUserName());

		thumbnailURLs.put(documentID, processingService.getThumbnailURL(documentID, THUMBNAIL_SIZE).toExternalForm());

		documents.put(documentID, document);

		requestRepaint();
	}

	/**
	 * Updates this {@link ExploreGoogleMap} with the given {@link ResultSet} of
	 * {@link Document}s.
	 * 
	 * @param documents A {@code ResultSet} of {@code Document}
	 */
	public void updateDocuments(final ResultSet<DocumentID, Document> documents)
	{
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				boolean loading = false;

				if(((ResultSetImpl<DocumentID, Document>) documents).getLoadedDataSize() >= paginationSize)
				{
					loading = true;

					for(LoadingProcessListener loadingProcessListener : loadingProcessListeners)
					{
						loadingProcessListener.onLoadingProcessStarted();
					}

					requestRepaint();
				}

				clearMap = true;

				Map<DocumentID, Document> documentsBatch = new HashMap<DocumentID, Document>(paginationSize);
				Map<DocumentID, UserID> userIDsBatch = new HashMap<DocumentID, UserID>(paginationSize);

				int count = 0;

				for(Iterator<Document> iterator = documents.iterator(); iterator.hasNext();)
				{
					synchronized(ExploreGoogleMap.this)
					{
						if(!Thread.currentThread().equals(currentThread))
						{
							ExploreGoogleMap.this.documents.clear();
							userNames.clear();
							thumbnailURLs.clear();

							if(loading)
							{
								for(LoadingProcessListener loadingProcessListener : loadingProcessListeners)
								{
									loadingProcessListener.onLoadingProcessFinished();
								}
							}

							requestRepaint();

							currentThread.start();

							return;
						}
					}

					Document document = iterator.next();
					DocumentID documentID = document.getID();

					documentsBatch.put(documentID, document);
					userIDsBatch.put(documentID, document.getPublisher());

					thumbnailURLs.put(documentID, processingService.getThumbnailURL(documentID, THUMBNAIL_SIZE).toExternalForm());

					count++;

					if(count == paginationSize || !iterator.hasNext())
					{
						ResultSet<UserID, UserInfo> userInfos = coreService.getUserInfosByIDs(new Vector<UserID>(userIDsBatch.values()));

						for(DocumentID id : userIDsBatch.keySet())
						{
							String userName = userInfos.get(userIDsBatch.get(id)).getUserName();

							userNames.put(id, userName);

							ExploreGoogleMap.this.documents.put(id, documentsBatch.get(id));
						}

						documentsBatch.clear();
						userIDsBatch.clear();

						count = 0;

						requestRepaint();
					}
				}

				if(loading)
				{
					for(LoadingProcessListener loadingProcessListener : loadingProcessListeners)
					{
						loadingProcessListener.onLoadingProcessFinished();
					}
				}

				requestRepaint();

				synchronized(ExploreGoogleMap.this)
				{
					if(Thread.currentThread().equals(currentThread))
					{
						currentThread = null;
					}
					else
					{
						currentThread.start();
					}
				}
			}
		});

		synchronized(this)
		{
			if(currentThread == null)
			{
				currentThread = thread;

				currentThread.start();
			}
			else
			{
				currentThread = thread;
			}
		}
	}

	/**
	 * Activates polling of the client side component.
	 */
	public void activatePolling()
	{
		polling = true;

		requestRepaint();
	}

	/**
	 * Deactivates polling of the client side component.
	 */
	public void deactivatePolling()
	{
		polling = false;

		requestRepaint();
	}

	/**
	 * Adds the given {@link LoadingProcessListener}.
	 * 
	 * @param loadingProcessListener A {@code LoadingProcessListener}
	 */
	public synchronized void addLoadingProcessListener(LoadingProcessListener loadingProcessListener)
	{
		if(!loadingProcessListeners.contains(loadingProcessListener))
		{
			loadingProcessListeners.add(loadingProcessListener);
		}
	}

	/**
	 * Removes the given {@link GoogleMapListener}.
	 * 
	 * @param googleMapListener A {@code GoogleMapListener}
	 */
	public synchronized void removeListener(GoogleMapListener googleMapListener)
	{
		if(googleMapListener instanceof LoadingProcessListener)
		{
			loadingProcessListeners.remove(googleMapListener);
		}
	}
}
