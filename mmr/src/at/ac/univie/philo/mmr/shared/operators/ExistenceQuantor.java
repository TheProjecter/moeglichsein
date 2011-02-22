package at.ac.univie.philo.mmr.shared.operators;

import java.util.Collection;

import com.google.gwt.user.client.rpc.IsSerializable;

import at.ac.univie.philo.mmr.shared.exceptions.NotASentenceException;
import at.ac.univie.philo.mmr.shared.expressions.ConstantExpression;
import at.ac.univie.philo.mmr.shared.expressions.Expression;
import at.ac.univie.philo.mmr.shared.expressions.TruthExpression;
import at.ac.univie.philo.mmr.shared.expressions.TruthValue;
import at.ac.univie.philo.mmr.shared.expressions.Variable;
import at.ac.univie.philo.mmr.shared.expressions.VariableExpression;
import at.ac.univie.philo.mmr.shared.semantic.Individual;
import at.ac.univie.philo.mmr.shared.semantic.Universe;
import at.ac.univie.philo.mmr.shared.semantic.World;
import at.ac.univie.philo.mmr.shared.visitors.EvaluationResult;
import at.ac.univie.philo.mmr.shared.visitors.ExpressionEvaluationVisitor;
import at.ac.univie.philo.mmr.shared.visitors.IExpressionVisitor;

public class ExistenceQuantor implements IQuantor,IsSerializable {

	@Override
	public EnumQuantor getQuantor() {
		return EnumQuantor.EXISTS;
	}

	@Override
	public String getName() {
		return "\\exists";
	}

}
