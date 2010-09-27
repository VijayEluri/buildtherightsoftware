package org.ericmignot.application;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ericmignot.adapters.LegacyPage;
import org.ericmignot.route.LegacyCreateRoute;
import org.ericmignot.route.LegacyNewRoute;
import org.ericmignot.route.LegacyPageBuilder;
import org.ericmignot.route.LegacySaveRoute;

public class LegacyRouter {

	private List<LegacyPageBuilder> candidates;
	
	public LegacyRouter() {
		candidates = new ArrayList<LegacyPageBuilder>();
		candidates.add( new LegacySaveRoute() );
		candidates.add( new LegacyNewRoute() );
		candidates.add( new LegacyCreateRoute() );
	}
	
	public LegacyPage choosePage(HttpServletRequest request) {
		for (LegacyPageBuilder candidate : candidates) {
			if ( candidate.isActivatedBy( request )) {
				return candidate.buildsPage( request );
			}
		}
		return null;
	}
	
}
