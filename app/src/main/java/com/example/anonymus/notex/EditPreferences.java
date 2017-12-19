package com.example.anonymus.notex;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;

public class EditPreferences extends PreferenceActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

    }
}
