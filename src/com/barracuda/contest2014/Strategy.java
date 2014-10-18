package com.barracuda.contest2014;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;

public class Strategy {
	public final int OP_TOKEN_GAP_THRES = 5;
	public final int OUR_TOKEN_GAP_THRES = 5;
	public final int LIST_TOP_NUM = 2;
	public final int WAIT_GAP = 2;
	public final int OUR_MAX_Z = 4;
	public final static int SLOW_START_THRES = 15;
	public final static int SLOW_START_TOKEN = 5;
	
	public static int decidedZ(int[][][] availPoints, int tokens){
		for (int z = tokens-1; z >= 0; z--) 
			for (int x = 9 - z; x >= 0; x--) {
				for (int y = 9 - z - x; y >= 0; y--) {
					if (availPoints[x][y][z] > 0){
					  return z;	
					}
				}
			}
		return 0;		
	}
	public static int getOppz(int x, int y, int z, int[][][] board, gamingTree gt,  int player, int tokens){
		int[][][] tmpboard=new int[10][10][10];
		gt.copyBoard(board,tmpboard);
		int[] tmpmov=new int[3];
		tmpmov[0]=x;tmpmov[1]=y;tmpmov[2]=z;
	//	System.out.println("here11");
		gt.updateBoard(tmpboard, tmpmov, player);
		int[][][] availPoints = new int[10][10][10];
		gt.getAvailpoints(tmpboard, Utils.getOpPlayerid(player), availPoints);
		return decidedZ(availPoints, tokens);		
	}
	
	
	static int newlyGetPointNum(int[][][] board, gamingTree gt, int[] move, int playerid){
		int[][][] curboard=new int[10][10][10];
		gt.copyBoard(board,curboard);
	//	System.out.println("update");
		ArrayDeque<int[]> upque=new ArrayDeque<int[]>();
		upque.add(move);
		int resnum=0;
		while (!upque.isEmpty()){
			int[] topmove=upque.poll();
			if (curboard[topmove[0]][topmove[1]][topmove[2]]!=playerid)
				resnum++;
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
		return resnum;
	//	System.out.println("update end");
	}
	
	public static PlayerMessage enforcedStrategy(gamingTree gt, int[][][] board) {
		if (gamingTree.madeMoves < SLOW_START_THRES) {
		//	if (gt.game_state.tokens < SLOW_START_TOKEN + 1 - gt.game_state.player) {
			if (false && gt.game_state.tokens < 2) {
				return new PlayerWaitMessage(gt.msgid);
			}
			else {
				int[][][] availPoints = new int[10][10][10];
				int[] tmpmov=new int[3];
				int[] tmpmovz=new int[3];
				double dist = 0.0, minDist = 0.0, minDistz = 0.0;
				int maxpointnum=0;
				int maxpointnumz=0;
				boolean exist = false;
				gt.getAvailpoints(board, gt.game_state.player, availPoints);
				int z=decidedZ(availPoints, gt.game_state.tokens);
				
		//		for (int z = gt.game_state.tokens-1; z >= 0; z--) {
					minDist = 100000.0;
					minDistz = 100000.0;
					exist = false;
					for (int x = 9 - z; x >= 0; x--) {
						for (int y = 9 - z - x; y >= 0; y--) {
							if (availPoints[x][y][z] > 0) {
								int[] lpmov=new int[3];
								lpmov[0]=x;lpmov[1]=y;lpmov[2]=z;
								int newpointnum=newlyGetPointNum(board, gt, lpmov, gt.game_state.player);
								int oppz=getOppz(x,y,z,board,gt, gt.game_state.player, gt.game_state.opponent_tokens+1);
								
								dist = ((double)x - (9.0-(double)z)/4.0) * ((double)x - (9.0-(double)z)/4.0)
										+ ((double)y - (9.0-(double)z)/4.0) * ((double)y - (9.0-(double)z)/4.0);
								if ((z > oppz) && (maxpointnumz < newpointnum || ((maxpointnumz == newpointnum) && minDistz > dist))) {
									exist = true;
									minDistz = dist;
									maxpointnumz = newpointnum;
									tmpmovz[0]=x;tmpmovz[1]=y;tmpmovz[2]=z;
								}
								if (maxpointnum < newpointnum || ((maxpointnum == newpointnum) && minDist > dist)) {
									minDist = dist;
									maxpointnum = newpointnum;
									tmpmov[0]=x;tmpmov[1]=y;tmpmov[2]=z;
								}
							}
						}
					}
					if (exist)
						return new PlayerMoveMessage(gt.msgid, tmpmovz);
					else {
					//what if we wait?
						System.out.println("shall we wait? ");
						int[][][] oppavailPoints = new int[10][10][10];
						gt.getAvailpoints(board, Utils.getOpPlayerid(gt.game_state.player), oppavailPoints);
						int oppz=decidedZ(oppavailPoints, gt.game_state.opponent_tokens+1);
						for (int oppx = 9 - oppz; oppx >= 0; oppx--) {
							for (int oppy = 9 - oppz - oppx; oppy >= 0; oppy--) 
								if (oppavailPoints[oppx][oppy][oppz] > 0) {
								int oppo_oppz=getOppz(oppx,oppy,oppz,board,gt, Utils.getOpPlayerid(gt.game_state.player), gt.game_state.tokens+1);
								if (oppo_oppz < oppz){
									System.out.println("can't wait! "+tmpmov[2]);
									return new PlayerMoveMessage(gt.msgid, tmpmov);
								}
							}
						}
						
						return new PlayerWaitMessage(gt.msgid);
					}
				}
						 
				
		//		System.out.println("Deeply investigate!!!");
	//		}
		}
		return null;
	}
	
	public double simpleBoardEvaluation(int[][][] board, int playerID, int ourTokens, int opTokens, boolean isOurTurn) {
		int opID = 3 - playerID;
		return (Utils.getPoints(board, playerID) - Utils.getPoints(board, opID));
	}
	

	
	public double boardEvaluation(int[][][] board, int playerID, int ourTokens, int opTokens, boolean isOurTurn) {
		//System.out.println("**** Op tokens: " + opTokens + " Our tokens: " + ourTokens);
		double ourScore = 0.0, opScore = 0.0, ourTmp = 0.0, opTmp = 0.0;
		double opNextMaxPoint = 0.0, opNextMaxEval = 0.0, pointEval = 0.0;
		double ourNextMaxPoint = 0.0, ourNextMaxEval = 0.0;
		int opID = 3 - playerID;
		double eval = Utils.getPoints(board, playerID) - Utils.getPoints(board, opID);
		if (eval < 0)
			eval *= (-eval);
		int max_z = 5;
		
		if (eval > -20) {
			max_z = 5;
		} else if (eval > -30) {
			max_z = 4;
		} else if (eval > -40) {
			max_z = 3;
		} else {
			max_z = 3;
		}
		
		if (gamingTree.madeMoves < SLOW_START_THRES) {
			
		}
		
		
		//int[][][] allTetra = Utils.allTetrahedron(board, playerID);
		int ourNumMoves = 0, opNumMoves = 0;
		int points = 0;
		double laterWeight = 0.25;
		double pointAdv = 0.0;
		
		ArrayList<Double> opScoreList = new ArrayList<Double>();
		ArrayList<Double> ourScoreList = new ArrayList<Double>();
				
		if (isOurTurn) {
			opTokens += 1;
		} else {
			ourTokens += 1;
		}
		
		
		
		for (int z = 6; z >= 0; z--) {
			for (int x = 9 - z; x >= 0; x--) {
				for (int y = 9 - z - x; y >= 0; y--) {
					//System.out.println("-- points: " + points);
					// Opponents
					//pointAdv = (double)((x+1) * (y+1)) / (double)((11-z)*(11-z)) * 4.0;
					pointAdv = 1.0;
					points = Utils.numNewlyTaken(x, y, z, board, opID);
					
					if (points > 0) {
						opNumMoves++;
					
						if (z+1 > opTokens) {
							pointEval = ((double) opTokens / (double) (z+1)) 
									* (double) points;// (double) (z+1);
							//System.out.println("^^ oppointEval: " + pointEval);
						} else {
							pointEval = (double) points * (double) (opTokens);// (double) (z+1);
							//System.out.println("^^ oppointEval: " + pointEval);
						}
						//opScore += pointEval;
						if (pointEval > opNextMaxEval) {
							opNextMaxPoint = pointEval;
							opNextMaxEval = pointEval;
						}
						opScoreList.add(new Double(pointEval));
					}
					
					// We
					points = Utils.numNewlyTaken(x, y, z, board, playerID);
					
					
					if (points > 0 && z <= max_z) {
						ourNumMoves++;
						if (z+1 > ourTokens) {
							pointEval = pointAdv * (double) ourTokens / (double) (z+1) * (double) points;//   / (double) (z+1);
							//System.out.println("** ourpointEval: " + pointEval);
						} else {
							pointEval = (double) points * (double) (ourTokens);

							//System.out.println("** ourpointEval: " + pointEval);
						}
						//if (points == Utils.getNumPointTetra(z))
						//	if (z+1 <= opTokens)opScore
						//		pointEval *= 0.75;
						//ourScore += pointEval * (double)(z+1);
						if (pointEval > ourNextMaxEval) {
							ourNextMaxPoint = pointEval;
							opNextMaxEval = pointEval;
						}
						ourScoreList.add(new Double(pointEval));
					}
				}
			}
		}
		Collections.sort(opScoreList);
		Collections.reverse(opScoreList);
		Collections.sort(ourScoreList);
		Collections.reverse(ourScoreList);
		
		//System.out.println("OP: " + opScore + " OUR: " + ourScore + " *** "
		//		+ opNumMoves + " " + ourNumMoves + " " + opNextMaxPoint + " " + ourNextMaxPoint);
		
		if (LIST_TOP_NUM != 0) {
			int count = 0;
			
			for (double score: opScoreList) {
				if (count < LIST_TOP_NUM)
					count++;
				else
					break;
				//System.out.println(" && " + score);
				opScore += score;
			}
			opScore /= (double) LIST_TOP_NUM;
			
			
			count = 0;
			
			for (double score: ourScoreList) {
				if (count < LIST_TOP_NUM)
					count++;
				else
					break;
				//System.out.println(" && " + score);
				ourScore += score;
			}
			ourScore /= (double) LIST_TOP_NUM;
		}
		
		//if (ourTokens >= 3)
		//	opNextMaxPoint *= 5;
			
		opScore += opNextMaxPoint;
		ourScore += ourNextMaxPoint;
		//opScore = opScore / (double) opNumMoves + opNextMaxPoint;
		//ourScore = ourScore / (double) ourNumMoves + ourNextMaxPoint;

		//System.out.println("OP: " + opScore + " OUR: " + ourScore + " ---");
		
		eval += (ourScore - 0.5 * opScore);
		//System.out.println(" ------------------------------------------------ eval: " + eval);
		return eval;
	}
}
