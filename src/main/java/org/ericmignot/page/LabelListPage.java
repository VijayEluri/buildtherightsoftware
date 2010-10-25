package org.ericmignot.page;

import static org.ericmignot.util.FileUtils.readFile;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ericmignot.adapters.domain.Spec;
import org.ericmignot.adapters.ui.ListRenderer;

public class LabelListPage implements ListRenderer {

	private List<Spec> specs;
	
	public void setSpecs(List<Spec> specs) {
		this.specs = specs;
	}

	public void render(Writer out) {
		try {
			String template = readFile( "target/html/template.html" );
			String page = template.replaceAll( "page-content", pageContent() );
			out.write( page );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected String pageContent() throws IOException {
		String list = "<ul>";
		Map<String, Integer> labels = labelSummary();
		for (String	label : labels.keySet()) {
			list += "<li><a class=\"label-filter\" href=\"/specs/list?label=" 
				+ label + "\" >" + label + " (" + labels.get(label) + ")</a></li>";
		}
		list += "</ul>";
		return list;
	}

	public Map<String, Integer> labelSummary() {
		Map<String, Integer> labels = new HashMap<String, Integer>();
		for (Spec spec : specs) {
			if (labels.containsKey( spec.getLabel() )) {
				int more = labels.get( spec.getLabel() ) + 1;
				labels.put( spec.getLabel(), more );
			}
			else {
				labels.put( spec.getLabel(), 1 );
			}
		}
		return labels;
	}

}
