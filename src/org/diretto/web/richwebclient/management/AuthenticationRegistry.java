package org.diretto.web.richwebclient.management;

import org.diretto.api.client.main.core.CoreService;
import org.diretto.api.client.session.UserSession;
import org.diretto.api.client.user.User;
import org.diretto.api.client.util.AbstractObservable;
import org.diretto.api.client.util.Observable;
import org.diretto.api.client.util.Observer;
import org.diretto.web.richwebclient.RichWebClientApplication;
import org.diretto.web.richwebclient.management.base.Manager;

/**
 * The {@code AuthenticationRegistry} is a {@link Manager} class and is
 * principally responsible for managing the login and logout procedures.
 * Furthermore it is an {@link Observable} object which notifies all of its
 * {@link Observer}s if the {@code LoggedIn/LoggedOut} state has changed. In
 * case a {@link User} has logged in, the corresponding {@link UserSession} will
 * be sent to all registered {@code Observer}s. In case a {@code User} has
 * logged out, {@code null} will be sent to all registered {@code Observer}s.
 * 
 * @author Tobias Schlecht
 */
public final class AuthenticationRegistry extends AbstractObservable<UserSession> implements Manager
{
	private final RichWebClientApplication application;
	private final CoreService coreService;

	private UserSession userSession = null;

	/**
	 * The constructor is {@code private} to have strict control what instances
	 * exist at any time. Instead of the constructor the {@code public}
	 * <i>static factory method</i>
	 * {@link #getInstance(RichWebClientApplication)} returns the instances of
	 * the class.
	 * 
	 * @param application The corresponding {@code RichWebClientApplication}
	 */
	private AuthenticationRegistry(RichWebClientApplication application)
	{
		this.application = application;

		coreService = application.getCoreService();
	}

	/**
	 * Returns an {@link AuthenticationRegistry} instance for the given
	 * {@link RichWebClientApplication}.
	 * 
	 * @param application The corresponding {@code RichWebClientApplication}
	 * @return An {@code AuthenticationRegistry} instance
	 */
	public static synchronized AuthenticationRegistry getInstance(RichWebClientApplication application)
	{
		return new AuthenticationRegistry(application);
	}

	@Override
	public RichWebClientApplication getApplication()
	{
		return application;
	}

	/**
	 * Determines whether there is a logged in {@link User} or not.
	 * 
	 * @return {@code true} if a {@code User} is logged in; {@code false} if
	 *         there is no logged in {@code User}
	 */
	public boolean isUserLoggedIn()
	{
		if(userSession == null)
		{
			return false;
		}

		return true;
	}

	/**
	 * Returns the {@link UserSession} of the logged in {@link User}.
	 * 
	 * @return The {@code UserSession} of the logged in {@code User} or
	 *         {@code null} if no {@code User} is logged in.
	 */
	public UserSession getActiveUserSession()
	{
		return userSession;
	}

	/**
	 * Executes the login procedure with the given data and returns the created
	 * {@link UserSession} for this authentication or {@code null} if there is
	 * no {@link User} with the given data. <br/><br/>
	 * 
	 * <i>Important:</i> For one {@link RichWebClientApplication} it is only
	 * possible that one {@link User} is logged in. Therefore the ratio between
	 * {@code RichWebClientApplication} and {@code UserSession} is always
	 * {@code 1:1}.
	 * 
	 * @param emailAddress The Email address of the {@code User}
	 * @param password The password of the {@code User}
	 * @return The active {@code UserSession} or {@code null} if there is no
	 *         {@link User} with the given data.
	 */
	public synchronized UserSession login(String emailAddress, String password)
	{
		if(!isUserLoggedIn())
		{
			User user = coreService.getUser(emailAddress, password);

			if(user == null)
			{
				return null;
			}

			userSession = coreService.getUserSession(user);

			notifyObservers(userSession);

			return userSession;
		}
		else
		{
			throw new IllegalStateException("There is already a logged in User.");
		}
	}

	/**
	 * Executes the logout procedure for the logged in {@link User} (if there is
	 * a logged in {@code User}). <br/><br/>
	 * 
	 * <i>Important:</i> For one {@link RichWebClientApplication} it is only
	 * possible that one {@link User} is logged in. Therefore the ratio between
	 * {@code RichWebClientApplication} and {@code UserSession} is always
	 * {@code 1:1}.
	 */
	public synchronized void logout()
	{
		userSession = null;

		notifyObservers(null);
	}

	/**
	 * Replaces the current active {@link UserSession} with the given
	 * {@code UserSession}.
	 * 
	 * @param userSession The new up-to-date {@code UserSession}
	 */
	public synchronized void updateUserSession(UserSession userSession)
	{
		this.userSession = userSession;

		notifyObservers(userSession);
	}
}
