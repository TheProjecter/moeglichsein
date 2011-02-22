package at.ac.univie.philo.mmr.shared.operators;

import at.ac.univie.philo.mmr.shared.expressions.Expression;
import at.ac.univie.philo.mmr.shared.semantic.Universe;
import at.ac.univie.philo.mmr.shared.semantic.World;
import at.ac.univie.philo.mmr.shared.visitors.EvaluationResult;

public interface IModalOperator {
	public EvaluationResult evaluate(Expression scope, Universe universe, World initialWorld);
	public EnumModality getModality();
	public String getName();
}
