package com.barracuda.contest2014;

import java.util.ArrayList;


//for basic util functions
public class Utils {
	static int[] numPointTetra = {1, 4, 10, 20, 35, 56, 84, 120, 165, 220};
	
	static int getOpPlayerid(int player){
		if (player==1) return 2;
		return 1;				
	}

	static int getNumPointTetra(int z) {
		return numPointTetra[z];
	}
	/**
	 * get all available tetrahedrons in current board
	 * @return 
	 */
	static int[][][] allTetrahedron(int[][][] board, int player) {
		int[][][] v = new int[10][10][10];

		for ( int i = 0; i < 10; ++i ) {
			for ( int j = 0; j < 10; ++j ) {
				for ( int z = 0; z < 10; ++z ) {
					v[i][j][z] = 0;
				}
			}
		}
		
		for ( int z = 9; z >= 1; --z ) {
			for ( int x = 9 - z; x >= 0; --x ) {
				for ( int y = 9 - x - z; y >= 0; --y ) {

					if ( board[x][y][z] == 0 ) { // empty point, points below: (x,y,z-1), (x+1,y,z-1), and (x,y+1,z-1)	
						// can we take it?
						Integer inc = 0;
						Integer dec = 0;
						if ( canTake(x, y, z, board, player, inc) )  {
							v[x][y][z] = inc + 1;
						}
						else if ( canTake(x, y, z, board, 3 - player, dec) ) {
							v[x][y][z] = -1 * dec - 1;
						}
					
					}
					
				}
			}
		}
		
		
		return v;
	}
	
	/**
	 * Does the point (x, y, z) dominate a tetrahedron for player
	 * @return
	 */
	static boolean canTake(int x, int y, int z, int[][][] board, int player, Integer inc) {
		for ( int zz = 0; zz < z; ++zz ) {
			for ( int xx = x; xx < x + z; ++xx ) {
				for ( int yy = y; yy < y + z; ++yy ) {
					if ( board[xx][yy][zz] == 0 )
						inc++;
					else if ( board[xx][yy][zz] == player )
						;
					else
						return false;
				}
			}
		}
		
		
		return true;
	}
}
