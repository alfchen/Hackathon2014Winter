package com.barracuda.contest2014;

public class Strategy {
	public final int OP_TOKEN_GAP_THRES = 3;
	public final int OUR_TOKEN_GAP_THRES = 3;
	
	public double simpleBoardEvaluation(int[][][] board, int playerID, int ourTokens, int opTokens) {
		int opID = 3 - playerID;
		return (Utils.getPoints(board, playerID) - Utils.getPoints(board, opID));
	}
	
	public double boardEvaluation(int[][][] board, int playerID, int ourTokens, int opTokens) {
		//System.out.println("**** Op tokens: " + opTokens + " Our tokens: " + ourTokens);
		double ourScore = 0.0, opScore = 0.0, ourTmp = 0.0, opTmp = 0.0;
		double opNextMaxPoint = 0.0, opNextMaxEval = 0.0, pointEval = 0.0;
		double ourNextMaxPoint = 0.0, ourNextMaxEval = 0.0;
		int opID = 3 - playerID;
		double eval = Utils.getPoints(board, playerID) - Utils.getPoints(board, opID);
		//int[][][] allTetra = Utils.allTetrahedron(board, playerID);
		int ourNumMoves = 0, opNumMoves = 0;
		int points = 0;
		double laterWeight = 0.25;
		
		ourTokens += 1;
		
		for (int z = opTokens + OP_TOKEN_GAP_THRES; z > 0; z--) {
			for (int x = 9 - z; x >= 0; x--) {
				for (int y = 9 - z - x; y >= 0; y--) {
					//System.out.println("-- points: " + points);
					// Opponents

					points = Utils.numNewlyTaken(x, y, z, board, opID);
					
					if (points > 0) {
						opNumMoves++;
					
						if (z+1 > opTokens) {
							pointEval = (double) opTokens / (double) (z+1) * (double) points;// (double) (z+1);
							//System.out.println("^^ oppointEval: " + pointEval);
						} else {
							pointEval = (double) points;// (double) (z+1);
							//System.out.println("^^ oppointEval: " + pointEval);
						}
						opScore += pointEval * (double)(z+1);
						if (pointEval > opNextMaxEval)
							opNextMaxPoint = pointEval * (double)(z+1);
					}
					
					// We
					points = Utils.numNewlyTaken(x, y, z, board, playerID);
					
					if (points > 0) {
						ourNumMoves++;
						if (z+1 > ourTokens) {
							pointEval = (double) ourTokens / (double) (z+1) * (double) points / 1.5;// / (double) (z+1);
							//System.out.println("** ourpointEval: " + pointEval);
						} else {
							pointEval = (double) points * 1.5;
							//System.out.println("** ourpointEval: " + pointEval);
						}
						if (points == Utils.getNumPointTetra(z))
							if (z+1 <= opTokens)
								pointEval *= 0.75;
						ourScore += pointEval * (double)(z+1);
						if (pointEval > ourNextMaxEval)
							ourNextMaxPoint = pointEval * (double)(z+1);
					}
				}
			}
		}
		
		System.out.println("OP: " + opScore + " OUR: " + ourScore + " *** "
				+ opNumMoves + " " + ourNumMoves + " " + opNextMaxPoint + " " + ourNextMaxPoint);
		
		opScore = opScore / (double) opNumMoves + opNextMaxPoint;
		ourScore = ourScore / (double) ourNumMoves + ourNextMaxPoint;

		System.out.println("OP: " + opScore + " OUR: " + ourScore + " ---");
		
		eval += (ourScore - opScore);
		
		return eval;
	}
}
