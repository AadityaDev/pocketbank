package com.technawabs.pocketbank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.technawabs.pocketbank.activities.MainActivity;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;

public class BubbleActivity extends AppCompatActivity {

    private BubblesManager bubblesManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble);

        // configure bubble manager
        initializeBubbleManager();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewNotification();
                findViewById(R.id.button).setVisibility(View.INVISIBLE);
            }
        });
    }

    /*
     * Inflate notifation layout  into bubble layout
     */
    private void addNewNotification() {
        final BubbleLayout bubbleView = (BubbleLayout) LayoutInflater.from(BubbleActivity.this)
                .inflate(R.layout.notification_layout, null);
        // this method call when user remove notification layout
        bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
            @Override
            public void onBubbleRemoved(BubbleLayout bubble) {
                finish();
                Toast.makeText(getApplicationContext(), "Visit Again !",
                        Toast.LENGTH_SHORT).show();
            }
        });
        // this methoid call when cuser click on the notification layout( bubble layout)
        bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {

            @Override
            public void onBubbleClick(BubbleLayout bubble) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(),"Welcome To Pocket Bank",Toast.LENGTH_SHORT).show();
            }
        });

        // add bubble view into bubble manager
        bubblesManager.addBubble(bubbleView, 60, 20);
    }

    /**
     * Configure the trash layout with your BubblesManager builder.
     */
    private void initializeBubbleManager() {
        bubblesManager = new BubblesManager.Builder(this)
                .setTrashLayout(R.layout.notification_trash_layout)
                .build();
        bubblesManager.initialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bubblesManager.recycle();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        moveTaskToBack(true);

    }
}
