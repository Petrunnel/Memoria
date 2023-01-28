package com.petrunnel.memoria;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class Settings extends PreferenceActivity implements Preference.OnPreferenceChangeListener
{
	ListPreference collection;
	ListPreference color;
	
	// два массива для хранения значений списков
	CharSequence[] pictValue, colValue; 
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        
        collection = (ListPreference)this.findPreference("PictureCollection");
        color = (ListPreference)this.findPreference("BackgroundColor");
        
        // устанавливаем слушатель
        collection.setOnPreferenceChangeListener(this);
        color.setOnPreferenceChangeListener(this);
        
        // пишем в summary текущее значение
        collection.setSummary(collection.getEntry());
        color.setSummary(color.getEntry());
        
        // получаем списки значений для каждой настройки 
        pictValue = collection.getEntries();
        colValue = color.getEntries();
    }
    
    @Override
	public boolean onPreferenceChange(Preference preference, Object newValue)
    {
    	// название (android:key) настройки, которая была изменена
    	String Key = preference.getKey();
    	
    	// если это коллекция картинок
    	if (Key.equals("PictureCollection"))
    	{
    		// определяем индекс нового значения
    		int i = ((ListPreference)preference).findIndexOfValue(newValue.toString());
    		// ищем русское название для значения с этим индексом и записываем в summary
    		preference.setSummary(pictValue[i]);
    		return true;
    	}
    	
    	// если это цвет
    	if (Key.equals("BackgroundColor"))
    	{
    		int i = ((ListPreference)preference).findIndexOfValue(newValue.toString());
    		preference.setSummary(colValue[i]);
    		return true;
    	}
    	
    	// для всех остальных настроек (хотя у нас их и нет), ставим пришедшее значение в summary
        preference.setSummary((CharSequence)newValue);
        return true;
    }
}