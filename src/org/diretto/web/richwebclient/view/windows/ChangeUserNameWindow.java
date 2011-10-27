package org.diretto.web.richwebclient.view.windows;

import org.diretto.api.client.main.core.CoreService;
import org.diretto.api.client.session.UserSession;
import org.diretto.api.client.user.User;
import org.diretto.web.richwebclient.management.AuthenticationRegistry;
import org.diretto.web.richwebclient.view.util.StyleUtils;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Reindeer;

/**
 * A {@code ChangeUserNameWindow} is responsible for offering and managing the
 * graphical user interface in case a {@link User} wants to change the name.
 * 
 * @author Tobias Schlecht
 */
public final class ChangeUserNameWindow extends Window
{
	private static final long serialVersionUID = 9123561962152219657L;

	private boolean isErrorDisplayed = false;

	/**
	 * Constructs a {@link ChangeUserNameWindow}.
	 * 
	 * @param mainWindow The corresponding {@code MainWindow}
	 * @param coreService The corresponding {@code CoreService}
	 * @param authenticationRegistry The corresponding
	 *        {@code AuthenticationRegistry}
	 */
	public ChangeUserNameWindow(final MainWindow mainWindow, CoreService coreService, final AuthenticationRegistry authenticationRegistry)
	{
		super("Change Username");

		final UserSession userSession = authenticationRegistry.getActiveUserSession();

		setModal(true);
		setStyleName(Reindeer.WINDOW_BLACK);
		setWidth("250px");
		setResizable(false);
		setClosable(false);
		setDraggable(false);
		setCloseShortcut(KeyCode.ESCAPE, null);

		final HorizontalLayout wrapperLayout = new HorizontalLayout();
		wrapperLayout.setSizeFull();
		wrapperLayout.setMargin(true, true, true, true);

		final VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		mainLayout.setSpacing(true);

		Label userLabel = StyleUtils.getLabelBold("Username");
		mainLayout.addComponent(userLabel);

		final TextField userField = new TextField();
		userField.setWidth("175px");
		userField.setImmediate(true);
		userField.focus();
		userField.setValue(userSession.getUser().getUserInfo().getUserName());
		mainLayout.addComponent(userField);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setMargin(true, false, false, false);
		buttonLayout.setSpacing(false);

		HorizontalLayout changeButtonLayout = new HorizontalLayout();
		changeButtonLayout.setMargin(false, true, false, false);

		Button changeButton = new Button("Change", new Button.ClickListener()
		{
			private static final long serialVersionUID = -3172076549971672550L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				String userValue = userField.getValue().toString().trim();

				if(userValue.length() < 5 || userValue.length() > 40)
				{
					if(!isErrorDisplayed)
					{
						isErrorDisplayed = true;

						HorizontalLayout errorLayout = new HorizontalLayout();
						errorLayout.setMargin(true, false, false, false);

						Label errorMessage = StyleUtils.getLabelSmallHTML("The Username you have entered is<br />not valid (min. 5 characters).");
						errorLayout.addComponent(errorMessage);

						mainLayout.addComponent(errorLayout, 2);

						wrapperLayout.setMargin(true, false, true, true);
					}
				}
				else
				{
					boolean wasSuccessful = userSession.getUser().changeUserName(userSession, userValue);

					mainWindow.removeWindow(event.getButton().getWindow());

					authenticationRegistry.updateUserSession(userSession);
					mainWindow.updateUserName();

					ConfirmWindow confirmWindow;

					if(wasSuccessful)
					{
						confirmWindow = new ConfirmWindow(mainWindow, "Change Username", StyleUtils.getLabelHTML("The change of your Username was successful."));
					}
					else
					{
						confirmWindow = new ConfirmWindow(mainWindow, "Change Username", StyleUtils.getLabelHTML("The change of your Username failed because of an unknown error."));
					}

					mainWindow.addWindow(confirmWindow);
				}
			}
		});

		changeButton.setStyleName(Reindeer.BUTTON_DEFAULT);
		changeButtonLayout.addComponent(changeButton);
		buttonLayout.addComponent(changeButtonLayout);

		Button cancelButton = new Button("Cancel", new Button.ClickListener()
		{
			private static final long serialVersionUID = -6110554568435945904L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				mainWindow.removeWindow(event.getButton().getWindow());
			}
		});

		cancelButton.setStyleName(Reindeer.BUTTON_DEFAULT);
		buttonLayout.addComponent(cancelButton);
		buttonLayout.setComponentAlignment(cancelButton, Alignment.BOTTOM_LEFT);

		mainLayout.addComponent(buttonLayout);
		mainLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);

		wrapperLayout.addComponent(mainLayout);

		addComponent(wrapperLayout);

		setPositionX((int) (mainWindow.getWidth() / 2.0f) - 125);
		setPositionY((int) (mainWindow.getHeight() / 3.0f) - 125);
	}
}
