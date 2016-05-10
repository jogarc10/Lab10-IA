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
		
		BruteForceAlgorithm b = new BruteForceAlgorithm();
		bruteCircle = b.BestSolution(p);
		
		RandomAlgorithm r = new RandomAlgorithm();
		randomCircle = r.BestSolution(p);
		
		GeneticAlgorithm genetic = new GeneticAlgorithm(populationSize);
		geneticCircle = genetic.BestSolution(p);
		
	}

}