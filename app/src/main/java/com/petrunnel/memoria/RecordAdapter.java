package com.petrunnel.memoria;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.widget.Toast;

class RecordAdapter 
{
	// Название файла в котором хранятся данные
	private static final String FILE_RECORDS = "memoria-records";
	
	// Два массива для рекордов
	ArrayList<String> recTime;
	ArrayList<Integer> recPoint;
	
	Context mContext;
	
	@SuppressWarnings("unchecked")
	public RecordAdapter(Context context)
	{
		recTime = new ArrayList<>();
		recPoint = new ArrayList<>();
		
		mContext = context;
		// читаем из файла два массива с рекордами
		try {
			FileInputStream fis = mContext.openFileInput(FILE_RECORDS);
			ObjectInputStream is = new ObjectInputStream(fis);
			recPoint = (ArrayList<Integer>) is.readObject();
			recTime = (ArrayList<String>) is.readObject();
			is.close();
		} catch (Exception e) {
			Toast.makeText(mContext, "Произошла ошибка чтения таблицы рекордов", Toast.LENGTH_LONG).show();
		}
	}
	
	public void WriteRecords ()
	{
		// записываем в файл массивы с рекордами
		try {
			FileOutputStream fos = mContext.openFileOutput(FILE_RECORDS, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(recPoint);
			os.writeObject(recTime);
			os.close();
		} catch (Exception e) {
			Toast.makeText(mContext, "Произошла ошибка записи в таблицу рекордов", Toast.LENGTH_LONG).show();
		}
	}
	
	public void addTime (String str)
	{
		// Добавляем новое значение времени,
		// если такого еще нет в массиве
		if (!recTime.contains(str))
			recTime.add(str);
		
		// сортируем
		Collections.sort(recTime);
		
		// оставляем в массиве только 5 элементов
		for (int i = 5; i < recTime.size(); i++)
			recTime.remove(i);
	}
	
	public void addPoint (Integer num)
	{
		if (!recPoint.contains(num))
			recPoint.add(num);
		
		Collections.sort(recPoint);
		
		for (int i = 5; i < recPoint.size(); i++)
			recPoint.remove(i);
	}
	
	public ArrayList<String> getRecTime()
	{
		// возвращаем массив рекордов по времени
		return recTime;
	}
	
	public ArrayList<String> getRecPoint()
	{
		// переделываем массив целых чисел в массив строк
		ArrayList<String> arr = new ArrayList<>();
		for (Integer temp : recPoint)
			arr.add(temp.toString());
		
		// возвращаем массив рекордов по очкам
		return arr;
	}
}