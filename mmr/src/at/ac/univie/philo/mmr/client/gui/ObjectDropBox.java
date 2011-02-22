package at.ac.univie.philo.mmr.client.gui;

import java.util.ArrayList;
import java.util.Set;

import at.ac.univie.philo.mmr.shared.semantic.World;

import com.google.gwt.user.client.ui.ListBox;

public class ObjectDropBox<T> extends ListBox {

	private ArrayList<T> objects;

	public ObjectDropBox() {
		super();
		objects = new ArrayList<T>();
		this.setVisibleItemCount(1);

	}

	public ObjectDropBox(Set<T> objects) {
		this();
		if (objects != null) {
			for (T w : objects) {
				addItem(w);
			}
		}
	}

	public void addItem(T w) {
		objects.add(w);
		int index = (objects.size() - 1);
		addItem(w.toString(), String.valueOf(index));
	}

	public T getSelectedObject() {
		int index = this.getSelectedIndex();
		if (index != -1) {
			return objects.get(Integer.valueOf(getValue(index)));
		}
		return null;
	}
}
