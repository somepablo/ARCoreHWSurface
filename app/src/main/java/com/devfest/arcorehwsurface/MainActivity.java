package com.devfest.arcorehwsurface;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    private ArFragment arFragment;
    private ModelRenderable astronautRenderable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        ModelRenderable.builder()
                .setSource(this, Uri.parse("Astronaut.sfb"))
                .build()
                .thenAccept(renderable -> astronautRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Log.e(TAG, "Cannot load astronaut model ...");
                            return null;
                        }
                );

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {

            if (astronautRenderable != null) {
                Anchor anchor = hitResult.createAnchor();
                AnchorNode anchorNode = new AnchorNode(anchor);
                anchorNode.setParent(arFragment.getArSceneView().getScene());

                TransformableNode astronaut = new TransformableNode(arFragment.getTransformationSystem());
                astronaut.setParent(anchorNode);
                astronaut.setRenderable(astronautRenderable);
                astronaut.select();
            }

        });

    }
}
