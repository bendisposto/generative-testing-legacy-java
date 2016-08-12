package com.adaptionsoft.games.trivia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Game {

	ArrayList<Player> players = new ArrayList<>();
	int currentPlayer = 0;

	private Player getCurrentPlayer() {
		// FIXME remove this, it is only present to simulate array in the
		// original version
		if (currentPlayer < 6 && players.size() <= currentPlayer)
			return new Player("");
		return players.get(currentPlayer);
	}

	Map<String, LinkedList<String>> questions = new HashMap<>();

	boolean isGettingOutOfPenaltyBox;

	public Game() {
		initializeQuestions();
	}

	private void initializeQuestions() {
		LinkedList<String> popQuestions = new LinkedList<>();
		LinkedList<String> scienceQuestions = new LinkedList<>();
		LinkedList<String> sportsQuestions = new LinkedList<>();
		LinkedList<String> rockQuestions = new LinkedList<>();
		for (int i = 0; i < 50; i++) {
			popQuestions.addLast("Pop Question " + i);
			scienceQuestions.addLast(("Science Question " + i));
			sportsQuestions.addLast(("Sports Question " + i));
			rockQuestions.addLast(createRockQuestion(i));
		}
		questions.put("Pop", popQuestions);
		questions.put("Science", scienceQuestions);
		questions.put("Sports", sportsQuestions);
		questions.put("Rock", rockQuestions);
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
		printCurrentPlayer();
		printRoll(roll);

		if (getCurrentPlayer().isInPenaltyBox()) {
			if (isOdd(roll)) {
				isGettingOutOfPenaltyBox = true;
				printIsGettingFree();
			} else {
				printIsNotGettingFree();
				isGettingOutOfPenaltyBox = false;
				return;
			}

		}
		advanceAndAskQuestion(roll);

	}

	private void printIsNotGettingFree() {
		System.out.println(players.get(currentPlayer) + " is not getting out of the penalty box");
	}

	private void printIsGettingFree() {
		System.out.println(players.get(currentPlayer) + " is getting out of the penalty box");
	}

	private void advanceAndAskQuestion(int roll) {
		getCurrentPlayer().advance(roll);

		System.out.println(players.get(currentPlayer)
				+ "'s new location is "
				+ getCurrentPlayer().getPlace());
		System.out.println("The category is " + currentCategory());
		askQuestion();
	}

	private boolean isOdd(int roll) {
		return roll % 2 != 0;
	}

	private void printRoll(int roll) {
		System.out.println("They have rolled a " + roll);
	}

	private void printCurrentPlayer() {
		System.out.println(players.get(currentPlayer) + " is the current player");
	}

	private void askQuestion() {
		System.out.println(questions.get(currentCategory()).removeFirst());
	}

	private String currentCategory() {
		String[] results = { "Pop", "Science", "Sports", "Rock" };
		int place = getCurrentPlayer().getPlace() % results.length;
		return results[place];
	}

	public boolean wasCorrectlyAnswered() {
		boolean inPenaltyBox = getCurrentPlayer().isInPenaltyBox();
		boolean result = true;
		if (!inPenaltyBox || isGettingOutOfPenaltyBox) {
			printCorrectAnswer(inPenaltyBox);
			result = correctAnswer();
		}
		nextPlayer();
		return result;
	}

	private void printCorrectAnswer(boolean inPenaltyBox) {
		// FIXME remove this if-then-else, it is only here because of the
		// typo in the original code
		if (inPenaltyBox)
			System.out.println("Answer was correct!!!!");
		else
			System.out.println("Answer was corrent!!!!");
	}

	private boolean correctAnswer() {
		getCurrentPlayer().correctAnswer();
		printGold();
		return didPlayerWin();
	}

	private void printGold() {
		System.out.println(players.get(currentPlayer)
				+ " now has "
				+ getCurrentPlayer().getPurse()
				+ " Gold Coins.");
	}

	public boolean wrongAnswer() {
		System.out.println("Question was incorrectly answered");
		System.out.println(players.get(currentPlayer) + " was sent to the penalty box");
		getCurrentPlayer().sendToPenaltyBox();

		nextPlayer();
		return true;
	}

	private void nextPlayer() {
		currentPlayer++;
		if (currentPlayer == players.size())
			currentPlayer = 0;
	}

	private boolean didPlayerWin() {
		return !(getCurrentPlayer().getPurse() == 6);
	}
}
