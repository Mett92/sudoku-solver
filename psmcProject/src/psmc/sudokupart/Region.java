package psmc.sudokupart;


/**
 * Questa classe rappresenta una regione 3x3 di una matrice di sudoku.
 * 
 * @author  Mattia Paolacci
 */
public class Region {
	
	/**
	 * Array di booleani, che rappresenta i valori legali nella regione.
	 * False: se legale
	 * True: illegale 
	 */
	private boolean[] illegal = new boolean[10];
	
	/**
	 * Matrice di oggetti di tipo {@link Block}
	 */
	protected Block[][] blockMatrix = new Block[3][3];
	
	/**
	 * numero di blocchi non vuoti dentro la regione.
	 */
	public Byte blocksFull = 0;
	
	/** 
	 * @param x {@link Byte} coordinata x.
	 * @param y {@link Byte} coordinata y.
	 * @return {@link Block} Il blocco con le coordinate in input.
	 */
	public Block getBlock(int x, int y){ return blockMatrix[y%3][x%3]; }
	
	/**
	 * Crea un blocco all'interno della regione. 
	 * @param x numero di colonna.
	 * @param y numero di riga.
	 * @param val valore da assegnare al blocco.
	 * @param s {@link SudokuInstance} di cui il blocco farà parte.
	 */
	protected void createBlockInRegion(int x, int y, int val, SudokuInstance s)
	{
		if(val != 0) blocksFull++;
		Block b = new Block(x, y, val, s);
		b.setRegion(this);
		blockMatrix[y%3][x%3] = b;
	}
	
	/**
	 * Crea un blocco all'interno della regione. 
	 * Aggiungendo aggiungendo i valori in legal come soluzioni legali.
	 * 
	 * @param x numero di colonna
	 * @param y numero di riga
	 * @param s {@link SudokuInstance} di cui farà parte
	 * @param legal array di {@link Byte[]}, con le soluzioni legali.
	 */
	protected void createBlockInRegion(int x, int y, SudokuInstance s, int val, Byte[] legal){
		if(val != 0) blocksFull++;
		Block b = new Block(x, y, val, s, legal);
		b.setRegion(this);
		blockMatrix[y%3][x%3] = b;
	}
	
	/**
	 * Dato in input un valore verifica se sia legale nella regione
	 * @param val valore da verificare se sia legale
	 * @return {@link Boolean} : False se il valore è legale nella regione, True altrimenti.
	 */
	public boolean isLegal(int val){ return !illegal[val];	}
	
	/**
	 * Dato un valore lo imposta come legale, nella regione.
	 * @param val {@link Integer} : Valore che si vuole dichiarare legale, nella regione. 
	 */
	protected void setLegal(int val){ if(illegal[val]) illegal[val] = false; }
	
	/**
	 * Dato un valore lo imposta come illegale.
	 * @param val : {@link Integer} : Valore che si vuole dichiarare illegale, nella regione.
	 */
	protected void setIllegal(int val) { 
		if(!illegal[val]) illegal[val] = true;
	}
	
	/**
	 * Segnala il valore dato in input illegale a tutti i blocchi presenti nella regione.
	 * @param val Valore illegale.
	 */
	protected void setIllegalToAllBlockInRegion(int val){
		for(int i = 0; i<3; i++)
			for(int j = 0; j<3; j++){
				if(!blockMatrix[i][j].isEmpty()) continue;
				blockMatrix[i][j].declareIllegal(val);
			}
	}
	
	/**
	 * Verifica che la regione abbia tutti i blocchi riempiti con un valore.
	 * @return true se la regione è completa, false se c'è almeno un blocco senza valore.
	 */
	protected boolean isFull(){
		if(blocksFull>=9) return true;
		return false;
	}
	
	/**
	 *  Cerca i valori illegali nella regione.
	 */
	protected void findIllegalValue(){
		for(int y = 0; y<3; y++)
			for(int x = 0; x<3; x++){
				int val = getBlock(x, y).getVal();
				if(val != 0) setIllegal(val);
			}
	}
}
