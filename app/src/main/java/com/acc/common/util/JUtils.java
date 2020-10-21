/***********************************************************************
 * Module:  JUtils.java
 * Author:  Helong
 * Purpose: Defines the Class JUtils
 ***********************************************************************/
package com.acc.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JUtils {

	public static void copy(InputStream is, OutputStream os) {
		try {
			byte[] bytes = new byte[is.available()];
			is.read(bytes);
			os.write(bytes);
			os.flush();
			os.close();
			is.close();		
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void copy(InputStream is, File file) {
		try {
			FileOutputStream fileoutstrem = new FileOutputStream(file);
			byte[] bytes = new byte[is.available()];
			is.read(bytes);
			fileoutstrem.write(bytes);
			fileoutstrem.flush();
			fileoutstrem.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
