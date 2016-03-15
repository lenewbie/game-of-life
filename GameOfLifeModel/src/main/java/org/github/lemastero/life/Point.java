package org.github.lemastero.life;

import java.util.*;
import lombok.*;
import static org.github.lemastero.life.PointSimpleFactory.*;

@AllArgsConstructor @EqualsAndHashCode @ToString
public class Point {
	@Getter private int row; 
	@Getter private int column;
	
	public List<Point> neighbours() {
		List<Point> result = new ArrayList<Point>();
		for(int i = row-1; i <= row+1; ++i)
			for(int j = column-1; j <= column+1; ++j)
				if(i != row || j != column)
					result.add(point(i,j));
		return result;
	}

	public List<Point> generateRelative(List<Point> points) {
		List<Point> result = new ArrayList<Point>(points.size());
		for(Point each : points)
			result.add(moveBy(each));
		return result;
	}

	private Point moveBy(Point each) {
		return point(row + each.getRow(), column + each.getColumn());
	}
}
