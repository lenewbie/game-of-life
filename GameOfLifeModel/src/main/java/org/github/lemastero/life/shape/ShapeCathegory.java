package org.github.lemastero.life.shape;

import java.util.*;

import lombok.*;

@RequiredArgsConstructor
public class ShapeCathegory {
	
	@Getter
	private final String name;
	
	@Getter
	private List<ShapeType> shapeTypes = new ArrayList<ShapeType>();

	public void addShape(ShapeType shape) {
		shapeTypes.add(shape);
	}
}
