package org.diretto.web.richwebclient.view.sections;

import org.diretto.api.client.session.UserSession;
import org.diretto.api.client.util.Observable;
import org.diretto.web.richwebclient.RichWebClientApplication;
import org.diretto.web.richwebclient.view.base.AbstractSection;

/**
 * This class represents a {@code TaskSection}.
 * 
 * @author Tobias Schlecht
 */
public final class TaskSection extends AbstractSection
{
	private static final long serialVersionUID = -3391170672720553642L;

	private boolean componentsAdded = false;

	/**
	 * Constructs a {@link TaskSection}.
	 * 
	 * @param application The corresponding {@code RichWebClientApplication}
	 */
	public TaskSection(RichWebClientApplication application)
	{
		super(application.getAuthenticationRegistry(), false, false, "Tasks", "Help each other");
	}

	@Override
	public synchronized void addComponents()
	{
		if(!componentsAdded)
		{
			// TODO

			componentsAdded = true;
		}
	}

	@Override
	public void update(Observable<UserSession> observable, UserSession userSession)
	{
		// TODO
	}
}
