package org.ericmignot;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.ericmignot.page.Page;
import org.ericmignot.page.PageChooser;
 
public class PageHandler extends AbstractHandler
{
	private PageChooser pageChooser;
	
	public PageHandler() {
		setPageChooser( new PageChooser() );
	}
	
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) 
        throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        
        Page choosen = pageChooser.choosePage(request);
        response.getWriter().println(choosen.html());
    }
    
    public PageChooser getPageChooser() {
		return pageChooser;
	}
	
	public void setPageChooser(PageChooser pageChooser) {
		this.pageChooser = pageChooser;
	}
 
}