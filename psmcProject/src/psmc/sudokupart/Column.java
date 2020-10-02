package psmc.sudokupart;

import psmc.MainClassSequential;

/**
 * Rappresenta una colonna, contenente i blocchi che ne fanno parte
 * e i possibili valori legali in essa.
 *
 * @author  Mattia Paolacci
 */
public class Column {

	/**
	 * Array di booleani, in cui se true : il valore è illegale, false : legale
	 */
	private boolean[] illegal = new boolean[10]; 
	
	/**
	 * Array corrispondente ad una colonna. L'indice nell'array corrisponde alla riga del blocco.
	 */
	private Block[] blocksCol = new Block[9];
	
	/**
	 * @param y indice di riga del blocco.
	 * @return {@link Block}
	 */
	public Block getBlock(int y){ return blocksCol[y]; } 
	
	/**
	 * Aggiunge un blocco alla colonna.
	 * @param b {@link Block}
	 */
	protected void addBlock(Block b){
		b.setColumn(this);
		blocksCol[b.getY()] = b;
	}
	
	/**
	 * Verifica che il valore val dato in input sia legale.
	 * @param val valore da verificare.
	 * @return {@link Boolean} True se il valore è legale, False altrimenti.
	 */
	protected boolean isLegal(int val){ return !illegal[val]; }
	
	/**
	 * Segnala il valore val legale.
	 * @param val : {@link Integer}
	 */
	protected void setLegal(int val){ if(illegal[val]) illegal[val] = false; }
	
	/**
	 * Segnala il valore val illegale alla colonna.
	 * @param val {@link Integer}
	 */
	protected void setIllegal(int val){ 
		if(!illegal[val]) illegal[val] = true;
		
	}
	
	/**
	 * Segnala il valore dato in input illegale, in tutti i blocchi appartenenti alla colonna.
	 * @param val Valore illegale
	 */
	protected void setIllegalToAllBlockInColumn(int val){
		for(int i = 0; i<9; i++){
			if(!blocksCol[i].isEmpty()) continue;
			blocksCol[i].declareIllegal(val);
		}
	}
	
	/**
	 *  Cerca quali sono i valori illegali nella colonna.
	 */
	protected void findIllegalValue(){
		for(int y = 0; y<9; y++){
			int val  = getBlock(y).getVal();
			if(val != 0) setIllegal(val);
		}
	}
}

