package at.ac.univie.philo.mmr.shared.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import at.ac.univie.philo.mmr.shared.exceptions.NotAExtensionResultException;
import at.ac.univie.philo.mmr.shared.exceptions.NotASentenceException;
import at.ac.univie.philo.mmr.shared.expressions.TruthExpression;
import at.ac.univie.philo.mmr.shared.semantic.Individual;
import at.ac.univie.philo.mmr.shared.semantic.World;

public class EvaluationResult {

	TruthExpression truthexpression = null;
	boolean issentence = true;
	HashMap<World, HashSet<ArrayList<Individual>>> result = null;
	
	public EvaluationResult(TruthExpression truth) {
		if (truth != null) {
			this.truthexpression = truth;
			issentence = true;
		} else {
			throw new IllegalArgumentException("input is null");
		}
	}
	
	public EvaluationResult(HashSet<ArrayList<Individual>> result, World w) {
		if (result != null && w != null) {
			issentence = false;
			this.result = new HashMap<World,HashSet<ArrayList<Individual>>>();
			this.result.put(w, result);
		} else {
			throw new IllegalArgumentException("input is null");
		}
	}
	
	public EvaluationResult(HashMap<World,HashSet<ArrayList<Individual>>> resultForAllWorlds) {
		if (resultForAllWorlds != null) {
			issentence = false;
			this.result = resultForAllWorlds;
		} else {
			throw new IllegalArgumentException("input is null");
		}
	}

	
	public TruthExpression getValue() throws NotASentenceException {
		if (issentence) {
			return truthexpression;
		} else {
			throw new NotASentenceException("This result is not derived from a sentence");
		}
		
	}
	
	public HashMap<World,HashSet<ArrayList<Individual>>> getResult() throws NotAExtensionResultException {
		if (issentence) {
			throw new NotAExtensionResultException("The Result evaluates to a TruthValue not to a Set of Sequences of Individuals. Call: getValue().");
		}
		return result;
	}
	
	public HashSet<ArrayList<Individual>> getResult(World w) throws NotAExtensionResultException {
		if (issentence) {
			throw new NotAExtensionResultException("The Result evaluates to a TruthValue not to a Set of Sequences of Individuals. Call: getValue().");
		}
		return result.get(w);
	}
	
	public boolean isSentence() {
		return issentence;
	}
	
	
	@Override
	public String toString() {
		if (issentence) {
			return truthexpression.toString();
		} else {
			StringBuffer sb = new StringBuffer();
			for(Entry<World,HashSet<ArrayList<Individual>>> entry : result.entrySet()) {
				World w = entry.getKey();
				sb.append(w.getName()+":\n");
				sb.append(CommentPrinter.printExtension(result.get(w)));
				sb.append("\n ---------------\n");
			}
			return sb.toString();
		}
	}
}
