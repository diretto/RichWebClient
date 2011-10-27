package org.diretto.web.richwebclient.view.windows;

import org.diretto.api.client.user.User;
import org.diretto.web.richwebclient.management.AuthenticationRegistry;

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
 * A {@code ConfirmWindow} serves as an information {@link Window} for the
 * {@link User}.
 * 
 * @author Tobias Schlecht
 */
public class ConfirmWindow extends Window
{
	private static final long serialVersionUID = 6366840696495315065L;

	private final Window parentWindow;

	/**
	 * Constructs a {@link ConfirmWindow}.
	 * 
	 * @param parentWindow The parent {@code Window} to which the
	 *        {@code ConfirmWindow} will be set
	 * @param caption The title of the {@code ConfirmWindow}
	 * @param message The message {@code Label} to be displayed
	 */
	public ConfirmWindow(final Window parentWindow, String caption, Label message)
	{
		this(parentWindow, caption, message, false, null);
	}

	/**
	 * Constructs a {@link ConfirmWindow}.
	 * 
	 * @param parentWindow The parent {@code Window} to which the
	 *        {@code ConfirmWindow} will be set
	 * @param caption The title of the {@code ConfirmWindow}
	 * @param message The message {@code Label} to be displayed
	 * @param proceedToLogin {@code true} if after the confirmation, a
	 *        {@code LoginWindow} should be opened; otherwise {@code false}
	 * @param authenticationRegistry The corresponding
	 *        {@code AuthenticationRegistry}
	 */
	public ConfirmWindow(final Window parentWindow, String caption, Label message, boolean proceedToLogin, final AuthenticationRegistry authenticationRegistry)
	{
		super(caption);

		this.parentWindow = parentWindow;

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

		VerticalLayout messageLayout = new VerticalLayout();
		messageLayout.setMargin(false, false, true, false);

		messageLayout.addComponent(message);

		mainLayout.addComponent(messageLayout);
		mainLayout.setComponentAlignment(messageLayout, Alignment.MIDDLE_CENTER);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setMargin(true, false, false, false);

		Button confirmButton = null;

		if(!proceedToLogin)
		{
			confirmButton = new Button("OK", new Button.ClickListener()
			{
				private static final long serialVersionUID = -7343876145850931143L;

				@Override
				public void buttonClick(ClickEvent event)
				{
					parentWindow.removeWindow(event.getButton().getWindow());
				}
			});
		}
		else
		{
			confirmButton = new Button("OK", new Button.ClickListener()
			{
				private static final long serialVersionUID = -7343876145850931143L;

				@Override
				public void buttonClick(ClickEvent event)
				{
					parentWindow.removeWindow(event.getButton().getWindow());

					LoginWindow loginWindow = new LoginWindow((MainWindow) parentWindow, authenticationRegistry);

					parentWindow.addWindow(loginWindow);
				}
			});
		}

		confirmButton.setStyleName(Reindeer.BUTTON_DEFAULT);
		confirmButton.focus();
		buttonLayout.addComponent(confirmButton);

		mainLayout.addComponent(buttonLayout);
		mainLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);

		wrapperLayout.addComponent(mainLayout);

		addComponent(wrapperLayout);

		setPositionX((int) (parentWindow.getWidth() / 2.0f) - 125);
		setPositionY((int) (parentWindow.getHeight() / 3.0f) - 125);
	}

	@Override
	public void setWidth(String width)
	{
		super.setWidth(width);

		setPositionX((int) (parentWindow.getWidth() / 2.0f) - (int) (super.getWidth() / 2.0f));
	}
}
