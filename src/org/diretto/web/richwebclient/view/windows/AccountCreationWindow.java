package org.diretto.web.richwebclient.view.windows;

import org.diretto.api.client.main.core.CoreService;
import org.diretto.api.client.session.UserSession;
import org.diretto.api.client.user.UserID;
import org.diretto.web.richwebclient.management.AuthenticationRegistry;
import org.diretto.web.richwebclient.view.util.StyleUtils;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Reindeer;

/**
 * An {@code AccountCreationWindow} is responsible for offering and managing the
 * graphical user interface in respect of the account creation procedure.
 * 
 * @author Tobias Schlecht
 */
public final class AccountCreationWindow extends Window
{
	private static final long serialVersionUID = -5688617635497913455L;

	private boolean isErrorDisplayed = false;

	private HorizontalLayout wrapperLayout;
	private VerticalLayout mainLayout;
	private HorizontalLayout errorLayout;

	/**
	 * Constructs an {@link AccountCreationWindow}.
	 * 
	 * @param mainWindow The corresponding {@code MainWindow}
	 * @param coreService The corresponding {@code CoreService}
	 * @param authenticationRegistry The corresponding
	 *        {@code AuthenticationRegistry}
	 */
	public AccountCreationWindow(final MainWindow mainWindow, final CoreService coreService, final AuthenticationRegistry authenticationRegistry)
	{
		super("Account Creation");

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

		Label userLabel = StyleUtils.getLabelBold("Username");
		mainLayout.addComponent(userLabel);

		final TextField userField = new TextField();
		userField.setWidth("175px");
		userField.setImmediate(true);
		userField.focus();
		mainLayout.addComponent(userField);

		mainLayout.addComponent(StyleUtils.getVerticalSpaceSmall());

		Label emailLabel = StyleUtils.getLabelBold("Email Address");
		mainLayout.addComponent(emailLabel);

		final TextField emailField = new TextField();
		emailField.setWidth("175px");
		emailField.setImmediate(true);
		mainLayout.addComponent(emailField);

		mainLayout.addComponent(StyleUtils.getVerticalSpaceSmall());

		Label password1Label = StyleUtils.getLabelBold("Password (min. 6 chars)");
		mainLayout.addComponent(password1Label);

		final PasswordField password1Field = new PasswordField();
		password1Field.setWidth("175px");
		password1Field.setImmediate(true);
		mainLayout.addComponent(password1Field);

		mainLayout.addComponent(StyleUtils.getVerticalSpaceSmall());

		Label password2lLabel = StyleUtils.getLabelBold("Confirm Password");
		mainLayout.addComponent(password2lLabel);

		final PasswordField password2Field = new PasswordField();
		password2Field.setWidth("175px");
		password2Field.setImmediate(true);
		mainLayout.addComponent(password2Field);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setMargin(true, false, false, false);
		buttonLayout.setSpacing(false);

		HorizontalLayout createButtonLayout = new HorizontalLayout();
		createButtonLayout.setMargin(false, true, false, false);

		Button createButton = new Button("Create", new Button.ClickListener()
		{
			private static final long serialVersionUID = 6065147826011280540L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				String userValue = userField.getValue().toString().trim();
				String emailValue = emailField.getValue().toString().trim();
				String password1Value = password1Field.getValue().toString().trim();
				String password2Value = password2Field.getValue().toString().trim();

				EmailValidator emailValidator = new EmailValidator("");

				boolean isEmailValid = emailValidator.isValid(emailField.getValue().toString().trim());

				if(userValue.equals("") || emailValue.equals("") || password1Value.equals("") || password2Value.equals(""))
				{
					setErrorMessage("All fields are required.");
				}
				else if(userValue.length() < 5 || userValue.length() > 40)
				{
					setErrorMessage("The Username you have entered is<br />not valid (min. 5 chars).");
				}
				else if(!isEmailValid || emailValue.length() > 50 || password1Value.length() < 6 || password1Value.length() > 40 || password1Value.length() != password1Field.getValue().toString().length())
				{
					setErrorMessage("The Email Address or the Password<br />you have entered is not valid.");
				}
				else if(!password1Value.equals(password2Field.getValue().toString()))
				{
					setErrorMessage("The Password fields do not match.");
				}
				else if(coreService.hasUser(emailValue))
				{
					setErrorMessage("The entered Email Address has<br />already been assigned.");
				}
				else
				{
					UserID userID = coreService.createUser(emailValue, password1Value, userValue);

					if(userID != null)
					{
						UserSession userSession = authenticationRegistry.login(emailValue, password1Value);

						if(userSession != null)
						{
							mainWindow.removeWindow(event.getButton().getWindow());
							mainWindow.createLoggedInLayout();

							ConfirmWindow confirmWindow = new ConfirmWindow(mainWindow, "Account Creation", StyleUtils.getLabelHTML("The creation of your new Account was successful."));
							mainWindow.addWindow(confirmWindow);
						}
						else
						{
							mainWindow.removeWindow(event.getButton().getWindow());

							ConfirmWindow confirmWindow = new ConfirmWindow(mainWindow, "Account Creation", StyleUtils.getLabelHTML("The creation of your new Account failed because of an unknown error."));
							mainWindow.addWindow(confirmWindow);
						}
					}
					else
					{
						mainWindow.removeWindow(event.getButton().getWindow());

						ConfirmWindow confirmWindow = new ConfirmWindow(mainWindow, "Account Creation", StyleUtils.getLabelHTML("The creation of your new Account failed because of an unknown error."));
						mainWindow.addWindow(confirmWindow);
					}
				}
			}
		});

		createButton.setStyleName(Reindeer.BUTTON_DEFAULT);
		createButtonLayout.addComponent(createButton);
		buttonLayout.addComponent(createButtonLayout);

		Button cancelButton = new Button("Cancel", new Button.ClickListener()
		{
			private static final long serialVersionUID = -7806319491559470218L;

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
		setPositionY((int) (mainWindow.getHeight() / 3.0f) - 150);
	}

	/**
	 * Sets the specified error {@code message} to this
	 * {@link AccountCreationWindow}.
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

		mainLayout.addComponent(errorLayout, 11);

		wrapperLayout.setMargin(true, false, true, true);
	}
}
