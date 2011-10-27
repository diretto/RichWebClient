package org.diretto.web.richwebclient.view.widgets.upload.client;

import java.util.HashMap;
import java.util.Map;

import org.diretto.web.richwebclient.view.widgets.upload.client.base.FileInfo;
import org.diretto.web.richwebclient.view.widgets.upload.server.MultipleUpload;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.VButton;
import com.vaadin.terminal.gwt.client.ui.dd.VHtml5File;

/**
 * The client side component of the {@link MultipleUpload}.
 * 
 * @author Tobias Schlecht
 */
public final class VMultipleUpload extends Composite implements Paintable
{
	private static final int POLLING_INTERVAL = 3000;

	private HorizontalPanel wrapperPanel = null;
	private VButton button = null;
	private FileUpload fileUpload = null;
	private String buttonCaption = null;
	private Timer timer = null;

	private String paintableID;
	private ApplicationConnection applicationConnection;

	private boolean initialized = false;

	private final Map<String, VHtml5File> files = new HashMap<String, VHtml5File>();

	/**
	 * Constructs a {@link VMultipleUpload}.
	 */
	public VMultipleUpload()
	{
		wrapperPanel = new HorizontalPanel();

		initWidget(wrapperPanel);
	}

	/**
	 * Initializes this {@link Widget}.
	 * 
	 * @param buttonStyleName The style of the upload button
	 */
	private void initUpload(String buttonStyleName)
	{
		timer = new Timer()
		{
			@Override
			public void run()
			{
				applicationConnection.sendPendingVariableChanges();
			}
		};

		fileUpload = new FileUpload();
		fileUpload.addStyleName("outside-screen");

		button = new VButton();

		if(buttonStyleName != null && !buttonStyleName.equals(""))
		{
			button.addStyleName(button.getStylePrimaryName() + "-" + buttonStyleName);
			button.addStyleName(buttonStyleName);
		}

		button.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				fireNativeClick(fileUpload.getElement());
			}
		});

		FormPanel formPanel = new FormPanel();
		formPanel.add(fileUpload);

		wrapperPanel.add(button);
		wrapperPanel.add(formPanel);
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection applicationConnection)
	{
		if(applicationConnection.updateComponent(this, uidl, true))
		{
			return;
		}

		paintableID = uidl.getId();

		this.applicationConnection = applicationConnection;

		if(!initialized)
		{
			initUpload(uidl.getStringAttribute("buttonStyleName"));

			initialized = true;
		}

		if(!uidl.getStringVariable("buttonCaption").equals(buttonCaption))
		{
			buttonCaption = uidl.getStringVariable("buttonCaption");

			button.setText(buttonCaption);
		}

		if(initialized)
		{
			String[] uploadFiles = uidl.getStringArrayVariable("uploadFiles");

			for(String fileName : uploadFiles)
			{
				if(files.containsKey(fileName))
				{
					post(uidl.getStringVariable("target_" + fileName.hashCode()), fileName);
				}
			}

			String[] cancelFiles = uidl.getStringArrayVariable("cancelFiles");

			for(String fileName : cancelFiles)
			{
				files.remove(fileName);
			}

			applicationConnection.updateVariable(paintableID, "cancelFilesConfirmed", cancelFiles, false);

			if(uidl.getBooleanVariable("stopPolling"))
			{
				timer.cancel();

				button.setEnabled(true);

				applicationConnection.updateVariable(paintableID, "stopPolling", false, true);
			}
		}
	}

	/**
	 * Executes the HTTP POST operation for the given data.
	 * 
	 * @param targetURL The target {@code URL}
	 * @param fileName The name of the file
	 */
	private void post(String targetURL, String fileName)
	{
		final VHtml5File file = files.remove(fileName);

		XMLHttpRequest xmlHttpRequest = (XMLHttpRequest) XMLHttpRequest.create();

		xmlHttpRequest.open("POST", applicationConnection.translateVaadinUri(targetURL));
		xmlHttpRequest.post(file);
	}

	/**
	 * This <i>member</i> class extends the
	 * {@link com.google.gwt.user.client.ui.FileUpload} class, allows to select
	 * multiple files and handles the upcoming browser events.
	 */
	private final class FileUpload extends com.google.gwt.user.client.ui.FileUpload
	{
		/**
		 * Creates a {@link FileUpload}.
		 */
		private FileUpload()
		{
			super();

			getElement().setPropertyString("multiple", "multiple");

			sinkEvents(Event.ONCHANGE);
		}

		@Override
		public void onBrowserEvent(Event event)
		{
			super.onBrowserEvent(event);

			button.setEnabled(false);

			int fileNumber = getNativeFileNumber(fileUpload.getElement());

			String[] fileInfoStrings = new String[fileNumber];

			for(int i = 0; i < fileNumber; i++)
			{
				VHtml5File file = getNativeFile(fileUpload.getElement(), i);

				files.put(file.getName(), file);

				FileInfo fileInfo = new FileInfo(file.getName(), file.getType(), file.getSize());

				fileInfoStrings[i] = fileInfo.toString();
			}

			timer.scheduleRepeating(POLLING_INTERVAL);

			applicationConnection.updateVariable(paintableID, "fileInfos", fileInfoStrings, true);
		}
	}

	/**
	 * This <i>static member</i> class extends the
	 * {@link com.google.gwt.xhr.client.XMLHttpRequest} class and is responsible
	 * for the specific <i>native</i> parts of the HTTP POST operation.
	 */
	private static final class XMLHttpRequest extends com.google.gwt.xhr.client.XMLHttpRequest
	{
		/**
		 * Creates a {@link XMLHttpRequest}.
		 */
		protected XMLHttpRequest()
		{
			super();
		}

		/**
		 * Executes the specific <i>native</i> parts of the HTTP POST operation
		 * for the given file.
		 * 
		 * @param file The file to post
		 */
		public native void post(VHtml5File file)
		/*-{
		    this.setRequestHeader('Accept', 'text/html');
		    this.setRequestHeader('Content-Type', 'multipart/form-data');
		    this.send(file);
		}-*/;
	}

	/**
	 * Triggers a click event at the given {@code DOM} {@link Element}.
	 * 
	 * @param element The corresponding {@code DOM} {@code Element}
	 */
	private static native void fireNativeClick(Element element)
	/*-{
	    element.click();
	}-*/;

	/**
	 * Returns the number of files of the given {@code DOM} {@link Element}.
	 * 
	 * @param element The corresponding {@code DOM} {@code Element}
	 * @return The number of files
	 */
	private static native int getNativeFileNumber(Element element)
	/*-{
	 	return element.files.length;
	}-*/;

	/**
	 * Returns the file with the given index from the given {@code DOM}
	 * {@link Element}.
	 * 
	 * @param element The corresponding {@code DOM} {@code Element}
	 * @param index The index of the file to be returned
	 * @return The requested {@code VHtml5File}
	 */
	private static native VHtml5File getNativeFile(Element element, int index)
	/*-{
		return element.files[index];
	}-*/;
}
