package application;

import java.util.ArrayList;
import java.util.List;

public class Player {

	public enum Colour {BLACK,WHITE};
	public Colour opponent;
	public String name;
	public Colour colour;
	public final String turnNotification;
	public boolean isChecked = false;
	private List<Piece> yourPieces = new ArrayList<>();

	public Player(String name, Colour colour){
		this.name = name;
		this.colour = colour;
		this.opponent = (this.colour == Colour.WHITE? Colour.BLACK:Colour.WHITE);
		this.turnNotification = (this.colour == Colour.WHITE ? Utils.turnW : Utils.turnB);
	}

	public void addPieces(Field[][] fieldArray){
		yourPieces.clear();
		for(Field[] row : fieldArray){
			for(Field field : row){
				if(field.getPieceOnTop() != null && field.getPieceOnTop().player.colour == this.colour){
					yourPieces.add(field.getPieceOnTop());
				}
			}
		}
	}
	public List<Piece> getPieces(){
		return this.yourPieces;
	}

}
