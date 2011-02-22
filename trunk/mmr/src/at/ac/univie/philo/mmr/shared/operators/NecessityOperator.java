package at.ac.univie.philo.mmr.shared.operators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import at.ac.univie.philo.mmr.shared.exceptions.NotAExtensionResultException;
import at.ac.univie.philo.mmr.shared.exceptions.NotASentenceException;
import at.ac.univie.philo.mmr.shared.expressions.Expression;
import at.ac.univie.philo.mmr.shared.expressions.TruthExpression;
import at.ac.univie.philo.mmr.shared.expressions.TruthValue;
import at.ac.univie.philo.mmr.shared.semantic.Individual;
import at.ac.univie.philo.mmr.shared.semantic.Universe;
import at.ac.univie.philo.mmr.shared.semantic.World;
import at.ac.univie.philo.mmr.shared.visitors.CommentPrinter;
import at.ac.univie.philo.mmr.shared.visitors.EvaluationResult;
import at.ac.univie.philo.mmr.shared.visitors.ExpressionEvaluationVisitor;

import com.google.gwt.user.client.rpc.IsSerializable;

public class NecessityOperator implements IModalOperator, IsSerializable {

	@Override
	public EvaluationResult evaluate(Expression scope, Universe universe,
			World initialWorld) {
		//for all accessible worlds we need to evaluate the expression
		//when we found one which evaluates to false, we can stop and the modal expression if false
		boolean hasFreeVars = false;
		HashMap<World,HashSet<ArrayList<Individual>>> resultingSequences = new HashMap<World, HashSet<ArrayList<Individual>>>();
		try {
			
			CommentPrinter.print("Box: We have to check for all Worlds which are accessible from World "+ initialWorld.getName()+ " that the Expression holds: "+scope);
			for (World w : universe.getAccessibleWorlds(initialWorld)) {
				ExpressionEvaluationVisitor visitor = new ExpressionEvaluationVisitor(w, universe);
				scope.accept(visitor);
				EvaluationResult scopeResult = visitor.getResultOf(scope);
				if(scopeResult.isSentence()) {
					if (scopeResult.getValue().getValue().equals(TruthValue.FALSE)) {
						CommentPrinter.print("Box: In World "+w.getName()+" the Scope "+scope+" does not hold. That means we found a counter-example and the Box-Expression evaluates to false.");
						return new EvaluationResult(new TruthExpression(false));
					} else {
						CommentPrinter.print("Box: In World "+w.getName()+" the Scope "+scope+" hold. Lets look in the other worlds.");
					}
				} else {
					hasFreeVars = true;
					resultingSequences.putAll(scopeResult.getResult());
				}
				
			}

		}catch (NotASentenceException e) {
			e.printStackTrace();
		} catch (NotAExtensionResultException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(hasFreeVars) {
			return new EvaluationResult(resultingSequences);
		}
		CommentPrinter.print("Box: We checked for all Worlds that the Scope "+scope+" hold. The Box-Expression therefore evalutes to true.");
		return new EvaluationResult(new TruthExpression(true));
	}

	@Override
	public EnumModality getModality() {
		return EnumModality.BOX;
	}

	@Override
	public String getName() {
		return "\\box";
	}

}
