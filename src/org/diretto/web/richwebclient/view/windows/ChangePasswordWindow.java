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
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Reindeer;

/**
 * A {@code ChangePasswordWindow} is responsible for offering and managing the
 * graphical user interface in case a {@link User} wants to change the password.
 * 
 * @author Tobias Schlecht
 */
public final class ChangePasswordWindow extends Window
{
	private static final long serialVersionUID = 670991212287110756L;

	private boolean isErrorDisplayed = false;

	private HorizontalLayout wrapperLayout;
	private VerticalLayout mainLayout;
	private HorizontalLayout errorLayout;

	/**
	 * Constructs a {@link ChangePasswordWindow}.
	 * 
	 * @param mainWindow The corresponding {@code MainWindow}
	 * @param coreService The corresponding {@code CoreService}
	 * @param authenticationRegistry The corresponding
	 *        {@code AuthenticationRegistry}
	 */
	public ChangePasswordWindow(final MainWindow mainWindow, final CoreService coreService, final AuthenticationRegistry authenticationRegistry)
	{
		super("Change Password");

		final UserSession userSession = authenticationRegistry.getActiveUserSession();

		setModal(true);
		setStyleName(Reindeer.WINDOW_BLACK);
		setWidth("250px");
		setResizable(false);
		setClosable(false);
		setDraggable(false);
		setCloseShortcut(KeyCode.ESCAPE, null);

		wrapperLayout = new HorizontalLayout();
		wrapperLayout.setSizeFull();
		wrapperLayout.setMargin(true, true, true, true);

		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		mainLayout.setSpacing(true);

		Label oldPasswordLabel = StyleUtils.getLabelBold("Old Password");
		mainLayout.addComponent(oldPasswordLabel);

		final PasswordField oldPasswordField = new PasswordField();
		oldPasswordField.setWidth("175px");
		oldPasswordField.setImmediate(true);
		oldPasswordField.focus();
		mainLayout.addComponent(oldPasswordField);

		mainLayout.addComponent(StyleUtils.getVerticalSpaceSmall());

		Label newPassword1Label = StyleUtils.getLabelBold("New Password (min. 6 chars)");
		mainLayout.addComponent(newPassword1Label);

		final PasswordField newPassword1Field = new PasswordField();
		newPassword1Field.setWidth("175px");
		newPassword1Field.setImmediate(true);
		mainLayout.addComponent(newPassword1Field);

		mainLayout.addComponent(StyleUtils.getVerticalSpaceSmall());

		Label newPassword2lLabel = StyleUtils.getLabelBold("Confirm new Password");
		mainLayout.addComponent(newPassword2lLabel);

		final PasswordField newPassword2Field = new PasswordField();
		newPassword2Field.setWidth("175px");
		newPassword2Field.setImmediate(true);
		mainLayout.addComponent(newPassword2Field);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setMargin(true, false, false, false);
		buttonLayout.setSpacing(false);

		HorizontalLayout changeButtonLayout = new HorizontalLayout();
		changeButtonLayout.setMargin(false, true, false, false);

		Button changeButton = new Button("Change", new Button.ClickListener()
		{
			private static final long serialVersionUID = -2541110995627121483L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				String oldPasswordValue = oldPasswordField.getValue().toString().trim();
				String newPassword1Value = newPassword1Field.getValue().toString().trim();
				String newPassword2Value = newPassword2Field.getValue().toString().trim();

				if(oldPasswordValue.equals("") || newPassword1Value.equals("") || newPassword2Value.equals(""))
				{
					setErrorMessage("All fields are required.");
				}
				else if(!userSession.getUser().verifyPassword(oldPasswordField.getValue().toString()))
				{
					setErrorMessage("The old Password is not correct.");
				}
				else if(newPassword1Value.length() < 6 || newPassword1Value.length() > 40 || newPassword1Value.length() != newPassword1Field.getValue().toString().length())
				{
					setErrorMessage("The new Password you have<br />entered is not valid.");
				}
				else if(!newPassword1Value.equals(newPassword2Field.getValue().toString()))
				{
					setErrorMessage("The Password fields do not match.");
				}
				else
				{
					boolean wasSuccessful = userSession.getUser().changePassword(userSession, newPassword1Value);

					mainWindow.removeWindow(event.getButton().getWindow());

					ConfirmWindow confirmWindow;

					if(wasSuccessful)
					{
						confirmWindow = new ConfirmWindow(mainWindow, "Change Password", StyleUtils.getLabelHTML("The change of your Password was successful."));
					}
					else
					{
						confirmWindow = new ConfirmWindow(mainWindow, "Change Password", StyleUtils.getLabelHTML("The change of your Password failed because of an unknown error."));
					}

					mainWindow.addWindow(confirmWindow);

					// Destroy the validity of the old password
					// (on the API server)

					coreService.getUser(userSession.getUser().getEmailAddress(), newPassword1Value);
				}
			}
		});

		changeButton.setStyleName(Reindeer.BUTTON_DEFAULT);
		changeButtonLayout.addComponent(changeButton);
		buttonLayout.addComponent(changeButtonLayout);

		Button cancelButton = new Button("Cancel", new Button.ClickListener()
		{
			private static final long serialVersionUID = -4626058880055750554L;

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

	/**
	 * Sets the specified error {@code message} to this
	 * {@link ChangePasswordWindow}.
	 * 
	 * @param message The error message to be set
	 */
	private void setErrorMessage(String message)
	{
		if(!isErrorDisplayed)
		{
			isErrorDisplayed = true;
		}
		else
		{
			mainLayout.removeComponent(errorLayout);
		}

		errorLayout = new HorizontalLayout();
		errorLayout.setMargin(true, false, false, false);

		Label errorMessage = StyleUtils.getLabelSmallHTML(message);
		errorLayout.addComponent(errorMessage);

		mainLayout.addComponent(errorLayout, 8);

		wrapperLayout.setMargin(true, false, true, true);
	}
}
