package org.diretto.web.richwebclient.view.windows;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.diretto.api.client.main.core.CoreService;
import org.diretto.api.client.user.User;
import org.diretto.web.richwebclient.RichWebClientApplication;
import org.diretto.web.richwebclient.management.AuthenticationRegistry;
import org.diretto.web.richwebclient.view.base.Section;
import org.diretto.web.richwebclient.view.sections.ContactSection;
import org.diretto.web.richwebclient.view.sections.ExploreSection;
import org.diretto.web.richwebclient.view.sections.HomeSection;
import org.diretto.web.richwebclient.view.sections.ProfileSection;
import org.diretto.web.richwebclient.view.sections.UploadSection;
import org.diretto.web.richwebclient.view.util.ResourceUtils;
import org.diretto.web.richwebclient.view.util.StyleUtils;
import org.diretto.web.richwebclient.view.windows.event.MainWindowListener;
import org.diretto.web.richwebclient.view.windows.event.SectionChangeListener;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.themes.Reindeer;

/**
 * A {@code MainWindow} is responsible for offering and managing the main
 * graphical user interface of the {@link RichWebClientApplication}.
 * 
 * @author Tobias Schlecht
 */
public final class MainWindow extends Window
{
	private static final long serialVersionUID = -1253814220800135480L;

	private final List<SectionChangeListener> sectionChangeListeners = new Vector<SectionChangeListener>();

	private final CoreService coreService;
	private final AuthenticationRegistry authenticationRegistry;

	private final String regularWidth = "960px";

	private final ArrayList<Section> sections = new ArrayList<Section>();

	private Label titleLabel = StyleUtils.getLabelH1("");
	private Label subtitleLabel = StyleUtils.getLabelHTML("");

	private HorizontalLayout headerLayout;
	private CssLayout tabSheetLayout;
	private HorizontalLayout authenticationLayout;
	private Button createAccountButton;
	private Button loginButton;
	private Button logoutButton;
	private TabSheet tabSheet;

	/**
	 * Constructs a {@link MainWindow}.
	 * 
	 * @param application The corresponding {@code RichWebClientApplication}
	 */
	public MainWindow(RichWebClientApplication application)
	{
		super("diretto");

		coreService = application.getCoreService();
		authenticationRegistry = application.getAuthenticationRegistry();

		sections.add(new HomeSection(application));
		// sections.add(new DashboardSection(application));
		sections.add(new ExploreSection(application));
		// sections.add(new TaskSection(application));
		sections.add(new UploadSection(application));
		// sections.add(new MessageSection(application));
		sections.add(new ProfileSection(application));
		sections.add(new ContactSection(application));

		VerticalLayout mainLayout = new VerticalLayout();

		mainLayout.addStyleName(Reindeer.LAYOUT_BLACK);
		mainLayout.setSizeFull();
		mainLayout.setMargin(false);

		setContent(mainLayout);

		mainLayout.addComponent(buildTopMenuBar());
		mainLayout.addComponent(buildHeader());
		mainLayout.setComponentAlignment(headerLayout, Alignment.TOP_CENTER);

		tabSheetLayout = new CssLayout();

		tabSheetLayout.setMargin(false, true, true, true);
		tabSheetLayout.setSizeFull();
		tabSheetLayout.addComponent(buildTabSheet());

		mainLayout.addComponent(tabSheetLayout);
		mainLayout.setComponentAlignment(tabSheetLayout, Alignment.TOP_CENTER);
		mainLayout.setExpandRatio(tabSheetLayout, 1);

		mainLayout.addComponent(buildBottomMenuBar());

		createLoggedOutLayout();
	}

	/**
	 * Builds and returns the {@link MenuBar} from the top of this
	 * {@link MainWindow}.
	 * 
	 * @return The built top {@code MenuBar}
	 */
	private MenuBar buildTopMenuBar()
	{
		MenuBar menuBar = new MenuBar();

		menuBar.setWidth("100%");

		return menuBar;
	}

	/**
	 * Builds and returns the header of this {@link MainWindow}.
	 * 
	 * @return The built header
	 */
	private HorizontalLayout buildHeader()
	{
		headerLayout = new HorizontalLayout();

		headerLayout.setWidth("100%");
		headerLayout.setMargin(true);
		headerLayout.setSpacing(true);

		Embedded logo = new Embedded(null, ResourceUtils.DIRETTO_LOGO_RESOURCE);

		logo.setType(Embedded.TYPE_IMAGE);
		logo.setWidth("250px");

		headerLayout.addComponent(logo);

		HorizontalLayout titleLayout = new HorizontalLayout();

		titleLayout.setSpacing(true);

		titleLayout.addComponent(titleLabel);
		titleLayout.setComponentAlignment(titleLabel, Alignment.MIDDLE_LEFT);

		Label bullLabel = StyleUtils.getLabelHTML("&nbsp;&nbsp;&nbsp;&bull;&nbsp;&nbsp;&nbsp;");
		titleLayout.addComponent(bullLabel);
		titleLayout.setComponentAlignment(bullLabel, Alignment.MIDDLE_LEFT);

		titleLayout.addComponent(subtitleLabel);
		titleLayout.setExpandRatio(subtitleLabel, 1);
		titleLayout.setComponentAlignment(subtitleLabel, Alignment.MIDDLE_LEFT);

		headerLayout.addComponent(titleLayout);
		headerLayout.setExpandRatio(titleLayout, 1);
		headerLayout.setComponentAlignment(titleLayout, Alignment.MIDDLE_CENTER);

		authenticationLayout = new HorizontalLayout();
		authenticationLayout.setSpacing(true);

		loginButton = new Button("Login", new Button.ClickListener()
		{
			private static final long serialVersionUID = 7913549447343403296L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				LoginWindow loginWindow = new LoginWindow((MainWindow) loginButton.getWindow(), authenticationRegistry);

				addWindow(loginWindow);
			}
		});

		loginButton.setStyleName(Reindeer.BUTTON_DEFAULT);

		logoutButton = new Button("Logout", new Button.ClickListener()
		{
			private static final long serialVersionUID = 2826676025564263888L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				LogoutWindow logoutWindow = new LogoutWindow((MainWindow) logoutButton.getWindow(), authenticationRegistry);

				addWindow(logoutWindow);
			}
		});

		logoutButton.setStyleName(Reindeer.BUTTON_DEFAULT);

		headerLayout.addComponent(authenticationLayout);
		headerLayout.setComponentAlignment(authenticationLayout, Alignment.BOTTOM_RIGHT);

		return headerLayout;
	}

	/**
	 * Builds and returns the {@link TabSheet} of this {@link MainWindow}.
	 * 
	 * @return The built {@code TabSheet}
	 */
	private TabSheet buildTabSheet()
	{
		tabSheet = new TabSheet();

		tabSheet.setSizeFull();

		tabSheet.addListener(new SelectedTabChangeListener()
		{
			private static final long serialVersionUID = 9041265975851915708L;

			@Override
			public void selectedTabChange(SelectedTabChangeEvent event)
			{
				Section selectedSection = (Section) ((TabSheet) event.getComponent()).getSelectedTab();

				titleLabel.setCaption(selectedSection.getTitle());
				subtitleLabel.setCaption(selectedSection.getSubtitle());

				if(selectedSection.isFullWidthRequired())
				{
					headerLayout.setWidth("100%");
					tabSheetLayout.setWidth("100%");
				}
				else
				{
					headerLayout.setWidth(regularWidth);
					tabSheetLayout.setWidth(regularWidth);
				}

				for(SectionChangeListener sectionChangeListener : sectionChangeListeners)
				{
					sectionChangeListener.onSectionChanged(selectedSection);
				}

				selectedSection.addComponents();
			}
		});

		return tabSheet;
	}

	/**
	 * Builds the content of the {@link TabSheet} of this {@link MainWindow}.
	 * 
	 * @param loggedIn {@code true} if a {@code User} is logged in; otherwise
	 *        {@code false}
	 */
	private void buildTabSheetContent(boolean loggedIn)
	{
		Section selectedSection = (Section) tabSheet.getSelectedTab();

		tabSheet.removeAllComponents();

		for(Section section : sections)
		{
			if(loggedIn || !section.isLoginNecessary())
			{
				tabSheet.addComponent(section);
			}
		}

		if(selectedSection != null && (loggedIn || !selectedSection.isLoginNecessary()))
		{
			tabSheet.setSelectedTab(selectedSection);

			titleLabel.setCaption(selectedSection.getTitle());
			subtitleLabel.setCaption(selectedSection.getSubtitle());

			if(selectedSection.isFullWidthRequired())
			{
				headerLayout.setWidth("100%");
				tabSheetLayout.setWidth("100%");
			}
			else
			{
				headerLayout.setWidth(regularWidth);
				tabSheetLayout.setWidth(regularWidth);
			}

			selectedSection.addComponents();
		}
	}

	/**
	 * Builds and returns the {@link MenuBar} from the bottom of this
	 * {@link MainWindow}.
	 * 
	 * @return The built bottom {@code MenuBar}
	 */
	private MenuBar buildBottomMenuBar()
	{
		MenuBar menuBar = new MenuBar();

		menuBar.setWidth("100%");

		return menuBar;
	}

	/**
	 * Creates the {@code LoggedIn} mode layout of this {@link MainWindow}.
	 */
	void createLoggedInLayout()
	{
		if(authenticationRegistry.getActiveUserSession() != null)
		{
			authenticationLayout.removeAllComponents();

			HorizontalLayout descriptionLayout = new HorizontalLayout();
			descriptionLayout.setSpacing(false);
			descriptionLayout.setMargin(false, true, false, true);

			Label statusLabel = StyleUtils.getLabelSmallHTML("Logged in as&nbsp;&nbsp;");
			descriptionLayout.addComponent(statusLabel);
			descriptionLayout.setComponentAlignment(statusLabel, Alignment.MIDDLE_RIGHT);

			Label nameLabel = StyleUtils.getLabelItalic(authenticationRegistry.getActiveUserSession().getUser().getUserInfo().getUserName());
			descriptionLayout.addComponent(nameLabel);
			descriptionLayout.setComponentAlignment(nameLabel, Alignment.MIDDLE_RIGHT);

			authenticationLayout.addComponent(descriptionLayout);
			authenticationLayout.setComponentAlignment(descriptionLayout, Alignment.MIDDLE_RIGHT);

			authenticationLayout.addComponent(logoutButton);
			authenticationLayout.setComponentAlignment(logoutButton, Alignment.MIDDLE_RIGHT);

			buildTabSheetContent(true);
		}
	}

	/**
	 * Creates the {@code LoggedOut} mode layout of this {@link MainWindow}.
	 */
	void createLoggedOutLayout()
	{
		authenticationLayout.removeAllComponents();

		HorizontalLayout createAccountLayout = new HorizontalLayout();
		createAccountLayout.setMargin(false, true, false, true);

		createAccountButton = new Button("Create new Account", new Button.ClickListener()
		{
			private static final long serialVersionUID = 540128237275480540L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				AccountCreationWindow accountCreationWindow = new AccountCreationWindow((MainWindow) createAccountButton.getWindow(), coreService, authenticationRegistry);

				addWindow(accountCreationWindow);
			}
		});

		createAccountButton.setStyleName(Reindeer.BUTTON_LINK);

		createAccountLayout.addComponent(createAccountButton);

		authenticationLayout.addComponent(createAccountLayout);
		authenticationLayout.setComponentAlignment(createAccountLayout, Alignment.MIDDLE_RIGHT);

		authenticationLayout.addComponent(loginButton);
		authenticationLayout.setComponentAlignment(loginButton, Alignment.MIDDLE_RIGHT);

		buildTabSheetContent(false);
	}

	/**
	 * Updates the displayed name of the {@link User}.
	 */
	void updateUserName()
	{
		if(authenticationRegistry.getActiveUserSession() != null)
		{
			createLoggedInLayout();
		}
		else
		{
			createLoggedOutLayout();
		}
	}

	/**
	 * Adds the given {@link SectionChangeListener}.
	 * 
	 * @param sectionChangeListener A {@code SectionChangeListener}
	 */
	public synchronized void addSectionChangeListener(SectionChangeListener sectionChangeListener)
	{
		if(!sectionChangeListeners.contains(sectionChangeListener))
		{
			sectionChangeListeners.add(sectionChangeListener);
		}
	}

	/**
	 * Removes the given {@link MainWindowListener}.
	 * 
	 * @param mainWindowListener A {@code MainWindowListener}
	 */
	public synchronized void removeListener(MainWindowListener mainWindowListener)
	{
		if(mainWindowListener instanceof SectionChangeListener)
		{
			sectionChangeListeners.remove(mainWindowListener);
		}
	}
}
