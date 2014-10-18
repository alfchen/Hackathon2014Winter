package com.barracuda.contest2014;

public class gamingTree {
	public int msgid;
	public String request;
	public int game_id;
	public GameState game_state;
	public gamingTree(MoveRequestMessage reqmsg){	
		msgid=reqmsg.id;
		request=reqmsg.request;
		game_id=reqmsg.game_id;
		game_state=reqmsg.state;
	}

	
	void dfsMoves(int depth){
		
	}
	
	PlayerMessage getBestNexttMove(){
		dfsMoves(game_state.board, true, game_state.player, game_state.tokens, Utils.getOpPlayerid(game_state.player), game_state.opponent_tokens, 0);
		if (Math.random() < 0.5) {
			return new PlayerWaitMessage(msgid);
		}
		else {
			int i = (int)(Math.random() * game_state.legal_moves.length);
			return new PlayerMoveMessage(msgid, game_state.legal_moves[i]);
		}
		
	}

}
