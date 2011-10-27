package org.diretto.web.richwebclient.view.windows;

import org.diretto.api.client.session.UserSession;
import org.diretto.web.richwebclient.management.AuthenticationRegistry;
import org.diretto.web.richwebclient.view.util.StyleUtils;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutListener;
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
 * A {@code LoginWindow} is responsible for offering and managing the graphical
 * user interface in respect of the login procedure.
 * 
 * @author Tobias Schlecht
 */
public final class LoginWindow extends Window
{
	private static final long serialVersionUID = 5129923854840953044L;

	private boolean isErrorDisplayed = false;

	private MainWindow mainWindow;
	private AuthenticationRegistry authenticationRegistry;

	private HorizontalLayout wrapperLayout;
	private VerticalLayout mainLayout;
	private TextField emailField;
	private PasswordField passwordField;

	/**
	 * Constructs a {@link LoginWindow}.
	 * 
	 * @param mainWindow The corresponding {@code MainWindow}
	 * @param authenticationRegistry The corresponding
	 *        {@code AuthenticationRegistry}
	 */
	public LoginWindow(final MainWindow mainWindow, AuthenticationRegistry authenticationRegistry)
	{
		super("Login");

		this.mainWindow = mainWindow;
		this.authenticationRegistry = authenticationRegistry;

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

		Label emailLabel = StyleUtils.getLabelBold("Email Address");
		mainLayout.addComponent(emailLabel);

		emailField = new TextField();
		emailField.setWidth("175px");
		emailField.setImmediate(true);
		emailField.focus();
		mainLayout.addComponent(emailField);

		mainLayout.addComponent(StyleUtils.getVerticalSpaceSmall());

		Label passwordLabel = StyleUtils.getLabelBold("Password");
		mainLayout.addComponent(passwordLabel);

		passwordField = new PasswordField();
		passwordField.setWidth("175px");
		passwordField.setImmediate(true);
		mainLayout.addComponent(passwordField);

		passwordField.addShortcutListener(new ShortcutListener(null, KeyCode.ENTER, new int[0])
		{
			private static final long serialVersionUID = -7925614431333167489L;

			@Override
			public void handleAction(Object sender, Object target)
			{
				handleLoginAction((LoginWindow) sender);
			}
		});

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setMargin(true, false, false, false);
		buttonLayout.setSpacing(false);

		HorizontalLayout loginButtonLayout = new HorizontalLayout();
		loginButtonLayout.setMargin(false, true, false, false);

		Button loginButton = new Button("Login", new Button.ClickListener()
		{
			private static final long serialVersionUID = 6065147826011280540L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleLoginAction((LoginWindow) event.getButton().getWindow());
			}
		});

		loginButton.setStyleName(Reindeer.BUTTON_DEFAULT);
		loginButtonLayout.addComponent(loginButton);
		buttonLayout.addComponent(loginButtonLayout);

		Button cancelButton = new Button("Cancel", new Button.ClickListener()
		{
			private static final long serialVersionUID = -6312983474087632470L;

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
	 * Handles the triggered login action.
	 * 
	 * @param loginWindow The {@code LoginWindow}
	 */
	private void handleLoginAction(LoginWindow loginWindow)
	{
		UserSession userSession = null;

		EmailValidator emailValidator = new EmailValidator("");

		boolean isEmailValid = emailValidator.isValid(emailField.getValue().toString().trim());

		if(!emailField.getValue().toString().trim().equals("") && !passwordField.getValue().toString().trim().equals("") && isEmailValid && passwordField.getValue().toString().trim().length() >= 6)
		{
			userSession = authenticationRegistry.login(emailField.getValue().toString(), passwordField.getValue().toString());
		}

		if(userSession != null)
		{
			mainWindow.removeWindow(loginWindow);
			mainWindow.createLoggedInLayout();
		}
		else
		{
			if(!isErrorDisplayed)
			{
				isErrorDisplayed = true;

				HorizontalLayout errorLayout = new HorizontalLayout();
				errorLayout.setMargin(true, false, false, false);

				Label errorMessage = StyleUtils.getLabelSmallHTML("The Email Address or the Password<br />you have entered is not valid.");
				errorLayout.addComponent(errorMessage);

				mainLayout.addComponent(errorLayout, 5);

				wrapperLayout.setMargin(true, false, true, true);
			}
		}
	}
}
