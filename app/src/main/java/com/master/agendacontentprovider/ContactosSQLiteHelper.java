package com.master.agendacontentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.master.agendacontentprovider.ContactContract.ContactEntry;
import com.master.agendacontentprovider.PhoneContract.PhoneEntry;
import static com.master.agendacontentprovider.utils.Utils.getMatColor;

/**
 * Created by Hector on 12/11/2016.
 */

public class ContactosSQLiteHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String SQL_CREATE_CONTACTS =
            "CREATE TABLE " + ContactEntry.TABLE_NAME + " (" +
                    ContactEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ContactEntry.COLUMN_NAME_FIRST_NAME + TEXT_TYPE + "," +
                    ContactEntry.COLUMN_NAME_LAST_NAME + TEXT_TYPE + "," +
                    ContactEntry.COLUMN_NAME_PHONE + INT_TYPE + "," +
                    " FOREIGN KEY("+ContactEntry.COLUMN_NAME_PHONE+") REFERENCES "+PhoneEntry.TABLE_NAME+"("+PhoneEntry._ID+")," +
                    ContactEntry.COLUMN_NAME_EMAIL + TEXT_TYPE + "," +
                    ContactEntry.COLUMN_NAME_ADDRESS + TEXT_TYPE + "," +
                    ContactEntry.COLUMN_NAME_COLOR + INT_TYPE + " )";

    private static final String SQL_CREATE_PHONES =
            "CREATE TABLE " + ContactEntry.TABLE_NAME + " (" +
                    PhoneEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    PhoneEntry.COLUMN_NAME_NUMBER + TEXT_TYPE + "," +
                    PhoneEntry.COLUMN_NAME_TYPE + INT_TYPE + " )";

    private static final String SQL_DELETE_CONTACTS =
            "DROP TABLE IF EXISTS " + ContactEntry.TABLE_NAME;
    private Context context;

    public ContactosSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CONTACTS);
        db.execSQL(SQL_CREATE_PHONES);
        for(int i=1; i<=15; i++)
        {
            //Generamos los datos de muestra
            String numero = "92143450" + i;
            int tipo = 1;

            //Insertamos los datos en la tabla Clientes
            db.execSQL("INSERT INTO Telefonos ("+PhoneEntry.COLUMN_NAME_NUMBER+"," +PhoneEntry.COLUMN_NAME_TYPE+") " +
                    "VALUES ('" + numero + "', '" + tipo +"'");
        }
        for(int i=1; i<=15; i++)
        {
            //Generamos los datos de muestra
            String nombre = "Contacto" + i;
            String apellidos = "Contacto Apellido" + i;
            String telefono = "900-123-00" + i;
            String email = "email" + i + "@mail.com";
            String direccion = "C/ Acacia "+ i;
            int color = getMatColor("500", context);
            Log.d("Perr", String.valueOf(color));
            //Insertamos los datos en la tabla Clientes
            db.execSQL("INSERT INTO Contactos ("+ContactEntry.COLUMN_NAME_FIRST_NAME+"," +ContactEntry.COLUMN_NAME_LAST_NAME + "," + ContactEntry.COLUMN_NAME_PHONE + ","+ ContactEntry.COLUMN_NAME_EMAIL + ","+ ContactEntry.COLUMN_NAME_ADDRESS + ","+ ContactEntry.COLUMN_NAME_COLOR+") " +
                    "VALUES ('" + nombre + "', '" + apellidos +"', '" + email + "', '"+ email + "', '" + direccion +"', '"+ color+ "')");
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
