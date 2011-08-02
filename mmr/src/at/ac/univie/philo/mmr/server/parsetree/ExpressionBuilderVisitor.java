package at.ac.univie.philo.mmr.server.parsetree;
import java.util.ArrayList;
import java.util.HashMap;

import at.ac.univie.philo.mmr.shared.expressions.*;
import at.ac.univie.philo.mmr.shared.operators.AllQuantor;
import at.ac.univie.philo.mmr.shared.operators.ExistenceQuantor;
import at.ac.univie.philo.mmr.shared.operators.NecessityOperator;
import at.ac.univie.philo.mmr.shared.operators.Operators;
import at.ac.univie.philo.mmr.shared.operators.PossibilityOperator;

public class ExpressionBuilderVisitor implements crflangVisitor {

	HashMap<SimpleNode, Expression> datastructure;
	
	public ExpressionBuilderVisitor() {
		datastructure = new HashMap<SimpleNode, Expression>();
	}
	
	public Expression getExpressionOf(SimpleNode node) {
		return datastructure.get(node);
	}
	
	public Object visit(SimpleNode node, Object data) {
		if (!datastructure.containsKey(node)) { 
			node.childrenAccept(this, data);
			datastructure.put(node, datastructure.get(node.children[0]));
		}
		return null;
	}

	public Object visit(ASTexpression node, Object data) {
		if (!datastructure.containsKey(node)) { 
			node.childrenAccept(this, data);
			datastructure.put(node, datastructure.get(node.children[0]));
		}
		return null;
	}

	public Object visit(ASTtruthvalue node, Object data) {
		boolean truthval = node.getValue();
		datastructure.put(node, new TruthExpression(truthval));
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTbiconditional node, Object data) {
		if (!datastructure.containsKey(node)) { 
			node.childrenAccept(this, data);
			Expression left = datastructure.get(node.children[0]);
			Expression right = datastructure.get(node.children[1]);
			datastructure.put(node, new BinaryExpression(Operators.getBiconditionalOperator(), left, right));
		}
		return null;
	}

	public Object visit(ASTnegation node, Object data) {
		if (!datastructure.containsKey(node)) { 
			node.childrenAccept(this, data);
			FormulaExpression position = (FormulaExpression) datastructure.get(node.children[0]);
			datastructure.put(node, new NegationExpression(Operators.getNegationOperator(), position));
		}
	return null;
	}

	public Object visit(ASTconjunction node, Object data) {
		if (!datastructure.containsKey(node)) { 
			node.childrenAccept(this, data);
			Expression left = datastructure.get(node.children[0]);
			Expression right = datastructure.get(node.children[1]);
			datastructure.put(node, new BinaryExpression(Operators.getAndOperator(), left, right));
		}
		return null;
	}

	public Object visit(ASTdisjunction node, Object data) {
		if (!datastructure.containsKey(node)) { 
			node.childrenAccept(this, data);
			Expression left = datastructure.get(node.children[0]);
			Expression right = datastructure.get(node.children[1]);
			datastructure.put(node, new BinaryExpression(Operators.getOrOperator(), left, right));
		}
		return null;
	}

	
	public Object visit(ASTimplication node, Object data) {
		if (!datastructure.containsKey(node)) { 
			node.childrenAccept(this, data);
			Expression left = datastructure.get(node.children[0]);
			Expression right = datastructure.get(node.children[1]);
			datastructure.put(node, new BinaryExpression(Operators.getImplicationOperator(), left, right));
		}
		return null;
	}

	
	public Object visit(ASTxor node, Object data) {
		if (!datastructure.containsKey(node)) { 
			node.childrenAccept(this, data);
			Expression left = datastructure.get(node.children[0]);
			Expression right = datastructure.get(node.children[1]);
			datastructure.put(node, new BinaryExpression(Operators.getXorOperator(), left, right));
		}
		return null;
	}

	
	public Object visit(ASTvariable node, Object data) {
		if (!datastructure.containsKey(node)) { 
			String name = node.getName();
			int index = node.getIndex();
			Variable var = new Variable(name, index);
			VariableExpression varExpression= new VariableExpression(var);
			datastructure.put(node, varExpression);
		}
		return null;
	}

	
	public Object visit(ASTpredicate node, Object data) {
		if (!datastructure.containsKey(node)) {
			ArrayList<SimpleNode> terms = node.getTerms();
			ArrayList<TermExpression> termExpressions = new ArrayList<TermExpression>();
			for (SimpleNode term : terms) {
				term.jjtAccept(this, data);
				TermExpression e = (TermExpression) datastructure.get(term);		
				termExpressions.add(e);
			}
			
			Integer arity = node.getArity();
			Integer index = node.getIndex();
			String name = node.getName();
			
			Predicate symbol = new Predicate(name, index, arity);
			TermExpression[] termExpressionArray = (termExpressions.toArray(new TermExpression[]{}));
			PredicateExpression predEx = new PredicateExpression(symbol, termExpressionArray);
			datastructure.put(node, predEx);
		}
		return null;
	}

	
	public Object visit(ASTconstant node, Object data) {
		if (!datastructure.containsKey(node)) {
			Integer i = node.getIndex();
			String n = node.getName();
			Constant c = new Constant(n, i);
			ConstantExpression constExpression = new ConstantExpression(c);
			datastructure.put(node, constExpression);
		}
		return null;
	}

	
	public Object visit(ASTfunction node, Object data) {
		if (!datastructure.containsKey(node)) {
			ArrayList<SimpleNode> terms = node.getTerms();
			ArrayList<TermExpression> termExpressions = new ArrayList<TermExpression>();
			for (SimpleNode term : terms) {
				term.jjtAccept(this, data);
				TermExpression e = (TermExpression) datastructure.get(term);		
				termExpressions.add(e);
			}
			
			Integer arity = node.getArity();
			Integer index = node.getIndex();
			String name = node.getName();
			
			Function symbol = new Function(name , index, arity);
			FunctionExpression funEx = new FunctionExpression(symbol, (TermExpression[])termExpressions.toArray(new TermExpression[]{}));
			datastructure.put(node, funEx);
		}
		return null;
	}


	
	public Object visit(ASTallquantor node, Object data) {
		if (!datastructure.containsKey(node)) { 
			node.childrenAccept(this, data);
			FormulaExpression formular = (FormulaExpression) datastructure.get(node.children[1]);
			VariableExpression scope = (VariableExpression) datastructure.get(node.children[0]);
			AllQuantor aq = new AllQuantor();
			datastructure.put(node, new QuantorExpression(aq, (Variable)scope.getSymbol(), formular));
		}
		return null;
	}

	
	public Object visit(ASTexistencequantor node, Object data) {
		if (!datastructure.containsKey(node)) { 
			node.childrenAccept(this, data);
			FormulaExpression scope = (FormulaExpression) datastructure.get(node.children[1]);
			VariableExpression boundedVar = (VariableExpression) datastructure.get(node.children[0]);
			ExistenceQuantor eq = new ExistenceQuantor();
			datastructure.put(node, new QuantorExpression(eq, (Variable)boundedVar.getSymbol(), scope));
		}
		return null;
	}
	
	
	public Object visit(ASTbox node, Object data) {
		if (!datastructure.containsKey(node)) { 
			node.childrenAccept(this, data);
			FormulaExpression scope = (FormulaExpression) datastructure.get(node.children[0]);
			datastructure.put(node, new ModalExpression(new NecessityOperator(), scope));
		}
		return null;
	}

	
	public Object visit(ASTdiamond node, Object data) {
		if (!datastructure.containsKey(node)) { 
			node.childrenAccept(this, data);
			FormulaExpression scope = (FormulaExpression) datastructure.get(node.children[0]);
			datastructure.put(node, new ModalExpression(new PossibilityOperator(), scope));
		}
		return null;
	}

	
	public Object visit(ASTfunctionname node, Object data) {
		return node.childrenAccept(this, data);
	}
	
	
	public Object visit(ASTpredicatename node, Object data) {
		return node.childrenAccept(this, data);
	}
	
}
