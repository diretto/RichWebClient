package org.diretto.web.richwebclient.view.util;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.themes.Reindeer;

/**
 * {@code StyleUtils} is a noninstantiable utility class and provides
 * miscellaneous methods for styling the graphical user interface.
 * 
 * @author Tobias Schlecht
 */
public final class StyleUtils
{
	/**
	 * The constructor is {@code private} to suppress the default constructor
	 * for noninstantiability.
	 */
	private StyleUtils()
	{
		throw new AssertionError();
	}

	/**
	 * Creates and returns a <i>regular</i> {@link Label} with the given
	 * content. <br/><br/>
	 * 
	 * <i>Annotation:</i> Content mode {@link Label#CONTENT_TEXT}
	 * 
	 * @param content The content of the {@code Label}
	 * @return The created {@code Label}
	 */
	public static Label getLabel(String content)
	{
		return new Label(content, Label.CONTENT_TEXT);
	}

	/**
	 * Creates and returns a <i>regular</i> {@link Label} with the given
	 * content. <br/><br/>
	 * 
	 * <i>Annotation:</i> Content mode {@link Label#CONTENT_XHTML}
	 * 
	 * @param content The content of the {@code Label}
	 * @return The created {@code Label}
	 */
	public static Label getLabelHTML(String content)
	{
		return new Label(content, Label.CONTENT_XHTML);
	}

	/**
	 * Creates and returns a <i>bold</i> {@link Label} with the given content.
	 * <br/><br/>
	 * 
	 * <i>Annotation:</i> Content mode {@link Label#CONTENT_TEXT}
	 * 
	 * @param content The content of the {@code Label}
	 * @return The created {@code Label}
	 */
	public static Label getLabelBold(String content)
	{
		Label label = new Label(content, Label.CONTENT_TEXT);

		label.addStyleName("label-bold");

		return label;
	}

	/**
	 * Creates and returns a <i>bold</i> {@link Label} with the given content.
	 * <br/><br/>
	 * 
	 * <i>Annotation:</i> Content mode {@link Label#CONTENT_XHTML}
	 * 
	 * @param content The content of the {@code Label}
	 * @return The created {@code Label}
	 */
	public static Label getLabelBoldHTML(String content)
	{
		Label label = new Label(content, Label.CONTENT_XHTML);

		label.addStyleName("label-bold");

		return label;
	}

	/**
	 * Creates and returns an <i>italic</i> {@link Label} with the given
	 * content. <br/><br/>
	 * 
	 * <i>Annotation:</i> Content mode {@link Label#CONTENT_TEXT}
	 * 
	 * @param content The content of the {@code Label}
	 * @return The created {@code Label}
	 */
	public static Label getLabelItalic(String content)
	{
		Label label = new Label(content, Label.CONTENT_TEXT);

		label.addStyleName("label-italic");

		return label;
	}

	/**
	 * Creates and returns an <i>italic</i> {@link Label} with the given
	 * content. <br/><br/>
	 * 
	 * <i>Annotation:</i> Content mode {@link Label#CONTENT_XHTML}
	 * 
	 * @param content The content of the {@code Label}
	 * @return The created {@code Label}
	 */
	public static Label getLabelItalicHTML(String content)
	{
		Label label = new Label(content, Label.CONTENT_XHTML);

		label.addStyleName("label-italic");

		return label;
	}

	/**
	 * Creates and returns an <i>code</i> {@link Label} with the given content.
	 * <br/><br/>
	 * 
	 * <i>Annotation:</i> Content mode {@link Label#CONTENT_XHTML}
	 * 
	 * @param content The content of the {@code Label}
	 * @return The created {@code Label}
	 */
	public static Label getLabelCodeHTML(String content)
	{
		return new Label("<code>" + content + "</code>", Label.CONTENT_XHTML);
	}

	/**
	 * Creates and returns a {@code H1} {@link Label} with the given content.
	 * <br/><br/>
	 * 
	 * <i>Annotation:</i> Content mode {@link Label#CONTENT_TEXT}
	 * 
	 * @param content The content of the {@code Label}
	 * @return The created {@code Label}
	 */
	public static Label getLabelH1(String content)
	{
		Label label = new Label(content, Label.CONTENT_TEXT);

		label.setStyleName(Reindeer.LABEL_H1);

		return label;
	}

	/**
	 * Creates and returns a {@code H1} {@link Label} with the given content.
	 * <br/><br/>
	 * 
	 * <i>Annotation:</i> Content mode {@link Label#CONTENT_XHTML}
	 * 
	 * @param content The content of the {@code Label}
	 * @return The created {@code Label}
	 */
	public static Label getLabelH1HTML(String content)
	{
		Label label = new Label(content, Label.CONTENT_XHTML);

		label.setStyleName(Reindeer.LABEL_H1);

		return label;
	}

	/**
	 * Creates and returns a {@code H2} {@link Label} with the given content.
	 * <br/><br/>
	 * 
	 * <i>Annotation:</i> Content mode {@link Label#CONTENT_TEXT}
	 * 
	 * @param content The content of the {@code Label}
	 * @return The created {@code Label}
	 */
	public static Label getLabelH2(String content)
	{
		Label label = new Label(content, Label.CONTENT_TEXT);

		label.setStyleName(Reindeer.LABEL_H2);

		return label;
	}

	/**
	 * Creates and returns a {@code H2} {@link Label} with the given content.
	 * <br/><br/>
	 * 
	 * <i>Annotation:</i> Content mode {@link Label#CONTENT_XHTML}
	 * 
	 * @param content The content of the {@code Label}
	 * @return The created {@code Label}
	 */
	public static Label getLabelH2HTML(String content)
	{
		Label label = new Label(content, Label.CONTENT_XHTML);

		label.setStyleName(Reindeer.LABEL_H2);

		return label;
	}

	/**
	 * Creates and returns a <i>small</i> {@link Label} with the given content.
	 * <br/><br/>
	 * 
	 * <i>Annotation:</i> Content mode {@link Label#CONTENT_TEXT}
	 * 
	 * @param content The content of the {@code Label}
	 * @return The created {@code Label}
	 */
	public static Label getLabelSmall(String content)
	{
		Label label = new Label(content, Label.CONTENT_TEXT);

		label.setStyleName(Reindeer.LABEL_SMALL);

		return label;
	}

	/**
	 * Creates and returns a <i>small</i> {@link Label} with the given content.
	 * <br/><br/>
	 * 
	 * <i>Annotation:</i> Content mode {@link Label#CONTENT_XHTML}
	 * 
	 * @param content The content of the {@code Label}
	 * @return The created {@code Label}
	 */
	public static Label getLabelSmallHTML(String content)
	{
		Label label = new Label(content, Label.CONTENT_XHTML);

		label.setStyleName(Reindeer.LABEL_SMALL);

		return label;
	}

	/**
	 * Creates and returns a {@link Label} with a <i>horizontal line</i>.
	 * 
	 * @return The created {@code Label}
	 */
	public static Label getHorizontalLine()
	{
		return new Label("<hr />", Label.CONTENT_XHTML);
	}

	/**
	 * Creates and returns a {@link Label} with a <i>horizontal line</i>.
	 * 
	 * @param width The width of the line
	 * @return The created {@code Label}
	 */
	public static Label getHorizontalLine(String width)
	{
		String label = "<hr style=\"width:" + width + ";\" />";

		return new Label(label, Label.CONTENT_XHTML);
	}

	/**
	 * Returns a {@link CssLayout} which will serve as vertical space.
	 * 
	 * @param width The width of the space
	 * @param height The height of the space
	 * @return The vertical space
	 */
	public static CssLayout getVerticalSpace(String width, String height)
	{
		CssLayout cssLayout = new CssLayout();

		cssLayout.setMargin(false);
		cssLayout.setWidth(width);
		cssLayout.setHeight(height);

		return cssLayout;
	}

	/**
	 * Returns a {@link CssLayout} which will serve as vertical space.
	 * 
	 * @return The vertical space
	 */
	public static CssLayout getVerticalSpaceSmall()
	{
		return getVerticalSpace("100%", "3px");
	}

	/**
	 * Returns the HTML {@code String} representation for a {@link Link} with
	 * the given data.
	 * 
	 * @param value The value (HTML encoded) to be displayed
	 * @param href The {@code String} representation of the hyper reference
	 * @return The HTML {@code String} representation for a {@code Link}
	 */
	public static String getHTMLLink(String value, String href)
	{
		return "<div class=\"v-link\" style=\"display:inline; line-height:12px; position:relative; top:-2px;\"><a href=\"" + href + "\"><span>" + value + "</span></a></div>";
	}
}
