package at.ac.univie.philo.mmr.client.gui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SyntaxHelpDialog extends Composite {

	private static SyntaxHelpDialogUiBinder uiBinder = GWT
			.create(SyntaxHelpDialogUiBinder.class);

	interface SyntaxHelpDialogUiBinder extends
			UiBinder<Widget, SyntaxHelpDialog> {
	}

	public SyntaxHelpDialog() {
		initWidget(uiBinder.createAndBindUi(this));
//		syntaxVPanel.setWidth("800px");
//		syntaxVPanel.setHeight("800px");
//		syntaxHtml.setWidth("100%");
//		syntaxHtml.setHeight("100%");
//		scrollPanel.setHeight("100%");
//		scrollPanel.setWidth("100%");
	}
	
//	@UiField
//	HTMLPanel syntaxHtml;
//	
//	@UiField
//	VerticalPanel syntaxVPanel;
//	
//	@UiField
//	ScrollPanel scrollPanel;
	

//	@UiField
//	Button okButton;
//
//	private DialogBox syntaxDialogBox;
//	
//
//	@UiHandler("okButton")
//	void onClick(ClickEvent e) {
//		syntaxDialogBox.hide();
//	}
	

}
