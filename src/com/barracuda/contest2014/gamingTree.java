package com.barracuda.contest2014;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.PriorityQueue;

public class gamingTree {
	public int msgid;
	public String request;
	public int game_id;
	public GameState game_state;
	static int boardsize=10;
	int mypid;
	int maxdepth=0;
	PlayerMessage resmsg;
	long beginfulldfs;
	boolean willGoDeeper;
	static int madeMoves=0;
	double MAXSCORE=1000;
	double MINSCORE=-1000;
	
	public void setGamingTree(MoveRequestMessage reqmsg){	
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
	
	
	 int getAvailpoints(int[][][] board, int movepid, int[][][] resavail){
	//	int[][][] resavail = new int[boardsize][boardsize][boardsize];
		 int availcnt=0;

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
							availcnt++;
						}
						else {
						   if ((board[i][j][k-1]==movepid || resavail[i][j][k-1]==1)
						     && (board[i+1][j][k-1]==movepid || resavail[i+1][j][k-1]==1)
						     && (board[i][j+1][k-1]==movepid || resavail[i][j+1][k-1]==1)
								   ){
							   resavail[i][j][k]=1;		
							   availcnt++;
						   }
						}						
					}
				}
		return availcnt;
		
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

	void setDepth(int availnum, int depth){
		int delta=0;
	//	if (availnum<50)
	//		maxdepth=1;
	//	if (availnum<10)
	//		maxdepth=2;
	//	maxdepth=depth+delta;
	}
	
	int estimateRemainingMoves(int availnum){
		int res=availnum;
		
		int lowerbound=0;
		if (madeMoves<27)
			lowerbound=27-madeMoves;
		if (lowerbound<availnum)
		  res=(lowerbound+availnum)/2;
		return res;
	}
	
	void setWillGoDeeper(int depth, int availnum, double usedfactor){
		if (!willGoDeeper) return;
		
		if (madeMoves<5) willGoDeeper=false;
		
		long usedtime=((new Date()).getTime()-beginfulldfs);
		long lefttime=-1;
		int rm=estimateRemainingMoves(availnum);
		if (rm>0)
			lefttime=game_state.time_remaining_ns/rm;
	//	System.out.println(usedtime+" "+lefttime+" "+rm+" "+availnum);
		if (!(lefttime==-1 || usedtime*1000000*(1.25+usedfactor) < lefttime)){
			willGoDeeper=false;
		}
		
		if (depth>5)
			willGoDeeper=false;
	
	}
	
	double getArrayListAverage(ArrayList<Double> arrd){
		int sum=0;
		for (int i=0;i<arrd.size();i++){
			sum+=arrd.get(i);
		}
		return sum/arrd.size();		
	}
	
	class BfsStatus{
		int[][][] board;
		boolean meOrNot;
        int movepid;
        int moveptoken;
        int opptoken;
        int depth;
        int branch;
        
        public BfsStatus(int[][][] mboard, boolean mmeOrNot, int mmovepid, int mmoveptoken, int mopptoken, int mdepth, int mbranch){
        	board=mboard;
        	meOrNot=mmeOrNot;
        	movepid=mmovepid;
            moveptoken=mmoveptoken;
            opptoken=mopptoken;
            depth=mdepth;      
            branch=mbranch;
        }
	};
	
	void bfsMoves(int[][][] gboard, boolean gmeOrNot, int gmovepid, int gmoveptoken, int gopptoken, int gdepth){		
		ArrayDeque<BfsStatus> upque=new ArrayDeque<BfsStatus>();
		BfsStatus firstmove=new BfsStatus(gboard, gmeOrNot, gmovepid, gmoveptoken, gopptoken, gdepth, -1);
		
		
		upque.add(firstmove);
		ArrayList<ArrayList<Double>> movewinsum=new ArrayList<ArrayList<Double>>();
		ArrayList<Double> waitwinsum=new ArrayList<Double>();
		ArrayList<int[]> moves=new ArrayList<int[]>();
		int handlednum=-1;
		
		while (!upque.isEmpty()){
			BfsStatus topmove=upque.poll();
			handlednum++;
			
			int[][][] board=topmove.board;
			boolean meOrNot=topmove.meOrNot;
			int movepid=topmove.movepid;
			int moveptoken=topmove.moveptoken;
			int opptoken=topmove.opptoken;
			int depth=topmove.depth;    
			int branch=topmove.branch;    
			
			long begindfst=(new Date()).getTime();
			int[][][] availpoints = new int[boardsize][boardsize][boardsize];
		//	System.out.println("here0");
			int availnum=getAvailpoints(board,movepid, availpoints);
		//	setDepth(depth, availnum);
			if (availnum==0){
			    //game over!
					int iw=amiwin(board);
					double score=0;
					if (iw>0)
						score=MAXSCORE;
					if (iw==0)
						score=(MINSCORE+MAXSCORE)/2;
					if (iw<0)				
						score=MINSCORE;
					if (branch==-2){
						  //wait
						  waitwinsum.add(score);
					  }
					  else {
						  (movewinsum.get(branch)).add(score);
					  }
				}
			
			
			if (!willGoDeeper){	
				if (meOrNot){
				double rest=(new Strategy()).boardEvaluation(board, mypid, moveptoken, opptoken, true);
	    		  if (branch==-2){
					  //wait
					  waitwinsum.add(rest);
				  }
				  else {
					  (movewinsum.get(branch)).add(rest);
				  }
				}
				else {
					double rest=(new Strategy()).boardEvaluation(board, mypid, moveptoken, opptoken, false);
					 if (branch==-2){
						  //wait
						  waitwinsum.add(rest);
					  }
					  else {
						  (movewinsum.get(branch)).add(rest);
					  }
					
				}
				continue;
			}
			
			

			
			

		//	System.out.println("here1");
			
			
			boolean hasavailpoint=false;
			int wincnt=0;
			for (int i=0;i<boardsize;i++)
				for (int j=0;i+j<boardsize;j++)
					for (int k=0;i+j+k<boardsize && k<moveptoken;k++){
						if (availpoints[i][j][k]>0){
							double usedfactor=0;
						//	if (handlednum>0) usedfactor=upque.size()*1.0/handlednum;
							setWillGoDeeper(depth,availnum,usedfactor);
							int[][][] tmpboard=new int[boardsize][boardsize][boardsize];
							hasavailpoint=true;
							copyBoard(board,tmpboard);
							int[] tmpmov=new int[3];
							tmpmov[0]=i;tmpmov[1]=j;tmpmov[2]=k;
						//	System.out.println("here11");
							updateBoard(tmpboard, tmpmov, movepid);
						//	System.out.println("here12");
							double rest=0;
							int subbranch=branch;
							if (branch==-1){
								subbranch=wincnt;
								ArrayList<Double> branchmv=new ArrayList<Double>();
								movewinsum.add(branchmv);
								moves.add(tmpmov);
							}
							if (meOrNot){
							  if (willGoDeeper){								  
								  BfsStatus nxtmove=new BfsStatus(tmpboard, false, Utils.getOpPlayerid(movepid), opptoken+1, moveptoken-(k+1), depth+1, subbranch);
								  upque.add(nxtmove);
							  }
							  else {		
								  rest=(new Strategy()).boardEvaluation(tmpboard, mypid, moveptoken-(k+1), opptoken+1, false);
								  if (subbranch==-2){
									  //wait
									  waitwinsum.add(rest);
								  }
								  else {
									  (movewinsum.get(subbranch)).add(rest);
								  }
								  								 
							//	  System.out.println("Yihua "+rest);
							//	  rest=(new Strategy()).simpleBoardEvaluation(tmpboard, mypid, moveptoken-(k+1), opptoken+1, false);
							  }
							}
						    else {		
						    	if (willGoDeeper){
						    		BfsStatus nxtmove=new BfsStatus(tmpboard, true, Utils.getOpPlayerid(movepid), opptoken+1, moveptoken-(k+1), depth+1, subbranch);
						    		upque.add(nxtmove);
						    	}
						    	else{
						    		rest=(new Strategy()).boardEvaluation(tmpboard, mypid, opptoken+1, moveptoken-(k+1), true);
						    		if (subbranch==-2){
										  //wait
										  waitwinsum.add(rest);
									  }
									  else {
										  (movewinsum.get(subbranch)).add(rest);
									  }
						    		//	  System.out.println("Yihua "+rest);
									//	  rest=(new Strategy()).simpleBoardEvaluation(tmpboard, mypid, moveptoken-(k+1), opptoken+1, true);
						    	}
						    }
						//	if (k==1){
						//		System.out.println("!!!!k=1 "+rest);
						//	}
						//	winsum.add(rest);
							
							wincnt++;
						}
					}

		//	System.out.println("here2");
			
			
			//wait
			double usedfactor=0;
	//		if (handlednum>0) usedfactor=upque.size()*1.0/handlednum;
			setWillGoDeeper(depth,availnum,usedfactor);
			int subbranch=branch;
			if (branch==-1){
				subbranch=-2;
			}

			double waitwin=0;
			
			if (meOrNot){
				if (willGoDeeper){
					BfsStatus nxtmove=new BfsStatus(board, false, Utils.getOpPlayerid(movepid), opptoken+1, moveptoken, depth+1, subbranch);
					upque.add(nxtmove);
				}
				else{
					waitwin=(new Strategy()).boardEvaluation(board, mypid, moveptoken, opptoken+1, false);
					if (subbranch==-2){
						  //wait
						  waitwinsum.add(waitwin);
					  }
					  else {
						  (movewinsum.get(subbranch)).add(waitwin);
					  }
				//	waitwin=(new Strategy()).simpleBoardEvaluation(tmpboard, mypid, moveptoken, opptoken+1, false);
				}
			}
			else {
				if (willGoDeeper){
					BfsStatus nxtmove=new BfsStatus(board, true, Utils.getOpPlayerid(movepid), opptoken+1, moveptoken, depth+1, subbranch);
					upque.add(nxtmove);
				}
				else {
					waitwin=(new Strategy()).boardEvaluation(board, mypid, opptoken+1, moveptoken, true);
					if (subbranch==-2){
						  //wait
						  waitwinsum.add(waitwin);
					  }
					  else {
						  (movewinsum.get(subbranch)).add(waitwin);
					  }
				//	waitwin=(new Strategy()).simpleBoardEvaluation(tmpboard, mypid, opptoken+1, moveptoken, true);
				}
			}			
			
			long enddfst=(new Date()).getTime();
			System.out.println("depth "+depth+" avail#: "+availnum+" dfs one round: "+(enddfst-begindfst));
		}
		
		//make decision
		
		double sum=0;
		boolean inside=false;
		double max=-1;
		int[] maxmove=game_state.legal_moves[0];
		for (int i=0;i<movewinsum.size();i++){
			double avei=getArrayListAverage(movewinsum.get(i));
	//		System.out.println("sum!!!!k="+(moves.get(i))[2]+" "+avei);
			sum+=avei;
		//	if (!inside || ((moves.get(i))[2] > maxmove[2]) || (((moves.get(i))[2] <= maxmove[2]) && (winsum.get(i) > max))){
			if (!inside || avei > max){
     			inside=true;
				max=avei;
				maxmove=moves.get(i);
			}
		}
		
		double waitwin=getArrayListAverage(waitwinsum);
		
			System.out.println("claim max prob: "+max);
			System.out.println("wait prob: "+waitwin);
			if (!inside || max<waitwin){
		    //need to wait
				resmsg=new PlayerWaitMessage(msgid);
				System.out.println("[Decision] wait!");
			}
			else {
				//need to claim point
				resmsg=new PlayerMoveMessage(msgid, maxmove);
				System.out.println("[Decision] claim point! z:"+maxmove[2]);
			}
		
	}
	
	double dfsMoves(int[][][] board, boolean meOrNot, int movepid, int moveptoken, int opptoken, int depth){
		//make move
		long begindfst=(new Date()).getTime();
		System.out.println("begin "+depth);
		int[][][] availpoints = new int[boardsize][boardsize][boardsize];
		int availnum=getAvailpoints(board,movepid, availpoints);
	//	setDepth(depth, availnum);
		setWillGoDeeper(depth,availnum,0);
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
						  if (willGoDeeper){
							  rest=dfsMoves(tmpboard, false, Utils.getOpPlayerid(movepid), opptoken+1, moveptoken-(k+1), depth+1);
						  }
						  else {							  
							  rest=(new Strategy()).boardEvaluation(tmpboard, mypid, moveptoken-(k+1), opptoken+1, false);
						//	  System.out.println("Yihua "+rest);
						//	  rest=(new Strategy()).simpleBoardEvaluation(tmpboard, mypid, moveptoken-(k+1), opptoken+1, false);
						  }
						}
					    else {		
					    	if (willGoDeeper){
					    	rest=dfsMoves(tmpboard, true, Utils.getOpPlayerid(movepid), opptoken+1, moveptoken-(k+1), depth+1);
					    	}
					    	else{
					    		rest=(new Strategy()).boardEvaluation(tmpboard, mypid, opptoken+1, moveptoken-(k+1), true);
								//	  System.out.println("Yihua "+rest);
								//	  rest=(new Strategy()).simpleBoardEvaluation(tmpboard, mypid, moveptoken-(k+1), opptoken+1, true);
					    	}
					    }
						if (k==1){
							System.out.println("!!!!k=1 "+rest);
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
				return MAXSCORE;
			if (iw==0)
				return (MINSCORE+MAXSCORE)/2;
			return MINSCORE;
		}

		double waitwin=0;
		//wait
		if (meOrNot){
			if (willGoDeeper){
			waitwin=dfsMoves(board, false, Utils.getOpPlayerid(movepid), opptoken+1, moveptoken, depth+1);
			}
			else{
				waitwin=(new Strategy()).boardEvaluation(board, mypid, moveptoken, opptoken+1, false);
			//	waitwin=(new Strategy()).simpleBoardEvaluation(tmpboard, mypid, moveptoken, opptoken+1, false);
			}
		}
		else {
			if (willGoDeeper){
			waitwin=dfsMoves(board, true, Utils.getOpPlayerid(movepid), opptoken+1, moveptoken, depth+1);
			}
			else {
				waitwin=(new Strategy()).boardEvaluation(board, mypid, opptoken+1, moveptoken, true);
			//	waitwin=(new Strategy()).simpleBoardEvaluation(tmpboard, mypid, opptoken+1, moveptoken, true);
			}
		}
		
		
		double sum=0;
		boolean inside=false;
		double max=-1;
		int[] maxmove=game_state.legal_moves[0];
		for (int i=0;i<winsum.size();i++){
			System.out.println("sum!!!!k="+(moves.get(i))[2]+" "+winsum.get(i));
			sum+=winsum.get(i);
		//	if (!inside || ((moves.get(i))[2] > maxmove[2]) || (((moves.get(i))[2] <= maxmove[2]) && (winsum.get(i) > max))){
			if (!inside || (winsum.get(i) > max)){
     			inside=true;
				max=winsum.get(i);
				maxmove=moves.get(i);
			}
		}
		
	
		
		if (depth==0 && meOrNot){
		//decision time!

			System.out.println("claim max prob: "+max);
			System.out.println("wait prob: "+waitwin);
			if (!inside || max<waitwin){
		    //need to wait
				resmsg=new PlayerWaitMessage(msgid);
				System.out.println("[Decision] wait!");
			}
			else {
				//need to claim point
				resmsg=new PlayerMoveMessage(msgid, maxmove);
				System.out.println("[Decision] claim point! z:"+maxmove[2]);
			}
		}
		
		System.out.println("wait!!!! "+waitwin);
		sum+=waitwin;
		double average=sum/(winsum.size()+1);	
		System.out.println("average!!!! "+average);
		
		long enddfst=(new Date()).getTime();
		System.out.println("depth "+depth+" avail#: "+availnum+" dfs one round: "+(enddfst-begindfst));
		
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
		
		beginfulldfs=(new Date()).getTime();
		willGoDeeper=true;
	//	dfsMoves(tempboard, true, game_state.player, game_state.tokens, game_state.opponent_tokens, 0);
		bfsMoves(tempboard, true, game_state.player, game_state.tokens, game_state.opponent_tokens, 0);
		long enddfst=(new Date()).getTime();
		System.out.println("dfs: "+(enddfst-beginfulldfs));
		
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
