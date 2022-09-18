import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sun.management.resources.agent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Benatmani
 */
public class Boss extends javax.swing.JFrame {
    DefaultTableModel modelTableBoss=new DefaultTableModel();

    /**
     * Creates new form Boss
     */
    public Boss() {
        initComponents();
        modelTableBoss.addColumn("enterprise");
        modelTableBoss.addColumn("Contract");
        modelTableBoss.addColumn("Avanant");
        modelTableBoss.addColumn("numeto facture");
        modelTableBoss.addColumn("montant HT");
        modelTableBoss.addColumn("Tarif");
        modelTableBoss.addColumn("montant TTC");
        modelTableBoss.addColumn("etat");
        modelTableBoss.addColumn("cause");
        
    }
private void affichage(){
      modelTableBoss.setRowCount(0);
      Connecter conn=new Connecter();
      Connection con=conn.ObteneurConction();
      
     // Affichage Facture Table
     try {
            String companyStatus="";
            String monthStatus="";
            String etatStatus="and etat = 'Payée'";
            String compteStatus="";
            PreparedStatement pst = null;
            if(company.getSelectedItem() !=null){
                if(company.getSelectedItem() !="Tous"){
                    companyStatus = " and name = '"+ company.getSelectedItem().toString()+"'";
                }
            }           
            if(month.getSelectedItem() !="Tous"){
                monthStatus = " and monthEnv = "+month.getSelectedIndex();
            }
            if(compte.getSelectedItem() !="Tous" & compte.getSelectedItem() !=null){
                compteStatus = " and agence = '"+compte.getSelectedItem().toString()+"'";
            }
            if(etat.getSelectedItem() !="Payée"){
                etatStatus = " and etat != 'Payée'";
            }
            String allSql = "SELECT name,CONCAT(num_facture,'/',anne) AS numero ,FORMAT(montant_ht,2) AS montant_ht,FORMAT(montant_retenu,2) AS montant_retenu,FORMAT(montant_ttc,2) AS montant_ttc,etat,cause,num_contract,ref_avn FROM all_factures WHERE anne = ? " + companyStatus +compteStatus + etatStatus + monthStatus;
            pst = con.prepareStatement(allSql);
            pst.setInt(1,anne.getYear());
            ResultSet rs=pst.executeQuery();
            while (rs.next()){
              modelTableBoss.addRow(new Object[]{rs.getString("name"),rs.getString("num_contract"),rs.getString("ref_avn"),rs.getString("numero"),rs.getString("montant_ht"),rs.getString("montant_retenu"),rs.getString("montant_ttc"),rs.getString("etat"),rs.getString("cause")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(facturation.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        tableStat.setModel(modelTableBoss);
  }
private String calculeMontant(String status){
        try {
            String companyStatus="";
            String compteStatus="";
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            String statusCheck = "";
            if(status =="Payée"){
                statusCheck = " etat = 'Payée'";
            }
            if(status =="NonPayée"){
                statusCheck = " etat != 'Payée'";
            }
            if(status =="Total"){
                statusCheck = " (etat != 'Payée' OR etat = 'Payée') ";
            }
            if(company.getSelectedItem() !=null){
                if(company.getSelectedItem() !="Tous"){
                    companyStatus = " and name = '"+ company.getSelectedItem().toString()+"'";
                }
            }
            if(compte.getSelectedItem() !="Tous" & compte.getSelectedItem() !=null){
                compteStatus = " and agence = '"+compte.getSelectedItem().toString()+"'";
            }
            PreparedStatement pst = con.prepareStatement("SELECT FORMAT(SUM(montant_ttc),2) AS Somme from all_factures where " + statusCheck + companyStatus +compteStatus + " and annuler != 'Annuler'");
            
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                return rs.getString("Somme");                
            }

        } catch (HeadlessException | SQLException e) {
           JOptionPane.showMessageDialog(null, e);
           
        }
        return null;
    
}
private String calculeRetenuAnne(){
        try {
            String companyStatus="";
            String compteStatus="";
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            if(company.getSelectedItem() !=null){
                if(company.getSelectedItem() !="Tous"){
                    companyStatus = " and name = '"+ company.getSelectedItem().toString()+"'";
                }
            }
            System.out.println("company = "+companyStatus);
            if(compte.getSelectedItem() !="Tous" & compte.getSelectedItem() !=null){
                compteStatus = " and agence = '"+compte.getSelectedItem().toString()+"'";
            }
            System.out.println("agence = "+compteStatus);
            PreparedStatement pst = con.prepareStatement("SELECT FORMAT(SUM(montant_retenu),2) AS Somme from all_factures where etat = 'Payée'"  + companyStatus +compteStatus + " and annuler != 'Annuler'");
           
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                System.out.println(rs.getString("Somme")); 
                return rs.getString("Somme");                   
            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return null;
    
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        company = new javax.swing.JComboBox<>();
        etat = new javax.swing.JComboBox<>();
        month = new javax.swing.JComboBox<>();
        compte = new javax.swing.JComboBox<>();
        retenuTotal = new javax.swing.JLabel();
        montantTotalAnne = new javax.swing.JLabel();
        montantTotalNonPayeeAnne = new javax.swing.JLabel();
        montantTotalPayeeAnne = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        anne = new com.toedter.calendar.JYearChooser();
        table = new javax.swing.JScrollPane();
        tableStat = new javax.swing.JTable();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(1200, 720));
        setMinimumSize(new java.awt.Dimension(1200, 720));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(null);

        company.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        company.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                companyActionPerformed(evt);
            }
        });
        getContentPane().add(company);
        company.setBounds(560, 240, 300, 35);

        etat.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        etat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Payée", "Non Payée" }));
        etat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                etatActionPerformed(evt);
            }
        });
        getContentPane().add(etat);
        etat.setBounds(390, 240, 150, 35);

        month.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        month.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tous", "Janvier", "Février", "Mars", "Avril", "Mai", "Juin ", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre" }));
        month.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthActionPerformed(evt);
            }
        });
        getContentPane().add(month);
        month.setBounds(210, 240, 160, 35);

        compte.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        compte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compteActionPerformed(evt);
            }
        });
        getContentPane().add(compte);
        compte.setBounds(880, 240, 300, 35);

        retenuTotal.setFont(new java.awt.Font("Arial", 3, 16)); // NOI18N
        retenuTotal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(retenuTotal);
        retenuTotal.setBounds(920, 93, 190, 30);

        montantTotalAnne.setFont(new java.awt.Font("Arial", 3, 16)); // NOI18N
        montantTotalAnne.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        getContentPane().add(montantTotalAnne);
        montantTotalAnne.setBounds(50, 93, 190, 30);

        montantTotalNonPayeeAnne.setFont(new java.awt.Font("Arial", 3, 16)); // NOI18N
        montantTotalNonPayeeAnne.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(montantTotalNonPayeeAnne);
        montantTotalNonPayeeAnne.setBounds(630, 93, 190, 30);

        montantTotalPayeeAnne.setFont(new java.awt.Font("Arial", 3, 16)); // NOI18N
        montantTotalPayeeAnne.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(montantTotalPayeeAnne);
        montantTotalPayeeAnne.setBounds(340, 93, 190, 30);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/export.png"))); // NOI18N
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel1);
        jLabel1.setBounds(950, 340, 230, 60);

        anne.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                annePropertyChange(evt);
            }
        });
        getContentPane().add(anne);
        anne.setBounds(40, 240, 120, 35);

        table.setBackground(new java.awt.Color(255, 255, 255));
        table.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N

        tableStat.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        tableStat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableStat.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tableStat.setRowHeight(30);
        table.setViewportView(tableStat);

        getContentPane().add(table);
        table.setBounds(20, 400, 1160, 290);

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/dashboard.png"))); // NOI18N
        getContentPane().add(background);
        background.setBounds(0, 0, 1200, 720);

        pack();
    }// </editor-fold>//GEN-END:initComponents
private void comboBoxCompany() { 
        try {
            Connecter conn=new Connecter();
           Connection con=conn.ObteneurConction();
            PreparedStatement pst = con.prepareStatement("SELECT DISTINCT name from enterprises ");

            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                String name = rs.getString("name");
                company.addItem(name);
                
            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }
private void comboBoxAgence() { 
        try {
            Connecter conn=new Connecter();
           Connection con=conn.ObteneurConction();
            PreparedStatement pst = con.prepareStatement("Select DISTINCT agence from contracts ");

            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                String name = rs.getString("agence");
                compte.addItem(name);
                
            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        this.setLocationRelativeTo(null);
        company.removeAllItems();
        company.addItem("Tous");
        comboBoxCompany();
        compte.removeAllItems();
        compte.addItem("Tous");
        comboBoxAgence();
        /*String montantTotal = String.format("%.2f",calculeMontant("Total"));
        String montantTotalPayee = String.format("%.2f",calculeMontant("Payée"));
        String montantTotalNonPayee = String.format("%.2f",calculeMontant("NonPayée"));
        montantTotalAnne.setText(montantTotal);
        montantTotalPayeeAnne.setText(montantTotalPayee);
        montantTotalNonPayeeAnne.setText(montantTotalNonPayee);
        //retenuTotal.setText(Double.toString(calculeRetenuAnne("NonPayée")));
        String montantRetenu = String.format("%.2f", calculeRetenuAnne("NonPayée"));
        retenuTotal.setText(montantRetenu);
        */
        String montantTotal = calculeMontant("Total");
        String montantTotalPayee = calculeMontant("Payée");
        String montantTotalNonPayee = calculeMontant("NonPayée");
        montantTotalAnne.setText(montantTotal);
        montantTotalPayeeAnne.setText(montantTotalPayee);
        montantTotalNonPayeeAnne.setText(montantTotalNonPayee);
        String montantRetenu = calculeRetenuAnne();
        retenuTotal.setText(montantRetenu);
        affichage();
        //fix cloumn width
        tableStat.getColumnModel().getColumn(0).setPreferredWidth(170);
        tableStat.getColumnModel().getColumn(1).setPreferredWidth(300);
        tableStat.getColumnModel().getColumn(2).setPreferredWidth(300);
        tableStat.getColumnModel().getColumn(3).setPreferredWidth(120);
        tableStat.getColumnModel().getColumn(4).setPreferredWidth(190);
        tableStat.getColumnModel().getColumn(5).setPreferredWidth(190);
        tableStat.getColumnModel().getColumn(6).setPreferredWidth(190);
        tableStat.getColumnModel().getColumn(7).setPreferredWidth(80);
        tableStat.getColumnModel().getColumn(8).setPreferredWidth(200);
        tableStat.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,14));
        tableStat.getTableHeader().setBackground(new Color(0,0,0));
        tableStat.getTableHeader().setForeground(new Color(0,0,0));
        DefaultTableCellRenderer renderRight = new DefaultTableCellRenderer();
        renderRight.setHorizontalAlignment(JLabel.RIGHT);
        tableStat.getColumnModel().getColumn(4).setCellRenderer(renderRight);
        tableStat.getColumnModel().getColumn(5).setCellRenderer(renderRight);
        tableStat.getColumnModel().getColumn(6).setCellRenderer(renderRight);
    }//GEN-LAST:event_formWindowOpened

    private void companyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_companyActionPerformed
        // TODO add your handling code here:
        
        affichage();
        /*
        String montantTotal = String.format("%.2f",calculeMontant("Total"));
        String montantTotalPayee = String.format("%.2f",calculeMontant("Payée"));
        String montantTotalNonPayee = String.format("%.2f",calculeMontant("NonPayée"));
        montantTotalAnne.setText(montantTotal);
        montantTotalPayeeAnne.setText(montantTotalPayee);
        montantTotalNonPayeeAnne.setText(montantTotalNonPayee);
        String montantRetenu = String.format("%.2f", calculeRetenuAnne("NonPayée"));
        retenuTotal.setText(montantRetenu);
        */
        String montantTotal = calculeMontant("Total");
        String montantTotalPayee = calculeMontant("Payée");
        String montantTotalNonPayee = calculeMontant("NonPayée");
        montantTotalAnne.setText(montantTotal);
        montantTotalPayeeAnne.setText(montantTotalPayee);
        montantTotalNonPayeeAnne.setText(montantTotalNonPayee);
        String montantRetenu = calculeRetenuAnne();
        System.out.println(montantRetenu);
        retenuTotal.setText(montantRetenu);
    }//GEN-LAST:event_companyActionPerformed

    private void annePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_annePropertyChange
        // TODO add your handling code here:
        affichage();
        /*
        String montantTotal = String.format("%.2f",calculeMontant("Total"));
        String montantTotalPayee = String.format("%.2f",calculeMontant("Payée"));
        String montantTotalNonPayee = String.format("%.2f",calculeMontant("NonPayée"));
        montantTotalAnne.setText(montantTotal);
        montantTotalPayeeAnne.setText(montantTotalPayee);
        montantTotalNonPayeeAnne.setText(montantTotalNonPayee);
        String montantRetenu = String.format("%.2f", calculeRetenuAnne("NonPayée"));
        retenuTotal.setText(montantRetenu);
        */
    }//GEN-LAST:event_annePropertyChange

    private void monthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthActionPerformed
        // TODO add your handling code here:
       affichage();
       /*
        String montantTotal = String.format("%.2f",calculeMontant("Total"));
        String montantTotalPayee = String.format("%.2f",calculeMontant("Payée"));
        String montantTotalNonPayee = String.format("%.2f",calculeMontant("NonPayée"));
        montantTotalAnne.setText(montantTotal);
        montantTotalPayeeAnne.setText(montantTotalPayee);
        montantTotalNonPayeeAnne.setText(montantTotalNonPayee);
        String montantRetenu = String.format("%.2f", calculeRetenuAnne("NonPayée"));
        retenuTotal.setText(montantRetenu);
       */
    }//GEN-LAST:event_monthActionPerformed

    private void compteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compteActionPerformed
        // TODO add your handling code here:
        affichage();
        /*
        String montantTotal = String.format("%.2f",calculeMontant("Total"));
        String montantTotalPayee = String.format("%.2f",calculeMontant("Payée"));
        String montantTotalNonPayee = String.format("%.2f",calculeMontant("NonPayée"));
        montantTotalAnne.setText(montantTotal);
        montantTotalPayeeAnne.setText(montantTotalPayee);
        montantTotalNonPayeeAnne.setText(montantTotalNonPayee);
        String montantRetenu = String.format("%.2f", calculeRetenuAnne("NonPayée"));
        retenuTotal.setText(montantRetenu);
        */
        String montantTotal = calculeMontant("Total");
        String montantTotalPayee = calculeMontant("Payée");
        String montantTotalNonPayee = calculeMontant("NonPayée");
        montantTotalAnne.setText(montantTotal);
        montantTotalPayeeAnne.setText(montantTotalPayee);
        montantTotalNonPayeeAnne.setText(montantTotalNonPayee);
        String montantRetenu = calculeRetenuAnne();
        retenuTotal.setText(montantRetenu);
        
    }//GEN-LAST:event_compteActionPerformed

    private void etatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_etatActionPerformed
        // TODO add your handling code here:
      affichage();
    }//GEN-LAST:event_etatActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
        try{
           JFileChooser jFileChooser = new JFileChooser();
           jFileChooser.showSaveDialog(this);
           File saveFile = jFileChooser.getSelectedFile();
           
           if(saveFile != null){
               saveFile = new File(saveFile.toString()+".xlsx");
               Workbook wb = new XSSFWorkbook();
               Sheet sheet = wb.createSheet("customer");             
               Row rowCol = sheet.createRow(0);
               for(int i=0;i<modelTableBoss.getColumnCount();i++){
                   Cell cell = rowCol.createCell(i);
                   cell.setCellValue(modelTableBoss.getColumnName(i));
               }
               
               for(int j=0;j<modelTableBoss.getRowCount();j++){
                   Row row = sheet.createRow(j+1);
                   for(int k=0;k<modelTableBoss.getColumnCount();k++){
                       Cell cell = row.createCell(k);
                       if(modelTableBoss.getValueAt(j, k)!=null){
                           cell.setCellValue(modelTableBoss.getValueAt(j, k).toString());
                       }
                   }
               }
               FileOutputStream out = new FileOutputStream(new File(saveFile.toString()));
               wb.write(out);
               wb.close();
               out.close();
               openFile(saveFile.toString());
           }else{
               JOptionPane.showMessageDialog(null,"Error al generar archivo");
           }
       }catch(FileNotFoundException e){
           System.out.println(e);
       }catch(IOException io){
           System.out.println(io);
       }
    }//GEN-LAST:event_jLabel1MouseClicked
public void openFile(String file){
        try{
            File path = new File(file);
            Desktop.getDesktop().open(path);
        }catch(IOException ioe){
            System.out.println(ioe);
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
            java.util.logging.Logger.getLogger(Boss.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Boss.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Boss.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Boss.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Boss().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JYearChooser anne;
    private javax.swing.JLabel background;
    private javax.swing.JComboBox<String> company;
    private javax.swing.JComboBox<String> compte;
    private javax.swing.JComboBox<String> etat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel montantTotalAnne;
    private javax.swing.JLabel montantTotalNonPayeeAnne;
    private javax.swing.JLabel montantTotalPayeeAnne;
    private javax.swing.JComboBox<String> month;
    private javax.swing.JLabel retenuTotal;
    private javax.swing.JScrollPane table;
    private javax.swing.JTable tableStat;
    // End of variables declaration//GEN-END:variables
}
