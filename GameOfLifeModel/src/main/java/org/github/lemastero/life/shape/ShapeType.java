package org.github.lemastero.life.shape;

import java.util.*;

import org.github.lemastero.life.Point;

import lombok.*;

@RequiredArgsConstructor
public class ShapeType {
	
	@Getter 
	private final String name;
	
	@Getter
	private final ShapeCathegory cathegory;

	@Getter @Setter
	private List<Point> shapeDef = new ArrayList<Point>();
	
}
