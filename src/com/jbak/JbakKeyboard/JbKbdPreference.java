package com.jbak.JbakKeyboard;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;


public class JbKbdPreference extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pref_view);
        addPreferencesFromResource(R.xml.preferences);
        PreferenceScreen ps = getPreferenceScreen();
        ps.removePreference(ps.findPreference("custom_key_settings"));
        setShiftState();
        st.pref(this).registerOnSharedPreferenceChangeListener(this);
    }
    @Override
    protected void onDestroy()
    {
        st.pref(this).unregisterOnSharedPreferenceChangeListener(this);
        if(JbKbdView.inst!=null)
            JbKbdView.inst.setPreferences();
        super.onDestroy();
    }
    void runSetKbd(int action)
    {
        try{
            Intent in = new Intent(Intent.ACTION_VIEW)
            .setComponent(new ComponentName(this, SetKbdActivity.class))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .putExtra(st.SET_INTENT_ACTION, action);
            startActivity(in);
        }
        catch(Throwable e)
        {
            st.logEx(e);
        }

    }
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) 
    {
        String k = preference.getKey();
        Context c = preference.getContext();
        if("set_skins".equals(k))
        {
            runSetKbd(st.SET_SELECT_SKIN);
            return true;
        }
        if("pref_port_key_height".equals(k))
        {
            runSetKbd(st.SET_KEY_HEIGHT_PORTRAIT);
            return true;
        }
        if("pref_land_key_height".equals(k))
        {
            runSetKbd(st.SET_KEY_HEIGHT_LANDSCAPE);
            return true;
        }
        if("set_key_main_font".equals(k))
        {
            c.startActivity(
                    new Intent(c,EditSetActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(EditSetActivity.EXTRA_PREF_KEY, st.PREF_KEY_MAIN_FONT)
                    .putExtra(EditSetActivity.EXTRA_DEFAULT_EDIT_SET, st.paint().getDefaultMain().toString())
                );
            
        }
        if("set_key_second_font".equals(k))
         {
             c.startActivity(
                     new Intent(c,EditSetActivity.class)
                     .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                     .putExtra(EditSetActivity.EXTRA_PREF_KEY, st.PREF_KEY_SECOND_FONT)
                     .putExtra(EditSetActivity.EXTRA_DEFAULT_EDIT_SET, st.paint().getDefaultSecond().toString())
                 );
             
         }
        if("set_key_label_font".equals(k))
         {
             c.startActivity(
                     new Intent(c,EditSetActivity.class)
                     .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                     .putExtra(EditSetActivity.EXTRA_PREF_KEY, st.PREF_KEY_LABEL_FONT)
                     .putExtra(EditSetActivity.EXTRA_DEFAULT_EDIT_SET, st.paint().getDefaultLabel().toString())
                 );
             
         }
        if("pref_languages".equals(k))
        {
            st.runAct(LangSetActivity.class,c);
            return true;
        }
        if("custom_key_settings".equals(k))
        {
            st.runAct(KeySetActivity.class,c);
            return true;
        }
        if("fs_editor_set".equals(k))
        {
            getApplicationContext().startActivity(
                    new Intent(getApplicationContext(),EditSetActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(EditSetActivity.EXTRA_PREF_KEY, st.PREF_KEY_EDIT_SETTINGS)
                );
            return true;
        }
        if("about_app".equals(k))
        {
            st.runAct(AboutActivity.class,c);
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
    void setShiftState()
    {
        Preference p = getPreferenceScreen().findPreference(st.PREF_KEY_SHIFT_STATE);
        if(p!=null)
        {
            int v = Integer.decode(st.pref(this).getString(st.PREF_KEY_SHIFT_STATE, "0"));
            p.setSummary(getResources().getStringArray(R.array.array_shift_vars)[v]);
        }
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if(st.PREF_KEY_SHIFT_STATE.equals(key))
            setShiftState();
    }
}
