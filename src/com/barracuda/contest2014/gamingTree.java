package com.barracuda.contest2014;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class gamingTree {
	public int msgid;
	public String request;
	public int game_id;
	public GameState game_state;
	static int boardsize=10;
	int mypid;
	static int maxdepth=0;
	PlayerMessage resmsg;
	
	public gamingTree(MoveRequestMessage reqmsg){	
		msgid=reqmsg.id;
		request=reqmsg.request;
		game_id=reqmsg.game_id;
		game_state=reqmsg.state;
	}
	
	void copyBoard(int[][][] curboard, int[][][] resboard){
	//	int[][][] resboard=new int[boardsize][boardsize][boardsize];
		
		for (int i=0;i<boardsize;i++)
			for (int j=0;i+j<boardsize;j++)
				for (int k=0;i+j+k<boardsize;k++)
			//	  if (i+j+k<boardsize){
			//		System.out.println("("+i+","+j+","+k+" -- "+curboard[i][j][k]+")\n");
					    resboard[i][j][k]=curboard[i][j][k];
			//	}		
	};
	
	
	int[][][] getAvailpoints(int[][][] board, int movepid){
		int[][][] resavail = new int[boardsize][boardsize][boardsize];

		for (int i=0;i<boardsize;i++)
			for (int j=0;i+j<boardsize;j++)
				for (int k=0;i+j+k<boardsize;k++){
					resavail[i][j][k] = 0;
				}
		for (int k=0;k<boardsize;k++)
			for (int i=0;i+k<boardsize;i++)
				for (int j=0;i+j+k<boardsize;j++){
					if (board[i][j][k]==0){
						if (k==0){
							resavail[i][j][k]=1;
						}
						else {
						   if ((board[i][j][k-1]==movepid || resavail[i][j][k-1]==1)
						     && (board[i+1][j][k-1]==movepid || resavail[i+1][j][k-1]==1)
						     && (board[i][j+1][k-1]==movepid || resavail[i][j+1][k-1]==1)
								   ){
							   resavail[i][j][k]=1;							   
						   }
						}						
					}
				}
		return resavail;
		
	}
	
	int amiwin(int[][][] board){
		int cntme=0;
		int cntop=0;
		for (int k=0;k<boardsize;k++)
			for (int i=0;i+k<boardsize;i++)
				for (int j=0;i+j+k<boardsize;j++){
					if (board[i][j][k] == mypid){
						cntme++;
					}
					if (board[i][j][k] == Utils.getOpPlayerid(mypid)){
						cntop++;
					}
				}
		if (cntme>cntop)
			return 1;
		if (cntme<cntop)
			return -1;
		return 0;		
	}
	
	void fillTriangle(int[][][] board){
		for (int k=1;k<boardsize;k++)
			for (int i=0;i+k<boardsize;i++)
				for (int j=0;i+j+k<boardsize;j++){
					int id=board[i][j][k-1];
					if ((board[i+1][j][k-1]==id) && (board[i][j+1][k-1]==id)){
						board[i][j][k]=id;
					}
				}
	}
	
	void updateBoard(int[][][] curboard, int[] move, int playerid){

	//	System.out.println("update");
		ArrayDeque<int[]> upque=new ArrayDeque<int[]>();
		upque.add(move);
		while (!upque.isEmpty()){
			int[] topmove=upque.poll();
			curboard[topmove[0]][topmove[1]][topmove[2]]=playerid;
		//	System.out.println("update1 "+topmove[2]);
			if (topmove[2] > 0){
			//	System.out.println("update2 "+topmove[2]);
				int[] l1=new int[3]; l1[0]=topmove[0];l1[1]=topmove[1];l1[2]=topmove[2]-1;			
				int[] l2=new int[3]; l2[0]=topmove[0];l2[1]=topmove[1]+1;l2[2]=topmove[2]-1;
				int[] l3=new int[3]; l3[0]=topmove[0]+1;l3[1]=topmove[1];l3[2]=topmove[2]-1;
				upque.add(l1);
				upque.add(l2);
				upque.add(l3);
			}			
		}	
	//	System.out.println("update end");
	}

	
	double dfsMoves(int[][][] board, boolean meOrNot, int movepid, int moveptoken, int opptoken, int depth){
		//make move
	//	System.out.println("begin "+depth);
		int[][][] availpoints = getAvailpoints(board,movepid);
		int[][][] tmpboard=new int[boardsize][boardsize][boardsize];

	//	System.out.println("here1");
		
		ArrayList<Double> winsum=new ArrayList<Double>();
		ArrayList<int[]> moves=new ArrayList<int[]>();
		boolean hasavailpoint=false;
	//	int wincnt=0;
		for (int i=0;i<boardsize;i++)
			for (int j=0;i+j<boardsize;j++)
				for (int k=0;i+j+k<boardsize && k<moveptoken;k++){
					if (availpoints[i][j][k]>0){
						hasavailpoint=true;
						copyBoard(board,tmpboard);
						int[] tmpmov=new int[3];
						tmpmov[0]=i;tmpmov[1]=j;tmpmov[2]=k;
					//	System.out.println("here11");
						updateBoard(tmpboard, tmpmov, movepid);
					//	System.out.println("here12");
						double rest=0;
						if (meOrNot){
						  if (depth < maxdepth){
							  rest=dfsMoves(tmpboard, false, Utils.getOpPlayerid(movepid), opptoken+1, moveptoken-(k+1), depth);
						  }
						  else {							  
							  rest=(new Strategy()).boardEvaluation(tmpboard, mypid, moveptoken-(k+1), opptoken+1);
							  System.out.println("Yihua "+rest);
						//	  rest=(new Strategy()).simpleBoardEvaluation(tmpboard, mypid, moveptoken-(k+1), opptoken+1);
						  }
						}
					    else {					      
					    	rest=dfsMoves(tmpboard, true, Utils.getOpPlayerid(movepid), opptoken+1, moveptoken-(k+1), depth+1);
						}
						winsum.add(rest);
						moves.add(tmpmov);
					}
				}

	//	System.out.println("here2");
		
		if (!hasavailpoint){
	    //game over!
			int iw=amiwin(board);
			if (iw>0)
				return 1.0;
			if (iw==0)
				return 0.5;
			return 0;
		}

		double waitwin=0;
		//wait
		if (meOrNot){
			if (depth < maxdepth){
			waitwin=dfsMoves(board, false, Utils.getOpPlayerid(movepid), opptoken+1, moveptoken, depth);
			}
			else{
				waitwin=(new Strategy()).simpleBoardEvaluation(tmpboard, mypid, moveptoken, opptoken+1);
			}
		}
		else {
			waitwin=dfsMoves(board, true, Utils.getOpPlayerid(movepid), opptoken+1, moveptoken, depth+1);
		}
		
		double sum=0;
		double max=-1;
		int[] maxmove=game_state.legal_moves[0];
		for (int i=0;i<winsum.size();i++){
			sum+=winsum.get(i);
			if (winsum.get(i) > max){
				max=winsum.get(i);
				maxmove=moves.get(i);
			}
		}
		
	
		
		if (depth==0 && meOrNot){
		//decision time!
			if (max<waitwin){
		    //need to wait
				resmsg=new PlayerWaitMessage(msgid);
				System.out.println("wait!");
			}
			else {
				//need to claim point
				resmsg=new PlayerMoveMessage(msgid, maxmove);
				System.out.println("claim point!");
			}
		}
		
		sum+=waitwin;
		double average=sum/(winsum.size()+1);	
		
		return average;
	}
	
	void printMove(int[] move){
		System.out.println("("+move[0]+","+move[1]+","+move[2]+")\n");
	}
	
	PlayerMessage getBestNexttMove(){
		int[][][] tempboard=Utils.copyBoard(game_state.board);
		fillTriangle(tempboard);
		mypid=game_state.player;

		resmsg=null;
		dfsMoves(tempboard, true, game_state.player, game_state.tokens, game_state.opponent_tokens, 0);
		
		if (resmsg!=null){
			System.out.println("!!!!return!");
		  return resmsg;
		}
		else {
			//random choice!
			System.out.println("bug!!!!!Random choice!!!");
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

}
