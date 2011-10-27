package org.diretto.web.richwebclient.view.util.client;

import com.vaadin.terminal.Resource;

/**
 * {@code ResourceUtils} is a noninstantiable utility class and is responsible
 * for all {@link Resource} related aspects on the client side.
 * 
 * @author Tobias Schlecht
 */
public final class ResourceUtils
{
	public static final String GOOGLE_MAP_MARKERS_PATH = "VAADIN/themes/diretto/widgets/googlemap/markers/";

	public static final int GOOGLE_MAP_MARKER_ICON_WIDTH = 21;
	public static final int GOOGLE_MAP_MARKER_ICON_HEIGHT = 26;

	/**
	 * The constructor is {@code private} to suppress the default constructor
	 * for noninstantiability.
	 */
	private ResourceUtils()
	{
		throw new AssertionError();
	}
}
