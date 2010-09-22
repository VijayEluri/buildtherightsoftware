package org.ericmignot.action;

import static org.ericmignot.util.HttpServletRequestMockBuilder.aMockRequest;
import static org.ericmignot.util.RepositoryMockBuilder.aMockRepo;
import static org.ericmignot.util.SpecBuilder.aSpec;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.Writer;

import org.ericmignot.core.Spec;
import org.ericmignot.jetty.View;
import org.ericmignot.page.ShowPage;
import org.ericmignot.store.Repository;
import org.junit.Before;
import org.junit.Test;

public class ShowTest {

	private Show action;
	
	@Before public void
	init() {
		action = new Show();
	}
	
	@Test public void
	activationSpecification() {
		assertTrue( "activation", action.isActivatedBy( aMockRequest().withThisUri( "/specs/show/sample").build() ) );
		assertTrue( "activation", action.isActivatedBy( aMockRequest().withThisUri( "/specs/show/sample-calculator").build() ) );
		assertFalse( "don't activate", action.isActivatedBy( aMockRequest().withThisUri( "/" ).build() ) );
		assertFalse( "don't activate", action.isActivatedBy( aMockRequest().withThisUri( "" ).build() ) );
		assertFalse( "don't activate", action.isActivatedBy( aMockRequest().withThisUri( null ).build() ) );
		assertFalse( "don't activate", action.isActivatedBy( aMockRequest().withThisUri( "/specs/show" ).build() ) );
		assertFalse( "don't activate", action.isActivatedBy( aMockRequest().withThisUri( "/specs/show/" ).build() ) );
	}
	
	@Test public void
	theRenderingViewIsAShowView() {
		assertTrue( action.getView() instanceof ShowPage );
	}
	
	@Test public void
	rendersTheViewDuringWork() {
		View viewMock = mock( View.class );
		action.setView( viewMock );
		Writer writerMock = mock( Writer.class );
		
		Spec spec = aSpec().withTitle( "sample" ).build();
		Repository repoMock = aMockRepo().withOneSpec( spec ).build();
		
		action.work( aMockRequest().withThisUri( "/specs/show/sample").build(), repoMock, writerMock );
		verify( repoMock ).getSpecByTitle( "sample" );
		verify( viewMock ).render( spec, writerMock );
	}
}
