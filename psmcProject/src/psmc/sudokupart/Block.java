package psmc.sudokupart;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import psmc.MainClassSequential;

/**
 * Questa classe rappresenta un blocchetto del sudoku.
 * @author Mattia Paolacci
 *
 */
public class Block{
	
	/**
	 * Istanza di sudoku di cui il blocco e' parte.
	 */
	private SudokuInstance s;
	
	/**
	 * Colonna a cui il blocco appartiene.
	 */
	private Column c;
	
	/**
	 * Riga a cui il blocco appartiene.
	 */
	private Row r;
	
	/**
	 * Regione a cui il blocco appartiene.
	 */
	private Region reg;
	
	/**
	 * Indice di colonna. X€[0,8]
	 */
	private final byte X;
	/**
	 * Indice di riga. X€[0,8]
	 */
	private final byte Y;
	
	/**
	 * Valore del blocco, se val = 0 allora il blocco è vuoto.
	 */
	private byte valore = 0;
	/**
	 * Insieme dei valori legali blocco. Se il blocco è pieno, allora legal = null; 
	 * Inizialmente è uguale a null.
	 */
	private Set<Byte> legal;
	
	/**
	 * Costruttore standard della classe. Da usare quando si inizializza una nuova {@link SudokuInstance}.
	 * @param x Indice di colonna.
	 * @param y Indice di riga.
	 * @param val Valore con cui inizializzare il blocco.
	 * @param s Istanza di sudoku di cui il blocco fara' parte.
	 */
	protected Block(int x, int y, int val, SudokuInstance s){ 
		this.valore = (byte) val;
		X = (byte) x; 
		Y = (byte)y; 
		this.s = s;
		// se il valore è != 0 creo l'insieme di valori legali, vuoto.
		if(val == 0) legal = new HashSet<Byte>();
		}
	
	/**
	 * Costruttore che inizializza anche l'insieme di soluzioni legali con legal. Utile quando si copia una
	 * {@link SudokuInstance}.
	 * 
	 * @param x Indice di colonna.
	 * @param y Indice di riga.
	 * @param val Valore del blocco.
	 * @param s Istanza di sudoku di cui il blocco farà parte.
	 * @param legal Array di soluzioni legali per il blocco.
	 */
	protected Block(int x, int y, int val, SudokuInstance s, Byte[] legal){
		this(x,y,val,s);
		if(val == 0) this.legal = new HashSet<Byte>();
		if(legal != null)
			for(int i = 0; i < legal.length; i++)
				this.legal.add(legal[i]);
			
	}
	
	/**
	 * Restituisce un iteratore sull'insieme delle soluzioni legali.
	 * @return Istanza di {@link Iterator<Byte>} sul Set delle soluzioni legali. 
	 */
	public Iterator<Byte> getIteratorSetLegal(){ return legal.iterator(); }
	
	/**
	 * Crea nel blocco il riferimento alla riga, alla quale appartiene.
	 * @param r {@link Row}
	 */
	protected void setRow(Row r){ this.r = r; }
	
	/**
	 * Crea nel blocco il riferimento alla colonna, alla quale appartiene.
	 * @param c {@link Column}
	 */
	protected void setColumn(Column c){ this.c = c; }
	
	/**
	 * Crea nel blocco il riferimento alla regione, alla quale appartiene.
	 * @param reg {@link Region}
	 */
	protected void setRegion(Region reg){ this.reg = reg; }

	/**
	 * @return Il numero di colonna, di tipo {@link Byte}
	 */
	public byte getX(){ return X; }
	
	/**
	 * @return Il numero di riga, di tipo {@link Byte}
	 */
	public byte getY(){ return Y; }
	
	/**
	 * @return Il valore del blocco, di tipo {@link Byte}
	 */
	public byte getVal(){ return valore; }
	
	/**
	 * Ritorna True se il blocco è vuoto, altrimenti False.
	 * @return Boolean
	 */
	public boolean isEmpty(){
		if(valore == 0) return true;
		return false;
	}

	/**
	 * La dimensione dell'insieme delle soluzioni legali.
	 * @return Il numero di soluzioni legali per il blocco, se 0 il blocco potrebbe essere pieno.
	 */
	public int getDimSetLegalSol(){
		if(isEmpty()) return legal.size();
		return 0;
	}
	
	/**
	 * Segnala il valore dato come illegale, se il blocco è vuoto.
	 * 
	 * @param val Valore da segnare come illegale.
	 */
	protected void declareIllegal(int val){
		if(isEmpty()) legal.remove((byte) val);
	}
	
	/**
	 * Segnala il valore dato come legale.
	 * @param val Valore legale.
	 */
	protected void declareLegal(int val){
		if(isEmpty()) legal.add((byte) val);
	}
	
	/**
	 * Ritorna, se il blocco è vuoto, un array contenente tutti i valori legali per il blocco; altrimenti 
	 * ritorna null.
	 * @return {@link Byte[]} Array di valori legali; se il blocco è pieno ritorna null.
	 */
	public Byte[] getLegalValues(){
		if(isEmpty()){
			Byte[] ans = new Byte[legal.size()];
			Iterator<Byte> iter = legal.iterator();
			for(int i = 0; i<ans.length; i++)
				ans[i] = (Byte) iter.next();
			return ans;
		}
		return null;
	} 
	
	/**
	 *  Trova i valori legali per il blocco.
	 */
	protected void findLegalValue(){
		if(isEmpty()){
			for(int i = 1; i<10; i++){
				if(reg.isLegal(i) && r.isLegal(i) && c.isLegal(i))
					declareLegal(i);
			}
		}
	}
	
	/**
	 *  Verifica che il blocco abbia un solo possibile valore legale. Se si lo imposta.
	 */
	public void setIfOneLegalValue(){
		if(getDimSetLegalSol() == 1)
			setVal(getIteratorSetLegal().next());
	}
	
	/**
	 * Ricerca all'interno della stessa regione, colonna e riga del blocco, se ci sia un blocco
	 * con un solo valore legale, impostandolo con il metodo setVal().
	 */
	private void findBlocksWithOneLegalValue(){
		for(int i = 0; i<9; i++){
			if(c.getBlock(i).getDimSetLegalSol() == 1 && !c.getBlock(i).equals(this)) {
				Iterator<Byte> ite = c.getBlock(i).getIteratorSetLegal();
				c.getBlock(i).setVal(ite.next());
			}
		}
		for(int i = 0; i<9; i++){
			if(r.getBlock(i).getDimSetLegalSol() == 1 && !r.getBlock(i).equals(this)){
				Iterator<Byte> ite = r.getBlock(i).getIteratorSetLegal();
				r.getBlock(i).setVal(ite.next());
			}
		}
		for(int i = 0; i<3; i++)
			for(int j = 0; j<3; j++){
				Block b = reg.blockMatrix[i][j];
				if(b.getDimSetLegalSol() == 1 && !b.equals(this)){
					Iterator<Byte> ite = b.getIteratorSetLegal();
					b.setVal(ite.next());
				}

			}
			
	}
	
	/**
	 * Se il blocco è vuoto, imposta val come valore del blocco, segnalandolo illegale 
	 * alla colonna, regione e riga di cui il blocco fa parte.
	 * Se il valore val è stato aggiunto, ricerca all'interno della riga, regione e colonna
	 * di cui il blocco fa parte se ci sia un blocco con un solo valore legale, inserendo
	 * quell'unico valore, richiamando cosi la funzione setVal().
	 * 
	 * @param val Valore che il blocco assumerà. 
	 */
	public void setVal(int val){
		if(isEmpty() && (c.isLegal(val) && r.isLegal(val) && reg.isLegal(val))) { 
			c.setIllegal(val);
			r.setIllegal(val);
			reg.setIllegal(val);
			c.setIllegalToAllBlockInColumn(val);
			r.setIllegalToAllBlockInRow(val);
			reg.setIllegalToAllBlockInRegion(val);
			this.valore = (byte) val;
			reg.blocksFull++;
			legal = null;
			findBlocksWithOneLegalValue();
		}
	}
	@Override
	public boolean equals(Object obj) {
		Block b = (Block) obj;
		if(getX() == b.getX() && getY() == b.getY()) return true;
		return false;
	}
}
