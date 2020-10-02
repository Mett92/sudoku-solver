package psmc.sudokupart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;


/**
 * Istanza di Sudoku.
 * 
 * @author Mattia Paolacci 
 */
public class SudokuInstance {
	
	/**
	 * Matrice di oggetti di tipo:{@link Region}, 3x3.
	 * 
	 * @author  Mattia Paolacci
	 */
	protected Region[][] regionMatrix = new Region[3][3];
	
	/**
	 * Rappresenta le {@link Row} nell'istanza di sudoku; l'indice corrisponde al n° di riga.
	 */
	protected Row[] rowsContainer = new Row[9];
	
	/**
	 * Rappresenta le {@link Column} nell'istanza di sudoku; l'indice corrisponde al n° di colonna.
	 */
	protected Column[] columnsContainer = new Column[9];
	
	/**
	 * Tiene il posto dell'ultimo blocco restituito.
	 */
	protected Segnaposto lastGivenBlock;
	
	/** 
	 * Costruttore di un istanza di sudoku. Costruisce un sudoku inserendo i valori presenti nel file
	 * nelle rispettive posizioni. Inoltre se trova un "." lo sostituisce con 0.
	 * 
	 * @param f	{@link FileReader} file con il sudoku.
	 * @throws IOException
	 */
	public SudokuInstance(FileReader f) throws IOException {
		
		// Istanzio la matrice di Regioni
		for (int i = 0; i<3; i++){
			for(int j=0; j<3; j++){
				regionMatrix[i][j] = new Region();
			}
		}
		BufferedReader b = new BufferedReader(f);
		String row;
		for(int i = 0; i<9; i++){
			//istanzio righe e colonne (vuote)
			columnsContainer[i] = new Column();
			rowsContainer[i] = new Row();
			
			// Istanzio blocchi nelle regioni
			row = b.readLine();
			for(int j = 0; j<9; j++){
				if(row.charAt(j)!='.') createBlockInRegionMatrix(j, i, Character.getNumericValue(row.charAt(j)));
				else createBlockInRegionMatrix(j, i, 0);
			}
		}
		// Aggiungo blocchi alle righe e alle colonne.
		for(int y = 0; y<9; y++){
			for(int x = 0; x<9; x++){
				rowsContainer[y].addBlock(getBlock(x, y));
				columnsContainer[x].addBlock(getBlock(x, y));
			}
		}
	}
	
	/**
	 * Costruttore. Crea un'istanza di sudoku con le sole regioni, inizializzate vuote.
	 */
	private SudokuInstance(){
		// istanzio le regioni vuote
		for(int i = 0; i<3; i++){
			for(int j = 0; j<3; j++){
				regionMatrix[i][j] = new Region();
			}
		}
	}
	
	/**
	 * Ritorna il primo blocchetto che occorre vuoto. Assume che l'istanza di sudoku non sia completa 
	 * e quindi che esiste almeno un blocco vuoto.
	 * @return Istanza di {@link Block}, vuota. Può ritornare null, solo nel caso l'istanza sia completa.
	 */
	public Block getFirstBlockEmpty(){
		// se non è mai stata chiamata la funzione parte da (0,0)
		if(lastGivenBlock == null)
			for(int i = 0; i<9; i++)
				for(int j = 0; j<9; j++)
					if(getBlock(j, i).isEmpty()){
						lastGivenBlock = new Segnaposto(j, i);
						return getBlock(j, i);
					}
		
		// Altrimenti parte dal segnaposto
		int r = lastGivenBlock.getRow();
		int c = lastGivenBlock.getColumn()+1;
		// se c >= 9 allora passo alla riga successiva.
		if(c>=9){ c = 0; r++; }
		while(r<9){
			Block b = getBlock(c, r);
			if(b.isEmpty()){
				lastGivenBlock.setColumn(c);
				lastGivenBlock.setRow(r);
				return b;
			}
			// se c<= di 8 aumento la colonna, altrimenti passo alla riga successiva.
			if(c<8) c++;
			else{ c = 0; r++; }
		}
		return null;
	}
	
	/**
	 * Verifica che il sudoku sia risolto.
	 * @return true se è risolto, false altrimenti.
	 */
	public boolean isComplete(){
		for(int r = 0; r<3; r++){
			for(int c = 0; c<3; c++){
				if(!regionMatrix[r][c].isFull()) return false;
			}
		}
		return true;
	}
	
	/**
	 * Ritorna la dimensione dello spazio delle soluzioni dell'istanza di sudoku, 
	 * ossia il prodotto del numero di candidati di ogni cella vuota.
	 *  
	 * @return Un intero che rappresenta lo spazio delle soluzioni.
	 */
	public String solutionSpace(){
		// mantissa
		float mant = 1;
		// esponente
		int exp = 0;
		for(int r = 0; r<9; r++){
			for(int c = 0; c<9; c++){
				if(getBlock(c, r).isEmpty()){
					mant = mant * getBlock(c, r).getDimSetLegalSol();
				}
				while(mant > 10){
					mant = (float) (mant/10.0);
					exp++;
					String s = mant+"";
					if(s.length() > 7){ 
						s = s.substring(0, 5);
					}
					mant = Float.parseFloat(s);
				}
			}
		}
		return mant+"*10^"+exp;
	}
	
	/**
	 * Ritorna la percetuale di riempimento del sudoku
	 * @return percentuale di riempimento
	 */
	public int fillRate(){
		int tot = 0;
		for(int i = 0; i<3; i++)
			for(int j = 0; j<3; j++)
				tot+=regionMatrix[i][j].blocksFull;
		return (tot*100)/81;
	}
	
	/**
	 * Ritorna il blocco di coordinate date.
	 * @param x numero di colonna.
	 * @param y numero di riga.
	 * @return {@link Block} di coordinate (x,y).
	 */
	public Block getBlock(int x, int y){
		return regionMatrix[y/3][x/3].getBlock(x, y);
	}

	/**
	 * Crea il blocco, inserendolo nella giusta regione in base alle coordinate date, assegnandogli il valore val dato.
	 * @param x numero di colonna.
	 * @param y numero di riga.
	 * @param val valore da assegnare al blocco
	 */
	public void createBlockInRegionMatrix(int x, int y, int val){
		regionMatrix[y/3][x/3].createBlockInRegion(x, y, val, this);
	}
	
	/**
	 * Crea un blocco nell'istanza s, impostando x, y e val rispettivamente l'indice di colonna di riga e il valore del blocco; 
	 * inoltre inserisce come soluzioni legali legal.
	 * 
	 * @param x numero di colonna.
	 * @param y numero di riga.
	 * @param val valore del blocco.
	 * @param legal {@link Byte[]} array di soluzioni legali.
	 * @param s {@link SudokuInstance} Istanza di sudoku di cui farà parte il blocco.
	 */
	private void createBlockInRegionMatrix(int x, int y, int val, SudokuInstance s, Byte[] legal){
		s.regionMatrix[y/3][x/3].createBlockInRegion(x, y, s, val, legal);
	}
	
	/**
	 *  Inizializza la matrice di Sudoku, ricercando i valori illegali nelle regioni, colonne e righe e 
	 *  infine i valori legali per i blocchi.
	 */
	public void findLegalValue(){
		// Cerca valori illegali nelle colonne e righe.
		for(int i = 0; i<9; i++){
			rowsContainer[i].findIllegalValue();
			columnsContainer[i].findIllegalValue();
		}
		// Cerca valori illegali nelle regioni
		for(int i=0; i<3; i++){
			for(int j = 0; j<3; j++){
				regionMatrix[i][j].findIllegalValue();
			}
		}
		// Cerca i valori legali per i blocchi.
		for(int r = 0; r<9; r++){
			for(int c = 0; c<9; c++){
				getBlock(c, r).findLegalValue();
			}
		}
	}
	
	/**
	 *  Ricerca all'interno di tutta la matrice di sudoku blocchetti con un solo possibile
	 *  valore legale, impostandolo.
	 */
	public void findBlockWithOneLegalValue(){
		for(int i = 0; i<9; i++){
			for(int j = 0; j<9; j++){
				getBlock(j, i).setIfOneLegalValue();
			}
		}
	}
	
	/**
	 * Ritorna una copia dell'istanza.
	 * @return {@link SudokuInstance}
	 */
	public SudokuInstance clona(){
		// istanzia un sudoku con le sole regioni, vuote.
		SudokuInstance s = new SudokuInstance();
		
		// istanzia le righe, le colonne e riempe le regioni con i blocchi, copiati dal sudoku this.
		for(int y = 0; y<9; y++){
			s.columnsContainer[y] = new Column();
			s.rowsContainer[y] = new Row();
			
			for(int x = 0; x<9; x++){
				Block b = getBlock(x, y);
				s.createBlockInRegionMatrix(b.getX(), b.getY(), b.getVal(), s, b.getLegalValues());
			}
		}
		
		for(int r = 0; r<9; r++){
			for(int c = 0; c<9; c++){
				s.rowsContainer[r].addBlock(s.getBlock(c,r));
				s.columnsContainer[c].addBlock(s.getBlock(c, r));
			}
		}
		return s;
	}
}
