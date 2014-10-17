package com.barracuda.contest2014;

public class PlayerWaitMessage extends PlayerMessage {
	public boolean wait;

	public PlayerWaitMessage(int id) {
		super(id);
		this.wait = true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Player Wait:\n");
		sb.append("\tid: " + id + "\n");
		sb.append("\ttype: " + type + "\n");
		sb.append("\twait: " + wait + "\n");
		return sb.toString();
	}
}
