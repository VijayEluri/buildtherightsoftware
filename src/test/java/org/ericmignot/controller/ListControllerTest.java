package org.ericmignot.controller;

import static org.ericmignot.util.HttpServletRequestMockBuilder.aMockRequest;
import static org.ericmignot.util.RepositoryMockBuilder.aMockRepo;
import static org.ericmignot.util.SpecBuilder.aSpec;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.Writer;

import org.ericmignot.adapters.ListRenderer;
import org.ericmignot.adapters.Spec;
import org.ericmignot.adapters.SpecRepository;
import org.junit.Before;
import org.junit.Test;

public class ListControllerTest {

	private ListController controller;
	private Writer writerMock;
	
	@Before public void
	init() {
		controller = new ListController();
		writerMock = mock( Writer.class );
	}
	
	@Test public void
	activationSpecification() {
		assertTrue( "activation", controller.isActivatedBy( aMockRequest().withThisUri( "/specs/list").build() ) );
		assertFalse( "activation", controller.isActivatedBy( aMockRequest().withThisUri( "/specs/list/toto").build() ) );
		assertFalse( "activation", controller.isActivatedBy( aMockRequest().withThisUri( "/specs/listtoto").build() ) );
		assertFalse( "don't activate", controller.isActivatedBy( aMockRequest().withThisUri( "/" ).build() ) );
		assertFalse( "don't activate", controller.isActivatedBy( aMockRequest().withThisUri( "" ).build() ) );
		assertFalse( "don't activate", controller.isActivatedBy( aMockRequest().withThisUri( null ).build() ) );
		assertFalse( "don't activate", controller.isActivatedBy( aMockRequest().withThisUri( "/specs/show" ).build() ) );
		assertFalse( "don't activate", controller.isActivatedBy( aMockRequest().withThisUri( "/specs/show/" ).build() ) );
	}
	
	@Test public void
	rendersTheViewDuringWork() {
		ListRenderer rendererMock = mock( ListRenderer.class );
		controller.setRenderer( rendererMock );
		
		Spec spec = aSpec().withTitle( "sample" ).build();
		SpecRepository repoMock = aMockRepo().withSpec( spec ).build();
		
		controller.handle( aMockRequest().withThisUri( "/specs/list").build(), repoMock, writerMock );
		verify( rendererMock ).setRepository( repoMock );
		verify( rendererMock ).render( writerMock );
	}
}