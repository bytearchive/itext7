package com.itextpdf.kernel.pdf;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.kernel.pdf.navigation.PdfStringDestination;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.test.ExtendedITextTest;
import com.itextpdf.test.annotations.LogMessage;
import com.itextpdf.test.annotations.LogMessages;
import com.itextpdf.test.annotations.type.IntegrationTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNull;

@Category(IntegrationTest.class)
public class PdfOutlineTest extends ExtendedITextTest {

    public static final String sourceFolder = "./src/test/resources/com/itextpdf/kernel/pdf/PdfOutlineTest/";
    public static final String destinationFolder = "./target/test/com/itextpdf/kernel/pdf/PdfOutlineTest/";

    @BeforeClass
    public static void beforeClass() throws FileNotFoundException {
        createDestinationFolder(destinationFolder);
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(destinationFolder + "documentWithOutlines.pdf"));
        pdfDoc.getCatalog().setPageMode(PdfName.UseOutlines);

        PdfPage firstPage = pdfDoc.addNewPage();
        PdfPage secondPage = pdfDoc.addNewPage();

        PdfOutline rootOutline = pdfDoc.getOutlines(false);
        PdfOutline firstOutline = rootOutline.addOutline("First Page");
        PdfOutline secondOutline = rootOutline.addOutline("Second Page");
        firstOutline.addDestination(PdfExplicitDestination.createFit(firstPage));
        secondOutline.addDestination(PdfExplicitDestination.createFit(secondPage));

        pdfDoc.close();
    }

    @Test
    public void outlinesTest() throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(sourceFolder + "iphone_user_guide.pdf"));
        PdfOutline outlines = pdfDoc.getOutlines(false);
        List<PdfOutline> children = outlines.getAllChildren().get(0).getAllChildren();

        Assert.assertEquals(outlines.getTitle(), "Outlines");
        Assert.assertEquals(children.size(), 13);
        Assert.assertTrue(children.get(0).getDestination() instanceof PdfStringDestination);
    }

    @Test
    public void outlinesWithPagesTest() throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(sourceFolder + "iphone_user_guide.pdf"));
        PdfPage page = pdfDoc.getPage(52);
        List<PdfOutline> pageOutlines = page.getOutlines(true);
        try {
            Assert.assertEquals(3, pageOutlines.size());
            Assert.assertTrue(pageOutlines.get(0).getTitle().equals("Safari"));
            Assert.assertEquals(pageOutlines.get(0).getAllChildren().size(), 4);
        } finally {
            pdfDoc.close();
        }
    }

    @Before
    public void setupAddOutlinesToDocumentTest() throws IOException {
        String filename = sourceFolder + "iphone_user_guide.pdf";

        PdfReader reader = new PdfReader(filename);
        PdfWriter writer = new PdfWriter(destinationFolder + "addOutlinesResult.pdf");
        PdfDocument pdfDoc = new PdfDocument(reader, writer);
        pdfDoc.setTagged();

        PdfOutline outlines = pdfDoc.getOutlines(false);

        PdfOutline firstPage = outlines.addOutline("firstPage");
        PdfOutline firstPageChild = firstPage.addOutline("firstPageChild");
        PdfOutline secondPage = outlines.addOutline("secondPage");
        PdfOutline secondPageChild = secondPage.addOutline("secondPageChild");
        firstPage.addDestination(PdfExplicitDestination.createFit(pdfDoc.getPage(1)));
        firstPageChild.addDestination(PdfExplicitDestination.createFit(pdfDoc.getPage(1)));
        secondPage.addDestination(PdfExplicitDestination.createFit(pdfDoc.getPage(2)));
        secondPageChild.addDestination(PdfExplicitDestination.createFit(pdfDoc.getPage(2)));
        outlines.getAllChildren().get(0).getAllChildren().get(1).addOutline("testOutline", 1).addDestination(PdfExplicitDestination.createFit(pdfDoc.getPage(102)));

        pdfDoc.close();
    }

    @Test
    public void addOutlinesToDocumentTest() throws IOException, InterruptedException {
        String filename = destinationFolder + "addOutlinesResult.pdf";

        PdfDocument pdfDoc = new PdfDocument(new PdfReader(filename));
        PdfOutline outlines = pdfDoc.getOutlines(false);
        try {
            Assert.assertEquals(3, outlines.getAllChildren().size());
            Assert.assertEquals("firstPageChild", outlines.getAllChildren().get(1).getAllChildren().get(0).getTitle());
        } finally {
            pdfDoc.close();
        }
    }

    @Before
    public void setupRemovePageWithOutlinesTest() throws IOException {
        String filename = sourceFolder + "iphone_user_guide.pdf";

        PdfDocument pdfDoc = new PdfDocument(new PdfReader(filename), new PdfWriter(destinationFolder + "removePagesWithOutlinesResult.pdf"));
        pdfDoc.removePage(102);

        pdfDoc.close();
    }

    @Test
    public void removePageWithOutlinesTest() throws IOException {
        String filename = destinationFolder + "removePagesWithOutlinesResult.pdf";

        PdfDocument pdfDoc = new PdfDocument(new PdfReader(filename));

        PdfPage page = pdfDoc.getPage(102);
        List<PdfOutline> pageOutlines = page.getOutlines(false);
        try {
            Assert.assertEquals(4, pageOutlines.size());
        } finally {
            pdfDoc.close();
        }
    }

    @Before
    public void setupUpdateOutlineTitle() throws IOException {
        String filename = sourceFolder + "iphone_user_guide.pdf";
        PdfReader reader = new PdfReader(filename);
        PdfWriter writer = new PdfWriter(destinationFolder + "updateOutlineTitleResult.pdf");
        PdfDocument pdfDoc = new PdfDocument(reader, writer);

        PdfOutline outlines = pdfDoc.getOutlines(false);
        outlines.getAllChildren().get(0).getAllChildren().get(1).setTitle("New Title");

        pdfDoc.close();
    }

    @Test
    public void updateOutlineTitle() throws IOException {
        String filename = destinationFolder + "updateOutlineTitleResult.pdf";
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(filename));

        PdfOutline outlines = pdfDoc.getOutlines(false);
        PdfOutline outline = outlines.getAllChildren().get(0).getAllChildren().get(1);
        try {
            Assert.assertEquals("New Title", outline.getTitle());
        } finally {
            pdfDoc.close();
        }
    }

    @Before
    public void setupAddOutlineInNotOutlineMode() throws IOException {
        String filename = sourceFolder + "iphone_user_guide.pdf";

        PdfReader reader = new PdfReader(filename);
        PdfWriter writer = new PdfWriter(destinationFolder + "addOutlinesWithoutOutlineModeResult.pdf");
        PdfDocument pdfDoc = new PdfDocument(reader, writer);

        PdfOutline outlines = new PdfOutline(pdfDoc);

        PdfOutline firstPage = outlines.addOutline("firstPage");
        PdfOutline firstPageChild = firstPage.addOutline("firstPageChild");
        PdfOutline secondPage = outlines.addOutline("secondPage");
        PdfOutline secondPageChild = secondPage.addOutline("secondPageChild");
        firstPage.addDestination(PdfExplicitDestination.createFit(pdfDoc.getPage(1)));
        firstPageChild.addDestination(PdfExplicitDestination.createFit(pdfDoc.getPage(1)));
        secondPage.addDestination(PdfExplicitDestination.createFit(pdfDoc.getPage(2)));
        secondPageChild.addDestination(PdfExplicitDestination.createFit(pdfDoc.getPage(2)));

        pdfDoc.close();
    }

    @Test
    public void addOutlineInNotOutlineMode() throws IOException {
        String filename = destinationFolder + "addOutlinesWithoutOutlineModeResult.pdf";
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(filename));

        List<PdfOutline> pageOutlines = pdfDoc.getPage(102).getOutlines(true);
        try {
            Assert.assertEquals(5, pageOutlines.size());
        } finally {
            pdfDoc.close();
        }
    }

    @Test
    public void createDocWithOutlines() throws IOException, InterruptedException {
        String filename = destinationFolder + "documentWithOutlines.pdf";

        PdfDocument pdfDoc = new PdfDocument(new PdfReader(filename));

        PdfOutline outlines = pdfDoc.getOutlines(false);
        try {
            Assert.assertEquals(2, outlines.getAllChildren().size());
            Assert.assertEquals("First Page", outlines.getAllChildren().get(0).getTitle());
        } finally {
            pdfDoc.close();
        }
    }


    @Test
    @LogMessages(messages = {
            @LogMessage(messageTemplate = LogMessageConstant.SOURCE_DOCUMENT_HAS_ACROFORM_DICTIONARY)
    })
    public void copyPagesWithOutlines() throws IOException {
        PdfReader reader = new PdfReader(sourceFolder + "iphone_user_guide.pdf");
        PdfWriter writer = new PdfWriter(destinationFolder + "copyPagesWithOutlines01.pdf");

        PdfDocument pdfDoc = new PdfDocument(reader);
        PdfDocument pdfDoc1 = new PdfDocument(writer);

        List<Integer> pages = new ArrayList<>();
        pages.add(1);
        pages.add(2);
        pages.add(3);
        pages.add(5);
        pages.add(52);
        pages.add(102);
        pdfDoc1.initializeOutlines();
        pdfDoc.copyPagesTo(pages, pdfDoc1);
        pdfDoc.close();

        Assert.assertEquals(6, pdfDoc1.getNumberOfPages());
        Assert.assertEquals(4, pdfDoc1.getOutlines(false).getAllChildren().get(0).getAllChildren().size());
        pdfDoc1.close();
    }

    @Test
    public void addOutlinesWithNamedDestinations01() throws IOException, InterruptedException {
        String filename = destinationFolder + "outlinesWithNamedDestinations01.pdf";

        PdfReader reader = new PdfReader(sourceFolder + "iphone_user_guide.pdf");
        PdfWriter writer = new PdfWriter(filename);

        PdfDocument pdfDoc = new PdfDocument(reader, writer);
        PdfArray array1 = new PdfArray();
        array1.add(pdfDoc.getPage(2).getPdfObject());
        array1.add(PdfName.XYZ);
        array1.add(new PdfNumber(36));
        array1.add(new PdfNumber(806));
        array1.add(new PdfNumber(0));

        PdfArray array2 = new PdfArray();
        array2.add(pdfDoc.getPage(3).getPdfObject());
        array2.add(PdfName.XYZ);
        array2.add(new PdfNumber(36));
        array2.add(new PdfNumber(806));
        array2.add(new PdfNumber(1.25));

        PdfArray array3 = new PdfArray();
        array3.add(pdfDoc.getPage(4).getPdfObject());
        array3.add(PdfName.XYZ);
        array3.add(new PdfNumber(36));
        array3.add(new PdfNumber(806));
        array3.add(new PdfNumber(1));

        pdfDoc.addNamedDestination("test1", array2);
        pdfDoc.addNamedDestination("test2", array3);
        pdfDoc.addNamedDestination("test3", array1);

        PdfOutline root = pdfDoc.getOutlines(false);

        PdfOutline firstOutline = root.addOutline("Test1");
        firstOutline.addDestination(PdfDestination.makeDestination(new PdfString("test1")));
        PdfOutline secondOutline = root.addOutline("Test2");
        secondOutline.addDestination(PdfDestination.makeDestination(new PdfString("test2")));
        PdfOutline thirdOutline = root.addOutline("Test3");
        thirdOutline.addDestination(PdfDestination.makeDestination(new PdfString("test3")));
        pdfDoc.close();

        assertNull(new CompareTool().compareByContent(filename, sourceFolder + "cmp_outlinesWithNamedDestinations01.pdf", destinationFolder, "diff_"));
    }

    @Test
    public void addOutlinesWithNamedDestinations02() throws IOException, InterruptedException {
        String filename = destinationFolder + "outlinesWithNamedDestinations02.pdf";

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filename));
        PdfArray array1 = new PdfArray();
        array1.add(pdfDoc.addNewPage().getPdfObject());
        array1.add(PdfName.XYZ);
        array1.add(new PdfNumber(36));
        array1.add(new PdfNumber(806));
        array1.add(new PdfNumber(0));

        PdfArray array2 = new PdfArray();
        array2.add(pdfDoc.addNewPage().getPdfObject());
        array2.add(PdfName.XYZ);
        array2.add(new PdfNumber(36));
        array2.add(new PdfNumber(806));
        array2.add(new PdfNumber(1.25));

        PdfArray array3 = new PdfArray();
        array3.add(pdfDoc.addNewPage().getPdfObject());
        array3.add(PdfName.XYZ);
        array3.add(new PdfNumber(36));
        array3.add(new PdfNumber(806));
        array3.add(new PdfNumber(1));

        pdfDoc.addNamedDestination("page1", array2);
        pdfDoc.addNamedDestination("page2", array3);
        pdfDoc.addNamedDestination("page3", array1);

        PdfOutline root = pdfDoc.getOutlines(false);
        PdfOutline firstOutline = root.addOutline("Test1");
        firstOutline.addDestination(PdfDestination.makeDestination(new PdfString("page1")));
        PdfOutline secondOutline = root.addOutline("Test2");
        secondOutline.addDestination(PdfDestination.makeDestination(new PdfString("page2")));
        PdfOutline thirdOutline = root.addOutline("Test3");
        thirdOutline.addDestination(PdfDestination.makeDestination(new PdfString("page3")));
        pdfDoc.close();

        Assert.assertNull(new CompareTool().compareByContent(filename, sourceFolder + "cmp_outlinesWithNamedDestinations02.pdf", destinationFolder, "diff_"));
    }
}
