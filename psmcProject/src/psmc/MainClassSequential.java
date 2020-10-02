package psmc;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import psmc.sudokupart.Block;
import psmc.sudokupart.SudokuInstance;

/**
 * Classe da chiamare per l'esecuzione sequenziale.
 *
 * @author  Mattia Paolacci
 */
public class MainClassSequential {
	
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
		int res = recursiveResolutor(s);
		
		System.out.println("Dimensione spazio soluzioni: "+spaceSol);
		System.out.println("Fattore di riempimento: "+perc+"%");
		System.out.println("Soluzioni Legali: "+ res);
	}
	
	/**
	 * Algoritmo sequenziale per la ricerca del numero di soluzioni legali di un
	 * istanza di sudoku.
	 * 
	 * @param s Istanza di {@link SudokuInstance}
	 * @return Il numero di soluzioni legali del sudoku dato in input.
	 */
	public static Integer recursiveResolutor(SudokuInstance s){
		int result = 0;
		return recursiveResolutor(s, result);
	}
	
	private static Integer recursiveResolutor(SudokuInstance s, int result){
		if(s.isComplete()) {
			return result + 1;
		}
		Block b = s.getFirstBlockEmpty();
		Iterator<Byte> ite = b.getIteratorSetLegal();
		while(ite.hasNext()){
			int v = ite.next();
			SudokuInstance sCopy = ite.hasNext() ? s.clona() : s;
			sCopy.getBlock(b.getX(), b.getY()).setVal(v);
			if(isLegalInstance(sCopy)) 
				result += recursiveResolutor(sCopy);
		}
		return result;
	}

	/**
	 * La legalità viene verificata controllando se ci sia un blocco con 0 soluzioni legali.
	 * 
	 * @param s Istanza di {@link SudokuInstance}, della quale bisogna verificare se sia legale 
	 * @return {@link Boolean}, True se legale, false altrimenti.
	 */
	protected static boolean isLegalInstance(SudokuInstance s){
		for(int i = 0; i<9; i++){
			for(int j = 0; j<9; j++){
				if(s.getBlock(j, i).isEmpty())
					if(s.getBlock(j, i).getDimSetLegalSol() == 0)
						return false;
			}
		}
		return true;
	}
}

