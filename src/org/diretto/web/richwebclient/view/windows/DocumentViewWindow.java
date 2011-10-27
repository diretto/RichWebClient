package org.diretto.web.richwebclient.view.windows;

import org.diretto.api.client.base.data.ResultSet;
import org.diretto.api.client.base.types.LoadType;
import org.diretto.api.client.external.processing.ProcessingService;
import org.diretto.api.client.external.processing.ProcessingService.Fixed;
import org.diretto.api.client.main.core.CoreService;
import org.diretto.api.client.main.core.entities.Attachment;
import org.diretto.api.client.main.core.entities.Document;
import org.diretto.api.client.main.core.entities.DocumentID;
import org.diretto.api.client.main.core.entities.Tag;
import org.diretto.api.client.main.core.entities.TagID;
import org.diretto.api.client.main.core.entities.Time;
import org.diretto.api.client.user.UserID;
import org.diretto.api.client.util.HTMLUtils;
import org.diretto.web.richwebclient.management.AuthenticationRegistry;
import org.diretto.web.richwebclient.view.util.StyleUtils;

import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.themes.Reindeer;

/**
 * A {@code DocumentViewWindow} is responsible for offering and managing the
 * graphical user interface in respect of displaying a {@link Document}.
 * 
 * @author Tobias Schlecht
 */
public final class DocumentViewWindow extends Window
{
	private static final long serialVersionUID = 2684148931427600545L;

	/**
	 * Constructs a {@link DocumentViewWindow}.
	 * 
	 * @param mainWindow The corresponding {@code MainWindow}
	 * @param coreService The corresponding {@code CoreService}
	 * @param processingService The corresponding {@code ProcessingService}
	 * @param authenticationRegistry The corresponding
	 *        {@code AuthenticationRegistry}
	 * @param documentID A {@code DocumentID}
	 */
	public DocumentViewWindow(final MainWindow mainWindow, CoreService coreService, ProcessingService processingService, AuthenticationRegistry authenticationRegistry, DocumentID documentID)
	{
		super("Document View");

		final Document document = coreService.getDocument(documentID, LoadType.COMPLETE, false);
		final Attachment initialAttachment = document.getInitialAttachment();

		setModal(true);
		setStyleName(Reindeer.WINDOW_BLACK);
		setWidth("940px");
		setHeight((((int) mainWindow.getHeight()) - 160) + "px");
		setResizable(false);
		setClosable(true);
		setDraggable(false);
		setCloseShortcut(KeyCode.ESCAPE, null);
		setPositionX((int) (mainWindow.getWidth() / 2.0f) - 470);
		setPositionY(126);

		HorizontalLayout wrapperLayout = new HorizontalLayout();
		wrapperLayout.setSizeFull();
		wrapperLayout.setMargin(true, true, true, true);
		addComponent(wrapperLayout);

		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		mainLayout.setSpacing(true);
		wrapperLayout.addComponent(mainLayout);

		VerticalLayout titleLayout = new VerticalLayout();
		titleLayout.setMargin(false, false, true, false);
		mainLayout.addComponent(titleLayout);

		Label titleLabel = StyleUtils.getLabelH2(document.getTitle());
		titleLabel.setSizeUndefined();
		titleLayout.addComponent(titleLabel);
		titleLayout.setComponentAlignment(titleLabel, Alignment.TOP_CENTER);

		VerticalLayout initialAttachmentEmbeddedLayout = new VerticalLayout();
		initialAttachmentEmbeddedLayout.setHeight("350px");
		mainLayout.addComponent(initialAttachmentEmbeddedLayout);

		Embedded initialAttachmentEmbedded;

		if(document.getMediaMainType().TYPE.equals("image"))
		{
			String resizedResourceURL = processingService.getImageURL(documentID, 350, Fixed.HEIGHT).toExternalForm();

			// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

			// TODO Remove code line when the Processing Service is implemented

			resizedResourceURL = initialAttachment.getFileURL().toExternalForm();

			// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

			ExternalResource resizedExternalResource = new ExternalResource(resizedResourceURL);

			initialAttachmentEmbedded = new Embedded(null, resizedExternalResource);

			initialAttachmentEmbedded.setHeight("100%");
		}
		else
		{
			String thumbnailResourceURL = processingService.getThumbnailURL(documentID, 256).toExternalForm();

			ExternalResource thumbnailExternalResource = new ExternalResource(thumbnailResourceURL);

			initialAttachmentEmbedded = new Embedded(null, thumbnailExternalResource);

			initialAttachmentEmbedded.setWidth("256px");
			initialAttachmentEmbedded.setHeight("256px");
		}

		initialAttachmentEmbedded.setType(Embedded.TYPE_IMAGE);
		initialAttachmentEmbedded.addStyleName("pointer-cursor");
		initialAttachmentEmbedded.setImmediate(true);

		initialAttachmentEmbedded.addListener(new ClickListener()
		{
			private static final long serialVersionUID = -7015150995736183718L;

			@Override
			public void click(ClickEvent event)
			{
				if(document.getMediaMainType().TYPE.equals("image") && event.getButton() == ClickEvent.BUTTON_LEFT)
				{
					String fullSizeResourceURL = initialAttachment.getFileURL().toExternalForm();

					ExternalResource fullSizeExternalResource = new ExternalResource(fullSizeResourceURL);

					Embedded embedded = new Embedded(null, fullSizeExternalResource);

					embedded.setType(Embedded.TYPE_IMAGE);
					embedded.setImmediate(true);

					FullScreenViewWindow fullScreenViewWindow = new FullScreenViewWindow(mainWindow, document.getTitle(), embedded);

					mainWindow.addWindow(fullScreenViewWindow);
					fullScreenViewWindow.focus();
				}
			}
		});

		initialAttachmentEmbeddedLayout.addComponent(initialAttachmentEmbedded);
		initialAttachmentEmbeddedLayout.setComponentAlignment(initialAttachmentEmbedded, Alignment.TOP_CENTER);

		VerticalLayout horizontalLine1Layout = new VerticalLayout();
		horizontalLine1Layout.setMargin(true, false, true, false);
		mainLayout.addComponent(horizontalLine1Layout);
		horizontalLine1Layout.addComponent(StyleUtils.getHorizontalLine());

		HorizontalSplitPanel basicInfoPanel = new HorizontalSplitPanel();
		basicInfoPanel.setWidth("100%");
		basicInfoPanel.setStyleName(Reindeer.SPLITPANEL_SMALL);
		basicInfoPanel.setSplitPosition(50);
		basicInfoPanel.setLocked(true);
		mainLayout.addComponent(basicInfoPanel);

		String description = document.getDescription().trim();

		VerticalLayout basicInfoLeftLayout = new VerticalLayout();

		if(description.equals(""))
		{
			basicInfoLeftLayout.setHeight("110px");
		}
		else
		{
			description = HTMLUtils.escapeHTML(description, true);

			basicInfoLeftLayout.setHeight("180px");
		}

		basicInfoLeftLayout.setSpacing(true);
		basicInfoLeftLayout.setMargin(false, true, false, false);
		basicInfoPanel.addComponent(basicInfoLeftLayout);

		UserID publisher = document.getPublisher();

		HorizontalLayout documentIDLayout = new HorizontalLayout();
		documentIDLayout.addComponent(StyleUtils.getLabelBoldHTML("Document ID:&nbsp;&nbsp;"));
		documentIDLayout.addComponent(StyleUtils.getLabel(documentID.getResourceIdentifier()));
		basicInfoLeftLayout.addComponent(documentIDLayout);
		basicInfoLeftLayout.setComponentAlignment(documentIDLayout, Alignment.TOP_LEFT);

		HorizontalLayout publisherLayout = new HorizontalLayout();
		publisherLayout.addComponent(StyleUtils.getLabelBoldHTML("Publisher:&nbsp;&nbsp;"));
		publisherLayout.addComponent(StyleUtils.getLabel(coreService.getUserInfo(publisher).getUserName()));
		basicInfoLeftLayout.addComponent(publisherLayout);
		basicInfoLeftLayout.setComponentAlignment(publisherLayout, Alignment.TOP_LEFT);

		HorizontalLayout publishingTimeLayout = new HorizontalLayout();
		publishingTimeLayout.addComponent(StyleUtils.getLabelBoldHTML("Publishing Time:&nbsp;&nbsp;"));
		publishingTimeLayout.addComponent(StyleUtils.getLabelHTML(Time.BIG_DISTANCE_HTML_DATE_TIME_FORMATTER.print(document.getPublishingTime())));
		basicInfoLeftLayout.addComponent(publishingTimeLayout);
		basicInfoLeftLayout.setComponentAlignment(publishingTimeLayout, Alignment.TOP_LEFT);

		HorizontalLayout mediaTypeLayout = new HorizontalLayout();
		mediaTypeLayout.addComponent(StyleUtils.getLabelBoldHTML("Media Type:&nbsp;&nbsp;"));
		mediaTypeLayout.addComponent(StyleUtils.getLabel(initialAttachment.getMediaType().toString()));
		basicInfoLeftLayout.addComponent(mediaTypeLayout);
		basicInfoLeftLayout.setComponentAlignment(mediaTypeLayout, Alignment.TOP_LEFT);
		basicInfoLeftLayout.setExpandRatio(mediaTypeLayout, 1.0f);

		VerticalLayout basicInfoRightLayout = new VerticalLayout();
		basicInfoRightLayout.setMargin(false, false, false, true);
		basicInfoRightLayout.setSpacing(true);
		basicInfoPanel.addComponent(basicInfoRightLayout);

		ResultSet<TagID, Tag> tags = document.getTags();

		TabSheet tagTabSheet = null;

		if(!tags.isEmpty())
		{
			tagTabSheet = new TabSheet();
			tagTabSheet.setStyleName(Reindeer.TABSHEET_SMALL);
			tagTabSheet.addStyleName("view");

			for(Tag tag : tags)
			{
				Tab tab = tagTabSheet.addTab(new Label(), tag.getValue(), null);
				tab.setClosable(false);
			}
		}

		VerticalLayout tagsLayout = new VerticalLayout();
		tagsLayout.setSpacing(true);
		Label tagsLabel = StyleUtils.getLabelBoldHTML("Tags:");
		tagsLayout.addComponent(tagsLabel);
		basicInfoRightLayout.addComponent(tagsLayout);

		if(tags.isEmpty())
		{
			tagsLayout.addComponent(StyleUtils.getLabelItalicHTML("No tags available"));
		}
		else
		{
			tagsLayout.addComponent(tagTabSheet);
			basicInfoRightLayout.addComponent(StyleUtils.getVerticalSpaceSmall());
		}

		basicInfoRightLayout.addComponent(StyleUtils.getLabelBold("Description:"));
		Panel descriptionPanel = new Panel();
		((AbstractLayout) descriptionPanel.getContent()).setMargin(false, true, false, false);
		descriptionPanel.setStyleName(Reindeer.PANEL_LIGHT);
		descriptionPanel.setWidth("100%");

		Label descriptionLabel = null;

		if(description.equals(""))
		{
			descriptionLabel = StyleUtils.getLabelItalicHTML("No description available");
		}
		else
		{
			descriptionLabel = StyleUtils.getLabelHTML(description);

			descriptionPanel.setHeight("90px");
		}

		descriptionPanel.setScrollable(true);
		descriptionPanel.addComponent(descriptionLabel);
		basicInfoRightLayout.addComponent(descriptionPanel);
	}
}
