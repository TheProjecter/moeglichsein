package at.ac.univie.philo.mmr.client.gui;

import java.util.ArrayList;

import at.ac.univie.philo.mmr.shared.examples.UniverseFactory;
import at.ac.univie.philo.mmr.shared.semantic.Individual;
import at.ac.univie.philo.mmr.shared.semantic.Universe;
import at.ac.univie.philo.mmr.shared.semantic.World;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

public final class UniverseTreeModel implements TreeViewModel {

	private ArrayList<Universe> universes_;
	private final SingleSelectionModel<Individual> selectionModel = new SingleSelectionModel<Individual>();
	private final SingleSelectionModel<World> selectionModelWorld = new SingleSelectionModel<World>();
	private final SingleSelectionModel<Universe> selectionModelUniverse = new SingleSelectionModel<Universe>();
	private ListDataProvider<Universe> dataProvider;
	private ListDataProvider<World> dataProviderWorld;
	private ListDataProvider<Individual> dataProviderIndi;
	private Ressources res = null;
//	private CellTree tree;
	private final MainScreen parentWidget;
	
	public UniverseTreeModel(final MainScreen parentWidget) {

		this.parentWidget = parentWidget;
		UniverseFactory universeFactory = UniverseFactory.get();
		universes_ = new ArrayList<Universe>();
		universes_.add(universeFactory.getPizzaUniverse());
	    if (res == null) {
	        res = GWT.create(Ressources.class);
	    }
	 // Create a data provider that contains the list of universes.
		dataProvider = new ListDataProvider<Universe>(
	            universes_);
		ArrayList<World> worlds = new ArrayList<World>(universes_.get(0).getWorlds());
		dataProviderWorld = new ListDataProvider<World>(worlds);
		ArrayList<Individual> indis = new ArrayList<Individual>(universes_.get(0).getConstantMap().values());
		dataProviderIndi = new ListDataProvider<Individual>(indis);
		
	    
	    //The selectionModel configurations
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
		      public void onSelectionChange(SelectionChangeEvent event) {
		          Individual selected = selectionModel.getSelectedObject();
		          if (selected != null) {
//		            Window.alert("You selected Indi: " + selected.getName());
		          }
		          
		        }
		      });
		
		selectionModelWorld.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
		      public void onSelectionChange(SelectionChangeEvent event) {
		          World selected = selectionModelWorld.getSelectedObject();
		          if (selected != null) {
		           selectionModelUniverse.setSelected(selected.getUniverse(), false);
		           parentWidget.showSelectedWorld(selected);
		          }
		        }
		      });
		
		
		selectionModelUniverse.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
		      public void onSelectionChange(SelectionChangeEvent event) {
		          Universe selected = selectionModelUniverse.getSelectedObject();
		          if (selected != null) {
//		            Window.alert("You selected Universe: " + selected.getName());
		            parentWidget.showSelectedUniverse(selected);
		          }
		        }
		      });
		
	}
	
	public void updateModel(Universe u) {
		dataProvider.refresh();
		dataProviderIndi.refresh();
		ArrayList<World> worlds = new ArrayList<World>(u.getWorlds());
        dataProviderWorld.setList(worlds);
		dataProviderWorld.refresh();
	}
	
	public void addDataView(HasData<World> hasData) {
		dataProviderWorld.addDataDisplay(hasData);
	}
	
	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		
		if (value == null) {
	        // LEVEL 0.
	        // We passed null as the root value. Return the availables Universe(s).

	        dataProvider.refresh();

	        // Create a cell to display a Universe
	        Cell<Universe> cell = new UniverseCell();

	        // Return a node info that pairs the data provider and the cell.
	        return new DefaultNodeInfo<Universe>(dataProvider, cell, selectionModelUniverse, null);
		} else if (value instanceof Universe) {
	        // LEVEL 1.
	        // We want the children of the Universe. Return the Worlds.
			Universe universe = (Universe) value;
			ArrayList<World> worlds = new ArrayList<World>(universe.getWorlds());
	        dataProviderWorld.setList(worlds);
	        // Create a cell to display a Universe
	        Cell<World> cell = new WorldCell(universe);
	        
	        return new DefaultNodeInfo<World>(dataProviderWorld, cell, selectionModelWorld, null);
	      } else if (value instanceof World) {
	        // LEVEL 2 - LEAF.
	        // We want the children of the playlist. Return the songs.
	    	  ArrayList<Individual> indis = new ArrayList<Individual>(((World) value).getInventory());
	         dataProviderIndi = new ListDataProvider<Individual>(
	            (indis));

	        // Create a cell to display a Universe
	        Cell<Individual> cell = new IndividualCell((World)value);
	        
	        
	        // Use the shared selection model.
	        return new DefaultNodeInfo<Individual>(dataProviderIndi, cell, selectionModel, null);
	      }

	      return null;
	}

	private class IndividualCell extends AbstractCell<Individual> {
		
			World world;

			public IndividualCell(World world) {
				super("click", "keydown");
				this.world = world;
			}
		
		
	          @Override
	          public void render(Context context, Individual individual, SafeHtmlBuilder sb) {
	            if (individual != null) {
	              sb.appendEscaped(individual.getName());
	            }
	          }
	          
	  		@Override
			public void onBrowserEvent(Context context, Element parent, Individual value,
				      NativeEvent event, ValueUpdater<Individual> valueUpdater) {
				    String eventType = event.getType();
				    // Special case the ENTER key for a unified user experience.
				    if ("keydown".equals(eventType) && event.getKeyCode() == KeyCodes.KEY_ENTER) {
				      onEnterKeyDown(context, parent, value, event, valueUpdater);
				    }
				    selectionModelWorld.setSelected(world, false);
				    selectionModelUniverse.setSelected(world.getUniverse(), false);
				    Window.alert("blub. Individual "+value.toString()+" of World: "+world.getName());
				  }
	          
	          
	}
	
	private class WorldCell extends AbstractCell<World> {
		private AbstractImagePrototype abstractImagePrototype = AbstractImagePrototype.create(res.world());
		private Image worldImg = abstractImagePrototype.createImage();
		private final String imageHtml = abstractImagePrototype.getHTML();
		private Universe universe;
    	
    	public WorldCell(Universe universe) {
    		super("click", "keydown", "mouseover");
    		 this.universe = universe;

    	}
    	
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				World world, SafeHtmlBuilder sb) {
	            if (world != null) {
	              sb.appendHtmlConstant("<table><tr><td>");
	              sb.appendHtmlConstant(imageHtml).appendEscaped(" ");
	              sb.appendHtmlConstant("</td><td>");
	              sb.appendEscaped(world.getName());
	              sb.appendHtmlConstant("</td></tr></table>");
	            }
		}
		
		@Override
		public void onBrowserEvent(Context context, Element parent, World value,
			      NativeEvent event, ValueUpdater<World> valueUpdater) {
			    String eventType = event.getType();
			    // Special case the ENTER key for a unified user experience.
			    if ("keydown".equals(eventType) && event.getKeyCode() == KeyCodes.KEY_ENTER) {
			      onEnterKeyDown(context, parent, value, event, valueUpdater);
			    }
//			    if ("mouseover".equals(eventType)){   	
//			    	VerticalPanel interactionPanel = new VerticalPanel();
//			    	Label showAccessTable = new Label("Show Accessability Table");
//			    	Label showExtension = new Label("Browse Predicate Extensions");
//			    	Label renameWorld = new Label("Rename World");
//			    	interactionPanel.add(showAccessTable);
//			    	interactionPanel.add(showExtension);
//			    	interactionPanel.add(renameWorld);
//			    	
//			    	PopupPanel worldInfoPopup = new PopupPanel();
//			    	worldInfoPopup.setWidget(interactionPanel);
//			    	worldInfoPopup.show();
//			    }
			  }
	}
	
	private class UniverseCell extends AbstractCell<Universe> {
		
    		public UniverseCell() {
    			super("click", "keydown");
    		}
		
	          @Override
	          public void render(Context context, Universe universe, SafeHtmlBuilder sb) {
	            if (universe != null) {
	              sb.appendEscaped(universe.getName());
	            }
	          }
        
	  		@Override
			public void onBrowserEvent(Context context, Element parent, Universe value,
				      NativeEvent event, ValueUpdater<Universe> valueUpdater) {
				    String eventType = event.getType();
				    // Special case the ENTER key for a unified user experience.
				    if ("keydown".equals(eventType) && event.getKeyCode() == KeyCodes.KEY_ENTER) {
				      onEnterKeyDown(context, parent, value, event, valueUpdater);
				    }
				  }
        
	}
		
	
	@Override
	public boolean isLeaf(Object value) {
		return (value instanceof Individual);
	}

	
	//not needed now..
//	public CellTree getTree() {
//		return tree;
//	}
//
//	public void setTree(CellTree tree) {
//		this.tree = tree;
//	}

}