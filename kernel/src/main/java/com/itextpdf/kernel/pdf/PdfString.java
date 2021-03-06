/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV
    Authors: Bruno Lowagie, Paulo Soares, et al.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS

    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/

    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.

    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.

    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.

    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
package com.itextpdf.kernel.pdf;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.source.ByteBuffer;
import com.itextpdf.io.source.PdfTokenizer;
import com.itextpdf.io.util.StreamUtil;

/**
 * A {@code PdfString}-class is the PDF-equivalent of a
 * JAVA-{@code String}-object.
 * <p/>
 * A string is a sequence of characters delimited by parenthesis.
 * If a string is too long to be conveniently placed on a single line, it may
 * be split across multiple lines by using the backslash character (\) at the
 * end of a line to indicate that the string continues on the following line.
 * Within a string, the backslash character is used as an escape to specify
 * unbalanced parenthesis, non-printing ASCII characters, and the backslash
 * character itself. Use of the \<i>ddd</i> escape sequence is the preferred
 * way to represent characters outside the printable ASCII character set.<br>
 * This object is described in the 'Portable Document Format Reference Manual
 * version 1.7' section 3.2.3 (page 53-56).
 * <p/>
 * {@link PdfObject}
 */
public class PdfString extends PdfPrimitiveObject {

    private static final long serialVersionUID = 390789504287887010L;

    protected String value;
    protected String encoding;
    protected boolean hexWriting = false;
    /*
    * using for decryption
    * */
    private int decryptInfoNum = 0;
    /*
    * using for decryption
    * */
    private int decryptInfoGen = 0;

    public PdfString(String value, String encoding) {
        super();
        this.value = value;
        this.encoding = encoding;
    }

    public PdfString(String value) {
        this(value, null);
    }

    public PdfString(byte[] content) {
        super();
        if (content != null && content.length > 0) {
            StringBuilder str = new StringBuilder(content.length);
            for (byte b : content) {
                str.append((char) (b & 0xff));
            }
            this.value = str.toString();
        } else {
            this.value = "";
        }
    }

    /**
     * Only PdfReader can use this method!
     */
    protected PdfString(byte[] content, boolean hexWriting) {
        super(content);
        this.hexWriting = hexWriting;
    }

    private PdfString() {
        super();
    }

    @Override
    public byte getType() {
        return STRING;
    }

    public boolean isHexWriting() {
        return hexWriting;
    }

    public PdfString setHexWriting(boolean hexWriting) {
        if (value == null) {
            generateValue();
            content = null;
        }
        this.hexWriting = hexWriting;
        return this;
    }

    public String getValue() {
        if (value == null)
            generateValue();
        return value;
    }

    /**
     * Gets the encoding of this string.
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Sets the encoding of this string.
     * NOTE. Byte content will be removed.
     * @deprecated Create a new instance with {@link PdfString#PdfString(String, String)} instead.
     */
    @Deprecated
    public void setEncoding(String encoding) {
        if (value == null) {
            generateValue();
            this.content = null;
        }
        this.encoding = encoding;
    }

    /**
     * Returns the Unicode {@code String} value of this
     * {@code PdfString}-object.
     */
    public String toUnicodeString() {
        if (encoding != null && encoding.length() != 0) {
            return getValue();
        }
        if (content == null) {
            generateContent();
        }
        byte[] b = PdfTokenizer.decodeStringContent(content, hexWriting);
        if (b.length >= 2 && b[0] == (byte) 0xFE && b[1] == (byte) 0xFF) {
            return PdfEncodings.convertToString(b, PdfEncodings.UNICODE_BIG);
        } else {
            return PdfEncodings.convertToString(b, PdfEncodings.PDF_DOC_ENCODING);
        }
    }

    /**
     * Gets bytes of String-value considering encoding.
     *
     * @return byte array
     */
    // Analog of com.itextpdf.text.pdf.PdfString.getBytes() method in iText5.
    public byte[] getValueBytes() {
        if (value == null)
            generateValue();
        if (encoding != null && encoding.equals(PdfEncodings.UNICODE_BIG) && PdfEncodings.isPdfDocEncoding(value))
            return PdfEncodings.convertToBytes(value, PdfEncodings.PDF_DOC_ENCODING);
        else
            return PdfEncodings.convertToBytes(value, encoding);
    }

    /**
     * Marks object to be saved as indirect.
     *
     * @param document a document the indirect reference will belong to.
     * @return object itself.
     */
    @SuppressWarnings("unchecked")
    @Override
    public PdfString makeIndirect(PdfDocument document) {
        return (PdfString) super.makeIndirect(document);
    }

    /**
     * Marks object to be saved as indirect.
     *
     * @param document a document the indirect reference will belong to.
     * @return object itself.
     */
    @SuppressWarnings("unchecked")
    @Override
    public PdfString makeIndirect(PdfDocument document, PdfIndirectReference reference) {
        return (PdfString) super.makeIndirect(document, reference);
    }

    /**
     * Copies object to a specified document.
     * Works only for objects that are read from existing document, otherwise an exception is thrown.
     *
     * @param document document to copy object to.
     * @return copied object.
     */
    @SuppressWarnings("unchecked")
    @Override
    public PdfString copyTo(PdfDocument document) {
        return (PdfString) super.copyTo(document, true);
    }

    /**
     * Copies object to a specified document.
     * Works only for objects that are read from existing document, otherwise an exception is thrown.
     *
     * @param document         document to copy object to.
     * @param allowDuplicating indicates if to allow copy objects which already have been copied.
     *                         If object is associated with any indirect reference and allowDuplicating is false then already existing reference will be returned instead of copying object.
     *                         If allowDuplicating is true then object will be copied and new indirect reference will be assigned.
     * @return copied object.
     */
    @SuppressWarnings("unchecked")
    @Override
    public PdfString copyTo(PdfDocument document, boolean allowDuplicating) {
        return (PdfString) super.copyTo(document, allowDuplicating);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PdfString that = (PdfString) o;
        String v1 = getValue();
        String v2 = that.getValue();
        if (v1 != null && v1.equals(v2)) {
            String e1 = getEncoding();
            String e2 = that.getEncoding();
            if ((e1 == null && e2 == null)
                    || (e1 != null && e1.equals(e2))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        if (value == null) {
            return new String(PdfTokenizer.decodeStringContent(content, hexWriting));
        } else {
            return getValue();
        }
    }

    @Override
    public int hashCode() {
        String v = getValue();
        String e = getEncoding();
        int result = v != null ? v.hashCode() : 0;
        return 31 * result + (e != null ? e.hashCode() : 0);
    }

    protected void generateValue() {
        assert content != null : "No byte[] content to generate value";
        value = PdfEncodings.convertToString(PdfTokenizer.decodeStringContent(content, hexWriting), null);
    }

    @Override
    protected void generateContent() {
        content = encodeBytes(getValueBytes());
    }

    /**
     * Decrypt content of an encrypted {@code PdfString}.
     */
    protected PdfString decrypt(PdfEncryption decrypt) {
        if (decrypt != null) {
            assert content != null : "No byte content to decrypt value";
            byte[] decodedContent = PdfTokenizer.decodeStringContent(content, hexWriting);
            content = null;
            decrypt.setHashKeyForNextObject(decryptInfoNum, decryptInfoGen);
            value = PdfEncodings.convertToString(decrypt.decryptByteArray(decodedContent), null);
        }
        return this;
    }


    /**
     * Encrypt content of {@code value} and set as content. {@code generateContent()} won't be called.
     *
     * @param encrypt @see PdfEncryption
     * @return true if value was encrypted, otherwise false.
     */
    protected boolean encrypt(PdfEncryption encrypt) {
        if (encrypt != null && !encrypt.isEmbeddedFilesOnly()) {
            byte[] b = encrypt.encryptByteArray(getValueBytes());
            content = encodeBytes(b);
            return true;
        }
        return false;
    }

    /**
     * Escape special symbols or convert to hexadecimal string.
     * This method don't change either {@code value} or {@code content} ot the {@code PdfString}.
     *
     * @param bytes byte array to manipulate with.
     * @return Hexadecimal string or string with escaped symbols in byte array view.
     */
    protected byte[] encodeBytes(byte[] bytes) {
        if (hexWriting) {
            ByteBuffer buf = new ByteBuffer(bytes.length * 2);
            for (byte b : bytes) {
                buf.appendHex(b);
            }
            return buf.getInternalBuffer();
        } else {
            ByteBuffer buf = StreamUtil.createBufferedEscapedString(bytes);
            return buf.toByteArray(1, buf.size() - 2);
        }
    }

    @Override
    protected PdfString newInstance() {
        return new PdfString();
    }

    @Override
    protected void copyContent(PdfObject from, PdfDocument document) {
        super.copyContent(from, document);
        PdfString string = (PdfString) from;
        value = string.value;
        hexWriting = string.hexWriting;
    }

    void setDecryptInfoNum(int decryptInfoNum) {
        this.decryptInfoNum = decryptInfoNum;
    }

    void setDecryptInfoGen(int decryptInfoGen) {
        this.decryptInfoGen = decryptInfoGen;
    }
}
