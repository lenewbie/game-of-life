package org.github.lemastero.life;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.github.lemastero.life.board.Model;
import org.github.lemastero.life.shape.ShapeCathegory;
import org.github.lemastero.life.shape.ShapeType;

import lombok.Setter;

/*
 * TODO Apply MVP or something with binding (PM? MVVM?)
 * TODO use WindowBuilderPro factories mechanism
 */
public class MainWindow implements View {

	private Shell shell;
	private Label lblStatus;
	private List<List<Composite>> matrix = new ArrayList<List<Composite>>();
	private @Setter Model model;
	private Optional<Color> blackColor = Optional.empty();
	private Color liveColor;
	private Button btnPlayStop;

	/**
	 * Open the window.
	 */
	public void openWindow() {		
		Display display = Display.getDefault();
		shell.setSize(600, 600);
		shell.open();
		layout();		
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
	}
	
	private boolean isStreaming = false;
	private int interval = 5; 
	private void updateStreaming() {
		isStreaming = ! isStreaming;
		setPlayStop(isStreaming ? "Stop" : "Play");
	}
	
	/**
	 * Create contents of the window.
	 */
	public void createContents() {
		shell = new Shell();
		shell.setLayout(new GridLayout(1, false));
		
		Composite cmpBoard = new Composite(shell, SWT.NONE);		
		cmpBoard.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout boardLayout = new GridLayout(model.getNumberOfColumns(), false);
		boardLayout.verticalSpacing = 0;
		boardLayout.horizontalSpacing = 0;
		boardLayout.marginWidth = 0;
		boardLayout.marginHeight = 0;
		cmpBoard.setLayout(boardLayout);
		
		for(int row = 0; row < model.getNumberOfRows(); ++row) {
			List<Composite> rowList = new ArrayList<Composite>();
			for(int column = 0; column < model.getNumberOfColumns(); ++column) {
				rowList.add(createCell(cmpBoard, getAliveColor(), new Point(row+1, column+1)));
			}
			matrix.add(rowList);
		}
		
		Composite cmpFooter = new Composite(shell, SWT.NONE);
		cmpFooter.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
		GridLayout gl_cmpFooter = new GridLayout(2, false);
		gl_cmpFooter.marginHeight = 0;
		gl_cmpFooter.marginWidth = 0;
		gl_cmpFooter.verticalSpacing = 0;
		cmpFooter.setLayout(gl_cmpFooter);
		
		Composite cmpFooterLeft = new Composite(cmpFooter, SWT.NONE);
		GridLayout gl_cmpFooterLeft = new GridLayout(1, false);
		gl_cmpFooterLeft.verticalSpacing = 0;
		gl_cmpFooterLeft.marginHeight = 0;
		gl_cmpFooterLeft.marginWidth = 0;
		cmpFooterLeft.setLayout(gl_cmpFooterLeft);
		cmpFooterLeft.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));
		
		lblStatus = new Label(cmpFooterLeft, SWT.NONE);
		lblStatus.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));
		updateFooter("Generation: 0");
		
		Composite cmpFooterRIght = new Composite(cmpFooter, SWT.NONE);
		cmpFooterRIght.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
		GridLayout gl_cmpFooterRIght = new GridLayout(2, true);
		gl_cmpFooterRIght.verticalSpacing = 0;
		cmpFooterRIght.setLayout(gl_cmpFooterRIght);
		
		btnPlayStop = new Button(cmpFooterRIght, SWT.NONE);
		btnPlayStop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateStreaming();
				if(isStreaming)
					shell.getDisplay().asyncExec( new Play() );
			}
		});
		btnPlayStop.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnPlayStop.setText("Play");
		
		Button btnNextTick = new Button(cmpFooterRIght, SWT.NONE);
		btnNextTick.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
		btnNextTick.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				model.onNextTick();
			}
		});
		btnNextTick.setText("Next tick");
	}

	public void updateFooter(String text) {
		lblStatus.setText(text);
	}

	public void layout() {
		shell.layout();
	}

	public void setTitle(String title) {
		shell.setText(title);
	}

	private Color getDeadColor() {
		if(! blackColor.isPresent() )
			blackColor = Optional.of(shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		return blackColor.get();
	}

	private Color getAliveColor() {
		if(liveColor == null)
			liveColor = shell.getDisplay().getSystemColor(SWT.COLOR_GREEN);
		return liveColor;
	}

	private Composite createCell(Composite container, Color color, Point point) {
		Composite cell = new Composite(container, SWT.NONE);
		cell.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		FillLayout layout = new FillLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		cell.setLayout(layout);
		cell.addMouseListener(new CellDoubleClickListener(model, point));
		cell.setBackground(color);
		attachMenu(point, cell, model.getAvailableShapes());
		return cell;
	}

	private void attachMenu(Point point, Composite cell, List<ShapeCathegory> shapeCathegories) {
		Menu popupMenu = new Menu(shell);
		
		for(ShapeCathegory cathegory : shapeCathegories) {
			MenuItem stillShapesItem = new MenuItem(popupMenu, SWT.CASCADE);
			stillShapesItem.setText(cathegory.getName());
		    
		    Menu stillSHapesMenu = new Menu(popupMenu);
		    stillShapesItem.setMenu(stillSHapesMenu);
		    
		    for(ShapeType type : cathegory.getShapeTypes()) {
		    	MenuItem blockItem = new MenuItem(stillSHapesMenu, SWT.NONE);
				blockItem.setText(type.getName());
			    blockItem.addSelectionListener(new SelectionAdapter() {
			    	
			    	@Override
			    	public void widgetSelected(SelectionEvent e) {
			    		model.onDrawShape(point, type);
			    	}
			    	
				});
		    }
		}
		
	    MenuItem clearItem = new MenuItem(popupMenu, SWT.CASCADE);
	    clearItem.setText("Clear");
	    
	    clearItem.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		updateStreaming();
	    		model.onClear();
	    	}
	    	
		});
	    
	    MenuItem randomItem = new MenuItem(popupMenu, SWT.CASCADE);
	    randomItem.setText("Generate Random");
	    
	    randomItem.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		updateStreaming();
	    		model.onGenerateRandomPoints();
	    	}
	    	
		});
	    
	    cell.setMenu(popupMenu);
	}

	@Override
	public void setDead(Point point) {
		getCell(point).setBackground(getDeadColor());
	}

	@Override
	public void setAlive(Point point) {
		getCell(point).setBackground(getAliveColor());
	}
	
	private Composite getCell(Point point) {
		return matrix.get(point.getRow() - 1).get(point.getColumn() - 1);
	}

	@Override
	public void setPlayStop(String text) {
		btnPlayStop.setText(text);		
	}
	
	private class Play implements Runnable { // TODO cleanpup
		
		@Override
		public void run() {
			try {
				if(isStreaming) {
					model.onNextTick();
					Thread.sleep(interval * 100);
					if(isStreaming)
						shell.getDisplay().asyncExec(new Play());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}				
		}
	}
}
