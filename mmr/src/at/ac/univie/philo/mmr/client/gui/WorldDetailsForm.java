package at.ac.univie.philo.mmr.client.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import at.ac.univie.philo.mmr.shared.exceptions.IndividuumDoesNotExistExcetion;
import at.ac.univie.philo.mmr.shared.expressions.Predicate;
import at.ac.univie.philo.mmr.shared.semantic.Individual;
import at.ac.univie.philo.mmr.shared.semantic.Universe;
import at.ac.univie.philo.mmr.shared.semantic.World;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
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
	
	private PredicateExtensionsModel model;
	private MainScreen parentWidget;
	
	public WorldDetailsForm(World w, final MainScreen parentWidget) {
		this.world = w;
		this.parentWidget = parentWidget;
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
		
		labelAddExtensionElement.addClickHandler(clickHandlerForAddExtensionElement);
		labelEmptyExtension.addClickHandler(clickHandlerForEmptyExtension);
		labelExportExtension.addClickHandler(clickHandlerForExportExtension);
		labelRemoveExtensionElement.addClickHandler(clickHandlerForRemoveExtensionElement);

		enableMouseOver(labelAddExtensionElement);
		enableMouseOver(labelEmptyExtension);
		enableMouseOver(labelExportExtension);
		enableMouseOver(labelRemoveExtensionElement);
	}
	
	
	private void enableMouseOver(final Label label) {
		//mouse-over-events
		label.addMouseOverHandler(new MouseOverHandler() {

			
			public void onMouseOver(MouseOverEvent event) {
				if(isLabelActive(label)) {
					label.addStyleName(style.mouseover());
				}
			}
		});
		label.addMouseOutHandler(new MouseOutHandler() {

			
			public void onMouseOut(MouseOutEvent event) {
					label.removeStyleName(style.mouseover());					
			}
		});
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
	
	private ClickHandler clickHandlerForEmptyExtension = new ClickHandler() {
		
		
		public void onClick(ClickEvent event) {
			if (isLabelActive(labelEmptyExtension)) {
				Entry<Predicate, HashSet<ArrayList<Individual>>> selection = model.getSelectedExtension();
				 final Predicate selectedPred = selection.getKey();
				if (selectedPred == null) {
					Window.alert("No Predicate selected to clear its extension.");
					return;
				}
				final DialogBox db = new DialogBox();
				db.setAnimationEnabled(true);
				db.setAutoHideEnabled(true);
				db.setText("Confirm clearing Predicate "+selectedPred.toString());
				
				VerticalPanel vp = new VerticalPanel();
				HTMLPanel explainPanel = new HTMLPanel("<div>This emtpies all "+selection.getValue().size()+" extension elements of Predicate "+selectedPred.toString()+".<br/><br/> Do you really want to empty the whole Extension of Predicate "+selectedPred.toString()+"?</div>");
				
				HorizontalPanel buttonPanel = new HorizontalPanel();
				buttonPanel.setSpacing(10);
				buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				
				Button yes = new Button("yes");
				yes.addClickHandler(new ClickHandler() {
					
					
					public void onClick(ClickEvent event) {
						HashSet<ArrayList<Individual>> emptyextension = new HashSet<ArrayList<Individual>>();
						world.overwriteExtension(selectedPred, emptyextension);
						db.hide();
						updateModel();
					}
				});
				Button no = new Button("no");
				no.addClickHandler(new ClickHandler() {
					
					
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				buttonPanel.add(yes);
				buttonPanel.add(no);
				
				vp.add(explainPanel);
				vp.add(buttonPanel);
				vp.setStyleName(style.distance());
				db.add(vp);
				db.center();
				db.show();
			}
		}
	};
	
	
	
	private ClickHandler clickHandlerForExportExtension = new ClickHandler() {
		
		
		public void onClick(ClickEvent event) {
			if (isLabelActive(labelExportExtension)) {
				Entry<Predicate, HashSet<ArrayList<Individual>>> selection = model.getSelectedExtension();
				final Predicate selectedPred = selection.getKey();
				final HashSet<ArrayList<Individual>> extensionElements = selection.getValue();
				
				if(selection != null && selectedPred != null && extensionElements != null) {
					Universe refUniverse = parentWidget.getReferenceUniverse();
					final DialogBox db = new DialogBox(true);
					db.setText("Copy Extension of "+selectedPred.toString()+" To ...");
					db.setAnimationEnabled(true);
					VerticalPanel vp = new VerticalPanel();
					HTMLPanel explaination = new HTMLPanel("<div>Copying all Extension Elements of Predicate p of World w to another Predicate p2 of the same or another world makes your life easier because you don't have to build up every extension for each world manually. This is especially useful when the extensions do not differ much from another and the number of elements is high. Select a world, a predicate and tell the system if you want to keep the old elements from the target extension. Note that only predicates of the same arity are shown as target predicates.</div>");
					HorizontalPanel worldSelectPanel = new HorizontalPanel();
					worldSelectPanel.setSpacing(10);
					Label worldSelectLabel = new Label("Select Target World:");
					final ObjectDropBox<World> worldSelector = new ObjectDropBox<World>(refUniverse.getWorlds());
					worldSelectPanel.add(worldSelectLabel);
					worldSelectPanel.add(worldSelector);
					worldSelectPanel.setCellVerticalAlignment(worldSelectLabel, HasVerticalAlignment.ALIGN_MIDDLE);
					worldSelectPanel.setCellVerticalAlignment(worldSelectPanel, HasVerticalAlignment.ALIGN_MIDDLE);
					
					HorizontalPanel predicateSelectPanel = new HorizontalPanel();
					predicateSelectPanel.setSpacing(10);
					Label predicateSelectLabel = new Label("Select Target Predicate:");
					//get all Predicates of the same arity than the selected one
					HashSet<Predicate> predsOfCertainArity = refUniverse.getPredicates(selectedPred.getArity());
					final ObjectDropBox<Predicate> predicateSelector = new ObjectDropBox<Predicate>(predsOfCertainArity);					
					predicateSelectPanel.add(predicateSelectLabel);
					predicateSelectPanel.add(predicateSelector);
					predicateSelectPanel.setCellVerticalAlignment(predicateSelectLabel, HasVerticalAlignment.ALIGN_MIDDLE);
					predicateSelectPanel.setCellVerticalAlignment(predicateSelector, HasVerticalAlignment.ALIGN_MIDDLE);
					
					final CheckBox checkbox = new CheckBox("Keep Old Extension Elements Of Target Predicate");
					checkbox.setValue(true);
					
					HorizontalPanel buttonPanel = new HorizontalPanel();
					buttonPanel.setSpacing(10);
					Button apply = new Button("Apply");
					apply.addClickHandler(new ClickHandler() {
						
						
						public void onClick(ClickEvent event) {
							World targetWorld = worldSelector.getSelectedObject();
							Predicate targetPredicate = predicateSelector.getSelectedObject();
							
							if(targetWorld.equals(world) && targetPredicate.equals(selectedPred)) {
								db.hide();
								return;
							}
							
							//keep old extension elements of target predicate
							if(checkbox.getValue()) {
								targetWorld.extendExtension(targetPredicate, extensionElements);							
							} else {
								targetWorld.overwriteExtension(targetPredicate, extensionElements);
							}
//							new HTMLPanel("<div><ul><li>Source: "+world.getName()+"/"+selectedPred.toString()+"</li><li>Target: "+targetWorld.getName()+"/"+targetPredicate.toString()+"</li><li>Number of copied elements: "+extensionElements.size()+"</li></ul></div>");					
							updateModel();
							db.hide();
						}
					});
					Button abort = new Button("Abort");
					abort.addClickHandler(new ClickHandler() {
						
						
						public void onClick(ClickEvent event) {
							db.hide();	
						}
					});
					buttonPanel.add(apply);
					buttonPanel.add(abort);
					buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					
					vp.setSpacing(10);
					vp.setStyleName(style.distance());
					vp.add(explaination);
					vp.add(worldSelectPanel);
					vp.add(predicateSelectPanel);
					vp.add(checkbox);
					vp.add(buttonPanel);

					
					db.add(vp);
					db.center();
					db.show();
					
				}
				
			}
		}
	};
	
	private ClickHandler clickHandlerForAddExtensionElement = new ClickHandler() {
		
		
		public void onClick(ClickEvent event) {
			if (isLabelActive(labelAddExtensionElement)) {
				Entry<Predicate, HashSet<ArrayList<Individual>>> selection = model.getSelectedExtension();
				final ArrayList<Individual> selectedExtensionElement = model.getSelectedExtensionElement();
				final Predicate selectedPred = selection.getKey();
				final int arity = selectedPred.getArity();
				final HashSet<ArrayList<Individual>> extensionElements = selection.getValue();
				Set<Individual> allIndividuals = parentWidget.getReferenceUniverse().getIndividuals();
				
				if (selectedPred != null) {				
					final DialogBox db = new DialogBox(true);
					db.setText("Add Extension Element to "+selectedPred.toString());
					db.setAnimationEnabled(true);
					
					VerticalPanel vp = new VerticalPanel();
					vp.setSpacing(10);
					vp.setStyleName(style.distance());
					HTMLPanel explain = new HTMLPanel("<div></div>");
					
					Label tip = new Label("Select an Individual for each parameter:");
					final Grid grid = new Grid(arity, 2);
					grid.setStyleName(style.grid());
					//for each param one row
					for (int i=0; i<arity; i++) {
						grid.setWidget(i, 0, new Label((i+1)+"."));
						grid.setWidget(i, 1, new ObjectDropBox<Individual>(allIndividuals));					
					}
					grid.setCellSpacing(10);
					HorizontalPanel buttonPanel = new HorizontalPanel();
					Button create = new Button("Create");
					create.addClickHandler(new ClickHandler() {
						
						
						public void onClick(ClickEvent event) {
							//assemble extensionElement
							ArrayList<Individual> extensionElement= new ArrayList<Individual>();
							for (int i=0; i<arity; i++) {
								ObjectDropBox<Individual> iSelector = (ObjectDropBox<Individual>)grid.getWidget(i, 1);
								Individual selectedIndividual = iSelector.getSelectedObject();
								if (selectedIndividual != null) {
									extensionElement.add(selectedIndividual);
								}
							}
							//update Extension
							world.extendExtension(extensionElement, selectedPred);
							updateModel();
							db.hide();
						}
					});
					Button abort = new Button("Abort");
					abort.addClickHandler(new ClickHandler() {
						
						
						public void onClick(ClickEvent event) {
							db.hide();
						}
					});
					buttonPanel.add(create);
					buttonPanel.add(abort);
					buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					buttonPanel.setSpacing(10);
					
					vp.add(explain);
					vp.add(tip);
					vp.add(grid);
					vp.add(buttonPanel);
					db.add(vp);
					db.center();
					db.show();
				}
			}
		}
	};
	
	private ClickHandler clickHandlerForRemoveExtensionElement = new ClickHandler() {
		
		
		public void onClick(ClickEvent event) {
			if (isLabelActive(labelRemoveExtensionElement)) {
					Entry<Predicate, HashSet<ArrayList<Individual>>> selection = model.getSelectedExtension();
					final Predicate selectedPred = selection.getKey();
					final HashSet<ArrayList<Individual>> extensionElements = selection.getValue();
					final ArrayList<Individual> selectedExtensionElement = model.getSelectedExtensionElement();
					 if (selectedExtensionElement == null) {
						Window.alert("No Element in the Extension selected");
						return;
					}
					final DialogBox db = new DialogBox();
					db.setAnimationEnabled(true);
					db.setAutoHideEnabled(true);
					db.setText("Confirm Removing Extension Element From "+selectedPred.toString());
					
					VerticalPanel vp = new VerticalPanel();
					HTMLPanel explainPanel = new HTMLPanel("<div>This removes the selected extension element from the Extension of Predicate "+selectedPred.toString()+".<br/><br/> Do you really want to remove the selected Extension?</div>");
					
					HorizontalPanel buttonPanel = new HorizontalPanel();
					buttonPanel.setSpacing(10);
					buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					
					Button yes = new Button("yes");
					yes.addClickHandler(new ClickHandler() {
						
						
						public void onClick(ClickEvent event) {
							extensionElements.remove(selectedExtensionElement);
							world.overwriteExtension(selectedPred, extensionElements);
							db.hide();
							updateModel();
						}
					});
					Button no = new Button("no");
					no.addClickHandler(new ClickHandler() {
						
						
						public void onClick(ClickEvent event) {
							db.hide();
						}
					});
					buttonPanel.add(yes);
					buttonPanel.add(no);
					
					vp.add(explainPanel);
					vp.add(buttonPanel);
					vp.setStyleName(style.distance());
					db.add(vp);
					db.center();
					db.show();
			}
		}
	};
	
	private void initCellBrowser() {
	    // Create a model for the tree.
	    model = new PredicateExtensionsModel(this);

	    
	    /*
	     * Create the tree using the model. We use World as the default
	     * value of the root node. The default value will be passed to
	     * CustomTreeModel#getNodeInfo();
	     */
	    extensionBrowser = new CellBrowser(model, this.world);
	    extensionBrowser.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
	    extensionBrowser.setAnimationEnabled(true);

	}
	
	private boolean isLabelActive(Label label) {
		return label.getStyleName().matches("(.* )?"+style.interactiveElement()+"( .*)?");
	}
	
	private void updateModel() {
		parentWidget.updateModel();
		model.updateModel();
		extensionBrowser.onResize();

	}


}
