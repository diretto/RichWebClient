package org.diretto.web.richwebclient.view.util;

import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;

/**
 * {@code ResourceUtils} is a noninstantiable utility class and is responsible
 * for all {@link Resource} related aspects on the server side.
 * 
 * @author Tobias Schlecht
 */
public final class ResourceUtils
{
	public static final ThemeResource DIRETTO_LOGO_RESOURCE = new ThemeResource("layout/images/diretto-logo.png");
	public static final ThemeResource VAADIN_LOGO_RESOURCE = new ThemeResource("base/images/vaadin-logo.png");
	public static final ThemeResource ULM_UNIVERSITY_LOGO_RESOURCE = new ThemeResource("content/images/ulm-university-logo.gif");
	public static final ThemeResource MFG_LOGO_RESOURCE = new ThemeResource("content/images/mfg-logo.png");
	public static final ThemeResource BASE_AJAX_LOADER_BIG_RESOURCE = new ThemeResource("../base/common/img/ajax-loader-big.gif");
	public static final ThemeResource RUNO_ICON_32_CANCEL_RESOURCE = new ThemeResource("../runo/icons/32/cancel.png");
	public static final ThemeResource RUNO_ICON_32_GLOBE_RESOURCE = new ThemeResource("../runo/icons/32/globe.png");
	public static final ThemeResource RUNO_ICON_32_OK_RESOURCE = new ThemeResource("../runo/icons/32/ok.png");

	/**
	 * The constructor is {@code private} to suppress the default constructor
	 * for noninstantiability.
	 */
	private ResourceUtils()
	{
		throw new AssertionError();
	}
}
