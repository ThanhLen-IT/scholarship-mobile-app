package com.nhom0.scholarshipgateway.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.nhom0.scholarshipgateway.R;

public class LoginActivity extends AppCompatActivity {

    private Button btnRequestCamera;
    private Button btnRequestStorage;


    private final ActivityResultLauncher<String> cameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, "Đã cấp quyền Camera thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    handlePermissionDenied("Camera", Manifest.permission.CAMERA);
                }
            });


    private final ActivityResultLauncher<String> storagePermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, "Đã cấp quyền truy cập Ảnh thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    String perm = getStoragePermissionName();
                    handlePermissionDenied("Bộ nhớ / Thư viện ảnh", perm);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        btnRequestCamera = findViewById(R.id.btnRequestCamera);
        btnRequestStorage = findViewById(R.id.btnRequestStorage);


        btnRequestCamera.setOnClickListener(v -> checkAndRequestCameraPermission());


        btnRequestStorage.setOnClickListener(v -> checkAndRequestStoragePermission());
    }


    private void checkAndRequestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Quyền Camera đã được cấp trước đó!", Toast.LENGTH_SHORT).show();
        } else {
            // Kiểm tra xem có cần giải thích lý do xin quyền (Rationale Dialog)
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                showRationaleDialog("Camera", "Ứng dụng cần quyền Camera để bạn chụp ảnh hồ sơ và quét mã QR Mentor.", () -> {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
                });
            } else {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
        }
    }


    private void checkAndRequestStoragePermission() {
        String permission = getStoragePermissionName();

        if (ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Quyền Bộ nhớ / Ảnh đã được cấp trước đó!", Toast.LENGTH_SHORT).show();
        } else {
            if (shouldShowRequestPermissionRationale(permission)) {
                showRationaleDialog("Bộ nhớ / Thư viện ảnh", "Ứng dụng cần quyền truy cập ảnh để tải mã QR thanh toán của Mentor.", () -> {
                    storagePermissionLauncher.launch(permission);
                });
            } else {
                storagePermissionLauncher.launch(permission);
            }
        }
    }


    private String getStoragePermissionName() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            return Manifest.permission.READ_EXTERNAL_STORAGE;
        }
    }


    private void showRationaleDialog(String featureName, String message, Runnable onConfirm) {
        new AlertDialog.Builder(this)
                .setTitle("Cần cấp quyền " + featureName)
                .setMessage(message)
                .setPositiveButton("Tiếp tục", (dialog, which) -> onConfirm.run())
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void handlePermissionDenied(String featureName, String permission) {
        if (!shouldShowRequestPermissionRationale(permission)) {
            Toast.makeText(this, "Bạn đã chặn quyền " + featureName + ". Vui lòng bật lại trong Cài đặt ứng dụng.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Quyền " + featureName + " bị từ chối!", Toast.LENGTH_SHORT).show();
        }
    }
}