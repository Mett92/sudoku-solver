package psmc;

import java.util.Iterator;
import java.util.concurrent.RecursiveTask;

import psmc.sudokupart.Block;
import psmc.sudokupart.SudokuInstance;


/**
 * Contiene l'algoritmo parallelo per la ricerca del numero di soluzioni legali
 * di un'istanza di sudoku.
 *
 * @author  Mattia Paolacci
 */
public class SudokuSolver extends RecursiveTask<Integer>{

	private SudokuInstance s;
	private SudokuSolver[] ans;
	
	SudokuSolver(SudokuInstance s) { this.s = s; }
	
	@Override
	protected Integer compute(){
		if(s.isComplete()) return 1;
		
		// Prende il primo blocco libero nel sudoku
		Block b = s.getFirstBlockEmpty();
		int lDim = b.getDimSetLegalSol();
		ans = new SudokuSolver[lDim];
		
		Iterator<Byte> ite = b.getIteratorSetLegal();
		int i = 0;
		while(ite.hasNext()){
			int v = ite.next();
			SudokuInstance sCopy = ite.hasNext() ? s.clona() : s;
			sCopy.getBlock(b.getX(), b.getY()).setVal(v);
			if(isLegalInstance(sCopy)){
				ans[i] = new SudokuSolver(sCopy);
				i++;
			}
		}
		if(ans[0] == null) return 0; 
		
		i--;
		while(i>0){
			ans[i].fork();
			i--;
		}
		int result = ans[0].compute();
		ans[0] = null;
		i++;
		while(i<lDim && ans[i]!= null){
			result += ans[i].join();
			ans[i] = null;
			i++;
		}
		return result;
		
	}
	
	/**
	 * La legalità viene verificata controllando se ci sia un blocco con 0 soluzioni legali.
	 * 
	 * @param s Istanza di {@link SudokuInstance}, della quale bisogna verificare se sia legale 
	 * @return {@link Boolean}, True se legale, false altrimenti.
	 */
	protected boolean isLegalInstance(SudokuInstance s){
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
