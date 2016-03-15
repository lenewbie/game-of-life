package org.github.lemastero.life;

import org.eclipse.swt.events.*;
import org.github.lemastero.life.board.Model;

import lombok.*;

@ToString @AllArgsConstructor
class CellDoubleClickListener extends MouseAdapter {
	private Model model;
	private Point point;
	
	@Override
	public void mouseDoubleClick(MouseEvent e) {
		model.onCellDoubleClicked(point);
	}
}