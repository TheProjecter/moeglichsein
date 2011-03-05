package at.ac.univie.philo.mmr.shared.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import com.google.gwt.user.client.rpc.IsSerializable;

import at.ac.univie.philo.mmr.shared.exceptions.NotAExtensionResultException;
import at.ac.univie.philo.mmr.shared.exceptions.NotASentenceException;
import at.ac.univie.philo.mmr.shared.expressions.TruthExpression;
import at.ac.univie.philo.mmr.shared.semantic.Individual;
import at.ac.univie.philo.mmr.shared.semantic.World;
import at.ac.univie.philo.mmr.shared.visitors.CommentPrinter;

public class EvaluationResult implements IsSerializable {

	TruthExpression truthexpression = null;
	boolean issentence = true;
	HashMap<World, HashSet<ArrayList<Individual>>> result = null;
	Comment comment;
	
	/**
	 * Dummy Constructor for GWT Serialization. Don't use it yourself!
	 */
	public EvaluationResult() {
		
	}
	
	public EvaluationResult(TruthExpression truth, Comment comment) {
		if (truth != null && comment != null) {
			this.truthexpression = truth;
			issentence = true;
			this.comment = comment;
		} else {
			throw new IllegalArgumentException("input is null");
		}
	}
	
	public EvaluationResult(HashSet<ArrayList<Individual>> result, World w, Comment comment) {
		if (result != null && w != null && comment != null) {
			issentence = false;
			this.result = new HashMap<World,HashSet<ArrayList<Individual>>>();
			this.result.put(w, result);
			this.comment = comment;
		} else {
			throw new IllegalArgumentException("input is null");
		}
	}
	
	public EvaluationResult(HashMap<World,HashSet<ArrayList<Individual>>> resultForAllWorlds, Comment comment) {
		if (resultForAllWorlds != null && comment != null) {
			issentence = false;
			this.result = resultForAllWorlds;
			this.comment = comment;
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
	
	public Comment getComment() {
		return comment;
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
