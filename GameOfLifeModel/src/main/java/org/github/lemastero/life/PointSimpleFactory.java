package org.github.lemastero.life;

public class PointSimpleFactory {
	
	public static final Point point_1_1 = point(1,1);
	public static final Point point_1_2 = point(1,2);
	public static final Point point_1_3 = point(1,3);
	public static final Point point_2_1 = point(2,1);
	public static final Point point_2_2 = point(2,2);
	public static final Point point_2_3 = point(2,3);
	public static final Point point_3_1 = point(3,1);
	public static final Point point_3_2 = point(3,2);
	public static final Point point_3_3 = point(3,3);
	public static final Point point_3_4 = point(3,4);
	public static final Point point_4_1 = point(4,1);
	public static final Point point_4_2 = point(4,2);
	public static final Point point_4_3 = point(4,3);
	public static final Point point_4_4 = point(4,4);
	
	public static Point point(int row, int column) {
		return new Point(row, column);
	}

}
