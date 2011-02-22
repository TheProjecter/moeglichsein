package at.ac.univie.philo.mmr.server.parsetree;

import java.io.InputStream;
import java.io.StringReader;

import at.ac.univie.philo.mmr.shared.exceptions.ExpressionParsingException;
import at.ac.univie.philo.mmr.shared.expressions.Expression;

public class ModallogicParser {

	crflang crflang = null;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("Type a modallogic formular by using the following latex-like syntax (case-sensitive):");
		System.out.println("<sentence> \\vee <sentence> \t\t\t Disjunction");
		System.out.println("<sentence> \\wedge <sentence> \t\t\t Conjunction");
		System.out.println("<sentence> \\oplus  <sentence> \t\t\t Exclusive OR");
		System.out.println("<sentence> \\rightarrow <sentence> \t\t Implication");
		System.out.println("<sentence> \\leftrightarrow <sentence>\t\t Biconditional");
		System.out.println("\\box <sentence> \t\t\t\t Neccessity (Box-Operator)");
		System.out.println("\\diamond <sentence>\t\t\t\t Possibility (Diamond-Operator)");
		System.out.println("\\exists <var> \t\t\t\t\t Existence-Quantor");
		System.out.println("\\forall <var> \t\t\t\t\t All-Quantor");
		System.out.println("x,x_1,x_2,x_3,... y,y_1,...,z,z_1,... \t\t Variables");
		System.out.println("a,a_1,a_2,... \t\t\t\t\t Constants");
		System.out.println("f^1(<term1>),f^2_1(<t>,<t>),f^3_2... \t\t Functions");		
		System.out.println("PRED^1(<term1>), PRED^3_1(<t>,<t>,<t>) \t\t PREDICATE where a <t> is a variable, a constant or a function");
		System.out.println("^n \t\t\t\t\t\t n is the arity of the function or the predicate, that means there must be n terms as arguments.");
		System.out.println("_i \t\t\t\t\t\t i is the index of the function or the predicate. For each arity there are infinite many functions or predicates.");
		System.out.println("\\neg <sentence>\t\t\t\t\t Negation");
		System.out.println("\n\n The evaluation order is: \\neg \\forall \\exists \\wedge \\vee \\rightarrow \\leftrightarrow");
		System.out.println("----------------------------------------------------------------------------------");
		System.out.print(" # ");
			if (new ModallogicParser().parse(System.in)) {
				System.out.println("Valid Expression");
			} else {
				System.out.println("Invalid Expression");
			}
//		CrfParser thisParser = new CrfParser();
//		thisParser.parse("PRED(x,f(x),a_1)");
//		thisParser.parse("\\exists y (PRED(y) \\vee PRED(y))");
//		thisParser.parse("(\\forall x PRED(x) \\vee PRED_1(a_1,a_2,a_3,a_4,x)) \\wedge \\exists y PRED_2(x,y)");
//		thisParser.parse("\\exists x ( PRED(x) \\wedge \\forall y ( PRED_1(y) \\rightarrow PRED_2(x,y) ) )");
	}

	public boolean parse (InputStream inputStream) {
		if (crflang == null) {
			crflang = new crflang( inputStream);
		} else {
			crflang.ReInit(inputStream);
		}
		try {
			SimpleNode node = crflang.predLogic();
			node.dump("-");
			ExpressionBuilderVisitor myVisitor = new ExpressionBuilderVisitor();
			node.jjtAccept(myVisitor, null);
			Expression finalExpression = myVisitor.getExpressionOf(node);
			System.out.println(finalExpression.toString());
		} catch ( ParseException e) {
			System.out.println("Not a valid expression. Reason: "+e.getMessage());
			return false;
		} catch ( TokenMgrError te ) {
			System.out.println("A token manager error! Reson: "+ te.getMessage());
			return false;
		}
		return true;
	}
	
	public Expression parse (String expression) throws ExpressionParsingException {
		if (crflang == null) {
			crflang = new crflang( new StringReader(expression));
		} else {
			crflang.ReInit(new StringReader(expression));
		}
		Expression finalExpression = null;
		try {
			SimpleNode node = crflang.predLogic();
//			node.dump("-");
			ExpressionBuilderVisitor myVisitor = new ExpressionBuilderVisitor();
			node.jjtAccept(myVisitor, null);
			finalExpression = myVisitor.getExpressionOf(node);
//			System.out.println(finalExpression.toString());
		} catch ( ParseException e) {
			throw new ExpressionParsingException("Not a valid expression. Reason: "+e.getMessage());
		} catch ( TokenMgrError te ) {
			throw new ExpressionParsingException("Not a valid expression (Token Error). Reason: "+ te.getMessage());
		}
		return finalExpression;
	}
	
	public Expression parseWithoutException (String expression) {
		try {
			return parse(expression);
		} catch (Exception e) {		
		}
		
		return null;
	}
	
}