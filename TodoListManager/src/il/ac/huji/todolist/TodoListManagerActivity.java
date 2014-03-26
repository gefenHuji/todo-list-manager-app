package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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

public class TodoListManagerActivity extends Activity {

	private static final int START_DIALOG_ACTIVITY = 1;
	Pattern pattern = Pattern.compile("Call (.*)");

	ListView listview = null;
	ArrayList<Task> array = new ArrayList<Task>();
	CostumArrayAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		listview = (ListView) findViewById(R.id.lstTodoItems);

		registerForContextMenu(listview);

		adapter = new CostumArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, array);
		listview.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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
				if (!data.hasExtra("title") || !data.hasExtra("dueDate")) {
					//TODO: there was a problem
				} else {
					String task = data.getStringExtra("title"); //TODO: consts title
					Date date =(Date) data.getSerializableExtra("dueDate"); //TODO: consts dueDate
					
					array.add(new Task(task, date));
					adapter.notifyDataSetChanged();
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
		String item = adapter.getItem(info.position).taskStr;

		menu.setHeaderTitle(item);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.edit_task_menu, menu);
		Matcher matcher = pattern.matcher(item);
		if (!matcher.find()) {
			menu.findItem(R.id.menuItemCall).setVisible(false);
		} else {
			menu.findItem(R.id.menuItemCall).setVisible(true);
			menu.findItem(R.id.menuItemCall).setTitle(item);
		}
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info =(AdapterContextMenuInfo)item.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.menuItemDelete:			
			array.remove(info.position);
			adapter.notifyDataSetChanged();
			break;
		case R.id.menuItemCall:
//			System.out.println(item.getTitle());
			String number = "";
			Matcher matcher = pattern.matcher(item.getTitle());
			if (!matcher.find()) {
				//TODO:
				System.out.println("something is wrong");
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

}
