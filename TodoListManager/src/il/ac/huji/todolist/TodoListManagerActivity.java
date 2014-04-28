package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import com.parse.ParseObject;

public class TodoListManagerActivity extends Activity {

	ArrayList<ParseObject> objects = new ArrayList<ParseObject>();
	
	private TasksDataSource datasource;
	
	private static final String CLASS_NAME = "todo";
	
	public static final String EXTRA_TITLE = "title";
	public static final String EXTRA_DATE = "dueDate";
	
	private static final String PARSE_TITLE = "title";
	private static final String PARSE_DATE = "due";

	private static final int START_DIALOG_ACTIVITY = 1;
	Pattern pattern = Pattern.compile("Call (.*)");

	CustomCursorAdapter cursorAdapter = null;
	ListView listview = null;
	ArrayList<String> array = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);

		String[] from = new String[] {MySQLiteHelper.COLUMN_TITLE,
				MySQLiteHelper.COLUMN_DATE};
		int[] to = new int[] {R.id.txtTodoTitle, R.id.txtTodoDueDate};
		listview = (ListView) findViewById(R.id.lstTodoItems);
		array = new ArrayList<String>();
		registerForContextMenu(listview);
		
		
		datasource = new TasksDataSource(getApplicationContext());
		cursorAdapter = new CustomCursorAdapter(
				getApplicationContext(), R.layout.row, null, from, to);
		
		GetAllTasks allTasks = new GetAllTasks();
		allTasks.execute();
		
		listview.setAdapter(cursorAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuItemAdd:
			Intent sendIntent = new Intent(getApplicationContext(), AddNewTodoItemActivity.class);
			startActivityForResult(sendIntent, START_DIALOG_ACTIVITY);
		}
		return false;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == START_DIALOG_ACTIVITY) {

			if(resultCode == RESULT_OK){
				if (!data.hasExtra(EXTRA_TITLE) || !data.hasExtra(EXTRA_DATE)) {
					//not suppose to get in here
				} else {
					String title = data.getStringExtra(EXTRA_TITLE);
					Date date =(Date) data.getSerializableExtra(EXTRA_DATE);
					Task task = new Task(title, date);
					CreateTask create = new CreateTask(task);
					create.execute();
					
					ParseObject obj = new ParseObject(CLASS_NAME);
					obj.put(PARSE_TITLE, title);
					obj.put(PARSE_DATE, task.getDateAsString());
					obj.saveInBackground();
					objects.add(obj);
				}
			}
		}
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info;
		try {
			info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		} catch (ClassCastException e) {
			return;
		}
		
		Cursor cursor = (Cursor) cursorAdapter.getItem(info.position);
		Task task = datasource.cursorToTask(cursor);
		String title = task.getTitle();
		
		menu.setHeaderTitle(title);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.edit_task_menu, menu);
		Matcher matcher = pattern.matcher(title);
		if (!matcher.find()) {
			menu.findItem(R.id.menuItemCall).setVisible(false);
		} else {
			menu.findItem(R.id.menuItemCall).setVisible(true);
			menu.findItem(R.id.menuItemCall).setTitle(title);
		}
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info =(AdapterContextMenuInfo)item.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.menuItemDelete:
			
			DeleteTask delete = new DeleteTask(info.id);
			delete.execute();
			
			objects.get(info.position).deleteInBackground();
			objects.remove(info.position);
			
			break;
		case R.id.menuItemCall:
			String number = "";
			Matcher matcher = pattern.matcher(item.getTitle());
			if (!matcher.find()) {
				break;
			} else {
				number = matcher.group(1);
			}
		    Intent intent = new Intent(Intent.ACTION_DIAL);
		    intent.setData(Uri.parse("tel:" +number));
		    startActivity(intent);
			break;
		default:
			break;
		}
		return false;
	}
	
	private class CreateTask extends AsyncTask<Void, Cursor, Void> {
		
		private Task task;

		public CreateTask(Task task) {
			this.task = task;
		}
		
		@Override
		protected void onPreExecute() {
			datasource.open();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			datasource.createTask(task);
			Cursor cursor = datasource.getCursorForAllTasks();

			publishProgress(cursor);
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Cursor... values) {
			cursorAdapter.changeCursor(values[0]);
		}
		
		@Override
		protected void onPostExecute(Void result) {
			datasource.close();
		}
	}
	
	
	
	private class DeleteTask extends AsyncTask<Integer, Cursor, Void> {

		private long id;

		public DeleteTask(long id) {
			this.id = id;
		}
		
		@Override
		protected void onPreExecute() {
			datasource.open();
		}

		@Override
		protected Void doInBackground(Integer... params) {
			datasource.deleteTask(id);
			Cursor cursor = datasource.getCursorForAllTasks();
			publishProgress(cursor);
			return null;
		}

		@Override
		protected void onProgressUpdate(Cursor... values) {
			cursorAdapter.changeCursor(values[0]);
		}

		@Override
		protected void onPostExecute(Void result) {
			datasource.close();
		}

	}
	
	private class GetAllTasks extends AsyncTask<Void, Cursor, Void> {

		@Override
		protected void onPreExecute() {
			datasource.open();
		}

		@Override
		protected Void doInBackground(Void... params) {
			Cursor cursor = datasource.getCursorForAllTasks();

			publishProgress(cursor);
			return null;
		}

		@Override
		protected void onProgressUpdate(Cursor... values) {
			cursorAdapter.changeCursor(values[0]);
		}

		@Override
		protected void onPostExecute(Void result) {
			datasource.close();
		}

	}
	
}
