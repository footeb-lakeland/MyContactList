package edu.bdf.mycontactlist;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageButton;

public class Navbar  {

    public static final String TAG = "Navbar";

   public static void initListButton(Activity activity)
   {
       ImageButton listButton = activity.findViewById(R.id.imageButtonList);
       setUpClickEvent(listButton, activity, ContactListActivity.class);
   }

    private static void setUpClickEvent(ImageButton ibImageButton,
                                        Activity fromActivity,
                                        Class<?> targetClass ) {

       // Disable the button that matches the screen that is currently displayed.
        ibImageButton.setEnabled(fromActivity.getClass() != targetClass);

        ibImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(fromActivity, targetClass);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            fromActivity.startActivity(intent);
        });
    }

    public static void initMapButton(Activity activity){
       ImageButton mapButton = activity.findViewById(R.id.imageButtonMap);
        setUpClickEvent(mapButton, activity, ContactMapActivity.class);
    }

   public static void initSettingsButton(Activity activity) {
       ImageButton settingsButton = activity.findViewById(R.id.imageButtonSettings);
       Log.d(TAG, "initSettingsButton: " + (settingsButton == null));
       setUpClickEvent(settingsButton, activity, ContactSettingsActivity.class);
   }


}