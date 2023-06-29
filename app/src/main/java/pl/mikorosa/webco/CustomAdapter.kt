package pl.mikorosa.webco

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val mList: List<GalaxyViewModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val GalaxyViewModel = mList[position]

        holder.id.text = GalaxyViewModel.id.toString()
        holder.cardPlanetName.text = GalaxyViewModel.planetName
        holder.cardOwnerUsername.text = GalaxyViewModel.username
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val id: TextView = itemView.findViewById(R.id.cardId)
        val cardPlanetName: TextView = itemView.findViewById(R.id.cardPlanetName)
        val cardOwnerUsername: TextView = itemView.findViewById(R.id.cardOwnerUsername)
    }
}
