package org.diretto.web.richwebclient.view.widgets.upload.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.configuration.XMLConfiguration;
import org.diretto.web.richwebclient.RichWebClientApplication;
import org.diretto.web.richwebclient.view.widgets.upload.client.VMultipleUpload;
import org.diretto.web.richwebclient.view.widgets.upload.client.base.FileInfo;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.StreamVariable;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.ProgressIndicator;

/**
 * The server side component of the {@code MultipleUpload}.
 * 
 * @author Tobias Schlecht
 */
@ClientWidget(VMultipleUpload.class)
public final class MultipleUpload extends AbstractComponent
{
	private static final long serialVersionUID = -6415400052804921797L;

	private final String tempFolderPath;
	private final int maxParallelStreams;

	private final List<MultipleUploadHandler> multipleUploadHandlers = new CopyOnWriteArrayList<MultipleUploadHandler>();

	private String buttonCaption;
	private String buttonStyleName;

	private boolean stopPolling = false;

	private List<FileInfo> fileInfos = null;

	private final Map<String, MultipleUploadStreamVariable> streamVariables = new ConcurrentHashMap<String, MultipleUploadStreamVariable>();

	private final List<String> uploadFiles = new CopyOnWriteArrayList<String>();
	private final List<String> cancelFiles = new CopyOnWriteArrayList<String>();

	private final Map<String, File> files = new ConcurrentHashMap<String, File>();

	private Timer timer = null;

	/**
	 * Constructs a {@link MultipleUpload}.
	 * 
	 * @param application The corresponding {@code RichWebClientApplication}
	 * @param caption The caption of the upload button
	 */
	public MultipleUpload(RichWebClientApplication application, String caption)
	{
		this(application, caption, "");
	}

	/**
	 * Constructs a {@link MultipleUpload}.
	 * 
	 * @param application The corresponding {@code RichWebClientApplication}
	 * @param caption The caption of the upload button
	 * @param styleName The style of the upload button
	 */
	public MultipleUpload(RichWebClientApplication application, String caption, String styleName)
	{
		buttonCaption = caption;
		buttonStyleName = styleName;

		XMLConfiguration xmlConfiguration = application.getXMLConfiguration();

		tempFolderPath = xmlConfiguration.getString("upload/temp-folder-path");
		maxParallelStreams = xmlConfiguration.getInt("upload/max-parallel-streams");
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException
	{
		super.paintContent(target);

		target.addAttribute("buttonStyleName", buttonStyleName);

		target.addVariable(this, "buttonCaption", buttonCaption);

		if(stopPolling && uploadFiles.size() == 0)
		{
			target.addVariable(this, "stopPolling", true);
		}
		else
		{
			target.addVariable(this, "stopPolling", false);
		}

		String[] uploads = uploadFiles.toArray(new String[0]);

		if(uploads.length > maxParallelStreams)
		{
			String[] limitedUploads = new String[maxParallelStreams];

			for(int i = 0; i < maxParallelStreams; i++)
			{
				limitedUploads[i] = uploads[i];
			}

			uploads = limitedUploads;
		}

		for(String fileName : uploads)
		{
			target.addVariable(this, "target_" + fileName.hashCode(), streamVariables.get(fileName));
		}

		target.addVariable(this, "uploadFiles", uploads);
		target.addVariable(this, "cancelFiles", cancelFiles.toArray(new String[0]));
	}

	@Override
	public void setCaption(String caption)
	{
		buttonCaption = caption;

		requestRepaint();
	}

	@Override
	public void changeVariables(Object source, Map<String, Object> variables)
	{
		super.changeVariables(source, variables);

		if(variables.containsKey("stopPolling"))
		{
			stopPolling = (Boolean) variables.get("stopPolling");
		}

		if(variables.containsKey("fileInfos"))
		{
			String[] fileInfoStrings = (String[]) variables.get("fileInfos");

			fileInfos = new CopyOnWriteArrayList<FileInfo>();

			for(String fileInfoString : fileInfoStrings)
			{
				if(fileInfoString != null)
				{
					FileInfo fileInfo = FileInfo.fromString(fileInfoString);

					if(!fileInfos.contains(fileInfo))
					{
						fileInfos.add(fileInfo);
					}
				}
			}

			for(MultipleUploadHandler multipleUploadHandler : multipleUploadHandlers)
			{
				multipleUploadHandler.onUploadsSelected(new Vector<FileInfo>(fileInfos));
			}
		}

		if(variables.containsKey("cancelFilesConfirmed"))
		{
			String[] cancelFilesConfirmed = (String[]) variables.get("cancelFilesConfirmed");

			for(String file : cancelFilesConfirmed)
			{
				cancelFiles.remove(file);
			}
		}
	}

	/**
	 * Uploads the file with the given {@link FileInfo}.
	 * 
	 * @param fileInfo The {@code FileInfo} of the file to be uploaded
	 * @return A {@code ProgressIndicator} for this upload procedure
	 */
	public ProgressIndicator upload(FileInfo fileInfo)
	{
		if(fileInfo == null)
		{
			throw new NullPointerException();
		}

		if(!files.containsKey(fileInfo.getName()))
		{
			File file = new File(tempFolderPath + "upload-" + UUID.randomUUID() + "-" + Math.abs(fileInfo.hashCode()) + ".tmp");

			files.put(fileInfo.getName(), file);

			MultipleUploadStreamVariable streamVariable = new MultipleUploadStreamVariable(fileInfo, file);
			streamVariables.put(fileInfo.getName(), streamVariable);

			if(!uploadFiles.contains(fileInfo.getName()))
			{
				uploadFiles.add(fileInfo.getName());
			}

			if(timer == null)
			{
				timer = new Timer();

				timer.schedule(new TimerTask()
				{
					@Override
					public void run()
					{
						requestRepaint();

						if(timer != null && uploadFiles.size() == 0)
						{
							timer.cancel();

							timer = null;
						}
					}
				}, 0, 2000);
			}

			return streamVariable.getProgressIndicator();
		}
		else
		{
			MultipleUploadStreamVariable streamVariable = streamVariables.get(fileInfo.getName());

			if(streamVariable != null)
			{
				return streamVariable.getProgressIndicator();
			}

			return null;
		}
	}

	/**
	 * Cancels the upload with the given {@link FileInfo}.
	 * 
	 * @param fileInfo The {@code FileInfo} of the upload to be cancelled
	 */
	public void cancelUpload(FileInfo fileInfo)
	{
		if(fileInfo == null)
		{
			throw new NullPointerException();
		}

		MultipleUploadStreamVariable multipleUploadStreamVariable = streamVariables.get(fileInfo.getName());

		if(multipleUploadStreamVariable != null)
		{
			multipleUploadStreamVariable.cancelStreaming();
		}
		else
		{
			if(!cancelFiles.contains(fileInfo.getName()))
			{
				cancelFiles.add(fileInfo.getName());
			}
		}

		requestRepaint();
	}

	/**
	 * Cancels the uploads with the given {@link FileInfo}s.
	 * 
	 * @param fileInfos The {@code FileInfo}s of the uploads to be cancelled
	 */
	public void cancelUploads(List<FileInfo> fileInfos)
	{
		if(fileInfos == null)
		{
			throw new NullPointerException();
		}

		for(FileInfo fileInfo : fileInfos)
		{
			cancelUpload(fileInfo);
		}
	}

	/**
	 * Cancels all uploads.
	 */
	public void cancelAllUploads()
	{
		if(fileInfos != null)
		{
			cancelUploads(new Vector<FileInfo>(fileInfos));
		}
	}

	/**
	 * Finishes the current upload batch.
	 */
	public void finishCurrentUploadBatch()
	{
		stopPolling = true;

		requestRepaint();
	}

	/**
	 * Adds the given {@link MultipleUploadHandler}.
	 * 
	 * @param multipleUploadHandler A {@code MultipleUploadHandler}
	 */
	public void addMultipleUploadHandler(MultipleUploadHandler multipleUploadHandler)
	{
		multipleUploadHandlers.add(multipleUploadHandler);
	}

	/**
	 * This interface represents a handler for {@link MultipleUpload} events.
	 */
	public interface MultipleUploadHandler
	{
		/**
		 * Called when the files to be uploaded have been selected.
		 * 
		 * @param fileInfos The {@code FileInfo}s of the files to be uploaded
		 */
		void onUploadsSelected(List<FileInfo> fileInfos);

		/**
		 * Called when an upload procedure has been started.
		 * 
		 * @param fileInfo The {@code FileInfo} of the corresponding file
		 */
		void onUploadStarted(FileInfo fileInfo);

		/**
		 * Called when an upload procedure has been finished. <br/><br/>
		 * 
		 * <i>Annotation:</i> The {@link File} object should be deleted if it is
		 * no longer used. This should be done by invoking the method
		 * {@link File#delete()}.
		 * 
		 * @param fileInfo The {@code FileInfo} of the corresponding file
		 * @param file The corresponding {@code File} object
		 */
		void onUploadFinished(FileInfo fileInfo, File file);

		/**
		 * Called when an upload procedure has been failed.
		 * 
		 * @param fileInfo The {@code FileInfo} of the corresponding file
		 * @param exception The corresponding {@code Exception}
		 */
		void onUploadFailed(FileInfo fileInfo, Exception exception);
	}

	/**
	 * This class is an implementation class of the {@link StreamVariable}
	 * interface and serves as a special {@code StreamVariable} for a
	 * {@link MultipleUpload}.
	 */
	public class MultipleUploadStreamVariable implements com.vaadin.terminal.StreamVariable
	{
		private static final long serialVersionUID = 1772565622658765956L;

		private final FileInfo fileInfo;
		private final File file;
		private final ProgressIndicator progressIndicator;

		private boolean interrupted = false;

		/**
		 * Constructs a {@link MultipleUploadStreamVariable} for the given data.
		 * 
		 * @param fileInfo The {@code FileInfo} of the file to be uploaded
		 * @param file The {@code File} object
		 */
		public MultipleUploadStreamVariable(FileInfo fileInfo, File file)
		{
			this.fileInfo = fileInfo;
			this.file = file;

			progressIndicator = new ProgressIndicator(0.0f);
			progressIndicator.setPollingInterval(500);
		}

		@Override
		public OutputStream getOutputStream()
		{
			try
			{
				return new FileOutputStream(file);
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();

				return null;
			}
		}

		@Override
		public boolean listenProgress()
		{
			return true;
		}

		@Override
		public void onProgress(StreamingProgressEvent event)
		{
			progressIndicator.setValue((double) event.getBytesReceived() / (double) event.getContentLength());
		}

		@Override
		public void streamingStarted(StreamingStartEvent event)
		{
			for(MultipleUploadHandler multipleUploadHandler : multipleUploadHandlers)
			{
				multipleUploadHandler.onUploadStarted(fileInfo);
			}
		}

		@Override
		public void streamingFinished(StreamingEndEvent event)
		{
			files.remove(fileInfo.getName());
			streamVariables.remove(fileInfo.getName());
			uploadFiles.remove(fileInfo.getName());

			for(MultipleUploadHandler multipleUploadHandler : multipleUploadHandlers)
			{
				multipleUploadHandler.onUploadFinished(fileInfo, file);
			}

			progressIndicator.setPollingInterval(Integer.MAX_VALUE);
		}

		@Override
		public void streamingFailed(StreamingErrorEvent event)
		{
			File file = files.remove(fileInfo.getName());

			if(file != null && file.exists())
			{
				file.delete();
			}

			streamVariables.remove(fileInfo.getName());
			uploadFiles.remove(fileInfo.getName());

			for(MultipleUploadHandler multipleUploadHandler : multipleUploadHandlers)
			{
				multipleUploadHandler.onUploadFailed(fileInfo, event.getException());
			}

			progressIndicator.setPollingInterval(Integer.MAX_VALUE);
		}

		@Override
		public boolean isInterrupted()
		{
			return interrupted;
		}

		/**
		 * Returns the corresponding {@link ProgressIndicator}.
		 * 
		 * @return The corresponding {@code ProgressIndicator}
		 */
		public ProgressIndicator getProgressIndicator()
		{
			return progressIndicator;
		}

		/**
		 * Cancels the current streaming procedure.
		 */
		public void cancelStreaming()
		{
			interrupted = true;
		}
	}
}
