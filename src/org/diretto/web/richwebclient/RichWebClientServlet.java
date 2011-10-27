package org.diretto.web.richwebclient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.XMLConfiguration;
import org.diretto.api.client.JavaClient;
import org.diretto.api.client.JavaClientManager;
import org.diretto.api.client.external.processing.ProcessingService;
import org.diretto.api.client.external.processing.ProcessingServiceID;
import org.diretto.api.client.external.task.TaskService;
import org.diretto.api.client.external.task.TaskServiceID;
import org.diretto.api.client.main.core.CoreService;
import org.diretto.api.client.main.feed.FeedService;
import org.diretto.api.client.main.feed.FeedServiceID;
import org.diretto.api.client.main.storage.StorageService;
import org.diretto.api.client.main.storage.StorageServiceID;
import org.diretto.api.client.session.SystemSession;
import org.diretto.api.client.util.ConfigUtils;
import org.diretto.api.client.util.URLTransformationUtils;

import com.vaadin.terminal.gwt.server.ApplicationServlet;

/**
 * This is the {@link ApplicationServlet} class of the {@code RichWebClient}.
 * 
 * @author Tobias Schlecht
 */
public final class RichWebClientServlet extends ApplicationServlet
{
	private static final long serialVersionUID = 2673494237174351329L;

	private static final String CONFIG_FILE = "org/diretto/web/richwebclient/config.xml";

	private static final XMLConfiguration xmlConfiguration = ConfigUtils.getXMLConfiguration(CONFIG_FILE);

	private static final URL apiBaseURL;
	private static final SystemSession systemSession;
	private static final JavaClient javaClient;
	private static final CoreService coreService;
	private static final StorageService storageService;
	private static final FeedService feedService;
	private static final ProcessingService processingService;
	private static final TaskService taskService;

	static
	{
		String apiBaseURLString = xmlConfiguration.getString("api-base-url");

		URL initAPIBaseURL = null;

		try
		{
			initAPIBaseURL = new URL(apiBaseURLString);
		}
		catch(MalformedURLException e)
		{
			e.printStackTrace();
		}

		apiBaseURL = URLTransformationUtils.adjustAPIBaseURL(initAPIBaseURL);

		String systemUserEmailAddress = xmlConfiguration.getString("system-user/email-address");
		String systemUserPassword = xmlConfiguration.getString("system-user/password");

		systemSession = JavaClientManager.INSTANCE.getSystemSession(apiBaseURL, systemUserEmailAddress, systemUserPassword);
		javaClient = JavaClientManager.INSTANCE.getJavaClient(systemSession);

		coreService = javaClient.getCoreService();
		storageService = (StorageService) javaClient.getService(StorageServiceID.INSTANCE);
		feedService = (FeedService) javaClient.getService(FeedServiceID.INSTANCE);
		processingService = (ProcessingService) javaClient.getService(ProcessingServiceID.INSTANCE);
		taskService = (TaskService) javaClient.getService(TaskServiceID.INSTANCE);
	}

	@Override
	public void init(ServletConfig servletConfig) throws ServletException
	{
		super.init(servletConfig);

	}

	@Override
	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException
	{
		super.doGet(httpServletRequest, httpServletResponse);

	}

	@Override
	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException
	{
		super.doPost(httpServletRequest, httpServletResponse);

	}

	/**
	 * Returns the {@link XMLConfiguration} object, which is loaded from the XML
	 * configuration file corresponding to the whole {@code RichWebClient}
	 * implementation.
	 * 
	 * @return The {@code XMLConfiguration} object
	 */
	static XMLConfiguration getXMLConfiguration()
	{
		return xmlConfiguration;
	}

	/**
	 * Returns the base {@link URL} of the API.
	 * 
	 * @return The base {@code URL} of the API
	 */
	static URL getAPIBaseURL()
	{
		return apiBaseURL;
	}

	/**
	 * Returns the corresponding {@link CoreService} of this
	 * {@link RichWebClientServlet}.
	 * 
	 * @return The {@code CoreService}
	 */
	static CoreService getCoreService()
	{
		return coreService;
	}

	/**
	 * Returns the corresponding {@link StorageService} of this
	 * {@link RichWebClientServlet}.
	 * 
	 * @return The {@code StorageService}
	 */
	static StorageService getStorageService()
	{
		return storageService;
	}

	/**
	 * Returns the corresponding {@link FeedService} of this
	 * {@link RichWebClientServlet}.
	 * 
	 * @return The {@code FeedService}
	 */
	static FeedService getFeedService()
	{
		return feedService;
	}

	/**
	 * Returns the corresponding {@link ProcessingService} of this
	 * {@link RichWebClientServlet}.
	 * 
	 * @return The {@code ProcessingService}
	 */
	static ProcessingService getProcessingService()
	{
		return processingService;
	}

	/**
	 * Returns the corresponding {@link TaskService} of this
	 * {@link RichWebClientServlet}.
	 * 
	 * @return The {@code TaskService}
	 */
	static TaskService getTaskService()
	{
		return taskService;
	}
}
