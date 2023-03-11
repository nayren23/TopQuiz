package BDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import model.User;

public class DatabaseUser extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "User_Manager";

    // Table name: User.
    private static final String TABLE_USER = "User";

    //On creer la structure de la table
    private static final String COLUMN_USER_ID ="User_Id";
    private static final String COLUMN_USER_FisrtName ="User_FisrtName";
    private static final String COLUMN_USER_ScoreJoueur = "User_ScoreJoueur";

    public DatabaseUser(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        // Script.
        String script = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY," + COLUMN_USER_FisrtName + " TEXT,"
                + COLUMN_USER_ScoreJoueur + " TEXT" + ")";
        // Execute Script.
        db.execSQL(script);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    // If User table has no data
    // default, Insert 2 records.
    public void createDefaultUsersIfNeed()  {
        int count = this.getUserCount();
        if(count ==0 ) {
            User user1 = new User("User1" , 2);
            User user2 = new User("User2" , 0);
        }
    }

    //On ajoute un User
    public void addUser(User user) {
        Log.i(TAG, "MyDatabaseHelper.addUser ... " + user.getFirstName()); // affiche un message dans la console android

        SQLiteDatabase db = this.getWritableDatabase();//ouvre une connexion à la base de données en mode écriture

        ContentValues values = new ContentValues(); //stocker des paires clé-valeur de données à insérer ou mettre à jour dans une base de données SQLite

        //on prepare les donnees suivantes
        values.put(COLUMN_USER_FisrtName, user.getFirstName());
        values.put(COLUMN_USER_ScoreJoueur, user.getScoreJoueur());

        // Inserting Row
        db.insert(TABLE_USER, null, values);

        // Closing database connection
        db.close();
    }

    public User getUser(int id) {
        Log.i(TAG, "MyDatabaseHelper.getUser ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, new String[] { COLUMN_USER_ID,
                        COLUMN_USER_FisrtName, COLUMN_USER_ScoreJoueur }, COLUMN_USER_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        User user = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2)));
        // return User
        return user;
    }

    public List<User> getAllUser() {
        Log.i(TAG, "MyDatabaseHelper.getAllUsers ... " );

        List<User> userList = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setUserId(Integer.parseInt(cursor.getString(0)));
                user.setFirstName(cursor.getString(1));
                user.setScoreJoueur(Integer.parseInt(cursor.getString(2)));
                // Adding user to list
                userList.add(user);
            } while (cursor.moveToNext());
        }

        // return user list
        return userList;
    }

    public int getUserCount() {
        Log.i(TAG, "MyDatabaseHelper.getUsersCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    public int updateUser(User user) {
        Log.i(TAG, "MyDatabaseHelper.updateUser ... "  + user.getFirstName());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_FisrtName, user.getFirstName());
        values.put(COLUMN_USER_ScoreJoueur, user.getScoreJoueur());

        // updating row
        return db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getUserId())});
    }

    public void deleteUser(User user) {
        Log.i(TAG, "MyDatabaseHelper.updateUser ... " + user.getFirstName());

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[] { String.valueOf(user.getUserId()) });
        db.close();
    }
}
