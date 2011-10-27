package org.diretto.web.richwebclient.view.widgets.googlemap.server.event;

/**
 * This interface represents a {@link GoogleMapListener} for {@code GoogleMap}
 * events in respect of loading processes.
 * 
 * @author Tobias Schlecht
 */
public interface LoadingProcessListener extends GoogleMapListener
{
	/**
	 * Called when the corresponding loading process has been started.
	 */
	void onLoadingProcessStarted();

	/**
	 * Called when the corresponding loading process has been finished.
	 */
	void onLoadingProcessFinished();
}
