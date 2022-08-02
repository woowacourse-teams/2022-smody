package com.woowacourse.smody.image;

import java.io.File;

public class FilePathBuilder {

	private final StringBuilder path;

	private FilePathBuilder(String root) {
		path = new StringBuilder();
		path.append(root);
	}

	public static FilePathBuilder root(String root) {
		return new FilePathBuilder(root);
	}

	public FilePathBuilder addPath(String path) {
		this.path.append("/")
			.append(path);
		return this;
	}

	public FilePathBuilder mkdir() {
		File folder = new File(path.toString());
		if (!folder.exists()) {
			folder.mkdirs();
		}
		return this;
	}

	public FilePathBuilder setFileName(String fileName) {
		this.path.append("/")
			.append(fileName);
		return this;
	}

	public FilePathBuilder setExtension(String extension) {
		this.path.append(".")
			.append(extension);
		return this;
	}

	public String build() {
		return path.toString();
	}
}
