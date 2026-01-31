package util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import java.awt.Color;

import javax.swing.table.TableModel;
import java.io.File;
import java.io.IOException;

/**
 * Utility class for exporting JTable data to PDF using Apache PDFBox
 */
public class PDFExporter {

    private static final float MARGIN = 40;
    private static final float TITLE_FONT_SIZE = 18;
    private static final float HEADER_FONT_SIZE = 11;
    private static final float DATA_FONT_SIZE = 10;
    private static final float ROW_HEIGHT = 20;
    private static final float TITLE_HEIGHT = 40;

    /**
     * Export table data to PDF file with equal column widths
     * 
     * @param tableModel The table model containing data
     * @param title      Report title
     * @param footer     Footer text (optional)
     * @param outputFile Output PDF file
     * @throws IOException if file writing fails
     */
    public static void exportTableToPDF(TableModel tableModel, String title, String footer, File outputFile)
            throws IOException {
        exportTableToPDF(tableModel, title, footer, outputFile, null);
    }

    /**
     * Export table data to PDF file with custom column widths
     * 
     * @param tableModel        The table model containing data
     * @param title             Report title
     * @param footer            Footer text (optional)
     * @param outputFile        Output PDF file
     * @param columnWidthRatios Array of width ratios for each column (null for
     *                          equal widths)
     * @throws IOException if file writing fails
     */
    public static void exportTableToPDF(TableModel tableModel, String title, String footer, File outputFile,
            float[] columnWidthRatios) throws IOException {
        PDDocument document = new PDDocument();

        try {
            // Load Vietnamese-compatible font
            PDType0Font font = PDType0Font.load(document, new File("C:/Windows/Fonts/arial.ttf"));

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setNonStrokingColor(Color.BLACK);
            float yPosition = page.getMediaBox().getHeight() - MARGIN;

            // Draw title
            contentStream.beginText();
            contentStream.setFont(font, TITLE_FONT_SIZE);
            float titleWidth = font.getStringWidth(title) / 1000 * TITLE_FONT_SIZE;
            contentStream.newLineAtOffset((page.getMediaBox().getWidth() - titleWidth) / 2, yPosition);
            contentStream.showText(title);
            contentStream.endText();

            yPosition -= TITLE_HEIGHT;

            // Calculate column widths
            int columnCount = tableModel.getColumnCount();
            float tableWidth = page.getMediaBox().getWidth() - 2 * MARGIN;
            float[] columnWidths = new float[columnCount];

            if (columnWidthRatios != null && columnWidthRatios.length == columnCount) {
                // Calculate total ratio
                float totalRatio = 0;
                for (float ratio : columnWidthRatios) {
                    totalRatio += ratio;
                }
                // Distribute width based on ratios
                for (int i = 0; i < columnCount; i++) {
                    columnWidths[i] = (columnWidthRatios[i] / totalRatio) * tableWidth;
                }
            } else {
                // Equal widths if no ratios provided
                float cellWidth = tableWidth / columnCount;
                for (int i = 0; i < columnCount; i++) {
                    columnWidths[i] = cellWidth;
                }
            }

            // Draw table headers
            contentStream.setFont(font, HEADER_FONT_SIZE);
            float xPosition = MARGIN;

            for (int i = 0; i < columnCount; i++) {
                String headerText = tableModel.getColumnName(i);
                contentStream.beginText();
                contentStream.newLineAtOffset(xPosition + 2, yPosition);
                contentStream.showText(headerText);
                contentStream.endText();
                xPosition += columnWidths[i];
            }

            // Draw header line
            contentStream.moveTo(MARGIN, yPosition - 5);
            contentStream.lineTo(page.getMediaBox().getWidth() - MARGIN, yPosition - 5);
            contentStream.stroke();

            yPosition -= ROW_HEIGHT;

            // Draw data rows
            contentStream.setFont(font, DATA_FONT_SIZE);

            for (int row = 0; row < tableModel.getRowCount(); row++) {
                // Check if we need a new page
                if (yPosition < MARGIN + 50) {
                    contentStream.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.setNonStrokingColor(Color.BLACK);
                    contentStream.setFont(font, DATA_FONT_SIZE);
                    yPosition = page.getMediaBox().getHeight() - MARGIN;
                }

                xPosition = MARGIN;
                for (int col = 0; col < columnCount; col++) {
                    Object value = tableModel.getValueAt(row, col);
                    String text = value != null ? value.toString() : "";

                    // Truncate if too long
                    float textWidth = font.getStringWidth(text) / 1000 * DATA_FONT_SIZE;
                    if (textWidth > columnWidths[col] - 4) {
                        while (textWidth > columnWidths[col] - 10 && text.length() > 3) {
                            text = text.substring(0, text.length() - 1);
                            textWidth = font.getStringWidth(text + "...") / 1000 * DATA_FONT_SIZE;
                        }
                        text = text + "...";
                    }

                    contentStream.beginText();
                    contentStream.newLineAtOffset(xPosition + 2, yPosition);
                    contentStream.showText(text);
                    contentStream.endText();
                    xPosition += columnWidths[col];
                }

                yPosition -= ROW_HEIGHT;
            }

            // Draw footer if provided
            if (footer != null && !footer.isEmpty()) {
                yPosition -= 20;
                contentStream.setFont(font, HEADER_FONT_SIZE);
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPosition);
                contentStream.showText(footer);
                contentStream.endText();
            }

            contentStream.close();
            document.save(outputFile);

        } finally {
            document.close();
        }
    }
}
