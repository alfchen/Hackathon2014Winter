package com.barracuda.contest2014;

public abstract class PlayerMessage extends Message {

	public PlayerMessage(int id) {
		this.id = id;
		type = "move";
	}

	@Override
	public abstract String toString();
}
