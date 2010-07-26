package org.ericmignot;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.ericmignot.jetty.FileHandler;
import org.ericmignot.jetty.PageHandler;
import org.ericmignot.page.ModifyPage;
import org.ericmignot.page.Page;
import org.ericmignot.page.ResultPage;
import org.ericmignot.page.SavePage;
import org.ericmignot.page.ShowPage;
import org.ericmignot.router.PageRouter;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class SystemTest {

	private static Server server;
	private static Thread thread;
	private WebDriver driver;
	
	@BeforeClass public static void
	setUp() throws Exception {
		server = new Server(8080);
		
		PageHandler testPageHandler = new PageHandler();
		testPageHandler.setPageRouter( new TestPageChooser() );
        
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { 
        		new FileHandler(), 
        		testPageHandler });
        server.setHandler(handlers);
 
        thread = new Thread(new Runnable() {
			public void run() {
		        try {
					server.start();
			        server.join();				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
        thread.start();
	}
	
	@AfterClass public static void
	tearDown() throws Exception {
		server.stop();
		thread.stop();
	}
	
	@Before public void
	initDriver() {
		driver = new HtmlUnitDriver(true);
	}
	
	@Test public void
	canShowASpec() {
		driver.get("http://localhost:8080/specs/show/calculator-sample");  
		String source = driver.getPageSource();
		assertThat( "rule for calculator", source, containsString( "<td>calculator</td>" ) );
	}
	
	@Test public void
	canExecuteASpec() throws InterruptedException {
        driver.get("http://localhost:8080/specs/show/execution-sample");
        WebElement link = driver.findElement(By.name("tryCodeLink"));
        link.click();

      	assertEquals( "Url", "http://localhost:8080/specs/execute/execution-sample?repo=git%3A%2F%2Fgithub.com%2Ftestaddict%2Fmastermind.git", driver.getCurrentUrl() );
      	
      	String source = driver.getPageSource();
      	assertThat( "spec passes", source, containsString( "background-color: #AAFFAA;") );
	}
	
	@Test public void
	canModifyASpec() {
		driver.get("http://localhost:8080/specs/modify/save-sample");
		WebElement textarea = driver.findElement(By.name("specX"));
		textarea.clear();
		CharSequence seq = new CharSequence() {
			public CharSequence subSequence(int start, int end) {
				return null;
			}
			public int length() {
				return 4;
			}
			public char charAt(int index) {
				return "toto".charAt(index);
			}
		};
		textarea.sendKeys(seq);
		
		WebElement saveLink = driver.findElement(By.name("saveSpecXLink"));
        saveLink.click();
        
        assertEquals( "http://localhost:8080/specs/save/save-sample", driver.getCurrentUrl() );
        WebElement modifyLink = driver.findElement(By.name("modifyLink"));
        assertNotNull("modify link present", modifyLink);
        
        String source = driver.getPageSource();
        assertThat( "modification saved", source, containsString( "toto" ) );
	}
	
	
	
	
	   
	
	
	
	private static class TestPageChooser extends PageRouter {
		
		public Page choosePage(HttpServletRequest request) {
			Page choosen = super.choosePage( request );
			if ( choosen instanceof ResultPage ) {
				ResultPage resultPage = (ResultPage) choosen;
				resultPage.setRunnerDirectory( "target/test-classes/test-system/" );
				return resultPage;
			} 
			if ( choosen instanceof ShowPage ) {
				ShowPage showPage = (ShowPage) choosen;
				showPage.setSpecXDirectory( "target/test-classes/test-system/" );
				return showPage;
			}	
			if ( choosen instanceof ModifyPage ) {
				ModifyPage modifyPage = (ModifyPage) choosen;
				modifyPage.setSpecXDirectory( "target/test-classes/test-system/" );
				return modifyPage;
			}
			if ( choosen instanceof SavePage ) {
				SavePage savePage = (SavePage) choosen;
				savePage.setSpecXDirectory( "target/test-classes/test-system/" );
				return savePage;
			}
			
			return choosen;
		}
	}
	
}
