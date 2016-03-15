package org.github.lemastero.life;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.github.lemastero.life.board.Board;
import static org.github.lemastero.life.PointSimpleFactory.*;

public class GameOfLIfe {
	
	/**
	 * Launch the application.
	 * 
	 * TODO implement self scroling of screen to follow movement
	 * 
	 * TODO implement other rule sets: http://conwaylife.com/wiki/Rulestring#Rules
	 * 
	 * TODO implement loading of different file formats:  RLE, Life 1.05, Life 1.06, Plaintext 
	 *  http://conwaylife.com/wiki/Life_1.05
	 * 	http://www.mirekw.com/ca/ca_files_formats.html
	 */
	public static void main(String[] args) {
		try {
			MainWindow view = new MainWindow();
			Board model = new Board( new Point(40, 40), view);
			model.onStart();
			model.onGenerateRandomPoints();
			view.openWindow();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
