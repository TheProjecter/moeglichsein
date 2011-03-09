package at.ac.univie.philo.mmr.shared.operators;

import java.util.Collection;

import com.google.gwt.user.client.rpc.IsSerializable;

import at.ac.univie.philo.mmr.shared.evaluation.EvaluationResult;
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
import at.ac.univie.philo.mmr.shared.visitors.ExpressionEvaluationVisitor;

public class AllQuantor implements IQuantor, IsSerializable {

	public AllQuantor() {
		
	}
	
	@Override
	public EnumQuantor getQuantor() {
		return EnumQuantor.ALL;
	}

	@Override
	public String getName() {
		return "\u2200";
	}

}
