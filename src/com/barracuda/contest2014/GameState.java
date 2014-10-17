package com.barracuda.contest2014;

import java.util.Arrays;

public class GameState {
	public long time_remaining_ns;
	public int moves_remaining;
	public int tokens;
	public int opponent_id;
	public int opponent_tokens;
	public int player;
	public Score score;
	public int[][][] board;
	public int[][] legal_moves;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Game State:\n");
		sb.append("\ttime_remaining_ns: " + time_remaining_ns + "\n");
		sb.append("\tmoves_remaining: " + moves_remaining + "\n");
		sb.append("\ttokens: " + tokens + "\n");
		sb.append("\topponent_id: " + opponent_id + "\n");
		sb.append("\topponent_tokens: " + opponent_tokens + "\n");
		sb.append("\tplayer: " + player + "\n");
		sb.append(score);
		sb.append("\tboard: " + Arrays.deepToString(board) + "\n");
		sb.append("\tlegal_moves: " + Arrays.deepToString(legal_moves) + "\n");
		return sb.toString();
	}
}
