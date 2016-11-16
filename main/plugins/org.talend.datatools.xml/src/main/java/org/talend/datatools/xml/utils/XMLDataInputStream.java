/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.talend.datatools.xml.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;



/**
 * This class creates a restart-able input stream instance from either an ordinary input stream
 * or an input url.
 */
public class XMLDataInputStream extends InputStream
{
	//
	private InputStream inputStream;
	
	//The url from the file or web address in which the InputStream come from.
	private URL url;
	
	
	/**
	 * 
	 * @param is
	 * @throws OdaException
	 */
	protected XMLDataInputStream( URL url ) throws OdaException
	{
		this.url = url;
	}

	/**
	 * @deprecated
	 * Temporary - for BZ 142789. Keep this for backward compatibility with TPTP RC2
	 * Can be removed after 2.1RC3
	 * 
	 */
	public XMLDataInputStream( InputStream is ) throws OdaException
	{
		BufferedInputStream bis = new BufferedInputStream(is);
				
		try
		{
			File file = createFileWithUniqueName( );
			FileOutputStream fos = new FileOutputStream( file );
			int abyte;
			while( (abyte = bis.read())!= -1)
			{	
				fos.write(abyte);
			}
			fos.close();
			url = file.toURL();
			inputStream = new BufferedInputStream(url.openStream());
		}
		catch ( IOException e )
		{
			throw new OdaException( e.getLocalizedMessage() );
		}
	}
	
	//The template file name which might be used to create a temp file for the InputStream
	private static final String TEMPFILENAME = "tempXMLData";
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private File createFileWithUniqueName( ) throws IOException
	{
		File file = File.createTempFile(TEMPFILENAME,null);
		file.deleteOnExit( );
		return file;
	}
	
	/**
	 * Reset the InputStream to the very first position.
	 * 
	 * @throws IOException
	 */
	public void init( ) throws IOException
	{
		if ( this.inputStream == null )
			this.inputStream = new BufferedInputStream( url.openStream( ) );
		else
		{
			this.inputStream.close( );
			this.inputStream = new BufferedInputStream( url.openStream( ) );
		}
	}

	public int read( ) throws IOException
	{
		return this.inputStream.read();
	}
	
	public void close( ) throws IOException
	{
		if ( this.inputStream != null )
			this.inputStream.close( );
		
		super.close();
	}
}
