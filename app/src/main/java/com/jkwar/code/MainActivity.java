package com.jkwar.code;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  TabLayout tabLayout;
  ViewPager pager;
  List<PageModel> pageModels = new ArrayList<>();

  {
    pageModels.add(
        new PageModel(R.layout.sample_scalable_image_view, R.string.title_scalable_image_view));
    pageModels.add(
        new PageModel(R.layout.sample_relay_type_view, R.string.title_relay_type_view));
    pageModels.add(
        new PageModel(R.layout.sample_collaborative_view, R.string.title_collaborative_view));
    pageModels.add(
        new PageModel(R.layout.sample_independent_type_view, R.string.title_independent_type_view));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    pager = findViewById(R.id.pager);
    pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

      @Override
      public Fragment getItem(int position) {
        PageModel pageModel = pageModels.get(position);
        return PageFragment.newInstance(pageModel.sampleLayoutRes);
      }

      @Override
      public int getCount() {
        return pageModels.size();
      }

      @Override
      public CharSequence getPageTitle(int position) {
        return getString(pageModels.get(position).titleRes);
      }
    });
    tabLayout = findViewById(R.id.tabLayout);
    tabLayout.setupWithViewPager(pager);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    return super.onCreateOptionsMenu(menu);
  }

  private class PageModel {
    @LayoutRes int sampleLayoutRes;
    @StringRes int titleRes;

    PageModel(@LayoutRes int sampleLayoutRes, @StringRes int titleRes) {
      this.sampleLayoutRes = sampleLayoutRes;
      this.titleRes = titleRes;
    }
  }
}
