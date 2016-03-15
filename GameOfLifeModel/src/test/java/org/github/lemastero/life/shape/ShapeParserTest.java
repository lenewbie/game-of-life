package org.github.lemastero.life.shape;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

public class ShapeParserTest {
	
	List<String> sampel = new ArrayList<String>();
	{
		sampel.add("!Name: pond");
		sampel.add("!");
		sampel.add(".OO.");
		sampel.add("O..O");
		sampel.add("O..O");
		sampel.add(".OO.");
	}
	
	@Test
	public void shouldRecognizeHeader() {
		ShapeParser parser = new ShapeParser();
		ShapeType result = parser.parsePlaintTextFIle(sampel);
		// TODO
	}

}
