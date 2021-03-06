package org.ericminio.btrs.workers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.ericminio.btrs.workers.MavenCompiler;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;

public class MavenCompilerTest {

	private MavenCompiler compiler;
	
	@Before public void
	init() {
		compiler = new MavenCompiler();
	}
	
	@Test public void 
	canConfigureWorkingDirectory() {
		compiler.setWorkingDirectory( "a-test-path-to-configure-maven-compiler" );
		assertThat( compiler.getWorkingDirectory(), equalTo( "a-test-path-to-configure-maven-compiler" ) );
	}

	@Test public void 
	mavenCompilerCommand() {
		assertThat( compiler.getCompilerCommand(), equalTo( "mvn clean cobertura:cobertura" ) );
	}
	
	@Test public void
	canConfigureRuntime() {
		Runtime doubleRuntime = mock( Runtime.class );
		compiler.setRuntime( doubleRuntime );
		assertThat( compiler.getRuntime(), equalTo( doubleRuntime ) );
	}
	
	@Test public void
	defaultRuntimeIsTheCurrentRuntime() {
		assertThat( compiler.getRuntime(), equalTo( Runtime.getRuntime() ) );
	}
	
	@Test public void
	executesCommandWithCorrectParameters() throws Exception {
		Runtime runtimeMock = aRuntimeMock(); 
		compiler.setRuntime( runtimeMock );
		compiler.setWorkingDirectory( "a-test-path-to-verify-maven-compiler-command" );
		compiler.work();
		verify( runtimeMock ).exec( compiler.getCompilerCommand(), 
									null,
									new File( "a-test-path-to-verify-maven-compiler-command" ) );
	}
	
	private Runtime aRuntimeMock() throws IOException {
		Runtime toReturn = mock( Runtime.class );
		Process processMock = mock( Process.class );
		when( toReturn.exec( anyString(), (String[])anyObject(), (File)anyObject() ) ).thenReturn( processMock );
		return toReturn;
	}
}
