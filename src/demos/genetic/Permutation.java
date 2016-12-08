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

import libai.genetics.chromosomes.IntegerChromosome;
import libai.genetics.chromosomes.Chromosome;
import libai.genetics.Fitness;
import libai.genetics.Engine;

/**
 *
 * @author kronenthaler
 */
public class Permutation extends javax.swing.JPanel implements Fitness {
	/**
	 * Creates new form Permutation
	 */
	public Permutation() {
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
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton1 = new javax.swing.JButton();

        jTextPane1.setText("Evolve a population of permutation chromosomes to approximate the function F(x) < F(x+1), ie, sort the positions in ascending order.\nThe evolving algorithm uses the roulette selection method and elitism. The pm = 0.01 and the pc = 0.6, with a population of 200 individuals.\nThe best chromosome is showed and the fitness for that chromosome.\n");
        jScrollPane1.setViewportView(jTextPane1);

        jProgressBar1.setString("evolving");
        jProgressBar1.setStringPainted(true);

        jButton1.setText("Evolve");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
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
				Engine engine = new Engine(IntegerChromosome.class, 200, 10, 0.6, 0.01, me);
				engine.setProgressBar(jProgressBar1);
				IntegerChromosome best = (IntegerChromosome) engine.evolve(2000);

				jTextPane1.setText(jTextPane1.getText() + "Best Chromosome: " + best + "\n");
				int[] g = best.getGenes();
				for (int i = 0; i < g.length; i++) {
					jTextPane1.setText(jTextPane1.getText() + g[i] + " ");
				}
				jTextPane1.setText(jTextPane1.getText() + "\nNumber of wrong positions: " + fitness(best));
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
	public double fitness(Chromosome c1) {
		IntegerChromosome c = (IntegerChromosome) c1;
		int g[] = c.getGenes();
		int count = g.length;
		for (int i = 0; i < g.length; i++) {
			if (g[i] == i)
				count--;
		}
		return count;
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
