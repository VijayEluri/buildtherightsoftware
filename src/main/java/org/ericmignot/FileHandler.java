package org.ericmignot;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;

public class FileHandler extends AbstractHandler {

	private ResourceHandler resourceHandler;
	
	Map<String, String> fileDirectories;
	
	public FileHandler() {
		setResourceHandler(new ResourceHandler());
		fileDirectories = new HashMap<String, String>();
		fileDirectories.put(".png", "target/images");
		fileDirectories.put(".css", "target/styles");
	}
	
	protected String extractExtension(String path) {
		if (path.length() > 3) {
			String extension = path.substring(path.length() - 4, path.length());
			return extension;
		}
		else {
			return null;
		}
	}

	public void handle(String target, Request request,
			HttpServletRequest servletRequest, HttpServletResponse response)
			throws IOException, ServletException {
		
		String extension = extractExtension(target);
		if (fileDirectories.containsKey(extension)) {
			resourceHandler.setResourceBase(fileDirectories.get(extension));
			resourceHandler.handle(target, request, servletRequest, response);
		}
		else {
			request.setHandled(false);
			return;
		}
	}

	public void setResourceHandler(ResourceHandler resourceHandler) {
		this.resourceHandler = resourceHandler;
	}

	public ResourceHandler getResourceHandler() {
		return resourceHandler;
	}

}
