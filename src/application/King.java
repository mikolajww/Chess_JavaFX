package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

public final class King extends Piece {
	public boolean isChecked = false;
	public boolean initialPos = true;

	King(int row, int col, Player player){
		super(row,col,player);
		if(player.colour == Player.Colour.BLACK){
			this.img = new Image(King.class.getResourceAsStream("/kingBlack.png"),100,100, true,true);
		}
		else{
			this.img = new Image(King.class.getResourceAsStream("/kingWhite.png"),100,100, true,true);
		}
	}
	@Override
	List<int[]> calculateLegalMoves(Field[][] fieldArray) {
		Utils.checkKingMoves(fieldArray);
		ArrayList<int[]> legalMoves =  new ArrayList<int[]>();
		int[][] toCheck = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{-1,-1},{1,-1},{-1,1}};
		for(int[] i : toCheck){
			try{
				if(fieldArray[this.row + i[0]][this.col + i[1]].getPieceOnTop() == null){
					if(this.player.colour == Player.Colour.BLACK && fieldArray[this.row + i[0]][this.col + i[1]].canBlackKingMove == true){
						legalMoves.add(new int[]{this.row + i[0],this.col + i[1]});
					}
					else if(this.player.colour == Player.Colour.WHITE && fieldArray[this.row + i[0]][this.col + i[1]].canWhiteKingMove == true){
						legalMoves.add(new int[]{this.row + i[0],this.col + i[1]});
					}
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
		ArrayList<int[]> attackingMoves =  new ArrayList<int[]>();
		int[][] toCheck = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{-1,-1},{1,-1},{-1,1}};
		for(int[] i : toCheck){
			try{
				if(fieldArray[this.row + i[0]][this.col + i[1]].getPieceOnTop() != null && fieldArray[this.row + i[0]][this.col + i[1]].getPieceOnTop().player.colour == this.player.opponent){
					attackingMoves.add(new int[]{this.row + i[0],this.col + i[1]});
				}
			}
			catch(ArrayIndexOutOfBoundsException e){
				continue;
			}
		}
		this.legalMoves = attackingMoves;
		return attackingMoves;
	}
	@Override
	List<int[]> calculateCheckingMoves(Field[][] fieldArray) {
		return calculateAttackingMoves(fieldArray);
	}


	@Override
	public Piece clone(Field[][] fieldArray) {
		King clone = new King(this.row, this.col, this.player);
		clone.isChecked = this.isChecked;
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
