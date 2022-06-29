package com.company.dreamgroceries

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.room.Room
import com.company.dreamgroceries.customviews.CustomProgressDialog
import com.company.dreamgroceries.data.local.AppDB
import com.company.dreamgroceries.di.AppComponent
import com.company.dreamgroceries.di.DaggerAppComponent
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp


class MyApp : Application(), Application.ActivityLifecycleCallbacks {


    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
        FirebaseApp.initializeApp(this)
        // Initialize the SDK
        // Initialize the SDK
        Places.initialize(applicationContext, "AIzaSyA65lQlT5KFpZpupRVWBpwoKNSL3WgM6aY")

// Create a new Places client instance

// Create a new Places client instance
        val placesClient = Places.createClient(this)
        mContext = this
        appComponent = DaggerAppComponent.create()


    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        /*if (activity is BaseActivity) {
            currentActivity = activity
            dialogInstance = CustomProgressDialog() // new instance per activity
        }*/
    }

    override fun onActivityResumed(activity: Activity) {}


    companion object {
        private var database: AppDB? = null
        private lateinit var appComponent: AppComponent
        private lateinit var mContext: MyApp
        lateinit var dialogInstance: CustomProgressDialog
     //   lateinit var currentActivity: AppCompatActivity
        fun getAppComponent(): AppComponent {
            return appComponent
        }

        fun getAppContext(): MyApp {
            return mContext
        }

        fun getInstance(): AppDB {
            if (database == null) {
                synchronized(AppDB::class) {
                    database = Room.databaseBuilder(
                        mContext,
                        AppDB::class.java, "notes_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return database!!
        }
    }
}