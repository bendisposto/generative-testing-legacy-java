package com.adaptionsoft.games.trivia;

public class Player {

	private final String name;
	private int place;
	private int purse;
	private boolean inPenaltyBox;

	public Player(String name) {
		this.name = name;
	}

	public boolean isInPenaltyBox() {
		return inPenaltyBox;
	}

	public void advance(int numberPlaces) {
		place = (place + numberPlaces) % 12; // FIXME magic number
	}

	public int getPlace() {
		return place;
	}

	public void correctAnswer() {
		purse++;
	}

	@Override
	public String toString() {
		return name;
	}

	public int getPurse() {
		return purse;
	}

	public void sendToPenaltyBox() {
		inPenaltyBox = true;
	}

}
