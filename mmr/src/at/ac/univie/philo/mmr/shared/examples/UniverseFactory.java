package at.ac.univie.philo.mmr.shared.examples;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import at.ac.univie.philo.mmr.client.gui.Resources;
import at.ac.univie.philo.mmr.shared.exceptions.ConstraintViolationException;
import at.ac.univie.philo.mmr.shared.expressions.Constant;
import at.ac.univie.philo.mmr.shared.expressions.Predicate;
import at.ac.univie.philo.mmr.shared.semantic.AccessabilityConstraint;
import at.ac.univie.philo.mmr.shared.semantic.Individual;
import at.ac.univie.philo.mmr.shared.semantic.Universe;
import at.ac.univie.philo.mmr.shared.semantic.World;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;

public class UniverseFactory {

	private static UniverseFactory instance_;
	private static Universe pizzaUniverse = null;
	private static Universe skifiUniverse = null;
	private ListDataProvider<Universe> dataProviderUniverse;
	private ListDataProvider<World> dataProviderWorld;
	private ListDataProvider<Individual> dataProviderIndividual;

	private UniverseFactory() {
		dataProviderUniverse = new ListDataProvider<Universe>();
		dataProviderIndividual = new ListDataProvider<Individual>();
		dataProviderWorld = new ListDataProvider<World>();
	}
	
	/**
	 * Add a display to the database. The current range of interest of the
	 * display will be populated with data.
	 * 
	 * @param display
	 *            a {@Link HasData}.
	 */
	public void addDataDisplay(HasData<Universe> display) {
		dataProviderUniverse.addDataDisplay(display);
	}

	public void addDataDisplay(HasData<World> display, Universe u) {
		dataProviderWorld.addDataDisplay(display);
	}

	public void addDataDisplay(HasData<Individual> display, World w) {
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
	
	public Universe getSkiFiUniverse() {
		if (skifiUniverse == null) {
			skifiUniverse = createSkiFiUniverse();
		}
		return skifiUniverse;
	}

	private Universe createSkiFiUniverse() {
		Resources res = GWT.create(Resources.class);
		
		// #########################################################
		// setup some individuals
		// #########################################################
		Individual a1Spok = new Individual("Spok", res.spok().getURL());
		Individual a2Data = new Individual("CommanderData", res.commanderData().getURL());
		Individual a3Sokrates = new Individual("Sokrates", res.sokrates().getURL());
		Individual a4Sauron = new Individual("Sauron", res.sauron().getURL());
		Individual a5ChuckNorris = new Individual("ChuckNorris", res.chuckNorris().getURL());
		Individual a6Alice = new Individual("Alice", res.alice().getURL());
		Individual a7Jesus = new Individual("Jesus", res.jesus().getURL());
		Individual a8Jobs = new Individual("SteveJobs", res.jobs().getURL());
		Individual a9Eve = new Individual("Eve", res.eve().getURL());
		Individual a10GrinCat = new Individual("GrinCat", res.grinCat().getURL());
		
		// #########################################################
		// setup some constants
		// #########################################################
		Constant a1 = new Constant("a", 1);
		Constant a2 = new Constant("a", 2);
		Constant a3 = new Constant("a", 3);
		Constant a4 = new Constant("a", 4);
		Constant a5 = new Constant("a", 5);
		Constant a6 = new Constant("a", 6);
		Constant a7 = new Constant("a", 7);
		Constant a8 = new Constant("a", 8);
		Constant a9 = new Constant("a", 9);
		Constant a10 = new Constant("a", 10);
		
		HashMap<Constant, Individual> constantMapping = new HashMap<Constant, Individual>();
		constantMapping.put(a1, a1Spok);
		constantMapping.put(a2, a2Data);
		constantMapping.put(a3, a3Sokrates);
		constantMapping.put(a4, a4Sauron);
		constantMapping.put(a5, a5ChuckNorris);
		constantMapping.put(a6, a6Alice);
		constantMapping.put(a7, a7Jesus);
		constantMapping.put(a8, a8Jobs);
		constantMapping.put(a9, a9Eve);
		constantMapping.put(a10, a10GrinCat);
		

		
		// #########################################################
		// setup a Set of all existing Individuals
		// #########################################################

		HashSet<Individual> inventory = new HashSet<Individual>();
		inventory.add(a1Spok);
		inventory.add(a2Data);
		inventory.add(a3Sokrates);
		inventory.add(a4Sauron);
		inventory.add(a5ChuckNorris);
		inventory.add(a6Alice);
		inventory.add(a7Jesus);
		inventory.add(a8Jobs);
		inventory.add(a9Eve);
		inventory.add(a10GrinCat);
		
		// #########################################################
		// setup some predicates
		// #########################################################
		Predicate pHuman = new Predicate("HUMAN", 0, 1);
		Predicate pEvil = new Predicate("EVIL", 0, 1);
		Predicate pHasSwissBankAccount = new Predicate("HASSWISSBANKACCOUNT", 0, 1);
		Predicate pLikesApples = new Predicate("LIKESAPPLES", 0, 1);
		Predicate pVegetarian = new Predicate("VEGETARIAN", 0, 1);
		Predicate pDog = new Predicate("DOG", 0, 1);
		Predicate pStudiesInMinTime = new Predicate("STUDIESINMINTIME", 0, 1);
		Predicate pCanDance = new Predicate("CANDANCE", 0, 1);
		Predicate pConservative = new Predicate("CONSERVATIVE", 0, 1);
		Predicate pHasSmartphone = new Predicate("HASSMARTPHONE", 0, 1);

		Predicate pGod = new Predicate("GOD", 0, 1);
		
		Predicate p2Friends = new Predicate("FRIENDOF", 0, 2);
		Predicate p2OlderThan = new Predicate("OLDERTHAN", 0, 2);
		Predicate p2SmarterThan = new Predicate("SMARTERTHAN", 0, 2);

		// #########################################################
		// define the extension of each predicate in World KPAX
		// #########################################################

		HashSet<ArrayList<Individual>> pExtHuman = new HashSet<ArrayList<Individual>>(); // Nobody is human
		HashSet<ArrayList<Individual>> pExtEvil = new HashSet<ArrayList<Individual>>(); //Nobody is evil
		HashSet<ArrayList<Individual>> pExtHasSwissBankAccount = new HashSet<ArrayList<Individual>>(createArity1Extension(a5ChuckNorris)); //Chuck Norris has a swiss bank account
		HashSet<ArrayList<Individual>> pExtLikesApples = new HashSet<ArrayList<Individual>>(createArity1Extension(a9Eve));
		HashSet<ArrayList<Individual>> pExtVegetarian = new HashSet<ArrayList<Individual>>(createArity1Extension(inventory));
		HashSet<ArrayList<Individual>> pExtDog = new HashSet<ArrayList<Individual>>();
		HashSet<ArrayList<Individual>> pExtStudiesInMinTime = new HashSet<ArrayList<Individual>>(createArity1Extension(a5ChuckNorris)); //Only Chuck Norris studies in minimal study time
		HashSet<ArrayList<Individual>> pExtCanDance = new HashSet<ArrayList<Individual>>(createArity1Extension(a2Data,a6Alice,a9Eve,a10GrinCat));
		HashSet<ArrayList<Individual>> pExtConservative = new HashSet<ArrayList<Individual>>();
		HashSet<ArrayList<Individual>> pExtHasSmartphone = new HashSet<ArrayList<Individual>>(createArity1Extension(a8Jobs));
		HashSet<ArrayList<Individual>> pExtGod = new HashSet<ArrayList<Individual>>(); //There is no god
		HashSet<ArrayList<Individual>> p2ExtFriends = new HashSet<ArrayList<Individual>>(createArity2Extension(inventory)); 
		HashSet<ArrayList<Individual>> p2ExtOlderThan = new HashSet<ArrayList<Individual>>();
		HashSet<ArrayList<Individual>> p2ExtSmarterThan = new HashSet<ArrayList<Individual>>();		
		
		HashMap<Predicate, HashSet<ArrayList<Individual>>> extensions = new HashMap<Predicate, HashSet<ArrayList<Individual>>>();
		extensions.put(p2Friends, p2ExtFriends); 
		extensions.put(p2OlderThan, p2ExtOlderThan);
		extensions.put(p2SmarterThan, p2ExtSmarterThan);
		extensions.put(pCanDance, pExtCanDance);
		extensions.put(pConservative, pExtConservative);
		extensions.put(pDog, pExtDog);
		extensions.put(pEvil, pExtEvil);
		extensions.put(pGod, pExtGod);
		extensions.put(pHasSmartphone, pExtHasSmartphone);
		extensions.put(pHasSwissBankAccount, pExtHasSwissBankAccount);
		extensions.put(pHuman, pExtHuman);
		extensions.put(pLikesApples, pExtLikesApples);
		extensions.put(pStudiesInMinTime, pExtStudiesInMinTime);
		extensions.put(pVegetarian, pExtVegetarian);

		// ###############################
		//    WORLD K-PAX
		// ###############################
		
		World kPax = new World("kPax", extensions);
		
		// #########################################################
		// define the extension of each predicate in World aLeA
		// #########################################################

		pExtHuman = new HashSet<ArrayList<Individual>>(createArity1Extension(a1Spok,a2Data,a4Sauron,a10GrinCat)); // Fantasies are human
		pExtEvil = new HashSet<ArrayList<Individual>>(createArity1Extension(a4Sauron)); //And Sauron is Evil
		pExtHasSwissBankAccount = new HashSet<ArrayList<Individual>>(createArity1Extension(a6Alice));
		pExtLikesApples = new HashSet<ArrayList<Individual>>(createArity1Extension(a9Eve,a10GrinCat));
		pExtVegetarian = new HashSet<ArrayList<Individual>>(createArity1Extension(a1Spok,a2Data,a5ChuckNorris));
		pExtDog = new HashSet<ArrayList<Individual>>(createArity1Extension(a4Sauron));
		pExtStudiesInMinTime = new HashSet<ArrayList<Individual>>(createArity1Extension(a5ChuckNorris)); //Only Chuck Norris studies in minimal study time
		pExtCanDance = new HashSet<ArrayList<Individual>>(createArity1Extension(a7Jesus));
		pExtConservative = new HashSet<ArrayList<Individual>>();
		pExtHasSmartphone = new HashSet<ArrayList<Individual>>(createArity1Extension(a8Jobs));
		pExtGod = new HashSet<ArrayList<Individual>>(createArity1Extension(a9Eve)); //There is no god
		p2ExtFriends = new HashSet<ArrayList<Individual>>(createArity2Extension(a9Eve,a1Spok,a9Eve,a2Data,a9Eve,a3Sokrates,a9Eve,a4Sauron,a9Eve,a5ChuckNorris,a9Eve,a6Alice,a9Eve,a7Jesus,a9Eve,a8Jobs,a9Eve,a9Eve,a9Eve,a10GrinCat,a1Spok,a10GrinCat,a3Sokrates,a4Sauron,a4Sauron,a3Sokrates,a5ChuckNorris,a6Alice,a6Alice,a5ChuckNorris)); 
		p2ExtOlderThan = new HashSet<ArrayList<Individual>>(createArity2Extension(a10GrinCat,a1Spok,a10GrinCat,a2Data,a10GrinCat,a3Sokrates,a10GrinCat,a4Sauron,a10GrinCat,a5ChuckNorris,a10GrinCat,a6Alice,a10GrinCat,a7Jesus,a10GrinCat,a8Jobs,a10GrinCat,a9Eve));
		p2ExtSmarterThan = new HashSet<ArrayList<Individual>>(createArity2Extension(a10GrinCat,a1Spok,a10GrinCat,a2Data,a10GrinCat,a3Sokrates,a10GrinCat,a4Sauron,a10GrinCat,a5ChuckNorris,a10GrinCat,a6Alice,a10GrinCat,a7Jesus,a10GrinCat,a8Jobs,a10GrinCat,a9Eve,a2Data,a6Alice,a5ChuckNorris,a6Alice));		
		
		HashMap<Predicate, HashSet<ArrayList<Individual>>> extensionsAlea = new HashMap<Predicate, HashSet<ArrayList<Individual>>>();
		extensionsAlea.put(p2Friends, p2ExtFriends); 
		extensionsAlea.put(p2OlderThan, p2ExtOlderThan);
		extensionsAlea.put(p2SmarterThan, p2ExtSmarterThan);
		extensionsAlea.put(pCanDance, pExtCanDance);
		extensionsAlea.put(pConservative, pExtConservative);
		extensionsAlea.put(pDog, pExtDog);
		extensionsAlea.put(pEvil, pExtEvil);
		extensionsAlea.put(pGod, pExtGod);
		extensionsAlea.put(pHasSmartphone, pExtHasSmartphone);
		extensionsAlea.put(pHasSwissBankAccount, pExtHasSwissBankAccount);
		extensionsAlea.put(pHuman, pExtHuman);
		extensionsAlea.put(pLikesApples, pExtLikesApples);
		extensionsAlea.put(pStudiesInMinTime, pExtStudiesInMinTime);
		extensionsAlea.put(pVegetarian, pExtVegetarian);

		// ###############################
		//    WORLD ALEA
		// ###############################
		
		World alea = new World("aLeA", extensionsAlea);		
		
		// #########################################################
		// define the extension of each predicate in World EARTH
		// #########################################################

		pExtHuman = new HashSet<ArrayList<Individual>>(createArity1Extension(a3Sokrates,a5ChuckNorris,a7Jesus,a8Jobs));
		pExtEvil = new HashSet<ArrayList<Individual>>(createArity1Extension(a8Jobs)); 
		pExtHasSwissBankAccount = new HashSet<ArrayList<Individual>>(createArity1Extension(a8Jobs));
		pExtLikesApples = new HashSet<ArrayList<Individual>>(createArity1Extension(a9Eve));
		pExtVegetarian = new HashSet<ArrayList<Individual>>(createArity1Extension(a8Jobs,a7Jesus));
		pExtDog = new HashSet<ArrayList<Individual>>(createArity1Extension(a1Spok,a2Data,a4Sauron,a6Alice));
		pExtStudiesInMinTime = new HashSet<ArrayList<Individual>>(createArity1Extension(a5ChuckNorris)); //Only Chuck Norris studies in minimal study time
		pExtCanDance = new HashSet<ArrayList<Individual>>(createArity1Extension(a3Sokrates,a5ChuckNorris,a7Jesus,a8Jobs)); //All Humans can dance
		pExtConservative = new HashSet<ArrayList<Individual>>(createArity1Extension(a8Jobs));
		pExtHasSmartphone = new HashSet<ArrayList<Individual>>(createArity1Extension(a8Jobs));
		pExtGod = new HashSet<ArrayList<Individual>>(createArity1Extension(a7Jesus)); //There is no god
		p2ExtFriends = new HashSet<ArrayList<Individual>>(createArity2Extension(a9Eve,a1Spok,a9Eve,a2Data,a9Eve,a3Sokrates,a9Eve,a4Sauron,a9Eve,a5ChuckNorris,a9Eve,a6Alice,a9Eve,a7Jesus,a9Eve,a8Jobs,a9Eve,a9Eve,a9Eve,a10GrinCat,a1Spok,a10GrinCat,a3Sokrates,a4Sauron,a4Sauron,a3Sokrates,a5ChuckNorris,a6Alice,a6Alice,a5ChuckNorris)); 
		p2ExtOlderThan = new HashSet<ArrayList<Individual>>(createArity2Extension(a3Sokrates,a1Spok,a3Sokrates,a2Data,a3Sokrates,a3Sokrates,a3Sokrates,a4Sauron,a3Sokrates,a5ChuckNorris,a3Sokrates,a6Alice,a3Sokrates,a7Jesus,a3Sokrates,a8Jobs,a3Sokrates,a9Eve,a3Sokrates,a10GrinCat));
		p2ExtSmarterThan = new HashSet<ArrayList<Individual>>(createArity2Extension(a3Sokrates,a1Spok,a3Sokrates,a2Data,a3Sokrates,a3Sokrates,a3Sokrates,a4Sauron,a3Sokrates,a5ChuckNorris,a3Sokrates,a6Alice,a3Sokrates,a7Jesus,a3Sokrates,a8Jobs,a3Sokrates,a9Eve,a3Sokrates,a10GrinCat));		
		
		HashMap<Predicate, HashSet<ArrayList<Individual>>> extensionsEarth = new HashMap<Predicate, HashSet<ArrayList<Individual>>>();
		extensionsEarth.put(p2Friends, p2ExtFriends); 
		extensionsEarth.put(p2OlderThan, p2ExtOlderThan);
		extensionsEarth.put(p2SmarterThan, p2ExtSmarterThan);
		extensionsEarth.put(pCanDance, pExtCanDance);
		extensionsEarth.put(pConservative, pExtConservative);
		extensionsEarth.put(pDog, pExtDog);
		extensionsEarth.put(pEvil, pExtEvil);
		extensionsEarth.put(pGod, pExtGod);
		extensionsEarth.put(pHasSmartphone, pExtHasSmartphone);
		extensionsEarth.put(pHasSwissBankAccount, pExtHasSwissBankAccount);
		extensionsEarth.put(pHuman, pExtHuman);
		extensionsEarth.put(pLikesApples, pExtLikesApples);
		extensionsEarth.put(pStudiesInMinTime, pExtStudiesInMinTime);
		extensionsEarth.put(pVegetarian, pExtVegetarian);

		// ###############################
		//    WORLD ALEA
		// ###############################
		
		World earth = new World("Earth", extensionsEarth);				
	
		
		
		
		// #############################################
		//     UNIVERSE STARGATE
		// #############################################
		Universe skiFi = null;
		try {
			skiFi = new Universe("Stargate", AccessabilityConstraint.S5, true, constantMapping, kPax,alea,earth);
		} catch (ConstraintViolationException e) {
		}
		return skiFi;
	}

	/**
	 * 
	 * @param setOfIndividuals: one individual is one extension element.
	 * @return a ready extension for Predicates with Arity 1
	 */
	private HashSet<ArrayList<Individual>> createArity1Extension(
			HashSet<Individual> setOfIndividuals) {
		return createArity1Extension(setOfIndividuals.toArray(new Individual[setOfIndividuals.size()]));
	}
	
	private HashSet<ArrayList<Individual>> createArity1Extension(Individual ... individuals) {
		HashSet<ArrayList<Individual>> extension = new HashSet<ArrayList<Individual>>();
		for (Individual i : individuals) {
			ArrayList<Individual> extensionElement = new ArrayList<Individual>();
			extensionElement.add(i);
			extension.add(extensionElement);
		}
		return extension;
	}
	
	/**
	 * 
	 * @param setOfIndividuals: each element of the set is created as inside exactly one extension element with each other element
	 * @return a ready extension for Predicates with Arity 2 where each of the individuals are in connection with each other (pairwise)
	 */
	private HashSet<ArrayList<Individual>> createArity2Extension(
			HashSet<Individual> setOfIndividuals) {
		HashSet<ArrayList<Individual>> extension = new HashSet<ArrayList<Individual>>();
		for(Individual left: setOfIndividuals) {
			for(Individual right: setOfIndividuals) {
				ArrayList<Individual> extAr2 = new ArrayList<Individual>();
				extAr2.add(left);
				extAr2.add(right);
				extension.add(extAr2);
			}
		}
		return extension;
	}
	
	/**
	 * 
	 * @param individuals: first individual gets connected with the second, third with the forth, ....
	 * @return a ready Artiy2-Extension
	 */
	private HashSet<ArrayList<Individual>> createArity2Extension(Individual ... individuals) {
		HashSet<ArrayList<Individual>> extension = new HashSet<ArrayList<Individual>>();
		//if the number of individuals is even
		if ((individuals.length % 2) == 0) {
			for(int i=0; i<individuals.length; i=i+2) {
				ArrayList<Individual> extAr2 = new ArrayList<Individual>();
				Individual left = individuals[i];
				Individual right = individuals[i+1];
				extAr2.add(left);
				extAr2.add(right);
			}
		}
		return extension;
	}

	@SuppressWarnings("unchecked")
	private Universe createPizzaUniverse() {
		// setup some icons (null for now)
		Image ico1 = new Image("http://www.buehne-pepperoni.de/skin/images/pepperoni_logo.gif");
		Image ico2 = new Image("http://www.grundschulmaterial.de/inhalte/de/Klasse14/Deutsch/Klasse%201/Anlautbilder/K-P/PRV-Mais-98384.jpg");
		Image ico3 = new Image("http://www.grundschulmaterial.de/inhalte/de/Klasse14/Deutsch/Klasse%201/Anlautbilder/K-P/PRV-Mais-98384.jpg");

		// #########################################################
		// setup some individuals
		// #########################################################

		Individual a1Pepperoni = new Individual("Pepperoni", ico1.getUrl());
		Individual a2Champignon = new Individual("Champignon", ico1.getUrl());
		Individual a3Tomatoe = new Individual("Tomatoe", ico2.getUrl());
		Individual a4Capres = new Individual("Capres", ico3.getUrl());
		Individual a5Mais = new Individual("Mais", ico3.getUrl());
		Individual a6Salami = new Individual("Salami", ico1.getUrl());

		// #########################################################
		// setup some constants
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
		// setup some predicates
		// #########################################################
		Predicate p1Spicy = new Predicate("SPICY", 0, 1);
		Predicate p2PizzaIngredient = new Predicate("PIZZAINGREDIENT", 0, 1);
		Predicate p3Round = new Predicate("ROUND", 0, 1);
		Predicate p4Red = new Predicate("RED", 0, 1);
		Predicate p5Meat = new Predicate("MEAT", 0, 1);
		Predicate p6Vegetable = new Predicate("VEGETABLE", 0, 1);
		Predicate p7Blue = new Predicate("BLUE", 0, 1);
		Predicate p8Cubic = new Predicate("CUBIC", 0, 1);
		Predicate p9Yellow = new Predicate("YELLOW", 0, 1);
		Predicate p10SmallerThan = new Predicate("SMALLERTHAN", 0, 2);

		// #########################################################
		// setup a Set of all existing Individuals in my world
		// #########################################################

		HashSet<Individual> inventory = new HashSet<Individual>();
		inventory.add(a1Pepperoni);
		inventory.add(a2Champignon);
		inventory.add(a3Tomatoe);
		inventory.add(a4Capres);
		inventory.add(a5Mais);
		inventory.add(a6Salami); // There is no Salami in my World

		// #########################################################
		// init the extension of each predicate in this World
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
		// extension-setup
		// #########################################################

		ArrayList<Individual> dummy = new ArrayList<Individual>();
		dummy.add(a1Pepperoni);
		p1SpicyExt.add(dummy); // Pepperoni is spicy

		for (Individual i : inventory) {
			dummy = new ArrayList<Individual>();
			dummy.add(i);
			p2PizzaIngredientExt.add(dummy); // every existing stuff here is a
												// pizzaincredient.
												// Salami does not exist and
												// here it is also no
												// pizzaincredient.
		}

		dummy = new ArrayList<Individual>();
		dummy.add(a3Tomatoe);
		p3RoundExt.add(dummy); // Tomatoe capres and Mais (and Salami) are
								// round.

		dummy = new ArrayList<Individual>();
		dummy.add(a4Capres); // Tomatoe capres and Mais (and Salami) are round.
		p3RoundExt.add(dummy);

		dummy = new ArrayList<Individual>();
		dummy.add(a5Mais); // Tomatoe capres and Mais (and Salami) are round.
		p3RoundExt.add(dummy);

		dummy = new ArrayList<Individual>();
		dummy.add(a6Salami);
		p3RoundExt.add(dummy); // a6 does not exist in this world, but is in the
								// extension of p3. That's not allowed, so it
								// should throw an exception later.

		dummy = new ArrayList<Individual>();
		dummy.add(a3Tomatoe);
		p4RedExt.add(dummy); // Tomate is red

		dummy = new ArrayList<Individual>();
		dummy.add(a6Salami);
		p4RedExt.add(dummy); // Tomatoe(a3) and Salami(a6) are Red, even if a6
								// does not exist. We talk about non-"existing"
								// things. That's not allowed, so it should
								// throw an exception later.

		dummy = new ArrayList<Individual>();
		dummy.add(a6Salami);
		p5MeatExt.add(dummy); // Salami(a6) is Meat, even if a6 does not exist.
								// That's not allowed, so it should throw an
								// exception later.

		// Every existing thing (a1-a5) is a vegetable.

		dummy = new ArrayList<Individual>();
		dummy.add(a1Pepperoni);
		p6VegetableExt.add(dummy);

		dummy = new ArrayList<Individual>();
		dummy.add(a2Champignon);
		p6VegetableExt.add(dummy); // We could also write: p6.addAll(inventory);

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
		p9YellowExt.add(dummy); // Mais is yellow

		dummy = new ArrayList<Individual>();
		dummy.add(a5Mais);
		dummy.add(a3Tomatoe);
		p10SmallerThanExt.add(dummy); // Mais is smaller than tomatoe.

		// #########################################################
		// Now map predicates and extensions together
		// #########################################################
		HashMap<Predicate, HashSet<ArrayList<Individual>>> extensions = new HashMap<Predicate, HashSet<ArrayList<Individual>>>();
		extensions.put(p1Spicy, p1SpicyExt); // predicate p1 got extension p1ext
		extensions.put(p2PizzaIngredient, p2PizzaIngredientExt); // vice versa
																	// mutatis
																	// mutandis
		extensions.put(p3Round, p3RoundExt);
		extensions.put(p4Red, p4RedExt);
		extensions.put(p5Meat, p5MeatExt);
		extensions.put(p6Vegetable, p6VegetableExt);
		extensions.put(p7Blue, p7BlueExt);
		extensions.put(p8Cubic, p8CubicExt);
		extensions.put(p9Yellow, p9YellowExt);
		extensions.put(p10SmallerThan, p10SmallerThanExt);

		// initialize a world with the declared extensions and give it a name
		Universe u = new Universe("PizzaUniverse", constantMapping);
		World w = new World("Pizza", extensions);
		World w2 = new World("Pizza2", extensions);
		World w3 = new World("Pizza3", extensions);

		w2.overwriteExtension(p7Blue, p4RedExt); // Salami and Tomatoe are blue
													// in this world
		w2.overwriteExtension(p4Red, p7BlueExt); // Nothing is Red in this world

		p10SmallerThanExt = new HashSet<ArrayList<Individual>>();
		for (Individual i : inventory) {
			// if (!i.equals(a5Mais)) {
			dummy = new ArrayList<Individual>();
			dummy.add(a5Mais);
			dummy.add(i);
			p10SmallerThanExt.add(dummy); // Mais is smaller than Everything.
			// }
		}
		w3.overwriteExtension(p10SmallerThan, p10SmallerThanExt);

		try {
			u = new Universe("PizzaUniverse", AccessabilityConstraint.S5, true,
					constantMapping, w, w2, w3);
		} catch (ConstraintViolationException e) {
			throw new IllegalArgumentException("ConstraintViolationException "
					+ e.getMessage());
		}

		return u;
	}

	// TODO! Namecheck
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
		return value.matches("[a-zA-Z]+");
	}

}
