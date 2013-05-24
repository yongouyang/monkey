package org.monkey.sample.market;

import java.util.Vector;

public class HtmlTableParser {

    private Vector<Vector> contents;
    private int maxsize;

    public void reset() {
        contents = new Vector<Vector>();
        maxsize = 0;
    }

    public void parse(String htmlContent) {
        reset();


        for (int i = 0; i < htmlContent.length(); i++) {
            // Search <tr
            int idxTr = htmlContent.indexOf("<tr", i);
            if (idxTr < 0)
                break;
            i = idxTr;

            Vector<String> row = new Vector<String>();

            // Search </tr
            int idxTrEnd = htmlContent.indexOf("</tr", i);
            if (idxTrEnd < 0)
                idxTrEnd = htmlContent.length();

            while (i < idxTrEnd) {
                // Search <td
                int idxTd = htmlContent.indexOf("<td", i);
                if (idxTd < 0)
                    break;
                if (idxTd > idxTrEnd) {
                    i = idxTrEnd;
                    break;
                }
                // Search Cell Start
                int idxCell = htmlContent.indexOf(">", idxTd);
                if (idxCell < 0)
                    break;
                int idxCellEnd = htmlContent.indexOf("</td", idxCell);
                if (idxCellEnd < 0)
                    idxCellEnd = htmlContent.length();
                String cellContent = htmlContent.substring(idxCell + 1, idxCellEnd);
                row.add(cellContent.replaceAll("\\<.*?\\>", ""));
                i = idxCellEnd;
            }

            if (row.size() > maxsize) {
                maxsize = row.size();
            }
            contents.add(row);
        }
    }

    public Vector<Vector> getContents() {
        return contents;
    }

    public String[][] getContentsArray() {
        String[][] contentsArray = new String[contents.size()][maxsize];
        for (int i = 0; i < contents.size(); i++) {
            for (int j = 0; j < contents.get(i).size(); j++) {
                contentsArray[i][j] = (String) contents.get(i).get(j);
            }
        }
        return contentsArray;
    }
}
