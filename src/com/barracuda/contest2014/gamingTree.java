package com.barracuda.contest2014;

import java.util.PriorityQueue;

public class gamingTree {
	public int msgid;
	public String request;
	public int game_id;
	public GameState game_state;
	static int boardsize=10;
	
	public gamingTree(MoveRequestMessage reqmsg){	
		msgid=reqmsg.id;
		request=reqmsg.request;
		game_id=reqmsg.game_id;
		game_state=reqmsg.state;
	}
	

	
	void dfsMoves(int[][][] board, boolean meOrNot, int movepid, int moveptoken, int opptoken, int depth){
		//make move
		int[][][] availpoints = Utils.allTetrahedron(board,movepid);
		int[] tmpmov=new int[3];
		//wait
		for (int i=0;i<boardsize;i++)
			for (int j=0;i+j<boardsize;j++)
				for (int k=0;i+j+k<boardsize && k<moveptoken;k++){
					if (availpoints[i][j][k]>0){
						tmpmov[0]=i;tmpmov[1]=j;tmpmov[2]=k;
						Utils.updateBoard(board, tmpmov, movepid);
						if (meOrNot)
						dfsMoves(board, false, Utils.getOpPlayerid(movepid), opptoken, moveptoken-(k+1), depth+1);
						else 
							dfsMoves(board, true, Utils.getOpPlayerid(movepid), opptoken, moveptoken-(k+1), depth);
					
					}
				}
	}
	
	void printMove(int[] move){
		System.out.println("("+move[0]+","+move[1]+","+move[2]+")\n");
	}
	
	PlayerMessage getBestNexttMove(){
		System.out.println("here1");
		int[][][] tempboard=Utils.copyBoard(game_state.board);
		System.out.println("here");
		
		dfsMoves(tempboard, true, game_state.player, game_state.tokens, game_state.opponent_tokens, 0);
		
        for (int i=0;i<game_state.legal_moves.length;i++)
        	printMove(game_state.legal_moves[i]);
		
		if (Math.random() < 0.5) {
			return new PlayerWaitMessage(msgid);
		}
		else {
			int i = (int)(Math.random() * game_state.legal_moves.length);
			return new PlayerMoveMessage(msgid, game_state.legal_moves[i]);
		}
		
	}

}
