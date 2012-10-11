package tests;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Set;

import board.IntBoard;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class IntBoardTests {
	
	@Before
	public void init() {
		IntBoard board = new IntBoard();
		board.calcAdjacencies();
	}
	
	//Test Index
	@Test
	public void testCalcIndex4() {
		int expected = 4;
		int actual = board.calcIndex(1, 0);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testCalcIndex13() {
		int expected = 13;
		int actual = board.calcIndex(3, 1);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testCalcIndex11() {
		int expected = 11;
		int actual = board.calcIndex(2, 3);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testCalcIndex9() {
		int expected = 9;
		int actual = board.calcIndex(2, 1);
		Assert.assertEquals(expected, actual);
	}
	
	//Test Adjacency
	IntBoard board = new IntBoard();
	@Test
	public void testAdjacency0()
	{
		board.calcAdjacencies();
		LinkedList testList = board.getAdjList(0);
		Assert.assertTrue(testList.contains(4));
		Assert.assertTrue(testList.contains(1));
		Assert.assertEquals(2, testList.size());
	}
	
	@Test
	public void testAdjacency15()
	{
		board.calcAdjacencies();
		LinkedList testList = board.getAdjList(15);
		Assert.assertTrue(testList.contains(14));
		Assert.assertTrue(testList.contains(11));
		Assert.assertEquals(2, testList.size());
	}
	
	@Test
	public void testAdjacency7()
	{
		board.calcAdjacencies();
		LinkedList testList = board.getAdjList(7);
		Assert.assertTrue(testList.contains(3));
		Assert.assertTrue(testList.contains(6));
		Assert.assertTrue(testList.contains(11));
		Assert.assertEquals(3, testList.size());
	}
	
	@Test
	public void testAdjacency8()
	{
		board.calcAdjacencies();
		LinkedList testList = board.getAdjList(8);
		Assert.assertTrue(testList.contains(9));
		Assert.assertTrue(testList.contains(4));
		Assert.assertTrue(testList.contains(12));
		Assert.assertEquals(3, testList.size());
	}
	
	@Test
	public void testAdjacency5()
	{
		board.calcAdjacencies();
		LinkedList testList = board.getAdjList(5);
		Assert.assertTrue(testList.contains(4));
		Assert.assertTrue(testList.contains(1));
		Assert.assertTrue(testList.contains(6));
		Assert.assertTrue(testList.contains(9));
		Assert.assertEquals(4, testList.size());
	}
	
	@Test
	public void testAdjacency10()
	{
		board.calcAdjacencies();
		LinkedList testList = board.getAdjList(10);
		Assert.assertTrue(testList.contains(9));
		Assert.assertTrue(testList.contains(11));
		Assert.assertTrue(testList.contains(6));
		Assert.assertTrue(testList.contains(14));
		Assert.assertEquals(4, testList.size());
	}
	
	//Test Targets
	@Test
	public void testTargets0_3()
	{
		board.calcAdjacencies();
		board.calcTargets(0, 3);
		Set<Integer> targets= board.getTargets();
		Assert.assertEquals(6, targets.size());
		Assert.assertTrue(targets.contains(12));
		Assert.assertTrue(targets.contains(9));
		Assert.assertTrue(targets.contains(1));
		Assert.assertTrue(targets.contains(6));
		Assert.assertTrue(targets.contains(3));
		Assert.assertTrue(targets.contains(4));
	}
	
	@Test
	public void testTargets8_2()
	{
		board.calcAdjacencies();
		board.calcTargets(8, 2);
		Set<Integer> targets= board.getTargets();
		Assert.assertEquals(4, targets.size());
		Assert.assertTrue(targets.contains(0));
		Assert.assertTrue(targets.contains(5));
		Assert.assertTrue(targets.contains(10));
		Assert.assertTrue(targets.contains(13));
	}
	
	@Test
	public void testTargets4_3()
	{
		board.calcAdjacencies();
		board.calcTargets(4, 3);
		Set<Integer> targets= board.getTargets();
		Assert.assertTrue(targets.contains(2));
		Assert.assertTrue(targets.contains(7));
		Assert.assertTrue(targets.contains(10));
		Assert.assertTrue(targets.contains(13));
		Assert.assertTrue(targets.contains(0));
		Assert.assertTrue(targets.contains(5));
		Assert.assertTrue(targets.contains(8));
	}
	
	@Test
	public void testTargets2_2()
	{
		board.calcAdjacencies();
		board.calcTargets(2, 2);
		Set<Integer> targets= board.getTargets();
		Assert.assertEquals(4, targets.size());
		Assert.assertTrue(targets.contains(0));
		Assert.assertTrue(targets.contains(5));
		Assert.assertTrue(targets.contains(10));
		Assert.assertTrue(targets.contains(7));
	}
	
	@Test
	public void testTargets12_2()
	{
		board.calcAdjacencies();
		board.calcTargets(12, 2);
		Set<Integer> targets= board.getTargets();
		Assert.assertEquals(3, targets.size());
		Assert.assertTrue(targets.contains(4));
		Assert.assertTrue(targets.contains(9));
		Assert.assertTrue(targets.contains(14));
	}
	
	@Test
	public void testTargets10_1()
	{
		board.calcAdjacencies();
		board.calcTargets(10, 1);
		Set<Integer> targets= board.getTargets();
		Assert.assertEquals(4, targets.size());
		Assert.assertTrue(targets.contains(11));
		Assert.assertTrue(targets.contains(9));
		Assert.assertTrue(targets.contains(6));
		Assert.assertTrue(targets.contains(14));
	}

}
