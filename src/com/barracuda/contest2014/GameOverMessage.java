package com.barracuda.contest2014;

import java.util.ArrayList;

public class GameOverMessage extends Message {
	public class SubMessage {
		public int winner;
		public Score score;
		public int[][][] board;
	}
	public int game_id;
	public SubMessage state = new SubMessage();

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Game Over:\n");
		sb.append("\tgame_id: " + game_id + "\n");
		sb.append("\tstate:\n");
		sb.append("\t\twinner: " + state.winner + "\n");
		sb.append("\t" + state.score + "\n");
		sb.append("\t\tboard: " + state.board + "\n");
		return sb.toString();
	}

}
