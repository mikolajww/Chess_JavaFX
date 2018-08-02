package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

public class Pawn extends Piece {
	public boolean initialPos;
	public boolean enpassant = true;

	Pawn(int row, int col, Player player){
		super(row,col,player);
		if(player.colour == Player.Colour.BLACK){
			this.img = new Image(Pawn.class.getResourceAsStream("/pawnBlack.png"),100,100, true,true);
		}
		else{
			this.img = new Image(Pawn.class.getResourceAsStream("/pawnWhite.png"),100,100, true,true);
		}
		if(row == 1 || row == 6) initialPos = true;
		else initialPos = false;

	}
	@Override
	List<int[]> calculateLegalMoves(Field[][] fieldArray) {
		ArrayList<int[]> legalMoves =  new ArrayList<int[]>();
		int[][] possibleMoves = {{1,0},{2,0},{-1,0},{-2,0}};
		ArrayList<int[]> toCheck =  new ArrayList<int[]>();
		if(player.colour == Player.Colour.BLACK){
			toCheck.add(possibleMoves[0]);
			if(initialPos)toCheck.add(possibleMoves[1]);
		}
		else if(player.colour == Player.Colour.WHITE){
			toCheck.add(possibleMoves[2]);
			if(initialPos)toCheck.add(possibleMoves[3]);
		}
		for(int[] i : toCheck){
			try{
				if(fieldArray[this.row + i[0]][this.col + i[1]].getPieceOnTop() == null){
					legalMoves.add(new int[]{this.row + i[0],this.col + i[1]});
				}
				else break;
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
		int[][] possibleMoves = {{1,1},{1,-1},{-1,1},{-1,-1}};
		ArrayList<int[]> toCheck =  new ArrayList<int[]>();

		if(player.colour == Player.Colour.BLACK){
			toCheck.add(possibleMoves[0]);
			toCheck.add(possibleMoves[1]);
		}
		else if(player.colour == Player.Colour.WHITE){
			toCheck.add(possibleMoves[2]);
			toCheck.add(possibleMoves[3]);
		}
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
		this.attackingMoves = attackingMoves;
		return attackingMoves;
	}
	@Override
	List<int[]> calculateCheckingMoves(Field[][] fieldArray) {
		ArrayList<int[]> attackingMoves =  new ArrayList<int[]>();
		int[][] possibleMoves = {{1,1},{1,-1},{-1,1},{-1,-1}};
		ArrayList<int[]> toCheck =  new ArrayList<int[]>();

		if(player.colour == Player.Colour.BLACK){
			toCheck.add(possibleMoves[0]);
			toCheck.add(possibleMoves[1]);
		}
		else if(player.colour == Player.Colour.WHITE){
			toCheck.add(possibleMoves[2]);
			toCheck.add(possibleMoves[3]);
		}
		for(int[] i : toCheck){
			try{
				fieldArray[this.row + i[0]][this.col + i[1]].getPieceOnTop();
				attackingMoves.add(new int[]{this.row + i[0],this.col + i[1]});
			}
			catch(ArrayIndexOutOfBoundsException e){
				continue;
			}
		}
		this.attackingMoves = attackingMoves;
		return attackingMoves;
	}
	@Override
	public Piece clone(Field[][] fieldArray) {
		Pawn clone = new Pawn(this.row, this.col, this.player);
		clone.img = this.img;
		clone.initialPos = this.initialPos;
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
