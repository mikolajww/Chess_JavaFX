package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

public final class Knight extends Piece {

	Knight(int row, int col, Player player){
		super(row,col,player);
		if(player.colour == Player.Colour.BLACK){
			this.img = new Image(Knight.class.getResourceAsStream("/knightBlack.png"),100,100, true,true);
		}
		else{
			this.img = new Image(Knight.class.getResourceAsStream("/knightWhite.png"),100,100, true,true);
		}
	}
	@Override
	ArrayList<int[]> calculateLegalMoves(Field[][] fieldArray) {
		int[][] lms = {{-1,-2},{-2,-1},{-2,1},{2,-1},{1,2},{2,1},{2,-1},{1,-2},{-1,2}};
		ArrayList<int[]> legalMoves =  new ArrayList<int[]>();
		for(int[] i : lms){
			try{
				if(fieldArray[this.row + i[0]][this.col + i[1]].getPieceOnTop() == null){
					legalMoves.add(new int[]{this.row + i[0],this.col + i[1]} );
				}
			}
			catch(ArrayIndexOutOfBoundsException e){
				continue;
			}
		}
		this.legalMoves = legalMoves;
		return legalMoves;
	}
	@Override
	List<int[]> calculateAttackingMoves(Field[][] fieldArray) {
		int[][] ams = {{-1,-2},{-2,-1},{-2,1},{2,-1},{1,2},{2,1},{2,-1},{1,-2},{-1,2}};
		ArrayList<int[]> attackingMoves =  new ArrayList<int[]>();
		for(int[] i : ams){
			try{
				if(fieldArray[this.row + i[0]][this.col + i[1]].getPieceOnTop() != null && fieldArray[this.row + i[0]][this.col + i[1]].getPieceOnTop().player.colour == this.player.opponent ){
					attackingMoves.add(new int[]{this.row + i[0],this.col + i[1]});
				}
			}
			catch(ArrayIndexOutOfBoundsException e){
				continue;
			}
		}
		this.attackingMoves = attackingMoves;
		return attackingMoves;
	}
	@Override
	List<int[]> calculateCheckingMoves(Field[][] fieldArray) {
		return calculateAttackingMoves(fieldArray);
	}
	@Override
	public Piece clone(Field[][] fieldArray) {
		Knight clone = new Knight(this.row, this.col, this.player);
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
