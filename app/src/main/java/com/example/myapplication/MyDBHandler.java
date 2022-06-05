package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
    public static final String COLUMN_IMEROMINIA="imerominia";

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


        //ACCOUNT_HAS_ITEMS(USERNAME,NAME,AMOUNT) NAME IS ITEMNAME+USERNAME+DATE AS COMPOSITE PRIMARY KEY

        String CREATE_ACCOUNT_HAS_ITEMS_TABLE="CREATE TABLE IF NOT EXISTS "+
                TABLE_ACCOUNT_HAS_ITEMS+"("+
                COLUMN_USERNAME+" TEXT,"+
                COLUMN_NAME+" TEXT,"+
                COLUMN_AMOUNT+" TEXT,"+
                COLUMN_IMEROMINIA+" TEXT, PRIMARY KEY ("+COLUMN_USERNAME+", "+COLUMN_NAME+", "+COLUMN_IMEROMINIA+"))";

        //execute the queries
        sqLiteDatabase.execSQL(CREATE_ACCOUNTS_TABLE);
        sqLiteDatabase.execSQL(CREATE_ITEMS_TABLE);
        sqLiteDatabase.execSQL(CREATE_ACCOUNT_HAS_ITEMS_TABLE);


        System.out.println("Tables created");


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


    //same for item but first we check if the item exists in the database already
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

    //add an item with its amount linked to a specific account and date as a log
    public void addItemToAccount(String username,Item item,int amount,String date){
        ContentValues values=new ContentValues();
        values.put(COLUMN_USERNAME,username);
        values.put(COLUMN_NAME,item.getName());
        values.put(COLUMN_IMEROMINIA,date);
        values.put(COLUMN_AMOUNT,amount);
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.insert(TABLE_ACCOUNT_HAS_ITEMS,null,values);
        sqLiteDatabase.close();

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


    //find all items in the database
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





    //grab user items  using username and return the arraylist
    //every item might be present many times in that table, so we first check if it has already been added and if not
    //we add up all the amounts corresponding to that item and add it to the arraylist
    public ArrayList<EnchancedItem> findUserData(String username){


        String query="SELECT * FROM "+TABLE_ACCOUNT_HAS_ITEMS+" WHERE "+COLUMN_USERNAME+" = '"+username+"'";

        ArrayList<EnchancedItem> userItems=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();

        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        if(cursor.moveToFirst()){
            boolean exists=false;
            do{
                for(EnchancedItem item:userItems){
                    if(item.getName().equals(cursor.getString(1))){
                        exists=true;
                    }
                }
                if(!exists){
                    userItems.add(new EnchancedItem(cursor.getString(1),getTotalValue(sqLiteDatabase.rawQuery(query,null),cursor.getString(1))));
                }
                exists=false;
            }while(cursor.moveToNext());
            cursor.close();
        }
        sqLiteDatabase.close();

        return userItems;
    }

    //about the same as previous, but here we also filter the date an item was added on
    //depending on user option in home fragment. Instead of arraylist, we use a hashmap to store amounts together with the names
    public Map<String, String> findUserDataForChart(String username,String usecase){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String currentDate=dateFormat.format(new Date());
        switch (usecase){
            case "total":
                usecase="%";
                break;
            case "annual":
                usecase="date"+(new SimpleDateFormat("yyyy", Locale.getDefault())).format(new Date())+"%";
                break;
            case "month":
                usecase="date"+(new SimpleDateFormat("yyyyMM", Locale.getDefault())).format(new Date())+"%";
                break;
            case "day":
                usecase="date"+(new SimpleDateFormat("yyyyMMdd", Locale.getDefault())).format(new Date())+"%";
                break;

        }
        String query="SELECT * FROM "+TABLE_ACCOUNT_HAS_ITEMS+" WHERE ("+COLUMN_USERNAME+" = '"+username+"' AND "+COLUMN_IMEROMINIA+" LIKE '"+usecase+"')";
        Map<String,String> userData=new HashMap<>();
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);


        if(cursor.moveToFirst()){
            boolean exists=false;
            do{

                for(Map.Entry<String,String> item:userData.entrySet()){
                    if(item.getKey().equals(cursor.getString(1))){
                        exists=true;
                    }
                }
                if(!exists){
                    userData.put(cursor.getString(1),Double.toString(getTotalValue(sqLiteDatabase.rawQuery(query,null),cursor.getString(1))));
                }
                exists=false;
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


    //deletes all logs of this item for a particular user
    public boolean deleteItemFromUser(String username,String itemName){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(TABLE_ACCOUNT_HAS_ITEMS,COLUMN_USERNAME+"= '"+username+"' AND "+COLUMN_NAME+"= '"+itemName+"'",null)>0;

    }

    //helper function for calculating total value of different amount logs
    public double getTotalValue(Cursor cursor,String itemName){
        double totalValue=0;
        if(cursor.moveToFirst()){
            do{
                if(cursor.getString(1).equals(itemName)){
                    totalValue+=Double.parseDouble(cursor.getString(2));
                }
            }while(cursor.moveToNext());
        }

        return totalValue;

    }





}
