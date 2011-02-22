package at.ac.univie.philosophie.crf.test;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import at.ac.univie.philo.mmr.shared.expressions.TruthExpression;
import at.ac.univie.philo.mmr.shared.operators.IBinaryOperator;
import at.ac.univie.philo.mmr.shared.operators.IModalOperator;
import at.ac.univie.philo.mmr.shared.operators.IQuantor;
import at.ac.univie.philo.mmr.shared.operators.NegationOperator;
import at.ac.univie.philo.mmr.shared.operators.Operators;

public class TestEvaluation {

	IBinaryOperator andOp;
	IBinaryOperator orOp;
	IBinaryOperator xorOp;
	IBinaryOperator implOp;
	IBinaryOperator bicondOp;
	NegationOperator negOp;
	IQuantor allQu;
	IQuantor exQu;
	IModalOperator boxOp;
	IModalOperator diamondOp;
	
	TruthExpression top;
	TruthExpression bot;
	
	@Before
	public void setUp() throws Exception {
		
		//get operands
		top = new TruthExpression(true);
		bot = new TruthExpression(false);
		
		//get all operators
		andOp = Operators.getAndOperator();
		orOp = Operators.getOrOperator();
		xorOp = Operators.getXorOperator();
		implOp = Operators.getImplicationOperator();
		bicondOp = Operators.getBiconditionalOperator();
		allQu = Operators.getAllQuantorOperator();
		exQu = Operators.getExistenceQuantor();
		boxOp = Operators.getNecessityOperator();
		diamondOp = Operators.getPossibilityOperator();
		negOp = Operators.getNegationOperator();
		
		//define a simple test semantic
		
		
	}
	
	@Test
	public void testNegation() {
		TruthExpression result = negOp.evaluate(bot);
		Assert.assertTrue(result.equals(top));
		result = negOp.evaluate(top);
		Assert.assertTrue(result.equals(bot));		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullNegation() {
			negOp.evaluate(null);
	}
	
	@Test
	public void testAnd(){
		Assert.assertTrue(andOp.evaluate(top, top).equals(top));
		Assert.assertTrue(andOp.evaluate(top, bot).equals(bot));
		Assert.assertTrue(andOp.evaluate(bot, top).equals(bot));
		Assert.assertTrue(andOp.evaluate(bot, bot).equals(bot));
	}
	
	@Test
	public void testOr(){
		Assert.assertTrue(orOp.evaluate(top, top).equals(top));
		Assert.assertTrue(orOp.evaluate(top, bot).equals(top));
		Assert.assertTrue(orOp.evaluate(bot, top).equals(top));
		Assert.assertTrue(orOp.evaluate(bot, bot).equals(bot));
	}
	
	@Test
	public void testXor(){
		Assert.assertTrue(xorOp.evaluate(top, top).equals(bot));
		Assert.assertTrue(xorOp.evaluate(top, bot).equals(top));
		Assert.assertTrue(xorOp.evaluate(bot, top).equals(top));
		Assert.assertTrue(xorOp.evaluate(bot, bot).equals(bot));
	}
	
	@Test
	public void testImpl(){
		Assert.assertTrue(implOp.evaluate(top, top).equals(top));
		Assert.assertTrue(implOp.evaluate(top, bot).equals(bot));
		Assert.assertTrue(implOp.evaluate(bot, top).equals(top));
		Assert.assertTrue(implOp.evaluate(bot, bot).equals(top));
	}
	
	@Test
	public void testBicond(){
		Assert.assertTrue(bicondOp.evaluate(top, top).equals(top));
		Assert.assertTrue(bicondOp.evaluate(top, bot).equals(bot));
		Assert.assertTrue(bicondOp.evaluate(bot, top).equals(bot));
		Assert.assertTrue(bicondOp.evaluate(bot, bot).equals(top));
	}


}
