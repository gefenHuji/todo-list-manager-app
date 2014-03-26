package il.ac.huji.todolist;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;

public class TodoListManagerActivity extends Activity {

	public static final String EMPTY_STRING = "^\\s*$";
	
	ListView listview = null;
	EditText editView = null;
	ArrayList<String> array = new ArrayList<String>();
	CostumArrayAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		listview = (ListView) findViewById(R.id.lstTodoItems);
		editView = (EditText) findViewById(R.id.edtNewItem);

		new ArrayList<String>();

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
			String newTask = editView.getText().toString();
			if (!(newTask.matches(EMPTY_STRING))) {				
				array.add(newTask);
				adapter.notifyDataSetChanged();
			}
			editView.setText("");
		}
		return false;
	}

	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info;
		try {
			info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		} catch (ClassCastException e) {
			return;
		}
		String item = adapter.getItem(info.position);

		menu.setHeaderTitle(item);// if your table name is name
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.edit_task_menu, menu);

	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info =(AdapterContextMenuInfo)item.getMenuInfo();
		array.remove(info.position);
		adapter.notifyDataSetChanged();
		return false;
	}

}
