package org.diretto.web.richwebclient.view.base;

import org.diretto.api.client.session.UserSession;
import org.diretto.api.client.util.Observable;
import org.diretto.api.client.util.Observer;
import org.diretto.web.richwebclient.management.AuthenticationRegistry;
import org.diretto.web.richwebclient.view.util.StyleUtils;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * On the one hand this {@code abstract} class provides a skeletal
 * implementation of the {@link Section} interface, to minimize the effort
 * required to implement this interface, and on the other hand this class
 * specifies the base layout of a {@code Section}.
 * 
 * @author Tobias Schlecht
 */
public abstract class AbstractSection extends VerticalLayout implements Section, Observer<UserSession>
{
	private static final long serialVersionUID = -8460208581088647034L;

	protected final AuthenticationRegistry authenticationRegistry;
	protected final boolean loginNecessary;
	protected final boolean fullWidthRequired;
	protected final String title;
	protected final String subtitle;

	private final Label titleLabel;

	/**
	 * Provides base implementation to construct a {@link Section}.
	 * 
	 * @param authenticationRegistry The corresponding
	 *        {@code AuthenticationRegistry}
	 * @param loginNecessary {@code true} if a {@code User} has to be logged in
	 *        to view this {@code Section}; otherwise false
	 * @param fullWidthRequired {@code true} if the full width of the screen is
	 *        required; otherwise {@code false}
	 * @param title The title of this {@code Section}
	 * @param subtitle The subtitle of this {@code Section}
	 */
	public AbstractSection(AuthenticationRegistry authenticationRegistry, boolean loginNecessary, boolean fullWidthRequired, String title, String subtitle)
	{
		super();

		this.authenticationRegistry = authenticationRegistry;
		this.loginNecessary = loginNecessary;
		this.fullWidthRequired = fullWidthRequired;
		this.title = title;
		this.subtitle = subtitle;

		authenticationRegistry.addObserver(this);

		setCaption(title);

		addStyleName(Reindeer.LAYOUT_WHITE);
		addStyleName("section");
		setMargin(true);
		setSpacing(true);

		titleLabel = StyleUtils.getLabelH1(title);

		addComponent(titleLabel);
	}

	/**
	 * Removes the title {@link Component}.
	 */
	protected void removeTitleComponent()
	{
		removeComponent(titleLabel);
	}

	@Override
	public boolean isLoginNecessary()
	{
		return loginNecessary;
	}

	@Override
	public boolean isFullWidthRequired()
	{
		return fullWidthRequired;
	}

	@Override
	public String getTitle()
	{
		return title;
	}

	@Override
	public String getSubtitle()
	{
		return subtitle;
	}

	@Override
	public abstract void update(Observable<UserSession> observable, UserSession userSession);
}
