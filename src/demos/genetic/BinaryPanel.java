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
package demos.genetic;

import libai.genetics.chromosomes.BinaryChromosome;
import libai.genetics.chromosomes.Chromosome;
import libai.genetics.Fitness;
import libai.genetics.Engine;

/**
 *
 * @author kronenthaler
 */
public class BinaryPanel extends javax.swing.JPanel implements Fitness {
	/**
	 * Creates new form GeneticPanel
	 */
	public BinaryPanel() {
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jButton1 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();

        jTextPane1.setText("Evolve a population of binary chromosomes to approximate the function x = e^-x. Having a precission of 1.e-5. \nThe evolving algorithm uses the roulette selection method and elitism. The pm = 0.01 and the pc = 0.6, with a population of 200 individuals.\nThe best chromosome is showed in its binary form and the representing value.\n");
        jScrollPane1.setViewportView(jTextPane1);

        jButton1.setText("Evolve");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jProgressBar1.setString("evolving");
        jProgressBar1.setStringPainted(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
		jTextPane1.setText("");
		final Fitness me = this;
		new Thread(new Runnable() {
			@Override
			public void run() {
				int MaxVal = 1;
				int MinVal = 0;
				int Nbits = (int) Math.ceil(Math.log10(1 + ((MaxVal - MinVal) / 1.e-5)) / Math.log10(2));

				Engine engine = new Engine(BinaryChromosome.class, 200, Nbits, 0.6, 0.01, me);
				engine.setProgressBar(jProgressBar1);
				BinaryChromosome best = (BinaryChromosome) engine.evolve(2000);

				double x = best.decode(0, 1);
				jTextPane1.setText(jTextPane1.getText() + "Best Chromosome: " + best + " = " + x + "\n");
				jTextPane1.setText(jTextPane1.getText() + x + " = x ~ e^-x = " + Math.exp(-x) + "\n");
				jTextPane1.setText(jTextPane1.getText() + "Difference = " + (Math.exp(-x) - x) + "\n");
			}
		}).start();
}//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables

	@Override
	public double fitness(Chromosome c) {
		double x = ((BinaryChromosome) c).decode(0, 1);
		return Math.abs(Math.exp(-x) - x); //=>0 si son iguales,
	}

	@Override
	public boolean isBetter(double fitness, double best) {
		return fitness < best;
	}

	@Override
	public double theWorst() {
		return Double.MAX_VALUE;
	}
}
