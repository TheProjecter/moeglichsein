package at.ac.univie.philo.mmr.client;

import at.ac.univie.philo.mmr.shared.evaluation.EvaluationReport;
import at.ac.univie.philo.mmr.shared.evaluation.EvaluationResult;
import at.ac.univie.philo.mmr.shared.expressions.BinaryExpression;
import at.ac.univie.philo.mmr.shared.expressions.Constant;
import at.ac.univie.philo.mmr.shared.expressions.ConstantExpression;
import at.ac.univie.philo.mmr.shared.expressions.Expression;
import at.ac.univie.philo.mmr.shared.expressions.FormulaExpression;
import at.ac.univie.philo.mmr.shared.expressions.FunctionExpression;
import at.ac.univie.philo.mmr.shared.expressions.ModalExpression;
import at.ac.univie.philo.mmr.shared.expressions.NegationExpression;
import at.ac.univie.philo.mmr.shared.expressions.Predicate;
import at.ac.univie.philo.mmr.shared.expressions.PredicateExpression;
import at.ac.univie.philo.mmr.shared.expressions.QuantorExpression;
import at.ac.univie.philo.mmr.shared.expressions.TermExpression;
import at.ac.univie.philo.mmr.shared.expressions.TruthExpression;
import at.ac.univie.philo.mmr.shared.expressions.VariableExpression;
import at.ac.univie.philo.mmr.shared.semantic.AccessabilityRelation;
import at.ac.univie.philo.mmr.shared.semantic.Individual;
import at.ac.univie.philo.mmr.shared.semantic.Universe;
import at.ac.univie.philo.mmr.shared.semantic.World;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Image;

public class Dummy implements IsSerializable { 
	
	public static final String VERSION = "0.97";
	
	BinaryExpression be;
	ConstantExpression ce;
	FormulaExpression fe;
	Expression e;
	FunctionExpression funce;
	ModalExpression me;
	NegationExpression ne;
	PredicateExpression pe;
	QuantorExpression qe;
	TermExpression te;
	TruthExpression truthe;
	VariableExpression ve;
	Universe universe;
	EvaluationReport evalReport;
	EvaluationResult evalResult;
	AccessabilityRelation accessRel;
	Predicate p;
	Constant c;
	World world;
	Individual i;
}
