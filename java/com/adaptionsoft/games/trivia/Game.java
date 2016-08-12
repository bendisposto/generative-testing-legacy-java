package com.adaptionsoft.games.trivia;

import java.util.ArrayList;
import java.util.LinkedList;

public class Game {

	ArrayList<Player> players = new ArrayList<>();
	int currentPlayer = 0;

	private Player getCurrentPlayer() {
		return players.get(currentPlayer);
	}

	LinkedList popQuestions = new LinkedList();
	LinkedList scienceQuestions = new LinkedList();
	LinkedList sportsQuestions = new LinkedList();
	LinkedList rockQuestions = new LinkedList();

	boolean isGettingOutOfPenaltyBox;

	public Game() {
		for (int i = 0; i < 50; i++) {
			popQuestions.addLast("Pop Question " + i);
			scienceQuestions.addLast(("Science Question " + i));
			sportsQuestions.addLast(("Sports Question " + i));
			rockQuestions.addLast(createRockQuestion(i));
		}
	}

	public String createRockQuestion(int index) {
		return "Rock Question " + index;
	}

	public boolean isPlayable() {
		return (howManyPlayers() >= 2);
	}

	public boolean add(String playerName) {

		players.add(new Player(playerName));

		if (players.size() > 5)
			throw new ArrayIndexOutOfBoundsException("" + players.size());
		System.out.println(playerName + " was added");
		System.out.println("They are player number " + players.size());
		return true;
	}

	public int howManyPlayers() {
		return players.size();
	}

	public void roll(int roll) {
		System.out.println(players.get(currentPlayer) + " is the current player");
		System.out.println("They have rolled a " + roll);

		if (getCurrentPlayer().isInPenaltyBox()) {
			if (roll % 2 != 0) {
				isGettingOutOfPenaltyBox = true;

				System.out.println(players.get(currentPlayer) + " is getting out of the penalty box");
				getCurrentPlayer().advance(roll);

				System.out.println(players.get(currentPlayer)
						+ "'s new location is "
						+ getCurrentPlayer().getPlace());
				System.out.println("The category is " + currentCategory());
				askQuestion();
			} else {
				System.out.println(players.get(currentPlayer) + " is not getting out of the penalty box");
				isGettingOutOfPenaltyBox = false;
			}

		} else {

			getCurrentPlayer().advance(roll);

			System.out.println(players.get(currentPlayer)
					+ "'s new location is "
					+ getCurrentPlayer().getPlace());
			System.out.println("The category is " + currentCategory());
			askQuestion();
		}

	}

	private void askQuestion() {
		if (currentCategory() == "Pop")
			System.out.println(popQuestions.removeFirst());
		if (currentCategory() == "Science")
			System.out.println(scienceQuestions.removeFirst());
		if (currentCategory() == "Sports")
			System.out.println(sportsQuestions.removeFirst());
		if (currentCategory() == "Rock")
			System.out.println(rockQuestions.removeFirst());
	}

	private String currentCategory() {
		if (getCurrentPlayer().getPlace() == 0)
			return "Pop";
		if (getCurrentPlayer().getPlace() == 4)
			return "Pop";
		if (getCurrentPlayer().getPlace() == 8)
			return "Pop";
		if (getCurrentPlayer().getPlace() == 1)
			return "Science";
		if (getCurrentPlayer().getPlace() == 5)
			return "Science";
		if (getCurrentPlayer().getPlace() == 9)
			return "Science";
		if (getCurrentPlayer().getPlace() == 2)
			return "Sports";
		if (getCurrentPlayer().getPlace() == 6)
			return "Sports";
		if (getCurrentPlayer().getPlace() == 10)
			return "Sports";
		return "Rock";
	}

	public boolean wasCorrectlyAnswered() {
		if (getCurrentPlayer().isInPenaltyBox()) {
			if (isGettingOutOfPenaltyBox) {
				System.out.println("Answer was correct!!!!");
				getCurrentPlayer().correctAnswer();
				System.out.println(players.get(currentPlayer)
						+ " now has "
						+ getCurrentPlayer().getPurse()
						+ " Gold Coins.");

				boolean winner = didPlayerWin();
				currentPlayer++;
				if (currentPlayer == players.size())
					currentPlayer = 0;

				return winner;
			} else {
				currentPlayer++;
				if (currentPlayer == players.size())
					currentPlayer = 0;
				return true;
			}

		} else {
			System.out.println("Answer was corrent!!!!");
			getCurrentPlayer().correctAnswer();
			System.out.println(players.get(currentPlayer)
					+ " now has "
					+ getCurrentPlayer().getPurse()
					+ " Gold Coins.");

			boolean winner = didPlayerWin();
			currentPlayer++;
			if (currentPlayer == players.size())
				currentPlayer = 0;

			return winner;
		}
	}

	public boolean wrongAnswer() {
		System.out.println("Question was incorrectly answered");
		System.out.println(players.get(currentPlayer) + " was sent to the penalty box");
		getCurrentPlayer().sendToPenaltyBox();

		currentPlayer++;
		if (currentPlayer == players.size())
			currentPlayer = 0;
		return true;
	}

	private boolean didPlayerWin() {
		return !(getCurrentPlayer().getPurse() == 6);
	}
}
