package com.finger.tools.util;

/**
 * Caller의 정보를  받아오기 위한 클래스
 */
public class ThrowTraceInfo {
	private String	caller		= null;
	private String	fileName	= null;
	private String	className	= null;
	private String	methodName	= null;
	private int		lineNumber	= 0x00;
	
	/**
	 * 예외 객체를 받아서 Caller 정보를 만든다.
	 * @param t
	 */
	public ThrowTraceInfo(Throwable t) {
		StackTraceElement element = t.getStackTrace()[1];
		
		this.fileName = element.getFileName();
		this.className = element.getClassName();
		this.methodName = element.getMethodName();
		this.lineNumber = element.getLineNumber();
		this.caller = new StringBuilder(this.className)
				.append(".")
				.append(this.methodName)
				.append("(")
				.append(this.fileName)
				.append(":")
				.append(this.lineNumber)
				.append(")")
				.toString();
	}
	
	public ThrowTraceInfo(Throwable t, int statkLevel) {
		StackTraceElement element = t.getStackTrace()[statkLevel];
		
		this.fileName = element.getFileName();
		this.className = element.getClassName();
		this.methodName = element.getMethodName();
		this.lineNumber = element.getLineNumber();
		this.caller = new StringBuilder(this.className)
				.append(".")
				.append(this.methodName)
				.append("(")
				.append(this.fileName)
				.append(":")
				.append(this.lineNumber)
				.append(")")
				.toString();
	}
	
	/**
	 * @return package.Class.Method(FileName.java:Line)
	 */
	public String getCaller() {
		return this.caller;
	}
	
	public String getFileName() {
		return this.getFileName();
	}
	
	public String getClassName() {
		return this.className;
	}
	
	public String getMethodName() {
		return this.methodName;
	}
	
	public int getLineNumber() {
		return this.lineNumber;
	}
}