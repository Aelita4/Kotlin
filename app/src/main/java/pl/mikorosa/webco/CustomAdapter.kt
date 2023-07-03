package pl.mikorosa.webco

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import okhttp3.FormBody
import kotlin.concurrent.fixedRateTimer


class CustomAdapter(private val mList: List<GalaxyViewModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val GalaxyViewModel = mList[position]
        val userId = GalaxyViewModel.userId;

        holder.id.text = GalaxyViewModel.id.toString()
        holder.cardPlanetName.text = GalaxyViewModel.planetName
        holder.cardOwnerUsername.text = GalaxyViewModel.username

        holder.buttonSpy.setOnClickListener {
            val response = APIManager.spy(userId)
            val resources = response.get("resources").asJsonObject

            val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(GalaxyViewModel.context)

            val builder: AlertDialog.Builder = alertDialogBuilder
                .setTitle("Spy report")
                .setMessage("Coal: " + resources.get("coal").asInt.toString() + "\nCopper: " + resources.get("copper").asInt.toString() + "\nGold: " + resources.get("gold").asInt.toString() + "\nIron: " + resources.get("iron").asInt.toString() + "\nOil: " + resources.get("oil").asInt.toString() + "\nUranium: " + resources.get("uranium").asInt.toString() + "\nWood: " + resources.get("wood").asInt.toString())
                .setPositiveButton("OK", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id: Int) {
                        run {}
                    }
                })


            val alertDialog: AlertDialog = alertDialogBuilder.create()
            alertDialog.setCancelable(true)
            alertDialog.setCanceledOnTouchOutside(false)

            alertDialog.show()
        }

        holder.buttonAttack.setOnClickListener {
            val reqBody = FormBody.Builder().build()

            APIManager.fetch("https://col.ael.ovh/api/attackPlayer/" + userId, "POST", GalaxyViewModel.loggedInId, reqBody)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val id: TextView = itemView.findViewById(R.id.cardId)
        val cardPlanetName: TextView = itemView.findViewById(R.id.cardPlanetName)
        val cardOwnerUsername: TextView = itemView.findViewById(R.id.cardOwnerUsername)
        val buttonSpy: Button = itemView.findViewById(R.id.buttonSpy)
        val buttonAttack: Button = itemView.findViewById(R.id.buttonAttack)
    }
}
