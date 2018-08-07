package me.iwf.photopicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.photopicker.R;

import java.util.List;

import me.iwf.photopicker.fragment.ImagePagerFragment;
import me.iwf.photopicker.widget.MultiPickResultView;
import me.iwf.photopicker.widget.Titlebar;


/**
 * Created by donglua on 15/6/24.
 */
public class PhotoPagerActivity extends AppCompatActivity {

  private ImagePagerFragment pagerFragment;

  private boolean showDelete;
  private Titlebar titlebar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.__picker_activity_photo_pager);

    int currentItem = getIntent().getIntExtra(PhotoPreview.EXTRA_CURRENT_ITEM, 0);
    List<String> paths = getIntent().getStringArrayListExtra(PhotoPreview.EXTRA_PHOTOS);
    showDelete = getIntent().getBooleanExtra(PhotoPreview.EXTRA_SHOW_DELETE, false);
    int action = getIntent().getIntExtra(PhotoPreview.EXTRA_ACTION, MultiPickResultView.ACTION_ONLY_SHOW);

    if (pagerFragment == null) {
      pagerFragment =
          (ImagePagerFragment) getSupportFragmentManager().findFragmentById(R.id.photoPagerFragment);
    }
    pagerFragment.setPhotos(paths, currentItem);
    titlebar = (Titlebar) findViewById(R.id.titlebar);
    titlebar.init(this);
    if (action == MultiPickResultView.ACTION_SELECT){
      //=======================删除图片
//      titlebar.setRitht(getApplicationContext().getResources().getDrawable(R.drawable.__picker_delete), "", new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//          int position = pagerFragment.getViewPager().getCurrentItem();
//          if (pagerFragment.getPaths().size() >0){
//            pagerFragment.getPaths().remove(position);
//            pagerFragment.getViewPager().getAdapter().notifyDataSetChanged();
//            if (pagerFragment.getPaths().size() ==0){
//              titlebar.setTitle(getString(R.string.__picker_preview) +" "+getString(R.string.__picker_image_index, 0,
//                      pagerFragment.getPaths().size()));
//            }
//
//          }
//
//
//        }
//      });
    }

    titlebar.setTitle(getString(R.string.__picker_preview));




    pagerFragment.getViewPager().addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
      @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        titlebar.setTitle(getString(R.string.__picker_preview) +" "+getString(R.string.__picker_image_index, pagerFragment.getViewPager().getCurrentItem() + 1,
                pagerFragment.getPaths().size()));
      }
    });
  }

  @Override public void onBackPressed() {

    Intent intent = new Intent();
    intent.putExtra(PhotoPicker.KEY_SELECTED_PHOTOS, pagerFragment.getPaths());
    setResult(RESULT_OK, intent);
    finish();

    super.onBackPressed();
  }
}
