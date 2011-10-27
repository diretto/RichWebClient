package org.diretto.web.richwebclient;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.XMLConfiguration;
import org.diretto.api.client.external.processing.ProcessingService;
import org.diretto.api.client.external.task.TaskService;
import org.diretto.api.client.main.core.CoreService;
import org.diretto.api.client.main.feed.FeedService;
import org.diretto.api.client.main.storage.StorageService;
import org.diretto.web.richwebclient.management.AuthenticationRegistry;
import org.diretto.web.richwebclient.view.windows.MainWindow;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.UploadException;

/**
 * This is the {@link Application} class of the {@code RichWebClient}.
 * 
 * @author Tobias Schlecht
 */
public class RichWebClientApplication extends Application
{
	private static final long serialVersionUID = -968770595914387337L;

	private AuthenticationRegistry authenticationRegistry = null;

	@Override
	public void init()
	{
		authenticationRegistry = AuthenticationRegistry.getInstance(this);

		setTheme("diretto");
		MainWindow mainWindow = new MainWindow(this);
		setMainWindow(mainWindow);
	}

	/**
	 * Returns the corresponding {@link AuthenticationRegistry}.
	 * 
	 * @return The corresponding {@code AuthenticationRegistry}
	 */
	public AuthenticationRegistry getAuthenticationRegistry()
	{
		return authenticationRegistry;
	}

	/**
	 * Returns the corresponding {@link CoreService}.
	 * 
	 * @return The corresponding {@code CoreService}
	 */
	public CoreService getCoreService()
	{
		return RichWebClientServlet.getCoreService();
	}

	/**
	 * Returns the corresponding {@link StorageService}.
	 * 
	 * @return The corresponding {@code StorageService}
	 */
	public StorageService getStorageService()
	{
		return RichWebClientServlet.getStorageService();
	}

	/**
	 * Returns the corresponding {@link FeedService}.
	 * 
	 * @return The corresponding {@code FeedService}
	 */
	public FeedService getFeedService()
	{
		return RichWebClientServlet.getFeedService();
	}

	/**
	 * Returns the corresponding {@link ProcessingService}.
	 * 
	 * @return The corresponding {@code ProcessingService}
	 */
	public ProcessingService getProcessingService()
	{
		return RichWebClientServlet.getProcessingService();
	}

	/**
	 * Returns the corresponding {@link TaskService}.
	 * 
	 * @return The corresponding {@code TaskService}
	 */
	public TaskService getTaskService()
	{
		return RichWebClientServlet.getTaskService();
	}

	/**
	 * Returns the {@link XMLConfiguration} object, which is loaded from the XML
	 * configuration file corresponding to the whole {@code RichWebClient}
	 * implementation.
	 * 
	 * @return The {@code XMLConfiguration} object
	 */
	public XMLConfiguration getXMLConfiguration()
	{
		return RichWebClientServlet.getXMLConfiguration();
	}

	@Override
	public void terminalError(com.vaadin.terminal.Terminal.ErrorEvent event)
	{
		if(!(event.getThrowable() instanceof UploadException))
		{
			super.terminalError(event);
		}
		else
		{
			Logger logger = Logger.getLogger(Application.class.getName());

			logger.log(Level.INFO, "[RichWebClientApplication] Terminal Error: " + event.getThrowable().getMessage() + " - " + UploadException.class.getCanonicalName());
		}
	}
}
