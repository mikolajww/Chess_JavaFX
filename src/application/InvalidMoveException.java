package application;

public class InvalidMoveException extends Exception {

	private static final long serialVersionUID = -1952333282890407890L;
	private int row;
	private int col;
	public InvalidMoveException(String msg){ super(msg);}

	public InvalidMoveException(int row, int col, String msg){
		super(msg);
		this.row = row;
		this.col = col;
	}

	public int getRow() { return this.row;}
	public int getCol() { return this.col;}

}
