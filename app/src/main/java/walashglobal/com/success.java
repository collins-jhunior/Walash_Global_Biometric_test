package walashglobal.com;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class success extends AppCompatActivity {
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        text = findViewById(R.id.text);
        owners_name();
    }

    public void owners_name() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
            }
        } else {
            ContentResolver cr = getContentResolver();
            Cursor c = cr.query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
            if (c != null) {
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    String name = c.getString(c.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME));
                    String text_ = "Authentication Successfull\nWelcome " + name;
                    text.setText(text_);
                }
                c.close();
            }

        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            ContentResolver cr = getContentResolver();
            Cursor c = cr.query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
            if (c != null) {
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    String name = c.getString(c.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME));
                    String text_ = "Authentication Successfull\nWelcome " + name;
                    text.setText(text_);
                }
                c.close();
            }
        } else {
            String text_ = "Authentication Successfull\nWelcome User";
            text.setText(text_);
        }
    }
}
