/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.talend.datatools.xml.utils;

/**
 * An exception class that provides information on an ODA runtime driver 
 * error or other data provider errors.
 * <P>Each <code>OdaException</code> provides several kinds of information: 
 * <UL>
 * <LI> a string describing the error.  This is used as the Java Exception
 * message, available via the method <code>getMessage</code>.
 * <LI> a SQLSTATE string, which follows either the XOPEN SQLSTATE conventions
 * or the SQL 99 conventions.
 * The values of the SQLSTATE string are described in the corresponding spec.
 * The method <code>IDataSetMetaData.getSQLStateType()</code>
 * can be used to discover whether the driver returns the XOPEN type or
 * the SQL 99 type.
 * <LI> an integer exception code that is specific to each vendor.  Normally this will
 * be the actual error code returned by the underlying data provider.
 * <LI> a chain to the next OdaException.  This can be used to provide additional
 * error information.
 * </UL>
 * <P>
 * <b>Note:</b> The OdaException chain is different from the "Cause" that was 
 * introduced in Java SDK 1.4 Throwable.  The chain is meant to handle situations 
 * where multiple (possibly unrelated) OdaExceptions may occur.
 */
public class OdaException extends Exception
{
	private String m_sqlState;
	private int m_vendorCode;
	private OdaException m_nextException;
	private boolean m_isCauseSet = false;
	private Throwable m_cause = null;
	
	/**
	 * Instantiates an OdaException object whose message text defaults to null, 
	 * SQLSTATE defaults to null, and vendorCode defaults to 0.
	 */
	public OdaException()
	{
		super();
		m_sqlState = null;
		m_vendorCode = 0;
		m_nextException = null;
	}
	
	/**
	 * Instantiates an OdaException object with a message. 
	 * The SQLSTATE defaults to null, and vendorCode defaults to 0.
	 * @param message	description of the exception.
	 */
	public OdaException( String message )
	{
		super( message );
		m_sqlState = null;
		m_vendorCode = 0;
		m_nextException = null;
	}
	
	/**
	 * Instantiates an OdaException object with a message and SQLSTATE.  
	 * The vendorCode defaults to 0.
	 * @param message	description of the exception.
	 * @param sqlState	an XOPEN or SQL 99 code identifying the exception.
	 */
	public OdaException( String message, String sqlState )
	{
		super( message );
		m_sqlState = sqlState;
		m_vendorCode = 0;
		m_nextException = null;
	}
	
	/**
	 * Instantiates a fully specified OdaException object.
	 * @param message		description of the exception.
	 * @param sqlState		an XOPEN or SQL 99 code identifying the exception.
	 * @param vendorCode	a data provider vendor-specific exception code.
	 */
	public OdaException( String message, String sqlState, int vendorCode )
	{
		super( message );
		m_sqlState = sqlState;
		m_vendorCode = vendorCode;
		m_nextException = null;
	}
	
    /**
     * Instantiates an OdaException object whose message text 
     * defaults to null, and initialized with given cause.
     * @param cause     cause of exception
     */
    public OdaException( Throwable cause )
    {
        super();
        initCause( cause );
    }
    
	/**
	 * Returns the SQLSTATE of this <code>OdaException</code> object.
	 * @return	the SQLSTATE value.
	 */
	public String getSQLState()
	{
		return m_sqlState;
	}
	
	/**
	 * Returns the vendor-specific exception code of this <code>OdaException</code>
	 * object.
	 * @return	the vendor's exception code.
	 */
	public int getErrorCode()
	{
		return m_vendorCode;
	}
	
	/**
	 * Returns the next <code>OdaException</code> chained to this <code>OdaException</code> 
	 * object.
	 * @return	the next <code>OdaException</code> object in the chain, 
	 * 			null if there are none.
	 */
	public OdaException getNextException()
	{
		return m_nextException;
	}
	
	/**
	 * Adds an <code>OdaException</code> object to the end of the 
	 * <code>OdaException</code> chain.
	 * @param nextException	the new <code>OdaException</code> object to be
	 * 						added to the <code>OdaException</code> chain.
	 */
	public void setNextException( OdaException nextException )
	{
		// iterate to the end of the exception chain, which is the 
		// exception with a null for its next exception.
		OdaException ex = this;
		while( ex.m_nextException != null )
			ex = ex.m_nextException;
			
		ex.m_nextException = nextException;
	}

	/**
	 * Initializes the <i>cause</i> of this OdaException to the specified value.
	 * The cause is the throwable that caused this OdaException to get thrown.
	 * This method can be called at most once.
	 * @param cause		the cause of this OdaException.  A <code>null</code> value 
	 * 					is permitted and indicates that the cause is nonexistent or 
	 * 					unknown.
	 * @return			a reference to this <code>OdaException</code>.
	 * @throws	IllegalArgumentException	if <code>cause</code> is this OdaException.
	 * 			IllegalStateException		if this method has already been called on 
	 * 										this <code>OdaException</code>.
	 */
	public Throwable initCause( Throwable cause ) throws IllegalArgumentException,
														 IllegalStateException
	{
		// provides backward compatibility to JRE earlier than 1.4
		if( m_isCauseSet )
			throw new IllegalStateException( "initCause() cannot be called multiple times." );
			
		if( this == cause )
			throw new IllegalArgumentException( "This cannot be caused by itself." );
			
		m_cause = cause;
		m_isCauseSet = true;
		return this;
	}
	
	/**
	 * Returns the cause of this <code>OdaException</code>, or <code>null</code> if the 
	 * cause is nonexistent or unknown.
	 * @return	the cause of this <code>OdaException</code>, or <code>null</code> if the 
	 * 			cause is nonexistent or unknown.
	 */
	public Throwable getCause()
	{
		// provides backward compatibility to JRE earlier than 1.4
		return m_cause;
	}

    public String toString()
    {
        String message = super.toString();
        
        Throwable cause = getCause();
        if( cause != null )
        {        
            // append cause message
            String causeString = cause.toString();
            if( causeString != null && causeString.length() > 0 )
                message += " ;\n    " + causeString;     //$NON-NLS-1$
        }
        
        return message;
    }
        
}
