package at.ac.univie.philosophie.crf.test;

import static org.junit.Assert.*;

import java.util.Collection;

import javax.swing.text.html.HTMLEditorKit.Parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import at.ac.univie.philo.mmr.server.parsetree.ModallogicParser;
import at.ac.univie.philo.mmr.shared.expressions.Expression;
import at.ac.univie.philo.mmr.shared.expressions.VariableExpression;


public class ParseTest {

	ModallogicParser parser;
	@Before
	public void setUp() throws Exception {
		parser = new ModallogicParser();
	}

	@Test
	public void testRejectVariableOnly() {
		Expression result = parser.parseWithoutException("x");
		Assert.assertNull(result);
		result = parser.parseWithoutException("y_20");
		Assert.assertNull(result);
	}
	
	@Test
	public void testRejectFunctionOnly() {
		Expression result = parser.parseWithoutException("a");
		Assert.assertNull(result);
		result = parser.parseWithoutException("decadd^10(a,a_2,a_3,a_4,a_5,a_6,a_7,a_8,a_9,a_10)");
		Assert.assertNull(result);
	}
	
	@Test
	public void testAcceptTruthValue() {
		Expression result = 
			     parser.parseWithoutException("\\top");
		Assert.assertNotNull(result);
		result = parser.parseWithoutException("\\bot");
		Assert.assertNotNull(result);
		result = parser.parseWithoutException("\\top \\wedge \\bot");
		Assert.assertNotNull(result);
		result = parser.parseWithoutException("\\bot \\vee \\bot");
		Assert.assertNotNull(result);
		result = parser.parseWithoutException("\\bot \\rightarrow \\top");
		Assert.assertNotNull(result);
		result = parser.parseWithoutException("\\bot \\oplus \\top");
		Assert.assertNotNull(result);
		result = parser.parseWithoutException("\\bot \\leftrightarrow \\top");
		Assert.assertNotNull(result);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRejectPredicatesWithWrongArity() {
		Expression result = 	parser.parseWithoutException("PRED^2(x)");
	}
	
	@Test
	public void testRejectPredicatesWithoutArity() {
		Expression result = 	
			    parser.parseWithoutException("PRED(x)"); 
		Assert.assertNull(result);
		result=	parser.parseWithoutException("PRED_1()");
		Assert.assertNull(result);
	}
	
	@Test
	public void testAcceptPredicates() {
		Expression result = 	
				 parser.parseWithoutException("PRED^1(x)");
		Assert.assertNotNull(result);
		result = parser.parseWithoutException("PRED^3_1(x,y,z_200)");
		Assert.assertNotNull(result);
		result = parser.parseWithoutException("PRED^2(a,x_2)");
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testAcceptQuantorExpressionsWithAndWithoutParentheses() {
		Expression result = 	
		parser.parseWithoutException("\\forall x PRED^1(x) \\wedge \\top");
		Assert.assertNotNull(result);
		result = parser.parseWithoutException("(\\forall x PRED^1(x)) \\leftrightarrow \\top");
		Assert.assertNotNull(result);
		result =parser.parseWithoutException("\\forall x_2 (PRED^3_1(x,y,z_200) \\vee \\top)");
		Assert.assertNotNull(result);
		result =parser.parseWithoutException("\\exists y_3 PRED^2(a,x_2) \\oplus PRED^2(a,a_1)");
	}
	
	@Test
	public void testAcceptNegationExpressionsWithAndWithoutParentheses() {
		Expression result = 
		parser.parseWithoutException("\\neg \\forall x PRED^1(x) \\wedge \\top");
		result = parser.parseWithoutException("(\\neg ( \\forall x PRED^1(x)) \\leftrightarrow \\top )");
		Assert.assertNotNull(result);
		result = parser.parseWithoutException("\\forall x_2 \\neg(PRED^3_1(x,y,z_200) \\vee \\top)");
		Assert.assertNotNull(result);
		result = parser.parseWithoutException("\\exists y_3 \\neg PRED^2(a,x_2) \\oplus \\neg PRED^2(a,a_1)");
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testModelExpressions() {
		Expression result = 	
		parser.parseWithoutException("\\box \\forall x PRED^1(x) \\wedge \\top");
		Assert.assertNotNull(result);
		Assert.assertTrue(result.allVariables().size() == 1);
		Collection<VariableExpression> freeVars = result.freeVariables();
		Assert.assertTrue(result.freeVariables().size() == 0);
		result = parser.parseWithoutException("(\\diamond \\forall x PRED^1(x)) \\leftrightarrow \\top");
		Assert.assertNotNull(result);
		result =parser.parseWithoutException("\\box \\forall x_2 (PRED^3_1(x,y,z_200) \\vee \\top)");
		Assert.assertNotNull(result);
		result =parser.parseWithoutException("\\neg \\diamond (\\diamond \\exists y_3 PRED^2(add^2(x,x),x_2) \\oplus PRED^2(a,a_1))");
		Assert.assertNotNull(result);
	}

}
