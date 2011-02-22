package at.ac.univie.philo.mmr.shared.visitors;

import at.ac.univie.philo.mmr.shared.expressions.BinaryExpression;
import at.ac.univie.philo.mmr.shared.expressions.ConstantExpression;
import at.ac.univie.philo.mmr.shared.expressions.Expression;
import at.ac.univie.philo.mmr.shared.expressions.FunctionExpression;
import at.ac.univie.philo.mmr.shared.expressions.ModalExpression;
import at.ac.univie.philo.mmr.shared.expressions.NegationExpression;
import at.ac.univie.philo.mmr.shared.expressions.PredicateExpression;
import at.ac.univie.philo.mmr.shared.expressions.QuantorExpression;
import at.ac.univie.philo.mmr.shared.expressions.TermExpression;
import at.ac.univie.philo.mmr.shared.expressions.TruthExpression;
import at.ac.univie.philo.mmr.shared.expressions.VariableExpression;

public interface IExpressionVisitor {

	 boolean preVisit(Expression expression);
	 void visit(BinaryExpression expression);
	 void visit(PredicateExpression expression);
	 void visit(QuantorExpression expression);
	 void visit(ModalExpression expression);
	 void visit(NegationExpression expression);
	 void visit(TruthExpression expression);
	 void visit(VariableExpression expression);
	 void visit(ConstantExpression expression);
	 void visit(FunctionExpression expression);
}
