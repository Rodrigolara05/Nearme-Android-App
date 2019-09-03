package resasoftware.com.pe.nearme.models

import android.util.Log
import com.orm.SugarRecord
import java.io.Serializable

data class Preferences (
    val name: String
) : SugarRecord(){
    constructor() : this("")
    companion object {
        fun addPreference(name: String): Boolean {
            val pref: Preferences = Preferences(name = name)
            var r = false
            try{
                val exits = SugarRecord.find(Preferences::class.java, "name = ?", name)
                if(exits.isEmpty()){
                    SugarRecord.save(pref)
                    r = true
                }
            }catch (e: Exception){
                Log.d("Remove Preferences: ", e.message)
            }
            return r
        }

        fun removePreference(name: String): Boolean{
            /*
            var r = false
            val pref: Preferences = Preferences(name = name)
            try {
                val exits = SugarRecord.find(Preferences::class.java, "name = ?", name)
                if(!exits.isEmpty()){
                    SugarRecord.delete(pref)
                    r = true
                }
            }
            catch (e: Exception){
                Log.d("Remove Preferences: ", e.message)
            }
            return r*/
            val preferences: Preferences? = findPreferences(name)
            if(preferences == null){
                return false
            }else{
                preferences.apply {
                    delete()
                }
                return true
            }
        }
        
        fun ExitsPreferences(name: String): Boolean{
            var r = true
            val preference = SugarRecord.find(Preferences::class.java, "name = ?", name)
            if(preference.isEmpty()){
                r = false
            }
            return r
        }

        fun findPreferences(name: String): Preferences? {
            val pref = SugarRecord.find(Preferences::class.java, "name = ?", name)
            return if(pref.isEmpty()) null else pref.first()
        }

        fun allPreferences(): ArrayList<Preferences>{
            val preferences = SugarRecord.findAll(Preferences::class.java)
            return preferences as ArrayList<Preferences>
        }
    }
}
