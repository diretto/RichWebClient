package org.diretto.web.richwebclient.view.sections;

import org.diretto.api.client.session.UserSession;
import org.diretto.api.client.util.Observable;
import org.diretto.web.richwebclient.RichWebClientApplication;
import org.diretto.web.richwebclient.view.base.AbstractSection;
import org.diretto.web.richwebclient.view.util.StyleUtils;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * This class represents a {@code ContactSection}.
 * 
 * @author Tobias Schlecht
 */
public final class ContactSection extends AbstractSection
{
	private static final long serialVersionUID = 6458619010393679945L;

	private boolean componentsAdded = false;

	/**
	 * Constructs a {@link ContactSection}.
	 * 
	 * @param application The corresponding {@code RichWebClientApplication}
	 */
	public ContactSection(RichWebClientApplication application)
	{
		super(application.getAuthenticationRegistry(), false, false, "Contact", "How you can reach us");
	}

	@Override
	public synchronized void addComponents()
	{
		if(!componentsAdded)
		{
			GridLayout mainLayout = new GridLayout(2, 1);
			addComponent(mainLayout);
			setExpandRatio(mainLayout, 1.0f);

			VerticalLayout contactDetailsLayout = new VerticalLayout();
			contactDetailsLayout.setStyleName(Reindeer.LAYOUT_BLACK);
			contactDetailsLayout.setWidth("280px");
			contactDetailsLayout.setMargin(true);
			contactDetailsLayout.setSpacing(true);

			contactDetailsLayout.addComponent(StyleUtils.getLabelH2("Contact Details"));
			contactDetailsLayout.addComponent(StyleUtils.getHorizontalLine());
			contactDetailsLayout.addComponent(StyleUtils.getLabelBold("Project Team"));
			contactDetailsLayout.addComponent(StyleUtils.getLabel("Benjamin Erb & Tobias Schlecht"));
			contactDetailsLayout.addComponent(StyleUtils.getHorizontalLine());
			contactDetailsLayout.addComponent(StyleUtils.getLabelBold("Address"));
			contactDetailsLayout.addComponent(StyleUtils.getLabel("Institute of Media Informatics, Ulm University"));
			contactDetailsLayout.addComponent(StyleUtils.getLabel("James-Franck-Ring, 89073 Ulm, Germany"));
			contactDetailsLayout.addComponent(StyleUtils.getHorizontalLine());
			contactDetailsLayout.addComponent(StyleUtils.getLabelBold("Project Website"));
			contactDetailsLayout.addComponent(new Link("www.diretto.org", new ExternalResource("http://www.diretto.org/")));
			contactDetailsLayout.addComponent(StyleUtils.getVerticalSpaceSmall());
			contactDetailsLayout.addComponent(StyleUtils.getLabelBold("GitHub Profile"));
			contactDetailsLayout.addComponent(new Link("github.com/diretto", new ExternalResource("https://github.com/diretto")));
			contactDetailsLayout.addComponent(StyleUtils.getVerticalSpaceSmall());
			contactDetailsLayout.addComponent(StyleUtils.getLabelBold("Twitter Account"));
			contactDetailsLayout.addComponent(new Link("twitter.com/diretto_project", new ExternalResource("http://twitter.com/diretto_project")));
			contactDetailsLayout.addComponent(StyleUtils.getHorizontalLine());
			contactDetailsLayout.addComponent(StyleUtils.getLabelBold("Email Address"));
			contactDetailsLayout.addComponent(new Link("diretto@googlegroups.com", new ExternalResource("mailto:diretto@googlegroups.com")));

			mainLayout.addComponent(contactDetailsLayout);

			VerticalLayout wrapperLayout = new VerticalLayout();
			wrapperLayout.setWidth("600px");
			wrapperLayout.setMargin(false, true, false, true);

			VerticalLayout termsOfUseLayout = new VerticalLayout();
			termsOfUseLayout.setWidth("520px");
			termsOfUseLayout.setMargin(true);
			termsOfUseLayout.setSpacing(true);

			termsOfUseLayout.addComponent(StyleUtils.getLabelH2("Terms of Use"));
			termsOfUseLayout.addComponent(StyleUtils.getHorizontalLine());
			termsOfUseLayout.addComponent(StyleUtils.getLabelBold("Terms"));
			termsOfUseLayout.addComponent(StyleUtils.getLabelHTML("By accessing this website, you are agreeing to be bound by these website <i>Terms of Use</i>, all applicable laws and regulations, and agree that you are responsible for compliance with any applicable local laws. If you do not agree with any of these terms, you are prohibited from using or accessing this site. The materials contained in this web site are protected by applicable copyright and trade mark law."));
			termsOfUseLayout.addComponent(StyleUtils.getHorizontalLine());
			termsOfUseLayout.addComponent(StyleUtils.getLabelBold("Use License"));
			termsOfUseLayout.addComponent(StyleUtils.getLabelHTML("Permission is granted to temporarily download one copy of the materials (information or software) on this website for personal, non-commercial transitory viewing only. This is the grant of a license, not a transfer of title, and under this license you may not:<br /><br />" + "&bull;&nbsp;&nbsp;modify or copy the materials<br />" + "&bull;&nbsp;&nbsp;use the materials for any commercial purpose, or for any public display<br />" + "&bull;&nbsp;&nbsp;attempt to decompile or reverse engineer any software contained on this website<br />" + "&bull;&nbsp;&nbsp;remove any copyright or other proprietary notations from the materials<br />" + "&bull;&nbsp;&nbsp;transfer the materials to another person or any other server<br />" + "<br />This license shall automatically terminate if you violate any of these restrictions and may be terminated by the <i>diretto</i> Project Team at any time. Upon terminating your viewing of these materials or upon the termination of this license, you must destroy any downloaded materials in your possession whether in electronic or printed format."));
			termsOfUseLayout.addComponent(StyleUtils.getHorizontalLine());
			termsOfUseLayout.addComponent(StyleUtils.getLabelBold("Disclaimer"));
			termsOfUseLayout.addComponent(StyleUtils.getLabelHTML("The materials on this website are provided <i>as is</i>. The <i>diretto</i> Project Team makes no warranties, expressed or implied, and hereby disclaims and negates all other warranties, including without limitation, implied warranties or conditions of merchantability, fitness for a particular purpose, or non-infringement of intellectual property or other violation of rights. Further, the <i>diretto</i> Project Team does not warrant or make any representations concerning the accuracy, likely results, or reliability of the use of the materials on its Internet website or otherwise relating to such materials or on any sites linked to this site."));
			termsOfUseLayout.addComponent(StyleUtils.getHorizontalLine());
			termsOfUseLayout.addComponent(StyleUtils.getLabelBold("Limitations"));
			termsOfUseLayout.addComponent(StyleUtils.getLabelHTML("In no event shall the <i>diretto</i> Project Team be liable for any damages (including, without limitation, damages for loss of data or profit, or due to business interruption) arising out of the use or inability to use the materials on this website, even if the <i>diretto</i> Project Team has been notified orally or in writing of the possibility of such damage. Because some jurisdictions do not allow limitations on implied warranties, or limitations of liability for consequential or incidental damages, these limitations may not apply to you. "));
			termsOfUseLayout.addComponent(StyleUtils.getHorizontalLine());
			termsOfUseLayout.addComponent(StyleUtils.getLabelBold("Revisions and Errata"));
			termsOfUseLayout.addComponent(StyleUtils.getLabelHTML("The materials appearing on this website could include technical, typographical, or photographic errors. The <i>diretto</i> Project Team does not warrant that any of the materials on its website are accurate, complete, or current. The <i>diretto</i> Project Team may make changes to the materials contained on its website at any time without notice. The <i>diretto</i> Project Team does not, however, make any commitment to update the materials. "));
			termsOfUseLayout.addComponent(StyleUtils.getHorizontalLine());
			termsOfUseLayout.addComponent(StyleUtils.getLabelBold("Links"));
			termsOfUseLayout.addComponent(StyleUtils.getLabelHTML("The <i>diretto</i> Project Team has not reviewed all of the sites linked to its Internet website and is not responsible for the contents of any such linked site. The inclusion of any link does not imply endorsement by the <i>diretto</i> Project Team of the site. Use of any such linked website is at the user's own risk. "));
			termsOfUseLayout.addComponent(StyleUtils.getHorizontalLine());
			termsOfUseLayout.addComponent(StyleUtils.getLabelBold("Site Terms of Use Modifications"));
			termsOfUseLayout.addComponent(StyleUtils.getLabelHTML("The <i>diretto</i> Project Team may revise these terms of use for its website at any time without notice. By using this website you are agreeing to be bound by the then current version of these <i>Terms of Use</i>."));
			termsOfUseLayout.addComponent(StyleUtils.getHorizontalLine());
			termsOfUseLayout.addComponent(StyleUtils.getLabelBold("Governing Law"));
			termsOfUseLayout.addComponent(StyleUtils.getLabelHTML("Any claim relating to this website shall be governed by the laws of <i>Germany</i> without regard to its conflict of law provisions. General terms and conditions are applicable to the use of this website."));

			wrapperLayout.addComponent(termsOfUseLayout);
			wrapperLayout.setComponentAlignment(termsOfUseLayout, Alignment.TOP_CENTER);
			mainLayout.addComponent(wrapperLayout);

			componentsAdded = true;
		}
	}

	@Override
	public void update(Observable<UserSession> observable, UserSession userSession)
	{
	}
}
