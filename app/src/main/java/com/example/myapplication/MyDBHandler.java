package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {
    //all table names and column names must be listed here
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="myAppDB.db";
    public static final String TABLE_ACCOUNTS="accounts";
    public static final String COLUMN_USERNAME="username";
    public static final String COLUMN_EMAIL="email";
    public static final String COLUMN_PASSWORD="password";
    public static final String TABLE_ITEMS="items";
    public static final String COLUMN_NAME="name";
    public static final String TABLE_ACCOUNT_HAS_ITEMS="userHasItems";
    public static final String COLUMN_AMOUNT="amount";
    public static final String COLUMN_COST="cost";
    public static final String TABLE_LOGGED_IN="loggedIn";

    public MyDBHandler(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,DATABASE_NAME,factory,DATABASE_VERSION);
    }

    //When app first runs on a phone or re-install onCreate makes tables
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //ACCOUNTS(USERNAME STRING KEY, EMAIL STRING, PASSWORD STRING)
        String CREATE_ACCOUNTS_TABLE="CREATE TABLE IF NOT EXISTS "+
                TABLE_ACCOUNTS+"("+
                COLUMN_USERNAME+" TEXT PRIMARY KEY,"+
                COLUMN_EMAIL+" TEXT,"+
                COLUMN_PASSWORD+" TEXT"+")";

        //ITEMS(NAME STRING KEY, COST DOUBLE)
        String CREATE_ITEMS_TABLE="CREATE TABLE IF NOT EXISTS "+
                TABLE_ITEMS+"("+
                COLUMN_NAME+" TEXT PRIMARY KEY,"+
                COLUMN_COST+" DOUBLE"
                + ")";

        //ACCOUNT_HAS_ITEMS(USERNAME,NAME,AMOUNT) NAME IS ITEMNAME USERNAME+NAME PRIMARY KEY
        String CREATE_ACCOUNT_HAS_ITEMS_TABLE="CREATE TABLE IF NOT EXISTS "+
                TABLE_ACCOUNT_HAS_ITEMS+"("+
                COLUMN_USERNAME+" TEXT,"+
                COLUMN_NAME+" TEXT,"+
                COLUMN_AMOUNT+" INTEGER, PRIMARY KEY ("+COLUMN_USERNAME+", "+COLUMN_NAME+"))";

        String CREATE_LOGGED_IN_TABLE="CREATE TABLE IF NOT EXISTS "+
                TABLE_LOGGED_IN+"("+
                COLUMN_USERNAME+" TEXT PRIMARY KEY"+")";

        sqLiteDatabase.execSQL(CREATE_ACCOUNTS_TABLE);
        sqLiteDatabase.execSQL(CREATE_ITEMS_TABLE);
        sqLiteDatabase.execSQL(CREATE_ACCOUNT_HAS_ITEMS_TABLE);
        sqLiteDatabase.execSQL(CREATE_LOGGED_IN_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_ACCOUNTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_ITEMS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_ACCOUNT_HAS_ITEMS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_LOGGED_IN);
        onCreate(sqLiteDatabase);
    }
    public void addLogged(String username){
        ContentValues values=new ContentValues();
        values.put(COLUMN_USERNAME,username);
        SQLiteDatabase db=this.getWritableDatabase();
        db.insert(TABLE_LOGGED_IN,null,values);
        db.close();
    }
    public void clearLogged(){
        SQLiteDatabase db=this.getWritableDatabase();
        //db.delete()
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
        if(findItem(it.getName())==null) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, it.getName());
            values.put(COLUMN_COST, it.getCost());
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            sqLiteDatabase.insert(TABLE_ITEMS, null, values);
            sqLiteDatabase.close();
        }
    }
    public void addItemToAccount(String username,Item item,int amount){
        ContentValues values=new ContentValues();
        values.put(COLUMN_USERNAME,username);
        values.put(COLUMN_NAME,item.getName());
        values.put(COLUMN_AMOUNT,amount);
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
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
    public ArrayList<Item> findUserItems(String username){
        String query="SELECT * FROM "+TABLE_ITEMS+" WHERE "+COLUMN_USERNAME+" = "+username;
        ArrayList<Item> userItems=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                Item item=new Item();
                item.setName(cursor.getString(0));
                item.setAmount(cursor.getInt(1));
                item.setUsername(cursor.getString(2));
                userItems.add(item);
            }while(cursor.moveToNext());
            cursor.close();
        }
        sqLiteDatabase.close();
        return userItems;
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
            item.setCost(cursor.getDouble(1));
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
