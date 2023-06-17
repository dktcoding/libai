/*
 * MIT License
 *
 * Copyright (c) 2009-2016 Ignacio Calderon <https://github.com/kronenthaler>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package demos.fuzzy;

import libai.fuzzy.*;
import libai.fuzzy.defuzzifiers.Defuzzifier;
import libai.fuzzy.operators.AndMethod;
import libai.fuzzy.operators.accumulation.Accumulation;
import libai.fuzzy.operators.activation.ActivationMethod;
import libai.fuzzy.sets.LeftLinearShape;
import libai.fuzzy.sets.RightLinearShape;
import libai.fuzzy.sets.TriangularShape;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kronenthaler
 */
public class FuzzyPanel extends javax.swing.JPanel {
    final FuzzyTerm left = new FuzzyTerm(new RightLinearShape(-1, 0), "left");
    final FuzzyTerm middle = new FuzzyTerm(new TriangularShape(-1, 0, 1), "middle");
    final FuzzyTerm right = new FuzzyTerm(new LeftLinearShape(0, 1), "right");
    final FuzzyVariable position = new libai.fuzzy.FuzzyVariable("position", -2, 2, "meters", left, middle, right);
    final FuzzyTerm movingLeft = new FuzzyTerm(new RightLinearShape(-0.5, 0), "movingLeft");
    final FuzzyTerm standingStill = new FuzzyTerm(new TriangularShape(-0.5, 0, 1), "standingStill");
    final FuzzyTerm movingRight = new FuzzyTerm(new LeftLinearShape(0, 1), "movingRight");
    final FuzzyVariable velocity = new FuzzyVariable("velocity", -1, 2, "m/s", movingLeft, standingStill, movingRight);
    final FuzzyTerm pull = new FuzzyTerm(new RightLinearShape(-0.5, 0), "pull");
    final FuzzyTerm none = new FuzzyTerm(new TriangularShape(-0.5, 0, 0.5), "none");
    final FuzzyTerm push = new FuzzyTerm(new LeftLinearShape(0, 0.5), "push");
    final FuzzyVariable force = new FuzzyVariable("force", -1, 1, 0, "Newtons", Accumulation.MAX, Defuzzifier.COG, pull, none, push);
    final KnowledgeBase kb = new KnowledgeBase(position, velocity, force);
    final Rule r1 = new Rule("r1", 1, AndMethod.MIN, new Antecedent(new Clause("position", "middle"), new Clause("velocity", "standingStill")), new Consequent(new Clause("force", "none")));
    final Rule r2 = new Rule("r2", 1, AndMethod.MIN, new Antecedent(new Clause("position", "left")), new Consequent(new Clause("force", "push")));
    final Rule r3 = new Rule("r3", 1, AndMethod.MIN, new Antecedent(new Clause("position", "right")), new Consequent(new Clause("force", "pull")));
    final Rule r4 = new Rule("r4", 1, AndMethod.MIN, new Antecedent(new Clause("position", "middle")), new Consequent(new Clause("force", "none")));
    final Rule r5 = new Rule("r5", 1, AndMethod.MIN, new Antecedent(new Clause("velocity", "movingLeft")), new Consequent(new Clause("force", "push")));
    final Rule r6 = new Rule("r6", 1, AndMethod.MIN, new Antecedent(new Clause("velocity", "standingStill")), new Consequent(new Clause("force", "none")));
    final Rule r7 = new Rule("r7", 1, AndMethod.MIN, new Antecedent(new Clause("velocity", "movingRight")), new Consequent(new Clause("force", "pull")));
    final Rule r8 = new Rule("r8", 1, AndMethod.MIN, new Antecedent(new Clause("position", "left"), new Clause("velocity", "movingLeft")), new Consequent(new Clause("force", "push")));
    final Rule r9 = new Rule("r9", 1, AndMethod.MIN, new Antecedent(new Clause("position", "right"), new Clause("velocity", "movingRight")), new Consequent(new Clause("force", "pull")));
    final RuleBase rb = new RuleBase("rules", ActivationMethod.MIN, r1, r2, r3, r4, r5, r6, r7, r8, r9);
    final FuzzyController controller = new FuzzyController("car-control", kb, rb);
    private final Car car = new Car(-0.5, 1);
    boolean exit = false;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel canvas;
    private javax.swing.JSpinner frictionsSpn;
    private javax.swing.JButton jButton1;
    private javax.swing.JSpinner posSpn;
    /**
     * Creates new form FuzzyPanel
     */
    public FuzzyPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JScrollPane jScrollPane1 = new JScrollPane();
        JTextPane jTextPane1 = new JTextPane();
        jButton1 = new javax.swing.JButton();
        canvas = new JPanel() {
            public void paint(Graphics g) {
                super.paint(g);

                double l = (car.position + 2) / 4; //interpolation seems weird.
                int w = getWidth();
                int h = getHeight();
                int x = (int) (l * w);
                int y = h - 10;

                g.drawLine(0, y, w, y); // baseline

                g.setColor(Color.red);
                g.drawLine(w / 2, 0, w / 2, h);

                int carH = h / 10;

                g.setColor(Color.blue);
                g.drawRect(x - carH, y - carH, 2 * carH, carH);
            }
        };
        posSpn = new javax.swing.JSpinner();
        JLabel jLabel1 = new JLabel();
        JLabel jLabel2 = new JLabel();
        frictionsSpn = new javax.swing.JSpinner();

        jTextPane1.setText("""
                This fuzzy example is about the classical problem of controlling a car to stop in the middle of a track, the track is 4 meters long, [-2, 2] are the possible starting positions for the car.The car has two variables: direction and position. The direction can be: moving left, standing still or moving right and the position can be:\s
                left, middle or right inside the platform.
                To make the car to stop in the center, it has the following rules:
                r0 = if(position is middle AND direction is standingStill) then none;
                r1 = if(position is left) then push;
                r2 = if(position is right) then pull;
                r3 = if(position is middle) then none;
                r4 = if(direction is movingLeft) then push;
                r5 = if(direction is standingStill) then none;
                r6 = if(direction is movingRight) then pull;
                r7 = if(position is left AND direction is movingLeft) then push;
                r8 = if(position is right AND direction is movingRight) then pull;

                The fuzzy sets pull, none and push forms a fuzzy variable. And the result of the inference is a quantity of force to apply in one direction or another.""");
        jScrollPane1.setViewportView(jTextPane1);

        jButton1.setText("Simulate");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        canvas.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout canvasLayout = new javax.swing.GroupLayout(canvas);
        canvas.setLayout(canvasLayout);
        canvasLayout.setHorizontalGroup(
                canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 449, Short.MAX_VALUE)
        );
        canvasLayout.setVerticalGroup(
                canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 100, Short.MAX_VALUE)
        );

        posSpn.setModel(new javax.swing.SpinnerNumberModel(0d, -2.0d, 2.0d, 0.1d));
        posSpn.addChangeListener(this::posSpnStateChanged);

        jLabel1.setText("Position:");

        jLabel2.setText("Friction:");

        frictionsSpn.setModel(new javax.swing.SpinnerNumberModel(0.5d, 0.d, 1.d, 0.1d));
        frictionsSpn.addChangeListener(this::dirSpnStateChanged);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
                                        .addComponent(canvas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(posSpn, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(frictionsSpn, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
                                                .addComponent(jButton1)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(canvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton1)
                                        .addComponent(posSpn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1)
                                        .addComponent(frictionsSpn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2))
                                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // car position
        // car velocity = exact value. it decreases with friction.
        // friction factor.
        // car mass.

        if (!jButton1.getText().equalsIgnoreCase("simulate")) {
            jButton1.setText("Simulate");
            exit = true;
        } else {
            jButton1.setText("Stop");

            double fps = 15;
            new Thread(() -> {
                car.velocity = 0;
                double force;
                int frames = 0;
                long start = System.currentTimeMillis();
                long lastFrame = System.currentTimeMillis();
                while (!exit) {
                    // update the frame.
                    Map<String, Double> variables = new HashMap<>();
                    variables.put("position", car.position);
                    variables.put("velocity", car.velocity);

                    Map<String, Double> adjustments = controller.fire(variables, 0.01);
                    force = adjustments.get("force");
                    car.applyForce(force, fps);

                    car.updatePosition(fps, (Double) frictionsSpn.getValue());
                    canvas.repaint();

                    // keep the framerate in sync to give context to the rest of the units on the system (meters, seconds, etc)
                    try {
                        long currentFrame = System.currentTimeMillis();

                        if (currentFrame - lastFrame < 1000 / fps)
                            Thread.sleep((long) ((1000 / fps) - (currentFrame - lastFrame)));
                    } catch (Exception ignored) {
                    }

                    lastFrame = System.currentTimeMillis();
                    frames++;
                }
                exit = false;
            }).start();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void posSpnStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_posSpnStateChanged
        car.position = (Double) posSpn.getValue();
        canvas.repaint();
    }//GEN-LAST:event_posSpnStateChanged

    private void dirSpnStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_dirSpnStateChanged
        canvas.repaint();
    }//GEN-LAST:event_dirSpnStateChanged

    static class Car {
        final double mass; // kg
        double position; // m
        double velocity; // m/s

        Car(double position, double mass) {
            this.position = position;
            this.mass = mass;
        }

        void applyForce(double force, double frameRate) { //kg m / s^2
            velocity += force / (mass * frameRate);
        }

        void updateVelocity(double frictionCoefficient) { // coeffiecient is usually small and non-zero
            velocity *= (1 - frictionCoefficient);
            if (Math.abs(velocity) < 1.e-2)
                velocity = 0;
        }

        // this method should be called every frame
        void updatePosition(double frameRate, double friction) {
            position += (velocity / frameRate); // update the position proportionally to the time spend between frames
            updateVelocity(friction / frameRate); //update the velocity proportionally to the framerate.
        }
    }
    // End of variables declaration//GEN-END:variables
}
