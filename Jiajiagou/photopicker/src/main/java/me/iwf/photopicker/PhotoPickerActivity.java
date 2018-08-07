package me.iwf.photopicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.photopicker.R;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.entity.Photo;
import me.iwf.photopicker.event.OnItemCheckListener;
import me.iwf.photopicker.fragment.ImagePagerFragment;
import me.iwf.photopicker.fragment.PhotoPickerFragment;
import me.iwf.photopicker.widget.Titlebar;

import static android.widget.Toast.LENGTH_LONG;

public class PhotoPickerActivity extends AppCompatActivity {

    private PhotoPickerFragment pickerFragment;
    private ImagePagerFragment imagePagerFragment;
    //private MenuItem menuDoneItem;

    private int maxCount = PhotoPicker.DEFAULT_MAX_COUNT;

    /**
     * to prevent multiple calls to inflate menu
     */
    // private boolean menuIsInflated = false;

    private boolean showGif = false;
    private int columnNumber = PhotoPicker.DEFAULT_COLUMN_NUMBER;
    private ArrayList<String> originalPhotos = null;

    private Titlebar titlebar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean showCamera = getIntent().getBooleanExtra(PhotoPicker.EXTRA_SHOW_CAMERA, true);
        boolean showGif = getIntent().getBooleanExtra(PhotoPicker.EXTRA_SHOW_GIF, false);
        boolean previewEnabled = getIntent().getBooleanExtra(PhotoPicker.EXTRA_PREVIEW_ENABLED,
                true);

        setShowGif(showGif);

        setContentView(R.layout.__picker_activity_photo_picker);

        titlebar = (Titlebar) findViewById(R.id.titlebar);
        titlebar.init(this);

        maxCount = getIntent().getIntExtra(PhotoPicker.EXTRA_MAX_COUNT, PhotoPicker
                .DEFAULT_MAX_COUNT);
        columnNumber = getIntent().getIntExtra(PhotoPicker.EXTRA_GRID_COLUMN, PhotoPicker
                .DEFAULT_COLUMN_NUMBER);
        originalPhotos = getIntent().getStringArrayListExtra(PhotoPicker.EXTRA_ORIGINAL_PHOTOS);


        pickerFragment = (PhotoPickerFragment) getSupportFragmentManager().findFragmentByTag("tag");
        if (pickerFragment == null) {
            pickerFragment = PhotoPickerFragment.newInstance(showCamera, showGif, previewEnabled,
                    columnNumber, maxCount, originalPhotos);
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    pickerFragment, "tag").commit();
            getSupportFragmentManager().executePendingTransactions();
        }

        //右边的点击事件
        titlebar.getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> photos = pickerFragment.getPhotoGridAdapter()
                        .getSelectedPhotoPaths();
                if (photos != null && photos.size() > 0) {
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS, photos);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "还没有选择图片", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pickerFragment.getPhotoGridAdapter().setOnItemCheckListener(new OnItemCheckListener() {
            @Override
            public boolean OnItemCheck(int position, Photo photo, final boolean isCheck, int
                    selectedItemCount) {

                int total = selectedItemCount + (isCheck ? -1 : 1);

                // menuDoneItem.setEnabled(total > 0);


                if (maxCount <= 1) {
                    List<Photo> photos = pickerFragment.getPhotoGridAdapter().getSelectedPhotos();
                    if (!photos.contains(photo)) {
                        photos.clear();
                        pickerFragment.getPhotoGridAdapter().notifyDataSetChanged();
                    }
                    return true;
                }

                if (total > maxCount) {
                    Toast.makeText(getActivity(), getString(R.string
                            .__picker_over_max_count_tips, maxCount), LENGTH_LONG).show();
                    return false;
                }
                titlebar.getTvRight().setText(getString(R.string.__picker_done_with_count, total,
                        maxCount));
                return true;
            }
        });

    }


    /**
     * Overriding this method allows us to run our exit animation first, then exiting
     * the activity when it complete.
     */
    @Override
    public void onBackPressed() {
        if (imagePagerFragment != null && imagePagerFragment.isVisible()) {
            imagePagerFragment.runExitAnimation(new Runnable() {
                public void run() {
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStack();
                    }
                }
            });
        } else {
            super.onBackPressed();
        }
    }


    public void addImagePagerFragment(ImagePagerFragment imagePagerFragment) {
        this.imagePagerFragment = imagePagerFragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.container, this
                .imagePagerFragment).addToBackStack(null).commit();
    }


    public PhotoPickerActivity getActivity() {
        return this;
    }

    public boolean isShowGif() {
        return showGif;
    }

    public void setShowGif(boolean showGif) {
        this.showGif = showGif;
    }
}
