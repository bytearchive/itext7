package com.itextpdf.kernel.pdf;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.layer.PdfLayer;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.test.ExtendedITextTest;
import com.itextpdf.test.annotations.type.IntegrationTest;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.util.List;

@Category(IntegrationTest.class)
public class PdfLayerTest extends ExtendedITextTest{

    public static final String sourceFolder = "./src/test/resources/com/itextpdf/kernel/pdf/PdfLayerTest/";
    public static final String destinationFolder = "./target/test/com/itextpdf/kernel/pdf/PdfLayerTest/";

    @BeforeClass
    public static void beforeClass() {
        createDestinationFolder(destinationFolder);
    }

    @Test
    public void testInStamperMode1() throws IOException, InterruptedException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(sourceFolder + "input_layered.pdf"),
                new PdfWriter(destinationFolder + "output_copy_layered.pdf"));
        pdfDoc.close();
        Assert.assertNull(new CompareTool().compareByContent(destinationFolder + "output_copy_layered.pdf", sourceFolder + "input_layered.pdf", destinationFolder, "diff"));
    }

    @Test
    public void testInStamperMode2() throws IOException, InterruptedException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(sourceFolder + "input_layered.pdf"),
                new PdfWriter(destinationFolder + "output_layered.pdf"));

        PdfCanvas canvas = new PdfCanvas(pdfDoc, 1);

        PdfLayer newLayer = new PdfLayer("appended", pdfDoc);
        canvas.beginLayer(newLayer).beginText().setFontAndSize(PdfFontFactory.createFont(FontConstants.HELVETICA), 18).
                moveText(200, 600).showText("APPENDED CONTENT").endText().endLayer();

        List<PdfLayer> allLayers = pdfDoc.getCatalog().getOCProperties(true).getLayers();
        for (PdfLayer layer : allLayers) {
            if (layer.isLocked())
                layer.setLocked(false);
            if ("Grouped layers".equals(layer.getTitle())) {
                layer.addChild(newLayer);
            }
        }

        pdfDoc.close();
        Assert.assertNull(new CompareTool().compareByContent(destinationFolder + "output_layered.pdf", sourceFolder + "cmp_output_layered.pdf", destinationFolder, "diff"));
    }

}
