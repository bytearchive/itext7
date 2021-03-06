package com.itextpdf.forms;

import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.test.ExtendedITextTest;
import com.itextpdf.test.annotations.type.IntegrationTest;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;

@Category(IntegrationTest.class)
public class FormFieldFlatteningTest extends ExtendedITextTest {

    public static final String sourceFolder = "./src/test/resources/com/itextpdf/forms/FormFieldFlatteningTest/";
    public static final String destinationFolder = "./target/test/com/itextpdf/forms/FormFieldFlatteningTest/";

    @BeforeClass
    public static void beforeClass() {
        createDestinationFolder(destinationFolder);
    }

    @Test
    public void formFlatteningTest01() throws IOException, InterruptedException {
        String srcFilename = sourceFolder + "formFlatteningSource.pdf";
        String filename = destinationFolder + "formFlatteningTest01.pdf";

        PdfDocument doc = new PdfDocument(new PdfReader(srcFilename), new PdfWriter(filename));

        PdfAcroForm form = PdfAcroForm.getAcroForm(doc, true);
        form.flattenFields();

        doc.close();

        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(filename, sourceFolder + "cmp_formFlatteningTest01.pdf", destinationFolder, "diff_");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void formFlatteningTest_DefaultAppearanceGeneration_Rot0() throws IOException, InterruptedException {
        String srcFilePattern = "FormFlatteningDefaultAppearance_0_";
        String destPattern = "FormFlatteningDefaultAppearance_0_";

        for (int i = 0; i < 360; i += 90) {
            String src = sourceFolder + srcFilePattern + i + ".pdf";
            String dest = destinationFolder + destPattern + i + "_flattened.pdf";
            String cmp = sourceFolder + "cmp_" + srcFilePattern + i + ".pdf";
            PdfDocument doc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));

            PdfAcroForm form = PdfAcroForm.getAcroForm(doc, true);
            for (PdfFormField field : form.getFormFields().values()) {
                field.setValue("Test");
            }
            form.flattenFields();

            doc.close();

            CompareTool compareTool = new CompareTool();
            String errorMessage = compareTool.compareByContent(dest, cmp, destinationFolder, "diff_");
            if (errorMessage != null) {
                Assert.fail(errorMessage);
            }
        }

    }

    @Test
    public void formFlatteningTest_DefaultAppearanceGeneration_Rot90() throws IOException, InterruptedException {
        String srcFilePattern = "FormFlatteningDefaultAppearance_90_";
        String destPattern = "FormFlatteningDefaultAppearance_90_";

        for (int i = 0; i < 360; i += 90) {
            String src = sourceFolder + srcFilePattern + i + ".pdf";
            String dest = destinationFolder + destPattern + i + "_flattened.pdf";
            String cmp = sourceFolder + "cmp_" + srcFilePattern + i + ".pdf";
            PdfDocument doc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));

            PdfAcroForm form = PdfAcroForm.getAcroForm(doc, true);
            for (PdfFormField field : form.getFormFields().values()) {
                field.setValue("Test");
            }
            form.flattenFields();

            doc.close();

            CompareTool compareTool = new CompareTool();
            String errorMessage = compareTool.compareByContent(dest, cmp, destinationFolder, "diff_");
            if (errorMessage != null) {
                Assert.fail(errorMessage);
            }
        }

    }

    @Test
    public void formFlatteningTest_DefaultAppearanceGeneration_Rot180() throws IOException, InterruptedException {
        String srcFilePattern = "FormFlatteningDefaultAppearance_180_";
        String destPattern = "FormFlatteningDefaultAppearance_180_";

        for (int i = 0; i < 360; i += 90) {
            String src = sourceFolder + srcFilePattern + i + ".pdf";
            String dest = destinationFolder + destPattern + i + "_flattened.pdf";
            String cmp = sourceFolder + "cmp_" + srcFilePattern + i + ".pdf";
            PdfDocument doc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));

            PdfAcroForm form = PdfAcroForm.getAcroForm(doc, true);
            for (PdfFormField field : form.getFormFields().values()) {
                field.setValue("Test");
            }
            form.flattenFields();

            doc.close();

            CompareTool compareTool = new CompareTool();
            String errorMessage = compareTool.compareByContent(dest, cmp, destinationFolder, "diff_");
            if (errorMessage != null) {
                Assert.fail(errorMessage);
            }
        }

    }

    @Test
    public void formFlatteningTest_DefaultAppearanceGeneration_Rot270() throws IOException, InterruptedException {
        String srcFilePattern = "FormFlatteningDefaultAppearance_270_";
        String destPattern = "FormFlatteningDefaultAppearance_270_";

        for (int i = 0; i < 360; i += 90) {
            String src = sourceFolder + srcFilePattern + i + ".pdf";
            String dest = destinationFolder + destPattern + i + "_flattened.pdf";
            String cmp = sourceFolder + "cmp_" + srcFilePattern + i + ".pdf";
            PdfDocument doc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));

            PdfAcroForm form = PdfAcroForm.getAcroForm(doc, true);
            for (PdfFormField field : form.getFormFields().values()) {
                field.setValue("Test");
            }
            form.flattenFields();

            doc.close();

            CompareTool compareTool = new CompareTool();
            String errorMessage = compareTool.compareByContent(dest, cmp, destinationFolder, "diff_");
            if (errorMessage != null) {
                Assert.fail(errorMessage);
            }
        }

    }
}
