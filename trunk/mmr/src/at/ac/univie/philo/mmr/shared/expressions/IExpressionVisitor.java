package at.ac.univie.philo.mmr.shared.expressions;

public interface IExpressionVisitor {
	boolean preVisit(Expression expression);
	
	//Modal Expression
	void visit(ModalExpression expression);
	//Sentence Expressions
	void visit(BinaryExpression expression);
	void visit(QuantorExpression expression);
	void visit(NegationExpression expression);
	void visit(PredicateExpression predicateExpression);
	//Term Expressions
	void visit(VariableExpression expression);
	void visit(ConstantExpression expression);
	void visit(FunctionExpression expression);
	
	//Endnode-Expression
	void visit(TruthExpression expression);



}
