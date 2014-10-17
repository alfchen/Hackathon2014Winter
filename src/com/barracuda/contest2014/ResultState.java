package com.barracuda.contest2014;

import java.util.Arrays;

public class ResultState {
	public int[] claimed;
	public int player;
	public Score score;
	public int[][][] board;
	public int tokens;
	public String error;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("\tState:\n");
		if (claimed != null) sb.append("\t\tclaimed: " + Arrays.toString(claimed) + "\n");
		sb.append("\t\tplayer: " + player + "\n");
		sb.append(score);
		sb.append("\t\tboard: " + Arrays.deepToString(board) + "\n");
		sb.append("\t\ttokens: " + tokens + "\n");
		if (error != null)   sb.append("\t\terror: " + error + "\n");
		return sb.toString();
	}

}