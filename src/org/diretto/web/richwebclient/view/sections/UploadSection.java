package org.diretto.web.richwebclient.view.sections;

import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;
import org.diretto.api.client.base.data.PlatformMediaType;
import org.diretto.api.client.base.data.UploadInfo;
import org.diretto.api.client.main.core.CoreService;
import org.diretto.api.client.main.core.entities.Document;
import org.diretto.api.client.main.core.entities.DocumentID;
import org.diretto.api.client.main.core.entities.creation.PlatformAttachmentCreationData;
import org.diretto.api.client.main.storage.StorageService;
import org.diretto.api.client.main.storage.upload.UploadProcess;
import org.diretto.api.client.main.storage.upload.UploadReport;
import org.diretto.api.client.session.UserSession;
import org.diretto.api.client.util.Observable;
import org.diretto.web.richwebclient.RichWebClientApplication;
import org.diretto.web.richwebclient.view.base.AbstractSection;
import org.diretto.web.richwebclient.view.base.Section;
import org.diretto.web.richwebclient.view.util.MediaTypeUtils;
import org.diretto.web.richwebclient.view.util.ResourceUtils;
import org.diretto.web.richwebclient.view.util.StyleUtils;
import org.diretto.web.richwebclient.view.widgets.upload.client.base.FileInfo;
import org.diretto.web.richwebclient.view.widgets.upload.server.MultipleUpload;
import org.diretto.web.richwebclient.view.widgets.upload.server.MultipleUpload.MultipleUploadHandler;
import org.diretto.web.richwebclient.view.windows.ConfirmWindow;
import org.diretto.web.richwebclient.view.windows.MainWindow;
import org.diretto.web.richwebclient.view.windows.UploadSettingsWindow;
import org.diretto.web.richwebclient.view.windows.UploadSettingsWindow.UploadSettings;
import org.diretto.web.richwebclient.view.windows.event.SectionChangeListener;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.Reindeer;

/**
 * This class represents an {@code UploadSection}.
 * 
 * @author Tobias Schlecht
 */
public final class UploadSection extends AbstractSection
{
	private static final long serialVersionUID = -359519159690651564L;

	private static final int POLLING_INTERVAL = 15000;

	private final RichWebClientApplication application;
	private final CoreService coreService;
	private final StorageService storageService;

	private boolean componentsAdded = false;

	private MainWindow mainWindow;
	private VerticalLayout mainLayout;
	private MultipleUpload multipleUpload;

	private HorizontalLayout captionLayout = null;

	private final Map<String, FileInfo> files = new ConcurrentHashMap<String, FileInfo>();
	private final List<String> fileNames = new CopyOnWriteArrayList<String>();
	private final Map<String, UploadSettings> settings = new ConcurrentHashMap<String, UploadSettings>();
	private final Map<String, UploadSettings> preSettings = new ConcurrentHashMap<String, UploadSettings>();
	private final MultiMap uploadedEmbeddeds = new MultiHashMap();
	private final MultiMap publishedEmbeddeds = new MultiHashMap();

	private FileInfo currentFile = null;

	/**
	 * Constructs an {@link UploadSection}.
	 * 
	 * @param application The corresponding {@code RichWebClientApplication}
	 */
	public UploadSection(RichWebClientApplication application)
	{
		super(application.getAuthenticationRegistry(), true, false, "Upload", "Share your media");

		this.application = application;

		coreService = application.getCoreService();
		storageService = application.getStorageService();
	}

	@Override
	public synchronized void addComponents()
	{
		if(!componentsAdded)
		{
			mainWindow = (MainWindow) getWindow();

			loadComponents();

			mainWindow.addSectionChangeListener(new SectionChangeListener()
			{
				@Override
				public void onSectionChanged(Section section)
				{
					if(componentsAdded && section == UploadSection.this)
					{
						if(captionLayout != null && captionLayout.getComponentCount() == 1)
						{
							captionLayout.addComponent(getPollingProgressIndicator());
						}
					}
					else
					{
						if(captionLayout != null && captionLayout.getComponentCount() > 1)
						{
							captionLayout.removeComponent(captionLayout.getComponent(1));
						}
					}
				}
			});

			componentsAdded = true;
		}
	}

	/**
	 * Loads the content of this {@link Section}.
	 */
	private void loadComponents()
	{
		removeAllComponents();

		captionLayout = new HorizontalLayout();
		captionLayout.addComponent(StyleUtils.getLabelH1(title));
		captionLayout.addComponent(getPollingProgressIndicator());
		addComponent(captionLayout);

		mainLayout = new VerticalLayout();
		mainLayout.setStyleName(Reindeer.LAYOUT_BLACK);
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		addComponent(mainLayout);

		mainLayout.addComponent(StyleUtils.getLabelH2("File Upload"));
		mainLayout.addComponent(StyleUtils.getHorizontalLine());
		mainLayout.addComponent(StyleUtils.getLabel("Select the files which you want to upload"));
		mainLayout.addComponent(StyleUtils.getLabelSmall("(To choose multiple files, just press the CTRL-key while selecting the files)"));
		mainLayout.addComponent(StyleUtils.getVerticalSpaceSmall());

		multipleUpload = new MultipleUpload(application, "Select Files", Reindeer.BUTTON_DEFAULT);

		multipleUpload.addMultipleUploadHandler(new MultipleUploadHandler()
		{
			@Override
			public void onUploadsSelected(List<FileInfo> fileInfos)
			{
				for(FileInfo file : fileInfos)
				{
					if(file != null)
					{
						files.put(file.getName(), file);

						if(!fileNames.contains(file.getName()))
						{
							fileNames.add(file.getName());
						}
					}
				}

				handleNextFile();
			}

			@Override
			public void onUploadStarted(FileInfo fileInfo)
			{
				return;
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onUploadFinished(final FileInfo fileInfo, final File file)
			{
				final UploadSettings uploadSettings = settings.remove(fileInfo.getName());
				final UserSession userSession = authenticationRegistry.getActiveUserSession();

				Embedded embedded;

				synchronized(UploadSection.this)
				{
					embedded = ((List<Embedded>) uploadedEmbeddeds.get(fileInfo)).get(0);
					uploadedEmbeddeds.remove(fileInfo, embedded);

					if(embedded != null)
					{
						embedded.setSource(ResourceUtils.RUNO_ICON_32_OK_RESOURCE);

						requestRepaint();
					}
				}

				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						if(uploadSettings != null && userSession != null)
						{
							PlatformMediaType platformMediaType = coreService.getPlatformMediaType(fileInfo.getType());
							PlatformAttachmentCreationData platformAttachmentCreationData = ((PlatformAttachmentCreationData.Builder) new PlatformAttachmentCreationData.Builder().fileSize(fileInfo.getSize()).platformMediaType(platformMediaType).title(uploadSettings.getTitle()).description(uploadSettings.getDescription()).license(uploadSettings.getLicense()).contributors(uploadSettings.getContributors()).creators(uploadSettings.getCreators())).build();
							UploadInfo uploadInfo = coreService.createDocument(userSession, platformAttachmentCreationData, uploadSettings.getTopographicPoint(), uploadSettings.getTimeRange());

							UploadProcess uploadProcess = storageService.createUploadProcess(userSession, uploadInfo, file);
							UploadReport uploadReport = storageService.executeUploadProcess(uploadProcess);

							Embedded embedded;

							synchronized(UploadSection.this)
							{
								embedded = ((List<Embedded>) publishedEmbeddeds.get(fileInfo)).get(0);
								publishedEmbeddeds.remove(fileInfo, embedded);
							}

							if(uploadReport != null)
							{
								for(String tag : uploadSettings.getTags())
								{
									Document document = coreService.getDocument((DocumentID) uploadReport.getAttachmentID().getRootID());
									document.addTag(userSession, tag);
								}

								NumberFormat numberFormat = NumberFormat.getInstance();
								numberFormat.setGroupingUsed(false);

								System.out.println("==================================");
								System.out.println("vvvvvvvvvv UploadReport vvvvvvvvvv");
								System.out.println("AttachmentID: " + uploadReport.getAttachmentID().getUniqueResourceURL().toExternalForm());
								System.out.println("File URL: " + uploadReport.getFileURL().toExternalForm());
								System.out.println("Media Type: " + uploadReport.getPlatformMediaType().getID());
								System.out.println("File Size: " + uploadReport.getFileSize() + " Bytes");
								System.out.println("Upload Time: " + numberFormat.format(uploadReport.getUploadTime()) + " ms");
								System.out.println("Upload Rate: " + numberFormat.format(uploadReport.getUploadRate()) + " Bytes/s");
								System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
								System.out.println("==================================");

								synchronized(UploadSection.this)
								{
									if(embedded != null)
									{
										embedded.setSource(ResourceUtils.RUNO_ICON_32_OK_RESOURCE);

										requestRepaint();
									}
								}
							}
							else
							{
								System.err.println("UploadReport: null");

								synchronized(UploadSection.this)
								{
									if(embedded != null)
									{
										embedded.setSource(ResourceUtils.RUNO_ICON_32_CANCEL_RESOURCE);

										requestRepaint();
									}
								}
							}
						}

						if(file.exists())
						{
							file.delete();
						}
					}
				}).start();
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onUploadFailed(FileInfo fileInfo, Exception exception)
			{
				Embedded uploadedEmbedded;

				synchronized(UploadSection.this)
				{
					uploadedEmbedded = ((List<Embedded>) uploadedEmbeddeds.get(fileInfo)).get(0);
					uploadedEmbeddeds.remove(fileInfo, uploadedEmbedded);

					if(uploadedEmbedded != null)
					{
						uploadedEmbedded.setSource(ResourceUtils.RUNO_ICON_32_CANCEL_RESOURCE);

						requestRepaint();
					}
				}

				Embedded publishedEmbedded;

				synchronized(UploadSection.this)
				{
					publishedEmbedded = ((List<Embedded>) publishedEmbeddeds.get(fileInfo)).get(0);
					publishedEmbeddeds.remove(fileInfo, publishedEmbedded);

					if(publishedEmbedded != null)
					{
						publishedEmbedded.setSource(ResourceUtils.RUNO_ICON_32_CANCEL_RESOURCE);

						requestRepaint();
					}
				}

				settings.remove(fileInfo.getName());
			}
		});

		mainLayout.addComponent(multipleUpload);
		mainLayout.addComponent(StyleUtils.getHorizontalLine());
	}

	/**
	 * Returns a new polling {@link ProgressIndicator}.
	 * 
	 * @return A new polling {@code ProgressIndicator}
	 */
	private ProgressIndicator getPollingProgressIndicator()
	{
		ProgressIndicator pollingProgressIndicator = new ProgressIndicator();

		pollingProgressIndicator.addStyleName("outside-screen");
		pollingProgressIndicator.setIndeterminate(true);
		pollingProgressIndicator.setImmediate(true);
		pollingProgressIndicator.setPollingInterval(POLLING_INTERVAL);

		return pollingProgressIndicator;
	}

	/**
	 * Induces the necessary actions for handling the next file.
	 */
	private void handleNextFile()
	{
		if(files.size() > 0)
		{
			currentFile = files.remove(fileNames.get(0));
			fileNames.remove(currentFile.getName());

			PlatformMediaType platformMediaType = coreService.getPlatformMediaType(currentFile.getType());

			if(platformMediaType != null && currentFile.getSize() > 0 && currentFile.getSize() <= platformMediaType.getMaxSize())
			{
				UploadSettings uploadSettings = null;

				if(preSettings.containsKey(currentFile.getName()))
				{
					uploadSettings = preSettings.get(currentFile.getName());
				}

				UploadSettingsWindow uploadSettingsWindow = new UploadSettingsWindow((MainWindow) getWindow(), this, currentFile, MediaTypeUtils.getMediaType(platformMediaType.getMediaMainType()), uploadSettings, fileNames);

				getWindow().addWindow(uploadSettingsWindow);
			}
			else
			{
				String message = null;

				if(currentFile.getType().equals(""))
				{
					message = "The file \"" + currentFile.getName() + "\" cannot be uploaded because files without a type are not supported by this platform.";
				}
				else if(currentFile.getSize() <= 0)
				{
					message = "The file \"" + currentFile.getName() + "\" cannot be uploaded because empty files are not supported by this platform.";
				}
				else if(platformMediaType != null && currentFile.getSize() > platformMediaType.getMaxSize())
				{
					message = "The file \"" + currentFile.getName() + "\" cannot be uploaded because the maximum file size for the type \"" + platformMediaType.getID() + "\" is \"" + platformMediaType.getMaxSize() + " Bytes\".";
				}
				else
				{
					message = "The file \"" + currentFile.getName() + "\" cannot be uploaded because the type \"" + currentFile.getType() + "\" is not supported by this platform.";
				}

				ConfirmWindow confirmWindow = new ConfirmWindow((MainWindow) getWindow(), "Upload Error", StyleUtils.getLabel(message));

				confirmWindow.setWidth("400px");

				confirmWindow.addListener(new CloseListener()
				{
					private static final long serialVersionUID = -5264116947530035902L;

					@Override
					public void windowClose(CloseEvent e)
					{
						cancelUpload();
					}
				});

				getWindow().addWindow(confirmWindow);
			}
		}
		else
		{
			finishCurrentUploadBatch();
		}
	}

	/**
	 * Uploads the currently processed file.
	 * 
	 * @param uploadSettings The {@code UploadSettings} of the file
	 * @param filesWithSameSettings A {@code List} with the names of the files
	 *        which should get the given {@code UploadSettings} as presetting
	 */
	public void upload(final UploadSettings uploadSettings, List<String> filesWithSameSettings)
	{
		final FileInfo fileInfo = currentFile;

		settings.put(fileInfo.getName(), uploadSettings);

		for(String fileName : filesWithSameSettings)
		{
			preSettings.put(fileName, uploadSettings);
		}

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				ProgressIndicator progressIndicator = multipleUpload.upload(fileInfo);

				synchronized(application)
				{
					VerticalLayout uploadBoxLayout = new VerticalLayout();
					mainLayout.addComponent(uploadBoxLayout);

					HorizontalLayout fileInfoLayout = new HorizontalLayout();
					uploadBoxLayout.addComponent(fileInfoLayout);

					Label nameLabel = StyleUtils.getLabelBold(fileInfo.getName());
					fileInfoLayout.addComponent(nameLabel);
					fileInfoLayout.addComponent(StyleUtils.getLabelSmallHTML("&nbsp;&nbsp;&nbsp;"));
					fileInfoLayout.setComponentAlignment(nameLabel, Alignment.MIDDLE_LEFT);

					BigDecimal fileSize = new BigDecimal((((double) fileInfo.getSize()) / 1000000.0d));
					fileSize = fileSize.setScale(2, BigDecimal.ROUND_HALF_UP);

					Label typeSizeLabel = StyleUtils.getLabelSmallHTML(fileInfo.getType() + "&nbsp;&nbsp;--&nbsp;&nbsp;" + fileSize.toPlainString() + " MB");
					fileInfoLayout.addComponent(typeSizeLabel);
					fileInfoLayout.setComponentAlignment(typeSizeLabel, Alignment.MIDDLE_LEFT);

					uploadBoxLayout.addComponent(StyleUtils.getVerticalSpace("100%", "8px"));

					uploadBoxLayout.addComponent(progressIndicator);

					uploadBoxLayout.addComponent(StyleUtils.getVerticalSpace("100%", "8px"));

					HorizontalLayout resultLayout = new HorizontalLayout();
					uploadBoxLayout.addComponent(resultLayout);

					Label uploadedLabel = StyleUtils.getLabelSmallHTML("Uploaded:&nbsp;");
					resultLayout.addComponent(uploadedLabel);
					resultLayout.setComponentAlignment(uploadedLabel, Alignment.MIDDLE_LEFT);

					Embedded uploadedEmbedded = new Embedded(null, ResourceUtils.RUNO_ICON_32_GLOBE_RESOURCE);
					uploadedEmbedded.addStyleName("image-opacity-65");
					uploadedEmbedded.setType(Embedded.TYPE_IMAGE);
					uploadedEmbedded.setImmediate(true);
					uploadedEmbedded.setWidth("22px");
					uploadedEmbedded.setHeight("22px");
					resultLayout.addComponent(uploadedEmbedded);

					uploadedEmbeddeds.put(fileInfo, uploadedEmbedded);

					resultLayout.addComponent(StyleUtils.getLabelSmallHTML("&nbsp;&nbsp;&nbsp;"));

					Label publishedLabel = StyleUtils.getLabelSmallHTML("Published:&nbsp;");
					resultLayout.addComponent(publishedLabel);
					resultLayout.setComponentAlignment(publishedLabel, Alignment.MIDDLE_LEFT);

					Embedded publishedEmbedded = new Embedded(null, ResourceUtils.RUNO_ICON_32_GLOBE_RESOURCE);
					publishedEmbedded.addStyleName("image-opacity-65");
					publishedEmbedded.setType(Embedded.TYPE_IMAGE);
					publishedEmbedded.setImmediate(true);
					publishedEmbedded.setWidth("22px");
					publishedEmbedded.setHeight("22px");
					resultLayout.addComponent(publishedEmbedded);

					publishedEmbeddeds.put(fileInfo, publishedEmbedded);

					mainLayout.addComponent(StyleUtils.getVerticalSpace("100%", "5px"));

					requestRepaint();
				}
			}
		}).start();

		handleNextFile();
	}

	/**
	 * Cancels the currently processed upload.
	 */
	public void cancelUpload()
	{
		if(currentFile != null)
		{
			multipleUpload.cancelUpload(currentFile);
		}

		handleNextFile();
	}

	/**
	 * Cancels all uploads.
	 */
	public void cancelAllUploads()
	{
		if(currentFile != null)
		{
			multipleUpload.cancelUpload(currentFile);
		}

		if(files.size() > 0)
		{
			multipleUpload.cancelUploads(new Vector<FileInfo>(files.values()));
		}

		finishCurrentUploadBatch();
	}

	/**
	 * Finishes the current upload batch.
	 */
	private void finishCurrentUploadBatch()
	{
		files.clear();
		fileNames.clear();
		preSettings.clear();
		currentFile = null;

		multipleUpload.finishCurrentUploadBatch();
	}

	@Override
	public void update(Observable<UserSession> observable, UserSession userSession)
	{
		if(componentsAdded)
		{
			if(userSession == null)
			{
				multipleUpload.cancelAllUploads();

				finishCurrentUploadBatch();

				removeAllComponents();
			}
			else
			{
				loadComponents();
			}
		}
	}
}
