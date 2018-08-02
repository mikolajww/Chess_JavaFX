package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public final class Field {
	final int row;
	final int col;
	private Piece pieceOnTop =	null;
	boolean isFocused = false;
	boolean isPossible = false;
	boolean isAttacked = false;
	boolean canWhiteKingMove = true;
	boolean canBlackKingMove = true;

	Field(int row, int col){
		this.row = row;
		this.col = col;
	}
	public Piece getPieceOnTop() {
		return pieceOnTop;
	}
	public void setPieceOnTop(Piece pieceOnTop) {
		this.pieceOnTop = pieceOnTop;
	}
	public void toggleSelection(){
		this.isFocused = this.isFocused == true? false: true;
	}
	public void drawField(GraphicsContext gc) {
		if(isFocused){
			gc.setFill(Color.web("#E0FFFF", 0.4)); //LIGHT CYAN
			gc.fillRect(this.col*100, this.row*100, 100, 100);
		}
		else if(isPossible){
			gc.setFill(Color.web("#90EE90", 0.7)); // LIGHT GREEN
			gc.fillRect(this.col*100, this.row*100, 100, 100);
		}
		else if(isAttacked){
			gc.setFill(Color.web("#FF0000", 0.4)); // RED
			gc.fillRect(this.col*100, this.row*100, 100, 100);
		}/*
		else if(canWhiteKingMove == false && canBlackKingMove == true){
			gc.setFill(Color.web("#8A2BE2",0.7));  //BLUEVIOLET
			gc.fillRect(this.col*100, this.row*100, 100, 100);
		}
		else if(canBlackKingMove == false && canWhiteKingMove == true){
			gc.setFill(Color.web("#FF00FF",0.7));
			gc.fillRect(this.col*100, this.row*100, 100, 100);
		}
		else if(canBlackKingMove == false && canWhiteKingMove == false){
			gc.setFill(Color.web("#FAFAD2",0.9));
			gc.fillRect(this.col*100, this.row*100, 100, 100);
		}*/
		if(this.getPieceOnTop() != null){
			gc.drawImage(this.pieceOnTop.img,this.pieceOnTop.getCol() * 100, this.pieceOnTop.getRow() * 100);
		}
	}
	public boolean movePieceOnTop(Field dest, Field[][] fieldArray){
		Piece backup = null;
		if(dest.getPieceOnTop() != null){
			backup = dest.getPieceOnTop().clone(fieldArray);
		}
		boolean triedToMovePawn = false;
		boolean triedToMoveKing = false;
		boolean triedToMoveRook = false;
				if (this.pieceOnTop == null) return false;
				pieceOnTop.moveTo(dest.row, dest.col);
				dest.setPieceOnTop(pieceOnTop);
				if(this.pieceOnTop instanceof Pawn && ((Pawn) this.pieceOnTop).initialPos == true){
					((Pawn) this.pieceOnTop).initialPos = false;
					if(Math.abs(this.row - dest.row) == 2){
						System.out.println("enpassant");
						((Pawn) this.pieceOnTop).enpassant = true;
					}
					triedToMovePawn = true;
				}
				if(this.pieceOnTop instanceof King && ((King) this.pieceOnTop).initialPos == true){
					((King) this.pieceOnTop).initialPos = false;
					triedToMoveKing = true;
				}
				if(this.pieceOnTop instanceof Rook && ((Rook) this.pieceOnTop).initialPos == true){
					((Rook) this.pieceOnTop).initialPos = false;
					triedToMoveRook = true;
				}
				this.pieceOnTop = null;
				Utils.checkKingMoves(fieldArray);
				if(Utils.check(fieldArray, dest.getPieceOnTop().player)){
					dest.getPieceOnTop().moveTo(this.row, this.col);
					this.setPieceOnTop(dest.getPieceOnTop());
					dest.setPieceOnTop(backup);
					if(this.pieceOnTop instanceof Pawn && triedToMovePawn){
						((Pawn) this.pieceOnTop).initialPos = true;
					}
					if(this.pieceOnTop instanceof King && triedToMoveKing){
						((King) this.pieceOnTop).initialPos = true;
					}
					if(this.pieceOnTop instanceof Rook && triedToMoveRook){
						((Rook) this.pieceOnTop).initialPos = true;
					}
					return false;
				}
				return true;
	}

	public boolean canMoveProtectKing(Field dest, Field[][] fieldArray){
		Piece backup = null;
		if(dest.getPieceOnTop() != null){
			backup = dest.getPieceOnTop().clone(fieldArray);
		}
		boolean triedToMovePawn = false;
		boolean triedToMoveKing = false;
		boolean triedToMoveRook = false;
		boolean isRecoverable;
			try{
				pieceOnTop.moveTo(dest.row, dest.col);
				dest.setPieceOnTop(pieceOnTop);
				if(this.pieceOnTop instanceof Pawn && ((Pawn) this.pieceOnTop).initialPos == true){
					((Pawn) this.pieceOnTop).initialPos = false;
					triedToMovePawn = true;
				}
				if(this.pieceOnTop instanceof King && ((King) this.pieceOnTop).initialPos == true){
					((King) this.pieceOnTop).initialPos = false;
					triedToMoveKing = true;
				}
				if(this.pieceOnTop instanceof Rook && ((Rook) this.pieceOnTop).initialPos == true){
					((Rook) this.pieceOnTop).initialPos = false;
					triedToMoveRook = true;
				}
				this.pieceOnTop = null;
				Utils.checkKingMoves(fieldArray);
				if(Utils.check(fieldArray, dest.getPieceOnTop().player))isRecoverable = false;
				else isRecoverable = true;
				dest.getPieceOnTop().moveTo(this.row, this.col);
				this.setPieceOnTop(dest.getPieceOnTop());
				dest.setPieceOnTop(backup);
				if(this.pieceOnTop instanceof Pawn && triedToMovePawn){
					((Pawn) this.pieceOnTop).initialPos = true;
				}
				if(this.pieceOnTop instanceof King && triedToMoveKing){
					((King) this.pieceOnTop).initialPos = true;
				}
				if(this.pieceOnTop instanceof Rook && triedToMoveRook){
					((Rook) this.pieceOnTop).initialPos = true;
				}
				if(isRecoverable){
					System.out.println("Can block king by moving " +isRecoverable + " " + dest.row + " " + dest.col);}
				return isRecoverable;
			}
			catch(NullPointerException e){
				return false;
			}
	}
	public void showPossibleMoves(Field[][] fieldArray){
		if(this.isFocused){
			if(pieceOnTop != null){
				for(int[] i : pieceOnTop.calculateLegalMoves(fieldArray)){
					try{
						if(fieldArray[i[0]][i[1]].pieceOnTop == null){
							fieldArray[i[0]][i[1]].isPossible = true;
						}
					}
					catch(ArrayIndexOutOfBoundsException e){
						continue;
					}
				}
			}
			if(pieceOnTop != null){
				for(int[] i : pieceOnTop.calculateAttackingMoves(fieldArray)){
					try{
						fieldArray[i[0]][i[1]].isAttacked = true;
					}
					catch(ArrayIndexOutOfBoundsException e){
						continue;
					}
				}
			}
		}
		else{
			if(pieceOnTop != null){
				for(int[] i : pieceOnTop.calculateLegalMoves(fieldArray)){
					try{
						fieldArray[i[0]][i[1]].isPossible = false;
					}
					catch(ArrayIndexOutOfBoundsException e){
						continue;
					}
				}
			}
		}

	}
}
