package application;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public final class Utils {

	public final static int BOARD_WIDTH = 800;
	public final static int FIELD_SIZE = 100;
	public final static String turnW = "White's turn";
	public final static String turnB = "Black's turn";
	public final static String checkW = "White King is in check!";
	public final static String checkB = "Black King is in check!";
	public final static String revealedW = "If you move there, the White King will be in check!";
	public final static String revealedB = "If you move there, the Black King will be in check!";
	public final static String checkmateW = "Checkmate! White wins!";
	public final static String checkmateB = "Checkmate! Black wins!";
	public final static String stalemate = "It's a stalemate!";
	private static Field selectionField = null;
	private static Piece selectionPiece = null;

	private Utils(){
		throw new RuntimeException("This class is not meant to be instantialized");
	}

	public static boolean enpassant(Field source, Field dest, Field[][] fieldArray){
		boolean canEnpassant = false;
		Field fieldToEnpassant = null;
		if(source.getPieceOnTop() instanceof Pawn){
			try{
				if(source.getPieceOnTop().player.colour == Player.Colour.WHITE){
					if(fieldArray[dest.row][dest.col].getPieceOnTop() == null
					&& (fieldArray[dest.row + 1][dest.col].getPieceOnTop() instanceof Pawn)){
						if(fieldArray[dest.row + 1][dest.col].getPieceOnTop().player.colour == Player.Colour.BLACK
						&& ((Pawn)fieldArray[dest.row + 1][dest.col].getPieceOnTop()).enpassant == true){
							fieldToEnpassant = fieldArray[dest.row + 1][dest.col];
							canEnpassant = true;
						}
					}
				}
				if(source.getPieceOnTop().player.colour == Player.Colour.BLACK){
					if(fieldArray[dest.row][dest.col].getPieceOnTop() == null
					&& (fieldArray[dest.row - 1][dest.col].getPieceOnTop() instanceof Pawn)){
						if(fieldArray[dest.row - 1][dest.col].getPieceOnTop().player.colour == Player.Colour.WHITE
						&& ((Pawn)fieldArray[dest.row - 1][dest.col].getPieceOnTop()).enpassant == true){
							fieldToEnpassant = fieldArray[dest.row - 1][dest.col];
							canEnpassant = true;
						}
					}
				}
			}
			catch(ArrayIndexOutOfBoundsException e){
			}
		}
		if(canEnpassant && fieldToEnpassant != null ){
			source.movePieceOnTop(dest, fieldArray);
			fieldArray[fieldToEnpassant.row][fieldToEnpassant.col].setPieceOnTop(null);
			return true;
		}
		else return false;
	}
	public static boolean check(Field[][] fieldArray, Player player){
		player.isChecked = false;
		for(Field[] row : fieldArray){
			for(Field field : row){
				if(field.getPieceOnTop() != null && field.getPieceOnTop() instanceof King){
					if(field.getPieceOnTop().player.colour == player.colour){
						if(player.colour == Player.Colour.BLACK && field.canBlackKingMove == false){
							((King)field.getPieceOnTop()).isChecked = true;
							player.isChecked = true;
							Board.setDescription(checkB,3);
							return true;
						}
						if(player.colour == Player.Colour.WHITE && field.canWhiteKingMove == false){
							((King)field.getPieceOnTop()).isChecked = true;
							player.isChecked = true;
							Board.setDescription(checkW,3);
							return true;
						}
						return false;
					}
				}
			}
		}
		return false;
	}
	public static boolean stalemate(Field[][] fieldArray, Player player){
		if(player.getPieces().size() == 1 && player.getPieces().get(0) instanceof King){
			if(player.colour == Player.Colour.BLACK){
				if(player.getPieces().get(0).calculateLegalMoves(fieldArray).size() == 0 && fieldArray[player.getPieces().get(0).row][player.getPieces().get(0).col].canBlackKingMove){
					Board.setDescription(stalemate, 4);
					return true;
				}
			}
			else if(player.colour == Player.Colour.WHITE){
				if(player.getPieces().get(0).calculateLegalMoves(fieldArray).size() == 0 && fieldArray[player.getPieces().get(0).row][player.getPieces().get(0).col].canWhiteKingMove){
					Board.setDescription(stalemate, 4);
					return true;
				}
			}
		}
		return false;
	}
	public static boolean checkMate(Field[][] fieldArray, Player player){
		for(Field[] row: fieldArray){
			for(Field field: row){
				if(field.getPieceOnTop() != null && field.getPieceOnTop().player == player){
					for(int[] move : field.getPieceOnTop().calculateLegalMoves(fieldArray)){
						if(field.canMoveProtectKing(fieldArray[move[0]][move[1]], fieldArray) == false){
							continue;
						}
						else return false;
					}
					for(int[] move : field.getPieceOnTop().calculateAttackingMoves(fieldArray)){
						if(field.canMoveProtectKing(fieldArray[move[0]][move[1]], fieldArray) == false){
							continue;
						}
						else return false;
					}
				}
			}
		}
		if(player.colour == Player.Colour.BLACK){
			System.out.println("CHECKMATE BLACK!");
			Board.setDescription(checkmateW,4);
		}
		else{
			System.out.println("CHECKMATE WHITE!");
			Board.setDescription(checkmateB,4);
		}
		return true;
	}
	public static void checkKingMoves(Field[][] fieldArray){
		for(Field[] row : fieldArray){
			for(Field field : row){
				field.canBlackKingMove = true;
				field.canWhiteKingMove = true;
			}
		}
		for(Field[] row : fieldArray){
			for(Field field : row){
				if(field.getPieceOnTop() != null && field.getPieceOnTop().player.colour == Player.Colour.WHITE){
					try{
						for(int[] move : field.getPieceOnTop().calculateCheckingMoves(fieldArray)){
							fieldArray[move[0]][move[1]].canBlackKingMove = false;
						}
					}
					catch(NullPointerException e){
						continue;
					}
				}
				else if(field.getPieceOnTop() != null && field.getPieceOnTop().player.colour == Player.Colour.BLACK){
					try{
						for(int[] move : field.getPieceOnTop().calculateCheckingMoves(fieldArray)){
							fieldArray[move[0]][move[1]].canWhiteKingMove = false;
						}
					}
					catch(NullPointerException e){
						continue;
					}
				}
			}
		}
	}
	public static boolean castle(Field source, Field dest, Field[][] fieldArray){
		Utils.checkKingMoves(fieldArray);
		source.getPieceOnTop().player.addPieces(fieldArray);
		if(source.getPieceOnTop() == null || dest.getPieceOnTop() == null){
			System.out.println("RETURNING HERE 0");
			return false;
		}
		boolean canKingCastle = false;
		boolean canRookCastle = false;
		System.out.println(dest.getPieceOnTop().toString() + " " + source.getPieceOnTop().toString());
		if(source.getPieceOnTop().player.colour != dest.getPieceOnTop().player.colour){
			return false;
		}
		if(source.getPieceOnTop() instanceof King && ((King) source.getPieceOnTop()).initialPos == true && ((King) source.getPieceOnTop()).isChecked == false){
			canKingCastle = true;
		}
		else
			return false;
		if(dest.getPieceOnTop() instanceof Rook && ((Rook) dest.getPieceOnTop()).initialPos == true){
			canRookCastle = true;
		}
		else return false;
		if(canKingCastle &&  canRookCastle){
			if(source.getPieceOnTop().player.colour == Player.Colour.BLACK){
				if(source.col > dest.col){
					if(fieldArray[source.row][source.col - 1].getPieceOnTop() != null || fieldArray[source.row][source.col - 1].canBlackKingMove == false ){
						return false;
					}
					if(fieldArray[source.row][source.col - 2].getPieceOnTop() != null || fieldArray[source.row][source.col - 2].canBlackKingMove == false ){
						return false;
					}

				}
				if(source.col < dest.col){
					if(fieldArray[source.row][source.col + 1].getPieceOnTop() != null || fieldArray[source.row][source.col + 1].canBlackKingMove == false ){
						return false;
					}
					if(fieldArray[source.row][source.col + 2].getPieceOnTop() != null || fieldArray[source.row][source.col + 2].canBlackKingMove == false ){
						return false;
					}
				}
			}
			else if(source.getPieceOnTop().player.colour == Player.Colour.WHITE){
				if(source.col > dest.col){
					if(fieldArray[source.row][source.col - 1].getPieceOnTop() != null || fieldArray[source.row][source.col - 1].canWhiteKingMove == false ){
						return false;
					}
					if(fieldArray[source.row][source.col - 2].getPieceOnTop() != null || fieldArray[source.row][source.col - 2].canWhiteKingMove == false ){
						return false;
					}
				}
				if(source.col < dest.col){
					System.out.println("IM HERE 7");
					if(fieldArray[source.row][source.col + 1].getPieceOnTop() != null || fieldArray[source.row][source.col + 1].canWhiteKingMove == false ){
						return false;
					}
					if(fieldArray[source.row][source.col + 2].getPieceOnTop() != null || fieldArray[source.row][source.col + 2].canWhiteKingMove == false ){
						return false;
					}
				}
			}
			if(source.col < dest.col){
				fieldArray[source.row][source.col].movePieceOnTop(fieldArray[source.row][source.col + 2], fieldArray);
				fieldArray[dest.row][dest.col].movePieceOnTop(fieldArray[source.row][source.col + 1], fieldArray);
			}
			else if(source.col > dest.col){
				fieldArray[source.row][source.col].movePieceOnTop(fieldArray[source.row][source.col - 2], fieldArray);
				fieldArray[dest.row][dest.col].movePieceOnTop(fieldArray[source.row][source.col - 1], fieldArray);
			}
			return true;
		}
		else return false;
	}
	public static void promotePawns(Field[][] fieldArray, Player turn){
		selectionField = null;
		selectionPiece = null;
		Button queen = new Button("Queen");
		Button rook = new Button("Rook");
		Button knight = new Button("Knight");
		Button bishop = new Button("Bishop");
		Stage stage = new Stage();
		stage.resizableProperty().setValue(Boolean.FALSE);
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent event) {
		        event.consume();
		    }
		});
		//stage.initStyle(StageStyle.UTILITY);
		Pane pane1 = new FlowPane();
		for(int x =0; x<8; x++){
			if(fieldArray[0][x].getPieceOnTop() != null && fieldArray[0][x].getPieceOnTop() instanceof Pawn){
				if(fieldArray[0][x].getPieceOnTop().player.colour == Player.Colour.WHITE){
					selectionField = fieldArray[0][x];
				}
			}
			if(fieldArray[7][x].getPieceOnTop() != null && fieldArray[7][x].getPieceOnTop() instanceof Pawn){
				if(fieldArray[7][x].getPieceOnTop().player.colour == Player.Colour.BLACK){
					selectionField = fieldArray[7][x];
				}
			}
		}
		if(selectionField != null){
			queen.setOnAction(actionEvent ->  {
				selectionPiece = new Queen(selectionField.row,selectionField.col,turn);
				selectionField.setPieceOnTop(selectionPiece);
				stage.close();
			});
			rook.setOnAction(actionEvent ->  {
				selectionPiece = new Rook(selectionField.row,selectionField.col,turn);
				selectionField.setPieceOnTop(selectionPiece);
				stage.close();
			});
			knight.setOnAction(actionEvent ->  {
				selectionPiece = new Knight(selectionField.row,selectionField.col,turn);
				selectionField.setPieceOnTop(selectionPiece);
				stage.close();
			});
			bishop.setOnAction(actionEvent ->  {
				selectionPiece = new Bishop(selectionField.row,selectionField.col,turn);
				selectionField.setPieceOnTop(selectionPiece);
				stage.close();
			});
			pane1.getChildren().addAll(queen,rook,knight,bishop);
			Scene scene = new Scene(pane1);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
		    stage.setTitle("Select a piece to promote to");
		    stage.show();
		}
	}

}
