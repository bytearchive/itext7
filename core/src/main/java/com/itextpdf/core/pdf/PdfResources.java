package com.itextpdf.core.pdf;

import com.itextpdf.core.exceptions.PdfException;
import com.itextpdf.core.fonts.PdfFont;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class PdfResources extends PdfObjectWrapper<PdfDictionary> {

    private static final String F = "F";

    /**
     * The fonts of this document
     */
    private HashMap<PdfFont, PdfName> fonts = new LinkedHashMap<PdfFont, PdfName>();

    /**
     * The font number counter for the fonts in the document.
     */
    private int fontNumber = 1;

    public PdfResources(PdfDictionary pdfObject) {
        super(pdfObject);
        makeResources(pdfObject);
    }

    public PdfResources() {
        this(new PdfDictionary());
    }

    public PdfName addFont(PdfFont font){
        PdfName fontName = fonts.get(font);
        if (fontName == null) {
            fontName = new PdfName(F + fontNumber++);
            fonts.put(font, fontName);
            PdfDictionary fontDictionary = (PdfDictionary)pdfObject.get(PdfName.Font);
            if (fontDictionary == null) {
                pdfObject.put(PdfName.Font, fontDictionary = new PdfDictionary());
            }
            fontDictionary.put(fontName, font.getPdfObject());
        }
        return fontName;
    }

    protected void makeResources(PdfDictionary dictionary) {
        //TODO: Implement populating PdfResources internals from PdfDictionary.
    }

}