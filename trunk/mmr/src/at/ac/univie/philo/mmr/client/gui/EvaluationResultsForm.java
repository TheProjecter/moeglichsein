package at.ac.univie.philo.mmr.client.gui;

import java.util.List;

import at.ac.univie.philo.mmr.client.gui.UniverseDetailsForm.DetailStyle;
import at.ac.univie.philo.mmr.shared.evaluation.Comment;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
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
	VerticalPanel mainExpressionLabelContainer;
	@UiField
	VerticalPanel mainExpressionTextContainer;
	@UiField
	Label mainExpressionLabel;
	@UiField
	Label mainExpressionText;

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
	@UiField
	SimplePanel commentsContainer;
	
	private EvaluationReport report;
	private World initWorld;
	private MainScreen parentWidget;
	private ParseTreeModel parseTreeModel;
	private Expression rootExpression;
	
	private ObjectDropBox<World> worldSelector;
	
	public EvaluationResultsForm(EvaluationReport report, World initWorld, MainScreen parentWidget) {
		if (report != null && initWorld != null && parentWidget != null) {
			this.rootExpression = report.getRootExpression();
			this.report = report;
			this.initWorld = initWorld;
			this.parentWidget = parentWidget;
			parseTreeModel = new ParseTreeModel(rootExpression, this);
			parsingTree = new CellTree(parseTreeModel, null);
			worldSelector = new ObjectDropBox<World>(parentWidget.getReferenceUniverse().getWorlds());
			initWidget(uiBinder.createAndBindUi(this));		
			worldSelectionContainer.setWidget(worldSelector);
			worldSelector.selectObject(initWorld);
			setupWidgets();
			showEvalDetails(rootExpression);

		}
	}

	public void showEvalDetails(Expression expression) {
		World selectedWorld = worldSelector.getSelectedObject();
		if (selectedWorld == null) {
			selectedWorld = initWorld;
		}
		subExpressionText.setText(expression.toString());
		try {
			EvaluationResult result = report.getResult(selectedWorld, expression);
//			Window.alert(result.toString());
			if (result != null) {
				resultExpressionText.setText(result.toString());
				Comment comment = result.getComment();
				List<String> lines = comment.getLines();
				Grid commentsGrid = new Grid(lines.size(), 2);
				for (int i = 0; i<lines.size(); i++) {
					commentsGrid.setWidget(i, 0, new Label((i+1)+""));
					commentsGrid.setWidget(i, 1, new HTMLPanel("<div>"+lines.get(i)+"</div>"));
				}
				commentsGrid.addStyleName(style.grid());
				commentsContainer.setWidget(commentsGrid);
			} else {
				resultExpressionText.setText("--");
				HTMLPanel info = new HTMLPanel("<div>No special comments</div>");
				info.setStyleName(style.grid());
				commentsContainer.setWidget(info);
			}
		} catch (EvaluationException e) {
			HTMLPanel errorPanel = new HTMLPanel("<div>Error: "+e.toString()+"</div>");
			errorPanel.addStyleName(style.errorText());
			commentsContainer.setWidget(errorPanel);
		}
		

		
	}

	private void setupWidgets() {
		verticalPanel.setHeight("100%");
		verticalPanel.setWidth("100%");
		verticalPanel.setStyleName(style.distance());
		headPanel.setHeight("100%");
		headPanel.setWidth("100%");
		

		mainExpressionPanel.setSpacing(10);
		mainExpressionText.setText(rootExpression.toString());


		try {
			EvaluationResult mainResult = report.getResult(initWorld);
			TruthExpression truthExpression = mainResult.getValue();
			if (truthExpression.getValue().equals(TruthValue.TRUE)) {
				mainResultText.addStyleName(style.truth());
			} else {
				mainResultText.addStyleName(style.falseness());
			}	
			mainResultText.setText(truthExpression.toString());

		} catch (NotASentenceException e) {
			Window.alert(e.toString());
		} catch (EvaluationException e) {
			Window.alert(e.toString());
		}
				

		worldSelectionContainer.addStyleName(style.distance());
		

		worldSelectionContainer.setWidth("100%");
		worldSelectionContainer.setHeight("100%");
		
		detailsCommentsContainer.setWidth("100%");
		detailsCommentsContainer.setHeight("100%");
		
		
	}

}
