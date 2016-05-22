package Algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Vector;

import main.Circulo;
import main.Individuo;
import main.Problema;
import main.Util;

public class GeneticAlgorithm extends Algorithms {

	private static final int ELITE_SIZE = 4;
	private static final int GENES_NUMBER = 3;  // Total number of genes
	private static final int GEN_DIMENSION = 10; // Dimension for each gene
	private static final int MAX_GENERATIONS = 100;
	private static final int POPULATION_SIZE = 10; // Initial population size
	private PriorityQueue<Individuo> population = new PriorityQueue<Individuo>();
	private float[] populationProbability = new float[POPULATION_SIZE];
	public static Problema problem;
	private float totalFitness;
	private Object[] individualsArray;

	public GeneticAlgorithm(Problema p) {
		GeneticAlgorithm.problem = p;
		this.population = generateInitialPopulation();
	}

	/*
	 * Generating a random population of size N
	 * Returns a priority queue of N individuals
	 */
	private PriorityQueue<Individuo> generateInitialPopulation() {
		PriorityQueue<Individuo> new_population = new PriorityQueue<Individuo>();
		int dimension = (int) Math.pow(2, GEN_DIMENSION); // maximum radio

		for (int i = 0; i < POPULATION_SIZE; i++) {
			Circulo randomCircle = Circulo.random(dimension);
			Individuo newIndividual = new Individuo(randomCircle);
			newIndividual.setFitness(generateFitness(randomCircle));
			new_population.add(newIndividual);
		}
		
		// TODO: Update probability.
		updateProbabilities();
				
		return new_population;
	}

	@Override
	public Circulo BestSolution(Problema p) {
		Circulo bestCircle = new Circulo(0, 0, 0);
		Circulo generationBestCircle = new Circulo(0, 0, 0);
		Individuo generationBestIndividual;

		for (int i = 0; i < MAX_GENERATIONS; i++) {
			
			System.out.println("GENERATING " + i + " POPULATION");
			
			generateAndUpdateNewPopulation(); // Assign new population after generated it
			
			generationBestIndividual = this.population.peek();
			generationBestCircle = generationBestIndividual.toCirculo();
			
			if (generationBestCircle.getRadio() > bestCircle.getRadio()) {
				bestCircle = generationBestCircle;
			}
			
			System.out.println("-----------");
		}

		return bestCircle;
	}
	
	public void generateAndUpdateNewPopulation() {
		PriorityQueue<Individuo> newPopulation = new PriorityQueue<Individuo>();
		Pair newPair;
		
		this.totalFitness = sumAllFitness();
		updateProbabilities(); // TODO

		// Merge population
		while (newPopulation.size() < POPULATION_SIZE - ELITE_SIZE) {
			
			newPair = routlettePickCouple();
			
			newPair.cross();
			newPair.mutate();
			newPair.calculateFitness();
			newPopulation.addAll(newPair.toList());
		}

		// Get elite from old population and add it to new population
		Individuo topEliteElement;
		
		System.out.println("Elite");
		for (int i = 0; i < ELITE_SIZE; i++) {
			if (population.size() > 0) {
				topEliteElement = population.poll();
				System.out.println(topEliteElement);
				newPopulation.add(topEliteElement);
			}
		}

		this.population = newPopulation;
	}
	
	/*
	 *  Sum all the individuals fitness score
	 */
	public float sumAllFitness() {
		float total = 0;

		for (Individuo e : population) {
			total += e.getFitness();
		}

		return total;
	}
	
	
	/*
	 * Iterate through the Priority queue and
	 * Calculate Probability for each individual to be used: pi = fi / fTotal
	 */
	public void updateProbabilities() {
		float tmpAccumProbability = 0;
		Individuo tmpIndividual;
		
		this.individualsArray = this.population.toArray();

		// Calculate probabilities
		for (int i = 0; i < this.individualsArray.length; i++) {
			
			tmpIndividual = (Individuo) this.individualsArray[i];
			tmpAccumProbability += tmpIndividual.getFitness() / this.totalFitness;
			this.populationProbability[i] = tmpAccumProbability;
		}
	}
	
	/*
	 * Returns 2 Individuals taking into account the probabilities
	 */
	public Pair routlettePickCouple() {
		
		int index;
		float randomProbability; // [0, 1)
		Individuo[] pickedCouple = new Individuo[2];
		
//		System.out.println("Picking elements...");
		
		for (int pairIndex = 0; pairIndex < 2; pairIndex++) {
			randomProbability = Util.random();
			index = 0;
			
			while (index < POPULATION_SIZE && populationProbability[index] < randomProbability) {
				index++;
			}
			
//			if (index == POPULATION_SIZE) {
//				index--;
//			}
//			
//			System.out.println("PairIndex" + pairIndex);
//			System.out.println("Index" + index);

			pickedCouple[pairIndex] = (Individuo) this.individualsArray[index];
		}
		
		return new Pair(new Individuo(pickedCouple[0]), new Individuo(pickedCouple[1]));
	}

	private float generateFitness(Circulo c) {
		if (this.problem.esSolucion(c)) {
			return (float) c.getRadio();
		} else {
			return 0;
		}
	}

	public void printPopulation() {
		String individual = "";

		for (Individuo e : population) {
			individual = "Cromosoma: ";
			individual += e.getCromosoma();
			individual += " | Fitness: ";
			individual += e.getFitness();
			System.out.println(individual);
		}
	}

}
