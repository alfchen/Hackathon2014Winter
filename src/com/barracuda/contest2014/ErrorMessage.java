package com.barracuda.contest2014;

public class ErrorMessage extends Message {
	public class SubMessage {
		public String error;
		public String seen_host;
	}
	public SubMessage state = new SubMessage();

	@Override
	public String toString() {
		return "Error: " + state.error + "\n";
	}
}
