package org.ericmignot.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ericmignot.adapters.domain.Spec;
import org.ericmignot.adapters.store.SpecRepository;
import org.ericmignot.domain.SpecMatcher;

public class InMemoryRepository implements SpecRepository {

	protected Map<String, Spec> specs;
	
	public InMemoryRepository() {
		setSpecStoringMap( new HashMap<String, Spec>() );
	}
	
	protected void setSpecStoringMap(Map<String, Spec> map) {
		this.specs = map;
	}
	
	public void saveSpec(Spec spec) {
		this.specs.put( spec.getTitle() , spec);
	}

	public Spec getSpecByTitle(String title) {
		return specs.get( title );
	}

	public List<Spec> getSpecs(SpecMatcher matcher) {
		List<Spec> filtered = new ArrayList<Spec>();
		for (Spec spec : specs.values()) {
			if ( matcher.matches(spec) ) {
				filtered.add( spec );
			}
		}
		return filtered;
	}
}
