package application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;



public class Game extends Application {
	static Board board;
	public Stage stage;
	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			primaryStage.setTitle("Chess");
			board = new Board(new Player("Player one", Player.Colour.WHITE), new Player("Player two", Player.Colour.BLACK));
			primaryStage.setScene(board.getBoard());
			primaryStage.setResizable(false);
			primaryStage.show();
			board.getCanvas().addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent mouseEvent) {
			    	Utils.checkKingMoves(board.fieldArray);
			    	//Utils.check(board.fieldArray, board.turn);
			    	board.drawBoard();
			    	try{
				    	int col =  (((int)mouseEvent.getX())/Utils.FIELD_SIZE);
				    	int row = (((int)mouseEvent.getY())/Utils.FIELD_SIZE);
				        //System.out.println("mouse click detected! " +  "x = " + mouseEvent.getX() + ", y = " + mouseEvent.getY() +", row = " + row + ", col = " + col );
				        if(board.selectedField == null){
				        	board.selectedField = board.fieldArray[row][col];
				        	if(board.selectedField.getPieceOnTop() != null && board.selectedField.getPieceOnTop().player != board.turn){
				        		System.out.println(board.selectedField.getPieceOnTop().player.name);
				        		throw new InvalidMoveException("Its not your turn!");
				        	}
				        	board.drawBoard();
				        	board.selectedField.toggleSelection();
				        	board.drawBoard();
					        board.selectedField.showPossibleMoves(board.fieldArray);
					        board.drawBoard();
				        }
				        else if(board.selectedField != null && board.destinationField == null){
				        	board.destinationField = board.fieldArray[row][col];
				        	board.checkPlayersPieces();
				        	board.handleMoves();
				        	board.drawBoard();
				        }
			    	}
			    	catch(InvalidMoveException e){
			    		board.selectedField = null;
			    		board.destinationField = null;
			    		Board.setDescription(e.getMessage(),2);
			    		board.deselectAllFields();
			    		board.drawBoard();
			    	}
			    	finally{
			    		board.checkPlayersPieces();
			    		Utils.checkKingMoves(board.fieldArray);
			    		if(board.turn.isChecked)Utils.checkMate(board.fieldArray, board.turn);

			    	}
			    }
			});
			new AnimationTimer()
			    {
			        public void handle(long arg)
			        {
			        	Board.frame++;
			        	if(Board.frame == 100){
			        		Board.resetDescription();
			        	}
			        }
			    }.start();

		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);

		}

	}

