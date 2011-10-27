package org.diretto.web.richwebclient.view.sections;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.diretto.api.client.base.data.BoundingBox;
import org.diretto.api.client.base.data.ResultSet;
import org.diretto.api.client.base.data.TimeRange;
import org.diretto.api.client.base.types.LoadType;
import org.diretto.api.client.main.core.CoreService;
import org.diretto.api.client.main.core.entities.Document;
import org.diretto.api.client.main.core.entities.DocumentID;
import org.diretto.api.client.main.core.entities.Time;
import org.diretto.api.client.main.feed.FeedService;
import org.diretto.api.client.main.feed.event.DocumentListener;
import org.diretto.api.client.session.UserSession;
import org.diretto.api.client.util.Observable;
import org.diretto.web.richwebclient.RichWebClientApplication;
import org.diretto.web.richwebclient.view.base.AbstractSection;
import org.diretto.web.richwebclient.view.base.Section;
import org.diretto.web.richwebclient.view.util.ResourceUtils;
import org.diretto.web.richwebclient.view.util.StyleUtils;
import org.diretto.web.richwebclient.view.widgets.googlemap.client.base.MapType;
import org.diretto.web.richwebclient.view.widgets.googlemap.server.ExploreGoogleMap;
import org.diretto.web.richwebclient.view.widgets.googlemap.server.event.LoadingProcessListener;
import org.diretto.web.richwebclient.view.widgets.googlemap.server.event.MapSectionListener;
import org.diretto.web.richwebclient.view.windows.ConfirmWindow;
import org.diretto.web.richwebclient.view.windows.MainWindow;
import org.diretto.web.richwebclient.view.windows.event.SectionChangeListener;
import org.joda.time.DateTime;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Reindeer;

/**
 * This class represents an {@code ExploreSection}.
 * 
 * @author Tobias Schlecht
 */
public final class ExploreSection extends AbstractSection
{
	private static final long serialVersionUID = -7792913701381814698L;

	private final RichWebClientApplication application;
	private final CoreService coreService;
	private final FeedService feedService;

	private boolean componentsAdded = false;

	private MainWindow mainWindow;
	private TextField addTagField;
	private TabSheet tabSheet;
	private ExploreGoogleMap exploreGoogleMap;
	private DocumentListener documentListener;

	private Date startTime = null;
	private Date endTime = null;
	private List<String> tags = new Vector<String>();
	private BoundingBox boundingBox = null;

	/**
	 * Constructs an {@link ExploreSection}.
	 * 
	 * @param application The corresponding {@code RichWebClientApplication}
	 */
	public ExploreSection(RichWebClientApplication application)
	{
		super(application.getAuthenticationRegistry(), false, true, "Explore", "Explore the data");

		this.application = application;

		coreService = application.getCoreService();
		feedService = application.getFeedService();
	}

	@Override
	public synchronized void addComponents()
	{
		if(!componentsAdded)
		{
			startTime = new DateTime().minusDays(1).toDate();
			endTime = new DateTime().plusDays(1).toDate();

			mainWindow = (MainWindow) getWindow();

			documentListener = new DocumentListener()
			{
				@Override
				public void onDocumentAdded(DocumentID documentID)
				{
					if(startTime != null && endTime != null && boundingBox != null)
					{
						Document document = coreService.getDocument(documentID, LoadType.SNAPSHOT, false);

						if(document.isLocatedWithinAndContainsTags(boundingBox, new TimeRange(startTime, endTime), tags))
						{
							exploreGoogleMap.addDocument(document);
						}
					}
				}
			};

			setHeight("100%");

			removeTitleComponent();

			HorizontalLayout captionLayout = new HorizontalLayout();

			HorizontalLayout labelLayout = new HorizontalLayout();
			labelLayout.setMargin(false, true, false, false);

			labelLayout.addComponent(StyleUtils.getLabelH1(title));

			captionLayout.addComponent(labelLayout);

			final Embedded loadingFeedback = new Embedded(null, ResourceUtils.BASE_AJAX_LOADER_BIG_RESOURCE);
			loadingFeedback.setType(Embedded.TYPE_IMAGE);
			loadingFeedback.setImmediate(true);
			loadingFeedback.setVisible(false);
			loadingFeedback.setWidth("27px");
			loadingFeedback.setHeight("27px");

			captionLayout.addComponent(loadingFeedback);

			addComponent(captionLayout);

			VerticalLayout mainLayout = new VerticalLayout();

			mainLayout.setSizeFull();

			HorizontalLayout controlsLayout = new HorizontalLayout();
			controlsLayout.setWidth("100%");
			controlsLayout.setMargin(false);
			controlsLayout.setSpacing(false);

			HorizontalLayout timeLayout = new HorizontalLayout();
			timeLayout.setStyleName(Reindeer.LAYOUT_BLACK);
			timeLayout.setMargin(true);
			timeLayout.setSpacing(true);

			Label startTimeLabel = StyleUtils.getLabelBoldHTML("Start Time&nbsp;");
			timeLayout.addComponent(startTimeLabel);
			timeLayout.setComponentAlignment(startTimeLabel, Alignment.MIDDLE_LEFT);

			final PopupDateField startTimeField = new PopupDateField()
			{
				private static final long serialVersionUID = -2578883361250692593L;

				@Override
				protected Date handleUnparsableDateString(String dateString) throws ConversionException
				{
					ConfirmWindow confirmWindow = new ConfirmWindow(mainWindow, "Start Time", StyleUtils.getLabelHTML("The format of the Start Time you have entered is not valid."));

					mainWindow.addWindow(confirmWindow);

					return startTime;
				}
			};

			startTimeField.addListener(new ValueChangeListener()
			{
				private static final long serialVersionUID = -8259141374026913509L;

				@Override
				public void valueChange(ValueChangeEvent event)
				{
					Date newStartTime = (Date) event.getProperty().getValue();

					if(!newStartTime.equals(startTime))
					{
						if(newStartTime.before(endTime))
						{
							startTime = newStartTime;

							requestDocuments();
						}
						else
						{
							ConfirmWindow confirmWindow = new ConfirmWindow(mainWindow, "Start Time", StyleUtils.getLabelHTML("The Start Time has to be before the End Time."));

							mainWindow.addWindow(confirmWindow);

							startTimeField.setValue(startTime);
						}
					}
				}
			});

			startTimeField.setImmediate(true);
			startTimeField.setWidth("185px");
			startTimeField.setLocale(Locale.US);
			startTimeField.setDateFormat(Time.BIG_DISTANCE_DATE_TIME_PATTERN);
			startTimeField.setResolution(PopupDateField.RESOLUTION_SEC);
			startTimeField.setValue(startTime);
			timeLayout.addComponent(startTimeField);
			timeLayout.setComponentAlignment(startTimeField, Alignment.MIDDLE_LEFT);

			Label endTimeLabel = StyleUtils.getLabelBoldHTML("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;End Time&nbsp;");
			timeLayout.addComponent(endTimeLabel);
			timeLayout.setComponentAlignment(endTimeLabel, Alignment.MIDDLE_LEFT);

			final PopupDateField endTimeField = new PopupDateField()
			{
				private static final long serialVersionUID = -2578883361250692593L;

				@Override
				protected Date handleUnparsableDateString(String dateString) throws ConversionException
				{
					ConfirmWindow confirmWindow = new ConfirmWindow(mainWindow, "End Time", StyleUtils.getLabelHTML("The format of the End Time you have entered is not valid."));

					mainWindow.addWindow(confirmWindow);

					return endTime;
				}
			};

			endTimeField.addListener(new ValueChangeListener()
			{
				private static final long serialVersionUID = 2847964810733180875L;

				@Override
				public void valueChange(ValueChangeEvent event)
				{
					Date newEndTime = (Date) event.getProperty().getValue();

					if(!newEndTime.equals(endTime))
					{
						if(newEndTime.after(startTime))
						{
							endTime = newEndTime;

							requestDocuments();
						}
						else
						{
							ConfirmWindow confirmWindow = new ConfirmWindow(mainWindow, "End Time", StyleUtils.getLabelHTML("The End Time has to be after the Start Time."));

							mainWindow.addWindow(confirmWindow);

							endTimeField.setValue(endTime);
						}
					}
				}
			});

			endTimeField.setImmediate(true);
			endTimeField.setWidth("185px");
			endTimeField.setLocale(Locale.US);
			endTimeField.setDateFormat(Time.BIG_DISTANCE_DATE_TIME_PATTERN);
			endTimeField.setResolution(PopupDateField.RESOLUTION_SEC);
			endTimeField.setValue(endTime);
			timeLayout.addComponent(endTimeField);
			timeLayout.setComponentAlignment(endTimeField, Alignment.MIDDLE_LEFT);

			controlsLayout.addComponent(timeLayout);
			controlsLayout.setComponentAlignment(timeLayout, Alignment.MIDDLE_LEFT);

			VerticalLayout wrapperLayout = new VerticalLayout();
			wrapperLayout.setSizeFull();
			wrapperLayout.setMargin(false, false, false, true);
			wrapperLayout.setSpacing(false);

			HorizontalLayout tagsLayout = new HorizontalLayout();
			tagsLayout.setStyleName(Reindeer.LAYOUT_BLACK);
			tagsLayout.setWidth("100%");
			tagsLayout.setMargin(true);
			tagsLayout.setSpacing(true);

			Label tagsLabel = StyleUtils.getLabelBoldHTML("Tags&nbsp;");
			tagsLabel.setSizeUndefined();
			tagsLayout.addComponent(tagsLabel);
			tagsLayout.setComponentAlignment(tagsLabel, Alignment.MIDDLE_LEFT);

			addTagField = new TextField();
			addTagField.setImmediate(true);
			addTagField.setInputPrompt("Enter new Tag");

			addTagField.addShortcutListener(new ShortcutListener(null, KeyCode.ENTER, new int[0])
			{
				private static final long serialVersionUID = -5625100360296912223L;

				@Override
				public void handleAction(Object sender, Object target)
				{
					if(target == addTagField)
					{
						addTag();
					}
				}
			});

			tagsLayout.addComponent(addTagField);
			tagsLayout.setComponentAlignment(addTagField, Alignment.MIDDLE_LEFT);

			tabSheet = new TabSheet();

			tabSheet.setStyleName(Reindeer.TABSHEET_SMALL);
			tabSheet.addStyleName("view");

			tabSheet.addListener(new ComponentDetachListener()
			{
				private static final long serialVersionUID = -4555446069389829560L;

				@Override
				public void componentDetachedFromContainer(ComponentDetachEvent event)
				{
					tags.remove(tabSheet.getTab(event.getDetachedComponent()).getCaption());

					requestDocuments();
				}
			});

			Button addTagButton = new Button("Add", new Button.ClickListener()
			{
				private static final long serialVersionUID = 4577061377601988261L;

				@Override
				public void buttonClick(ClickEvent event)
				{
					addTag();
				}
			});

			addTagButton.setStyleName(Reindeer.BUTTON_DEFAULT);

			tagsLayout.addComponent(addTagButton);
			tagsLayout.setComponentAlignment(addTagButton, Alignment.MIDDLE_LEFT);

			Label spaceLabel = StyleUtils.getLabelHTML("");
			spaceLabel.setSizeUndefined();
			tagsLayout.addComponent(spaceLabel);
			tagsLayout.setComponentAlignment(spaceLabel, Alignment.MIDDLE_LEFT);

			tagsLayout.addComponent(tabSheet);
			tagsLayout.setComponentAlignment(tabSheet, Alignment.MIDDLE_LEFT);
			tagsLayout.setExpandRatio(tabSheet, 1.0f);

			wrapperLayout.addComponent(tagsLayout);
			wrapperLayout.setComponentAlignment(tagsLayout, Alignment.MIDDLE_LEFT);

			controlsLayout.addComponent(wrapperLayout);
			controlsLayout.setExpandRatio(wrapperLayout, 1.0f);

			mainLayout.addComponent(controlsLayout);

			VerticalLayout mapLayout = new VerticalLayout();

			mapLayout.setSizeFull();
			mapLayout.setMargin(true, false, false, false);

			exploreGoogleMap = new ExploreGoogleMap(application, 12, 48.42255269321401d, 9.956477880477905d, MapType.HYBRID);

			exploreGoogleMap.setWidth("100%");
			exploreGoogleMap.setHeight("100%");

			mainWindow.addSectionChangeListener(new SectionChangeListener()
			{
				@Override
				public void onSectionChanged(Section section)
				{
					if(componentsAdded && section == ExploreSection.this)
					{
						exploreGoogleMap.activatePolling();

						feedService.addDocumentListener(documentListener);
					}
					else
					{
						exploreGoogleMap.deactivatePolling();

						feedService.removeListener(documentListener);
					}
				}
			});

			exploreGoogleMap.addMapSectionListener(new MapSectionListener()
			{
				@Override
				public void onMapSectionChanged(BoundingBox boundingBox)
				{
					ExploreSection.this.boundingBox = boundingBox;

					requestDocuments();
				}
			});

			mapLayout.addComponent(exploreGoogleMap);

			mainLayout.addComponent(mapLayout);
			mainLayout.setExpandRatio(mapLayout, 1.0f);

			addComponent(mainLayout);
			setExpandRatio(mainLayout, 1.0f);

			exploreGoogleMap.addLoadingProcessListener(new LoadingProcessListener()
			{
				@Override
				public void onLoadingProcessStarted()
				{
					loadingFeedback.setVisible(true);
				}

				@Override
				public void onLoadingProcessFinished()
				{
					loadingFeedback.setVisible(false);
				}
			});

			feedService.addDocumentListener(documentListener);

			componentsAdded = true;
		}
	}

	/**
	 * Adds a new {@link Tag} specified by the current value of the
	 * {@link TextField} as an additional filter setting and induces that the
	 * relevant {@link Document}s will be requested.
	 */
	private void addTag()
	{
		String value = ((String) addTagField.getValue()).trim();

		if(tags.contains(value))
		{
			addTagField.setValue("");
		}
		else if(value.length() >= 3 && value.length() <= 25)
		{
			Tab tab = tabSheet.addTab(new Label(), value, null);

			tab.setClosable(true);

			tags.add(value);

			addTagField.setValue("");

			requestDocuments();
		}
		else
		{
			ConfirmWindow confirmWindow = new ConfirmWindow(mainWindow, "Tags", StyleUtils.getLabelHTML("The number of characters has to be between 3 and 25."));

			mainWindow.addWindow(confirmWindow);

			addTagField.setValue("");
		}
	}

	/**
	 * Requests the {@link Document}s which are relevant according to the
	 * specified filter settings and updates the {@link ExploreGoogleMap}.
	 */
	private void requestDocuments()
	{
		if(startTime != null && endTime != null && boundingBox != null)
		{
			ResultSet<DocumentID, Document> documents = coreService.getDocuments(tags, boundingBox, new TimeRange(new DateTime(startTime), new DateTime(endTime)), LoadType.SNAPSHOT);

			exploreGoogleMap.updateDocuments(documents);
		}
	}

	@Override
	public void update(Observable<UserSession> observable, UserSession userSession)
	{
	}
}
