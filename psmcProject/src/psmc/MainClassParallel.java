package psmc;

import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

import psmc.sudokupart.SudokuInstance;

/**
 * Classe da chiamare per l'esecuzione parallela.
 *
 * @author  Mattia Paolacci
 */
public class MainClassParallel {
	
	public static void main(String args[]) throws IOException
	{		
		
		FileReader f = new FileReader(args[0]);
		SudokuInstance s = new SudokuInstance(f);

		// Inizializza la matrice con i possibili valori legali per ogni blocco.
		s.findLegalValue();
		
		// Dimensione dello spazio delle soluzioni.
		String spaceSol = s.solutionSpace();
		
		// fattore di riempimento matrice iniziale
		int perc = s.fillRate(); 
		
		// cerco se ci sono soluzioni immediate. Ossia blocche con un'unica soluzione lagale.
		s.findBlockWithOneLegalValue();
		
		// Start algoritmo
		int res = sudokuSolve(s);
		
		System.out.println("Dimensione spazio soluzioni: "+spaceSol);
		System.out.println("Fattore di riempimento: "+perc+"%");
		System.out.println("Soluzioni Legali: "+ res);
		
	}
	
	public static int sudokuSolve(SudokuInstance s){
		//Forkjoinpool per passare la funzione
		return ForkJoinPool.commonPool().invoke(new SudokuSolver(s));
		
	}
}
