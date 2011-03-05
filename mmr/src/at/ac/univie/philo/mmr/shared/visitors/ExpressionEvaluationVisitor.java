package at.ac.univie.philo.mmr.shared.visitors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import at.ac.univie.philo.mmr.shared.evaluation.Comment;
import at.ac.univie.philo.mmr.shared.evaluation.EvaluationResult;
import at.ac.univie.philo.mmr.shared.evaluation.EvaluationStorage;
import at.ac.univie.philo.mmr.shared.exceptions.NotAExtensionResultException;
import at.ac.univie.philo.mmr.shared.exceptions.NotASentenceException;
import at.ac.univie.philo.mmr.shared.exceptions.PredicateNotExistsException;
import at.ac.univie.philo.mmr.shared.expressions.BinaryExpression;
import at.ac.univie.philo.mmr.shared.expressions.Constant;
import at.ac.univie.philo.mmr.shared.expressions.ConstantExpression;
import at.ac.univie.philo.mmr.shared.expressions.Expression;
import at.ac.univie.philo.mmr.shared.expressions.FunctionExpression;
import at.ac.univie.philo.mmr.shared.expressions.IExpressionVisitor;
import at.ac.univie.philo.mmr.shared.expressions.ModalExpression;
import at.ac.univie.philo.mmr.shared.expressions.NegationExpression;
import at.ac.univie.philo.mmr.shared.expressions.Predicate;
import at.ac.univie.philo.mmr.shared.expressions.PredicateExpression;
import at.ac.univie.philo.mmr.shared.expressions.QuantorExpression;
import at.ac.univie.philo.mmr.shared.expressions.TermExpression;
import at.ac.univie.philo.mmr.shared.expressions.TruthExpression;
import at.ac.univie.philo.mmr.shared.expressions.TruthValue;
import at.ac.univie.philo.mmr.shared.expressions.VariableExpression;
import at.ac.univie.philo.mmr.shared.operators.AllQuantor;
import at.ac.univie.philo.mmr.shared.operators.ExistenceQuantor;
import at.ac.univie.philo.mmr.shared.operators.IBinaryOperator;
import at.ac.univie.philo.mmr.shared.operators.IModalOperator;
import at.ac.univie.philo.mmr.shared.operators.IQuantor;
import at.ac.univie.philo.mmr.shared.operators.NegationOperator;
import at.ac.univie.philo.mmr.shared.semantic.Individual;
import at.ac.univie.philo.mmr.shared.semantic.Universe;
import at.ac.univie.philo.mmr.shared.semantic.World;

public class ExpressionEvaluationVisitor implements IExpressionVisitor {

	EvaluationStorage cache;
	World initWorld;
	Universe universe;
	HashMap<Constant,Individual> constantMap;
	private Comment errorComment;
	
	public ExpressionEvaluationVisitor(World initWorld, Universe universe) {
		if (initWorld != null && universe != null) {
			this.initWorld = initWorld;
			this.universe = universe;
			
			cache = new EvaluationStorage();
			constantMap = universe.getConstantMap();
			errorComment = new Comment();
		} else {
			throw new IllegalArgumentException("input null");
		}
	}
	
	private void valid(Expression expression) {
		if (expression == null) {
			throw new IllegalArgumentException("input expression of visitor is null.");
		}
	}
	
	@Override
	public boolean preVisit(Expression expression) {
		valid(expression);
		
		if(cache.hasResult(expression)) {
			return false;
		}
		return true;
	}

	@Override
	public void visit(BinaryExpression expression) {
		valid(expression);
		Expression left = expression.getLeft();
		Expression right = expression.getRight();
		
		EvaluationResult leftRes = cache.getResult(left);
		EvaluationResult rightRes = cache.getResult(right);
		
		
		if (leftRes != null && rightRes != null) {
			IBinaryOperator op = expression.getOperator();
			try {
				TruthExpression binaryResult = op.evaluate(leftRes.getValue(), rightRes.getValue());
				Comment comment = new Comment(leftRes.toString() + " " + op.getName() + " " + rightRes.toString() +" evaluates to: " +binaryResult.toString());
				cache.addResult(expression, new EvaluationResult(binaryResult, comment));
							
			} catch (NotASentenceException e) {
				errorComment.addLine("It should not occur that left or right Expressions of "+expression.toString()+" do not evaluate to a TruthExpression");
			}
		} else {
			errorComment.addLine("It should not occur that left and right Expressions of "+expression.toString()+" are not yet evaluated when the BinaryExpression is to be evaluated.");
		}
		
		
	}
	
	public EvaluationResult getResultOf(Expression e) {
		return cache.getResult(e);
	}

	@Override
	public void visit(QuantorExpression expression) {
		valid(expression);
		IQuantor qu = expression.getQuantor();
		EvaluationResult evalres = null;
		if (qu instanceof ExistenceQuantor) {
			evalres = evaluateExistenceExpression(expression);
		} else if (qu instanceof AllQuantor) {
			evalres = evaluateAllExpression(expression);
		}
		cache.addResult(expression, evalres);
	}

	private EvaluationResult evaluateAllExpression(QuantorExpression expression) {
		Collection<Individual> indis = initWorld.getInventory();
		VariableExpression v = new VariableExpression(expression.getBoundedVar());
		Comment comment = new Comment("We require for all Individuals a in World "+initWorld.getName() + " that the expression "+ expression.getScope() +" evaluates to true when replacing the Quantor-Variable "+v.toString()+" with Individual a:");

		for (Individual i : indis) {
			Expression exptemp = expression.getScope().replace(v, new ConstantExpression(i));
			exptemp.accept(this);
			EvaluationResult exptempRes = this.getResultOf(exptemp);
			try {
				if (exptempRes.getValue().getValue().equals(TruthValue.FALSE)) {
					comment.addLine("Is it true in World "+initWorld.getName() + " that: "+exptemp.toString()+ " ? No. We found a Counter-Example. So the allquantor-Expression evalutes to false.");
					return new EvaluationResult(new TruthExpression(false), comment);
				} else {
					comment.addLine("Is it true in World "+initWorld.getName() + " that: "+exptemp.toString()+ " ? Yes, but we need to check for the other individuals too.");
				}
			} catch (NotASentenceException e) {
				errorComment.addLine("It should not occur that evaluation of scope of a quantor does not evaluate to a sentence. That may imply that we have a free variable and have not dealt with is for now.");
			}
		}
		comment.addLine("We have not found any Individual that makes the Scope false. That means, the allquantor-Expression evalutes to true.");	
		return new EvaluationResult(new TruthExpression(true), comment);
	}

	private EvaluationResult evaluateExistenceExpression(
			QuantorExpression expression) {
		//when we say "exists" we have to replace the variable with every constant individual
		//until we found one which evaluates to true
		Collection<Individual> indis = initWorld.getInventory();
		VariableExpression v = new VariableExpression(expression.getBoundedVar());
		Comment comment = new Comment("We are looking for at least one Individual in World "+initWorld.getName() + " such that the expression "+ expression.getScope() +" evaluates to true when replacing the Quantor-Variable "+v.toString()+" with the Individual:");	

		
		for (Individual i : indis) {
			Expression exptemp = expression.getScope().replace(v, new ConstantExpression(i));
			exptemp.accept(this);
			EvaluationResult exptempRes = this.getResultOf(exptemp);
			try {
				if (exptempRes.getValue().getValue().equals(TruthValue.TRUE)) {
					comment.addLine("Is it true in World "+initWorld.getName() + " that: "+exptemp.toString()+ " ? Yes. So the quantor-Expression evalutes to true.");	
					return new EvaluationResult(new TruthExpression(true),comment);
				} else {
					comment.addLine("Is it true in World "+initWorld.getName() + " that: "+exptemp.toString()+ " ? No.");	
				}
			} catch (NotASentenceException e) {
				errorComment.addLine("it should not occur that the evaluation of the scope of a quantor does not evaluate to a sentence. That may imply that we have a free variable and have not dealt with is for now.");
			}
		}
		comment.addLine("We have not found an Individual that makes the Scope true. That means, the quantor-Expression evalutes to false.");	
		
		return new EvaluationResult(new TruthExpression(false), comment);
	}

	@Override
	public void visit(ModalExpression expression) {
		valid(expression);
		
		IModalOperator mod = expression.getModalOperator();
		EvaluationResult exprRes = mod.evaluate(expression.getScope(), universe, initWorld);
		cache.addResult(expression, exprRes);
//		CommentPrinter.print("Evaluation of ModalExpression "+expression.toString() +" evaluates to "+exprRes.toString() + " in World "+initWorld.getName()+".");
	}

	@Override
	public void visit(NegationExpression expression) {
		valid(expression);
		Expression position = expression.getOperand(0);
		EvaluationResult positionRes= cache.getResult(position);
		NegationOperator negOp = expression.getOperator();
	
		try {
		if (positionRes != null) {

				if (positionRes.isSentence()) {
					TruthExpression truthRes = negOp.evaluate(positionRes.getValue());
					
					Comment comment = new Comment("Negated Expression of " +position.toString() + " evaluates to: " + truthRes.toString() +" in World "+initWorld.getName());
					cache.addResult(expression, new EvaluationResult(truthRes, comment));
				} else {
					Comment comment = new Comment("We have to negotiate a set of sequences of possible Assignments because there are free variables involved.");
					HashMap<World,HashSet<ArrayList<Individual>>> complement = getComplementOfSet(positionRes.getResult());
					cache.addResult(expression, new EvaluationResult(complement, comment));
				}
		} else {
			errorComment.addLine("It should not occur that the child node of the negation expression is not called for evaluation before the neg-expr-node itself.");
		}
		} catch (NotASentenceException e) {
			// does not occur!
			throw new RuntimeException("This never occurs.");
		} catch (NotAExtensionResultException e) {
			throw new RuntimeException("This should not occur");
		}
	}



	private HashMap<World,HashSet<ArrayList<Individual>>> getComplementOfSet(
			HashMap<World,HashSet<ArrayList<Individual>>> worldResIn) {
		
		HashMap<World,HashSet<ArrayList<Individual>>> worldResOut = new HashMap<World, HashSet<ArrayList<Individual>>>();
		
		for (World w : worldResIn.keySet()) {
			HashSet<ArrayList<Individual>> inputCombinations = worldResIn.get(w);
			HashSet<ArrayList<Individual>> outputCombinations = new HashSet<ArrayList<Individual>>();
			Individual[] indis = initWorld.getInventory().toArray(new Individual[0]);
			int numberOfFreeVars = (inputCombinations.toArray()).length;
			int[] indices;
			CombinationGenerator x = new CombinationGenerator (indis.length, numberOfFreeVars);
			
			while (x.hasMore ()) {
				ArrayList<Individual> aicombi= new ArrayList<Individual>();
			  indices = x.getNext ();
			  for (int i = 0; i < indices.length; i++) {
			    aicombi.add(indis[indices[i]]);
			  }
			  if(!inputCombinations.contains(aicombi)) {
				  outputCombinations.add(aicombi);
			  }
			}
			worldResOut.put(w, outputCombinations);
		}
		return worldResOut;
	}

	@Override
	public void visit(PredicateExpression expression) {
		valid(expression);
		TermExpression[] terms = expression.getTerms();
		
//		ArrayList<ArrayList<Individual>> resultextension = new ArrayList<ArrayList<Individual>>();
		ArrayList<Individual> evaluatedTerms = new ArrayList<Individual>();
		Comment comment = new Comment("PredicateExpression "+expression+". We need to evaluate the terms first for the World: "+initWorld.getName()+":");
		for(int i=0; i<terms.length; i++) {
			EvaluationResult res = cache.getResult(terms[i]);
			if (!res.isSentence()) {
				try {
					HashSet<ArrayList<Individual>> allmatchingindis = res.getResult(initWorld);
					if (allmatchingindis == null) {
						errorComment.addLine("Null result for Term: "+terms[i]);
						return;
					}
					if (allmatchingindis.size() != 1) {
						//there are free variables. We have to go to each of them and store the ones which give a true evaluation.
						evaluatePredicateWithFreeVariables(expression);
						return;
					}
					Individual indi = extractIndividual(allmatchingindis);
					comment.addLine("Term #"+i+" "+terms[i] +" evaluates to "+indi);
					evaluatedTerms.add(i, indi);
				} catch (NotAExtensionResultException e) {
					errorComment.addLine(e.toString());
				}	
			} else {
				errorComment.addLine("Something wrong. The Evaluation of a Term resulted in a TruthValue.");
				return;
			}
		}
		
		EvaluationResult predEval = evaluate(evaluatedTerms, expression.getSymbol(), initWorld, comment);
		cache.addResult(expression, predEval);
	}
	
	private EvaluationResult evaluatePredicateWithFreeVariables(PredicateExpression expression) {
		try {
			ArrayList<Integer> freeVarIndices = expression.getfreeVariableIndices();
			HashSet<ArrayList<Individual>> haiResult = new HashSet<ArrayList<Individual>>();
			//generate and all possible combinations of Individuals for the number of free variables
			HashSet<PredicateExpression> input = new HashSet<PredicateExpression>();
			input.add(expression);
			Collection<PredicateExpression> allPossiblePredExpressions = expandAllCombinationsOfIndividualAssignmentsForFreeVariables(input);
			//evaluate all possible PEs
			for (PredicateExpression pe : allPossiblePredExpressions) {
				visit(pe);
				EvaluationResult evalRes = cache.getResult(pe);
				TermExpression[] terms = pe.getTerms();
				if (evalRes.isSentence()) {
					ArrayList<Individual> oneAi = new ArrayList<Individual>();
					//now extract the assignments for each free variable if it evaluates to TRUE and add it to the result.
					if (evalRes.getValue().getValue().equals(TruthValue.TRUE)) {
						for(Integer index : freeVarIndices) {
							Individual ind = extractIndividual(terms[index]);
							oneAi.add(ind);
						}
						haiResult.add(oneAi);
					}
				} else {
					throw new RuntimeException("it should not happen that after assignment of all free Variable the PredicateExpression is still not a Sentence.");
				}
			}
			return new EvaluationResult(haiResult, initWorld, new Comment("Be aware that evaluation of expressions with free Variables is in experimental mode. Don't trust them too much."));
				
		} catch ( NotASentenceException e) {
			//should not happen
			errorComment.addLine("it should not happen that after assignment of all free Variable the PredicateExpression is still not a Sentence.");
			return null;
		}
	}

	private Individual extractIndividual(TermExpression termExpression) {
		if (termExpression instanceof ConstantExpression) {
			ConstantExpression coe = (ConstantExpression)termExpression;
			Individual ind = coe.getIndividual();
			if (ind != null) {
				return ind;
			}
		}
			throw new RuntimeException("This is not a ConstantExpression with hardcoded Individual");
	}

	private Collection<PredicateExpression> expandAllCombinationsOfIndividualAssignmentsForFreeVariables(Collection<PredicateExpression> candidates) {
		Collection<PredicateExpression> newCandidates = new HashSet<PredicateExpression>();
		boolean needToContinue = false;
		for(PredicateExpression pe : candidates) {
			Collection<VariableExpression> freeVars = pe.freeVariables();
			if (freeVars.size() == 0) {
				newCandidates.add(pe);
				continue; //this one is already finished
			} else {
				needToContinue = true;
				//get next free Var
				Iterator<VariableExpression> itr = freeVars.iterator();
				if (itr.hasNext()) {
					VariableExpression v = itr.next();
					for(Individual ind : initWorld.getInventory()) {
						newCandidates.add(pe.replace(v, new ConstantExpression(ind)));
					}	
				} else {
					//should not occur
					throw new RuntimeException("It should not occur that there are no freeVariables in the logic of the code.");
				}
			}	
		}
		return needToContinue ? expandAllCombinationsOfIndividualAssignmentsForFreeVariables(newCandidates): newCandidates;
	}

	private Individual extractIndividual(HashSet<ArrayList<Individual>> allmatchingindis) {
		for (ArrayList<Individual> ai : allmatchingindis) {
			if (ai.size() == 1) {
				return ai.get(0);
			} else {
				errorComment.addLine("I found out that a Term evaluates to more than one Individual. That may imply unbounded (free) variables which are not supported yet.");
			}
		}
		return null;
	}

	@Override
	public void visit(TruthExpression expression) {
		valid(expression);
		cache.addResult(expression, new EvaluationResult(expression, new Comment("For this expression there is nothing to evaluate.")));
	}

	@Override
	public void visit(VariableExpression expression) {
		valid(expression);
		Comment comment = new Comment("Free variable detected: "+expression.toString() +"Be aware that free variable handling is highly experimental.");
//		CommentPrinter.print("I therefor place every individual of the inital world as possible result.");
		//TODO! We have to make sure that in every world in this universe we have the same individuals
		Collection<Individual> indis = initWorld.getInventory();
		HashSet<ArrayList<Individual>> allIndividuals = new HashSet<ArrayList<Individual>>();
		for (Individual indi : indis) {
			ArrayList<Individual> oneIndiList = new ArrayList<Individual>();
			oneIndiList.add(indi);
			allIndividuals.add(oneIndiList);
		}
		cache.addResult(expression, new EvaluationResult(allIndividuals, initWorld, comment));
	}

	@Override
	public void visit(ConstantExpression expression) {
		valid(expression);
		ArrayList<Individual> ai = new ArrayList<Individual>();
		HashSet<ArrayList<Individual>> hai = new HashSet<ArrayList<Individual>>();
		Individual i = evaluate(expression);
		ai.add(i);
		hai.add(ai);
		cache.addResult(expression, new EvaluationResult(hai, initWorld, new Comment("Constant directly maps to Individual "+i.getName())));
//		CommentPrinter.print("Constant in World "+initWorld.getName()+" maps to Individual "+i.toString());
	}

	@Override
	public void visit(FunctionExpression expression) {
		valid(expression);
		EvaluationResult result = evaluate(expression);
		cache.addResult(expression, result);
	}
	
	/**
	 * Does a certain statement (Individual a is-a Predicate p) hold in this world?
	 *
	 * @param indilist the sequence of individuals which are filled into the terms of the predicate (one solution)
	 * @param p the predicate
	 * @return true, iff the sequence is defined in the extension of p. false, otherwise or if the predicate does not exist or the arities are different.
	 */
	public EvaluationResult evaluate(ArrayList<Individual> indilist, Predicate p, World w, Comment comment) {
		
		TruthExpression result = null;
		if (indilist != null && p != null) {
			try {
				if (w.getExtension(p).contains(indilist)) {
					result = new TruthExpression(true);
				} else {
					result = new TruthExpression(false);	
				}						
			} catch (PredicateNotExistsException e) {
				//should not occur because every predicate 'exists' in each world
				result = new TruthExpression(false);
				comment.addLine("All Terms evaluated. But Predicate does not 'exist' in World " +w.getName()+ ". Therefore evaluates to "+result.toString());
				return new EvaluationResult(result, comment);
			}
			comment.addLine("All Terms evaluated. Predicate "+p.toString()+" evaluates to "+result.toString());
			return new EvaluationResult(result, comment);
		} else {
			throw new IllegalArgumentException("Input check failed because Individual or Predicate is null.");
		}
	}
	
	public Individual evaluate(ConstantExpression exp) {
		Individual hardCodedIndividual = exp.getIndividual();
		if (hardCodedIndividual == null) {
			Individual i = constantMap.get((Constant)exp.getSymbol());
			return i;
		} else {
			if (!initWorld.hasExistingIndividual(hardCodedIndividual)) {
				throw new RuntimeException("The hardcoded Individual in this expression does not exist in this World.");
			}
			return hardCodedIndividual;
		}
	}
	
	public EvaluationResult evaluate(FunctionExpression exp) {
		errorComment.addLine("Functions are not yet supported. Thus, FunctionExpression "+exp+" cannot be evaluated.");
		return null;
	}
	
	/**
	 * The storage contains all intermediate results of the evaluated expression
	 * @return
	 */
	public EvaluationStorage getEvaluationStorage() {
		return this.cache;
	}
	
	public Comment getErrors() {
		return errorComment;
	}

}
