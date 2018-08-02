package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

public final class Queen extends Piece {
	Queen(int row, int col, Player player){
		super(row,col,player);
		if(player.colour == Player.Colour.BLACK){
			this.img = new Image(Queen.class.getResourceAsStream("/queenBlack.png"),100,100, true,true);
		}
		else{
			this.img = new Image(Queen.class.getResourceAsStream("/queenWhite.png"),100,100, true,true);
		}
	}
	@Override
	List<int[]> calculateLegalMoves(Field[][] fieldArray) {
		Rook tempRook = new Rook(this.row,this.col, this.player);
		List<int[]> listRook = tempRook.calculateLegalMoves(fieldArray);
		Bishop tempBishop = new Bishop(this.row,this.col, this.player);
		List<int[]> listBishop = tempBishop.calculateLegalMoves(fieldArray);
		ArrayList<int[]> legalMoves =  new ArrayList<int[]>(listRook);
		legalMoves.addAll(listBishop);
		this.legalMoves = legalMoves;
		return legalMoves;
	}
	@Override
	List<int[]> calculateAttackingMoves(Field[][] fieldArray) {
		Rook tempRook = new Rook(this.row,this.col, this.player);
		List<int[]> listRook = tempRook.calculateAttackingMoves(fieldArray);
		Bishop tempBishop = new Bishop(this.row,this.col, this.player);
		List<int[]> listBishop = tempBishop.calculateAttackingMoves(fieldArray);
		ArrayList<int[]> attackingMoves =  new ArrayList<int[]>(listRook);
		attackingMoves.addAll(listBishop);
		this.attackingMoves = attackingMoves;
		return attackingMoves;
	}

	@Override
	List<int[]> calculateCheckingMoves(Field[][] fieldArray) {
		List<int[]>checking = new ArrayList<int[]>(calculateAttackingMoves(fieldArray));
		checking.addAll(calculateLegalMoves(fieldArray));
		return checking;
	}
	@Override
	public Piece clone(Field[][] fieldArray) {
		Queen clone = new Queen(this.row, this.col, this.player);
		clone.img = this.img;
		ArrayList<int[]> lms = new ArrayList<>();
		for(int[] i : calculateLegalMoves(fieldArray)){
			lms.add(i.clone());
		}
		ArrayList<int[]> ams = new ArrayList<>();
		for(int[] i : calculateAttackingMoves(fieldArray)){
			ams.add(i.clone());
		}
		clone.attackingMoves = ams;
		clone.legalMoves = lms;
		return clone;
	}

}
