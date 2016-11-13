package com.master.agendacontentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;



/**
 * Created by Hector on 13/11/2016.
 */

public class ContactosProvider extends ContentProvider {

    public static final String TABLE_NAME = "Contactos";

    public static final	class Contacto	implements BaseColumns
    {		private Contacto()	{}
        //Nombres	de	columnas
        public static final String COLUMN_NAME_FIRST_NAME = "nombre";
        public static final String COLUMN_NAME_LAST_NAME = "apellidos";
        public static final String COLUMN_NAME_EMAIL = "correo";
        public static final String COLUMN_NAME_ADDRESS = "direccion";
        public static final String COLUMN_NAME_COLOR = "color";
    }

    //Definición del CONTENT_URI
    public static final	String AUTHORITY ="com.master.agendacontentprovider";
    private static final String uri =
            "content://"+AUTHORITY+"/"+TABLE_NAME;

    public static final Uri CONTENT_URI = Uri.parse(uri);

    private ContactosSQLiteHelper contdbh;
    private static final String DB_NOMBRE = "DBContactos";
    private static final int DB_VERSION = 1;

    //UriMatcher
    private static final int CONTACTOS = 1;
    private static final int CONTACTOS_ID = 2;
    private static final UriMatcher uriMatcher;

    public final	static String SINGLE_MIME	=
            "vnd.android.cursor.item/vnd.android.contacto";
    public final	static String MULTIPLE_MIME	=
            "vnd.android.cursor.dir/vnd.android.contacto";

    //Inicializamos el UriMatcher
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "contactos", CONTACTOS);
        uriMatcher.addURI(AUTHORITY, "contactos/#", CONTACTOS_ID);
    }


    @Override
    public boolean onCreate() {
        contdbh = new ContactosSQLiteHelper(
                getContext(), DB_NOMBRE, null, DB_VERSION);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] columns, String selection, String[] selectionArgs, String sortOrder) {

        //Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;
        if (uriMatcher.match(uri) == CONTACTOS_ID) {
            where = "_id=" + uri.getLastPathSegment();
        }

        SQLiteDatabase db = contdbh.getWritableDatabase();

        Cursor c = db.query(TABLE_NAME, columns, where,
                selectionArgs, null, null, sortOrder);

        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match)
        {
            case CONTACTOS:
                return SINGLE_MIME;
            case CONTACTOS_ID:
                return MULTIPLE_MIME;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long regId = 1;

        SQLiteDatabase db = contdbh.getWritableDatabase();

        regId = db.insert(TABLE_NAME, null, values);

        Uri newUri = ContentUris.withAppendedId(CONTENT_URI, regId);

        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int cont;

        //Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;
        if (uriMatcher.match(uri) == CONTACTOS_ID) {
            where = "_id=" + uri.getLastPathSegment();
        }

        SQLiteDatabase db = contdbh.getWritableDatabase();

        cont = db.delete(TABLE_NAME, where, selectionArgs);

        return cont;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int cont;

        //Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;
        if (uriMatcher.match(uri) == CONTACTOS_ID) {
            where = "_id=" + uri.getLastPathSegment();
        }

        SQLiteDatabase db = contdbh.getWritableDatabase();

        cont = db.update(TABLE_NAME, values, where, selectionArgs);

        return cont;
    }
}
