package at.ac.univie.philo.mmr.shared.examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.google.gwt.dev.GWTMain;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;

import at.ac.univie.philo.mmr.shared.exceptions.ConstraintViolationException;
import at.ac.univie.philo.mmr.shared.exceptions.IndividuumDoesNotExistExcetion;
import at.ac.univie.philo.mmr.shared.expressions.Constant;
import at.ac.univie.philo.mmr.shared.expressions.Predicate;
import at.ac.univie.philo.mmr.shared.semantic.AccessabilityConstraint;
import at.ac.univie.philo.mmr.shared.semantic.Individual;
import at.ac.univie.philo.mmr.shared.semantic.Universe;
import at.ac.univie.philo.mmr.shared.semantic.World;

public class UniverseFactory {

	private static UniverseFactory instance_;
	private static Universe pizzaUniverse = null;
	private ListDataProvider<Universe> dataProviderUniverse;
	private ListDataProvider<World> dataProviderWorld;
	private ListDataProvider<Individual> dataProviderIndividual;
	
	private UniverseFactory() {
		dataProviderUniverse = new ListDataProvider<Universe>();
		dataProviderIndividual = new ListDataProvider<Individual>();
		dataProviderWorld = new ListDataProvider<World>();
	}
	
	  /**
	   * Add a display to the database. The current range of interest of the display
	   * will be populated with data.
	   *
	   * @param display a {@Link HasData}.
	   */
	  public void addDataDisplay(HasData<Universe> display) {
	    dataProviderUniverse.addDataDisplay(display);
	  }

	  public void addDataDisplay(HasData<World> display, Universe u) {
		    dataProviderWorld.addDataDisplay(display);
	  }
	  
	  public void addDataDisplay(HasData<Individual> display,World w) {
		    dataProviderIndividual.addDataDisplay(display);
		  }
	  
	
	public static UniverseFactory get() {
		if (instance_ == null) {
			instance_ = new UniverseFactory();
		}
		return instance_;
	}
	public Universe getPizzaUniverse() {
		if (pizzaUniverse == null) {
		pizzaUniverse = createPizzaUniverse();
		}
		return pizzaUniverse;
	}

	@SuppressWarnings("unchecked")
	private Universe createPizzaUniverse() {
		//setup some icons (null for now)
		Image ico1 = null;
		Image ico2 = null;
		Image ico3 = null;
	
		// #########################################################
		//                 setup some individuals
		// #########################################################
		
		Individual a1Pepperoni = new Individual("Pepperoni",ico1);
		Individual a2Champignon = new Individual("Champignon",ico1);
		Individual a3Tomatoe = new Individual("Tomatoe",ico2);
		Individual a4Capres = new Individual("Capres",ico3);
		Individual a5Mais = new Individual("Mais",ico3);
		Individual a6Salami = new Individual("Salami",ico1);
		
		// #########################################################
		//                 setup some constants
		// #########################################################
		Constant a1 = new Constant("a", 1);
		Constant a2 = new Constant("a", 2);
		Constant a3 = new Constant("a", 3);
		Constant a4 = new Constant("a", 4);
		Constant a5 = new Constant("a", 5);
		Constant a6 = new Constant("a", 6);
		Constant a7 = new Constant("a", 7);
		HashMap<Constant, Individual> constantMapping = new HashMap<Constant, Individual>();
		constantMapping.put(a1, a1Pepperoni);
		constantMapping.put(a2, a2Champignon);
		constantMapping.put(a3, a3Tomatoe);
		constantMapping.put(a4, a4Capres);
		constantMapping.put(a5, a5Mais);
		constantMapping.put(a6, a6Salami);
		constantMapping.put(a7, a5Mais);
		
		// #########################################################
		//                 setup some predicates
		// #########################################################
		Predicate p1Spicy = new Predicate("SPICY", 0, 1);
		Predicate p2PizzaIngredient = new Predicate("PIZZAINGREDIENT", 0,1);
		Predicate p3Round = new Predicate("ROUND",0,1);
		Predicate p4Red = new Predicate("RED",0,1);
		Predicate p5Meat = new Predicate("MEAT",0,1);
		Predicate p6Vegetable = new Predicate("VEGETABLE",0,1);
		Predicate p7Blue = new Predicate("BLUE",0,1);
		Predicate p8Cubic = new Predicate("CUBIC",0,1);
		Predicate p9Yellow = new Predicate("YELLOW",0,1);
		Predicate p10SmallerThan = new Predicate("SMALLERTHAN",0,2);

		// #########################################################
		//    setup a Set of all existing Individuals in my world
		// #########################################################
		
		HashSet<Individual> inventory = new HashSet<Individual>();
		inventory.add(a1Pepperoni);
		inventory.add(a2Champignon);
		inventory.add(a3Tomatoe);
		inventory.add(a4Capres);
		inventory.add(a5Mais);
		inventory.add(a6Salami); // There is no Salami in my World
		

		// #########################################################
		//    init the extension of each predicate in this World
		// #########################################################
		
		HashSet<ArrayList<Individual>> p1SpicyExt = new HashSet<ArrayList<Individual>>();
		HashSet<ArrayList<Individual>> p2PizzaIngredientExt = new HashSet<ArrayList<Individual>>();
		HashSet<ArrayList<Individual>> p3RoundExt = new HashSet<ArrayList<Individual>>();
		HashSet<ArrayList<Individual>> p4RedExt = new HashSet<ArrayList<Individual>>();
		HashSet<ArrayList<Individual>> p5MeatExt = new HashSet<ArrayList<Individual>>();
		HashSet<ArrayList<Individual>> p6VegetableExt = new HashSet<ArrayList<Individual>>();
		HashSet<ArrayList<Individual>> p7BlueExt = new HashSet<ArrayList<Individual>>();
		HashSet<ArrayList<Individual>> p8CubicExt = new HashSet<ArrayList<Individual>>();
		HashSet<ArrayList<Individual>> p9YellowExt = new HashSet<ArrayList<Individual>>();
		HashSet<ArrayList<Individual>> p10SmallerThanExt = new HashSet<ArrayList<Individual>>();
		
		// #########################################################
		//                      extension-setup
		// #########################################################
		
		ArrayList<Individual> dummy = new ArrayList<Individual>();
		dummy.add(a1Pepperoni);
		p1SpicyExt.add(dummy); // Pepperoni is spicy
		
		for(Individual i : inventory) {
			dummy = new ArrayList<Individual>();
			dummy.add(i);
			p2PizzaIngredientExt.add(dummy);	//every existing stuff here is a pizzaincredient.
												//Salami does not exist and here it is also no pizzaincredient.
		}
		
		
		
		dummy = new ArrayList<Individual>();
		dummy.add(a3Tomatoe);
		p3RoundExt.add(dummy); //Tomatoe capres and Mais (and Salami) are round.
		
		dummy = new ArrayList<Individual>();
		dummy.add(a4Capres); //Tomatoe capres and Mais (and Salami) are round.
		p3RoundExt.add(dummy);
		
		dummy = new ArrayList<Individual>();
		dummy.add(a5Mais); //Tomatoe capres and Mais (and Salami) are round.
		p3RoundExt.add(dummy);
		
		dummy = new ArrayList<Individual>();
		dummy.add(a6Salami);
		p3RoundExt.add(dummy); // a6 does not exist in this world, but is in the extension of p3. That's not allowed, so it should throw an exception later.
		
		dummy = new ArrayList<Individual>();
		dummy.add(a3Tomatoe);
		p4RedExt.add(dummy); //Tomate is red
		
		dummy = new ArrayList<Individual>();
		dummy.add(a6Salami);
		p4RedExt.add(dummy); //Tomatoe(a3) and Salami(a6) are Red, even if a6 does not exist. We talk about non-"existing" things. That's not allowed, so it should throw an exception later.
		
		dummy = new ArrayList<Individual>();
		dummy.add(a6Salami);
		p5MeatExt.add(dummy); //Salami(a6) is Meat, even if a6 does not exist. That's not allowed, so it should throw an exception later.
		
		
		//Every existing thing (a1-a5) is a vegetable.
		
		dummy = new ArrayList<Individual>();
		dummy.add(a1Pepperoni);
		p6VegetableExt.add(dummy); 
		
		dummy = new ArrayList<Individual>();
		dummy.add(a2Champignon);
		p6VegetableExt.add(dummy); //We could also write: p6.addAll(inventory);
		
		dummy = new ArrayList<Individual>();
		dummy.add(a1Pepperoni);
		p6VegetableExt.add(dummy);
		
		dummy = new ArrayList<Individual>();
		dummy.add(a4Capres);
		p6VegetableExt.add(dummy);
		
		dummy = new ArrayList<Individual>();
		dummy.add(a5Mais);
		p6VegetableExt.add(dummy);
		
		// Nothing is blue in this World.
		// Nothing is cubic in this World.
		
		dummy = new ArrayList<Individual>();
		dummy.add(a5Mais);
		p9YellowExt.add(dummy);     // Mais is yellow
		
		dummy = new ArrayList<Individual>();
		dummy.add(a5Mais);
		dummy.add(a3Tomatoe);
		p10SmallerThanExt.add(dummy); // Mais is smaller than tomatoe.
		
		
		// #########################################################
		//         Now map predicates and extensions together
		// #########################################################
		HashMap<Predicate,HashSet<ArrayList<Individual>>> extensions = new HashMap<Predicate, HashSet<ArrayList<Individual>>>();
		extensions.put(p1Spicy, p1SpicyExt); //predicate p1 got extension p1ext
		extensions.put(p2PizzaIngredient, p2PizzaIngredientExt); //vice versa mutatis mutandis
		extensions.put(p3Round, p3RoundExt);
		extensions.put(p4Red, p4RedExt);
		extensions.put(p5Meat, p5MeatExt);
		extensions.put(p6Vegetable, p6VegetableExt);
		extensions.put(p7Blue, p7BlueExt);
		extensions.put(p8Cubic, p8CubicExt);
		extensions.put(p9Yellow, p9YellowExt);
		extensions.put(p10SmallerThan, p10SmallerThanExt);
		
		//initialize a world with the declared extensions and give it a name
		Universe u = new Universe("PizzaUniverse", constantMapping);
		World w = new World("Pizza",extensions);
		World w2 = new World("Pizza2",extensions);
		World w3 = new World("Pizza3",extensions);

		
		try {
			w2.overwriteExtension(p7Blue, p4RedExt); //Salami and Tomatoe are blue in this world
			w2.overwriteExtension(p4Red, p7BlueExt); //Nothing is Red in this world
			
			
			p10SmallerThanExt = new HashSet<ArrayList<Individual>>();
			for(Individual i : inventory) {
//				if (!i.equals(a5Mais)) {
				dummy = new ArrayList<Individual>();
				dummy.add(a5Mais);
				dummy.add(i);
				p10SmallerThanExt.add(dummy); // Mais is smaller than Everything.
//				}
			}
			w3.overwriteExtension(p10SmallerThan, p10SmallerThanExt);
		} catch (IndividuumDoesNotExistExcetion e1) {
			e1.printStackTrace();
		} 
		
		try {
			u = new Universe("PizzaUniverse", AccessabilityConstraint.S5, true, constantMapping, w,w2,w3);
		} catch (ConstraintViolationException e) {
			throw new IllegalArgumentException("ConstraintViolationException "+e.getMessage());
		}
		
		return u;
	}

	//TODO! Namecheck
	public static boolean validWorldName(String value) {
		return true;
	}

	public static boolean validIndividualName(String value) {
		return true;
	}

	public static boolean validUniverseName(String universeName) {
		return true;
	}

	public static boolean validPredicateName(String value) {
		return value.matches("[a-zA-Z0-9]+");
	}
	
}
