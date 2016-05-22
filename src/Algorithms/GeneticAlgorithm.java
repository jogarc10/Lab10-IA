package Algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.PriorityQueue;

import main.Circulo;
import main.Individuo;
import main.Problema;
import main.Util;

public class GeneticAlgorithm extends Algorithms {

	private static final int ELITE_SIZE = 4;
	private static final int GENES_NUMBER = 3;  // Total number of genes
	private static final int GEN_DIMENSION = 10; // Dimension for each gene
	private static final int MAX_GENERATIONS = 5;
	private static final int POPULATION_SIZE = 10; // Initial population size
	private PriorityQueue<Individuo> population = new PriorityQueue<Individuo>();
	private float[] populationProbability = new float[POPULATION_SIZE];
	private Problema problem;
	private float totalFitness;

	public GeneticAlgorithm(Problema p) {
		this.problem = p;
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
			// TODO: Update probability.
		}

		return new_population;
	}

	@Override
	public Circulo BestSolution(Problema p) {
		Circulo bestCircle = new Circulo(0, 0, 0);
		Individuo bestGenerationIndividual;

		for (int i = 0; i < MAX_GENERATIONS; i++) {
			generateAndUpdateNewPopulation(); // Assign new population after generated it
			// TODO: Cada nueva generacion coger el mejor circulo
			// Si el circulo es mejor que el encontrado hasta la fecha, actualizarlo.
			bestGenerationIndividual = this.population.peek();
		}

		return bestCircle;
	}

	public void generateAndUpdateNewPopulation() {
		int createdChromosomes = 0;
		PriorityQueue<Individuo> newPopulation = new PriorityQueue<Individuo>();
		float[] accumProbability = new float[POPULATION_SIZE];
		float totalFitness;
		Pair newPair;
		
		this.totalFitness = sumAllFitness();
		updatePopulationProbability(); // TODO
		

		// Merge population
		while (newPopulation.size() < POPULATION_SIZE - ELITE_SIZE) {

			newPair = pickCouples(); // returns list of size 2 chromosomes to work with them

//			newPair.cross();
//			newPair.mutate();
//			newPair.calculateFitness();
//			newPopulation.addAll(newPair.toList());
		}

		// Get elite from old population and add it to new population
		Individuo topEliteElement;
		for (int i = 0; i < ELITE_SIZE; i++) {
			if (population.size() > 0) {
				topEliteElement = population.poll();
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
	 * Calculate Probability for each individual to be used: pi = fi / fTotal
	 */
	
	public void updatePopulationProbability() {
		// http://stackoverflow.com/questions/8129122/how-to-iterate-over-a-priorityqueue
		// float[] newProbability = Arrays.sort(population.toArray());
		for (Individuo e: this.population) {
			System.out.println(e);
			System.out.println(e.getCromosoma());
			System.out.println(e.getFitness());
			System.out.println("----");
			// fitness / this.totalFitness;
		}
	}

	private Pair pickCouples() {
		// this.population
		return null;
	}

	/*
	public float[] calculateAccumProbability(float totalFitness) {
		float accumProbability[] = new float[POPULATION_SIZE];
		float tmpAccumProbability;
		float fitness;

		tmpAccumProbability = 0;

		for (int j = 0; j < POPULATION_SIZE; j++) {
			fitness = this.population.get(j).getFitness();
			tmpAccumProbability += fitness / totalFitness;
			accumProbability[j] = tmpAccumProbability;
		}

		return accumProbability;
	}
	*/

	/*
	 * Roulette: Select a random individual that fills the condition:
	 */
	public int selectIndividualIndex(float accumProbability[]) {
		int index = 0;
		boolean found = false;
		float randomProbability = Util.random(); // [0, 1)

		while (index < POPULATION_SIZE && !found) {
			if (accumProbability[index] < randomProbability) {
				index++;
			}
			else {
				found = true;
			}
		}

		return index;
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
