package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TasksDataSource {
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = {MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_TITLE, MySQLiteHelper.COLUMN_DATE};
	
	public TasksDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void open() {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public Task createTask(Task task) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_TITLE, task.getTitle());
		values.put(MySQLiteHelper.COLUMN_DATE, task.getDateAsString());
		long insertId = database.insert(MySQLiteHelper.TABLE_NAME, null, values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME, allColumns, 
				MySQLiteHelper.COLUMN_ID + "=" + insertId, null, null, null, null);
		cursor.moveToFirst();
		Task newTask = cursorToTask(cursor);
		cursor.close();
		return newTask;
	}
	
	public Task getTask(int position) {
		return getAllTasks().get(position);
	}
	
	public List<Task> getAllTasks() {
		List<Task> tasksList = new ArrayList<Task>();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME, 
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (cursor.moveToNext()) {
			Task task = cursorToTask(cursor);
			tasksList.add(task);
		}
		cursor.close();
		return tasksList;
	}
	
	public void deleteTask(Long id) {
		database.delete(MySQLiteHelper.TABLE_NAME, 
				MySQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	public Task cursorToTask(Cursor cursor) {
		Task task = new Task();
		task.setId(cursor.getLong(0));
		task.setTitle(cursor.getString(1));
		task.setDateAsString(cursor.getString(2));
		return task;
	}
	
	public Cursor getCursorForAllTasks() {
		return database.query(MySQLiteHelper.TABLE_NAME, allColumns,
				null, null, null, null, null);
	}
}
