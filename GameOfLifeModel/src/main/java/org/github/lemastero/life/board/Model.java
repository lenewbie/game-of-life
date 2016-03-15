package org.github.lemastero.life.board;

import java.util.List;

import org.github.lemastero.life.Point;
import org.github.lemastero.life.shape.ShapeCathegory;
import org.github.lemastero.life.shape.ShapeType;

public interface Model {

	void onCellDoubleClicked(Point point);

	void onNextTick();

	void onStart();

	int getNumberOfColumns();

	int getNumberOfRows();

	void markCells(Point... points);

	void onDrawShape(Point point, ShapeType shape);

	List<ShapeCathegory> getAvailableShapes();

	void onClear();
	
	void onGenerateRandomPoints();
}