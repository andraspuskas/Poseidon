package hu.pemik.poseidon.importing;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import hu.pemik.poseidon.MainActivity;
import hu.pemik.poseidon.R;

public class ImportingScheduleDemo extends AppCompatActivity {
    public static final int REQUEST_CODE_FOR_READING_SCHEDULE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importing_schedule_demo);
    }

    public void buttonClicked(View view) {
        if (havePermissionToReadExternalStorage()) {
            readSchedule();
        } else {
            askForPermissionAndReadScheduleIfGranted();
        }
    }

    private void askForPermissionAndReadScheduleIfGranted() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                ImportingScheduleDemo.REQUEST_CODE_FOR_READING_SCHEDULE);
    }

    private void readSchedule() {
        TextView textView = findViewById(R.id.textView);
        textView.setText("");
        try {
            XlsxSchedule xlsxSchedule = new XlsxSchedule();
            List<ScheduleEntry> entries = xlsxSchedule.getEntries();
            for (ScheduleEntry e : entries) {
                textView.append(e.toString() + "\n");
            }
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.toString());
            textView.append(e.toString());
        }
    }

    private boolean havePermissionToReadExternalStorage() {
        return ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ImportingScheduleDemo.REQUEST_CODE_FOR_READING_SCHEDULE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("ImportingScheduleDemo", "Permission was granted yay");
                    readSchedule();
                } else {
                    // TODO What if no permission?
                }
            }
        }
    }
}