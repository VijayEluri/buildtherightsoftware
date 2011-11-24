package org.ericmignot.controller;

import static org.ericmignot.util.HttpServletRequestStubBuilder.aStubRequest;
import static org.ericmignot.util.RepositoryMockBuilder.aRepo;
import static org.ericmignot.util.SpecBuilder.aSpec;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.Writer;

import org.ericmignot.adapters.domain.Spec;
import org.ericmignot.adapters.store.SpecRepository;
import org.ericmignot.adapters.ui.SpecRenderer;
import org.junit.Before;
import org.junit.Test;

public class ModifyControllerTest {

	private ModifyController controller;
	
	@Before public void
	init() {
		controller = new ModifyController();
	}
	
	@Test public void
	activationSpecification() {
		assertTrue( "activation", controller.isActivatedBy( aStubRequest().withThisUri( "/specs/modify/sample" ).build() ) );
		assertTrue( "activation", controller.isActivatedBy( aStubRequest().withThisUri( "/specs/modify/sample-calculator").build() ) );
		assertFalse( "don't activate", controller.isActivatedBy( aStubRequest().withThisUri( "/" ).build() ) );
		assertFalse( "don't activate", controller.isActivatedBy( aStubRequest().withThisUri( "" ).build() ) );
		assertFalse( "don't activate", controller.isActivatedBy( aStubRequest().withThisUri( null ).build() ) );
		assertFalse( "don't activate", controller.isActivatedBy( aStubRequest().withThisUri( "/specs/modify" ).build() ) );
		assertFalse( "don't activate", controller.isActivatedBy( aStubRequest().withThisUri( "/specs/modify/" ).build() ) );
	}
	
	@Test public void
	rendersTheViewDuringWork() throws Exception {
		SpecRenderer viewMock = mock( SpecRenderer.class );
		controller.setRenderer( viewMock );
		
		Writer writerMock = mock( Writer.class );
		
		Spec spec = aSpec().withTitle( "sample" ).build();
		SpecRepository repoMock = aRepo().withSpec( spec ).build();
		
		controller.handle( aStubRequest().withThisUri( "/specs/modify/sample").build(), repoMock, writerMock );
		verify( viewMock ).setSpec( spec );
		verify( viewMock ).render( writerMock );
	}
}
