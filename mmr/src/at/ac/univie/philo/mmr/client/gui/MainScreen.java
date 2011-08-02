/**
 * 
 */
package at.ac.univie.philo.mmr.client.gui;

import at.ac.univie.philo.mmr.client.Dummy;
import at.ac.univie.philo.mmr.client.ModalParsingService;
import at.ac.univie.philo.mmr.client.ModalParsingServiceAsync;
import at.ac.univie.philo.mmr.shared.evaluation.EvaluationReport;
import at.ac.univie.philo.mmr.shared.evaluation.EvaluationResult;
import at.ac.univie.philo.mmr.shared.examples.UniverseFactory;
import at.ac.univie.philo.mmr.shared.exceptions.EvaluationException;
import at.ac.univie.philo.mmr.shared.exceptions.NotASentenceException;
import at.ac.univie.philo.mmr.shared.expressions.Expression;
import at.ac.univie.philo.mmr.shared.semantic.Individual;
import at.ac.univie.philo.mmr.shared.semantic.Universe;
import at.ac.univie.philo.mmr.shared.semantic.World;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Bethy
 *
 */
public class MainScreen extends Composite {

	private Universe referenceUniverse;
	private final UniverseFactory universeFactory;
	public final ObjectDropBox<World> worldSelector;
	private UniverseTreeModel treeModel = null;
	private TreeNode lastUniverseTreeNode = null;
	private Widget lastUniverseDetailsWidget = null;
	private DialogBox waitingBox = null;
	private static MainScreenUiBinder uiBinder = GWT
			.create(MainScreenUiBinder.class);

	/**
	   * The UiBinder interface used by this example.
	*/
	interface MainScreenUiBinder extends UiBinder<Widget, MainScreen> {
	}
	
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final ModalParsingServiceAsync parsingService = GWT
			.create(ModalParsingService.class);
	
	  interface DetailStyle extends CssResource {
		    String mainbox();
		    String addBorder();
		    String alignElements();
		    String mouseover();
			String distance();
			String grid();
	}

		 @UiField 
		 DetailStyle style;
	 
		
		@UiField
		DockLayoutPanel overallDockPanel;
		
		@UiField
		HorizontalPanel headerHorizonalPanel;
		
		@UiField
		HTMLPanel statusHtmlPanel;
		
		@UiField
		VerticalPanel navigationVerticalPanel;
		
		@UiField
		ScrollPanel navigationTreeScrollPanel;
		
		@UiField(provided=true) 
		CellTree cellTree;
		
		@UiField
		HorizontalPanel navigationHorizontalButtonPanel;
		
		@UiField
		SimplePanel simplePanelForParserPanel;
		
		@UiField
		ScrollPanel mainScrollPanel;
		
		@UiField
		VerticalPanel mainPanel;
		
		@UiField
		SpanElement version;
		
		@UiField
		Label sendFeedbackLabel;

	/**
	 * Because this class has a default constructor, it can
	 * be used as a binder template. In other words, it can be used in other
	 * *.ui.xml files as follows:
	 * <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 *   xmlns:g="urn:import:**user's package**">
	 *  <g:**UserClassName**>Hello!</g:**UserClassName>
	 * </ui:UiBinder>
	 * Note that depending on the widget that is used, it may be necessary to
	 * implement HasHTML instead of HasText.
	 */
	public MainScreen() {
	    // Create a model for the tree.
	    treeModel = new UniverseTreeModel(this);
	    // WorldSelector for EvaluationTextBox
		worldSelector = new ObjectDropBox<World>();
		universeFactory = UniverseFactory.get();
		referenceUniverse = universeFactory.getSkiFiUniverse();
	    
	    /*
	     * Create the tree using the model. We use <code>null</code> as the default
	     * value of the root node. The default value will be passed to
	     * CustomTreeModel#getNodeInfo();
	     */
	    cellTree = new CellTree(treeModel, null);
	    cellTree.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
	    cellTree.setAnimationEnabled(true);
//	    model.setTree(cellTree);
//		cellTree.addOpenHandler(new OpenHandler<TreeNode>() {
//			@Override
//			public void onOpen(OpenEvent<TreeNode> event) {
//				TreeNode openedNode = event.getTarget();
//				if (openedNode.getValue() instanceof Universe) {
//					lastUniverseTreeNode = openedNode;
//					Universe u = (Universe) openedNode.getValue();
//					showSelectedUniverse(u);
//				}
//			}
//		});
		

	    
		initWidget(uiBinder.createAndBindUi(this));
	    cellTree.addStyleName(style.alignElements());
	    setUpFormular();
	    
	    overallDockPanel.setWidth("100%");
	    overallDockPanel.setHeight("100%");
	    headerHorizonalPanel.setWidth("100%");
	    headerHorizonalPanel.setHeight("100%");
	    statusHtmlPanel.setWidth("100%");
	    statusHtmlPanel.setHeight("100%");
	    navigationVerticalPanel.setWidth("100%");
	    navigationVerticalPanel.setHeight("100%");
	    navigationTreeScrollPanel.setWidth("100%");
	    navigationTreeScrollPanel.setHeight("100%");
	    navigationHorizontalButtonPanel.setWidth("100%");
	    navigationHorizontalButtonPanel.setHeight("100%");
	    navigationHorizontalButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	    cellTree.setWidth("100%");
	    cellTree.setHeight("100%");
	    mainPanel.setWidth("100%");
	    mainPanel.setHeight("100%");
	    mainScrollPanel.setWidth("100%");
	    mainScrollPanel.setHeight("100%");
	    simplePanelForParserPanel.setWidth("100%");
	    simplePanelForParserPanel.setHeight("100%");
	    quickEvalContainer.setWidth("100%");
	    quickEvalContainer.setHeight("100%");
	    version.setInnerText(Dummy.VERSION);
	    setupFeedbackInteraction();
	    treeModel.selectUniverse(referenceUniverse);
	    expandFirstUniverse();
	    
	}

	private void expandFirstUniverse() {
		cellTree.getRootTreeNode().setChildOpen(0, true);
	}

	private void setupFeedbackInteraction() {
		sendFeedbackLabel.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				final DialogBox db = new DialogBox(true);
				db.setStyleName(style.grid());
				db.setHeight("30%");
				db.setWidth("30%");
				db.setText("Send Feedback To Developer");
				db.setAnimationEnabled(true);
				
				VerticalPanel vp = new VerticalPanel();
				vp.setSpacing(10);
				vp.setStyleName(style.distance());
				
				HorizontalPanel purposeContainer = new HorizontalPanel();
				Label purposeLabel = new Label("Feedback Type:");
				final ListBox purposeListBox = new ListBox(false);
				purposeListBox.addItem("Feature Request");
				purposeListBox.addItem("Bug Report");
				purposeListBox.addItem("Usability Question");
				purposeListBox.addItem("Other");
				purposeContainer.add(purposeLabel);
				purposeContainer.add(purposeListBox);
				
				HorizontalPanel contactContainer = new HorizontalPanel();
				Label contactLabel = new Label("E-Mail (optional):");
				final TextBox contactBox = new TextBox();
				contactContainer.add(contactLabel);
				contactContainer.add(contactBox);
				
				HorizontalPanel textContainer = new HorizontalPanel();
				Label textLabel = new Label("Your Message:");
				final TextArea textBox = new TextArea();
				textBox.setCharacterWidth(30);
				textBox.setHeight("100px");
				textBox.setFocus(true);
				textContainer.add(textLabel);
				textContainer.add(textBox);
				
				HorizontalPanel buttonContainer = new HorizontalPanel();
				Button send = new Button("Send Report");
				send.addClickHandler(new ClickHandler() {
					
					public void onClick(ClickEvent event) {
						//get input
						String type = purposeListBox.getItemText(purposeListBox.getSelectedIndex());
						String message = textBox.getText();
						String contact = contactBox.getText();
						sendReportToServer(type, message, contact);
						db.hide();
					}
				});
				Button abort = new Button("Abort");
				abort.addClickHandler(new ClickHandler() {
					
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				buttonContainer.add(send);
				buttonContainer.add(abort);
				
				vp.add(purposeContainer);
				vp.add(textContainer);
				vp.add(contactContainer);
				vp.add(buttonContainer);
				
				db.add(vp);
				db.center();
				db.show();
			}
		});
		
		//mouse-over-events
		sendFeedbackLabel.addMouseOverHandler(new MouseOverHandler() {

			public void onMouseOver(MouseOverEvent event) {
				sendFeedbackLabel.addStyleName(style.mouseover());
			}
		});
		sendFeedbackLabel.addMouseOutHandler(new MouseOutHandler() {

			public void onMouseOut(MouseOutEvent event) {
				sendFeedbackLabel.removeStyleName(style.mouseover());
			}
		});
	}
	
	private void sendReportToServer(String type,
			String message, String contact) {
		parsingService.sendReport(type, message, contact, new AsyncCallback<Void> () {
			
			public void onSuccess(Void result) {
					final DialogBox db = new DialogBox();
					VerticalPanel vp = new VerticalPanel();
					vp.addStyleName(style.distance());
					vp.addStyleName(style.grid());
					HTMLPanel error = new HTMLPanel("<div>Feedback successfully transmitted. If you submitted your contact, you'll get an answer ASAP.<br/><br/> Best regards,<br/> Andreas Kirchner</a></div>");
					Button ok = new Button("OK");
					ok.addClickHandler(new ClickHandler() {
						
						public void onClick(ClickEvent event) {
							db.hide();
						}
					});
					vp.add(error);
					vp.add(ok);
					db.add(vp);
					db.center();
					db.setAnimationEnabled(true);
					db.show();
			}
			
			public void onFailure(Throwable caught) {
				final DialogBox db = new DialogBox();
				VerticalPanel vp = new VerticalPanel();
				vp.addStyleName(style.distance());
				vp.addStyleName(style.grid());
				HTMLPanel error = new HTMLPanel("<div>An error occured during report transmission:<br/><br/>"+caught.toString()+"<br/><br/><i>Sorry for any inconvenience. Please send a mail directly to <a href='mailto:akalypse+moeglichsein@gmail.com'>the developer</a></div>");
				Button ok = new Button("OK");
				ok.addClickHandler(new ClickHandler() {
					
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				vp.add(error);
				vp.add(ok);
				db.add(vp);
				db.center();
				db.setAnimationEnabled(true);
				db.show();
				
			}
		});
	}

	private void setUpFormular() {
		final Button sendButton = new Button("Evaluate");
		final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		oracle.add("\\box \\forall x(HUMAN^1(x) \\rightarrow CANDANCE^1(x))");
		oracle.add("\\neg (\\forall x \\forall y SMARTERTHAN^2(x,y))");
		oracle.add("\\forall x \\forall y FRIENDOF^2(x,y)");
		oracle.add("\\forall x DOG^1(x)");
		oracle.add("\\neg \\exists x HUMAN^1(x)");
		oracle.add("\\box \\exists x LIKESAPPLES^1(x)");
		oracle.add("\\box \\exists x LIKESAPPLES^1(a_9)");
		oracle.add("\\box \\forall x FRIENDOF^2(x,a_7)");
		oracle.add("\\forall x OLDERTHAN^2(a_10,x)");
		oracle.add("\\exists x \\forall y OLDERTHAN^2(x,y,)");
		oracle.add("\\box \\forall x \\forall y (GOD^1(x) \\rightarrow FRIENDOF^2(x,y))");
		oracle.add("\\box STUDIESINMINTIME^1(a_5)");
		oracle.add("\\forall x SMARTERTHAN^2(a_3,x)");
		oracle.add("\\box \\forall x (CONSERVATIVE^1(x) \\rightarrow EVIL^1(x))");
		oracle.add("\\diamond \\neg \\forall x (EVIL^1(x) \\rightarrow CONSERVATIVE^1(x))");
		oracle.add("\\forall x (HASSMARTPHONE^1(x) \\leftrightarrow CANDANCE^1(x))");
		oracle.add("\\forall z SMARTERTHAN^2(a_10,z)");
		oracle.add("\\exists x \\exists y (OLDERTHAN^2(x,y) \\rightarrow \\diamond FRIENDOF^2(x,y))");
		
		final SuggestBox expressionField = new SuggestBox(oracle);
		expressionField.setText("\\box STUDIESINMINTIME^1(a_5)");
		expressionField.setTitle("Type a leading operator like \\forall or \\box to get suggestions");
		expressionField.setWidth("30em");
		final Label errorLabel = new Label();
		
		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		final Button helpButton = new Button("?");
		helpButton.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				showSyntaxHelp();
			}
		});
		
		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
//		HTMLPanel parserPanel = mscreen.getMainScreen();
		quickEvalContainer.add(expressionField, "nameFieldContainer");
		quickEvalContainer.add(sendButton, "sendButtonContainer");
		quickEvalContainer.add(errorLabel, "errorLabelContainer");
		quickEvalContainer.add(helpButton, "helpButtonContainer");
		
		worldSelector.setVisibleItemCount(1);
		updateWorldDropBox();

		
		quickEvalContainer.add(worldSelector, "worldBoxContainer");
		

		// Focus the cursor on the name field when the app loads
		expressionField.setFocus(true);

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending expression to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendExpressionToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendExpressionToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendExpressionToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				final World selectedWorld = worldSelector.getSelectedObject();
				if (selectedWorld == null) {
					errorLabel.setText("Please select a world to enable Evaluation. Every concrete evaluation needs a reference to the initial world (our real world).");
				}
				
				final String textToServer = expressionField.getText();
//				if (!FieldVerifier.isValidName(textToServer)) {
//					errorLabel.setText("Please enter at least four characters");
//					return;
//				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				showWaitingDialog(textToServer);
				parsingService.parse(textToServer, referenceUniverse,
						new AsyncCallback<EvaluationReport>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								hideWaitingDialog();
								dialogBox
										.setText("Evaluation (World: "+selectedWorld.getName()+") - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(caught.getMessage());
								dialogBox.center();
								closeButton.setFocus(true);
								sendButton.setEnabled(true);
							}

							public void onSuccess(EvaluationReport result) {
								hideWaitingDialog();
								oracle.add(textToServer);
								showEvaluationResults(result, selectedWorld);
								sendButton.setEnabled(true);
							}
								
						});
			}

		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
//		nameField.addKeyUpHandler(handler);
	}

	private void showWaitingDialog(String expression) {
		waitingBox = new DialogBox(false, true);
		waitingBox.setText("Evaluation in Progress...");
		
		VerticalPanel vp = new VerticalPanel();
		vp.setStyleName(style.distance());
		vp.add(new HTML("<div>Please wait... Waiting time depends on the server load, the warm-up-time of Google App Engine and the Parsing and Evaluation Process itself.</div>"));
		waitingBox.add(vp);
		waitingBox.center();
		waitingBox.show();
	}
	
	private void hideWaitingDialog() {
		waitingBox.hide();
	}
	
	private void updateWorldDropBox() {
		// 		UniverseFactory factory = UniverseFactory.get();
		worldSelector.clear();
		for(World w : referenceUniverse.getWorlds()) {
			worldSelector.addItem(w);
		}
		
	}

	public void showSyntaxHelp() {
		Widget dialog = new SyntaxHelpDialog();
		mainPanel.remove(0);
		mainPanel.add(dialog);
	}
	
	public void showSelectedUniverse(Universe universe) {
		referenceUniverse = universe;
		closeOtherUniverses(universe);
		
		//update the state
		lastUniverseDetailsWidget = new UniverseDetailsForm(universe, this);
		lastUniverseDetailsWidget.setVisible(true);
		lastUniverseDetailsWidget.setWidth("100%");
		lastUniverseDetailsWidget.setHeight("100%");

		mainPanel.remove(0);
		mainPanel.add(lastUniverseDetailsWidget);

	}
	
	public void showSelectedIndividual(Individual value, World world) {
		IndividualDetailsForm indiDetailsForm = new IndividualDetailsForm(value, world, this);
		mainPanel.remove(0);
		mainPanel.add(indiDetailsForm);
		indiDetailsForm.setWidth("100%");
		indiDetailsForm.setHeight("100%");
	}
	
	public void showSelectedWorld(World w) {
		Widget widget = new WorldDetailsForm(w, this);
		mainPanel.remove(0);
		mainPanel.add(widget);
		widget.setWidth("100%");
		widget.setHeight("100%");
	}
	
	private void closeOtherUniverses(Universe universe) {
		//close the last selected Universe-TreeNode
		if (lastUniverseTreeNode != null && lastUniverseDetailsWidget != null) {
			TreeNode root = cellTree.getRootTreeNode();
			int numUniverses = root.getChildCount();
			for (int i=0; i<numUniverses; i++) {
				if (!(universe.equals(root.getChildValue(i)))) {
					root.setChildOpen(i, false);
				}
			}
		}
	}
	
	private void showEvaluationResults(
			EvaluationReport result, World selectedWorld) {
		Widget widget = new EvaluationResultsForm(result, selectedWorld, this);
		mainPanel.remove(0);
		mainPanel.add(widget);
		widget.setWidth("100%");
		widget.setHeight("100%");
		
	}

	public UniverseTreeModel getUniverseTree() {
		return this.treeModel;
	}
	
//	@UiField
//	Button addButton;
//	
//	@UiField
//	Button delButton;

	@UiField
	HTMLPanel quickEvalContainer;
	
	public SimplePanel getMainScreen() {
		return simplePanelForParserPanel;
	}


//	@UiHandler("addButton")
//	void onClickAdd(ClickEvent e) {
//		Window.alert("Add under Development!");
//	}
//	
//	@UiHandler("delButton")
//	void onClickDel(ClickEvent e) {
//		Window.alert("Delete under Development!");
//	}

	/**
	 * Returns the last selected Universe
	 */
	public Universe getReferenceUniverse() {
		return referenceUniverse;
	}

	public void updateModel() {
		//update cellTree
		treeModel.updateModel(this.referenceUniverse);
		updateWorldDropBox();
	}

}
