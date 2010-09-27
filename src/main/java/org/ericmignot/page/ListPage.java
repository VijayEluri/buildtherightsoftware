package org.ericmignot.page;

import static org.ericmignot.util.FileUtils.readFile;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.ericmignot.adapters.ListRenderer;
import org.ericmignot.adapters.Spec;
import org.ericmignot.adapters.SpecRepository;

public class ListPage implements ListRenderer {

	private SpecRepository repository;
	
	public void setRepository(SpecRepository repo) {
		this.repository = repo;
	}

	public void render(Writer out) {
		try {
			String template = pageTemplate();
			String page = template.replaceAll( "page-content", pageContent() );
			out.write( page );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected String pageTemplate() throws IOException {
		String template = readFile( "target/html/template.html" );
		return template;
	}
	
	protected String pageContent() throws IOException {
		String list = "<ul>";
		List<Spec> specs = repository.getSpecs();
		for (Spec spec : specs) {
			list += "<li><a class=\"list\" href=\"/specs/show/" + spec.getTitle() + "\" >" + spec.getTitle() + "</a></li>";
		}
		list += "</ul>";
		return list;
	}

}