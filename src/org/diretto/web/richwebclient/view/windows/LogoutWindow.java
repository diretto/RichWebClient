package org.diretto.web.richwebclient.view.windows;

import org.diretto.web.richwebclient.management.AuthenticationRegistry;
import org.diretto.web.richwebclient.view.util.StyleUtils;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Reindeer;

/**
 * A {@code LogoutWindow} is responsible for offering and managing the graphical
 * user interface in respect of the logout procedure.
 * 
 * @author Tobias Schlecht
 */
public final class LogoutWindow extends Window
{
	private static final long serialVersionUID = -1468212220918718927L;

	/**
	 * Constructs a {@link LogoutWindow}.
	 * 
	 * @param mainWindow The corresponding {@code MainWindow}
	 * @param authenticationRegistry The corresponding
	 *        {@code AuthenticationRegistry}
	 */
	public LogoutWindow(final MainWindow mainWindow, final AuthenticationRegistry authenticationRegistry)
	{
		super("Logout");

		setModal(true);
		setStyleName(Reindeer.WINDOW_BLACK);
		setWidth("250px");
		setResizable(false);
		setClosable(false);
		setDraggable(false);
		setCloseShortcut(KeyCode.ESCAPE, null);

		HorizontalLayout wrapperLayout = new HorizontalLayout();
		wrapperLayout.setSizeFull();
		wrapperLayout.setMargin(true, true, true, true);

		VerticalLayout mainLayout = new VerticalLayout();

		HorizontalLayout questionLayout = new HorizontalLayout();
		questionLayout.setMargin(false, false, true, false);

		Label questionLabel = StyleUtils.getLabelHTML("Do you really want to log out?");
		questionLayout.addComponent(questionLabel);

		mainLayout.addComponent(questionLayout);
		mainLayout.setComponentAlignment(questionLayout, Alignment.MIDDLE_CENTER);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setMargin(true, false, false, false);
		buttonLayout.setSpacing(false);

		HorizontalLayout logoutButtonLayout = new HorizontalLayout();
		logoutButtonLayout.setMargin(false, true, false, false);

		Button logoutButton = new Button("Logout", new Button.ClickListener()
		{
			private static final long serialVersionUID = 2148556316276482811L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				mainWindow.removeWindow(event.getButton().getWindow());

				authenticationRegistry.logout();

				mainWindow.createLoggedOutLayout();
			}
		});

		logoutButton.setStyleName(Reindeer.BUTTON_DEFAULT);
		logoutButton.focus();
		logoutButtonLayout.addComponent(logoutButton);
		buttonLayout.addComponent(logoutButtonLayout);

		Button cancelButton = new Button("Cancel", new Button.ClickListener()
		{
			private static final long serialVersionUID = -7343876145850931143L;

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
