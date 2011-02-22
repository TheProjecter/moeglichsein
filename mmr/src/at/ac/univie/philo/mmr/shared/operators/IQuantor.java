package at.ac.univie.philo.mmr.shared.operators;

import at.ac.univie.philo.mmr.shared.expressions.Expression;
import at.ac.univie.philo.mmr.shared.expressions.TruthExpression;
import at.ac.univie.philo.mmr.shared.expressions.Variable;
import at.ac.univie.philo.mmr.shared.semantic.Universe;
import at.ac.univie.philo.mmr.shared.semantic.World;

public interface IQuantor {

	public EnumQuantor getQuantor();
	
//	public TruthExpression evaluate(Variable boundedVar, Expression scope, Universe universe, World initialWorld);
	public String getName();
}
