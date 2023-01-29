/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package henry.savaryjackson.javatetris.GUI;

/**
 *
 * @author hsavaryjackson
 */
public class MainScreen extends javax.swing.JFrame {

    /**
     * Creates new form MainScreen
     */
    public MainScreen() {
	invalidate();
	updateUI();
	initComponents();
	screen.requestFocus();
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setSize(900,800);
	setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnPlay = new javax.swing.JButton();
        btnStop = new javax.swing.JButton();
        lblScore = new javax.swing.JLabel();
        lblLevel = new javax.swing.JLabel();
        lblLines = new javax.swing.JLabel();
        lblNextPiece = new javax.swing.JLabel();
        grid = new henry.savaryjackson.javatetris.GUI.TetrisGrid(6,6);
        screen = new henry.savaryjackson.javatetris.GUI.TetrisScreen(10,24,grid,this);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnPlay.setFont(new java.awt.Font("Futura", 1, 14)); // NOI18N
        btnPlay.setText("Play");
        btnPlay.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayActionPerformed(evt);
            }
        });

        btnStop.setFont(new java.awt.Font("Futura", 1, 14)); // NOI18N
        btnStop.setText("Quit");
        btnStop.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });

        lblScore.setFont(new java.awt.Font("Futura", 1, 14)); // NOI18N
        lblScore.setText("Score:");

        lblLevel.setFont(new java.awt.Font("Futura", 1, 14)); // NOI18N
        lblLevel.setText("Level:");

        lblLines.setFont(new java.awt.Font("Futura", 1, 14)); // NOI18N
        lblLines.setText("Lines:");

        lblNextPiece.setFont(new java.awt.Font("Futura", 1, 14)); // NOI18N
        lblNextPiece.setText("Next Piece:");

        grid.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout gridLayout = new javax.swing.GroupLayout(grid);
        grid.setLayout(gridLayout);
        gridLayout.setHorizontalGroup(
            gridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 96, Short.MAX_VALUE)
        );
        gridLayout.setVerticalGroup(
            gridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 97, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lblNextPiece)
                    .addComponent(btnPlay, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnStop, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLevel)
                    .addComponent(lblLines)
                    .addComponent(lblScore)
                    .addComponent(grid, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblNextPiece)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(grid, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(lblLevel)
                .addGap(3, 3, 3)
                .addComponent(lblLines)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblScore)
                .addGap(39, 39, 39)
                .addComponent(btnPlay)
                .addGap(18, 18, 18)
                .addComponent(btnStop)
                .addGap(33, 33, 33))
        );

        screen.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout screenLayout = new javax.swing.GroupLayout(screen);
        screen.setLayout(screenLayout);
        screenLayout.setHorizontalGroup(
            screenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 254, Short.MAX_VALUE)
        );
        screenLayout.setVerticalGroup(
            screenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 372, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(screen, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(screen, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(63, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed

        if (screen.paused){
            dispose();
            System.exit(0);
        }else {
            btnStop.setText("Quit");
            screen.paused = true;
        }
    }//GEN-LAST:event_btnStopActionPerformed
 public void updateUI(){
	lblScore.setText(String.format("Score: %d", screen.points));
	lblLines.setText(String.format("Lines: %d" , screen.linesCleared));
	if (screen.paused){
	    btnStop.setText("Quit");
	}
	repaint();
	
    }
    private void btnPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayActionPerformed
       //switch the whether the screen is paused
        screen.paused = !screen.paused;

        //if the game is paused, and the game has not yet started or is over
        if (!screen.inSession){
            screen.initTiles();
            screen.updateSurface(0, screen.w-1);
            screen.createPiece();
            screen.clock.start();
            screen.inSession = true;

        }
        if (!screen.paused)
        btnStop.setText("Pause");

       screen.requestFocus();

    }//GEN-LAST:event_btnPlayActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPlay;
    private javax.swing.JButton btnStop;
    private henry.savaryjackson.javatetris.GUI.TetrisGrid grid;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblLevel;
    private javax.swing.JLabel lblLines;
    private javax.swing.JLabel lblNextPiece;
    private javax.swing.JLabel lblScore;
    private henry.savaryjackson.javatetris.GUI.TetrisScreen screen;
    // End of variables declaration//GEN-END:variables
}
