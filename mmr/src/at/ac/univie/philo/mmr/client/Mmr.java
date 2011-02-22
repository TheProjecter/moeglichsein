package at.ac.univie.philo.mmr.client;

import at.ac.univie.philo.mmr.client.gui.MainScreen;
import at.ac.univie.philo.mmr.client.gui.UniverseDetailsForm;
import at.ac.univie.philo.mmr.shared.FieldVerifier;
import at.ac.univie.philo.mmr.shared.examples.UniverseFactory;
import at.ac.univie.philo.mmr.shared.expressions.Expression;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Mmr implements EntryPoint {
	
	//everything of the design we will need
	@UiField
	MainScreen mscreen;
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		mscreen = new MainScreen();
		createTopLayout();
	}

	private void createTopLayout() {
//		RootPanel.get("mainScreenContainer").add(mscreen);
        RootLayoutPanel root = RootLayoutPanel.get(); 
        root.add(mscreen); 
//        root.add(new UniverseDetailsForm(new UniverseFactory().getPizzaUniverse()));

		
	}

}
