package org.ericmignot.page;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.ericmignot.FileHandler;
import org.ericmignot.PageHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class HomePageTryCodeSystemTest {

	private Server server;
	private Thread thread;
	
	@Before public void
	setUp() throws Exception {
		server = new Server(8080);
		
		PageHandler testPageHandler = new PageHandler();
		testPageHandler.setPageChooser( new TestPageChooser() );
        
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
	
	@After public void
	tearDown() throws Exception {
		server.stop();
		thread.stop();
	}
	
	@Test public void
	formSubmission() throws InterruptedException {
        WebDriver driver = new HtmlUnitDriver(true);
        
        driver.get("http://localhost:8080/");
        WebElement link = driver.findElement(By.name("tryCodeLink"));
        link.click();

      	assertEquals( "Url", "http://localhost:8080/specs/sample/execute?repo=git%3A%2F%2Fgithub.com%2Ftestaddict%2Fmastermind.git", driver.getCurrentUrl() );
	}
	
	private class TestPageChooser extends PageChooser {
		
		public Page choosePage(HttpServletRequest request) {
			Page choosen = super.choosePage( request );
			if ( choosen instanceof ResultPage ) {
				ResultPage resultPage = (ResultPage) choosen;
				resultPage.setRunnerDirectory( "target/test-classes/test-system/" );
				return resultPage;
			} else {
				return choosen;
			}
		}
	}
	
}
