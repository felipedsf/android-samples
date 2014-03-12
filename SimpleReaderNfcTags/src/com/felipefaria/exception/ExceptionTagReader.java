package com.felipefaria.exception;

public class ExceptionTagReader extends Exception {

	public ExceptionTagReader() {
		super();
	}

	public ExceptionTagReader(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ExceptionTagReader(String detailMessage) {
		super(detailMessage);
	}

	public ExceptionTagReader(Throwable throwable) {
		super(throwable);
	}

}
