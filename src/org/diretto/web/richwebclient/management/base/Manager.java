package org.diretto.web.richwebclient.management.base;

import org.diretto.web.richwebclient.RichWebClientApplication;

/**
 * This interface represents a {@code Manager} for a
 * {@link RichWebClientApplication}.
 * 
 * @author Tobias Schlecht
 */
public interface Manager
{
	/**
	 * Returns the corresponding {@link RichWebClientApplication}.
	 * 
	 * @return The corresponding {@code RichWebClientApplication}
	 */
	RichWebClientApplication getApplication();
}
