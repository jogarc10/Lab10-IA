package main;
import java.io.FileNotFoundException;

import Algorithms.BruteForceAlgorithm;
import Algorithms.GeneticAlgorithm;
import Algorithms.RandomAlgorithm;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		
		Problema p = new Problema("case001.txt");
		Circulo bruteCircle, randomCircle, geneticCircle;
		int populationSize = 10;

		GeneticAlgorithm genetic = new GeneticAlgorithm(p);
		genetic.printPopulation();
		
		System.out.println("------------------------");
		
		geneticCircle = genetic.BestSolution(p);		
		genetic.printPopulation();
		
		System.out.println("");
		System.out.println("Best circle: " + geneticCircle.toString());
		
		
	}

}
