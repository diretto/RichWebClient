package org.diretto.web.richwebclient.view.base;

import org.diretto.api.client.user.User;

import com.vaadin.ui.Component;

/**
 * This interface represents a {@code Section}.
 * 
 * @author Tobias Schlecht
 */
public interface Section extends Component
{
	/**
	 * Determines whether a {@link User} has to be logged in to view this
	 * {@link Section} or not.
	 * 
	 * @return {@code true} if a {@code User} has to be logged in to view this
	 *         {@code Section}; otherwise {@code false}
	 */
	boolean isLoginNecessary();

	/**
	 * Determines whether this {@link Section} requires the complete width of
	 * the screen.
	 * 
	 * @return {@code true} if the full width is required; otherwise
	 *         {@code false}
	 */
	boolean isFullWidthRequired();

	/**
	 * Returns the title of this {@link Section}.
	 * 
	 * @return The title
	 */
	String getTitle();

	/**
	 * Returns the subtitle of this {@link Section}.
	 * 
	 * @return The subtitle
	 */
	String getSubtitle();

	/**
	 * Adds the content components of this {@link Section}. <br/><br/>
	 * 
	 * <i>Annotation:</i> In many cases it is necessary that a {@code Section}
	 * has been initialized but its contents are not needed yet. Therefore the
	 * addition of the components to the {@code Section} should be performed
	 * within this method. So this method can be called only then, when the
	 * contents are really needed. <br/><br/>
	 * 
	 * <i>Important:</i> It is very important that each {@code Section} assures
	 * that repeated invocations of this methods do not have negative
	 * consequences.
	 */
	void addComponents();
}
