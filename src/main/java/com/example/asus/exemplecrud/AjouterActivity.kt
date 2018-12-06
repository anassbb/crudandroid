package com.example.asus.exemplecrud

/**
 * Created by asus on 25/10/2018.
 */

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_ajouter.*
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import java.util.*

// Le code de cette activity provient pour beaucoup de l'ancien code de MainActivity
class AjouterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajouter)

        btn_send.setOnClickListener {
            startActivityForResult<ConfirmationActivity>(42, ConfirmationActivity.EXTRA_MESSAGE to txt_message.text.toString())
        }
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            42 -> {
                if (resultCode == RESULT_OK) {
                    val r = data?.getIntExtra(ConfirmationActivity.EXTRA_ISCONFIRMED, ConfirmationActivity.VAL_CANCEL) ?: ConfirmationActivity.VAL_CANCEL
                    when(r){
                        ConfirmationActivity.VAL_CONFIRMED -> {
                            dbMessages.use {
                                insert(DBMessages.TABLE_MESSAGES,
                                        DBMessages.COLUMN_MESSAGES_CONTENU to txt_message.text.toString(),
                                        DBMessages.COLUMN_MESSAGES_EXPEDITEUR to txt_expediteur.text.toString(),
                                        DBMessages.COLUMN_MESSAGES_DATE to Date(txt_date.text.toString()).time)
                            }
                            toast(R.string.message_ajoute)
                            // Et on ferme l'écran de saisie et on revient à MainActivity
                            // Ca va aussi effacer tous les champs
                            finish()
                        }
                        ConfirmationActivity.VAL_EDIT -> { } // rien de spécial à faire
                        ConfirmationActivity.VAL_CANCEL -> {
                            // On ferme l'écran de saisie et on revient à MainActivity
                            // Ca va aussi effacer tous les champs
                            finish()
                        }
                    }
                } else if(resultCode == RESULT_CANCELED) {
                    // on va considérer que le bouton "retour" du téléphone est équivalent à notre bouton "modifier"
                    // comme ci-dessus, rien de spécial à faire du coup
                }
            }
        }
    }

}
