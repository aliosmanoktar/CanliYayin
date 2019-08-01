package net.catsbilisim.canliyayin.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class veritabani extends SQLiteOpenHelper {

    private final String TAG=getClass().getName();
    private static final int DataBaseVersion=1;
    public static final String DatabaseName="canliyayin.db";
    private final String YoutubeTable="Youtube";
    private final String InstagramTable="Instagram";
    private final String PeriscopeTable="Periscope";
    private final String TokenType="TokenType";
    private final String AccesToken="AccesToken";
    private final String UserID="UserID";
    private final String UserName="UserName";
    private final String AccountName="AccountName";
    private final String ID="ID";
    private Context _context;
    private SQLiteDatabase _database;

    public veritabani( Context context) {
        super(context,DatabaseName,null,DataBaseVersion);
        _context=context;
        OpenDataBase();
    }

    private void OpenDataBase(){
        String path=_context.getDatabasePath(DatabaseName).getPath();
        if(_database!=null && _database.isOpen())
            return;
        _database=SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
    }

    private void CloseDataBase(){
        if(_database!=null)
            _database.close();
    }

    public List<Object> getAll(){
        List<Object> all=new ArrayList<>();
        all.addAll(getInstagram());
        all.addAll(getPeriscope());
        all.addAll(getYoutube());
        return all;
    }

    public List<InstagramUser> getInstagram(){
        boolean close =(_database==null || !_database.isOpen());
        if (close)
            OpenDataBase();
        List<InstagramUser> users = new ArrayList<>();
        Cursor c= _database.rawQuery("Select * from "+InstagramTable,null);
        while (c.moveToNext()){
            String name=c.getString(c.getColumnIndex(UserName));
            long Userid=c.getLong(c.getColumnIndex(UserID));
            int id = c.getInt(c.getColumnIndex(ID));
            users.add(new InstagramUser()
                    .setName(name)
                    .setUserID(Userid)
                    .setID(id));
        }
        if (close)
            CloseDataBase();
        return users;
    }

    public List<PeriscopeUser> getPeriscope(){
        boolean close =!_database.isOpen();
        if (close)
            OpenDataBase();
        List<PeriscopeUser> users = new ArrayList<>();
        Cursor c = _database.rawQuery("Select * from "+PeriscopeTable,null);
        while (c.moveToNext()){
            String tokenType = c.getString(c.getColumnIndex(TokenType));
            String accesToken = c.getString(c.getColumnIndex(AccesToken));
            String userName=c.getString(c.getColumnIndex(UserName));
            int id = c.getInt(c.getColumnIndex(ID));
            users.add(new PeriscopeUser()
                    .setTokenType(tokenType)
                    .setAccesToken(accesToken)
                    .setUserName(userName)
                    .setID(id));
        }
        if (close)
            CloseDataBase();
        return users;
    }

    public List<YoutubeUser> getYoutube(){
        boolean close =!_database.isOpen();
        if (close)
            OpenDataBase();
        List<YoutubeUser> users = new ArrayList<>();
        Cursor c= _database.rawQuery("Select * from "+YoutubeTable,null);
        while (c.moveToNext()){
            String accountname=c.getString(c.getColumnIndex(AccountName));
            int id = c.getInt(c.getColumnIndex(ID));
            users.add(new YoutubeUser()
                    .setName(accountname)
                    .setID(id));
        }
        if (close)
            CloseDataBase();
        return users;
    }

    public boolean AddYoutubeUser(YoutubeUser user){
        boolean close =!_database.isOpen();
        if (close)
            OpenDataBase();
        if (GetYoutubeUser(user.getName())!=null)
            return false;
        ContentValues values = new ContentValues();
        values.put(AccountName,user.getName());
        _database.insert(YoutubeTable,null,values);
        if (close)
            CloseDataBase();
        return true;
    }

    public boolean AddInstagramUser(InstagramUser user){
        boolean close =!_database.isOpen();
        if (close)
            OpenDataBase();
        if (GetInstagramUser(user.getUserID())!=null)
            return false;
        ContentValues values = new ContentValues();
        values.put(UserName,user.getName());
        values.put(UserID,user.getUserID());
        _database.insert(InstagramTable,null,values);
        if (close)
            CloseDataBase();
        return true;
    }

    public boolean AddPeriscopeUser(PeriscopeUser user){
        boolean close =!_database.isOpen();
        if (close)
            OpenDataBase();
        if (GetPeriscopeUser(user.getUserName())!=null)
            return false;
        ContentValues values = new ContentValues();
        values.put(TokenType,user.getTokenType());
        values.put(AccesToken,user.getAccesToken());
        values.put(UserName,user.getUserName());
        _database.insert(PeriscopeTable,null,values);
        if (close)
            CloseDataBase();
        return true;
    }

    public void DeletePeriscopeUser(int id){
        boolean close =!_database.isOpen();
        if (close)
            OpenDataBase();
        String where=ID+"="+id;
        _database.delete(PeriscopeTable,where,null);
        if (close)
            CloseDataBase();
    }

    public void DeleteInstagramUser(int id){
        boolean close =!_database.isOpen();
        if (close)
            OpenDataBase();
        String where=ID+"="+id;
        _database.delete(InstagramTable,where,null);
        if (close)
            CloseDataBase();
    }

    public void DeleteYoutubeUser(int id){
        boolean close =!_database.isOpen();
        if (close)
            OpenDataBase();
        String where=ID+"="+id;
        _database.delete(YoutubeTable,where,null);
        if (close)
            CloseDataBase();
    }

    public InstagramUser GetInstagramUser(long user_id){
        String sorgu=String.format("Select * from %s where %s=%s",InstagramTable,UserID,user_id);
        Log.e(TAG, "GetInstagramUser: "+sorgu);
        InstagramUser user;
        boolean close =!_database.isOpen();
        if (close)
            OpenDataBase();
        Cursor c=_database.rawQuery(sorgu,null);
        if (c.moveToNext()){
            String name=c.getString(c.getColumnIndex(UserName));
            long Userid=c.getLong(c.getColumnIndex(UserID));
            int id = c.getInt(c.getColumnIndex(ID));
            user=(new InstagramUser()
                    .setName(name)
                    .setUserID(Userid)
                    .setID(id));
        }
        else
            user=null;
        if (close)
            CloseDataBase();
        return user;
    }

    public YoutubeUser GetYoutubeUser(String account_name){
        String sorgu=String.format("Select * from %s where %s=\"%s\"",YoutubeTable,AccountName,account_name);
        Log.e(TAG, "GetYoutubeUser: "+sorgu );
        YoutubeUser user;
        boolean close =!_database.isOpen();
        if (close)
            OpenDataBase();
        Cursor c= _database.rawQuery(sorgu,null);
        if (c.moveToNext()){
            String accountname=c.getString(c.getColumnIndex(AccountName));
            int id = c.getInt(c.getColumnIndex(ID));
            user=(new YoutubeUser()
                    .setName(accountname)
                    .setID(id));
        }
        else user=null;
        if (close)
            CloseDataBase();
        return user;
    }

    public PeriscopeUser GetPeriscopeUser(String name){
        String sorgu=String.format("Select * from %s where %s=\"%s\"",PeriscopeTable,UserName,name);
        Log.e(TAG, "GetPeriscopeUser: "+sorgu);
        PeriscopeUser user;
        boolean close =!_database.isOpen();
        if (close)
            OpenDataBase();
        Cursor c= _database.rawQuery(sorgu,null);
        if (c.moveToNext()){
            String tokenType = c.getString(c.getColumnIndex(TokenType));
            String accesToken = c.getString(c.getColumnIndex(AccesToken));
            String userName=c.getString(c.getColumnIndex(UserName));
            int id = c.getInt(c.getColumnIndex(ID));
            user=(new PeriscopeUser()
                    .setTokenType(tokenType)
                    .setAccesToken(accesToken)
                    .setUserName(userName)
                    .setID(id));
        }
        else user=null;
        if (close)
            CloseDataBase();
        return user;
    }

    public void DeleteAll(){
        OpenDataBase();
        _database.delete(InstagramTable,null,null);
        _database.delete(YoutubeTable,null,null);
        _database.delete(PeriscopeTable,null,null);
        CloseDataBase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}