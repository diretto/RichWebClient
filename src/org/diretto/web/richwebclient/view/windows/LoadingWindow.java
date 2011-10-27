package org.diretto.web.richwebclient.view.windows;

import org.diretto.api.client.user.User;
import org.diretto.web.richwebclient.view.util.StyleUtils;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

/**
 * A {@code LoadingWindow} serves as a feedback {@link Window} for the
 * {@link User} during a loading process.
 * 
 * @author Tobias Schlecht
 */
public class LoadingWindow extends Window
{
	private static final long serialVersionUID = 5924501140479776672L;

	private final Window parentWindow;

	/**
	 * Constructs a {@link LoadingWindow}.
	 * 
	 * @param parentWindow The parent {@code Window} to which the
	 *        {@code LoadingWindow} will be set
	 * @param caption The title of the {@code LoadingWindow}
	 */
	public LoadingWindow(Window parentWindow, String caption)
	{
		this(parentWindow, caption, StyleUtils.getLabelBold("Loading..."));
	}

	/**
	 * Constructs a {@link LoadingWindow}.
	 * 
	 * @param parentWindow The parent {@code Window} to which the
	 *        {@code LoadingWindow} will be set
	 * @param caption The title of the {@code LoadingWindow}
	 * @param message The message {@code Label} to be displayed
	 */
	public LoadingWindow(final Window parentWindow, String caption, Label message)
	{
		super(caption);

		this.parentWindow = parentWindow;

		setModal(true);
		setStyleName(Reindeer.WINDOW_BLACK);
		setWidth("250px");
		setResizable(false);
		setClosable(false);
		setDraggable(false);

		HorizontalLayout wrapperLayout = new HorizontalLayout();
		wrapperLayout.setSizeFull();
		wrapperLayout.setMargin(true, true, true, true);

		HorizontalLayout mainLayout = new HorizontalLayout();
		mainLayout.setSizeFull();

		VerticalLayout progressLayout = new VerticalLayout();
		progressLayout.setWidth("55px");
		progressLayout.setHeight("100%");

		ProgressIndicator progressIndicator = new ProgressIndicator();
		progressIndicator.addStyleName("indeterminate-big");
		progressIndicator.setImmediate(true);
		progressIndicator.setIndeterminate(true);
		progressIndicator.setEnabled(true);

		progressLayout.addComponent(progressIndicator);
		mainLayout.addComponent(progressLayout);
		mainLayout.setComponentAlignment(progressLayout, Alignment.MIDDLE_LEFT);
		mainLayout.setExpandRatio(progressLayout, 0.0f);

		VerticalLayout messageLayout = new VerticalLayout();
		messageLayout.setHeight("100%");
		messageLayout.addComponent(message);
		mainLayout.addComponent(messageLayout);
		mainLayout.setComponentAlignment(messageLayout, Alignment.MIDDLE_LEFT);

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
