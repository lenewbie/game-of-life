package org.github.lemastero.life.board;

import static org.github.lemastero.life.PointSimpleFactory.*;
import static java.lang.Boolean.*;
import java.util.*;

import org.github.lemastero.life.Point;
import org.github.lemastero.life.View;
import org.github.lemastero.life.shape.ShapeCathegory;
import org.github.lemastero.life.shape.ShapeParser;
import org.github.lemastero.life.shape.ShapeRepository;
import org.github.lemastero.life.shape.ShapeType;

import lombok.*;

/*
 * TODO extract presentation logic somwhere to UI
 * TODO extract Interactors, Entitys and Boundries according to clean architecture
 * TODO better modularize (module with common interfaces ?, module for main ?)
 */
@ToString @EqualsAndHashCode
public class Board implements Model {
	@Getter private final Point size;
	private final View view;
	private List<List<Boolean>> currentCellsState = new ArrayList<List<Boolean>>();
	private List<List<Boolean>> nextCellsState = new ArrayList<List<Boolean>>(); 
	private int generation = 0;
	
	@Override
	public void onCellDoubleClicked(Point point) {
		if(isAlive(point)) {
			currentCellsState.get(row(point)).set(column(point), FALSE);
			view.setDead(point);
		} else {
			currentCellsState.get(row(point)).set(column(point), TRUE);
			view.setAlive(point);
		}
	}
	
	@Override
	public void onNextTick() {
		calculateNextStep();
		++generation;
		applyCalculatedState();
	}

	private void applyCalculatedState() {
		currentCellsState = nextCellsState;
		nextCellsState = new ArrayList<List<Boolean>>();
		updateBoard();
	}

	private void calculateNextStep() {
		// if first row has life and last row is dead => move up all row and remove last
		if(exists(firstRow(), TRUE) && !exists(lastRow(), TRUE) ) {
			currentCellsState.remove(currentCellsState.size()-1);
			currentCellsState.add(0, emptyRow());
		}
		if(exists(firstRow(), TRUE) && !exists(lastRow(), TRUE) ) {
			currentCellsState.remove(currentCellsState.size()-1);
			currentCellsState.add(0, emptyRow());
		}
		if(exists(firstRow(), TRUE) && !exists(lastRow(), TRUE) ) {
			currentCellsState.remove(currentCellsState.size()-1);
			currentCellsState.add(0, emptyRow());
		}
		
		if(exists(lastRow(), TRUE) && !exists(firstRow(), TRUE) ) {
			for(int i = 1; i < currentCellsState.size(); ++i )
				currentCellsState.set(i-1, currentCellsState.get(i));
			currentCellsState.add(emptyRow());
		}	

		for(int rowIndex = 0; rowIndex < getNumberOfRows(); ++rowIndex)
			nextCellsState.add(calculateNextRow(rowIndex));
	}

	private List<Boolean> firstRow() {
		return currentCellsState.get(0);
	}

	private List<Boolean> lastRow() {
		return currentCellsState.get(currentCellsState.size()-1);
	}

	private List<Boolean> emptyRow() {
		List<Boolean> emptyRow = new ArrayList<Boolean>(size.getColumn());
		for(int column = 0; column < getNumberOfColumns(); ++column)
			emptyRow.add(column, FALSE);
		return emptyRow;
	}

	private boolean exists(List<Boolean> list, Boolean isFilled) {
		for(Boolean eachCell : list) {
			if(isFilled.equals(eachCell)) return true;
		}
		return false;
	}

	private List<Boolean> calculateNextRow(int rowIndex) {
		List<Boolean> nextRow = new ArrayList<Boolean>(getNumberOfColumns());
		for(int columnId = 0; columnId < getNumberOfColumns(); ++columnId)
			nextRow.add(columnId, isAliveAfterTick(point(rowIndex, columnId)));
		return nextRow;
	}

	private Boolean isAliveAfterTick(Point point) {
		int aliveNeigbours = getNumberOfAliveNieghbours(point);
		if( isAlive(asExternal(point)) )
			return isProperPopulation(aliveNeigbours);
		else
			return shouldBeBorned(aliveNeigbours);
	}

	private Boolean isProperPopulation(int aliveNeigbours) {
		if(isUnderPopulation(aliveNeigbours)) 
			return false;
		else if(isOverpopulation(aliveNeigbours)) 
			return false;
		return true;
	}

	private boolean shouldBeBorned(int aliveNeigbours) {
		return aliveNeigbours == 3;
	}

	private boolean isOverpopulation(int aliveNeigbours) {
		return aliveNeigbours > 3;
	}

	private boolean isUnderPopulation(int aliveNeigbours) {
		return aliveNeigbours < 2;
	}

	private int getNumberOfAliveNieghbours(Point point) {
		int result = 0;
		for(Point each : point.neighbours())
			if( isOnBoard(asExternal(each)) && isAlive(asExternal(each)) )
				++result;
		return result;
	}

	public boolean isOnBoard(Point point) {
		if(column(point) < 0) return false;
		if(column(point) >= getNumberOfColumns()) return false;
		if(row(point) < 0) return false;
		if(row(point) >= getNumberOfRows()) return false;
		return true;
	}

	@Override
	public void onStart() {
		view.setModel(this);
		view.createContents();
		view.setTitle("Conway's Game of Life (B2/S23)");
		updateBoard();
	}

	public boolean isAlive(Point point) {
		return currentCellsState.get(row(point)).get(column(point));
	}

	private void updateBoard() {
		for(int row = 1; row <= getNumberOfRows(); ++row)
			for(int column = 1; column <= getNumberOfColumns(); ++column)
				updateCell(point(row, column));
		view.updateFooter("Generation: " + generation);
		view.layout();
	}

	private void updateCell(Point point) {
		if( isAlive(point) ) view.setAlive(point);
		else view.setDead(point);
	}

	@Override
	public int getNumberOfColumns() {
		return size.getColumn();
	}

	@Override
	public int getNumberOfRows() {
		return size.getRow();
	}

	@Override
	public void markCells(Point... points) {		
		for(Point each : points)
			currentCellsState.get(row(each)).set(column(each), TRUE);
	}

	private int row(Point point) {
		return point.getRow()-1;
	}
	
	private int column(Point point) {
		return point.getColumn()-1;
	}
	
	private Point asExternal(Point point) {
		return point(point.getRow()+1, point.getColumn()+1);
	}

	public Board(Point size, View view) {
		this.size = size;
		this.view = view;
		for(int row = 0; row < getNumberOfRows(); ++row) {
			List<Boolean> rows = new ArrayList<Boolean>(size.getColumn());
			for(int column = 0; column < getNumberOfColumns(); ++column)
				rows.add(column, FALSE);
			currentCellsState.add(rows);
		}
	}

	@Override
	public void onDrawShape(Point point, ShapeType shape) {
		if(shape == null) {
			System.out.println("No shape defined");
			return;
		}
		
		System.out.println("Draw block for " + shape.getName() + "" + shape.getCathegory());
		if(shape.getName() == null || shape.getCathegory() == null || shape.getShapeDef().isEmpty()) {
			System.out.println("No shape defined enough");
			return;
		}
		
		List<Point> generateRelative = point.generateRelative(shape.getShapeDef());
		markCells(generateRelative.toArray(new Point[0]));
		updateBoard();
	}

	ShapeRepository shapeRepository;
	@Override
	public List<ShapeCathegory> getAvailableShapes() {
		if(shapeRepository == null) {
			shapeRepository = new ShapeRepository();
			shapeRepository.initialize(new ShapeParser());
		}
		return shapeRepository.getShapeTypes();
	}

	@Override
	public void onClear() {
		currentCellsState = new ArrayList<List<Boolean>>();
		for(int row = 0; row < getNumberOfRows(); ++row)
			currentCellsState.add(emptyRow());
		updateBoard();
	}

	public void onGenerateRandomPoints() {
		List<Point> shapePoints = new ArrayList<Point>();
		
		Random rand = new Random();
		int pointsToGenerate = rand.nextInt(getNumberOfColumns() * getNumberOfRows());
		for(int i = 1; i<= pointsToGenerate; ++i) {
			int col = rand.nextInt(getNumberOfColumns()-2) + 1;
			int row = rand.nextInt(getNumberOfRows()-2) + 1;
			
			shapePoints.add( point(row, col) );
		}
		markCells(shapePoints.toArray(new Point[0]));
		updateBoard();
	}
}
