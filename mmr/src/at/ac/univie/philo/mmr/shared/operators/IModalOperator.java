package at.ac.univie.philo.mmr.shared.operators;

import at.ac.univie.philo.mmr.shared.evaluation.EvaluationResult;
import at.ac.univie.philo.mmr.shared.expressions.ModalExpression;
import at.ac.univie.philo.mmr.shared.semantic.Universe;
import at.ac.univie.philo.mmr.shared.semantic.World;

public interface IModalOperator {
	public EvaluationResult evaluate(ModalExpression modalExpression, Universe universe, World initialWorld);
	public EnumModality getModality();
	public String getName();
}
