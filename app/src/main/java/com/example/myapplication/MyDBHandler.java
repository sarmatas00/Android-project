package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="myAppDB.db";
    public static final String TABLE_ACCOUNTS="accounts";
    public static final String COLUMN_USERNAME="username";
    public static final String COLUMN_EMAIL="email";
    public static final String COLUMN_PASSWORD="password";
    public static final String TABLE_ITEMS="items";
    public static final String COLUMN_NAME="name";
    public static final String COLUMN_AMOUNT="amount";
    public static final String COLUMN_USERID="userId";

    public MyDBHandler(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,DATABASE_NAME,factory,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_ACCOUNTS_TABLE="CREATE TABLE IF NOT EXISTS "+
                TABLE_ACCOUNTS+"("+
                COLUMN_USERNAME+" TEXT PRIMARY KEY,"+
                COLUMN_EMAIL+" TEXT,"+
                COLUMN_PASSWORD+" TEXT"+")";
        sqLiteDatabase.execSQL(CREATE_ACCOUNTS_TABLE);
        String CREATE_ITEMS_TABLE="CREATE TABLE IF NOT EXISTS "+
                TABLE_ITEMS+"("+
                COLUMN_NAME+" TEXT PRIMARY KEY,"+
                COLUMN_AMOUNT+" INTEGER,"+
                COLUMN_USERNAME+" TEXT REFERENCES accounts(username)"+")";

        sqLiteDatabase.execSQL(CREATE_ACCOUNTS_TABLE);
        sqLiteDatabase.execSQL(CREATE_ITEMS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_ACCOUNTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_ITEMS);
        onCreate(sqLiteDatabase);
    }
    public void addAccount(Account acc){
        ContentValues values=new ContentValues();
        values.put(COLUMN_USERNAME,acc.getUsername());
        values.put(COLUMN_EMAIL,acc.getEmail());
        values.put(COLUMN_PASSWORD,acc.getPassword());
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.insert(TABLE_ACCOUNTS,null,values);
        sqLiteDatabase.close();
    }
    public void addItem(Item it){
        ContentValues values=new ContentValues();
        values.put(COLUMN_NAME,it.getName());
        values.put(COLUMN_AMOUNT,it.getAmount());
        values.put(COLUMN_USERNAME,it.getUsername());
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.insert(TABLE_ITEMS,null,values);
        sqLiteDatabase.close();
    }
    //find based on username
    public Account findAccount(String username){
        String query="SELECT * FROM "+TABLE_ACCOUNTS+" WHERE "+COLUMN_USERNAME+" = '"+username+"'";
        return queryAccountDB(query);
    }
    public Item findItem(String itemName){
        String query="SELECT * FROM "+TABLE_ITEMS+" WHERE "+COLUMN_NAME+" = '"+itemName+"'";
        return queryItemDB(query);
    }
    /*
    public Item[] findUserItems(String username){
        String query="SELECT * FROM "+TABLE_ITEMS+" WHERE "+COLUMN_USERID+" = "+id;
    }

     */
    public Account queryAccountDB(String query){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        Account account=new Account();
        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            account.setUsername(cursor.getString(0));
            account.setEmail(cursor.getString(1));
            account.setPassword(cursor.getString(2));
            cursor.close();
        }else{
            account=null;
        }
        sqLiteDatabase.close();
        return account;
    }
    public Item queryItemDB(String query){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        Item item=new Item();
        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            item.setName(cursor.getString(0));
            item.setAmount(cursor.getInt(1));
            item.setUsername(cursor.getString(2));
            cursor.close();
        }else{
            item=null;
        }
        sqLiteDatabase.close();
        return item;
    }
    public boolean deleteAccount(String username){
        boolean result=false;
        Account account=findAccount(username);
        if (account != null) {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            result=sqLiteDatabase.delete(TABLE_ACCOUNTS, COLUMN_USERNAME + " =?",new String[]{username})>0;
            sqLiteDatabase.close();
        }
        return result;
    }


}
