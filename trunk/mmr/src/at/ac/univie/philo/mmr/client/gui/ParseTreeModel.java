package at.ac.univie.philo.mmr.client.gui;

import java.util.ArrayList;

import at.ac.univie.philo.mmr.shared.expressions.Expression;
import at.ac.univie.philo.mmr.shared.semantic.Universe;
import at.ac.univie.philo.mmr.shared.semantic.World;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.google.gwt.view.client.TreeViewModel.DefaultNodeInfo;

public class ParseTreeModel implements TreeViewModel {

	Expression rootExpression;
	EvaluationResultsForm parentWidget;
	ListDataProvider<Expression> childExpressionDataProvider;
	private SingleSelectionModel<Expression> selectionModel = new SingleSelectionModel<Expression>();
	
	public ParseTreeModel(Expression rootExpression, EvaluationResultsForm parentWidget) {
		if (rootExpression != null && parentWidget != null) {
			this.rootExpression = rootExpression;
			this.parentWidget = parentWidget;
		}
	}
	
	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		if (value == null) {
			ArrayList<Expression> oneExpr = new ArrayList<Expression>();
			oneExpr.add(rootExpression);
			childExpressionDataProvider = new ListDataProvider<Expression>(oneExpr);
		} else if (value instanceof Expression) {
			Expression expr = (Expression) value;
			childExpressionDataProvider = new ListDataProvider<Expression>(
					expr.getChildExpressions());
		} else {
			return null;
		}
		
		Cell<Expression> cell = new ExpressionCell();
		return new DefaultNodeInfo<Expression>(childExpressionDataProvider, cell, selectionModel, null);

	}

	@Override
	public boolean isLeaf(Object value) {
		if (value == null) return false;
		return ((Expression) value).getOperandCount() == 0;
	}

	private class ExpressionCell extends AbstractCell<Expression> {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				Expression value, SafeHtmlBuilder sb) {
			//since simpleName is not available in GWT we have to parse them out
			String className = value.getClass().getName();
			className = className.replaceAll(".*[.]","");
			sb.appendEscaped(className);
			sb.appendHtmlConstant("<br/>");
			sb.appendEscaped(value.toString());
		}
		
	}
	
}
