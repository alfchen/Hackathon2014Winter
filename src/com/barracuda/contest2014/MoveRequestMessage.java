package com.barracuda.contest2014;

public class MoveRequestMessage extends Message {
	public String request;
	public int game_id;
	public GameState state;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Move Request:\n");
		sb.append("\tid: " + id + "\n");
		sb.append("\ttype: " + type + "\n");
		sb.append("\tgame_id: " + game_id + "\n");
		sb.append(state);
		return sb.toString();
	}
}
