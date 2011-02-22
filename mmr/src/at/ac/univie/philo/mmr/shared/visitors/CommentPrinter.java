package at.ac.univie.philo.mmr.shared.visitors;

import java.util.ArrayList;
import java.util.HashSet;

import at.ac.univie.philo.mmr.shared.Constants;
import at.ac.univie.philo.mmr.shared.semantic.Individual;

public class CommentPrinter {

	public static void print(String string) {
		if (Constants.SHOWEVALSTEPS) {
			System.out.println(string);
		}
	}

	public static String printExtension(HashSet<ArrayList<Individual>> ext) {
		if (ext == null) return "";
		
		StringBuffer result = new StringBuffer();
		int count = 0;
		for(ArrayList<Individual> ai : ext) {
			if (count != 0) {
				result.append(",\n");
			}
			result.append("\t(");
			int countPerIndividuum = 0;
			for(Individual i : ai) {
				if (countPerIndividuum != 0) {
					result.append(",");
				}
				result.append(i.getName());
				countPerIndividuum++;
			}
			result.append(")");
			count++;
		}
		return result.toString();
	}
}
