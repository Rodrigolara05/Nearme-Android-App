package resasoftware.com.pe.nearme.controllers.fragments


import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_settings.*
import resasoftware.com.pe.nearme.R
import resasoftware.com.pe.nearme.controllers.activities.MainActivity
import resasoftware.com.pe.nearme.models.Category
import resasoftware.com.pe.nearme.models.Preferences
import resasoftware.com.pe.nearme.network.NearmeApi
import resasoftware.com.pe.nearme.network.Notifications


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SettingsFragment : Fragment() {

    var MAPStyle = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //preferences = Preferences.allPreferences()
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onResume() {
        super.onResume()
        AllPreferences.removeAllViews()
        loadSettings()

        var selects: ArrayList<CheckBox> = ArrayList<CheckBox>()
        slide_range.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                //range.text = (progress + 5).toString() +  "m"
                UpdateLabelRange(progress + 5)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) { }

            override fun onStopTrackingTouch(seekBar: SeekBar) { }
        })

        NearmeApi.getCategories(
            {
                if(it != null) {
                    val categories: ArrayList<Category> = it as ArrayList<Category>
                    for( i in 0..(categories.size-1)){
                        var checkBox = CheckBox(this@SettingsFragment.context)
                        checkBox.setTextColor(Color.parseColor("#434343"));
                        checkBox.text = categories[i].name
                        checkBox.id = i
                        checkBox.isChecked = false
                        if(Preferences.ExitsPreferences(categories[i].name)){
                            checkBox.isChecked = true
                            Log.d("existe", checkBox.text.toString())
                        }
                        checkBox.setOnClickListener {
                            /*
                            if (selects[i].first == 0){
                                selects[i] = Pair<Int, String>(1,categories[i].name)
                            }else{
                                selects[i] = Pair<Int, String>(0,categories[i].name)
                            }*/
                        }
                        selects.add(checkBox)
                        AllPreferences.addView(checkBox)
                    }
                }else{
                    //Notifications.toastNotifications(getString(R.string.notifications_fail), this@SettingsFragment.context, Toast.LENGTH_SHORT, Gravity.BOTTOM )
                }
            },{
               // Notifications.toastNotifications(getString(R.string.notifications_fail), this@SettingsFragment.context, Toast.LENGTH_SHORT, Gravity.BOTTOM )
            },
            getString(R.string.nearme_api_key)
        )

        mapSatelital.setOnClickListener {
            changeSatelitalStyle()
        }

        mapNormal.setOnClickListener {
            changeNormalStyle()
        }

        buttonSave.setOnClickListener {
            for(item in selects){
                if(!item.isChecked){
                    if(!Preferences.removePreference(item.text.toString())){
                        Log.d("remove", "no se pudo: "+ item.text.toString())
                    }
                }else{
                    if(!Preferences.addPreference(item.text.toString())){
                        Log.d("add", "no se pudo: "+ item.text.toString())
                    }
                }
            }
            saveSettings()
        }
    }

    private fun loadSettings(){
        val preference: SharedPreferences? = activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)

        val range: Int = preference?.getInt("range",5) as Int
        val mapStyle: String = preference?.getString("mapStyle","Normal") as String

        slide_range.progress = range - 5
        MAPStyle = mapStyle

        UpdateLabelRange(range)

        if(mapStyle == "Normal"){
            changeNormalStyle()
        }else if(mapStyle == "Satelital"){
            changeSatelitalStyle()
        }
    }

    private fun saveSettings(){
        val preference: SharedPreferences? = activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)

        val range: Int = slide_range.progress + 5
        val mapStyle: String = MAPStyle

        val editor: SharedPreferences.Editor? = preference?.edit()

        editor?.putInt("range",range)
        editor?.putString("mapStyle",mapStyle)

        editor?.commit()
    }

    private fun UpdateLabelRange(newRange: Int){
        range.text = newRange.toString() + "m"
    }

    private fun changeNormalStyle(){
        mapNormal.scaleType = ImageView.ScaleType.FIT_CENTER
        mapSatelital.scaleType = ImageView.ScaleType.CENTER
        MAPStyle = "Normal"
    }

    private fun changeSatelitalStyle(){
        mapSatelital.scaleType = ImageView.ScaleType.FIT_CENTER
        mapNormal.scaleType = ImageView.ScaleType.CENTER
        MAPStyle = "Satelital"
    }
}
