package com.master.agendacontentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.master.agendacontentprovider.ContactContract.ContactEntry;


/**
 * Created by Hector on 13/11/2016.
 */

public class ContactosProvider extends ContentProvider {

    //Definición del CONTENT_URI
    public static final String AUTHORITY = "com.master.agendacontentprovider";
    private static final String uriContact =
            "content://" + AUTHORITY + "/" + ContactEntry.TABLE_NAME;

    public static final Uri CONTENT_CONTACT_URI = Uri.parse(uriContact);
    private static final String phoneContact =
            "content://" + AUTHORITY + "/" + ContactEntry.TABLE_NAME;

    public static final Uri CONTENT_PHONE_URI = Uri.parse(phoneContact);

    private ContactosSQLiteHelper contdbh;
    private static final String DB_NOMBRE = "DBContactos";
    private static final int DB_VERSION = 1;

    //UriMatcher
    private static final int CONTACTOS = 1;
    private static final int CONTACTOS_ID = 2;
    private static final int TELEFONOS = 3;
    private static final int TELEFONOS_ID = 4;
    private static final UriMatcher uriMatcher;

    public final static String SINGLE_MIME_CONTACT =
            "vnd.android.cursor.item/vnd.android.contacto";
    public final static String MULTIPLE_MIME_CONTACT =
            "vnd.android.cursor.dir/vnd.android.contacto";
    public final static String SINGLE_MIME_PHONE =
            "vnd.android.cursor.item/vnd.android.telefono";
    public final static String MULTIPLE_MIME_PHONE =
            "vnd.android.cursor.dir/vnd.android.telefono";

    //Inicializamos el UriMatcher
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "contactos", CONTACTOS);
        uriMatcher.addURI(AUTHORITY, "contactos/#", CONTACTOS_ID);
        uriMatcher.addURI(AUTHORITY, "telefonos/", TELEFONOS);
        uriMatcher.addURI(AUTHORITY, "telefonos/#", TELEFONOS_ID);
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
        SQLiteDatabase db = contdbh.getWritableDatabase();

        if (selection != null) {
            selection = "";
        }
        switch (uriMatcher.match(uri)) {
            case CONTACTOS:
                return db.query(ContactEntry.TABLE_NAME, columns, selection,
                        selectionArgs, null, null, sortOrder);

            case CONTACTOS_ID:
                return db.query(ContactEntry.TABLE_NAME, columns, selection+ContactEntry._ID+"=" + uri.getLastPathSegment(),
                        selectionArgs, null, null, sortOrder);
            case TELEFONOS:
                return db.query(PhoneContract.PhoneEntry.TABLE_NAME, columns, selection,
                        selectionArgs, null, null, sortOrder);

            case TELEFONOS_ID:
                return db.query(PhoneContract.PhoneEntry.TABLE_NAME, columns, selection+PhoneContract.PhoneEntry._ID +"=" + uri.getLastPathSegment(),
                        selectionArgs, null, null, sortOrder);

        }

        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match) {
            case CONTACTOS:
                return SINGLE_MIME_CONTACT;
            case CONTACTOS_ID:
                return MULTIPLE_MIME_CONTACT;
            case TELEFONOS:
                return SINGLE_MIME_PHONE;
            case TELEFONOS_ID:
                return MULTIPLE_MIME_PHONE;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long regId = 1;

        SQLiteDatabase db = contdbh.getWritableDatabase();

        switch (uriMatcher.match(uri)){
            case CONTACTOS:
                regId = db.insert(ContactEntry.TABLE_NAME, null, values);
                return ContentUris.withAppendedId(CONTENT_CONTACT_URI, regId);
            case TELEFONOS:
                regId = db.insert(PhoneContract.PhoneEntry.TABLE_NAME, null, values);
                return ContentUris.withAppendedId(CONTENT_PHONE_URI, regId);
        }

        return null;
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

        cont = db.delete(ContactEntry.TABLE_NAME, where, selectionArgs);

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

        cont = db.update(ContactEntry.TABLE_NAME, values, where, selectionArgs);

        return cont;
    }
}
