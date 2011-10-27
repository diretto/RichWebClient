package org.diretto.web.richwebclient.view.windows;

import org.diretto.web.richwebclient.view.util.StyleUtils;

import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

/**
 * A {@code FullScreenViewWindow} is responsible for displaying {@link Embedded}
 * objects on a full screen {@link Window}.
 * 
 * @author Tobias Schlecht
 */
public final class FullScreenViewWindow extends Window
{
	private static final long serialVersionUID = -5840805251763801711L;

	/**
	 * Constructs a {@link FullScreenViewWindow}.
	 * 
	 * @param mainWindow The corresponding {@code MainWindow}
	 * @param embedded An {@code Embedded} object
	 */
	public FullScreenViewWindow(MainWindow mainWindow, Embedded embedded)
	{
		this(mainWindow, null, embedded);
	}

	/**
	 * Constructs a {@link FullScreenViewWindow}.
	 * 
	 * @param mainWindow The corresponding {@code MainWindow}
	 * @param caption The title
	 * @param embedded An {@code Embedded} object
	 */
	public FullScreenViewWindow(MainWindow mainWindow, String title, Embedded embedded)
	{
		super();

		setModal(true);
		setStyleName(Reindeer.WINDOW_BLACK);
		setSizeFull();
		setScrollable(true);
		setResizable(false);
		setClosable(true);
		setDraggable(false);
		setCloseShortcut(KeyCode.ESCAPE, null);

		addListener(new ClickListener()
		{
			private static final long serialVersionUID = -7015150995736183718L;

			@Override
			public void click(ClickEvent event)
			{
				if(event.getButton() == ClickEvent.BUTTON_LEFT)
				{
					Window mainWindow = FullScreenViewWindow.this.getApplication().getMainWindow();

					mainWindow.removeWindow(FullScreenViewWindow.this);
				}
			}
		});

		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setSizeUndefined();
		setContent(mainLayout);

		VerticalLayout titleLayout = new VerticalLayout();
		titleLayout.setWidth(mainWindow.getWidth() - 80 + "px");
		mainLayout.addComponent(titleLayout);

		if(title != null)
		{
			titleLayout.setMargin(false, false, true, false);

			Label titleLabel = StyleUtils.getLabelH2(title);
			titleLabel.setSizeUndefined();
			titleLayout.addComponent(titleLabel);
			titleLayout.setComponentAlignment(titleLabel, Alignment.TOP_CENTER);
		}
		else
		{
			titleLayout.setMargin(false);
		}

		HorizontalLayout resourceLayout = new HorizontalLayout();
		resourceLayout.setSizeUndefined();
		mainLayout.addComponent(resourceLayout);
		mainLayout.setComponentAlignment(resourceLayout, Alignment.TOP_CENTER);

		resourceLayout.addComponent(embedded);
	}
}
