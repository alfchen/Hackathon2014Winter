package com.barracuda.contest2014;

import java.util.ArrayList;
import java.util.Collections;

public class Strategy {
	public final int OP_TOKEN_GAP_THRES = 5;
	public final int OUR_TOKEN_GAP_THRES = 5;
	public final int LIST_TOP_NUM = 2;
	public final int WAIT_GAP = 2;
	public final int OUR_MAX_Z = 4;
	
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
					pointAdv = (double)((x+1) * (y+1)) / (double)((11-z)*(11-z)) * 4.0;
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
