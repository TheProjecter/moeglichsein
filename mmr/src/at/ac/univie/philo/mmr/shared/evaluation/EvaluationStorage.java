package at.ac.univie.philo.mmr.shared.evaluation;


import java.util.HashMap;
import java.util.Map.Entry;

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

	public void addAllResults(EvaluationStorage exprRes) {
		if (exprRes != null) {
		evalStore.putAll(exprRes.evalStore);
		} else {
			throw new IllegalArgumentException("inputStorage is null");
		}
	}

	public Comment summarizeComments() {
		Comment summary = new Comment();
		for(Entry<Expression,EvaluationResult> entry : evalStore.entrySet()) {
			Expression expr = entry.getKey();
			EvaluationResult result = entry.getValue();
			if (result == null) {
				summary.addLine("### "+expr.toString()+" == Evaluation failed ###");
			} else {
				//skip nonsaying-comments like: Eve == EVE due to the following reasoning: Eve maps to Eve.
				if (!result.isSentence()) {
					continue;
				}
				summary.addLine("### "+expr.toString()+" == "+result.toString()+" ###");
//				summary.addLines(result.getComment());
//				summary.addLine("### END OF "+expr.toString() + "###");
			}
		}
		return summary;
	}
}
