package org.diretto.web.richwebclient.view.sections;

import org.diretto.api.client.session.UserSession;
import org.diretto.api.client.util.Observable;
import org.diretto.web.richwebclient.RichWebClientApplication;
import org.diretto.web.richwebclient.view.base.AbstractSection;

/**
 * This class represents a {@code MessageSection}.
 * 
 * @author Tobias Schlecht
 */
public final class MessageSection extends AbstractSection
{
	private static final long serialVersionUID = -8616917165873538022L;

	private boolean componentsAdded = false;

	/**
	 * Constructs a {@link MessageSection}.
	 * 
	 * @param application The corresponding {@code RichWebClientApplication}
	 */
	public MessageSection(RichWebClientApplication application)
	{
		super(application.getAuthenticationRegistry(), true, false, "Messages", "Exchange with other users");
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
