package com.richify.iamrich.surface;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import com.richify.iamrich.R;

import su.levenetc.android.textsurface.Text;
import su.levenetc.android.textsurface.TextBuilder;
import su.levenetc.android.textsurface.TextSurface;
import su.levenetc.android.textsurface.animations.ChangeColor;
import su.levenetc.android.textsurface.animations.Circle;
import su.levenetc.android.textsurface.animations.Delay;
import su.levenetc.android.textsurface.animations.Parallel;
import su.levenetc.android.textsurface.animations.Rotate3D;
import su.levenetc.android.textsurface.animations.Sequential;
import su.levenetc.android.textsurface.animations.ShapeReveal;
import su.levenetc.android.textsurface.animations.SideCut;
import su.levenetc.android.textsurface.animations.Slide;
import su.levenetc.android.textsurface.animations.TransSurface;
import su.levenetc.android.textsurface.contants.Align;
import su.levenetc.android.textsurface.contants.Direction;
import su.levenetc.android.textsurface.contants.Pivot;
import su.levenetc.android.textsurface.contants.Side;


public class SurfaceTextAnimation {

    public static void play(TextSurface textSurface, AssetManager assetManager, Context context) {

        final Typeface reckonerTypeface = Typeface.createFromAsset(
                assetManager, "fonts/Reckoner.ttf");
        final Typeface quicksandTypeface = Typeface.createFromAsset(
                assetManager, "fonts/Quicksand-Regular.ttf");
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTypeface(quicksandTypeface);

        Text textHello = TextBuilder
                .create("Hello!")
                .setPaint(paint)
                .setSize(64)
                .setAlpha(0)
                .setColor(ContextCompat.getColor(context, R.color.primary_text))
                .setPosition(Align.SURFACE_CENTER).build();

        Text textCongrats = TextBuilder
                .create("Congratulations! ")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(ContextCompat.getColor(context, R.color.primary))
                .setPosition(Align.BOTTOM_OF, textHello).build();

        Text textPurchase = TextBuilder
                .create("You just purchased ")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(ContextCompat.getColor(context, R.color.primary))
                .setPosition(Align.RIGHT_OF, textCongrats).build();

        Text textMost = TextBuilder
                .create("the most")
                .setPaint(paint)
                .setSize(74)
                .setAlpha(0)
                .setColor(ContextCompat.getColor(context, R.color.primary))
                .setPosition(Align.BOTTOM_OF, textPurchase).build();

        Text textExpensive = TextBuilder
                .create("expensive app ")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(ContextCompat.getColor(context, R.color.primary_text))
                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textMost).build();

        Text textPlatform = TextBuilder
                .create("on Google Play!")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(ContextCompat.getColor(context, R.color.primary_text))
                .setPosition(Align.RIGHT_OF, textExpensive).build();

        Text textEnjoy = TextBuilder
                .create("Be proud!")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(ContextCompat.getColor(context, R.color.primary))
                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textPlatform).build();

        Text textShare = TextBuilder
                .create("And share it on")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(ContextCompat.getColor(context, R.color.primary))
                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textEnjoy).build();

        Text textSocial = TextBuilder
                .create("your social media!")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(ContextCompat.getColor(context, R.color.primary))
                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textShare).build();

        textSurface.play(
                new Sequential(
                        ShapeReveal.create(textHello, 750, SideCut.show(Side.LEFT), false),
                        new Parallel(
                                ShapeReveal.create(textHello, 600, SideCut.hide(Side.LEFT), false),
                                new Sequential(Delay.duration(300), ShapeReveal.create(textHello, 600, SideCut.show(Side.LEFT), false))
                        ),
                        new Parallel(
                                new TransSurface(500, textCongrats, Pivot.CENTER),
                                ShapeReveal.create(textCongrats, 1300, SideCut.show(Side.LEFT), false)
                        ),
                        Delay.duration(1000),
                        new Parallel(
                                new TransSurface(750, textPurchase, Pivot.CENTER),
                                Slide.showFrom(Side.LEFT, textPurchase, 750),
                                ChangeColor.to(textPurchase, 750, ContextCompat.getColor(context, R.color.primary_text))
                        ),
                        Delay.duration(500),
                        new Parallel(
                                TransSurface.toCenter(textMost, 500),
                                Rotate3D.showFromSide(textMost, 750, Pivot.TOP)
                        ),
                        Delay.duration(300),
                        new Parallel(
                                TransSurface.toCenter(textExpensive, 500),
                                Slide.showFrom(Side.TOP, textExpensive, 500)
                        ),
                        Delay.duration(300),
                        new Parallel(
                                TransSurface.toCenter(textPlatform, 750),
                                Slide.showFrom(Side.LEFT, textPlatform, 500)
                        ),
                        Delay.duration(500),
                        new Parallel(
                                new TransSurface(1500, textSocial, Pivot.CENTER),
                                new Sequential(
                                        new Sequential(ShapeReveal.create(textEnjoy, 500, Circle.show(Side.CENTER, Direction.OUT), false)),
                                        new Sequential(ShapeReveal.create(textShare, 500, Circle.show(Side.CENTER, Direction.OUT), false)),
                                        new Sequential(ShapeReveal.create(textSocial, 500, Circle.show(Side.CENTER, Direction.OUT), false))

                                )
                        ),
                        Delay.duration(200),
                        new Parallel(
                                ShapeReveal.create(textHello, 1500, SideCut.hide(Side.LEFT), true),
                                ShapeReveal.create(textCongrats, 1500, SideCut.hide(Side.LEFT), true),
                                ShapeReveal.create(textPurchase, 1500, SideCut.hide(Side.LEFT), true),
                                ShapeReveal.create(textMost, 1500, SideCut.hide(Side.LEFT), true),
                                ShapeReveal.create(textExpensive, 1500, SideCut.hide(Side.LEFT), true),
                                ShapeReveal.create(textPlatform, 1500, SideCut.hide(Side.LEFT), true)
                        )
                )
        );
    }
}
