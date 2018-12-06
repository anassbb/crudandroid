
package com.example.asus.exemplecrud

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.asus.exemplecrud.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.db.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val STATE_CURRENT_MESSAGE = "MainActivity.currentMessage"
    }

    var currentMessage : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_send.setOnClickListener {
            startActivity<AjouterActivity>()
        }

        btn_prev.setOnClickListener {
            chargerMessage(currentMessage - 1)
        }

        btn_next.setOnClickListener {
            chargerMessage(currentMessage + 1)
        }

        // Au cas où il n'y est aucun message
        val am = getString(R.string.aucun_message)
        tv_pk.text = am
        tv_expediteur.text = am
        tv_date.text = am
        tv_message.text = am

        // On charge le premier message de la base de données, si il existe
        // En SQL, la première primary key est 1 (pas 0)
        chargerMessage(1)
    }

    fun chargerMessage(id: Long){
        dbMessages.use {
            select(DBMessages.TABLE_MESSAGES, // Table
                    DBMessages.COLUMN_MESSAGES_ID, // Toutes les colones
                    DBMessages.COLUMN_MESSAGES_EXPEDITEUR,
                    DBMessages.COLUMN_MESSAGES_DATE,
                    DBMessages.COLUMN_MESSAGES_CONTENU)
                    .whereArgs("${DBMessages.COLUMN_MESSAGES_ID} = {nextMsg}", // On va charger un message à la fois avec comme identifiant id
                            "nextMsg" to id)
                    .exec {
                        if(count == 0){
                            // Aucun message, on ne change pas l'affichage
                            toast(R.string.dernier_message)
                        }else {
                            // 1 message maximum retourné, parce que les id sont uniques (PK)
                            for (row in asMapSequence()) {
                                // On ne passe qu'une seule fois dans le for du coup
                                // On met à jour currentMessage
                                currentMessage = row[DBMessages.COLUMN_MESSAGES_ID] as Long
                                tv_pk.text = currentMessage.toString()
                                tv_expediteur.text = row[DBMessages.COLUMN_MESSAGES_EXPEDITEUR] as String
                                val date = Date()
                                date.time = row[DBMessages.COLUMN_MESSAGES_DATE] as Long
                                tv_date.text = date.toLocaleString()
                                tv_message.text = row[DBMessages.COLUMN_MESSAGES_CONTENU] as String
                            }
                        }
                    }
        }
    }

    // On oublie pas de mettre à jour ces deux fonctions :

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong(STATE_CURRENT_MESSAGE, currentMessage)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentMessage = savedInstanceState.getLong(STATE_CURRENT_MESSAGE)
        // On recharhe le message
        chargerMessage(currentMessage)
    }
}

