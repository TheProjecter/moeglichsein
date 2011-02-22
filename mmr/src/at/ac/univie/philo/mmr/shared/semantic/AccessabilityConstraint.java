package at.ac.univie.philo.mmr.shared.semantic;

public enum AccessabilityConstraint implements Comparable<AccessabilityConstraint>{
	NONE(0), REFLEXIVE(1), S5(2);

	 private int strength;

	 private AccessabilityConstraint(int c) {
	   strength = c;
	 }
}
