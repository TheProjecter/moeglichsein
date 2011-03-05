package at.ac.univie.philo.mmr.client.gui;

import at.ac.univie.philo.mmr.client.gui.UniverseDetailsForm.DetailStyle;
import at.ac.univie.philo.mmr.shared.evaluation.EvaluationReport;
import at.ac.univie.philo.mmr.shared.evaluation.EvaluationResult;
import at.ac.univie.philo.mmr.shared.exceptions.EvaluationException;
import at.ac.univie.philo.mmr.shared.exceptions.NotASentenceException;
import at.ac.univie.philo.mmr.shared.expressions.Expression;
import at.ac.univie.philo.mmr.shared.expressions.TruthExpression;
import at.ac.univie.philo.mmr.shared.expressions.TruthValue;
import at.ac.univie.philo.mmr.shared.semantic.World;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class EvaluationResultsForm extends Composite {

	private static EvaluationResultsFormUiBinder uiBinder = GWT
			.create(EvaluationResultsFormUiBinder.class);

	interface EvaluationResultsFormUiBinder extends
			UiBinder<Widget, EvaluationResultsForm> {
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
		
		String truth();
		
		String falseness();
	}
	

	@UiField
	DetailStyle style;
	
	@UiField
	VerticalPanel verticalPanel;
	@UiField
	HTMLPanel headPanel;

	@UiField
	HorizontalPanel mainExpressionPanel;
	@UiField
	Label mainExpressionLabel;
	@UiField
	Label mainExpressionText;
	
	@UiField
	HorizontalPanel mainResultPanel;
	@UiField
	Label mainResultLabel;
	@UiField
	Label mainResultText;

	@UiField
	HorizontalPanel detailsContainer;
	@UiField(provided = true)
	CellTree parsingTree;

	@UiField
	VerticalPanel detailsInfoContainer;
	@UiField
	SimplePanel worldSelectionContainer;

	@UiField
	VerticalPanel detailsCommentsContainer;
	@UiField
	HorizontalPanel inContainer;
	@UiField
	Label subExpressionLabel;
	@UiField
	Label subExpressionText;
	@UiField
	HorizontalPanel outContainer;
	@UiField
	Label resultExpressionLabel;
	@UiField
	Label resultExpressionText;
	
	
	private EvaluationReport report;
	private World initWorld;
	private MainScreen parentWidget;
	private ParseTreeModel parseTreeModel;
	private Expression rootExpression;
	
	public EvaluationResultsForm(EvaluationReport report, World initWorld, MainScreen parentWidget) {
		if (report != null && initWorld != null && parentWidget != null) {
			this.rootExpression = report.getRootExpression();
			this.report = report;
			this.initWorld = initWorld;
			this.parentWidget = parentWidget;
			parseTreeModel = new ParseTreeModel(rootExpression, this);
			parsingTree = new CellTree(parseTreeModel, null);
			initWidget(uiBinder.createAndBindUi(this));		
			setupWidgets();
			showEvalDetails(initWorld, rootExpression);
		}
	}

	public void showEvalDetails(World initWorld2, Expression rootExpression2) {
		// TODO Auto-generated method stub
		
	}

	private void setupWidgets() {
		verticalPanel.setHeight("100%");
		verticalPanel.setWidth("100%");
		verticalPanel.setStyleName(style.distance());
		headPanel.setHeight("100%");
		headPanel.setWidth("100%");
		mainExpressionPanel.setHeight("100%");
		mainExpressionPanel.setWidth("100%");
		mainExpressionPanel.setSpacing(10);
		mainExpressionPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainExpressionLabel.setHeight("100%");
		mainExpressionLabel.setWidth("100%");
		mainExpressionText.setHeight("100%");
		mainExpressionText.setWidth("100%");
		mainExpressionText.setText(rootExpression.toString());
		mainExpressionPanel.setCellVerticalAlignment(mainExpressionLabel, HasVerticalAlignment.ALIGN_MIDDLE);
		mainExpressionPanel.setCellVerticalAlignment(mainExpressionText, HasVerticalAlignment.ALIGN_MIDDLE);
		mainExpressionPanel.setCellHorizontalAlignment(mainExpressionLabel, HasHorizontalAlignment.ALIGN_LEFT);
		mainExpressionPanel.setCellHorizontalAlignment(mainExpressionText, HasHorizontalAlignment.ALIGN_LEFT);
		
		mainResultPanel.setHeight("100%");
		mainResultPanel.setWidth("80%");
		mainResultPanel.setSpacing(10);
		mainResultPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//		mainResultLabel.setHeight("100%");
//		mainResultLabel.setWidth("100%");
//		mainResultText.setHeight("100%");
//		mainResultText.setWidth("100%");
		EvaluationResult mainResult = report.getResult(initWorld);
		TruthExpression truthExpression = null;
		try {
			truthExpression = mainResult.getValue();
			if (truthExpression.getValue().equals(TruthValue.TRUE)) {
				mainResultText.addStyleName(style.truth());
			} else {
				mainResultText.addStyleName(style.falseness());
			}	
		} catch (NotASentenceException e) {
			return;
		}
		
		
		mainResultText.setText(truthExpression.toString());
		mainResultPanel.setCellVerticalAlignment(mainResultLabel, HasVerticalAlignment.ALIGN_MIDDLE);
		mainResultPanel.setCellVerticalAlignment(mainResultText, HasVerticalAlignment.ALIGN_MIDDLE);
		mainResultPanel.setCellHorizontalAlignment(mainResultLabel, HasHorizontalAlignment.ALIGN_LEFT);
		mainResultPanel.setCellHorizontalAlignment(mainResultText, HasHorizontalAlignment.ALIGN_LEFT);
		
		detailsContainer.setWidth("100%");
		detailsContainer.setHeight("100%");
		parsingTree.setHeight("100%");
		parsingTree.setWidth("100%");
		detailsInfoContainer.setWidth("100%");
		detailsInfoContainer.setHeight("100%");
		worldSelectionContainer.setWidth("100%");
		worldSelectionContainer.setHeight("100%");
		
		detailsCommentsContainer.setWidth("100%");
		detailsCommentsContainer.setHeight("100%");
	}

}
