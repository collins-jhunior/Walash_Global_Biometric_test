package walashglobal.com;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    TextView sub_text;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sub_text = findViewById(R.id.sub_text);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void begin(View v) {
        if (canAuthenticate()) {
            final Executor executor = Executors.newSingleThreadExecutor();
            final BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(MainActivity.this)
                    .setTitle("Biometric Authentication")
                    .setDescription("Please complete authentication using what ever default biometric authentication you have setup on this device e.g fingerprint scanning, iris scanning or facial recognition e.t.c to proceed.")
                    .setNegativeButton("Cancel", executor, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).build();
            biometricPrompt.authenticate(new CancellationSignal(), executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(MainActivity.this, success.class));
                        }
                    });
                }

                @Override
                public void onAuthenticationFailed() {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            message("Your provided biometrics does not match any registered biometrics.");
                        }
                    });
                }

                @Override
                public void onAuthenticationError(int errorCode, final CharSequence errString) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            message(errString.toString());
                        }
                    });
                }

                @Override
                public void onAuthenticationHelp(int helpCode, final CharSequence helpString) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            message(helpString.toString());
                        }
                    });
                }
            });
        } else {
            message(canAuthenticate_());
        }
    }

    /*
     * Check if the android version in device is greater than
     * Marshmallow, since fingerprint authentication is only supported
     * from Android 6.0.
     * Note: If your project's minSdkversion is 23 or higher,
     * then you won't need to perform this check.
     *
     * */
    public boolean isSdkVersionSupported() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    public boolean isBiometricPromptEnabled() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P);
    }


    //Check if your device has biometric features.
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public boolean hasBiometric() {

        BiometricManager biometricManager = BiometricManager.from(this);
        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public boolean canAuthenticate() {
        return isSdkVersionSupported() && isBiometricPromptEnabled() && hasBiometric();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public String canAuthenticate_() {
        String error_message = "";
        if (!isSdkVersionSupported()) {
            error_message = "";
        } else if (!isBiometricPromptEnabled()) {
            error_message = "";
        } else if (!hasBiometric()) {
            BiometricManager biometricManager = BiometricManager.from(this);
            switch (biometricManager.canAuthenticate()) {
                case BiometricManager.BIOMETRIC_SUCCESS:
                    break;
                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    error_message = "No biometric features available on this device.";
                    break;
                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    error_message = "Biometric features are currently unavailable.";
                    break;
                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    error_message = "The user hasn't associated any biometric credentials with their account.";
                    break;
                default:
                    error_message = "Biometric authentication not supported on this device.";
                    break;
            }
        }
        return error_message;
    }

    public void message(String body) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        sub_text.setLayoutParams(lp);
        String text = "An error occurred. " + body;
        sub_text.setText(text);
    }
}
