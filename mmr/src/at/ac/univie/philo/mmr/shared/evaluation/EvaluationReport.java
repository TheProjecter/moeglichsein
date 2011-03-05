package at.ac.univie.philo.mmr.shared.evaluation;



import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

import at.ac.univie.philo.mmr.shared.exceptions.EvaluationException;
import at.ac.univie.philo.mmr.shared.expressions.Expression;
import at.ac.univie.philo.mmr.shared.semantic.Universe;
import at.ac.univie.philo.mmr.shared.semantic.World;
import at.ac.univie.philo.mmr.shared.visitors.ExpressionEvaluationVisitor;

/**
 * This is a top-level-class which generates a report for a given Expression.
 * It evaluates the Expression for all Worlds in the given Universe
 * @author Bethy
 *
 */
public class EvaluationReport implements IsSerializable{

	private Expression rootExpression;
	private Universe universe;
	private HashMap<World, EvaluationStorage> allResults;
	
	/**
	 * Dummy Constructor for GWT Serialization. Don't use it yourself!
	 */
	public EvaluationReport() {
		
	}
	
	public EvaluationReport(Expression rootExpression, Universe universe) {
		if(rootExpression != null && universe != null) {
			this.rootExpression = rootExpression;
			this.universe = universe;
			allResults = new HashMap<World, EvaluationStorage>();
			
			for (World w: universe.getWorlds()) {
				ExpressionEvaluationVisitor evaluator = new ExpressionEvaluationVisitor(w, universe);
				rootExpression.accept(evaluator);
				EvaluationStorage storage = evaluator.getEvaluationStorage();
				allResults.put(w, storage);
			}
			
		} else {
			throw new IllegalArgumentException("root Expression or universe is null");
		}
	}
	
	public EvaluationResult getResult(World w, Expression expr) throws EvaluationException {
		if (w != null && expr != null) {
			checkInputParams(w, expr);
			return allResults.get(w).getResult(expr);		
		} 
			return null;
	}
	
	/**
	 * 
	 * @param w
	 * @return the EvaluationResult for the rootExpression for the given World w
	 * @throws EvaluationException
	 */
	public EvaluationResult getResult(World w) throws EvaluationException {
		return getResult(w, rootExpression);
	}

	private void checkInputParams(World w, Expression expr) throws EvaluationException {
		if (!rootExpression.hasSubExpression(expr)) {
			throw new EvaluationException("Expression "+expr.toString()+" is not a SubExpression of rootExpression "+rootExpression);		
		}
		if(!universe.getWorlds().contains(w)) {
			throw new EvaluationException("World "+w.getName()+" is not included in Universe "+universe.getName());
		}
	}
}
