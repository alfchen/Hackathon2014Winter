package com.barracuda.contest2014;

public class GreetingMessage extends Message {
	public class SubMessage {
		public int team_id;
		public String welcome;
	}
	public SubMessage state = new SubMessage();

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Greeting Message:\n");
		sb.append("\tteam_id: " + state.team_id + "\n");
		sb.append("\twelcome: " + state.welcome + "\n");
		return sb.toString();
	}

}
