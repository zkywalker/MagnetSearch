package org.zky.tool.magnetsearch;

import android.content.ComponentName;
import android.content.Intent;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.content.IntentCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.zky.tool.magnetsearch.search.MainActivity;
import org.zky.tool.magnetsearch.utils.GetRes;
import org.zky.tool.magnetsearch.utils.PreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends BaseThemeActivity {
    private static final String TAG = "SettingsActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        toolbar.setTitle(GetRes.getString(R.string.action_settings));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);

            ListPreference preference = (ListPreference) findPreference(GetRes.getString(R.string.key_theme));
            setListPreferenceSummary(preference);
            preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.e(TAG, "onPreferenceChange: newvalue:" + newValue.toString());
                    PreferenceUtils.setTheme(getActivity(), (String) newValue);
                    ((SettingsActivity) getActivity()).recreateActivity();
                    return false;
                }
            });
        }

        public void setListPreferenceSummary(ListPreference preference) {
            switch (PreferenceUtils.getTheme(getActivity())) {
                case THEME_DEFAULT:
                    preference.setSummary(GetRes.getString(R.string.color_default));
                    break;
                case THEME_BLUE:
                    preference.setSummary(GetRes.getString(R.string.color_blue));
                    break;
                case THEME_RED:
                    preference.setSummary(GetRes.getString(R.string.color_red));

                    break;
                case THEME_GREEN:
                    preference.setSummary(GetRes.getString(R.string.color_green));

                    break;
                case THEME_CYAN:
                    preference.setSummary(GetRes.getString(R.string.color_cyan));

                    break;
                case THEME_PURPLE:
                    preference.setSummary(GetRes.getString(R.string.color_purple));

                    break;
                case THEME_ORANGE:
                    preference.setSummary(GetRes.getString(R.string.color_orange));

                    break;
                case THEME_PINK:
                    preference.setSummary(GetRes.getString(R.string.color_pink));

                    break;
                case THEME_TEAL:
                    preference.setSummary(GetRes.getString(R.string.color_teal));
                    break;
            }
        }
    }


    public void recreateActivity() {
        final Intent intent = IntentCompat.makeMainActivity(new ComponentName(this, MainActivity.class));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}