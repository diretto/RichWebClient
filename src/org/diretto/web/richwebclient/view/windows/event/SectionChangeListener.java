package org.diretto.web.richwebclient.view.windows.event;

import org.diretto.web.richwebclient.view.base.Section;
import org.diretto.web.richwebclient.view.windows.MainWindow;

/**
 * This interface represents a {@link MainWindowListener} for {@link MainWindow}
 * events in respect of {@link Section} changes.
 * 
 * @author Tobias Schlecht
 */
public interface SectionChangeListener extends MainWindowListener
{
	/**
	 * Called when the selected {@link Section} has been changed. <br/><br/>
	 * 
	 * <i>Annotation:</i> This method will be invoked before the
	 * {@link Section#addComponents()} method will be invoked.
	 * 
	 * @param section The new selected {@code Section}
	 */
	void onSectionChanged(Section section);
}
