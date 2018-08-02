package application;


import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

public abstract class Piece{
	protected int row;
	protected int col;
	public Player player;
	public Image img;
	public ArrayList<int[]> legalMoves;
	public ArrayList<int[]> attackingMoves;

	public Piece(int row,int column, Player player){
		this.row = row;
		this.col = column;
		this.player = player;
	}

	public void moveTo(int row, int col){
		this.row = row;
		this.col = col;
	}
	public abstract Piece clone(Field[][] fieldArray);
	public int getRow(){return row;}
	public int getCol(){return col;}
	abstract List<int[]> calculateLegalMoves(Field[][] fieldArray);
	abstract List<int[]> calculateAttackingMoves(Field[][] fieldArray);
	abstract List<int[]> calculateCheckingMoves(Field[][] fieldArray);
}
