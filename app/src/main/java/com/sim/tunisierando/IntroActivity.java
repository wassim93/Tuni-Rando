package com.sim.tunisierando;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;

import java.util.ArrayList;
import java.util.List;


public class IntroActivity extends AhoyOnboarderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard("Camping", "profiter de la liberté et de l'esprit de la nature ",R.drawable.ic_camping);
        AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard("Achetez et vendez près de chez vous !", "", R.drawable.travel);
        AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard("Nouvelles aventures", "Découvrez des endroits extraordinaires en tunisie",R.drawable.forest);

        ahoyOnboarderCard1.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard2.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard3.setBackgroundColor(R.color.black_transparent);

        List<AhoyOnboarderCard> pages = new ArrayList<>();

        pages.add(ahoyOnboarderCard1);
        pages.add(ahoyOnboarderCard2);
        pages.add(ahoyOnboarderCard3);

        for (AhoyOnboarderCard page : pages) {
            page.setTitleColor(R.color.white);
            page.setDescriptionColor(R.color.white);
        }

        setFinishButtonTitle("commencer l'aventure");
        showNavigationControls(true);
        setGradientBackground();

        setImageBackground(R.drawable.rando);


        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        setFont(face);

        setInactiveIndicatorColor(R.color.grey_600);
        setActiveIndicatorColor(R.color.white);

        setOnboardPages(pages);
    }

    @Override
    public void onFinishButtonPressed() {
        Intent i =new Intent(IntroActivity.this,LoginActivity.class);
        startActivity(i);


    }
}
