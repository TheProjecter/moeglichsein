/**
 * 
 */
package at.ac.univie.philo.mmr.client.gui;

import java.util.ArrayList;

import at.ac.univie.philo.mmr.client.Dummy;
import at.ac.univie.philo.mmr.client.ModalParsingService;
import at.ac.univie.philo.mmr.client.ModalParsingServiceAsync;
import at.ac.univie.philo.mmr.client.gui.UniverseDetailsForm.DetailStyle;
import at.ac.univie.philo.mmr.shared.examples.UniverseFactory;
import at.ac.univie.philo.mmr.shared.expressions.Expression;
import at.ac.univie.philo.mmr.shared.semantic.Individual;
import at.ac.univie.philo.mmr.shared.semantic.Universe;
import at.ac.univie.philo.mmr.shared.semantic.World;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.TreeViewModel;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel.AbstractSelectionModel;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.TreeViewModel.NodeInfo;
import com.google.gwt.view.client.TreeViewModel.DefaultNodeInfo;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.SpanElement;

/**
 * @author Bethy
 *
 */
public class MainScreen extends Composite {

	private Universe referenceUniverse;
	private final UniverseFactory universeFactory;
	private final ObjectDropBox<World> worldSelector;
	private UniverseTreeModel treeModel = null;
	private TreeNode lastUniverseTreeNode = null;
	private Widget lastUniverseDetailsWidget = null;
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
		  }

		 @UiField DetailStyle style;
	 
		
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
		referenceUniverse = universeFactory.getPizzaUniverse();
	    
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
	    
	    
	    
	}

	private void setUpFormular() {
		final Button sendButton = new Button("Evaluate");
		final TextBox nameField = new TextBox();
		nameField.setText("\\box \\forall x \\exists y BETTER^2(y,x)");
		nameField.setWidth("20em");
		final Label errorLabel = new Label();
		
		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		final Button helpButton = new Button("?");
		helpButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				showSyntaxHelp();
			}
		});
		
		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
//		HTMLPanel parserPanel = mscreen.getMainScreen();
		quickEvalContainer.add(nameField, "nameFieldContainer");
		quickEvalContainer.add(sendButton, "sendButtonContainer");
		quickEvalContainer.add(errorLabel, "errorLabelContainer");
		quickEvalContainer.add(helpButton, "helpButtonContainer");
		
		worldSelector.setVisibleItemCount(1);
		updateWorldDropBox();

		
		quickEvalContainer.add(worldSelector, "worldBoxContainer");
		

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

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
				
				String textToServer = nameField.getText();
//				if (!FieldVerifier.isValidName(textToServer)) {
//					errorLabel.setText("Please enter at least four characters");
//					return;
//				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				parsingService.parse(textToServer,
						new AsyncCallback<Expression>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText("Parser Check (World: "+selectedWorld.getName()+") - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(caught.getMessage());
								dialogBox.center();
								closeButton.setFocus(true);
							}

							@Override
							public void onSuccess(Expression result) {
								dialogBox.setText("Parser Check (World: "+selectedWorld.getName()+")");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								
								if (result != null) {
									serverResponseLabel.setHTML("<br/>Expression parsed successfully. Variables: "+result.allVariables()+" (Free Variables: "+result.freeVariables()+" ).<br/>");
								} else {
									serverResponseLabel.setHTML("<br/>Not parsed successfull.<br/>");
								}
								
								dialogBox.center();
								closeButton.setFocus(true);
							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
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
	
	public void showSelectedWorld(World w) {
		Widget widget = new WorldDetailsForm(w);
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

	public UniverseTreeModel getUniverseTree() {
		return this.treeModel;
	}
	
	@UiField
	Button addButton;
	
	@UiField
	Button delButton;

	@UiField
	HTMLPanel quickEvalContainer;
	
	public SimplePanel getMainScreen() {
		return simplePanelForParserPanel;

	}

	@UiHandler("addButton")
	void onClickAdd(ClickEvent e) {
		Window.alert("Add under Development!");
	}
	
	@UiHandler("delButton")
	void onClickDel(ClickEvent e) {
		Window.alert("Delete under Development!");
	}

	public void updateModel() {
		//update cellTree
		treeModel.updateModel(this.referenceUniverse);
		updateWorldDropBox();
	}

}
