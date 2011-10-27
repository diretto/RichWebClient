package org.diretto.web.richwebclient.view.sections;

import org.diretto.api.client.session.UserSession;
import org.diretto.api.client.util.Observable;
import org.diretto.web.richwebclient.RichWebClientApplication;
import org.diretto.web.richwebclient.view.base.AbstractSection;
import org.diretto.web.richwebclient.view.util.ResourceUtils;
import org.diretto.web.richwebclient.view.util.StyleUtils;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * This class represents a {@code HomeSection}.
 * 
 * @author Tobias Schlecht
 */
public final class HomeSection extends AbstractSection
{
	private static final long serialVersionUID = 4053949590191778303L;

	private boolean componentsAdded = false;

	/**
	 * Constructs a {@link HomeSection}.
	 * 
	 * @param application The corresponding {@code RichWebClientApplication}
	 */
	public HomeSection(RichWebClientApplication application)
	{
		super(application.getAuthenticationRegistry(), false, false, "Home", "Welcome to diretto");
	}

	@Override
	public synchronized void addComponents()
	{
		if(!componentsAdded)
		{
			GridLayout mainLayout = new GridLayout(2, 1);
			addComponent(mainLayout);
			setExpandRatio(mainLayout, 1.0f);

			VerticalLayout leftLayout = new VerticalLayout();
			leftLayout.setStyleName(Reindeer.LAYOUT_BLACK);
			leftLayout.setWidth("350px");
			leftLayout.setMargin(true);
			leftLayout.setSpacing(true);

			leftLayout.addComponent(StyleUtils.getLabelH2("Rich Web Client"));

			leftLayout.addComponent(StyleUtils.getHorizontalLine());
			leftLayout.addComponent(StyleUtils.getLabelBold("Demo Application"));
			leftLayout.addComponent(StyleUtils.getVerticalSpaceSmall());
			leftLayout.addComponent(StyleUtils.getLabelHTML("This <i>Rich Web Application</i> is only a Demo Application and hence shows nowhere near all functionalities which are provided by the background platform components (API, Server and Java Client Libraries)."));

			leftLayout.addComponent(StyleUtils.getHorizontalLine());
			leftLayout.addComponent(StyleUtils.getLabelBold("Testing"));
			leftLayout.addComponent(StyleUtils.getVerticalSpaceSmall());
			leftLayout.addComponent(StyleUtils.getLabelHTML("Feel free to test the implemented functionalities and create your own Account, but due to the fact that <i>diretto</i> is under continuous development, the platform components (e.g. the Database) will be reseted, changed or replaced from time to time. Therefore the <i>diretto</i> Project Team makes no warranties, whether, how long or where your committed data will be stored or displayed."));

			leftLayout.addComponent(StyleUtils.getHorizontalLine());
			leftLayout.addComponent(StyleUtils.getLabelBold("Feedback"));
			leftLayout.addComponent(StyleUtils.getVerticalSpaceSmall());
			leftLayout.addComponent(StyleUtils.getLabelHTML("We would be pleased to receive your feedback, particularly if you discover any errors. Our Contact Details (Email, Website, GitHub, Twitter) can be found in the <i>Contact</i> section of this application."));

			mainLayout.addComponent(leftLayout);

			VerticalLayout wrapperLayout = new VerticalLayout();
			wrapperLayout.setWidth("530px");
			wrapperLayout.setMargin(false, true, false, true);

			VerticalLayout rightLayout = new VerticalLayout();
			rightLayout.setWidth("450px");
			rightLayout.setMargin(true);
			rightLayout.setSpacing(true);

			rightLayout.addComponent(StyleUtils.getLabelH2("Development"));
			rightLayout.addComponent(StyleUtils.getHorizontalLine());
			rightLayout.addComponent(StyleUtils.getVerticalSpaceSmall());

			rightLayout.addComponent(StyleUtils.getLabelBold("Vaadin"));
			rightLayout.addComponent(StyleUtils.getVerticalSpaceSmall());
			Link vaadinLogo = new Link(null, new ExternalResource("https://vaadin.com"));
			vaadinLogo.setStyleName("link-width-250");
			vaadinLogo.setIcon(ResourceUtils.VAADIN_LOGO_RESOURCE);
			rightLayout.addComponent(vaadinLogo);
			rightLayout.addComponent(StyleUtils.getVerticalSpaceSmall());
			HorizontalLayout vaadinDescriptionlayout = new HorizontalLayout();
			vaadinDescriptionlayout.setWidth("100%");
			rightLayout.addComponent(vaadinDescriptionlayout);
			vaadinDescriptionlayout.addComponent(StyleUtils.getLabelHTML("For the development of this <i>Rich Web Application</i> (front-end) we used the Open Source Java Framework " + StyleUtils.getHTMLLink("Vaadin", "https://vaadin.com/") + ". More information about the other platform components and technologies can be found on our " + StyleUtils.getHTMLLink("Project Website", "http://www.diretto.org/platform/") + "."));

			rightLayout.addComponent(StyleUtils.getVerticalSpaceSmall());
			rightLayout.addComponent(StyleUtils.getHorizontalLine());
			rightLayout.addComponent(StyleUtils.getVerticalSpaceSmall());

			rightLayout.addComponent(StyleUtils.getLabelBold("Ulm University"));
			rightLayout.addComponent(StyleUtils.getVerticalSpaceSmall());
			rightLayout.addComponent(StyleUtils.getVerticalSpaceSmall());
			Link ulmUniversityLogo = new Link(null, new ExternalResource("http://www.uni-ulm.de/"));
			ulmUniversityLogo.setStyleName("link-width-250");
			ulmUniversityLogo.setIcon(ResourceUtils.ULM_UNIVERSITY_LOGO_RESOURCE);
			rightLayout.addComponent(ulmUniversityLogo);
			rightLayout.addComponent(StyleUtils.getVerticalSpaceSmall());
			rightLayout.addComponent(StyleUtils.getVerticalSpaceSmall());
			HorizontalLayout ulmUniversityDescriptionLayout = new HorizontalLayout();
			ulmUniversityDescriptionLayout.setWidth("100%");
			rightLayout.addComponent(ulmUniversityDescriptionLayout);
			ulmUniversityDescriptionLayout.addComponent(StyleUtils.getLabelHTML("The current project members Benjamin Erb and Tobias Schlecht have been studying Media Informatics at the " + StyleUtils.getHTMLLink("Ulm University", "http://www.uni-ulm.de/") + ". The advisor for the project is Florian Schaub and the supervisor is Prof. Dr. Michael Weber."));

			rightLayout.addComponent(StyleUtils.getVerticalSpaceSmall());
			rightLayout.addComponent(StyleUtils.getHorizontalLine());
			rightLayout.addComponent(StyleUtils.getVerticalSpaceSmall());

			rightLayout.addComponent(StyleUtils.getLabelBoldHTML("MFG Stiftung Baden-W&uuml;rttemberg"));
			rightLayout.addComponent(StyleUtils.getVerticalSpaceSmall());
			rightLayout.addComponent(StyleUtils.getVerticalSpaceSmall());
			Link mfgLogo = new Link(null, new ExternalResource("http://innovation.mfg.de/"));
			mfgLogo.setStyleName("link-width-250");
			mfgLogo.setIcon(ResourceUtils.MFG_LOGO_RESOURCE);
			rightLayout.addComponent(mfgLogo);
			rightLayout.addComponent(StyleUtils.getVerticalSpaceSmall());
			rightLayout.addComponent(StyleUtils.getVerticalSpaceSmall());
			HorizontalLayout mfgDescriptionLayout = new HorizontalLayout();
			mfgDescriptionLayout.setWidth("100%");
			rightLayout.addComponent(mfgDescriptionLayout);
			mfgDescriptionLayout.addComponent(StyleUtils.getLabelHTML("The project is descended from a University project and is now funded by the " + StyleUtils.getHTMLLink("MFG Stiftung Baden-W&uuml;rttemberg", "http://innovation.mfg.de/") + " in form of a " + StyleUtils.getHTMLLink("Karl-Steinbuch scholarship", "http://www.karl-steinbuch-stipendium.de/") + "."));

			wrapperLayout.addComponent(rightLayout);
			wrapperLayout.setComponentAlignment(rightLayout, Alignment.TOP_CENTER);
			mainLayout.addComponent(wrapperLayout);

			componentsAdded = true;
		}
	}

	@Override
	public void update(Observable<UserSession> observable, UserSession userSession)
	{
	}
}
