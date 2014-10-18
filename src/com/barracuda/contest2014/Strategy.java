package com.barracuda.contest2014;

public class Strategy {
	public double boardEvaluation(int[][][] board, double ourAdv, int tokens, int opToken) {
		double eval = ourAdv;
		int[][][] allTetra = Utils.allTetrahedron(board);
		
		for (int z = 9; z > 0; z--) {
			for (int x = 9 - z; x >= 0; x--) {
				for (int y = 9 - z - x; y >= 0; y--) {
					
				}
			}
		}
		return eval;
	}
}
