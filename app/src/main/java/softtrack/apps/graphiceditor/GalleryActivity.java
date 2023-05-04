package softtrack.apps.graphiceditor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.ActionBar;
import androidx.gridlayout.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class GalleryActivity extends AppCompatActivity {

    public GridLayout activityGalleryContainerImages;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        initialize();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initialize() {
        findViews();
        initializeActionBar();
        showFiles(activityGalleryContainerImages);
    }

    public void findViews() {
        activityGalleryContainerImages = findViewById(R.id.activity_gallery_container_images);
    }

    public void initializeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Моя галерея");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showFiles(GridLayout fileManager) {
        try {
            String currentPath = getApplicationContext().getDataDir().getPath();
            File[] listOfFiles = new FileTask().execute(currentPath).get();
            for(File fileInDir : listOfFiles){
                String filePath = fileInDir.getPath();
                Log.d("debug", "manager: " + filePath);
                LinearLayout file = new LinearLayout(GalleryActivity.this);
                file.setOrientation(LinearLayout.VERTICAL);
                file.setLayoutParams(new LinearLayout.LayoutParams(350, ViewGroup.LayoutParams.WRAP_CONTENT));
                ImageView fileImg = new ImageView(GalleryActivity.this);
                fileImg.setLayoutParams(new LinearLayout.LayoutParams(350, 350));
                fileImg.setScaleType(ImageView.ScaleType.FIT_XY);
                LinearLayout fileInfo = new LinearLayout(GalleryActivity.this);
                fileInfo.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams fileInfoLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                fileInfoLayoutParams.setMargins(35, 0, 0, 0);
                fileInfo.setLayoutParams(fileInfoLayoutParams);
                String rawFileName = fileInDir.getName();
                TextView fileMetaData = new TextView(GalleryActivity.this);
                long countOfMeasure = 0l;
                String measure = "MB";
                if (fileInDir.length() >= 8796093022208l) {
                    countOfMeasure = fileInDir.length() / 8796093022208l;
                    measure = "GB";
                } else if (fileInDir.length() >= 8589934592l) {
                    countOfMeasure = fileInDir.length() / 8589934592l;
                    measure = "MB";
                } else if (fileInDir.length() >= 8388608l) {
                    countOfMeasure = fileInDir.length() / 8388608l;
                    measure = "KB";
                } else if (fileInDir.getTotalSpace() >= 8192l) {
                    countOfMeasure = fileInDir.length() / 8192l;
                    measure = "B";
                }
                String rawFileSize = countOfMeasure + " " + measure;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // the format of your date
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    sdf = new SimpleDateFormat("dd MM yyyy г. HH:mm");
                }
                String formattedDate = sdf.format(new Date(fileInDir.lastModified() * 1000L));
                String rawFileMetaData = formattedDate;
                fileMetaData.setText(rawFileMetaData);
                TextView fileSizeLabel = new TextView(GalleryActivity.this);
                LinearLayout.LayoutParams fileSizeLabelLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                fileSizeLabelLayoutParams.setMargins(35, 0, 0, 0);
                fileSizeLabel.setLayoutParams(fileSizeLabelLayoutParams);
                fileSizeLabel.setText(rawFileSize);
                fileInfo.addView(fileMetaData);
                file.addView(fileImg);
                file.addView(fileInfo);
                file.addView(fileSizeLabel);
                boolean isImageDetect = rawFileName.endsWith(".png") || rawFileName.endsWith(".jpg");
                if (isImageDetect) {
                    fileImg.setImageResource(R.drawable.shape);
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    fileImg.setImageBitmap(bitmap);
                    fileManager.addView(file);
                }
                file.setContentDescription(fileInDir.getPath());
                file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String filePath = view.getContentDescription().toString();
                        Log.d("debug", "filePath: " + filePath);
                        Intent intent = new Intent(GalleryActivity.this, GalleryActivity.class);
                        intent.putExtra("filePath", filePath);
                        GalleryActivity.this.startActivity(intent);
                    }
                });
            }
        } catch (InterruptedException e) {
            Log.d("debug", "ошибка в FileTask");
        } catch (ExecutionException e) {
            Log.d("debug", "ошибка в FileTask");
        }
    }

}
