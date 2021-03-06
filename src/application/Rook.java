package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

public final class Rook extends Piece {
	public boolean initialPos;

	Rook(int row, int col, Player player){
		super(row,col,player);
		if(player.colour == Player.Colour.BLACK){
			this.img = new Image(Rook.class.getResourceAsStream("/rookBlack.png"),100,100, true,true);
		}
		else{
			this.img = new Image(Rook.class.getResourceAsStream("/rookWhite.png"),100,100, true,true);
		}
		initialPos = true;
	}

	@Override
	List<int[]> calculateLegalMoves(Field[][] fieldArray) {
		ArrayList<int[]> legalMoves =  new ArrayList<int[]>();
		for(int i = 1 ; i<8; i++){
			try{
				if(fieldArray[this.row + i][this.col].getPieceOnTop() == null){
					legalMoves.add(new int[]{this.row + i,this.col});
				}
				else break;
			}
			catch(ArrayIndexOutOfBoundsException e){
				break;
			}
		}
		for(int i = 1 ; i<8; i++){
			try{
				if(fieldArray[this.row -i][this.col].getPieceOnTop() == null){
					legalMoves.add(new int[]{this.row -i,this.col});
				}
				else break;
			}
			catch(ArrayIndexOutOfBoundsException e){
				break;
			}
		}
		for(int i =1 ; i<8; i++){
			try{
				if(fieldArray[this.row][this.col + i].getPieceOnTop() == null){
					legalMoves.add(new int[]{this.row , this.col +i});
				}
				else break;
			}
			catch(ArrayIndexOutOfBoundsException e){
				break;
			}
		}
		for(int i =1 ; i<8; i++){
			try{
				if(fieldArray[this.row][this.col - i].getPieceOnTop() == null){
					legalMoves.add(new int[]{this.row, this.col-i});
				}
				else break;
			}
			catch(ArrayIndexOutOfBoundsException e){
				break;
			}
		}
		this.legalMoves = legalMoves;
		return legalMoves;
	}
	@Override
	List<int[]> calculateAttackingMoves(Field[][] fieldArray) {
		ArrayList<int[]> attackingMoves =  new ArrayList<int[]>();
		for(int i = 1 ; i<8; i++){
			try{
				if(fieldArray[this.row + i][this.col].getPieceOnTop() != null){
					if(fieldArray[this.row + i][this.col].getPieceOnTop().player.colour == this.player.opponent){
						attackingMoves.add(new int[]{this.row + i,this.col});
						break;
					}
					else if(fieldArray[this.row + i][this.col].getPieceOnTop().player.colour == this.player.colour){
						break;
					}
				}
			}
			catch(ArrayIndexOutOfBoundsException e){
				break;
			}
		}
		for(int i = 1 ; i<8; i++){
			try{
				if(fieldArray[this.row  - i][this.col].getPieceOnTop() != null){
					if(fieldArray[this.row - i][this.col].getPieceOnTop().player.colour == this.player.opponent){
						attackingMoves.add(new int[]{this.row - i,this.col});
						break;
					}
					else if(fieldArray[this.row - i][this.col].getPieceOnTop().player.colour == this.player.colour){
						break;
					}
				}
			}
			catch(ArrayIndexOutOfBoundsException e){
				break;
			}
		}
		for(int i =1 ; i<8; i++){
			try{
				if(fieldArray[this.row][this.col +i].getPieceOnTop() != null){
					if(fieldArray[this.row][this.col + i].getPieceOnTop().player.colour == this.player.opponent){
						attackingMoves.add(new int[]{this.row ,this.col + i});
						break;
					}
					else if(fieldArray[this.row][this.col + i].getPieceOnTop().player.colour == this.player.colour){
						break;
					}
				}
			}
			catch(ArrayIndexOutOfBoundsException e){
				break;
			}
		}
		for(int i =1 ; i<8; i++){
			try{
				if(fieldArray[this.row][this.col-i].getPieceOnTop() != null){
					if(fieldArray[this.row][this.col-i].getPieceOnTop().player.colour == this.player.opponent){
						attackingMoves.add(new int[]{this.row ,this.col - i});
						break;
					}
					else if(fieldArray[this.row][this.col-i].getPieceOnTop().player.colour == this.player.colour){
						break;
					}
				}
			}
			catch(ArrayIndexOutOfBoundsException e){
				break;
			}
		}
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
		Rook clone = new Rook(this.row, this.col, this.player);
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
