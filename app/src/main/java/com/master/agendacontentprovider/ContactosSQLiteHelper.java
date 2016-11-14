package com.master.agendacontentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import static com.master.agendacontentprovider.ContactosProvider.Contacto;

import static com.master.agendacontentprovider.ContactosProvider.TABLE_NAME;
import static com.master.agendacontentprovider.utils.Utils.getMatColor;

/**
 * Created by Hector on 12/11/2016.
 */

public class ContactosSQLiteHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String SQL_CREATE_CONTACTS =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    ContactosProvider.Contacto._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Contacto.COLUMN_NAME_FIRST_NAME + TEXT_TYPE + "," +
                    Contacto.COLUMN_NAME_LAST_NAME + TEXT_TYPE + "," +
                    Contacto.COLUMN_NAME_EMAIL + TEXT_TYPE + "," +
                    Contacto.COLUMN_NAME_ADDRESS + TEXT_TYPE + "," +
                    Contacto.COLUMN_NAME_COLOR + INT_TYPE + " )";
    private static final String SQL_DELETE_CONTACTS =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public ContactosSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CONTACTS);
        for(int i=1; i<=15; i++)
        {
            //Generamos los datos de muestra
            String nombre = "Contacto" + i;
            String apellidos = "Contacto Apellido" + i;
            String telefono = "900-123-00" + i;
            String email = "email" + i + "@mail.com";
            String direccion = "C/ Acacia "+ i;
            int color = getMatColor("500", );

            //Insertamos los datos en la tabla Clientes
            db.execSQL("INSERT INTO Contactos ("+Contacto.COLUMN_NAME_FIRST_NAME+"," +Contacto.COLUMN_NAME_LAST_NAME + "," + Contacto.COLUMN_NAME_EMAIL + ","+ Contacto.COLUMN_NAME_ADDRESS + ","+ Contacto.COLUMN_NAME_COLOR+") " +
                    "VALUES ('" + nombre + "', '" + apellidos +"', '" + email + "', '" + direccion +"', '"+ color+ "')");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_CONTACTS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
