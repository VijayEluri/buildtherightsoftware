package org.ericminio.btrs.workers;

import java.io.File;

import org.ericminio.btrs.domain.SpecRunner;

public class GreenPepperRunner implements SpecRunner {

	private String directory;

	private String classpathRelativePath;
	private String specFileRelativePath;
	private String outRelativePath;

	public void setWorkingDirectory(String path) {
		this.directory = path;
	}

	public void setClassPathRelativeDirectory(String cpRelativePath) {
		this.classpathRelativePath = cpRelativePath;
	}

	public void setSpecFileRelativeFile(String specFileRelativePath) {
		this.specFileRelativePath = specFileRelativePath;
	}

	public void setOutputRelativeDirectory(String outRelativePath) {
		this.outRelativePath = outRelativePath;
	}

	public void work() throws Exception {
		File dir = new File(directory);
		String command = "java -cp greenpepper-core-2.7.jar:"
				+ classpathRelativePath + " com.greenpepper.runner.Main" + " "
				+ specFileRelativePath + " -o " + outRelativePath;
		Process process;
		process = Runtime.getRuntime().exec(command, null, dir);
		process.waitFor();
	}

}
