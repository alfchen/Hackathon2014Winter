package com.barracuda.contest2014;

public class Strategy {
	public double boardEvaluation(int[][][] board, int playerID, int ourTokens, int opTokens) {
		double ourScore = 0.0, opScore = 0.0, ourTmp = 0.0, opTmp = 0.0;
		double opNextMax = 0.0, pointEval = 0.0;
		int opID = (playerID == 1)? 2: 1;
		double eval = Utils.getPoints(board, playerID) - Utils.getPoints(board, opID);
		int[][][] allTetra = Utils.allTetrahedron(board, playerID);
		
		for (int z = 9; z > 0; z--) {
			for (int x = 9 - z; x >= 0; x--) {
				for (int y = 9 - z - x; y >= 0; y--) {
					// Opponents
					if (z+1 > opTokens) {
						pointEval = (double) opTokens / (double) (z+1) * allTetra[x][y][z];
					} else {
						pointEval = allTetra[x][y][z];
					}
				}
			}
		}
		return eval;
	}
}
