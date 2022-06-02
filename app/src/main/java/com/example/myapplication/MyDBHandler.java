package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//This class handles the SQL database here we create the tables and create methods for them
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
                COLUMN_NAME+" TEXT PRIMARY KEY" + ")";

        //ACCOUNT_HAS_ITEMS(USERNAME,NAME,AMOUNT) NAME IS ITEMNAME USERNAME+NAME PRIMARY KEY
        String CREATE_ACCOUNT_HAS_ITEMS_TABLE="CREATE TABLE IF NOT EXISTS "+
                TABLE_ACCOUNT_HAS_ITEMS+"("+
                COLUMN_USERNAME+" TEXT,"+
                COLUMN_NAME+" TEXT,"+
                COLUMN_AMOUNT+" TEXT, PRIMARY KEY ("+COLUMN_USERNAME+", "+COLUMN_NAME+"))";


        sqLiteDatabase.execSQL(CREATE_ACCOUNTS_TABLE);
        sqLiteDatabase.execSQL(CREATE_ITEMS_TABLE);
        sqLiteDatabase.execSQL(CREATE_ACCOUNT_HAS_ITEMS_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_ACCOUNTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_ITEMS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_ACCOUNT_HAS_ITEMS);
        onCreate(sqLiteDatabase);
    }

    //This is how we add an account to database but getting an account item and creating a new row
    public void addAccount(Account acc){
        ContentValues values=new ContentValues();
        values.put(COLUMN_USERNAME,acc.getUsername());
        values.put(COLUMN_EMAIL,acc.getEmail());
        values.put(COLUMN_PASSWORD,acc.getPassword());
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.insertWithOnConflict(TABLE_ACCOUNTS,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        sqLiteDatabase.close();
    }
    //same for item
    public boolean addItem(Item it){
        if(findItem(it.getName())==null) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, it.getName());
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            sqLiteDatabase.insert(TABLE_ITEMS, null, values);
            sqLiteDatabase.close();
            return true;
        }
        return false;
    }
    public void addItemToAccount(String username,Item item,int amount){
        ContentValues values=new ContentValues();
        values.put(COLUMN_USERNAME,username);
        values.put(COLUMN_NAME,item.getName());
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        String previousAmount=findPreviousAmount(username,item.getName());
        if(previousAmount!=null) {
            values.put(COLUMN_AMOUNT,Integer.toString(amount+Integer.parseInt(previousAmount)));
            sqLiteDatabase.update(TABLE_ACCOUNT_HAS_ITEMS,values,COLUMN_USERNAME+"=? AND "+COLUMN_NAME+"=?",new String[]{username,item.getName()});
        }else{
            values.put(COLUMN_AMOUNT,Integer.toString(amount));
            sqLiteDatabase.insert(TABLE_ACCOUNT_HAS_ITEMS,null,values);
        }
        sqLiteDatabase.close();

    }


    public String findPreviousAmount(String username,String itemname){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT "+COLUMN_AMOUNT+" FROM "+TABLE_ACCOUNT_HAS_ITEMS+" WHERE "+COLUMN_USERNAME+"=? AND "+COLUMN_NAME+"=?",new String[]{username,itemname});
        if(cursor.moveToFirst()){
            return cursor.getString(0);
        }
        return null;
    }




    //find Account based on username users helper function
    public Account findAccount(String username){
        String query="SELECT * FROM "+TABLE_ACCOUNTS+" WHERE "+COLUMN_USERNAME+" = '"+username+"'";
        return queryAccountDB(query);
    }
    //Find item based on its name uses helper function queryItemDB
    public Item findItem(String itemName){
        String query="SELECT * FROM "+TABLE_ITEMS+" WHERE "+COLUMN_NAME+" = '"+itemName+"'";
        return queryItemDB(query);
    }

    public ArrayList<Item> findAllItems(){
        String query="SELECT * FROM "+TABLE_ITEMS;
        ArrayList<Item> allItems=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                Item item=new Item();
                item.setName(cursor.getString(0));
                allItems.add(item);
            }while(cursor.moveToNext());
            cursor.close();
        }
        sqLiteDatabase.close();
        return allItems;

    }


    // grab user Items using username and return the arraylist, gets called from My items fragment (gallery fragment)
    public ArrayList<Item> findUserItems(String username){
        String query="SELECT * FROM "+TABLE_ACCOUNT_HAS_ITEMS+" WHERE "+COLUMN_USERNAME+" = '"+username+"'";

        ArrayList<Item> userItems=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                Item item=new Item();
                item.setName(cursor.getString(1));
                userItems.add(item);
            }while(cursor.moveToNext());
            cursor.close();
        }
        sqLiteDatabase.close();

        return userItems;
    }


    //grab user items and amounts
    // grab user Items using username and return the arraylist, gets called from My items fragment (gallery fragment)
    public Map<String, String> findUserData(String username){


        String query="SELECT * FROM "+TABLE_ACCOUNT_HAS_ITEMS+" WHERE "+COLUMN_USERNAME+" = '"+username+"'";


        Map<String,String> userData=new HashMap<>();
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{

                userData.put(cursor.getString(1),cursor.getString(2));

            }while(cursor.moveToNext());
            cursor.close();
        }
        sqLiteDatabase.close();

        return userData;
    }



    //Helper function for finding items
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
    //helper for items
    public Item queryItemDB(String query){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        Item item=new Item();
        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            item.setName(cursor.getString(0));
            cursor.close();
        }else{
            item=null;
        }
        sqLiteDatabase.close();
        return item;
    }
    //deletes row and inserts new row with new amount
//    public boolean updateRow(String username,String itemName,int amount){
//        boolean result=false;
//        String query="SELECT * FROM "+TABLE_ACCOUNT_HAS_ITEMS+" WHERE "+COLUMN_USERNAME+" AND "+COLUMN_AMOUNT+" = '"+username+"'";
//        if (account != null) {
//            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//            result=sqLiteDatabase.delete(TABLE_ACCOUNTS, COLUMN_USERNAME + " =?",new String[]{username})>0;
//            sqLiteDatabase.close();
//        }
//        return result;
//    }





}
