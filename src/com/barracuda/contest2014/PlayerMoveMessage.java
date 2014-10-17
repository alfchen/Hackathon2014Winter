package com.barracuda.contest2014;

public class PlayerMoveMessage extends PlayerMessage {
	public int[] location;

	public PlayerMoveMessage(int id, int[] location) {
		super(id);
		this.location = location;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Player Move:\n");
		sb.append("\tid: " + id + "\n");
		sb.append("\ttype: " + type + "\n");
		sb.append("\tlocation: " + location + "\n");
		return sb.toString();
	}
}
