package com.barracuda.contest2014;

import java.util.ArrayList;


//for basic util functions
public class Utils {
	
	static int getOpPlayerid(int player){
		if (player==1) return 2;
		return 1;				
	}

	/**
	 * get all available tetrahedrons in current board
	 * @return 
	 */
	static int[][][] allTetrahedron(int[][][] board) {
		int[][][] v = new int[10][10][10];

		for ( int i = 0; i < 10; ++i ) {
			for ( int j = 0; j < 10; ++j ) {
				for ( int z = 0; z < 10; ++z ) {
					v[i][j][z] = 0;
				}
			}
		}
		
		for ( int z = 9; z >= 0; --z ) {
			for ( int x = 9 - z; x >= 0; --x ) {
				for ( int y = 9 - x - z; y >= 0; --y ) {

					if ( board[x][y][z] == 0 ) { // empty point
					// points below: (x,y,z-1), (x+1,y,z-1), and (x,y+1,z-1)	
						
					}
					
				}
			}
		}
		
		
		return v;
	}
}
