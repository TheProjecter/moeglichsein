package at.ac.univie.philo.mmr.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import at.ac.univie.philo.mmr.shared.expressions.Predicate;
import at.ac.univie.philo.mmr.shared.semantic.Individual;
import at.ac.univie.philo.mmr.shared.semantic.Universe;
import at.ac.univie.philo.mmr.shared.semantic.World;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.google.gwt.view.client.TreeViewModel.DefaultNodeInfo;

public class PredicateExtensionsModel implements TreeViewModel {

	private final SingleSelectionModel<Individual> selectionModel = new SingleSelectionModel<Individual>();
	private final Cell<Individual>  compositeIndiCell;
	private HashMap<Predicate, HashSet<ArrayList<Individual>>> extensionMap;
	private Ressources res;
	
	private ListDataProvider<Entry<Predicate, HashSet<ArrayList<Individual>>>> dataProviderExtensionMap;
	private ListDataProvider<ArrayList<Individual>> dataProviderExtension;
	private ListDataProvider<Individual> dataProviderIndis;
	
	public PredicateExtensionsModel() {
		res = GWT.create(Ressources.class);
		compositeIndiCell = createCompositeCell();
		dataProviderExtensionMap = new ListDataProvider<Entry<Predicate, HashSet<ArrayList<Individual>>>>();
		dataProviderExtension = new ListDataProvider<ArrayList<Individual>>();
		dataProviderIndis = new ListDataProvider<Individual>();
	}
	
	private CompositeCell<Individual> createCompositeCell() {
		// Construct a composite cell for contacts that includes a checkbox.
	    List<HasCell<Individual, ?>> hasCells = new ArrayList<HasCell<Individual, ?>>();
//	    hasCells.add(new HasCell<Individual, Boolean>() {
//
//	      private CheckboxCell cell = new CheckboxCell(true, false);
//
//	      public Cell<Boolean> getCell() {
//	        return cell;
//	      }
//
//	      public FieldUpdater<Individual, Boolean> getFieldUpdater() {
//	        return null;
//	      }
//
//	      public Boolean getValue(Individual object) {
//	        return selectionModel.isSelected(object);
//	      }
//	    });
	    hasCells.add(new HasCell<Individual, Individual>() {

	      private IndividualCell cell = new IndividualCell();

	      public Cell<Individual> getCell() {
	        return cell;
	      }

	      public FieldUpdater<Individual, Individual> getFieldUpdater() {
	        return null;
	      }

	      public Individual getValue(Individual object) {
	        return object;
	      }
	    });
	    
	    return new CompositeCell<Individual>(hasCells) {
		      @Override
		      public void render(Context context, Individual value, SafeHtmlBuilder sb) {
		        sb.appendHtmlConstant("<table><tbody><tr>");
		        super.render(context, value, sb);
		        sb.appendHtmlConstant("</tr></tbody></table>");
		      }

		      @Override
		      protected Element getContainerElement(Element parent) {
		        // Return the first TR element in the table.
		        return parent.getFirstChildElement().getFirstChildElement().getFirstChildElement();
		      }

		      @Override
		      protected <X> void render(Context context, Individual value,
		          SafeHtmlBuilder sb, HasCell<Individual, X> hasCell) {
		        Cell<X> cell = hasCell.getCell();
		        sb.appendHtmlConstant("<td>");
		        cell.render(context, hasCell.getValue(value), sb);
		        sb.appendHtmlConstant("</td>");
		      }
		    };
	    
 }


	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		
		if(value == null) {
			Window.alert("Should not happen that value is null.");
			return null;
		}
		
		if (value instanceof World) {
			World w = (World) value;
			this.extensionMap = w.getExtensionMap();
			
			// Create a data provider that contains the list of universes.
			ArrayList<Entry<Predicate, HashSet<ArrayList<Individual>>>> extensionEntryList = new ArrayList<Entry<Predicate, HashSet<ArrayList<Individual>>>>(extensionMap.entrySet());
	        dataProviderExtensionMap.setList(extensionEntryList);
			

	        // Create a cell to display a Predicate
	        Cell<Entry<Predicate, HashSet<ArrayList<Individual>>>> cell = new PredicateCell();

	        // Return a node info that pairs the data provider and the cell.
	        return new DefaultNodeInfo<Entry<Predicate, HashSet<ArrayList<Individual>>>>(dataProviderExtensionMap, cell);
			
		}
		
		if (value instanceof Entry<?,?>) {
			Entry<Predicate, HashSet<ArrayList<Individual>>> entry = (Entry<Predicate, HashSet<ArrayList<Individual>>>) value;
			ArrayList<ArrayList<Individual>> extensionList = new ArrayList<ArrayList<Individual>>(entry.getValue());
			dataProviderExtension.setList(extensionList);
			
	        Cell<ArrayList<Individual>> cell = new ExtensionCell();
	        
	        return new DefaultNodeInfo<ArrayList<Individual>>(dataProviderExtension, cell);
		}
		
		if (value instanceof ArrayList<?>) {
			ArrayList<Individual> ai = (ArrayList<Individual>) value;
			dataProviderIndis.setList(ai);
			
			return new DefaultNodeInfo<Individual>(dataProviderIndis,compositeIndiCell, selectionModel, null);
			
		}
		Window.alert("value ..... "+value.toString()+"  ..... unkown");
		return null;
		
		
	}

	@Override
	public boolean isLeaf(Object value) {
		return value instanceof Individual;
	}

	
	private class PredicateCell extends AbstractCell<Entry<Predicate, HashSet<ArrayList<Individual>>>> {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				Entry<Predicate, HashSet<ArrayList<Individual>>> value,
				SafeHtmlBuilder sb) {
			Predicate p = value.getKey();
			sb.appendHtmlConstant("<ul><li><b>");
			sb.appendEscaped(p.getName()+"( ");
			int numArity = p.getArity();
			for(int i=1; i<=numArity; i++) {
				if(i!= 1) {
					sb.appendEscaped(", ");
				}
				sb.appendHtmlConstant("<font color='maroon'>x"+i+"</font>");
			}
			
            sb.appendHtmlConstant(" )</b> <font color=#c0c0c0>("+value.getValue().size()+")</font></li></ul>");
		}
		
	}
	
	private class ExtensionCell extends AbstractCell<ArrayList<Individual>> {

		private final String imageHtml = AbstractImagePrototype.create(res.individual()).getHTML();
		
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				ArrayList<Individual> value, SafeHtmlBuilder sb) {
			
			int count = 0;
			sb.appendHtmlConstant("<b>{</b> ");
			
			for (Individual i : value) {
				if(count!=0) {
					sb.appendHtmlConstant("<b>,</b> ");
				}
				sb.appendHtmlConstant("<font color='maroon'>");
				sb.appendEscaped(i.toString());
				sb.appendHtmlConstant("</font>");
				count++;
			}
			sb.appendHtmlConstant(" <b>}</b>");
		}

		
	}
	
	private class IndividualCell extends AbstractCell<Individual> {

		private final String imageHtml = AbstractImagePrototype.create(res.individual()).getHTML();
		
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				Individual value, SafeHtmlBuilder sb) {	
				sb.appendHtmlConstant(imageHtml).appendEscaped(" ");
				sb.appendEscaped(value.toString());
			}
	}
		
	
}
