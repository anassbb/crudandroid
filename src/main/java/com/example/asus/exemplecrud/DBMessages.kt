package com.example.asus.exemplecrud

/**
 * Created by asus on 25/10/2018.
 */


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

val Context.dbMessages: DBMessages
    get() = DBMessages.getInstance(applicationContext)

class DBMessages : ManagedSQLiteOpenHelper(ctx, DATABASE_NAME, null, DATABASE_VERSION){

    companion object {
        const val DATABASE_NAME = "messages.db"
        const val DATABASE_VERSION = 1

        const val TABLE_MESSAGES = "messages"
        const val COLUMN_MESSAGES_ID = "id"
        const val COLUMN_MESSAGES_EXPEDITEUR = "exped"
        const val COLUMN_MESSAGES_CONTENU = "contenu"
        const val COLUMN_MESSAGES_DATE = "date"

        private var instance: DBMessages? = null

        @Synchronized
        fun getInstance(ctx: Context): DBMessages {
            if (instance == null) {
                instance = DBMessages(ctx)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(TABLE_MESSAGES, true,
                COLUMN_MESSAGES_ID to INTEGER + PRIMARY_KEY,
                COLUMN_MESSAGES_EXPEDITEUR to TEXT + NOT_NULL,
                COLUMN_MESSAGES_CONTENU to TEXT + NOT_NULL,
                COLUMN_MESSAGES_DATE to INTEGER + NOT_NULL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Pas de mises à jour pour l'instant. Mise à jour suivante :
        /*
        if(oldVersion < 2) {
            // Mise à jour de la v1 à la v2
        }
        */
    }
}
