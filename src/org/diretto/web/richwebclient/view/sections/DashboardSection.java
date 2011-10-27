package org.diretto.web.richwebclient.view.sections;

import org.diretto.api.client.session.UserSession;
import org.diretto.api.client.util.Observable;
import org.diretto.web.richwebclient.RichWebClientApplication;
import org.diretto.web.richwebclient.view.base.AbstractSection;

/**
 * This class represents a {@code DashboardSection}.
 * 
 * @author Tobias Schlecht
 */
public final class DashboardSection extends AbstractSection
{
	private static final long serialVersionUID = 7596815118346819309L;

	private boolean componentsAdded = false;

	/**
	 * Constructs a {@link DashboardSection}.
	 * 
	 * @param application The corresponding {@code RichWebClientApplication}
	 */
	public DashboardSection(RichWebClientApplication application)
	{
		super(application.getAuthenticationRegistry(), true, false, "Dashboard", "Organize the data");
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
