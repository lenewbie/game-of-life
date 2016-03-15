package org.github.lemastero.life;

import static org.testng.Assert.*;

import org.github.lemastero.life.board.Board;

import static org.mockito.Mockito.*;
import org.testng.annotations.Test;

import static org.github.lemastero.life.PointSimpleFactory.*;

public class BoardTest {
	
	private static final View view = mock(View.class);
	
	@Test public void pointsOnBoard2on2AreRecognized() {
		// when
		Board board = new Board(point_2_3, view);
		
		// then
		assertTrue(board.isOnBoard(point_1_1));
		assertTrue(board.isOnBoard(point_1_3));
		assertTrue(board.isOnBoard(point_2_1));
		assertTrue(board.isOnBoard(point_2_3));
		
		assertFalse(board.isOnBoard( point(2,4)) );
		assertFalse(board.isOnBoard( point_3_1) );
		assertFalse(board.isOnBoard( point_3_2) );
		assertFalse(board.isOnBoard( point_3_3) );
		assertFalse(board.isOnBoard( point(0,1)) );
		assertFalse(board.isOnBoard( point(1,0)) );
		assertFalse(board.isOnBoard( point(0,0)) );
	}
	
	@Test public void remembersIfCellIsAlive() {
		// given
		Board board = new Board(point_1_1, view);
		
		// when
		board.markCells(point_1_1);
		
		// then
		assertEquals(board.isAlive(point_1_1), true);
	}
	
	@Test public void remembersIfCellIsDead() {		
		// when
		Board board = new Board(point_1_1, view);
		
		// then
		assertEquals(board.isAlive(point_1_1), false);
	}
	
	/*   2
	 * 2   2   
	 *   2
	 *   
	 */   
	@Test public void deadCellRemainDead() {
		// given
		Board board = new Board(point_4_3, view);
		board.markCells(point_1_2, point_2_1, point_2_3, point_3_2);
		
		// when
		board.onNextTick();	
		
		// then
		assertEquals(board.isAlive(point_1_1), false); // 2
		assertEquals(board.isAlive(point_2_2), false); // 4
		assertEquals(board.isAlive(point_4_2), false); // 1
	}
	
	/*
	 *  2 2 X 
	 *  2
	 */
	@Test public void liveCellsWithLessThan2NeighboursDies() {
		// given
		Board board = new Board(point_2_3, view);
		board.markCells(point_1_1, point_1_2, point_2_1, point_1_3);
		
		// when
		board.onNextTick();	
		
		// then
		assertEquals(board.isAlive(point_1_3), false);
	}
	
	/*
	 *  3 X 2 
	 *  3 X
	 */
	@Test public void liveCellDiesWhenHasGreaterThan3Neighbours() {
		// given
		Board board = new Board(point_3_3, view);
		board.markCells(point_1_1, point_1_2, point_1_3, point_2_1, point_2_2);
		
		// when
		board.onNextTick();	
		
		// then
		assertEquals(board.isAlive(point_1_2), false);
		assertEquals(board.isAlive(point_2_2), false);
	}
	
	/*
	 *   1
	 * 1 1 
	 */
	@Test public void deadCellLivesWhenHas3Neighbours() {
		// given
		Board board = new Board(point_2_2, view);
		board.markCells(point_1_2, point_2_1, point_2_2);
		
		// when
		board.onNextTick();	
		
		// then
		assertEquals(board.isAlive(point_1_1), true);
	}
}
