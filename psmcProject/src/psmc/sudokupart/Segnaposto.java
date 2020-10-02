package psmc.sudokupart;

/**
 * Questa classe rappresenta l'entità di "Segnaposto" esso identifica un posto all'interno di un'istanza di sudoku.
 *
 * @author  Mattia Paolacci
 */
public class Segnaposto {
	
	private int col;
	private int row;
	public Segnaposto(int c, int r){ col = c; row = r; }
	
	public int getColumn(){ return col; }
	
	public int getRow(){ return row; }

	public void setColumn(int c){ col = c; }
	
	public void setRow(int r){ row = r; }
}
