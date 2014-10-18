package com.barracuda.contest2014;

public class Strategy {
	public final int OP_TOKEN_GAP_THRES = 3;
	public final int OUR_TOKEN_GAP_THRES = 3;
	
	
	public double boardEvaluation(int[][][] board, int playerID, int ourTokens, int opTokens) {
		double ourScore = 0.0, opScore = 0.0, ourTmp = 0.0, opTmp = 0.0;
		double opNextMaxPoint = 0.0, opNextMaxEval = 0.0, pointEval = 0.0;
		double ourNextMaxPoint = 0.0, ourNextMaxEval = 0.0;
		int opID = (playerID == 1)? 2: 1;
		double eval = Utils.getPoints(board, playerID) - Utils.getPoints(board, opID);
		//int[][][] allTetra = Utils.allTetrahedron(board, playerID);
		int ourNumMoves = 0, opNumMoves = 0;
		int points = 0;
		double laterWeight = 0.25;
		
		
		for (int z = opTokens + OP_TOKEN_GAP_THRES; z > 0; z--) {
			for (int x = 9 - z; x >= 0; x--) {
				for (int y = 9 - z - x; y >= 0; y--) {
					points = Utils.numNewlyTaken(x, y, z, board, playerID);
					// Opponents
					if (points < 0 || points == Utils.getNumPointTetra(z)) {
						if (points < 0)
							points = -points;
						if (z+1 > opTokens) {
							pointEval = (double) opTokens / (double) (z+1) * (double) points / (double) (z+1);
						} else {
							pointEval = (double) points / (double) (z+1);
						}
						opScore += pointEval * (double)(z+1);
						if (pointEval > opNextMaxEval)
							opNextMaxPoint = pointEval * (double)(z+1);
						opNumMoves++;
					}
					// We
					if (points > 0 || points == Utils.getNumPointTetra(z)) {
						if (z+1 > ourTokens) {
							pointEval = (double) ourTokens / (double) (z+1) * (double) points / (double) (z+1);
						} else {
							pointEval = (double) points / (double) (z+1);
						}
						if (points == Utils.getNumPointTetra(z))
							pointEval /= 2;
						ourScore += pointEval * (double)(z+1);
						if (pointEval > ourNextMaxEval)
							ourNextMaxPoint = pointEval * (double)(z+1);
						ourNumMoves++;
					}
				}
			}
		}
		
		opScore = opScore * laterWeight + opNextMaxPoint;
		ourScore = ourScore * laterWeight + ourNextMaxPoint;
		
		eval += (ourScore - opScore);
		
		return eval;
	}
}
