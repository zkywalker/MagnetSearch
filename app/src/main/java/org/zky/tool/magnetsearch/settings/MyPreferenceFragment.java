package org.zky.tool.magnetsearch.settings;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AlertDialog;

import org.zky.tool.magnetsearch.R;
import org.zky.tool.magnetsearch.search.MainActivity;
import org.zky.tool.magnetsearch.search.factory.SearchSourceFactory;
import org.zky.tool.magnetsearch.utils.GetRes;
import org.zky.tool.magnetsearch.utils.PreferenceUtils;
import org.zky.tool.magnetsearch.utils.StorageUtils;

import static org.zky.tool.magnetsearch.base.BaseThemeActivity.THEME_BLUE;
import static org.zky.tool.magnetsearch.base.BaseThemeActivity.THEME_CYAN;
import static org.zky.tool.magnetsearch.base.BaseThemeActivity.THEME_DEFAULT;
import static org.zky.tool.magnetsearch.base.BaseThemeActivity.THEME_GREEN;
import static org.zky.tool.magnetsearch.base.BaseThemeActivity.THEME_ORANGE;
import static org.zky.tool.magnetsearch.base.BaseThemeActivity.THEME_PINK;
import static org.zky.tool.magnetsearch.base.BaseThemeActivity.THEME_PURPLE;
import static org.zky.tool.magnetsearch.base.BaseThemeActivity.THEME_RED;
import static org.zky.tool.magnetsearch.base.BaseThemeActivity.THEME_TEAL;

public class MyPreferenceFragment extends PreferenceFragment {

        private void market() {
            try {
                String str = "market://details?id=org.zky.tool.magnetsearch";
                Intent localIntent = new Intent(Intent.ACTION_VIEW);
                localIntent.setData(Uri.parse(str));
                startActivity(localIntent);
            } catch (Exception e) {
                // 打开应用商店失败 可能是没有手机没有安装应用市场
                e.printStackTrace();
            }
        }

        /**
         * @deprecated 废弃
         */
        public void showList() {
            final String[] list = new String[]{GetRes.getString(R.string.todo_1),
                    GetRes.getString(R.string.todo_2),
                    GetRes.getString(R.string.todo_3)

            };
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());
            builder.setTitle(GetRes.getString(R.string.pref_title_todo));
            builder.setItems(list, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                Snackbar.make(v, list[which], Snackbar.LENGTH_LONG).show();
                }
            }).show();
        }

        @Override
        public void onResume() {
            super.onResume();

        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);

            Preference preference1 = findPreference(GetRes.getString(R.string.key_search_source));
            preference1.setSummary(SearchSourceFactory.getInstance(PreferenceUtils.getValue(getActivity(),R.string.key_search_source)).getName());
            preference1.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    preference.setSummary(SearchSourceFactory.getInstance((String)newValue).getName());
                    return true;
                }
            });


            ListPreference preference = (ListPreference) findPreference(GetRes.getString(R.string.key_theme));
            setListPreferenceSummary(preference);
            preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    PreferenceUtils.setTheme(getActivity(), (String) newValue);
                    recreateActivity();
                    return false;
                }
            });

            final Preference preCache = findPreference(GetRes.getString(R.string.key_clean));
            preCache.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    StorageUtils.cleanData();
                    StorageUtils.cleanDatabaseByName("history-db");
//                    preCache.setSummary(StorageUtils.getSize(MagnetSearchApp.getInstance()));
                    Snackbar.make(getView(),GetRes.getString(R.string.clean_success),Snackbar.LENGTH_LONG).show();
                    return true;
                }
            });
//            preCache.setSummary(StorageUtils.getSize(MagnetSearchApp.getInstance()));

            Preference preScore = findPreference(GetRes.getString(R.string.key_score));
            preScore.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    market();
                    return true;
                }
            });

//            Preference preTodo = findPreference(GetRes.getString(R.string.key_todo_list));
//            preTodo.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference preference) {
//                    showList();
//                    return true;
//                }
//            });

//            final SwitchPreference preOpenVideo = (SwitchPreference) findPreference(GetRes.getString(R.string.key_open_video));
//            preOpenVideo.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//                @Override
//                public boolean onPreferenceChange(final Preference preference, Object newValue) {
//                    if (newValue == true) {
//                        if (dialog == null)
//                            dialog = new AlertDialog.Builder(getActivity())
//                                    .setTitle("FBI WARNING")
//                                    .setMessage("该功能仅供测试，不稳定，慎用")
//                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//
//                                            dialog.dismiss();
//                                        }
//                                    })
//                                    .create();
//                    }
//                    return false;
//                }
//            });

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

        public void recreateActivity() {
            final Intent intent = IntentCompat.makeMainActivity(new ComponentName(getActivity(), MainActivity.class));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        }
    }