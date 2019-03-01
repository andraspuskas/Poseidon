package hu.pemik.poseidon.importing;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import hu.pemik.poseidon.importing.ScheduleEntry;

public class XlsxSchedule {

    public List<ScheduleEntry> getEntries() throws IOException, XmlPullParserException {
        List<ScheduleEntry> ret = new ArrayList<>();
        List<List<String>> rows = getRows();
        rows.remove(0);
        for (List<String> row : rows) {
            ret.add(new ScheduleEntry(row));
        }
        return ret;
    }

    public List<List<String>> getRows() throws IOException, XmlPullParserException {
        ZipFile zipFile = new ZipFile("sdcard/Download/export.xlsx");
        List<String> sharedStrings = getSharedStrings(zipFile);

        ZipEntry sheet1 = zipFile.getEntry("xl/worksheets/sheet1.xml");
        InputStream sheet1InputStream = zipFile.getInputStream(sheet1);

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(sheet1InputStream, "UTF-8");

        List<List<String>> rows = new ArrayList<>();
        boolean inValue = false;
        List<String> row = null;

        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("row")) {
                row = new ArrayList<>();
            } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equals("row")) {
                if (row != null) rows.add(row);
            } else if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("v")) {
                inValue = true;
            } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equals("v")) {
                inValue = false;
            } else if (inValue && eventType == XmlPullParser.TEXT) {
                row.add(sharedStrings.get(Integer.parseInt(xpp.getText())));
            }
            eventType = xpp.next();
        }
        return rows;

    }

    private List<String> getSharedStrings(ZipFile zipFile) throws IOException, XmlPullParserException {
        ZipEntry sharedStringsZipEntry = zipFile.getEntry("xl/sharedStrings.xml");
        InputStream sharedStringsInputStream = zipFile.getInputStream(sharedStringsZipEntry);

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput(sharedStringsInputStream, "UTF-8");

        ArrayList<String> sharedStrings = new ArrayList<>();
        String s = null;
        int eventType = xpp.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("si")) {
                s = "";
            }
            if (eventType == XmlPullParser.TEXT) {
                s = xpp.getText();
            }
            if (eventType == XmlPullParser.END_TAG && xpp.getName().equals("si")) {
                sharedStrings.add(s);
            }
            eventType = xpp.next();
        }
        return sharedStrings;
    }
}
