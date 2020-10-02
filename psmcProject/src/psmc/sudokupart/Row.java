package psmc.sudokupart;

/**
 * Rappresenta una riga di un'istanza di sudoku
 *  e i valori legali che può contenere
 *
 * @author  Mattia Paolacci
 */
public class Row {
	
	/**
	 * Array di booleani, in cui se true : il valore è illegale, false : legale
	 */
	private boolean[] illegal = new boolean[10];
	
	/**
	 * Array corrispondente ad una riga. L'indice corrisponde al numero di colonna.
	 */
	private Block[] blocksRow = new Block[9];
	
	/**
	 * Segnala il valore val illegale nella riga.
	 * @param val {@link Integer}
	 */
	protected void setIllegal(int val){ 
		if(!illegal[val]) illegal[val] = true; 
		
	}
	
	/**
	 * Imposta il valore 'val' illegale in tutti i blocci presenti nella riga. 
	 * @param val Valore illegale.
	 */
	protected void setIllegalToAllBlockInRow(int val){
		for(int i=0; i<9; i++){
			if(!blocksRow[i].isEmpty()) continue;
			blocksRow[i].declareIllegal(val);
		}
	}
	
	/**
	 * Segnala il valore val legale.
	 * @param val : {@link Integer}
	 */
	protected void setLegal(int val){ if(illegal[val]) illegal[val] = false; }
	
	/**
	 * Verifica se il valore dato sia legale.
	 */
	protected boolean isLegal(int val){ return !illegal[val]; }
	
	/**
	 * Ritorna un blocco.
	 * @param x indice di colonna.
	 * @return {@link Block}
	 */
	public Block getBlock(int x){ return blocksRow[x]; }
	
	/**
	 * Aggiunge un blocco alla colonna.
	 * @param b {@link Block}
	 */
	protected void addBlock(Block b){ 
		b.setRow(this);
		blocksRow[b.getX()] = b;
	}
	
	/**
	 * Trova i valori illegali nella riga. E imposta il flag a true relativo a quel valore in illegal
	 */
	protected void findIllegalValue(){
		for(int x=0; x<9; x++){
			int val = getBlock(x).getVal();
			if(val != 0) setIllegal(val);
		}
	}

}
