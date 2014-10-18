package com.barracuda.contest2014;

import java.util.ArrayList;
import java.util.PriorityQueue;


//for basic util functions
public class Utils {
	static int[] numPointTetra = {1, 4, 10, 20, 35, 56, 84, 120, 165, 220};
	static int boardsize=10;
	
	static int getOpPlayerid(int player){
		if (player==1) return 2;
		return 1;				
	}
	
	static int[][][] copyBoard(int[][][] curboard){
		int[][][] resboard=new int[boardsize][boardsize][boardsize];
		
		for (int i=0;i<boardsize;i++)
			for (int j=0;i+j<boardsize;j++)
				for (int k=0;i+j+k<boardsize;k++)
			//	  if (i+j+k<boardsize){
			//		System.out.println("("+i+","+j+","+k+" -- "+curboard[i][j][k]+")\n");
					    resboard[i][j][k]=curboard[i][j][k];
			//	}
		return resboard;		
	};
	
	static void updateBoard(int[][][] curboard, int[] move, int playerid){
		PriorityQueue<int[]> upque=new PriorityQueue<int[]>();
		upque.add(move);
		while (!upque.isEmpty()){
			int[] topmove=upque.poll();
			curboard[topmove[0]][topmove[1]][topmove[2]]=playerid;
			if (topmove[2]>0){
				int[] l1=new int[3]; l1[0]=topmove[0];l1[1]=topmove[1];l1[2]=topmove[2]-1;			
				int[] l2=new int[3]; l2[0]=topmove[0];l2[1]=topmove[1]+1;l2[2]=topmove[2]-1;
				int[] l3=new int[3]; l3[0]=topmove[0]+1;l3[1]=topmove[1];l3[2]=topmove[2]-1;
				upque.add(l1);
				upque.add(l2);
				upque.add(l3);
			}			
		}		
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
		
		for ( int z = 9; z >= 0; --z ) {
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
