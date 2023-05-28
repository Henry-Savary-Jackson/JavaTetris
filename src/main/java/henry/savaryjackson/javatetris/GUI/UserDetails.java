/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package henry.savaryjackson.javatetris.GUI;

import com.google.gson.JsonObject;
import henry.savaryjackson.javatetris.utils.WebUtils.APIUtils;
import javax.swing.JOptionPane;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author hsavaryjackson
 */
public class UserDetails extends javax.swing.JFrame {

    /**
     * Creates new form UserDetails
     */
    private JsonObject userData;
    private Screen parent;
    private String token;
    private int highScore;
    private String username;

    public UserDetails(Screen Parent, String token) {

	this.parent = Parent;
	this.token = token;

	initComponents();

	userData = APIUtils.getInfo(this.token);

	if (userData.has("high_score")) {
	    highScore = userData.getAsJsonPrimitive("high_score").getAsInt();
	    lblHighScore.setText("High Score: " + String.valueOf(highScore));
	}
	if (userData.has("username")) {
	    username = userData.getAsJsonPrimitive("username").getAsString();
	    lblUsername.setText("Username: " + username);
	}

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnChangeUsername = new javax.swing.JButton();
        btnDeleteAccount = new javax.swing.JButton();
        lblUsername = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblHighScore = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnChangeUsername.setFont(new java.awt.Font("Futura", 1, 20)); // NOI18N
        btnChangeUsername.setText("Change Username");
        btnChangeUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeUsernameActionPerformed(evt);
            }
        });

        btnDeleteAccount.setFont(new java.awt.Font("Futura", 1, 20)); // NOI18N
        btnDeleteAccount.setText("Delete Account");

        lblUsername.setFont(new java.awt.Font("Futura", 1, 20)); // NOI18N
        lblUsername.setText("Username:");

        lblHighScore.setFont(new java.awt.Font("Futura", 1, 20)); // NOI18N
        lblHighScore.setText("High Score:");

        btnBack.setFont(new java.awt.Font("Futura", 1, 20)); // NOI18N
        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(btnBack)
                .addContainerGap(324, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(226, 226, 226))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnChangeUsername)
                        .addGap(82, 82, 82))))
            .addGroup(layout.createSequentialGroup()
                .addGap(138, 138, 138)
                .addComponent(btnDeleteAccount)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblUsername)
                    .addComponent(lblHighScore))
                .addGap(142, 142, 142))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(btnBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblUsername)
                .addGap(27, 27, 27)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(lblHighScore)
                .addGap(56, 56, 56)
                .addComponent(btnChangeUsername)
                .addGap(42, 42, 42)
                .addComponent(btnDeleteAccount)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnChangeUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeUsernameActionPerformed
	// TODO add your handling code here:
	String newUsername;

	while (true) {
	    newUsername = JOptionPane.showInputDialog(this, "Please enter your new username");

	    if (newUsername == null) {
		return;
	    }

	    if (newUsername.equals("")) {
		JOptionPane.showMessageDialog(this, "Please enter a username", "Error", JOptionPane.ERROR_MESSAGE);
		continue;
	    }

	    break;
	}

	String password;

	while (true) {
	    password = JOptionPane.showInputDialog(this, "Please renter your password");

	    if (password == null) {
		return;
	    }

	    if (password.equals("")) {
		JOptionPane.showMessageDialog(this, "Please enter your password", "Error", JOptionPane.ERROR_MESSAGE);
		continue;
	    }

	    try {
		APIUtils.changeUsername(this.token, newUsername);
	    } catch (NullPointerException ex) {
		JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		continue;
	    } catch (WebClientResponseException ex) {
		JsonObject body = ex.getResponseBodyAs(JsonObject.class);
		if (body == null) {
		    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		    continue;
		}
		JOptionPane.showMessageDialog(this, body.getAsJsonPrimitive("Status").getAsString(), "Error", JOptionPane.ERROR_MESSAGE);
		continue;

	    }

	    break;
	}


    }//GEN-LAST:event_btnChangeUsernameActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
	// TODO add your handling code here:
	this.dispose();
	parent.setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnChangeUsername;
    private javax.swing.JButton btnDeleteAccount;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblHighScore;
    private javax.swing.JLabel lblUsername;
    // End of variables declaration//GEN-END:variables
}
