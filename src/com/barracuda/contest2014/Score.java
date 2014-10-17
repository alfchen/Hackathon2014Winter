package com.barracuda.contest2014;

public class Score {
	public int player1;
	public int player2;

	public Score(int player1, int player2) {
		this.player1 = player1;
		this.player2 = player2;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("\t\tScore:\n");
		sb.append("\t\t\tplayer1: " + player1 + "\n");
		sb.append("\t\t\tplayer2: " + player2 + "\n");
		return sb.toString();
	}
}
