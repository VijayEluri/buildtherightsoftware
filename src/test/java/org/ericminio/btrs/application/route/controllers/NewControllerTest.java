package org.ericminio.btrs.application.route.controllers;

import static org.ericminio.btrs.application.route.HttpServletRequestStubBuilder.aStubRequest;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.Writer;

import org.ericminio.btrs.application.route.controllers.NewController;
import org.ericminio.btrs.application.view.Renderer;
import org.ericminio.btrs.domain.SpecRepository;
import org.junit.Before;
import org.junit.Test;

public class NewControllerTest {

	private NewController controller;
	private Writer writerMock;
	
	@Before public void
	init() {
		controller = new NewController();
		writerMock = mock( Writer.class );
	}
	
	@Test public void
	activationSpecification() {
		assertTrue( "activation", controller.isActivatedBy( aStubRequest().withThisUri( "/specs/new").build() ) );
		assertFalse( "activation", controller.isActivatedBy( aStubRequest().withThisUri( "/specs/new/toto").build() ) );
		assertFalse( "activation", controller.isActivatedBy( aStubRequest().withThisUri( "/specs/newtoto").build() ) );
		assertFalse( "don't activate", controller.isActivatedBy( aStubRequest().withThisUri( "/" ).build() ) );
		assertFalse( "don't activate", controller.isActivatedBy( aStubRequest().withThisUri( "" ).build() ) );
		assertFalse( "don't activate", controller.isActivatedBy( aStubRequest().withThisUri( null ).build() ) );
	}
	
	@Test public void
	rendersTheViewDuringWork() throws Exception {
		Renderer rendererMock = mock( Renderer.class );
		controller.setRenderer( rendererMock );
		
		controller.handle( aStubRequest().withThisUri( "/specs/list").build(), mock(SpecRepository.class), writerMock );
		verify( rendererMock ).render( writerMock );
	}
}
