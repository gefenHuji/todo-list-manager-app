package il.ac.huji.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

	//DB CONSTS
	public static final String DATABASE_NAME="todo_db";
	public static final int DATABASE_VERSION = 1;
	
	//TABLE CONSTS
	public static final String TABLE_NAME = "todo";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_DATE = "due";
	
	//QUERY CONSTS
	private static final String DATABASE_CREATE =
			"create table " + TABLE_NAME + "(" + COLUMN_ID +
			" integer primary key autoincrement, " + COLUMN_TITLE +
			" text not null, " + COLUMN_DATE + " text not null );";
	
	private static final String DROP_TABLE = 
			"drop table if exists ";
	
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL(DROP_TABLE + TABLE_NAME);
		onCreate(database);
	}

}
