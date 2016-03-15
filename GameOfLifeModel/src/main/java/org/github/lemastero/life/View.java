package org.github.lemastero.life;

import org.github.lemastero.life.board.Model;

public interface View {

	void setDead(Point point);

	void setAlive(Point point);

	void openWindow();

	void setTitle(String string);

	void createContents();

	void setModel(Model board);

	void updateFooter(String string);

	void layout();

	void setPlayStop(String object);
}
