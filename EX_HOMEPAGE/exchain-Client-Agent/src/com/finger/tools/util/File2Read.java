package com.finger.tools.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import com.finger.agent.config.FcAgentConfig;

public class File2Read {
	private static Logger logger = Logger.getLogger(File2Read.class);

	public File2Read() { }

	/**
	 * FcAgentConfig._CHAR_SET 형식으로 파일을 읽어 들인다. (None-BOM 파일이어야 함)
	 * @param fileName
	 */
	public static BufferedReader getBufferedReaderByFile11 (String fileName) {

		BufferedReader rtnReader = null;

		Charset charset = Charset.forName(FcAgentConfig._CharSet_);

		try {
			rtnReader = (new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charset)));
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
		return rtnReader;
	}

	/**
	 * FcAgentConfig._CHAR_SET 형식으로 파일을 읽어 들인다. (None-BOM 파일이어야 함)
	 * @param fileName
	 */
	public static ByteArrayOutputStream getOutputStreamByFile11 (String fileName) {

		ByteArrayOutputStream rtnBAOS = new ByteArrayOutputStream();

		Charset charset = Charset.forName(FcAgentConfig._CharSet_);

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charset));
			String str_line = null;
			while ( (str_line = reader.readLine()) != null ) {
				rtnBAOS.write(str_line.getBytes());
				rtnBAOS.write("\r\n".getBytes());
			}
			reader.close();
		} catch (FileNotFoundException fnfe) {
			logger.error(fnfe.getMessage(), fnfe);
		} catch (IOException ioe) {
			logger.error(ioe.getMessage(), ioe);
		}
		return rtnBAOS;
	}
}