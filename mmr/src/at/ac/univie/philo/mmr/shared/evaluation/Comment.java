package at.ac.univie.philo.mmr.shared.evaluation;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Comment implements IsSerializable{

	public ArrayList<String> lines;
	
	public Comment() {
		lines = new ArrayList<String>();
	}
	public Comment(String firstLineMsg) {
		this();
		if (firstLineMsg != null) {
			lines.add(firstLineMsg);
		}
	}
	
	public void addLine(String lineMsg) {
		if (lineMsg != null) {
			lines.add(lineMsg);
		}
	}
	
	public void addLines(Comment comment) {
		if (comment != null) {
			for (String s : comment.getLines()) {
					this.addLine("\t "+s);
			}
		}
	}
	
	public List<String> getLines() {
		return new ArrayList<String>(lines);
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i<=lines.size(); i++) {
			sb.append(i+": "+lines.get(i-1)+"\n");
		}
		return sb.toString();
	}
}
