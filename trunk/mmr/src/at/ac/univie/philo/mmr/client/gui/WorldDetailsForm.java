package at.ac.univie.philo.mmr.client.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map.Entry;

import at.ac.univie.philo.mmr.shared.expressions.Predicate;
import at.ac.univie.philo.mmr.shared.semantic.Individual;
import at.ac.univie.philo.mmr.shared.semantic.World;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
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
import com.google.gwt.user.client.ui.Label;
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
	
	public interface DetailStyle extends CssResource {
		String mouseover();

		String leftDistance();

		String distance();

		String grid();
		
		String bigger();
		
		String buttonCenter();
		
		String validInput();
		
		String invalidInput();
		
		String setCurserPointer();
		
		String selection();
		
		String interactiveElement();
		
		String interactiveElementOff();
	}
	
	@UiField
	DetailStyle style;

	
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
	
	@UiField
	Label labelEmptyExtension;

	@UiField
	Label labelExportExtension;

	@UiField
	Label labelAddExtensionElement;

	@UiField
	Label labelRemoveExtensionElement;
	
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
	    extensionBrowser.addStyleName(style.selection());
	    headingHtml.setWidth("100%");
	    headingHtml.setHeight("100%");

		worldName.setInnerText(w.getName());

	}
	
	public void activateLabel(Label label) {
		label.removeStyleName(style.interactiveElement());
		label.removeStyleName(style.interactiveElementOff());
		label.addStyleName(style.interactiveElement());
	}
	
	public void deactivateLabel(Label label) {
		label.setStyleName(style.interactiveElementOff());
	}
	
	protected void updateToolBox(
			Entry<Predicate, HashSet<ArrayList<Individual>>> lastPredicateEntry2) {
		//we selected one Predicate and the Extension-Elements are shown.
		//we can now activate all Buttons

		deactivateLabel(labelRemoveExtensionElement);
		activateLabel(labelAddExtensionElement);
		activateLabel(labelEmptyExtension);
		activateLabel(labelExportExtension);
	
	}

	protected void updateToolBox(ArrayList<Individual> lastExtensionElement2) {
		activateLabel(labelRemoveExtensionElement);
//		activateLabel(labelAddExtensionElement);
//		deactivateLabel(labelEmptyExtension);
//		deactivateLabel(labelExportExtension);

		
	}

	protected void updateToolBox(Individual lastIndividual2) {
		activateLabel(labelAddExtensionElement);
		activateLabel(labelEmptyExtension);
		activateLabel(labelExportExtension);
		activateLabel(labelRemoveExtensionElement);
	}
	
	private void initCellBrowser() {
	    // Create a model for the tree.
	    PredicateExtensionsModel model = new PredicateExtensionsModel(this);

	    
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
