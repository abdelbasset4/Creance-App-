import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Benatmani
 */
public class Login extends javax.swing.JFrame {
    String idUser;
    /**
     * Creates new form Login
     */
    public Login() {
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

        logIn = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        password = new javax.swing.JPasswordField();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(450, 630));
        setPreferredSize(new java.awt.Dimension(450, 630));
        setResizable(false);
        setType(java.awt.Window.Type.POPUP);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(null);

        logIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/login.jpg"))); // NOI18N
        logIn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        logIn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logInMouseClicked(evt);
            }
        });
        getContentPane().add(logIn);
        logIn.setBounds(40, 370, 380, 60);

        username.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        username.setBorder(null);
        getContentPane().add(username);
        username.setBounds(55, 212, 330, 33);

        password.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        password.setBorder(null);
        password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passwordKeyPressed(evt);
            }
        });
        getContentPane().add(password);
        password.setBounds(55, 308, 330, 33);

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/log in.png"))); // NOI18N
        background.setMaximumSize(new java.awt.Dimension(450, 630));
        background.setMinimumSize(new java.awt.Dimension(450, 630));
        getContentPane().add(background);
        background.setBounds(0, 0, 450, 630);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        this.setLocationRelativeTo(null);
    }//GEN-LAST:event_formWindowOpened

    private void logInMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logInMouseClicked
        // TODO add your handling code here:
        Connecter conn=new Connecter();
        Connection con=conn.ObteneurConction();      
         if ((!"".equals(username.getText())) & (!"".equals(password.getText()))){
            
        try {                  
            PreparedStatement pst = con.prepareStatement("Select *from user where username=? and password=? ");
            pst.setString(1, username.getText().toLowerCase().trim());
            pst.setString(2, password.getText());
            ResultSet rs=pst.executeQuery();
            if (rs.next()) {
                admin admin = new admin();
                facturation facture = new facturation();
                Boss boss = new Boss();
                if("admin".equals(username.getText().toLowerCase().trim())){
                    admin.setVisible(true);
                    this.dispose();
                }else if("gerant".equals(username.getText().toLowerCase().trim())){
                    boss.setVisible(true);
                    this.dispose();
                }else{
                    getIdUser(username.getText());
                    facture.my_update(idUser);
                    facture.setVisible(true);
                    System.out.println("erorr");
                    this.dispose();
                }
                }else{
                    JOptionPane.showMessageDialog(null, "Utilisateur non valid ou mot de passe erreur");
                    password.setText("");
                }
            
            
        }   catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
         }else{
             JOptionPane.showMessageDialog(null, "Champ vide");
         }
    }//GEN-LAST:event_logInMouseClicked

    private void passwordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()== KeyEvent.VK_ENTER){
            Connecter conn=new Connecter();
        Connection con=conn.ObteneurConction();      
         if ((!"".equals(username.getText())) & (!"".equals(password.getText()))){
            
        try {                  
            PreparedStatement pst = con.prepareStatement("Select *from user where username=? and password=? ");
            pst.setString(1, username.getText().toLowerCase().trim());
            pst.setString(2, password.getText());
            ResultSet rs=pst.executeQuery();
            if (rs.next()) {
                admin admin = new admin();
                facturation facture = new facturation();
                Boss boss = new Boss();
                if("admin".equals(username.getText().toLowerCase().trim())){
                    admin.setVisible(true);
                    this.dispose();
                }else if("gerant".equals(username.getText().toLowerCase().trim())){
                    boss.setVisible(true);
                    this.dispose();
                }else{
                    
                    getIdUser(username.getText());
                    facture.my_update(idUser);
                    facture.setVisible(true);
                    this.dispose();
                }
                }else{
                    JOptionPane.showMessageDialog(null, "Utilisateur non valid ou mot de passe erreur");
                    password.setText("");
                }
            
            
        }   catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
         }else{
             JOptionPane.showMessageDialog(null, "Champ vide");
         }
        }
        
    }//GEN-LAST:event_passwordKeyPressed
public void getIdUser(String T){
        try {
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            PreparedStatement pst = con.prepareStatement("SELECT id FROM user WHERE username=?");
            pst.setString(1, T);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                idUser =  rs.getString("id");
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
}
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel background;
    private javax.swing.JLabel logIn;
    private javax.swing.JPasswordField password;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}