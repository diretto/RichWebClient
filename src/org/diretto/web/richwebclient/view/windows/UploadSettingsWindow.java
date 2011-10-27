package org.diretto.web.richwebclient.view.windows;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.diretto.api.client.base.data.Contributor;
import org.diretto.api.client.base.data.TimeRange;
import org.diretto.api.client.base.data.TopographicPoint;
import org.diretto.api.client.user.UserID;
import org.diretto.web.richwebclient.view.base.client.MediaType;
import org.diretto.web.richwebclient.view.sections.UploadSection;
import org.diretto.web.richwebclient.view.util.StyleUtils;
import org.diretto.web.richwebclient.view.widgets.googlemap.client.base.MapType;
import org.diretto.web.richwebclient.view.widgets.googlemap.server.UploadGoogleMap;
import org.diretto.web.richwebclient.view.widgets.upload.client.base.FileInfo;
import org.joda.time.DateTime;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.themes.Reindeer;

/**
 * An {@code UploadSettingsWindow} is responsible for offering and managing the
 * graphical user interface in respect of the upload settings.
 * 
 * @author Tobias Schlecht
 */
public final class UploadSettingsWindow extends Window
{
	private static final long serialVersionUID = -8301381498296782604L;

	private final MainWindow mainWindow;
	private final HorizontalLayout wrapperLayout;
	private final VerticalLayout mainLayout;

	private final TextField addTagField;
	private final TabSheet tabSheet;
	private final Button uploadButton;
	private final Button cancelButton;
	private final Button cancelAllButton;

	private final List<String> tags = new Vector<String>();

	/**
	 * Constructs an {@link UploadSettingsWindow}.
	 * 
	 * @param mainWindow The corresponding {@code MainWindow}
	 * @param uploadSection The corresponding {@code UploadSection}
	 * @param fileInfo The {@code FileInfo} object
	 * @param mediaType The {@code MediaType} of the file
	 * @param uploadSettings The corresponding {@code UploadSettings}
	 * @param otherFiles The names of the other upload files
	 */
	public UploadSettingsWindow(final MainWindow mainWindow, final UploadSection uploadSection, FileInfo fileInfo, MediaType mediaType, UploadSettings uploadSettings, List<String> otherFiles)
	{
		super("Upload Settings");

		this.mainWindow = mainWindow;

		setModal(true);
		setStyleName(Reindeer.WINDOW_BLACK);
		setWidth("940px");
		setHeight((((int) mainWindow.getHeight()) - 160) + "px");
		setResizable(false);
		setClosable(false);
		setDraggable(false);
		setPositionX((int) (mainWindow.getWidth() / 2.0f) - 470);
		setPositionY(126);

		wrapperLayout = new HorizontalLayout();
		wrapperLayout.setSizeFull();
		wrapperLayout.setMargin(true, true, true, true);
		addComponent(wrapperLayout);

		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		mainLayout.setSpacing(true);

		HorizontalLayout titleLayout = new HorizontalLayout();
		titleLayout.setWidth("100%");
		mainLayout.addComponent(titleLayout);

		HorizontalLayout leftTitleLayout = new HorizontalLayout();
		titleLayout.addComponent(leftTitleLayout);
		titleLayout.setComponentAlignment(leftTitleLayout, Alignment.MIDDLE_LEFT);

		Label fileNameLabel = StyleUtils.getLabelH2(fileInfo.getName());
		leftTitleLayout.addComponent(fileNameLabel);
		leftTitleLayout.setComponentAlignment(fileNameLabel, Alignment.MIDDLE_LEFT);

		Label bullLabel = StyleUtils.getLabelHTML("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&bull;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		leftTitleLayout.addComponent(bullLabel);
		leftTitleLayout.setComponentAlignment(bullLabel, Alignment.MIDDLE_LEFT);

		Label titleLabel = StyleUtils.getLabelHTML("<b>Title</b>&nbsp;&nbsp;&nbsp;");
		leftTitleLayout.addComponent(titleLabel);
		leftTitleLayout.setComponentAlignment(titleLabel, Alignment.MIDDLE_LEFT);

		final TextField titleField = new TextField();
		titleField.setMaxLength(50);
		titleField.setWidth("100%");
		titleField.setImmediate(true);

		if(uploadSettings != null && uploadSettings.getTitle() != null)
		{
			titleField.setValue(uploadSettings.getTitle());
		}

		titleField.focus();
		titleField.setCursorPosition(((String) titleField.getValue()).length());

		titleLayout.addComponent(titleField);
		titleLayout.setComponentAlignment(titleField, Alignment.MIDDLE_RIGHT);

		titleLayout.setExpandRatio(leftTitleLayout, 0);
		titleLayout.setExpandRatio(titleField, 1);

		mainLayout.addComponent(StyleUtils.getHorizontalLine());

		GridLayout topGridLayout = new GridLayout(2, 3);
		topGridLayout.setColumnExpandRatio(0, 1.0f);
		topGridLayout.setColumnExpandRatio(1, 0.0f);
		topGridLayout.setWidth("100%");
		topGridLayout.setSpacing(true);
		mainLayout.addComponent(topGridLayout);

		Label descriptionLabel = StyleUtils.getLabelBold("Description");
		topGridLayout.addComponent(descriptionLabel, 0, 0);

		final TextArea descriptionArea = new TextArea();
		descriptionArea.setSizeFull();
		descriptionArea.setImmediate(true);

		if(uploadSettings != null && uploadSettings.getDescription() != null)
		{
			descriptionArea.setValue(uploadSettings.getDescription());
		}

		topGridLayout.addComponent(descriptionArea, 0, 1);

		VerticalLayout tagsWrapperLayout = new VerticalLayout();
		tagsWrapperLayout.setHeight("30px");
		tagsWrapperLayout.setSpacing(false);
		topGridLayout.addComponent(tagsWrapperLayout, 0, 2);

		HorizontalLayout tagsLayout = new HorizontalLayout();
		tagsLayout.setWidth("100%");
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
			private static final long serialVersionUID = -4767515198819351723L;

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
			private static final long serialVersionUID = -657657505471281795L;

			@Override
			public void componentDetachedFromContainer(ComponentDetachEvent event)
			{
				tags.remove(tabSheet.getTab(event.getDetachedComponent()).getCaption());
			}
		});

		Button addTagButton = new Button("Add", new Button.ClickListener()
		{
			private static final long serialVersionUID = 5914473126402594623L;

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

		tagsWrapperLayout.addComponent(tagsLayout);
		tagsWrapperLayout.setComponentAlignment(tagsLayout, Alignment.TOP_LEFT);

		if(uploadSettings != null && uploadSettings.getTags() != null && uploadSettings.getTags().size() > 0)
		{
			for(String tag : uploadSettings.getTags())
			{
				addTagField.setValue(tag);

				addTag();
			}
		}

		Label presettingLabel = StyleUtils.getLabelBold("Presetting");
		topGridLayout.addComponent(presettingLabel, 1, 0);

		final TwinColSelect twinColSelect;

		if(otherFiles != null && otherFiles.size() > 0)
		{
			twinColSelect = new TwinColSelect(null, otherFiles);
		}
		else
		{
			twinColSelect = new TwinColSelect();
		}

		twinColSelect.setWidth("400px");
		twinColSelect.setRows(10);
		topGridLayout.addComponent(twinColSelect, 1, 1);
		topGridLayout.setComponentAlignment(twinColSelect, Alignment.TOP_RIGHT);

		Label otherFilesLabel = StyleUtils.getLabelSmallHTML("Select the files which should get the settings of this file as presetting.");
		otherFilesLabel.setSizeUndefined();
		topGridLayout.addComponent(otherFilesLabel, 1, 2);
		topGridLayout.setComponentAlignment(otherFilesLabel, Alignment.MIDDLE_CENTER);

		mainLayout.addComponent(StyleUtils.getHorizontalLine());

		final UploadGoogleMap googleMap;

		if(uploadSettings != null && uploadSettings.getTopographicPoint() != null)
		{
			googleMap = new UploadGoogleMap(mediaType, 12, uploadSettings.getTopographicPoint().getLatitude(), uploadSettings.getTopographicPoint().getLongitude(), MapType.HYBRID);
		}
		else
		{
			googleMap = new UploadGoogleMap(mediaType, 12, 48.42255269321401d, 9.956477880477905d, MapType.HYBRID);
		}

		googleMap.setWidth("100%");
		googleMap.setHeight("300px");
		mainLayout.addComponent(googleMap);

		mainLayout.addComponent(StyleUtils.getHorizontalLine());

		GridLayout bottomGridLayout = new GridLayout(3, 3);
		bottomGridLayout.setSizeFull();
		bottomGridLayout.setSpacing(true);
		mainLayout.addComponent(bottomGridLayout);

		Label licenseLabel = StyleUtils.getLabelBold("License");
		bottomGridLayout.addComponent(licenseLabel, 0, 0);

		final TextArea licenseArea = new TextArea();
		licenseArea.setWidth("320px");
		licenseArea.setHeight("175px");
		licenseArea.setImmediate(true);

		if(uploadSettings != null && uploadSettings.getLicense() != null)
		{
			licenseArea.setValue(uploadSettings.getLicense());
		}

		bottomGridLayout.addComponent(licenseArea, 0, 1);

		final Label startTimeLabel = StyleUtils.getLabelBold("Earliest possible Start Date");
		startTimeLabel.setSizeUndefined();
		bottomGridLayout.setComponentAlignment(startTimeLabel, Alignment.TOP_CENTER);
		bottomGridLayout.addComponent(startTimeLabel, 1, 0);

		final InlineDateField startTimeField = new InlineDateField();
		startTimeField.setImmediate(true);
		startTimeField.setResolution(InlineDateField.RESOLUTION_SEC);

		boolean currentTimeAdjusted = false;

		if(uploadSettings != null && uploadSettings.getTimeRange() != null && uploadSettings.getTimeRange().getStartDateTime() != null)
		{
			startTimeField.setValue(uploadSettings.getTimeRange().getStartDateTime().toDate());
		}
		else
		{
			currentTimeAdjusted = true;

			startTimeField.setValue(new Date());
		}

		bottomGridLayout.setComponentAlignment(startTimeField, Alignment.TOP_CENTER);
		bottomGridLayout.addComponent(startTimeField, 1, 1);

		final CheckBox exactTimeCheckBox = new CheckBox("This is the exact point in time.");
		exactTimeCheckBox.setImmediate(true);
		bottomGridLayout.setComponentAlignment(exactTimeCheckBox, Alignment.TOP_CENTER);
		bottomGridLayout.addComponent(exactTimeCheckBox, 1, 2);

		final Label endTimeLabel = StyleUtils.getLabelBold("Latest possible Start Date");
		endTimeLabel.setSizeUndefined();
		bottomGridLayout.setComponentAlignment(endTimeLabel, Alignment.TOP_CENTER);
		bottomGridLayout.addComponent(endTimeLabel, 2, 0);

		final InlineDateField endTimeField = new InlineDateField();
		endTimeField.setImmediate(true);
		endTimeField.setResolution(InlineDateField.RESOLUTION_SEC);

		if(uploadSettings != null && uploadSettings.getTimeRange() != null && uploadSettings.getTimeRange().getEndDateTime() != null)
		{
			endTimeField.setValue(uploadSettings.getTimeRange().getEndDateTime().toDate());
		}
		else
		{
			endTimeField.setValue(startTimeField.getValue());
		}

		bottomGridLayout.setComponentAlignment(endTimeField, Alignment.TOP_CENTER);
		bottomGridLayout.addComponent(endTimeField, 2, 1);

		if(!currentTimeAdjusted && ((Date) startTimeField.getValue()).equals((Date) endTimeField.getValue()))
		{
			exactTimeCheckBox.setValue(true);

			endTimeLabel.setEnabled(false);
			endTimeField.setEnabled(false);
		}

		exactTimeCheckBox.addListener(new ValueChangeListener()
		{
			private static final long serialVersionUID = 7193545421803538364L;

			@Override
			public void valueChange(ValueChangeEvent event)
			{
				if((Boolean) event.getProperty().getValue())
				{
					endTimeLabel.setEnabled(false);
					endTimeField.setEnabled(false);
				}
				else
				{
					endTimeLabel.setEnabled(true);
					endTimeField.setEnabled(true);
				}
			}
		});

		mainLayout.addComponent(StyleUtils.getHorizontalLine());

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setMargin(true, false, false, false);
		buttonLayout.setSpacing(false);

		HorizontalLayout uploadButtonLayout = new HorizontalLayout();
		uploadButtonLayout.setMargin(false, true, false, false);

		uploadButton = new Button("Upload File", new Button.ClickListener()
		{
			private static final long serialVersionUID = 8013811216568950479L;

			@Override
			@SuppressWarnings("unchecked")
			public void buttonClick(ClickEvent event)
			{
				String titleValue = titleField.getValue().toString().trim();
				Date startTimeValue = (Date) startTimeField.getValue();
				Date endTimeValue = (Date) endTimeField.getValue();
				boolean exactTimeValue = (Boolean) exactTimeCheckBox.getValue();

				if(titleValue.equals(""))
				{
					ConfirmWindow confirmWindow = new ConfirmWindow(mainWindow, "Title Field", StyleUtils.getLabelHTML("A title entry is required."));

					mainWindow.addWindow(confirmWindow);
				}
				else if(titleValue.length() < 5 || titleValue.length() > 50)
				{
					ConfirmWindow confirmWindow = new ConfirmWindow(mainWindow, "Title Field", StyleUtils.getLabelHTML("The number of characters of the title has to be between 5 and 50."));

					mainWindow.addWindow(confirmWindow);
				}
				else if(!exactTimeValue && startTimeValue.after(endTimeValue))
				{
					ConfirmWindow confirmWindow = new ConfirmWindow(mainWindow, "Date Entries", StyleUtils.getLabelHTML("The second date has to be after the first date."));

					mainWindow.addWindow(confirmWindow);
				}
				else if(startTimeValue.after(new Date()) || (!exactTimeValue && endTimeValue.after(new Date())))
				{
					ConfirmWindow confirmWindow = new ConfirmWindow(mainWindow, "Date Entries", StyleUtils.getLabelHTML("The dates are not allowed to be in the future."));

					mainWindow.addWindow(confirmWindow);
				}
				else
				{
					disableButtons();

					String descriptionValue = descriptionArea.getValue().toString().trim();
					String licenseValue = licenseArea.getValue().toString().trim();
					TopographicPoint topographicPointValue = googleMap.getMarkerPosition();
					Collection<String> presettingValues = (Collection<String>) twinColSelect.getValue();

					if(exactTimeValue)
					{
						endTimeValue = startTimeValue;
					}

					TimeRange timeRange = new TimeRange(new DateTime(startTimeValue), new DateTime(endTimeValue));

					UploadSettings uploadSettings = new UploadSettings(titleValue, descriptionValue, licenseValue, new Vector<String>(tags), topographicPointValue, timeRange);

					mainWindow.removeWindow(event.getButton().getWindow());

					requestRepaint();

					uploadSection.upload(uploadSettings, new Vector<String>(presettingValues));
				}
			}
		});

		uploadButton.setStyleName(Reindeer.BUTTON_DEFAULT);
		uploadButtonLayout.addComponent(uploadButton);
		buttonLayout.addComponent(uploadButtonLayout);

		HorizontalLayout cancelButtonLayout = new HorizontalLayout();
		cancelButtonLayout.setMargin(false, true, false, false);

		cancelButton = new Button("Cancel", new Button.ClickListener()
		{
			private static final long serialVersionUID = -2565870159504952913L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				disableButtons();

				mainWindow.removeWindow(event.getButton().getWindow());

				requestRepaint();

				uploadSection.cancelUpload();
			}
		});

		cancelButton.setStyleName(Reindeer.BUTTON_DEFAULT);
		cancelButtonLayout.addComponent(cancelButton);
		buttonLayout.addComponent(cancelButtonLayout);

		cancelAllButton = new Button("Cancel All", new Button.ClickListener()
		{
			private static final long serialVersionUID = -8578124709201789182L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				disableButtons();

				mainWindow.removeWindow(event.getButton().getWindow());

				requestRepaint();

				uploadSection.cancelAllUploads();
			}
		});

		cancelAllButton.setStyleName(Reindeer.BUTTON_DEFAULT);
		buttonLayout.addComponent(cancelAllButton);

		mainLayout.addComponent(buttonLayout);
		mainLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);

		wrapperLayout.addComponent(mainLayout);
	}

	/**
	 * Adds a new {@link Tag} specified by the current value of the
	 * {@link TextField}.
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
		}
		else
		{
			ConfirmWindow confirmWindow = new ConfirmWindow(mainWindow, "Tags", StyleUtils.getLabelHTML("The number of characters has to be between 3 and 25."));

			mainWindow.addWindow(confirmWindow);

			addTagField.setValue("");
		}
	}

	/**
	 * Disables all {@link Button}s.
	 */
	private void disableButtons()
	{
		uploadButton.setEnabled(false);
		cancelButton.setEnabled(false);
		cancelAllButton.setEnabled(false);
	}

	/**
	 * This <i>static member</i> class represents the settings of the
	 * corresponding upload.
	 */
	public static class UploadSettings
	{
		private String title;
		private String description;
		private String license;
		private List<String> tags;
		private TopographicPoint topographicPoint;
		private TimeRange timeRange;
		private ArrayList<Contributor> contributors;
		private ArrayList<UserID> creators;

		/**
		 * Constructs an {@code UploadSettings} object.
		 */
		public UploadSettings()
		{
			this(null, null, null, null);
		}

		/**
		 * Constructs an {@code UploadSettings} object.
		 * 
		 * @param title The title
		 * @param description The description
		 * @param license The license
		 * @param tags The {@code List} of tags
		 */
		public UploadSettings(String title, String description, String license, List<String> tags)
		{
			this(title, description, license, tags, null, null);
		}

		/**
		 * Constructs an {@code UploadSettings} object.
		 * 
		 * @param title The title
		 * @param description The description
		 * @param license The license
		 * @param tags The {@code List} of tags
		 * @param topographicPoint The {@code TopographicPoint}
		 * @param timeRange The {@code TimeRange}
		 */
		public UploadSettings(String title, String description, String license, List<String> tags, TopographicPoint topographicPoint, TimeRange timeRange)
		{
			this.title = title;
			this.description = description;
			this.license = license;
			this.tags = tags;
			this.topographicPoint = topographicPoint;
			this.timeRange = timeRange;

			this.contributors = null;
			this.creators = null;
		}

		/**
		 * Returns the title.
		 * 
		 * @return The title
		 */
		public String getTitle()
		{
			return title;
		}

		/**
		 * Sets the title.
		 * 
		 * @param title The title
		 */
		public void setTitle(String title)
		{
			this.title = title;
		}

		/**
		 * Returns the description.
		 * 
		 * @return The description
		 */
		public String getDescription()
		{
			return description;
		}

		/**
		 * Sets the description.
		 * 
		 * @param description The description
		 */
		public void setDescription(String description)
		{
			this.description = description;
		}

		/**
		 * Returns the license.
		 * 
		 * @return The license
		 */
		public String getLicense()
		{
			return license;
		}

		/**
		 * Sets the license.
		 * 
		 * @param license The license
		 */
		public void setLicense(String license)
		{
			this.license = license;
		}

		/**
		 * Returns the {@link List} of tags.
		 * 
		 * @return The {@code List} of tags
		 */
		public List<String> getTags()
		{
			return tags;
		}

		/**
		 * Sets the {@link List} of tags.
		 * 
		 * @param tags The {@code List} of tags
		 */
		public void setTags(List<String> tags)
		{
			this.tags = tags;
		}

		/**
		 * Returns the {@link TopographicPoint}.
		 * 
		 * @return The {@code TopographicPoint}
		 */
		public TopographicPoint getTopographicPoint()
		{
			return topographicPoint;
		}

		/**
		 * Sets the {@link TopographicPoint}.
		 * 
		 * @param topographicPoint The {@code TopographicPoint}
		 */
		public void setTopographicPoint(TopographicPoint topographicPoint)
		{
			this.topographicPoint = topographicPoint;
		}

		/**
		 * Returns the {@link TimeRange}.
		 * 
		 * @return The {@code TimeRange}
		 */
		public TimeRange getTimeRange()
		{
			return timeRange;
		}

		/**
		 * Sets the {@link TimeRange}.
		 * 
		 * @param timeRange The {@code TimeRange}
		 */
		public void setTimeRange(TimeRange timeRange)
		{
			this.timeRange = timeRange;
		}

		/**
		 * Returns the {@link List} of {@link Contributor}s.
		 * 
		 * @return The {@code List} of {@code Contributor}s
		 */
		public ArrayList<Contributor> getContributors()
		{
			return contributors;
		}

		/**
		 * Sets the {@link List} of {@link Contributor}s.
		 * 
		 * @param contributors The {@code List} of {@code Contributor}s
		 */
		public void setContributors(ArrayList<Contributor> contributors)
		{
			this.contributors = contributors;
		}

		/**
		 * Returns the {@link List} of creators.
		 * 
		 * @return The {@code List} of creators
		 */
		public ArrayList<UserID> getCreators()
		{
			return creators;
		}

		/**
		 * Sets the {@link List} of creators.
		 * 
		 * @param creators The {@code List} of creators
		 */
		public void setCreators(ArrayList<UserID> creators)
		{
			this.creators = creators;
		}
	}
}
