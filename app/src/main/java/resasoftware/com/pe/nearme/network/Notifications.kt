package resasoftware.com.pe.nearme.network

import android.content.Context
import android.widget.Toast

class Notifications {
    companion object {
        fun toastNotifications(msm: String, context: Context, duration: Int, gravity: Int) {
            val toast: Toast = Toast.makeText(context, msm, duration)
            toast.setGravity(gravity, 0, 0)
            toast.show()
        }
    }
}