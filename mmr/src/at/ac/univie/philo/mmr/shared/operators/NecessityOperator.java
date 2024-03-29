package at.ac.univie.philo.mmr.shared.operators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import at.ac.univie.philo.mmr.shared.evaluation.Comment;
import at.ac.univie.philo.mmr.shared.evaluation.EvaluationResult;
import at.ac.univie.philo.mmr.shared.evaluation.EvaluationStorage;
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;

public class NecessityOperator implements IModalOperator, IsSerializable {

	public EvaluationResult evaluate(ModalExpression modalExpression, Universe universe,
			World initialWorld) {
		//for all accessible worlds we need to evaluate the expression
		//when we found one which evaluates to false, we can stop and the modal expression if false
		boolean hasFreeVars = false;
		Expression scope = modalExpression.getScope();
		HashSet<World> accessibleWorlds = universe.getAccessibleWorlds(initialWorld);
		HashMap<World,HashSet<ArrayList<Individual>>> resultingSequences = new HashMap<World, HashSet<ArrayList<Individual>>>();
		Comment comment = new Comment("START of Evaluation of BOX-Expression: "+modalExpression.toString()+"\n We have to check for all Worlds which are accessible from World "+ initialWorld.getName()+ " that the Expression holds: "+scope+"\n From "+initialWorld.getName()+" the following "+accessibleWorlds.size()+" worlds are accessible: "+accessibleWorlds.toString());
		try {
			for (World w : accessibleWorlds) {
				ExpressionEvaluationVisitor visitor = new ExpressionEvaluationVisitor(w, universe);
				scope.accept(visitor);
				EvaluationResult scopeResult = visitor.getResultOf(scope);
				Comment summary = visitor.summarizeComments();
				comment.addLines(summary);
				if(scopeResult.isSentence()) {
					if (scopeResult.getValue().getValue().equals(TruthValue.FALSE)) {
						comment.addLine("END of Evaluation of BOX-Expression: "+modalExpression.toString()+"\n In World "+w.getName()+" the Scope "+scope+" does not hold. That means we found a counter-example and the Box-Expression evaluates to false.");
						return new EvaluationResult(new TruthExpression(false), comment);
					} else {
						comment.addLine("CHECKPOINT of Evaluation of BOX-Expression: "+modalExpression.toString()+"\n In World "+w.getName()+" the Scope "+scope+" hold. Lets look in the other worlds.");
					}
				} else {
					hasFreeVars = true;
					resultingSequences.putAll(scopeResult.getResult());
				}
				
			}

		}catch (NotASentenceException e) {
			GWT.log(e.toString());
			return null;

		} catch (NotAExtensionResultException e) {
			GWT.log(e.toString());
			return null;
		}
		
		if(hasFreeVars) {
			return new EvaluationResult(resultingSequences, comment);
		}
		comment.addLine("END of Evaluation of BOX-Expression: "+modalExpression.toString()+"\n We checked for all Worlds that the Scope "+scope+" hold. The Box-Expression therefore evalutes to true.");
		return new EvaluationResult(new TruthExpression(true), comment);
	}

	public EnumModality getModality() {
		return EnumModality.BOX;
	}

	public String getName() {
		return "\u2610";
	}

}
