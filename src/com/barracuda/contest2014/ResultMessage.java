package com.barracuda.contest2014;

public class ResultMessage extends Message {
	public int game_id;
	public ResultState state;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Result Message:\n");
		sb.append("\tgame id: " + game_id + "\n");
		sb.append(state.toString());
		return sb.toString();
	}
}
