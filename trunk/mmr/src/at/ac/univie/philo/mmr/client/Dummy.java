package at.ac.univie.philo.mmr.client;

import at.ac.univie.philo.mmr.shared.expressions.BinaryExpression;
import at.ac.univie.philo.mmr.shared.expressions.ConstantExpression;
import at.ac.univie.philo.mmr.shared.expressions.Expression;
import at.ac.univie.philo.mmr.shared.expressions.FormulaExpression;
import at.ac.univie.philo.mmr.shared.expressions.FunctionExpression;
import at.ac.univie.philo.mmr.shared.expressions.ModalExpression;
import at.ac.univie.philo.mmr.shared.expressions.NegationExpression;
import at.ac.univie.philo.mmr.shared.expressions.PredicateExpression;
import at.ac.univie.philo.mmr.shared.expressions.QuantorExpression;
import at.ac.univie.philo.mmr.shared.expressions.TermExpression;
import at.ac.univie.philo.mmr.shared.expressions.TruthExpression;
import at.ac.univie.philo.mmr.shared.expressions.VariableExpression;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Dummy implements IsSerializable { 
	
	public static final String VERSION = "0.8";
	
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
}
