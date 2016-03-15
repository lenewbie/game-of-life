package org.github.lemastero.life.shape;

import static org.github.lemastero.life.PointSimpleFactory.point;
import static org.github.lemastero.life.PointSimpleFactory.point_1_2;
import static org.github.lemastero.life.PointSimpleFactory.point_2_3;
import static org.github.lemastero.life.PointSimpleFactory.point_3_1;
import static org.github.lemastero.life.PointSimpleFactory.point_3_2;
import static org.github.lemastero.life.PointSimpleFactory.point_3_3;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.github.lemastero.life.Point;

import lombok.Getter;

public class ShapeRepository {
	
	@Getter
	private List<ShapeCathegory> shapeTypes = new ArrayList<ShapeCathegory>();
	
	{
		ShapeCathegory cathegory = new ShapeCathegory("Spaceships");
		{
			ShapeType shape = new ShapeType("Glider", cathegory);		
			List<Point> shapePoints = new ArrayList<Point>();
			shapePoints.add(point_1_2);
			shapePoints.add(point_2_3);
			shapePoints.add(point_3_1);
			shapePoints.add(point_3_2);
			shapePoints.add(point_3_3);
			shape.setShapeDef(shapePoints);
			cathegory.addShape(shape);
		}
		
		{
			ShapeType shape = new ShapeType("Copperhead", cathegory);		
			List<Point> shapePoints = new ArrayList<Point>();
			shapePoints.add( point_2_3  );
			shapePoints.add( point(2,4) );
			shapePoints.add( point(2,7) );
			shapePoints.add( point(2,8) );
			
			shapePoints.add( point(3,5) );
			shapePoints.add( point(3,6) );
			
			shapePoints.add( point(4,5) );
			shapePoints.add( point(4,6) );
			
			shapePoints.add( point(5,2) );
			shapePoints.add( point(5,4) );
			shapePoints.add( point(5,7) );
			shapePoints.add( point(5,9) );
			
			shapePoints.add( point(6,2) );
			shapePoints.add( point(6,9) );
			
			shapePoints.add( point(8,2) );
			shapePoints.add( point(8,9) );
			
			shapePoints.add( point(9,3) );
			shapePoints.add( point(9,4) );
			shapePoints.add( point(9,7) );
			shapePoints.add( point(9,8) );
			
			shapePoints.add( point(10,4) );
			shapePoints.add( point(10,5) );
			shapePoints.add( point(10,6) );
			shapePoints.add( point(10,7) );
			
			shapePoints.add( point(12,5) );
			shapePoints.add( point(12,6) );
			
			shapePoints.add( point(13,5) );
			shapePoints.add( point(13,6) );
			
			shape.setShapeDef(shapePoints);
			cathegory.addShape(shape);
		}
		shapeTypes.add(cathegory);
	}	
	
	
	public void initialize(ShapeParser parser) {	
		String pathname = "C:\\Users\\Piotr\\Documents\\data"; // TODO param
		
		List<ShapeCathegory> readCateghories = new ArrayList<ShapeCathegory>();
		File file = new File(pathname);
		if(file.isDirectory()) {
			String[] childs = file.list();
			for(String each : childs) {
				ShapeCathegory cathegory = new ShapeCathegory(each);
				File dir = new File(pathname + "\\" + each);
				if(dir.isDirectory()) {
					String[] spec = dir.list();
					for(String eachFile : spec) {
//						System.out.println(each + "/" + eachFile);
						
						File specFile = new File(pathname + "\\" + each, eachFile);
						
						List<String> result = new ArrayList<String>();
						try (Scanner scanner = new Scanner(specFile)) {
							while (scanner.hasNextLine()) {
								String line = scanner.nextLine();
								result.add(line);
							}
							scanner.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
//						System.out.println(result); TODO implement me lazy !@@$#Q%#
						cathegory.addShape(parser.parsePlaintTextFIle(result));
					}
				}
				readCateghories.add(cathegory);
			}
				
		}
		
	}

}
