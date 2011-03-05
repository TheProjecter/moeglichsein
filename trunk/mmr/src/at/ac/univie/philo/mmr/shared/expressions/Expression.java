package at.ac.univie.philo.mmr.shared.expressions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Abstract class encapsulating the basic structure of an expression node. 
 * 
 * Note that the equals() and hashCode() functions are not defined and they
 * shouldn't be. We consider two expression nodes as being equal iff they
 * are the *same* object.
 *  
 */
public abstract class Expression implements IsSerializable {

	private Expression[] operands;
	
	/**
	 * Dummy Constructor for GWT Serialization. Don't use it yourself!
	 */
	public Expression() {
		this.operands = new Expression[0];
	}
	
	protected Expression(Expression[] operands) {
		if (operands == null) {
			throw new IllegalArgumentException("The operands array is null");
		}
		
		for (Expression operand: operands) {
			if (operand == null)
				throw new IllegalArgumentException("Null operand");
		}
		
		this.operands = operands;
	}
	
	public int getOperandCount() {
		return operands.length;
	}
	
	public Expression getOperand(int index) {
		return operands[index];
	}
	public abstract Collection<VariableExpression> freeVariables();
	public abstract Collection<VariableExpression> allVariables();
	public abstract void accept(IExpressionVisitor visitor);
//	public abstract ExpressionType getType();

	/**
	 * Replaces every occurrence of one term with another term. This is a helper-method for evaluating terms.
	 * @param oldTerm
	 * @param newTerm
	 * @return the resultingPredicateExpression (instead of oldTerm there is newTerm)
	 */
	public abstract Expression replace(TermExpression oldTerm,
			TermExpression newTerm);
	
	public Expression myClone() {
		return this;
	}

	public boolean hasSubExpression(Expression expr) {
		if (this.equals(expr)) {
			return true;
		}
		for(Expression e : operands) {
			 if (e.hasSubExpression(expr)) {
				return true;
			}
		}
		return false;
	}

	public List<Expression> getChildExpressions() {
		ArrayList<Expression> res = new ArrayList<Expression>();
		for (Expression expr : operands) {
			res.add(expr); 
		}
		return res;
	}
	
}
