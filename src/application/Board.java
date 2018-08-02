package application;


import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public final class Board {

	private Image img;
	private Scene background;
	private BorderPane root;
	public Field[][] fieldArray;
	private static final Canvas canvas = new Canvas(800,800);
	private final GraphicsContext gc;
	public Field selectedField = null;
	public Field destinationField = null;
	public Player player1;
	public Player player2;
	public Player turn;
	public Text textHelperTurn = new Text();
	private static Text textHelper2 = new Text();
	private static Text textHelper3 = new Text();
	private static Text textHelper4 = new Text();
	public static int frame = 0;

  	public Board(Player player1, Player player2) {
  		this.player1 = player1;
  		this.player2 = player2;
  		this.turn = player1;
		img = new Image(Board.class.getResourceAsStream("/board.png"),800,800,true,true);
		root = new BorderPane(new ImageView(img));
		background = new Scene(root,1000,960);
		background.setFill(Color.ALICEBLUE);
		fieldArray = new Field[8][8];
		for(int i =0; i<8; i++){
			for(int j=0; j<8; j++){
				fieldArray[i][j] = new Field(i,j);
			}
		}
		root.setCenter(canvas);
		gc = canvas.getGraphicsContext2D();
		prepareBoard();
		textHelperTurn.setFont(new Font(20));
		textHelperTurn.setTextAlignment(TextAlignment.CENTER);
		textHelperTurn.setTextOrigin(VPos.CENTER);
		textHelper4.setFont(new Font(40));
		textHelper4.setTextAlignment(TextAlignment.CENTER);
		textHelper4.setTextOrigin(VPos.CENTER);
		textHelper2.setFont(new Font(20));
		textHelper2.setTextAlignment(TextAlignment.CENTER);
		textHelper2.setTextOrigin(VPos.CENTER);
		textHelper3.setFont(new Font(20));
		textHelper3.setTextAlignment(TextAlignment.CENTER);
		textHelper3.setTextOrigin(VPos.CENTER);
		VBox vbox = new VBox();
		vbox.getChildren().addAll(textHelper4,textHelperTurn, textHelper2, textHelper3);
		vbox.setAlignment(Pos.CENTER);
		root.setBottom(vbox);
		drawBoard();
	}
	public void prepareBoard(){
		for(int i=0 ; i<8; i++){
			fieldArray[1][i].setPieceOnTop(new Pawn(1,i, player2));
			fieldArray[6][i].setPieceOnTop(new Pawn(6,i,player1));
		}
		fieldArray[0][0].setPieceOnTop(new Rook(0,0,player2));
		fieldArray[0][7].setPieceOnTop(new Rook(0,7,player2));
		fieldArray[7][0].setPieceOnTop(new Rook(7,0,player1));
		fieldArray[7][7].setPieceOnTop(new Rook(7,7,player1));
		fieldArray[0][1].setPieceOnTop(new Knight(0,1,player2));
		fieldArray[0][6].setPieceOnTop(new Knight(0,6,player2));
		fieldArray[7][1].setPieceOnTop(new Knight(7,1,player1));
		fieldArray[7][6].setPieceOnTop(new Knight(7,6,player1));
		fieldArray[0][2].setPieceOnTop(new Bishop(0,2,player2));
		fieldArray[0][5].setPieceOnTop(new Bishop(0,5,player2));
		fieldArray[7][2].setPieceOnTop(new Bishop(7,2,player1));
		fieldArray[7][5].setPieceOnTop(new Bishop(7,5,player1));
		fieldArray[0][3].setPieceOnTop(new Queen(0,3,player2));
		fieldArray[0][4].setPieceOnTop(new King(0,4,player2));
		fieldArray[7][3].setPieceOnTop(new Queen(7,3,player1));
		fieldArray[7][4].setPieceOnTop(new King(7,4,player1));
		player1.addPieces(fieldArray);
		player2.addPieces(fieldArray);
	}

	public void drawBoard(){
		textHelperTurn.setText(this.turn.turnNotification);
		gc.drawImage(img, 0, 0);
		for(int i =0; i<8; i++){
			for(int j=0; j<8; j++){
					fieldArray[i][j].drawField(gc);
			}
		}

	}
	public void checkPlayersPieces(){
		player1.addPieces(fieldArray);
		player2.addPieces(fieldArray);
	}
	public void handleMoves(){
		Utils.checkKingMoves(fieldArray);
		if(selectedField != null && destinationField != null){
			 try{
				 if(selectedField.getPieceOnTop() == null) throw new InvalidMoveException(" ");
				 //Try legal moves
				 for(int[] move: selectedField.getPieceOnTop().calculateLegalMoves(fieldArray)){
					if(destinationField.row == move[0] && destinationField.col == move[1]){
						 if(selectedField.getPieceOnTop() instanceof King){
							 //Moving black king to attacked field
							 if(destinationField.canBlackKingMove == false && selectedField.getPieceOnTop().player.colour==Player.Colour.BLACK){
								 throw new InvalidMoveException(Utils.revealedB);
							 }
							 //Moving white king to attacked field
							 else if(destinationField.canWhiteKingMove == false && selectedField.getPieceOnTop().player.colour==Player.Colour.WHITE){
								 throw new InvalidMoveException(Utils.revealedW);
							 }
						 }
						//Moving to a valid field
						if(selectedField.movePieceOnTop(destinationField,fieldArray)){
							toggleTurn();
					    	selectedField = null;
					    	destinationField = null;
					    	deselectAllFields();
					    	return;
						}
						else throw new InvalidMoveException("You can't leave your King in check!");
					}
				}
				//Try attacking moves
				for(int[] move: selectedField.getPieceOnTop().calculateAttackingMoves(fieldArray)){
					if(destinationField.row == move[0] && destinationField.col == move[1] ){
						 if(selectedField.getPieceOnTop() instanceof King){
							 //Attacking black king onto a field that is covered
							 if(destinationField.canBlackKingMove == false && selectedField.getPieceOnTop().player.colour==Player.Colour.BLACK){
								 throw new InvalidMoveException(Utils.revealedB);
							 }
							 //Attacking white king onto a field that is covered
							 else if(destinationField.canWhiteKingMove == false && selectedField.getPieceOnTop().player.colour==Player.Colour.WHITE){
								 throw new InvalidMoveException(Utils.revealedW);
							 }
						 }
						//Attacking a valid field
						if(selectedField.movePieceOnTop(destinationField,fieldArray)){
							toggleTurn();
				    		selectedField = null;
				    		destinationField = null;
				    		deselectAllFields();
				    		return;
						}
						else throw new InvalidMoveException("You can't leave your King in check!");
					}
				}
				if(Utils.enpassant(selectedField, destinationField, fieldArray)) toggleTurn();
				else if(Utils.castle(selectedField, destinationField, fieldArray)) toggleTurn();
	    		selectedField = null;
	    		destinationField = null;
	    		deselectAllFields();
			}
			catch (InvalidMoveException e){
				//e.printStackTrace();
				setDescription(e.getMessage(),2);
	    		selectedField = null;
	    		destinationField = null;
	    		deselectAllFields();
	    		return;
			}
		}
		Utils.checkKingMoves(fieldArray);
		Utils.check(fieldArray, turn);
		Utils.stalemate(fieldArray,turn);
		drawBoard();
	}
	public Scene getBoard(){
		return background;
	}
	public static void resetDescription(){
		textHelper2.setText(" ");
		textHelper3.setText(" ");
		frame = 0;
	}
	public static void setDescription(String msg, int textBoxIndex){
		if(textBoxIndex == 2){
			frame = 0;
			textHelper2.setText(msg);
		}
		else if(textBoxIndex == 3){
			frame = 0;
			textHelper3.setText(msg);
		}
		else{
			textHelper4.setText(msg);
		}
	}
	public Canvas getCanvas(){
		return canvas;
	}
	public void toggleTurn(){
		Utils.promotePawns(fieldArray, turn);
		drawBoard();
		for(Field[] row : fieldArray){
			for(Field field : row){
				if(field.getPieceOnTop() != null && field.getPieceOnTop() instanceof Pawn && field.getPieceOnTop().player.colour != turn.colour){
					((Pawn)field.getPieceOnTop()).enpassant = false;
					//System.out.println("enpassant reset for player " + field.getPieceOnTop().player.colour.toString());
				}
			}
		}
		turn = turn==player1? player2 : player1;
		drawBoard();
	}
	public void deselectAllFields(){
		for(Field[] row : fieldArray){
			for(Field field : row){
				field.isFocused = false;
				field.isPossible = false;
				field.isAttacked = false;
			}
		}

	}
}

