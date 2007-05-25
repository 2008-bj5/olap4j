/*
// $Id: $
// This software is subject to the terms of the Common Public License
// Agreement, available at the following URL:
// http://www.opensource.org/licenses/cpl.html.
// Copyright (C) 2006-2006 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
*/
package org.olap4j;

import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.DriverManager;

/**
 * <p>An exception describing an error accessing an OLAP database.</p>
 *
 * <p>Since olap4j extends JDBC, it is natural that <code>OlapException</code>
 * should extend JDBC's {@link SQLException}. The implementation by an olap4j
 * driver of a JDBC method which is declared to throw a SQLException may, if the
 * driver chooses, throw instead an OlapException.</p>
 *
 * <p>OlapException provides some additional information to help an OLAP client
 * identify the location of the error. This information is
 *
 * @author jhyde
 * @version $Id: $
 * @since Oct 23, 2006
 */
public class OlapException extends SQLException {
    private Region region;
    private Object context;

    /**
     * Constructs a fully-specified <code>SQLException</code> object.
     *
     * @param reason a description of the exception
     * @param sqlState an XOPEN or SQL 99 code identifying the exception
     * @param vendorCode a database vendor-specific exception code
     */
    public OlapException(String reason, String sqlState, int vendorCode) {
        super(reason, sqlState, vendorCode);
    }

    /**
     * Constructs an <code>SQLException</code> object with the given reason and
     * SQLState; the <code>vendorCode</code> field defaults to 0.
     *
     * @param reason a description of the exception
     * @param sqlState an XOPEN or SQL 99 code identifying the exception
     */
    public OlapException(String reason, String sqlState) {
        super(reason, sqlState);
    }

    /**
     * Constructs an <code>SQLException</code> object with a reason;
     * the <code>sqlState</code> field defaults to <code>null</code>, and
     * the <code>vendorCode</code> field defaults to 0.
     *
     * @param reason a description of the exception
     */
    public OlapException(String reason) {
        super(reason);
    }

    /**
     * Constructs an <code>SQLException</code> object;
     * the <code>reason</code> field defaults to null,
     * the <code>sqlState</code> field defaults to <code>null</code>, and
     * the <code>vendorCode</code> field defaults to 0.
     */
    public OlapException() {
        super();
    }

    /**
     *  Constructs an <code>OlapException</code> object with a given
     * <code>reason</code> and <code>cause</code>.
     * <p>
     * @param cause the underlying reason for this <code>SQLException</code>
     * (which is saved for later retrieval by the <code>getCause()</code> method);
     * may be null indicating the cause is non-existent or unknown.
     */
    public OlapException(String reason, Throwable cause) {
        super(reason, cause);
    }

    /**
     * Sets the textual region where the exception occurred.
     */
    public void setRegion(Region region) {
        this.region = region;
    }

    /**
     * Returns the textual region where the exception occurred, or null if no
     * region can be identified.
     */
    public Region getRegion() {
        return region;
    }

    /**
     * Sets the context where the exception occurred.
     *
     * @param context Context where the exception occurred
     * @throws IllegalArgumentException If context is not a {@link Cell}
     *   or a {@link Position}
     */
    public void setContext(Object context) {
        if (!(context instanceof Cell) &&
            !(context instanceof Position)) {
            throw new IllegalArgumentException(
                "expected Cell or Position");
        }
        this.context = context;
    }

    /**
     * Returns the context where the exception occurred.
     * Typically a {@link Cell} or a {@link Position}, or null.
     */
    public Object getContext() {
        return context;
    }

    /**
     * Description of the position of a syntax or validation error in the source
     * MDX string.
     *
     * <p>Row and column positions are 1-based and inclusive. For example,
     * in
     *
     * <blockquote>
     * SELECT { [Measures].MEMBERS } ON COLUMNS, { } ON ROWS FROM [Sales]
     * </blockquote>
     *
     * the <code>SELECT</code> keyword occupies positions (1, 1) through (1, 6),
     * and would have a <code>Region(startLine=1, startColumn=1, endColumn=1,
     * endLine=6)</code>.
     */
    public static final class Region {
        public final int startLine;
        public final int startColumn;
        public final int endLine;
        public final int endColumn;
        protected Region(
            int startLine,
            int startColumn,
            int endLine,
            int endColumn) {
            this.startLine = startLine;
            this.startColumn = startColumn;
            this.endColumn = endLine;
            this.endLine = endColumn;
        }

        public String toString() {
            if (startLine == endColumn && startColumn == endLine) {
                return "line " + startLine + ", column " + startColumn;
            } else {
                return "line " + startLine + ", column " + startColumn +
                    " through line " + endLine + ", column " + endColumn;
            }
        }
    }
}

// End OlapException.java
