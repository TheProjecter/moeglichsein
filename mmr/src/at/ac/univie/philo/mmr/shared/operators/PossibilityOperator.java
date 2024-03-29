package at.ac.univie.philo.mmr.shared.operators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import at.ac.univie.philo.mmr.shared.evaluation.Comment;
import at.ac.univie.philo.mmr.shared.evaluation.EvaluationResult;
import at.ac.univie.philo.mmr.shared.exceptions.NotAExtensionResultException;
import at.ac.univie.philo.mmr.shared.exceptions.NotASentenceException;
import at.ac.univie.philo.mmr.shared.expressions.Expression;
import at.ac.univie.philo.mmr.shared.expressions.ModalExpression;
import at.ac.univie.philo.mmr.shared.expressions.TruthExpression;
import at.ac.univie.philo.mmr.shared.expressions.TruthValue;
import at.ac.univie.philo.mmr.shared.semantic.Individual;
import at.ac.univie.philo.mmr.shared.semantic.Universe;
import at.ac.univie.philo.mmr.shared.semantic.World;
import at.ac.univie.philo.mmr.shared.visitors.CommentPrinter;
import at.ac.univie.philo.mmr.shared.visitors.ExpressionEvaluationVisitor;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PossibilityOperator implements IModalOperator, IsSerializable {

	
	public EvaluationResult evaluate(ModalExpression modalExpression, Universe universe,
			World initialWorld) {
		//for all accessible worlds we need to evaluate the expression
		//when we found one which evaluates to false, we can stop and the modal expression if false
		//if there are free variables in it, we merge the results together.
		boolean hasFreeVars = false;
		Expression scope = modalExpression.getScope();
		HashMap<World,HashSet<ArrayList<Individual>>> resultingSequences = new HashMap<World, HashSet<ArrayList<Individual>>>();
		HashSet<World> accessibleWorlds = universe.getAccessibleWorlds(initialWorld);
		Comment comment = new Comment("START of Evaluation of DIAMOND-Expression: "+modalExpression.toString()+"\n We have to find at least one World which is accessible from World "+ initialWorld.getName()+ " and such that in this world the Scope holds: "+scope+"\n From "+initialWorld.getName()+" the following "+accessibleWorlds.size()+" worlds are accessible: "+accessibleWorlds.toString());
		try {	
			for (World w : accessibleWorlds) {
				ExpressionEvaluationVisitor visitor = new ExpressionEvaluationVisitor(w, universe);
				scope.accept(visitor);
				EvaluationResult scopeResult = visitor.getResultOf(scope);
				Comment summary = visitor.summarizeComments();
				comment.addLines(summary);
				if(scopeResult.isSentence()) {
					if (scopeResult.getValue().getValue().equals(TruthValue.TRUE)) {
						comment.addLine("END of Evaluation of DIAMOND-Expression: "+modalExpression.toString()+"\n In World "+w.getName()+" the Scope "+scope+" does hold. That means we found an example and the Diamond-Expression evaluates to true.");
						if(hasFreeVars) {
							return new EvaluationResult(resultingSequences, comment);
						}
						return new EvaluationResult(new TruthExpression(true), comment);
					} else {
						comment.addLine("CHECKPOINT of Evaluation of DIAMOND-Expression: "+modalExpression.toString()+"\n In World "+w.getName()+" the Scope "+scope+" does not hold. Lets look in the other worlds.");
					}
				} else {
					hasFreeVars = true;
					resultingSequences.putAll(scopeResult.getResult());
				}
			}
		} catch (NotASentenceException e) {
			throw new RuntimeException("Should never happen.");
		} catch (NotAExtensionResultException e) {
			throw new RuntimeException("Should never happen.");
		}
		

		comment.addLine("END of Evaluation of DIAMOND-Expression: "+modalExpression.toString()+"\n We checked for all Worlds that the Scope "+scope+" hold and did not find one. The Possibility-Expression therefore evalutes to false.");
		return new EvaluationResult(new TruthExpression(false), comment);
	}

	
	public EnumModality getModality() {
		return EnumModality.DIAMOND;
	}

	
	public String getName() {
		return "\u25CA";
	}

}
