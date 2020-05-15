package com.example.mob_ingilizce_hatirlatici;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import com.example.mob_ingilizce_hatirlatici.kelimeContract.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class testDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mob_ingilizce_hatirlatici.db";
    private static final int DATABASE_VERSION = 1; //Telefon hafızasına kaydeder

    private static testDbHelper instance;

    private SQLiteDatabase db;

    private testDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static  synchronized testDbHelper getInstance(Context context){
        if (instance == null) {
            instance = new testDbHelper(context.getApplicationContext());
        }
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db=db;

        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                CategoriesTable.TABLE_NAME + "( " +
                CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoriesTable.COLUMN_NAME + " TEXT " +
                ")";

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                kelimeTable.TABLE_NAME + " ( " +
                kelimeTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                kelimeTable.COLUMN_QUESTION + " TEXT, " +
                kelimeTable.COLUMN_OPTION1 + " TEXT, " +
                kelimeTable.COLUMN_OPTION2 + " TEXT, " +
                kelimeTable.COLUMN_OPTION3 + " TEXT, " +
                kelimeTable.COLUMN_ANSWER_NR + " INTEGER, " +
                kelimeTable.COLUMN_DIFFICULTY + " TEXT, " +
                kelimeTable.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + kelimeTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                CategoriesTable.TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +
                ")";

        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillKategoriTable();
        fillkelimeTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ kelimeTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void fillKategoriTable() {

        kategori c1=new kategori("Kelime");
        insertKategori(c1);
        kategori c2 = new kategori("Boşluk Doldurma");
        insertKategori(c2);
        kategori c3 = new kategori("Cümle");
        insertKategori(c3);

    }
    public void addCategory(kategori kategori){
        db=getWritableDatabase();
        insertKategori(kategori);
    }
public void addCategories(List<kategori> kategoris){
        db=getWritableDatabase();
        for(kategori kategori:kategoris){
           insertKategori(kategori);
        }
}

    private void insertKategori(kategori kategori){
        ContentValues cv=new ContentValues();
        cv.put(CategoriesTable.COLUMN_NAME, kategori.getName());
        db.insert(CategoriesTable.TABLE_NAME,null,cv);
    }

    private void fillkelimeTable() {


      /* kelimeler q1 = new kelimeler("Kelime, Kolay: Red", "Kırmızı", "Sarı", "Ev",
                1,kelimeler.DIFFICULTY_EASY, kategori.KELİME); ///veritabınımıza verilerilerimizi ve kelimelerimizi ekliyoruz
        insertKelime(q1);
        kelimeler q4 = new kelimeler("Kelime, Kolay: Home", "Kırmızı", "Sarı", "Ev",
                3,kelimeler.DIFFICULTY_EASY, kategori.KELİME);
        insertKelime(q4);
        kelimeler q5 = new kelimeler("Kelime, Kolay: Red", "Kırmızı", "Sarı", "Ev",
                1,kelimeler.DIFFICULTY_EASY, kategori.KELİME);
        insertKelime(q5);
        kelimeler q2 = new kelimeler("Boşluk Doldurma, Orta: A is correct again", "A", "B", "C",
                1,kelimeler.DIFFICULTY_MEDIUM, kategori.KELİME2);
        insertKelime(q2);
        kelimeler q3 = new kelimeler("Cümle, Zor: A is correct again", "A", "B", "C",
                1,kelimeler.DIFFICULTY_HARD, kategori.CUMLE);
        insertKelime(q3);*/

    }
    public void addKelime(kelimeler kelimeler){
        db=getWritableDatabase();
        insertKelime(kelimeler);
    }
    public void addKelimeler(List<kelimeler> kelimelers){
        db=getWritableDatabase();
        for(kelimeler kelimeler:kelimelers){
            insertKelime(kelimeler);
        }
    }

    private void insertKelime(kelimeler kelimeler) {

        ContentValues cv= new ContentValues();
        cv.put(kelimeTable.COLUMN_QUESTION,kelimeler.getQuestion());
        cv.put(kelimeTable.COLUMN_OPTION1, kelimeler.getOption1());
        cv.put(kelimeTable.COLUMN_OPTION2, kelimeler.getOption2());
        cv.put(kelimeTable.COLUMN_OPTION3, kelimeler.getOption3());
        cv.put(kelimeTable.COLUMN_ANSWER_NR, kelimeler.getAnswerNr());
        cv.put(kelimeTable.COLUMN_DIFFICULTY, kelimeler.getDifficulty());
        cv.put(kelimeTable.COLUMN_CATEGORY_ID,kelimeler.getCategoryID());
        db.insert(kelimeTable.TABLE_NAME, null, cv);

    }
    public List<kategori> getAllCategories(){
        List<kategori> kategoriList=new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CategoriesTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                kategori category = new kategori();
                category.setId(c.getInt(c.getColumnIndex(CategoriesTable._ID)));
                category.setName(c.getString(c.getColumnIndex(CategoriesTable.COLUMN_NAME)));
                kategoriList.add(category);
            } while (c.moveToNext());
        }

        c.close();
        return kategoriList;
    }

    public ArrayList<kelimeler> getAllQuestions() {
        ArrayList<kelimeler> questionList = new ArrayList<>();
        db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + kelimeTable.TABLE_NAME, null);

        if(c.moveToFirst()){

            do {
                kelimeler question = new kelimeler();
                question.setId(c.getInt(c.getColumnIndex(kelimeTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(kelimeTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(kelimeTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(kelimeTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(kelimeTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(kelimeTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(kelimeTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(kelimeTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }

    public ArrayList<kelimeler> getQuestions(int categoryID,String difficulty) {
        ArrayList<kelimeler> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String selection = kelimeTable.COLUMN_CATEGORY_ID + " = ? " +
                " AND " + kelimeTable.COLUMN_DIFFICULTY + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(categoryID), difficulty};

        Cursor c = db.query(
                kelimeTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

       /* String[] selectionArgs=new String[]{difficulty};
        Cursor c = db.rawQuery("SELECT * FROM " + kelimeTable.TABLE_NAME +
                " WHERE " + kelimeTable.COLUMN_DIFFICULTY + " = ?",selectionArgs);*/

        if(c.moveToFirst()){
            do {
                kelimeler question = new kelimeler();
                question.setId(c.getInt(c.getColumnIndex(kelimeTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(kelimeTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(kelimeTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(kelimeTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(kelimeTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(kelimeTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(kelimeTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(kelimeTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }
        c.close();
        return questionList; //kelimelerden question oluşturmus onuda questionliste eklemiş
    }


}
