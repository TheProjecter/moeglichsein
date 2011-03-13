package at.ac.univie.philo.mmr.shared.expressions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.google.gwt.user.client.rpc.IsSerializable;

import at.ac.univie.philo.mmr.shared.operators.IBinaryOperator;

public class BinaryExpression extends FormulaExpression implements IsSerializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Expression left;
	Expression right;
	IBinaryOperator operator;
	Collection<VariableExpression> freeVars;
	Collection<VariableExpression> vars;
	
	/**
	 * Dummy Constructor for GWT Serialization. Don't use it yourself!
	 */
	public BinaryExpression() {
		super();
	}
	
	public BinaryExpression(IBinaryOperator operator, Expression left, Expression right) {
		super(new Expression[]{left,right});
		this.left = left;
		this.right = right;
		this.operator = operator;
		freeVars = new HashSet<VariableExpression>();
		freeVars.addAll(left.freeVariables());
		freeVars.addAll(right.freeVariables());
		vars = new ArrayList<VariableExpression>();
		vars.addAll(left.allVariables());
		vars.addAll(right.allVariables());
		valid();
	}
	
	private void valid() {
		if(left == null || right == null || operator == null) {
			throw new IllegalArgumentException("at least one of (operator,left or right Expression) is null");
		}
	}

	public Expression getLeft() {
		return left;
	}
	
	public Expression getRight() {
		return right;
	}
	
	public IBinaryOperator getOperator() {
		return operator;
	}

	@Override
	public void accept(IExpressionVisitor visitor) {
		if (visitor.preVisit(this)) {
			left.accept(visitor);
			right.accept(visitor);
			visitor.visit(this);
		}
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		String leftString = left.toString();
		String rightString = right.toString();
		result.append(leftString + " " + operator.getName() +" "+ rightString);
		return result.toString();
	}
	
	@Override
	public Collection<VariableExpression> allVariables() {
		return vars;
	}

	@Override
	public Collection<VariableExpression> freeVariables() {
		return freeVars;
	}

	@Override
	public Expression replace(TermExpression oldTerm, TermExpression newTerm) {
		return new BinaryExpression(operator, left.replace(oldTerm, newTerm), right.replace(oldTerm, newTerm));
	}

}
