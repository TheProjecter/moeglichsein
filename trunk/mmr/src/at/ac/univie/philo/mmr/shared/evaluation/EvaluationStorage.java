package at.ac.univie.philo.mmr.shared.evaluation;


import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

import at.ac.univie.philo.mmr.shared.expressions.Expression;

/**
 * Stores results of all intermediate expressions
 * @author Bethy
 *
 */
public class EvaluationStorage implements IsSerializable {

	HashMap<Expression, EvaluationResult> evalStore;
	
	public EvaluationStorage() {
		evalStore = new HashMap<Expression, EvaluationResult>();
	}
	
	public void addResult(Expression expression, EvaluationResult result) {
		if(expression != null && result != null) {
			evalStore.put(expression, result);
		}
	}
	
	public EvaluationResult getResult(Expression expression) {
		return evalStore.get(expression);
	}

	public boolean hasResult(Expression expression) {
		return evalStore.containsKey(expression);
	}
}
