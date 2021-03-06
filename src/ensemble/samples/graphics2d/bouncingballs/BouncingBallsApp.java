/*
 * Copyright (c) 2008, 2014, Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package ensemble.samples.graphics2d.bouncingballs;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;

/**
 * A sample that shows animated bouncing balls.
 * Select a ball to start or stop the animation.
 * Select the reset button to stop all the balls.
 *
 * @sampleName Bouncing Balls
 * @preview preview.png
 *
 * @see java.util.ArrayList
 * @see java.util.List
 * @see javafx.util.Duration
 * @see Stage
 * @see javafx.stage.Screen
 * @see Parent
 * @see Group
 * @see Scene
 * @see javafx.scene.Node
 * @see javafx.scene.effect.Reflection
 * @see javafx.scene.shape.Rectangle
 * @see javafx.scene.shape.Line
 * @see javafx.scene.shape.Circle
 * @see Color
 * @see javafx.scene.paint.CycleMethod
 * @see javafx.scene.paint.RadialGradient
 * @see javafx.scene.paint.Stop
 * @see Button
 * @see javafx.scene.text.Text
 * @see Application
 * @see javafx.animation.Interpolator
 * @see javafx.animation.KeyFrame
 * @see javafx.animation.KeyValue
 * @see javafx.animation.Timeline
 * @see javafx.animation.Animation.Status
 * @see EventHandler
 * @see ActionEvent
 * @see javafx.scene.input.MouseEvent
 * @see Insets
 * @embedded
 */

public class BouncingBallsApp extends Application {
    private BallsScreen ballsscreen;

    public Parent createContent() {
        ballsscreen = new BallsScreen();
        ballsscreen.setLayoutX(15);
        ballsscreen.setLayoutY(20);
        final BallsPane pane = ballsscreen.getPane();

        Button resetButton = new Button("Reset");
        resetButton.setOnAction((ActionEvent event) -> {
            pane.resetBalls();
        });
        VBox vb = new VBox(10);
        vb.getChildren().addAll(resetButton, ballsscreen);
        vb.setPadding(new Insets(15, 24, 15, 24));
        vb.setMaxSize(VBox.USE_PREF_SIZE, VBox.USE_PREF_SIZE);
        vb.setMinSize(VBox.USE_PREF_SIZE, VBox.USE_PREF_SIZE);
        return vb;
    }

    @Override public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    /**
     * Java main for when running without JavaFX launcher
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
