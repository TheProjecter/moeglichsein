package at.ac.univie.philo.mmr.client.gui;

import at.ac.univie.philo.mmr.shared.semantic.World;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class WorldDetailsForm extends Composite {

	World world = null;
	
	private static WorldDetailsFormUiBinder uiBinder = GWT
			.create(WorldDetailsFormUiBinder.class);

	interface WorldDetailsFormUiBinder extends
			UiBinder<Widget, WorldDetailsForm> {
	}

	
	@UiField(provided = true)
	CellBrowser extensionBrowser;
	
	@UiField
	SpanElement worldName;

//	@UiField
//	ScrollPanel surroundingScrollPanel;
//	
	@UiField
	VerticalPanel browserVPanel;
	
	@UiField
	HTMLPanel headingHtml;
	
	public WorldDetailsForm(World w) {
		this.world = w;
		initCellBrowser();


		initWidget(uiBinder.createAndBindUi(this));	
//	    surroundingScrollPanel.setWidth("100%");
//	    surroundingScrollPanel.setHeight("100%");
	    browserVPanel.setWidth("100%");
	    browserVPanel.setHeight("100%");
	    extensionBrowser.setWidth("98%");
	    int ch = Window.getClientHeight();
	    int browserheight = ch - (17*ch/100);
	    extensionBrowser.setHeight(browserheight+"px");
	    extensionBrowser.onResize();
//	    surroundingScrollPanel.setAlwaysShowScrollBars(false);
	    headingHtml.setWidth("100%");
	    headingHtml.setHeight("100%");

		worldName.setInnerText(w.getName());

	}


	
	private void initCellBrowser() {
	    // Create a model for the tree.
	    PredicateExtensionsModel model = new PredicateExtensionsModel();

	    
	    /*
	     * Create the tree using the model. We use World as the default
	     * value of the root node. The default value will be passed to
	     * CustomTreeModel#getNodeInfo();
	     */
	    extensionBrowser = new CellBrowser(model, this.world);
	    extensionBrowser.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
	    extensionBrowser.setAnimationEnabled(true);

	}



	


}
