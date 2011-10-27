package org.diretto.web.richwebclient.view.sections;

import org.diretto.api.client.main.core.CoreService;
import org.diretto.api.client.session.UserSession;
import org.diretto.api.client.user.User;
import org.diretto.api.client.util.Observable;
import org.diretto.web.richwebclient.RichWebClientApplication;
import org.diretto.web.richwebclient.view.base.AbstractSection;
import org.diretto.web.richwebclient.view.base.Section;
import org.diretto.web.richwebclient.view.util.StyleUtils;
import org.diretto.web.richwebclient.view.windows.ChangePasswordWindow;
import org.diretto.web.richwebclient.view.windows.ChangeUserNameWindow;
import org.diretto.web.richwebclient.view.windows.MainWindow;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Reindeer;

/**
 * This class represents a {@code ProfileSection}.
 * 
 * @author Tobias Schlecht
 */
public final class ProfileSection extends AbstractSection
{
	private static final long serialVersionUID = -4270456540351375412L;

	private CoreService coreService;

	private boolean componentsAdded = false;

	private Embedded profilePic;
	private Button changeUsernameButton;
	private Button changePasswordButton;

	/**
	 * Constructs a {@link ProfileSection}.
	 * 
	 * @param application The corresponding {@code RichWebClientApplication}
	 */
	public ProfileSection(RichWebClientApplication application)
	{
		super(application.getAuthenticationRegistry(), true, false, "Profile", "Edit your profile");

		coreService = application.getCoreService();
	}

	@Override
	public synchronized void addComponents()
	{
		if(!componentsAdded)
		{
			changeUsernameButton = new Button("Change Username", new Button.ClickListener()
			{
				private static final long serialVersionUID = -7469802704183136435L;

				@Override
				public void buttonClick(ClickEvent event)
				{
					ChangeUserNameWindow changeUserNameWindow = new ChangeUserNameWindow((MainWindow) changeUsernameButton.getWindow(), coreService, authenticationRegistry);

					getWindow().addWindow(changeUserNameWindow);
				}
			});

			changeUsernameButton.setStyleName(Reindeer.BUTTON_DEFAULT);

			changePasswordButton = new Button("Change Password", new Button.ClickListener()
			{
				private static final long serialVersionUID = -537354528200927683L;

				@Override
				public void buttonClick(ClickEvent event)
				{
					ChangePasswordWindow changePasswordWindow = new ChangePasswordWindow((MainWindow) changePasswordButton.getWindow(), coreService, authenticationRegistry);

					getWindow().addWindow(changePasswordWindow);
				}
			});

			changePasswordButton.setStyleName(Reindeer.BUTTON_DEFAULT);

			loadComponents(authenticationRegistry.getActiveUserSession());

			componentsAdded = true;
		}
	}

	/**
	 * Loads the content of this {@link Section}.
	 * 
	 * @param userSession The active {@code UserSession}
	 */
	private void loadComponents(UserSession userSession)
	{
		removeAllComponents();

		if(userSession != null)
		{
			User user = userSession.getUser();

			addComponent(StyleUtils.getLabelH1(title));

			VerticalLayout profileLayout = new VerticalLayout();
			profileLayout.setStyleName(Reindeer.LAYOUT_BLACK);
			profileLayout.setMargin(true);
			profileLayout.setSpacing(true);

			profileLayout.addComponent(StyleUtils.getLabelH2("Your Account Details"));
			profileLayout.addComponent(StyleUtils.getHorizontalLine());
			profileLayout.addComponent(profilePic);
			profileLayout.addComponent(StyleUtils.getHorizontalLine());
			profileLayout.addComponent(StyleUtils.getLabelBold("Username"));
			profileLayout.addComponent(StyleUtils.getLabel(user.getUserInfo().getUserName()));
			profileLayout.addComponent(changeUsernameButton);
			profileLayout.addComponent(StyleUtils.getHorizontalLine());
			profileLayout.addComponent(StyleUtils.getLabelBold("User ID"));
			profileLayout.addComponent(StyleUtils.getLabel(user.getAuthID()));
			profileLayout.addComponent(StyleUtils.getHorizontalLine());
			profileLayout.addComponent(StyleUtils.getLabelBold("Email Address"));
			profileLayout.addComponent(new Link(user.getEmailAddress(), new ExternalResource("mailto:" + user.getEmailAddress())));
			profileLayout.addComponent(StyleUtils.getHorizontalLine());
			profileLayout.addComponent(StyleUtils.getLabelBold("Password"));
			profileLayout.addComponent(StyleUtils.getLabelSmall("NOT DISPLAYED"));
			profileLayout.addComponent(changePasswordButton);

			addComponent(profileLayout);
		}
	}

	@Override
	public void update(Observable<UserSession> observable, UserSession userSession)
	{
		if(userSession != null)
		{
			String profilePicURL = "http://www.gravatar.com/avatar/" + userSession.getUser().getAuthID() + ".jpg?s=120&r=pg&d=mm";

			profilePic = new Embedded(null, new ExternalResource(profilePicURL));

			profilePic.setType(Embedded.TYPE_IMAGE);
			profilePic.setImmediate(true);
			profilePic.setWidth("120px");
			profilePic.setHeight("120px");

			addComponent(profilePic);
		}

		if(componentsAdded)
		{
			if(userSession != null)
			{
				loadComponents(userSession);
			}
		}
	}
}
