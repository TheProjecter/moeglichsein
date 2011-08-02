package at.ac.univie.philo.mmr.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.bcel.classfile.Field;

import at.ac.univie.philo.mmr.shared.examples.UniverseFactory;
import at.ac.univie.philo.mmr.shared.exceptions.ConstraintViolationException;
import at.ac.univie.philo.mmr.shared.expressions.Constant;
import at.ac.univie.philo.mmr.shared.expressions.ConstantExpression;
import at.ac.univie.philo.mmr.shared.expressions.Predicate;
import at.ac.univie.philo.mmr.shared.semantic.AccessabilityConstraint;
import at.ac.univie.philo.mmr.shared.semantic.AccessabilityRelation;
import at.ac.univie.philo.mmr.shared.semantic.Individual;
import at.ac.univie.philo.mmr.shared.semantic.Universe;
import at.ac.univie.philo.mmr.shared.semantic.World;

import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class UniverseDetailsForm extends Composite {

	
	private boolean isFirstNewIndividual = true;
	
	private static UniverseDetailsFormUiBinder uiBinder = GWT
			.create(UniverseDetailsFormUiBinder.class);

	interface UniverseDetailsFormUiBinder extends
			UiBinder<Widget, UniverseDetailsForm> {
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

		String errorText();
	}

	@UiField
	DetailStyle style;

	@UiField(provided = true)
	CellTable<World> accessCellTable;

	@UiField(provided = true)
	SimplePager accessPager;

	@UiField
	SpanElement universeName;

	@UiField
	Label labelChangeAccessabilityConstraint;

	@UiField
	Label labelRenameUniverse;
	@UiField
	Label labelAddIndividual;
	
	@UiField
	Label labelAddConstant;
	
	@UiField
	Label labelAddWorld;

	@UiField(provided = true)
	CellTable<Entry<Constant, Individual>> globalIndividualsCellTable;

	@UiField(provided = true)
	SimplePager globalIndividualsPager;

	@UiField
	VerticalPanel verticalPanel;
	
	@UiField
	HorizontalPanel worldToolBox;
	
	@UiField
	HorizontalPanel indiToolBox;
	
	@UiField
	HTMLPanel individualTableExplainationPanel;

	@UiField(provided=true)
	CellTable<Predicate> predicatesCellTable;

	@UiField(provided=true)
	SimplePager predicatesPager;

	@UiField
	HorizontalPanel predToolBox;

	@UiField
	Label labelAddPredicate;


	// @UiField
	// HTMLPanel htmlPanelAccess;
	//
	// @UiField
	// HTMLPanel htmlPanelIndividuals;

	private Universe universe;
	private AccessabilityConstraint accessabilityConstraint;
	private HashMap<Constant, Individual> constants;
	private MainScreen parentWidget;
	private UniverseTreeModel universeTree;
	private ListDataProvider<Entry<Constant, Individual>> dataProviderIndividual;
	private ListDataProvider<Predicate> dataProviderPredicates;
	
	public UniverseDetailsForm(Universe u, final MainScreen parentWidget) {
		if (u != null && parentWidget != null) {
			this.universe = u;
			accessabilityConstraint = universe.getRelations().getConstraint();
			this.parentWidget = parentWidget;
		} else {
			throw new IllegalArgumentException("Universe is null.");
		}
		constants = universe.getConstantMap();
		universeTree = parentWidget.getUniverseTree();
		dataProviderIndividual = new ListDataProvider<Entry<Constant, Individual>>();
		dataProviderPredicates = new ListDataProvider<Predicate>();
		
		createWorldAccessTable();
		createGlobalIndividualsTable();
		createPredicatesTable();
		initWidget(uiBinder.createAndBindUi(this));
		createRenameUniverseInteraction();
		createAddPredicateInteraction();
		createAddWorldInteraction();
		createAddIndividualInteraction();
		createAddConstantInteraction();

		labelChangeAccessabilityConstraint.setText("Change Accessibility Constraint "+accessabilityConstraint
				.toString());
		labelChangeAccessabilityConstraint.setTitle("The Accessability Constraint defines minimum criteria on connections between Worlds. REFLEXIVE means that each World should be able to access itself.");
		universeName.setInnerText(u.getName());
		createChangeAccessabilityInteraction();
		accessCellTable.setWidth("95%");
		accessCellTable.setHeight("100%");
		// accessPager.setWidth("100%");
		// accessPager.setHeight("100%");
		globalIndividualsCellTable.setWidth("95%");
		globalIndividualsCellTable.setHeight("100%");
		// globalIndividualsPager.setWidth("100%");
		// globalIndividualsPager.setHeight("100%");
		verticalPanel.setWidth("95%");
		verticalPanel.setHeight("100%");
		// htmlPanelAccess.setWidth("100%");
		// htmlPanelAccess.setHeight("100%");
		// htmlPanelIndividuals.setWidth("100%");
		// htmlPanelIndividuals.setHeight("100%");
		worldToolBox.setWidth("95%");
		worldToolBox.setHeight("100%");
		indiToolBox.setWidth("100%");
		indiToolBox.setHeight("100%");
		individualTableExplainationPanel.setWidth("95%");
		
		predicatesCellTable.setWidth("95%");
		predicatesCellTable.setHeight("100%");
		predToolBox.setWidth("95%");
		predToolBox.setHeight("100%");

	}

	private void createAddPredicateInteraction() {
		labelAddPredicate.addClickHandler(new ClickHandler() {
			
			
			public void onClick(ClickEvent event) {
				
				
				final DialogBox dialogBox = new DialogBox();
				dialogBox.setText("Create New Predicate");
				HTMLPanel explainationText = new HTMLPanel("<div>Creating a new predicate enriches the structure of each world because it allows you to make individuals more distinct from others and make them to what makes up an individual: a set of properties. Some might be common to, some might be distrinct from other individuals. Moreover, you can connect individuals together with predicates which have an arity greater than 1.<br/><br/> After introducing a new predicate you can use it in modal logic expressions.<br/><br/></div>");
				final Label predLabel = new Label("Name: ");
				final TextBox predicateName= new TextBox();
				predicateName.setText("PRED");
				predicateName.setWidth("100%");
				predicateName.setFocus(true);
				predicateName.selectAll();
				predicateName.addStyleName(style.leftDistance());
				final HorizontalPanel predicateNameContainer = new HorizontalPanel();
				predicateNameContainer.setSpacing(10);
				
				final HorizontalPanel arityPanel = new HorizontalPanel();
				final Label arityText = new Label("Arity: ");
				final ObjectDropBox<Integer> aritySelector = new ObjectDropBox<Integer>();
				aritySelector.addItem(1);
				aritySelector.addItem(2);
				aritySelector.addItem(3);
				aritySelector.addItem(4);
				aritySelector.addItem(5);
				aritySelector.addStyleName(style.leftDistance());
				arityPanel.addStyleName(style.distance());
				arityPanel.add(arityText);
				arityPanel.add(aritySelector);
				
				predicateNameContainer.add(predLabel);
				predicateNameContainer.add(predicateName);
				predicateName.setMaxLength(20);
				final Label alreadyTakenLabel = new Label("Predicate Name already used or invalid.");
				alreadyTakenLabel.addStyleName(style.leftDistance());

				predicateName.addKeyUpHandler(new KeyUpHandler() {
					
					
					public void onKeyUp(KeyUpEvent event) {

						String currentText = predicateName.getText();
						int cursorPos = predicateName.getCursorPos();
						predicateName.setText(currentText.toUpperCase());
						predicateName.setCursorPos(cursorPos);
						if(!universe.isFreePredicateName(currentText) || !UniverseFactory.validPredicateName(currentText)) {
							markInputAsInvalid(predicateName);
							predicateNameContainer.add(alreadyTakenLabel);
							predicateNameContainer.setCellVerticalAlignment(alreadyTakenLabel, HasVerticalAlignment.ALIGN_MIDDLE);
							
						} else {
							predicateNameContainer.remove(alreadyTakenLabel);
							markInputAsValid(predicateName);
						}
						
					}
					
				});

				predicateNameContainer.setCellVerticalAlignment(predicateName, HasVerticalAlignment.ALIGN_MIDDLE);
				Button apply = new Button("Create");
				apply.addClickHandler(new ClickHandler() {
					
					
					public void onClick(ClickEvent event) {
							String predName = predicateName.getText();
							Integer arity = aritySelector.getSelectedObject();
							if (arity == null) {
								arity = 1;
							}
						if(!universe.isFreeIndividualName(predName) || !UniverseFactory.validPredicateName(predName)) {
							Window.alert("I'm afraid but Predicate name "+predName+" is invalid or already used.");
							return;
						}
						universe.addPredicate(new Predicate(predName,0,arity));
						updateModel();
						predicatesCellTable.redraw();
						dialogBox.hide();
					}


				});
				Button abort = new Button("Abort");
				abort.addClickHandler(new ClickHandler() {
					
					
					public void onClick(ClickEvent event) {
						dialogBox.hide();
					}
				});
				
				VerticalPanel framePanel = new VerticalPanel();
				framePanel.setSpacing(10);
				framePanel.addStyleName(style.distance());
				HorizontalPanel buttonPanel = new HorizontalPanel();
				buttonPanel.addStyleName(style.distance());
				buttonPanel.add(apply);
				buttonPanel.add(abort);
				framePanel.add(explainationText);
				framePanel.add(predicateNameContainer);
				framePanel.add(arityPanel);
				framePanel.add(buttonPanel);
				dialogBox.setWidget(framePanel);
				dialogBox.setAnimationEnabled(true);
				dialogBox.center();				
				
			}
		});
		
		//mouse-over-events
		labelAddPredicate.addMouseOverHandler(new MouseOverHandler() {

			
			public void onMouseOver(MouseOverEvent event) {
				labelAddPredicate.addStyleName(style.mouseover());
			}
		});
		labelAddPredicate.addMouseOutHandler(new MouseOutHandler() {

			
			public void onMouseOut(MouseOutEvent event) {
				labelAddPredicate.removeStyleName(style.mouseover());
			}
		});
	}

	private void createAddIndividualInteraction() {
		labelAddIndividual.addClickHandler(new ClickHandler() {
			
			
			public void onClick(ClickEvent event) {

		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Create New Individual");
		HTMLPanel explainationText = new HTMLPanel("<div>To create a new individual affects the semantical level because after creating it there is one more thing in the universe to speak and reason about.<br/><br/> If you add an Individual, you can later add it to any extension of any world.<br/><br/><i>(Note that this a generous introduction of individuals. You will find other approaches in modal logic. One alternative assigns one individual to exactly one world. In this approach the concept of a possible world implies that each world has his own set of unique individuals. Between worlds there might be similar individuals (i.e. with similar properties such that modal operators still make sense) but you have to model these similarities with an additional relation. Thus, we rejected to offer this possibility in this tool, but you should be aware that this is a decision on the semantical level.)</i></div>");
		final TextBox individualName= new TextBox();
		individualName.setText("Ordinary Name Of Individual");
		individualName.setWidth("100%");
		individualName.setFocus(true);
		individualName.selectAll();
		individualName.addStyleName(style.leftDistance());
		final HorizontalPanel indiOrdinaryNameContainer = new HorizontalPanel();
		indiOrdinaryNameContainer.addStyleName(style.distance());
		Button indiNameHelpButton = new Button("?");
		indiNameHelpButton.addStyleName(style.leftDistance());
		indiNameHelpButton.setTitle("What is the ordinary name of an Individual?");
		indiNameHelpButton.addClickHandler(new ClickHandler() {
			
			
			public void onClick(ClickEvent e) {
				final DialogBox dialogBox = new DialogBox();
				dialogBox.setText("What is the ordinary name of an Individual?");
				VerticalPanel vp = new VerticalPanel();
				vp.addStyleName(style.distance());
				HTMLPanel hpanel = new HTMLPanel("<div>The ordinary name is not a name in our artificial language. It is just a name we usually refer to when we speak about an individual. For humans it usually is easier to associate something with a name like 'Mr. Spok' than the constant name in the artificial language 'a_1'. Thus, this makes life easier when constructing extensions of predicates or when reading comments about the evaluation process.</div>");
				Button ok = new Button("OK");
				ok.addStyleName(style.distance());
				ok.addClickHandler(new ClickHandler() {
					
					
					public void onClick(ClickEvent event) {
						dialogBox.hide();
					}
				});
				vp.add(hpanel);
				vp.add(ok);	
				dialogBox.add(vp);
				dialogBox.show();
				individualName.setVisible(true);
				individualName.selectAll();
				dialogBox.setAnimationEnabled(true);
				dialogBox.center();
			}
		});
		indiOrdinaryNameContainer.add(individualName);
		indiOrdinaryNameContainer.add(indiNameHelpButton);
		final Label alreadyTakenLabel = new Label("Ordinary Name already used by another Individual.");
		alreadyTakenLabel.addStyleName(style.leftDistance());

		individualName.addKeyUpHandler(new KeyUpHandler() {
			
			
			public void onKeyUp(KeyUpEvent event) {

				String currentText = individualName.getText();
				if(!universe.isFreeIndividualName(currentText)) {
					markInputAsInvalid(individualName);
					indiOrdinaryNameContainer.add(alreadyTakenLabel);
					indiOrdinaryNameContainer.setCellVerticalAlignment(alreadyTakenLabel, HasVerticalAlignment.ALIGN_MIDDLE);
					
				} else {
					indiOrdinaryNameContainer.remove(alreadyTakenLabel);
					markInputAsValid(individualName);
				}
				
			}
			
		});

		indiOrdinaryNameContainer.setCellVerticalAlignment(individualName, HasVerticalAlignment.ALIGN_MIDDLE);
		indiOrdinaryNameContainer.setCellVerticalAlignment(indiNameHelpButton, HasVerticalAlignment.ALIGN_MIDDLE);
		Button apply = new Button("Create");
		apply.addClickHandler(new ClickHandler() {
			
			
			public void onClick(ClickEvent event) {
					String indiName = individualName.getText();
				if(!universe.isFreeIndividualName(indiName)) {
					Window.alert("Ordinary name is already taken by another Individual. This is not a problem for the artificial language but in order to not confuse yourself please use another name.");
					return;
				}
				universe.addIndividual(new Individual(indiName,null));
				updateModel();
				globalIndividualsCellTable.redraw();
				dialogBox.hide();
				if (isFirstNewIndividual) {
					isFirstNewIndividual = false;
					final DialogBox informationBox = new DialogBox();
					informationBox.setText(indiName+" successfully created");
					HTMLPanel explaination = new HTMLPanel("You successfully created "+indiName+".<br/><br/>A few advises: <ul><li>Currently this individual has no property, it just exists and from the point of view of the artificial language it is invisible.</li><li>If you want to directly refer to it, create a constant.</li><li>Usally Individuals have some properties (i.e. are parts of predicate extensions). You may want to add the individual to an extension in at least one world or create a new predicate for it.</li></ul>");
					Button ok = new Button("OK");
					ok.addClickHandler(new ClickHandler() {
						
						
						public void onClick(ClickEvent event) {
							informationBox.hide();
						}
					});
					
					VerticalPanel frame = new VerticalPanel();
					frame.add(explaination);
					frame.add(ok);
					informationBox.add(frame);
					informationBox.setAnimationEnabled(true);
					informationBox.show();
					informationBox.center();
				}
			}


		});
		Button abort = new Button("Abort");
		abort.addClickHandler(new ClickHandler() {
			
			
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		
		VerticalPanel framePanel = new VerticalPanel();
		framePanel.setSpacing(10);
		framePanel.addStyleName(style.distance());
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.addStyleName(style.distance());
		buttonPanel.add(apply);
		buttonPanel.add(abort);
		framePanel.add(explainationText);
		framePanel.add(indiOrdinaryNameContainer);
		framePanel.add(buttonPanel);
		dialogBox.setWidget(framePanel);
		dialogBox.setAnimationEnabled(true);
		dialogBox.center();
		
			}
		});
		
		//mouse-over-events
		labelAddIndividual.addMouseOverHandler(new MouseOverHandler() {

			
			public void onMouseOver(MouseOverEvent event) {
				labelAddIndividual.addStyleName(style.mouseover());
			}
		});
		labelAddIndividual.addMouseOutHandler(new MouseOutHandler() {

			
			public void onMouseOut(MouseOutEvent event) {
				labelAddIndividual.removeStyleName(style.mouseover());
			}
		});
	}
	
	private void createAddConstantInteraction() {
		labelAddConstant.addClickHandler(new ClickHandler() {
			
			
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Create new constant for an individual!");
		HTMLPanel explainationText = new HTMLPanel("<div>To create a new reference for an individual you first choose one Individual and then give him a unique name within the artificial language. The syntax of our language only allows constant names of the form a_<b>i</b> where <b>i</b> is an integer.</div>");
		final ObjectDropBox<Individual> individualDropBox = new ObjectDropBox<Individual>(universe.getIndividuals());
		individualDropBox.addStyleName(style.leftDistance());
		HorizontalPanel indiSelector = new HorizontalPanel();
		indiSelector.addStyleName(style.distance());
		Label selectText = new Label("Select Individual");
		indiSelector.add(selectText);
		indiSelector.add(individualDropBox);
		final Label alreadyTakenLabel = new Label("Constant already used to identify another Individual");
		alreadyTakenLabel.addStyleName(style.leftDistance());
		final HorizontalPanel newConstantEditPanel = new HorizontalPanel();
		HTML newConstantPrefix = new HTML("<div>a_</div>");
		newConstantPrefix.addStyleName(style.bigger());
		final TextBox newConstantTextBox = new TextBox();
		newConstantTextBox.setText(universe.getFreeConstantIndex()+"");
		newConstantTextBox.setMaxLength(4);
		newConstantTextBox.addKeyUpHandler(new KeyUpHandler() {
			
			
			public void onKeyUp(KeyUpEvent event) {

				String currentText = newConstantTextBox.getText();
				if (isNumber(currentText)) {
					markInputAsValid(newConstantTextBox);
				} else {
					markInputAsInvalid(newConstantTextBox);
				}
				;
				if(!isFreeIdentifier(currentText, individualDropBox.getSelectedObject())) {
					markInputAsInvalid(newConstantTextBox);
					newConstantEditPanel.add(alreadyTakenLabel);
					newConstantEditPanel.setCellVerticalAlignment(alreadyTakenLabel, HasVerticalAlignment.ALIGN_MIDDLE);
					
				} else {
					newConstantEditPanel.remove(alreadyTakenLabel);
				}
				
			}
			
		});
		newConstantEditPanel.add(newConstantPrefix);
		newConstantEditPanel.add(newConstantTextBox);
		newConstantEditPanel.addStyleName(style.distance());
		newConstantEditPanel.setCellVerticalAlignment(newConstantPrefix, HasVerticalAlignment.ALIGN_MIDDLE);
		newConstantEditPanel.setCellVerticalAlignment(newConstantTextBox, HasVerticalAlignment.ALIGN_MIDDLE);
		Button apply = new Button("Create");
		apply.addClickHandler(new ClickHandler() {
			
			
			public void onClick(ClickEvent event) {
				int constIndex = 0;
				try {
					constIndex = Integer.valueOf(newConstantTextBox.getText());
				} catch (NumberFormatException e){
					Window.alert("I'm afraid but "+newConstantTextBox.getText()+" is not an integer and therefore cannot be an index.");
					return;
				}
				
				Individual selectedIndividual = individualDropBox.getSelectedObject();
				
				if(isFreeIdentifier(constIndex+"", selectedIndividual)) {
					constants.put(new Constant("a", constIndex), selectedIndividual);
					updateModel();
					globalIndividualsCellTable.redraw();
					dialogBox.hide();
				} else {
					Window.alert("I'm afraid but constant a_"+constIndex+ "is already used. Try another one.");
				}
			}
		});
		Button abort = new Button("Abort");
		abort.addClickHandler(new ClickHandler() {
			
			
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		
		VerticalPanel framePanel = new VerticalPanel();
		framePanel.setSpacing(10);
		framePanel.addStyleName(style.distance());
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.addStyleName(style.distance());
		buttonPanel.add(apply);
		buttonPanel.add(abort);
		framePanel.add(explainationText);
		framePanel.add(indiSelector);
		framePanel.add(newConstantEditPanel);
		framePanel.add(buttonPanel);
		dialogBox.setWidget(framePanel);
		dialogBox.setAnimationEnabled(true);
		dialogBox.center();
		
			}
		});
		
		//mouse-over-events
		labelAddConstant.addMouseOverHandler(new MouseOverHandler() {

			
			public void onMouseOver(MouseOverEvent event) {
				labelAddConstant.addStyleName(style.mouseover());
			}
		});
		labelAddConstant.addMouseOutHandler(new MouseOutHandler() {

			
			public void onMouseOut(MouseOutEvent event) {
				labelAddConstant.removeStyleName(style.mouseover());
			}
		});
		
	}

	private void createAddWorldInteraction() {
		labelAddWorld.addClickHandler(new ClickHandler() {
			
			
			public void onClick(ClickEvent event) {
				final DialogBox dialogBox = new DialogBox();
				dialogBox.setText("Create World");
				dialogBox.setAnimationEnabled(true);

				Label worldNameLabel = new Label("Name of the World: ");
				worldNameLabel.addStyleName(style.distance());
				final TextBox newName = new TextBox();
				newName.setText("Enter name of the World");
				newName.selectAll();
				final Button createButton = new Button("Create");
				final Button abortButton = new Button("Abort");

				createButton.addClickHandler(new ClickHandler() {

					
					public void onClick(ClickEvent event) {
						String worldName = newName.getText();
						if (UniverseFactory.validWorldName(worldName)) {
							boolean success = universe.addNewWorld(worldName);
							if (!success) {
								Window.alert("World with name "+worldName+" already exists");
								return;
							}
							updateModel();
							dialogBox.hide();
						} else {
							Window.alert("World name invalid.");
							newName.selectAll();
						}
					}
				});
				
				abortButton.addClickHandler(new ClickHandler() {

					
					public void onClick(ClickEvent event) {
						dialogBox.hide();
					}
				});
				
				
				final HorizontalPanel worldCreateHPanel = new HorizontalPanel();
				worldCreateHPanel.add(worldNameLabel);
				worldCreateHPanel.add(newName);
				final HorizontalPanel hPanelButtons = new HorizontalPanel();
				hPanelButtons.add(createButton);
				hPanelButtons.add(abortButton);
				final VerticalPanel vPanel = new VerticalPanel();
				vPanel.add(worldCreateHPanel);
				vPanel.add(hPanelButtons);

				dialogBox.center();
				dialogBox.add(vPanel);
				dialogBox.show();
				newName.selectAll();
				newName.setFocus(true);		
			}
		});
		//mouse-over-events
		labelAddWorld.addMouseOverHandler(new MouseOverHandler() {

			
			public void onMouseOver(MouseOverEvent event) {
				labelAddWorld.addStyleName(style.mouseover());
			}
		});
		labelAddWorld.addMouseOutHandler(new MouseOutHandler() {

			
			public void onMouseOut(MouseOutEvent event) {
				labelAddWorld.removeStyleName(style.mouseover());
			}
		});
		
}

	public boolean updateAccessabilityConstraint(
			AccessabilityConstraint accessabilityConstraint, boolean force) {
		AccessabilityRelation accessRel = universe.getRelations();
		try {
			accessRel.changeConstraint(accessabilityConstraint);
		} catch (ConstraintViolationException e) {
			if (force) {
				accessRel.enforceConstraint(accessabilityConstraint);
			} else {
				return false;
			}
		}
		labelChangeAccessabilityConstraint.setText("Change Accessibility Constraint "+accessabilityConstraint
				.toString());
		this.accessabilityConstraint = accessabilityConstraint;
		return true;
	}

	private void createRenameUniverseInteraction() {
		labelRenameUniverse.addClickHandler(new ClickHandler() {

			
			public void onClick(ClickEvent event) {
				// create Popup
				final DialogBox dialogBox = new DialogBox();
				dialogBox.setText("Rename Universe");
				dialogBox.setAnimationEnabled(true);

				Label universeName = new Label("Change universe name to: ");
				universeName.addStyleName(style.distance());
				final TextBox newName = new TextBox();
				newName.setText(universe.getName());
				final Button renameButton = new Button("Rename");
				final Button abortButton = new Button("Abort");

				renameButton.addClickHandler(new ClickHandler() {

					
					public void onClick(ClickEvent event) {
						String universeName = newName.getText();
						if (UniverseFactory.validUniverseName(universeName)) {
							universe.setName(universeName);
							updateModel();
							dialogBox.hide();
						} else {
							Window.alert("Univere name invalid.");
							newName.setText(universe.getName());
							newName.selectAll();
						}
					}
				});

				abortButton.addClickHandler(new ClickHandler() {

					
					public void onClick(ClickEvent event) {
						dialogBox.hide();
					}
				});

				newName.addKeyPressHandler(new KeyPressHandler() {

					
					public void onKeyPress(KeyPressEvent event) {
						if (event.getUnicodeCharCode() == KeyCodes.KEY_ENTER) {
							renameButton.click();
						}
					}
				});

				final HorizontalPanel universeRenameHPanel = new HorizontalPanel();
				universeRenameHPanel.add(universeName);
				universeRenameHPanel.add(newName);
				final HorizontalPanel hPanelButtons = new HorizontalPanel();
				hPanelButtons.add(renameButton);
				hPanelButtons.add(abortButton);
				final VerticalPanel vPanel = new VerticalPanel();
				vPanel.add(universeRenameHPanel);
				vPanel.add(hPanelButtons);

				dialogBox.center();
				dialogBox.add(vPanel);
				dialogBox.show();
				newName.selectAll();
				newName.setFocus(true);
			}
		});

		labelRenameUniverse.addMouseOverHandler(new MouseOverHandler() {

			
			public void onMouseOver(MouseOverEvent event) {
				labelRenameUniverse.addStyleName(style.mouseover());
			}
		});
		labelRenameUniverse.addMouseOutHandler(new MouseOutHandler() {

			
			public void onMouseOut(MouseOutEvent event) {
				labelRenameUniverse.removeStyleName(style.mouseover());
			}
		});
	}

	private void createChangeAccessabilityInteraction() {
		labelChangeAccessabilityConstraint.addClickHandler(new ClickHandler() {

			
			public void onClick(ClickEvent event) {
				// Create the popup dialog box
				final DialogBox dialogBox = new DialogBox();
				dialogBox.setText("Change Accessibility Constraint");
				dialogBox.setAnimationEnabled(true);

				final Button changeButton = new Button("OK");
				changeButton.getElement().setId("changeButton");
				final Button abortButton = new Button("Abort");
				abortButton.getElement().setId("abortButton");

				final HTML explainationDiv = new HTML();
				explainationDiv
						.setHTML("<div>"
								+ "					The Accessibility Constraint defines minimum criteria on connections between Worlds."
								+ "					<ul>"
								+ "						<li><b>S5</b> means that every world has to see any other world in this universe.</li>"
								+ "						<li><b>REFLEXIVE</b> means that each World should be able to access itself.</li>"
								+ "						<li><b>NONE</b> does not demand any access to another world.</li>"
								+ "					</ul>" + "				</div>");
				explainationDiv.addStyleName("left-distance");
				final HTML beforeListBox = new HTML(
						"<div>Change Constraint: </div>");
				final ListBox constraintListBox = new ListBox();
				final HTML currentConstraintHTML = new HTML("<div>Current: <b>"
						+ accessabilityConstraint.toString() + "</b></div>");
				currentConstraintHTML.addStyleName(style.leftDistance());
				for (AccessabilityConstraint c : AccessabilityConstraint
						.values()) {
					constraintListBox.addItem(c.toString());
					if (universe.getRelations().getConstraint().equals(c)) {
						int lastItemIndex = constraintListBox.getItemCount() - 1;
						constraintListBox.setSelectedIndex(lastItemIndex);
					}
				}
				// 1 means dropdownlist
				constraintListBox.setVisibleItemCount(1);
				constraintListBox.addStyleName(style.leftDistance());

				HorizontalPanel buttonFrame = new HorizontalPanel();
				HorizontalPanel selectionFrame = new HorizontalPanel();
				selectionFrame.add(beforeListBox);
				selectionFrame.add(constraintListBox);
				selectionFrame.add(currentConstraintHTML);
				buttonFrame.add(changeButton);
				buttonFrame.add(abortButton);

				VerticalPanel dialogVPanel = new VerticalPanel();
				dialogVPanel.addStyleName("dialogVPanel");
				dialogVPanel.add(explainationDiv);
				dialogVPanel.add(selectionFrame);
				dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
				dialogVPanel.add(buttonFrame);
				dialogBox.setWidget(dialogVPanel);

				abortButton.addClickHandler(new ClickHandler() {

					
					public void onClick(ClickEvent event) {
						dialogBox.hide();

					}
				});

				changeButton.addClickHandler(new ClickHandler() {

					
					public void onClick(ClickEvent event) {
						String newAccessabilityConstraintText = constraintListBox
								.getItemText(constraintListBox
										.getSelectedIndex());
						final AccessabilityConstraint newAccessabilityConstraint = AccessabilityConstraint
								.valueOf(newAccessabilityConstraintText);
						AccessabilityRelation accessRel = universe
								.getRelations();
						if (!accessabilityConstraint.toString().equals(
								newAccessabilityConstraintText)) {

							if (!updateAccessabilityConstraint(
									newAccessabilityConstraint, false)) {
								// popup that changes are necessary
								final DialogBox dialogBoxConfirmation = new DialogBox();
								dialogBoxConfirmation
										.setText("Constraints are violated");
								VerticalPanel frame = new VerticalPanel();
								HTMLPanel html = new HTMLPanel(
										"<div>You want to make changes in the accessibility constraint of the universe "
												+ universe.getName()
												+ ". At the moment, at least one World violates the constraint. Do you want to change the accessibility list of the worlds which do not fit to the constraint? Be aware: This changes the universe =).</div>");
								html.setStyleName(style.distance());
								frame.setStyleName(style.distance());
								HorizontalPanel buttonPanel = new HorizontalPanel();
								Button yes = new Button("Yes");
								yes.addClickHandler(new ClickHandler() {

									
									public void onClick(ClickEvent event) {
										updateAccessabilityConstraint(
												newAccessabilityConstraint,
												true);
										currentConstraintHTML
												.setHTML("<div>Current: <b>"
														+ newAccessabilityConstraint
																.toString()
														+ "</b></div>");
										updateModel();
										dialogBoxConfirmation.hide();
										changeButton.setText("OK");
										abortButton.setEnabled(false);
									}
								});
								Button no = new Button("No");
								no.addClickHandler(new ClickHandler() {

									
									public void onClick(ClickEvent event) {
										dialogBoxConfirmation.hide();
									}
								});
								buttonPanel.add(yes);
								buttonPanel.add(no);
								frame.add(html);
								frame.add(buttonPanel);
								dialogBoxConfirmation.setWidget(frame);
								dialogBoxConfirmation.show();
								dialogBoxConfirmation.setModal(true);
								dialogBoxConfirmation.setAnimationEnabled(true);
								dialogBoxConfirmation.center();
								return;
							}
							changeButton.setText("OK");
							abortButton.setEnabled(false);
						} else {
							dialogBox.hide();
						}
					}

				});

				constraintListBox.addChangeHandler(new ChangeHandler() {

					
					public void onChange(ChangeEvent event) {
						changeButton.setText("Change");
					}
				});

				dialogBox.show();
				dialogBox.center();
			}
		});

		labelChangeAccessabilityConstraint
				.addMouseOverHandler(new MouseOverHandler() {

					
					public void onMouseOver(MouseOverEvent event) {
						labelChangeAccessabilityConstraint.addStyleName(style
								.mouseover());
					}
				});
		labelChangeAccessabilityConstraint
				.addMouseOutHandler(new MouseOutHandler() {

					
					public void onMouseOut(MouseOutEvent event) {
						labelChangeAccessabilityConstraint
								.removeStyleName(style.mouseover());
					}
				});

	}

	private void createPredicatesTable() {
		predicatesCellTable = new CellTable<Predicate>();
		predicatesCellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		predicatesCellTable.setTitle("Click on the content of the table cells to rename the predicate");

		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		predicatesPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		predicatesPager.setDisplay(predicatesCellTable);
		
		//Predname as EditTExtCell
		final EditTextCell editCell = new EditTextCell();
		Column<Predicate, String> predColumn = new Column<Predicate, String>(
				editCell) {

			@Override
			public String getValue(Predicate p) {
				return p.getName();
			}
		};
		predicatesCellTable.addColumn(predColumn, "Predicate Name");
		predColumn.setFieldUpdater(new FieldUpdater<Predicate, String>() {

			
			public void update(int index, final Predicate p, String value) {
				//that's not an update
				if (p.getName().equals(value)) {
					return;
				}
				
				if (UniverseFactory.validPredicateName(value) && universe.isFreePredicateName(value)) {
					p.setName(value.toUpperCase());
					editCell.clearViewData(p);
					updateModel();
				} else {
					final DialogBox db = new DialogBox();
					db.setText("Renaming Failed: Invalid or Duplicate Predicate Name");
					db.setAutoHideEnabled(true);
					VerticalPanel vp = new VerticalPanel();
					HTMLPanel explainPanel = new HTMLPanel("<div>You tried to modify a predicate name. Unfortunately, you entered an invalid name or one that was already used to refer to another predicate, even if the arities are different. The system don't want to get you confused so it disallowed such ambiguities.</div>");
					explainPanel.addStyleName(style.errorText());
					Button ok = new Button("OK");
					ok.addClickHandler(new ClickHandler() {
						
						
						public void onClick(ClickEvent event) {
							db.hide();
							editCell.clearViewData(p);
							updateModel();

						}
					});
					vp.add(explainPanel);
					vp.add(ok);
					vp.addStyleName(style.distance());
					vp.setSpacing(10);
					db.add(vp);
					db.setAnimationEnabled(true);
					db.show();
					db.center();
				}
				
			}
		}); // end of setFieldUpdater
		
		//Arity as TextCell (not modifyable)
		TextColumn<Predicate> arityColumn = new TextColumn<Predicate>() {

			@Override
			public String getValue(Predicate object) {
				return object.getArity()+"";
			}
		};
		predicatesCellTable.addColumn(arityColumn, "Arity");
		//Del-Button-Column
		Column<Predicate, String> delColumn = new Column<Predicate, String>(
				new ButtonCell()) {

					@Override
					public String getValue(Predicate object) {
						return "X";
					}
		};
		predicatesCellTable.addColumn(delColumn, "Actions");

		delColumn.setFieldUpdater(new FieldUpdater<Predicate, String>() {

			
			public void update(int index, final Predicate object, String value) {
				//remove World from Universe
				final DialogBox db = new DialogBox();
				db.setText("Confirm removal of Predicate "+object.getName());
				db.setTitle("Confirm removal of Predicate "+object.getName());
				VerticalPanel vp = new VerticalPanel();
				vp.addStyleName(style.distance());
				HTML confirmationText = new HTML("<div>Do you really want to remove Predicate <b>"+object.getName()+"</b> from Universe?</div><br/><div>This also removes all corresponding extensions in each world. This means, they are lost and evaluations which contain this predicate result in error messages. Just to inform you what you are about to do.</div>");
				confirmationText.addStyleName(style.distance());
				HorizontalPanel hp = new HorizontalPanel();
				hp.addStyleName(style.distance());
				hp.setWidth("40%");
				hp.setSpacing(10);
				Button yes = new Button("yes");
				yes.addClickHandler(new ClickHandler() {
					
					
					public void onClick(ClickEvent event) {
						universe.removePredicate(object);
						//update the model of whole application
						updateModel();
						db.hide();
					}
				});
				Button no = new Button("no");
				no.addClickHandler(new ClickHandler() {
					
					
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				hp.add(yes);
				hp.add(no);
				vp.add(confirmationText);
				vp.add(hp);
				db.add(vp);
				db.center();
				db.setAnimationEnabled(true);
				db.show();
			}
		});
		
		// Push the data into the widget.
		ArrayList<Predicate> allEntries = new ArrayList<Predicate>(
				universe.getPredicates());
		// globalIndividualsCellTable.setRowData(0, allIndis);

		dataProviderPredicates.addDataDisplay(predicatesCellTable);
		dataProviderPredicates.setList(allEntries);
		
	}
	
	private void createWorldAccessTable() {
		// Create a CellTable
		accessCellTable = new CellTable<World>();
		// accessCellTable.setWidth("100%");

		accessCellTable
				.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		accessCellTable.setTitle("Click on the content of the table cells to edit the ingredients of this Universe");
		
		// Create a Pager to control the table.
		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		accessPager = new SimplePager(TextLocation.CENTER, pagerResources,
				false, 0, true);
		accessPager.setDisplay(accessCellTable);
		// accessPager.setWidth("100%");

		// Add a text column to show the name.
		final EditTextCell editCell = new EditTextCell();
		Column<World, String> worldColumn = new Column<World, String>(
				editCell) {

			@Override
			public String getValue(World w) {
				return w.getName();
			}
		};
		accessCellTable.addColumn(worldColumn, "World");
		worldColumn.setFieldUpdater(new FieldUpdater<World, String>() {

			
			public void update(int index, final World w, String value) {
				//that's not an update
				if (w.getName().equals(value)) {
					return;
				}
				
				if (UniverseFactory.validWorldName(value) && universe.isFreeWorldName(value)) {
					w.setName(value);
					editCell.clearViewData(w);
					updateModel();
				} else {
					final DialogBox db = new DialogBox();
					db.setText("Renaming Failed: Invalid or Duplicate World Name");
					db.setAutoHideEnabled(true);
					VerticalPanel vp = new VerticalPanel();
					HTMLPanel explainPanel = new HTMLPanel("<div>You tried to use either an invalid world name or one which already identifies another world. You might not want this. Thus, to keep things in order for you, rename is reversed and don't affect the universe.</div>");
					explainPanel.addStyleName(style.errorText());
					Button ok = new Button("OK");
					ok.addClickHandler(new ClickHandler() {
						
						
						public void onClick(ClickEvent event) {
							db.hide();
							editCell.clearViewData(w);
							updateModel();

						}
					});
					vp.add(explainPanel);
					vp.add(ok);
					vp.addStyleName(style.distance());
					vp.setSpacing(10);
					db.add(vp);
					db.setAnimationEnabled(true);
					db.show();
					db.center();
				}
				
			}
		}); // end of setFieldUpdater

		// Add a text column to show the address.
		Column<World, HashSet<World>> accessColumn = new Column<World, HashSet<World>>(
				new AccessabilityEditCell()) {
			@Override
			public HashSet<World> getValue(World w) {
				return w.getUniverse().getAccessibleWorlds(w);
			}
		};
		accessCellTable.addColumn(accessColumn, "has Access to");

		accessColumn.setFieldUpdater(new FieldUpdater<World, HashSet<World>>() {

			
			public void update(int index, final World object,
					final HashSet<World> value) {
				if (value == null)
					return;
				final AccessabilityRelation axxRel = object.getUniverse()
						.getRelations();
				AccessabilityConstraint requiredConstraint = axxRel
						.overrideAccess(object, value, false);
				if (!accessabilityConstraint.equals(requiredConstraint)) {
					final DialogBox askConfirmationDialog = new DialogBox();
					askConfirmationDialog.setText("Constraints are violated");
					VerticalPanel frame = new VerticalPanel();
					HTMLPanel html = new HTMLPanel(
							"<div>You want to make changes in the accessability list of world "
									+ object.getName()
									+ ". If you apply the changes the system need to make the following changes to avoid inconsistency and constraint violation:<br/><br/><u>Should we change constraint:</u> <ul><li><b>FROM:</b> "
									+ accessabilityConstraint
									+ "</li><li><b>TO:</b> "
									+ requiredConstraint
									+ "</li></ul> If you confirm with 'yes' changes are applied.</div>");
					html.setStyleName(style.distance());
					frame.setStyleName(style.distance());
					HorizontalPanel buttonPanel = new HorizontalPanel();
					Button yes = new Button("Yes");
					yes.addClickHandler(new ClickHandler() {

						
						public void onClick(ClickEvent event) {
							accessabilityConstraint = axxRel.overrideAccess(
									object, value, true);
							updateModel();
							askConfirmationDialog.hide();
						}
					});
					Button no = new Button("No");
					no.addClickHandler(new ClickHandler() {

						
						public void onClick(ClickEvent event) {
							updateModel();
							askConfirmationDialog.hide();
						}
					});
					buttonPanel.add(yes);
					buttonPanel.add(no);
					frame.add(html);
					frame.add(buttonPanel);
					askConfirmationDialog.setWidget(frame);
					askConfirmationDialog.show();
					askConfirmationDialog.setModal(true);
					askConfirmationDialog.setAnimationEnabled(true);
					askConfirmationDialog.center();
					return;
				}
				updateModel();
				// accessCellTable.redraw();

			}
		});

		Column<World, String> delColumn = new Column<World, String>(
				new ButtonCell()) {

					@Override
					public String getValue(World object) {
						return "X";
					}

		};
		accessCellTable.addColumn(delColumn, "Actions");

		delColumn.setFieldUpdater(new FieldUpdater<World, String>() {

			
			public void update(int index, final World object, String value) {
				//remove World from Universe
				final DialogBox db = new DialogBox();
				db.setText("Confirm removal of World "+object.getName());
				db.setTitle("Confirm removal of World "+object.getName());
				VerticalPanel vp = new VerticalPanel();
				vp.addStyleName(style.distance());
				HTML confirmationText = new HTML("<div>Do you really want to remove the World <b>"+object.getName()+"</b>?</div><br/><div>This also removes the extensions defined in this world. It updates the accessibility list of the other worlds. It does not affect individuals of the universe or predicates.</div>");
				confirmationText.addStyleName(style.distance());
				HorizontalPanel hp = new HorizontalPanel();
				hp.addStyleName(style.distance());
				hp.setWidth("40%");
				hp.setSpacing(10);
				Button yes = new Button("yes");
				yes.addClickHandler(new ClickHandler() {
					
					
					public void onClick(ClickEvent event) {
						try {
							universe.getRelations().removeWorld(object);
						} catch (Exception e) {
							GWT.log(e.toString());
						}
						//update the model of whole application
						updateModel();
						db.hide();
					}
				});
				Button no = new Button("no");
				no.addClickHandler(new ClickHandler() {
					
					
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				hp.add(yes);
				hp.add(no);
				vp.add(confirmationText);
				vp.add(hp);
				db.add(vp);
				db.center();
				db.setAnimationEnabled(true);
				db.show();
			}
		});
		
		// Add a selection model to handle user selection.
		// final SingleSelectionModel<World> selectionModel = new
		// SingleSelectionModel<World>();
		// accessCellTable.setSelectionModel(selectionModel);
		// selectionModel.addSelectionChangeHandler(new
		// SelectionChangeEvent.Handler() {
		// public void onSelectionChange(SelectionChangeEvent event) {
		// World selected = selectionModel.getSelectedObject();
		// if (selected != null) {
		// lastSelectedWorld = selected;
		// }
		// }
		// });

		// Set the total row count. This isn't strictly necessary, but it
		// affects
		// paging calculations, so its good habit to keep the row count up to
		// date.
		// accessCellTable.setRowCount(universe.getWorlds().size(), true);

		// Push the data into the widget.
		// ArrayList<World> allWorlds = new
		// ArrayList<World>(universe.getWorlds());
		// accessCellTable.setRowData(0, allWorlds);

		// reuse the dataProvider from the UniverseTree
		universeTree.addDataView(accessCellTable);
		// dataProvider.addDataDisplay(accessCellTable);
		// dataProvider.setList(allWorlds);

	}

	public void updateModel() {
		parentWidget.updateModel();

		universeName.setInnerText(universe.getName());
		accessabilityConstraint = universe.getRelations().getConstraint();
		labelChangeAccessabilityConstraint.setText("Change Accessibility Constraint "+accessabilityConstraint
				.toString());
		
		ArrayList<Entry<Constant, Individual>> allEntries = new ArrayList<Entry<Constant, Individual>>(
				constants.entrySet());		
		dataProviderIndividual.setList(allEntries);

		accessCellTable.redraw();
		dataProviderIndividual.refresh();
		globalIndividualsCellTable.redraw();
		
		ArrayList<Predicate> allPreds = new ArrayList<Predicate>(universe.getPredicates());
		dataProviderPredicates.setList(allPreds);
		dataProviderPredicates.refresh();
		predicatesCellTable.redraw();

		
		universeTree.updateModel(universe);
	}

	private void createGlobalIndividualsTable() {
		// Create a CellTable.
		globalIndividualsCellTable = new CellTable<Entry<Constant, Individual>>();
		globalIndividualsCellTable.setWidth("100%");
		globalIndividualsCellTable.setTitle("Click on the content of the table cells to edit the mapping of constants and individuals");
		globalIndividualsCellTable
				.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

		// Create a Pager to control the table.
		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		globalIndividualsPager = new SimplePager(TextLocation.CENTER,
				pagerResources, false, 0, true);
		globalIndividualsPager.setDisplay(globalIndividualsCellTable);
		globalIndividualsPager.setWidth("100%");
		globalIndividualsPager.setPageSize(5);

		// Add a text column to show the address.
		Column<Entry<Constant, Individual>, Constant> constantColumn = new Column<Entry<Constant, Individual>, Constant>(new ConstantCell()) {
			@Override
			public Constant getValue(Entry<Constant, Individual> entry) {
				return entry.getKey();
			}
		};
		globalIndividualsCellTable.addColumn(constantColumn, "constant name");

		constantColumn.setFieldUpdater(new FieldUpdater<Map.Entry<Constant,Individual>, Constant>() {

			
			public void update(int index, Entry<Constant, Individual> entry,
					Constant newConst) {
				if(entry.getKey().equals(newConst)) {
					//no changes
					return;
				}
				if (constants.containsKey(newConst)) {
					Window.alert("Error: Constant already taken to identify another Individual.");
					return;
				}
				Individual i = constants.remove(entry.getKey());
				constants.put(newConst, i);
				updateModel();
			}
		});
		
		final EditTextCell editCell = new EditTextCell();
		Column<Entry<Constant, Individual>, String> indiColumn = new Column<Entry<Constant, Individual>, String>(
				editCell) {

			@Override
			public String getValue(Entry<Constant, Individual> entry) {
				return entry.getValue().getName();
			}
		};

		globalIndividualsCellTable.addColumn(indiColumn, "Individual (itself)");
		indiColumn
				.setFieldUpdater(new FieldUpdater<Entry<Constant, Individual>, String>() {

					
					public void update(int index,
							final Entry<Constant, Individual> i, String value) {
						//that's not an update
						if (i.getValue().getName().equals(value)) {
							return;
						}
						
						if (UniverseFactory.validIndividualName(value) && universe.isFreeIndividualName(value)) {
							i.getValue().setName(value);
							editCell.clearViewData(i);
							updateModel();
						} else {
							final DialogBox db = new DialogBox();
							db.setText("Renaming Failed: Invalid or Duplicate Ordinary Individual Name");
							db.setAutoHideEnabled(true);
							VerticalPanel vp = new VerticalPanel();
							HTMLPanel explainPanel = new HTMLPanel("<div>The only purpose for ordinary names here are to provide you a familiar name for an individual. You tried to use either an invalid ordinary individual name or one which already connects to another individual. Although ambiguities are somethinges important in ordinary language, it makes things very complicated in the context of an artificial language. Thus, renaming is reversed and don't affect the universe.</div>");
							explainPanel.addStyleName(style.errorText());
							Button ok = new Button("OK");
							ok.addClickHandler(new ClickHandler() {
								
								
								public void onClick(ClickEvent event) {
									db.hide();
									editCell.clearViewData(i);
									updateModel();

								}
							});
							vp.add(explainPanel);
							vp.add(ok);
							vp.addStyleName(style.distance());
							vp.setSpacing(10);
							db.add(vp);
							db.setAnimationEnabled(true);
							db.show();
							db.center();
						}
					}

				});
		
		// // Add a selection model to handle user selection.
		// final SingleSelectionModel<Entry<Constant,Individual>>
		// selectionModel= new
		// SingleSelectionModel<Entry<Constant,Individual>>();
		// globalIndividualsCellTable.setSelectionModel(selectionModel);
		// selectionModel.addSelectionChangeHandler(new
		// SelectionChangeEvent.Handler() {
		// public void onSelectionChange(SelectionChangeEvent event) {
		// Entry<Constant,Individual> selected =
		// selectionModel.getSelectedObject();
		// if (selected != null) {
		// Window.alert("You selected: " + selected.getValue().getName()
		// +" referencable in the whole Universe with Constant "+selected.getKey().toString());
		// }
		// }
		// });

		// Set the total row count. This isn't strictly necessary, but it
		// affects
		// paging calculations, so its good habit to keep the row count up to
		// date.
		// globalIndividualsCellTable.setRowCount(universe.getWorlds().size(),
		// true);

		ButtonCell b = new ButtonCell();
		Column<Entry<Constant, Individual>, String> delColumn = new Column<Entry<Constant, Individual>, String>(
				new ButtonCell()) {

					@Override
					public String getValue(Entry<Constant, Individual> object) {
						return "X";
					}

		};
		globalIndividualsCellTable.addColumn(delColumn, "Actions");

		delColumn.setFieldUpdater(new FieldUpdater<Entry<Constant, Individual>, String>() {

			
			public void update(int index,final Entry<Constant, Individual> object, String value) {
				//remove Individual from Universe
				final DialogBox db = new DialogBox();
				db.setText("Confirm global removal of Individual "+object.getValue().getName());
				db.setTitle("Confirm global removal of Individual "+object.getValue().getName());
				VerticalPanel vp = new VerticalPanel();
				vp.addStyleName(style.distance());
				HTML confirmationText = new HTML("<div>Do you really want to remove the Individual <b>"+object.getValue().getName()+"</b>?</div><br/><div>This removal concerns the semantical/ontological level. That means: If you confirm with 'yes' you completely kick-out the Individual from the Universe. All references from constants will be removed. Moreover, the system updates the extensions of each predicate in each world. (It's like deleting a file: All links to the file become invalid and have to be removed)</div>");
				confirmationText.addStyleName(style.distance());
				HorizontalPanel hp = new HorizontalPanel();
				hp.addStyleName(style.distance());
				hp.setWidth("40%");
				hp.setSpacing(10);
				Button yes = new Button("yes");
				yes.addClickHandler(new ClickHandler() {
					
					
					public void onClick(ClickEvent event) {
						universe.removeIndividual(object.getValue());
						//update the model of whole application
						updateModel();
						db.hide();
					}
				});
				Button no = new Button("no");
				no.addClickHandler(new ClickHandler() {
					
					
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				hp.add(yes);
				hp.add(no);
				vp.add(confirmationText);
				vp.add(hp);
				db.add(vp);
				db.center();
				db.setAnimationEnabled(true);
				db.show();
			}
		});
		
		// Push the data into the widget.
		ArrayList<Entry<Constant, Individual>> allEntries = new ArrayList<Entry<Constant, Individual>>(
				constants.entrySet());
		// globalIndividualsCellTable.setRowData(0, allIndis);

		dataProviderIndividual.addDataDisplay(globalIndividualsCellTable);
		dataProviderIndividual.setList(allEntries);

	}


	private class ConstantCell extends AbstractCell<Constant> {

		private boolean editMode = false;
		private Element lastParent = null;
		private Constant formerAxxTable = null;
		private DialogBox dialogBox;
		private HTMLPanel explainationText;
		private VerticalPanel framePanel;
		private HorizontalPanel buttonPanel;
		private HorizontalPanel newConstantEditPanel;
		private HTML newConstantPrefix;
		private TextBox newConstantTextBox;
		private Label alreadyTakenLabel;
		private Button apply;
		private Button abort;
		private Button del;
		private ObjectDropBox<Integer> SelectBox;
		
		public ConstantCell() {
			super("change", "keydown", "click");
		}
		
		private void setUpDialogBox(final Individual refIndi, final Constant c, final ValueUpdater<Constant> valueUpdater) {
			dialogBox = new DialogBox();
			dialogBox.setText("Change Constant for identifying Individual "+refIndi.getName());
			explainationText = new HTMLPanel("<div>You can change or delete one name of the Individual <b><i>"+refIndi.getName()+"</i></b>. The syntax of our language only allows constant names of the form a_<b>i</b> where <b>i</b> is an integer.</div>");
			alreadyTakenLabel = new Label("Constant already used to identify another Individual");
			alreadyTakenLabel.addStyleName(style.leftDistance());
			newConstantEditPanel = new HorizontalPanel();
			newConstantPrefix = new HTML("<div>a_</div>");
			newConstantPrefix.addStyleName(style.bigger());
			newConstantTextBox = new TextBox();
			newConstantTextBox.setText(c.getIndex()+"");
			newConstantTextBox.setMaxLength(4);
			newConstantTextBox.addKeyUpHandler(new KeyUpHandler() {
				
				
				public void onKeyUp(KeyUpEvent event) {

					String currentText = newConstantTextBox.getText();
					if (isNumber(currentText)) {
						markInputAsValid(newConstantTextBox);
					} else {
						markInputAsInvalid(newConstantTextBox);
					}
					if(!isFreeIdentifier(currentText, refIndi)) {
						markInputAsInvalid(newConstantTextBox);
						newConstantEditPanel.add(alreadyTakenLabel);
						newConstantEditPanel.setCellVerticalAlignment(alreadyTakenLabel, HasVerticalAlignment.ALIGN_MIDDLE);
						
					} else {
						newConstantEditPanel.remove(alreadyTakenLabel);
					}
					
//					int code = event.getCharCode();
//					if(isNumberEventCode(code)) {
//						newConstantTextBox.addStyleName(style)
//					} else {
//						
//					}
				}
				
			});
			newConstantEditPanel.add(newConstantPrefix);
			newConstantEditPanel.add(newConstantTextBox);
			newConstantEditPanel.addStyleName(style.distance());
			newConstantEditPanel.setCellVerticalAlignment(newConstantPrefix, HasVerticalAlignment.ALIGN_MIDDLE);
			newConstantEditPanel.setCellVerticalAlignment(newConstantTextBox, HasVerticalAlignment.ALIGN_MIDDLE);
			apply = new Button("Apply");
			apply.addClickHandler(new ClickHandler() {
				
				
				public void onClick(ClickEvent event) {
					editMode = false;
					int constIndex = 0;
					try {
						constIndex = Integer.valueOf(newConstantTextBox.getText());
					} catch (NumberFormatException e){
						Window.alert("I'm afraid but a_"+newConstantTextBox.getText()+" is not a valid constant.");
						return;
					}
					
					if(isFreeIdentifier(constIndex+"", refIndi)) {
						valueUpdater.update(new Constant("a",constIndex));
						globalIndividualsCellTable.redraw();
						dialogBox.hide();
					} else {
						Window.alert("I'm afraid but a_"+constIndex+" is already used. Try again.");
					}							
				}
			});
			abort = new Button("Abort");
			abort.addClickHandler(new ClickHandler() {
				
				
				public void onClick(ClickEvent event) {
					editMode = false;
					dialogBox.hide();
					globalIndividualsCellTable.redraw();
				}
			});
			del = new Button("Delete");
			del.setTitle("Delete this Constant "+c.toString()+" for Individual "+refIndi);
			del.addClickHandler(new ClickHandler() {
				
				
				public void onClick(ClickEvent event) {
					final DialogBox db = new DialogBox();
					db.setText("Confirm the removal of Constant "+c.toString());
					db.setTitle("Confirm the removal of Constant "+c.toString());
					VerticalPanel vp = new VerticalPanel();
					vp.addStyleName(style.distance());
					HTML confirmationText = new HTML("<div>Do you really want to remove the Constant <b>"+c.toString()+"</b>?</div><br/><div>This removal only concerns the syntactical not the semantical/ontological level, i.e. the individual <i>"+refIndi.getName()+"</i> continues its existence and still can be assigned to a variable in an expression or from another constant.</div>");
					confirmationText.addStyleName(style.distance());
					HorizontalPanel hp = new HorizontalPanel();
					hp.addStyleName(style.distance());
					hp.setWidth("40%");
					hp.setSpacing(10);
					Button yes = new Button("yes");
					yes.addClickHandler(new ClickHandler() {
						
						
						public void onClick(ClickEvent event) {
							universe.getConstantMap().remove(c);
							//update the model at whole application
							updateModel();
							db.hide();
							dialogBox.hide();
						}
					});
					Button no = new Button("no");
					no.addClickHandler(new ClickHandler() {
						
						
						public void onClick(ClickEvent event) {
							globalIndividualsCellTable.redraw();
							db.hide();
						}
					});
					hp.add(yes);
					hp.add(no);
					vp.add(confirmationText);
					vp.add(hp);
					db.add(vp);
					db.center();
					db.setAnimationEnabled(true);
					db.show();
					dialogBox.hide();
				}
			});
			framePanel = new VerticalPanel();
			framePanel.setSpacing(10);
			framePanel.addStyleName(style.distance());
			buttonPanel = new HorizontalPanel();
			buttonPanel.addStyleName(style.distance());
			buttonPanel.add(apply);
			buttonPanel.add(abort);
			buttonPanel.add(del);
			framePanel.add(explainationText);
			framePanel.add(newConstantEditPanel);
			framePanel.add(buttonPanel);
			dialogBox.setWidget(framePanel);
			dialogBox.setAnimationEnabled(true);
			dialogBox.center();
			
		}

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				Constant value, SafeHtmlBuilder sb) {
			sb.appendEscaped(value.toString());			
		}
		
		@Override
		public void onBrowserEvent(Context context, Element parent,
				Constant value, NativeEvent event,
				ValueUpdater<Constant> valueUpdater) {
			
			if (value == null) {
				return;
			}

			// Call the super handler, which handlers the enter key.
			super.onBrowserEvent(context, parent, value, event, valueUpdater);

			// Handle click events.
			if ("change".equals(event.getType())) {
				Window.alert("change event: " + event.getString());
			}
			if ("click".equals(event.getType())) {
				if (editMode) {
					// edit mode - cell is the same as last click
					if (lastParent.equals(parent)) {
						return;
					}
					// edit mode - cell has changed so turn
					// editmode off and redraw the unselected cell in read mode
					// and draw the selected cell in edit mode
					// dialogBox.hide();
					// dialogBox.removeFromParent();
					editMode = false;
					setValue(context, lastParent, formerAxxTable);
					if(dialogBox != null) {
						dialogBox.hide();
					}

					// if not in edit mode and it is a click-event: draw the
					// edit-mode
				}

				formerAxxTable = new Constant(value.getName(), value.getIndex());
				lastParent = parent;
				Individual refIndi = constants.get(value);
				setUpDialogBox(refIndi, formerAxxTable, valueUpdater);

				if (!dialogBox.isShowing()) {
					dialogBox.show();
					dialogBox.center();
					dialogBox.setModal(false);
					dialogBox.setAnimationEnabled(true);
				}

				parent.setInnerHTML("<i><font color='maroon'>Editing...</font></i>");
				// parent.appendChild(horizontalPanel.getElement());
				editMode = true;
			}
			
			
		}
		
	}
	
	// Helper Methods for Individuals and Constants
	
	private void markInputAsInvalid(TextBox textbox) {
		textbox.removeStyleName(style.validInput());
		textbox.addStyleName(style.invalidInput());
	}
	private void markInputAsValid(TextBox textbox) {
		textbox.removeStyleName(style.invalidInput());
		textbox.addStyleName(style.validInput());
	}
	
	private boolean isFreeIdentifier(String currentText, Individual refIndi ) {
		Constant fictiveConst = new Constant("a", Integer.valueOf(currentText));
		Individual takenInd = constants.get(fictiveConst);
		if(takenInd == null || takenInd.equals(refIndi)) {
			return true;
		}
		return !constants.containsKey(fictiveConst);
	}

	private boolean isNumber(String currentText) {
		return currentText.matches("^([1-9]([0-9]*)?|0)$");
	}

	private boolean isNumberEventCode(int code) {
		return (code >=48 && code <=57) || ( code >= 96 && code <= 105);
	}
	
	// End of Helper Methods for Individuals 
	
	
	private class AccessabilityEditCell extends AbstractCell<HashSet<World>> {

		private boolean editMode = false;
		private Element lastParent = null;
		private Button remAllButton;
		private Button addAllButton;
		private Button abortButton;
		private Button updateButton;
		private Grid grid;
		private HashSet<World> currentAxxTable;
		private HashSet<World> formerAxxTable;
		private DialogBox dialogBox;
		private HorizontalPanel horizontalGridPanel;
		private HorizontalPanel finishHPanel;
		private VerticalPanel framePanel;
		private HandlerRegistration addAllHandReg;
		private HandlerRegistration remAllHandReg;
		private HandlerRegistration abortHandReg;
		private HandlerRegistration updateHandReg;

		public AccessabilityEditCell() {
			// Our cell responds to change events and keydown events.
			super("change", "keydown", "click");

			addAllButton = new Button("Add All");
			remAllButton = new Button("Remove All");
			abortButton = new Button("Abort");
			updateButton = new Button("Apply");
			dialogBox = new DialogBox();
			horizontalGridPanel = new HorizontalPanel();
			finishHPanel = new HorizontalPanel();
			framePanel = new VerticalPanel();
		}

		@Override
		public boolean isEditing(Cell.Context context, Element parent,
				java.util.HashSet<World> value) {
			return editMode;
		}

		@Override
		public void onBrowserEvent(Context context, Element parent,
				HashSet<World> value, NativeEvent event,
				ValueUpdater<HashSet<World>> valueUpdater) {
			// Check that the value is not null.
			if (value == null) {
				return;
			}

			// Call the super handler, which handlers the enter key.
			super.onBrowserEvent(context, parent, value, event, valueUpdater);

			// Handle click events.
			if ("change".equals(event.getType())) {
				Window.alert("change event: " + event.getString());
			}
			if ("click".equals(event.getType())) {
				if (editMode) {
					// edit mode - cell is the same as last click
					if (lastParent.equals(parent)) {
						return;
					}
					// edit mode - cell has changed so turn
					// editmode off and redraw the unselected cell in read mode
					// and draw the selected cell in edit mode
					// dialogBox.hide();
					// dialogBox.removeFromParent();
					editMode = false;
					setValue(context, lastParent, formerAxxTable);

					// if not in edit mode and it is a click-event: draw the
					// edit-mode
				}

				// set currentValue
				formerAxxTable = new HashSet<World>(value);
				lastParent = parent;
				currentAxxTable = new HashSet<World>(value);

				configButtons(context, valueUpdater, parent);

				finishHPanel.add(updateButton);
				finishHPanel.add(abortButton);
				finishHPanel.setWidth("100%");
				finishHPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				finishHPanel.setCellHorizontalAlignment(updateButton, HasHorizontalAlignment.ALIGN_LEFT);
				finishHPanel.setCellHorizontalAlignment(abortButton, HasHorizontalAlignment.ALIGN_RIGHT);
				dialogBox.setText("Modify Access Table for World");
				drawDialogBox(value);

				if (!dialogBox.isShowing()) {
					dialogBox.show();
					dialogBox.center();
					dialogBox.setModal(false);
					dialogBox.setAnimationEnabled(true);
				}

				parent.setInnerHTML("<i><font color='maroon'>Editing...</font></i>");
				// parent.appendChild(horizontalPanel.getElement());
				editMode = true;
			}
		}

		private void drawDialogBox(final HashSet<World> value) {
			Set<World> remainingWorlds = new HashSet<World>(universe.getWorlds());
			remainingWorlds.removeAll(value);
			grid = new Grid(value.size()+1, 2);
			currentAxxTable = new HashSet<World>(value);
			int row = 0;
			final ObjectDropBox<World> addNewWorldDrop= new ObjectDropBox<World>(remainingWorlds);

			for (final World w : value) {
				Label worldLabel = new Label(w.getName());
				final Label close = new Label("\u22a0");
				close.addStyleName(style.bigger());
				close.addClickHandler(new ClickHandler() {
					
					
					public void onClick(ClickEvent event) {
						value.remove(w);
						drawDialogBox(value);
					}
				});
				close.addMouseOverHandler(new MouseOverHandler() {
					
					
					public void onMouseOver(MouseOverEvent event) {
						close.addStyleName(style.mouseover());
					}
				});
				close.addMouseOutHandler(new MouseOutHandler() {
					
					
					public void onMouseOut(MouseOutEvent event) {
						close.removeStyleName(style.mouseover());
					}
				});
				close.setTitle("Remove "+w.getName()+" from access list");
				grid.setWidget(row, 0, worldLabel);
				grid.setWidget(row, 1, close);
				row++;
			}
			

			Button addButton = new Button("+");
			addButton.addClickHandler(new ClickHandler() {
				
				
				public void onClick(ClickEvent event) {
					World w = addNewWorldDrop.getSelectedObject();
					value.add(w);
					drawDialogBox(value);
				}
			});
			
			if (remainingWorlds.size() == 0) {
				addNewWorldDrop.setEnabled(false);
				addButton.setEnabled(false);
			} 
				else {
				addNewWorldDrop.setEnabled(true);
				addButton.setEnabled(true);
			}

			grid.setWidget(row, 0, addNewWorldDrop);
			grid.setWidget(row, 1, addButton);
			grid.setStyleName(style.grid());

			horizontalGridPanel.clear();
			horizontalGridPanel.add(grid);
			horizontalGridPanel.add(addAllButton);
			horizontalGridPanel.add(remAllButton);
			horizontalGridPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			horizontalGridPanel.setCellVerticalAlignment(grid, HasVerticalAlignment.ALIGN_MIDDLE);
			horizontalGridPanel.setCellVerticalAlignment(addAllButton, HasVerticalAlignment.ALIGN_MIDDLE);
			horizontalGridPanel.setCellVerticalAlignment(remAllButton, HasVerticalAlignment.ALIGN_MIDDLE);
			addAllButton.addStyleName(style.buttonCenter());
			remAllButton.addStyleName(style.buttonCenter());
			
			framePanel.clear();
			framePanel.add(horizontalGridPanel);
			framePanel.add(finishHPanel);
			framePanel.addStyleName(style.distance());

			dialogBox.clear();
			dialogBox.add(framePanel);

			refreshButtons();

		}

		private void configButtons(final Context context,
				final ValueUpdater<HashSet<World>> valueUpdater,
				final Element parent) {

			refreshButtons();
			resetHandler(addAllHandReg);
			resetHandler(remAllHandReg);
			resetHandler(updateHandReg);
//			resetHandler(abortHandReg);

			addAllHandReg = addAllButton.addClickHandler(new ClickHandler() {

				
				public void onClick(ClickEvent event) {
					drawDialogBox(new HashSet<World>(universe.getWorlds()));
					// valueUpdater.update(value);
					// editMode=false;
					// refreshButtons(value);
					// setValue(context, lastParent, value);
				}
			});

			remAllHandReg = remAllButton.addClickHandler(new ClickHandler() {

				
				public void onClick(ClickEvent event) {
					drawDialogBox(new HashSet<World>());
				}
			});

			updateHandReg = updateButton.addClickHandler(new ClickHandler() {

				
				public void onClick(ClickEvent event) {

					editMode = false;
					HashSet<World> hw = new HashSet<World>(currentAxxTable);
					// setValue(context, parent, hw);
					valueUpdater.update(hw);
					dialogBox.hide();
				}
			});

			abortHandReg = abortButton.addClickHandler(new ClickHandler() {

				
				public void onClick(ClickEvent event) {
					editMode = false;
					// HashSet<World> hw = new HashSet<World>(formerAxxTable);
					// setValue(context, parent, hw);
					updateModel();
					accessCellTable.redraw();
					dialogBox.hide();
					// valueUpdater.update(value);

				}
			});

		}

		private void resetHandler(HandlerRegistration handler) {
			if (handler != null) {
				handler.removeHandler();
			}
		}

		private void refreshButtons() {
			if (currentAxxTable.size() == universe.getWorlds().size()) {
				addAllButton.setEnabled(false);
			} else {
				addAllButton.setEnabled(true);
			}
			if (currentAxxTable.size() == 0) {
				remAllButton.setEnabled(false);
			} else {
				remAllButton.setEnabled(true);
			}
		}

		@Override
		public void render(Context context, HashSet<World> value,
				SafeHtmlBuilder sb) {
			/*
			 * Always do a null check on the value. Cell widgets can pass null
			 * to cells if the underlying data contains a null, or if the data
			 * arrives out of order.
			 */
			if (value == null) {
				return;
			}

			int count = 0;
			for (World accessibleWorld : value) {
				if (count != 0) {
					sb.appendEscaped(", ");
				}
				count++;
				sb.appendEscaped(accessibleWorld.getName());
			}
			if (count == 0) {
				sb.appendHtmlConstant("<i>none</i>");
			}
		}

		/**
		 * By convention, cells that respond to user events should handle the
		 * enter key. This provides a consistent user experience when users use
		 * keyboard navigation in the widget. Our cell will toggle the checkbox
		 * on Enter.
		 */
		@Override
		protected void onEnterKeyDown(Context context, Element parent,
				HashSet<World> value, NativeEvent event,
				ValueUpdater<HashSet<World>> valueUpdater) {
			// Toggle the checkbox.
			editMode = false;
			
		}
	}

}
