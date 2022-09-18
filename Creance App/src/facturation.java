
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/*
 * To change this license header, choisir License Headers in Project Properties.
 * To change this template file, choisir Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Benatmani
 */
public class facturation extends javax.swing.JFrame {
    String getIdUser;
    String retenuInOldFac,retenuInOldFac1,tvaInOldFac;
    Date dateDebutOldFacture,getDateDebutNewFacture,getDateDebutUpdateFacture;
    String getretenuNewFacture,getRetenuNewFacture1,getTvaNewFacture;
    String getRetenuUpdateFacture,getRetenuUpdateFacture1,getTvaUpdateFacture;
    String idOldContract,idOldAvenant;
    String idNewContract,idUpdateContract;
    boolean clickTableFacture = false;
    String getFullNumero;
    String oldNumberFacture;
    DefaultTableModel modelTableFacturation=new DefaultTableModel();
    DefaultTableModel modelTableNotification=new DefaultTableModel();
    DefaultTableModel modelTableRetenu=new DefaultTableModel();

    /**
     * Creates new form facturation
     */
    public facturation() {
        initComponents();
        causeOldFacture.setEnabled(false);
        modelTableFacturation.addColumn("Enterprise");
        modelTableFacturation.addColumn("Ref Contract");
        modelTableFacturation.addColumn("Ref Avanant");
        modelTableFacturation.addColumn("Numeto facture");
        modelTableFacturation.addColumn("Montant HT");
        modelTableFacturation.addColumn("Tarif");
        modelTableFacturation.addColumn("TVA");
        modelTableFacturation.addColumn("Montant TTC");
        modelTableFacturation.addColumn("Date facturation");
        modelTableFacturation.addColumn("Date d'envoi");
        modelTableFacturation.addColumn("Etat");
        modelTableFacturation.addColumn("Cause");
        modelTableFacturation.addColumn("Annuler");
        modelTableNotification.addColumn("Enterprise");
        modelTableNotification.addColumn("Ref Contract");
        modelTableNotification.addColumn("Ref Avanant");
        modelTableNotification.addColumn("Numeto facture");
        modelTableNotification.addColumn("Montant HT");
        modelTableNotification.addColumn("Tarif");
        modelTableNotification.addColumn("Montant TTC");
        modelTableNotification.addColumn("Date facturation");
        modelTableNotification.addColumn("Date d'envoi");
        modelTableNotification.addColumn("Etat");
        modelTableNotification.addColumn("Cause");
        modelTableRetenu.addColumn("Numero_Contract");
        modelTableRetenu.addColumn("Total retenue");
      
    }
    facturation(int idUser) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choisir Tools | Templates.
    }
    public void my_update(String str) {
            getIdUser = str;
	}
public void getSumHt(){
     float sum = 0;
    for(int i = 0;i<listFactureTable.getRowCount();i++){
        if(!"Annuler".equals(listFactureTable.getValueAt(i,12).toString())){
       sum += Float.parseFloat(listFactureTable.getValueAt(i,4).toString().replace(",",""));
        }
    }
    String s = String.format("%.2f",sum);
    totalHtTable.setText(s + " DA");
}
public void getSumRetenu(){
     float sum = 0;
    for(int i = 0;i<listFactureTable.getRowCount();i++){
        if(!"Annuler".equals(listFactureTable.getValueAt(i,12).toString())){
       sum += Float.parseFloat(listFactureTable.getValueAt(i,5).toString().replace(",",""));
        }
    }
    String s = String.format("%.2f",sum);
    totalRetTable.setText(s + " DA");
}
public void getSumTtc(){
     float sum = 0;
    for(int i = 0;i<listFactureTable.getRowCount();i++){
       if(!"Annuler".equals(listFactureTable.getValueAt(i,12).toString())){
       sum += Float.parseFloat(listFactureTable.getValueAt(i,7).toString().replace(",",""));
        }
    }
    String s = String.format("%.2f",sum);
    totalTtcTable.setText(s + " DA");
}
public void getSumTva(){
     float sum = 0;
    for(int i = 0;i<listFactureTable.getRowCount();i++){
        if(!"Annuler".equals(listFactureTable.getValueAt(i,12).toString())){
       sum += Float.parseFloat(listFactureTable.getValueAt(i,6).toString().replace(",",""));
        }
    }
    String s = String.format("%.2f",sum);
    totalTvaTable.setText(s + " DA");
}
private void affichage(String id){
      modelTableFacturation.setRowCount(0);
      Connecter conn=new Connecter();
      Connection con=conn.ObteneurConction();
     // Affichage Facture Table
     try {
            //PreparedStatement pst = con.prepareStatement("SELECT CONCAT(facture.num_facture,\"/\",facture.anne) AS \"numero facture\",facture.montant_ht,facture.montant_tva,facture.montant_retenu,facture.montant_ttc,facture.date_fact,facture.date_env,facture.etat,facture.cause,enterprises.name,contracts.num_contract,facture.ref_avn FROM enterprises,contracts,facture,user WHERE facture.id_cont = contracts.id_cont and contracts.id_ent = enterprises.id and user.id = facture.idUser and facture.idUser =? and facture.anne = ?");
            PreparedStatement pst = con.prepareStatement("SELECT CONCAT(num_facture,\"/\",anne) AS \"numero facture\",FORMAT(montant_ht,2) AS montant_ht,FORMAT(montant_retenu,2) AS montant_retenu,FORMAT((montant_ht - montant_retenu)*(SELECT contracts.tva /100 from contracts where contracts.num_contract = all_factures.num_contract),2) AS TVA,FORMAT(montant_ttc,2) AS montant_ttc,date_fact,date_env,etat,cause,name,num_contract,ref_avn,annuler FROM all_factures WHERE idUser =? and anne = ?");
            pst.setString(1, id);
            pst.setInt(2,yearSearch.getYear());
            ResultSet rs=pst.executeQuery();
            while (rs.next()){
              modelTableFacturation.addRow(new Object[]{rs.getString("name"),rs.getString("num_contract"),rs.getString("ref_avn"),rs.getString("numero facture"),rs.getString("montant_ht"),rs.getString("montant_retenu"),rs.getString("tva"),rs.getString("montant_ttc"),rs.getString("date_fact"),rs.getString("date_env"),rs.getString("etat"),rs.getString("cause"),rs.getString("annuler")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(facturation.class.getName()).log(Level.SEVERE, null, ex);
        }
        listFactureTable.setModel(modelTableFacturation);
  }
/////////////
////////////
private void affichageTotalRetenu(String id){
      modelTableRetenu.setRowCount(0);
      Connecter conn=new Connecter();
      Connection con=conn.ObteneurConction();
     // Affichage Total Retenu Table
     try {
            //PreparedStatement pst = con.prepareStatement("SELECT CONCAT(facture.num_facture,\"/\",facture.anne) AS \"numero facture\",facture.montant_ht,facture.montant_tva,facture.montant_retenu,facture.montant_ttc,facture.date_fact,facture.date_env,facture.etat,facture.cause,enterprises.name,contracts.num_contract,facture.ref_avn FROM enterprises,contracts,facture,user WHERE facture.id_cont = contracts.id_cont and contracts.id_ent = enterprises.id and user.id = facture.idUser and facture.idUser =? and facture.anne = ?");
            PreparedStatement pst = con.prepareStatement("SELECT all_factures.num_contract,FORMAT(SUM(all_factures.montant_retenu),2) AS total_retenu FROM all_factures WHERE all_factures.idUser = ? and etat = 'Payée' and annuler != 'Annuler' GROUP BY all_factures.num_contract ");
            pst.setString(1, id);
            ResultSet rs=pst.executeQuery();
            while (rs.next()){
              modelTableRetenu.addRow(new Object[]{rs.getString("num_contract"),rs.getString("total_retenu")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(facturation.class.getName()).log(Level.SEVERE, null, ex);
        }
        totalRetenuTable.setModel(modelTableRetenu);
}
private void affichageNotification(String id){
      modelTableNotification.setRowCount(0);
      Connecter conn=new Connecter();
      Connection con=conn.ObteneurConction();
     // Affichage Facture Table
     try {
            //PreparedStatement pst = con.prepareStatement("SELECT CONCAT(facture.num_facture,\"/\",facture.anne) AS \"numero facture\",facture.montant_ht,facture.montant_retenu,facture.montant_ttc,facture.date_fact,facture.date_env,facture.etat,facture.cause,enterprises.name,contracts.num_contract,facture.ref_avn FROM enterprises,contracts,facture,user WHERE facture.id_cont = contracts.id_cont and contracts.id_ent = enterprises.id and user.id = facture.idUser and facture.idUser =? and facture.etat != \"Payée\" and contracts.id_cont IN (SELECT facture.id_cont from facture GROUP BY `id_cont` HAVING ADDDATE(facture.date_env,60) < CURRENT_DATE())");
            PreparedStatement pst = con.prepareStatement("SELECT CONCAT(num_facture,\"/\",anne) AS \"numero facture\",montant_ht,montant_retenu,montant_ttc,date_fact,date_env,etat,cause,name,num_contract,ref_avn FROM all_factures WHERE idUser =? and etat != \"Payée\" and ADDDATE(date_env,60) < CURRENT_DATE()");
            pst.setString(1, id);
            ResultSet rs=pst.executeQuery();
            while (rs.next()){
              modelTableNotification.addRow(new Object[]{rs.getString("name"),rs.getString("num_contract"),rs.getString("ref_avn"),rs.getString("numero facture"),rs.getString("montant_ht"),rs.getString("montant_retenu"),rs.getString("montant_ttc"),rs.getString("date_fact"),rs.getString("date_env"),rs.getString("etat"),rs.getString("cause")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(facturation.class.getName()).log(Level.SEVERE, null, ex);
        }
        notificationTable.setModel(modelTableNotification);
  }
 private void getCountNotification(){
            int rowCount = notificationTable.getRowCount();
            if(rowCount>9){
                notificationCount.setText("+9");
            }else{
                notificationCount.setText(Integer.toString(rowCount));
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

        changePasswordDialog = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        close1 = new javax.swing.JLabel();
        passwordChanged = new javax.swing.JTextField();
        confirmPassword = new javax.swing.JTextField();
        saveConfirmPassword = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        totalRetenuDialog = new javax.swing.JDialog();
        jPanel2 = new javax.swing.JPanel();
        backTotalRetenu = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        totalRetenuTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        box = new javax.swing.JPanel();
        mainFacturePanel = new javax.swing.JPanel();
        menuPanel = new javax.swing.JPanel();
        totalRetenuBtn = new javax.swing.JLabel();
        changePasswordBtn = new javax.swing.JLabel();
        logOutBtn = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        notificationCount = new javax.swing.JLabel();
        userNameChange = new javax.swing.JLabel();
        newFacture = new javax.swing.JLabel();
        deleteFactureBtn = new javax.swing.JLabel();
        updateFacture = new javax.swing.JLabel();
        oldFacture = new javax.swing.JLabel();
        numFactureSearch = new javax.swing.JTextField();
        yearSearch = new com.toedter.calendar.JYearChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        listFactureTable = new javax.swing.JTable();
        notificationBtn = new javax.swing.JLabel();
        userMenu = new javax.swing.JLabel();
        annulerFacture = new javax.swing.JLabel();
        etat = new javax.swing.JComboBox<>();
        contractSearch = new javax.swing.JComboBox<>();
        totalTtcTable = new javax.swing.JLabel();
        totalTvaTable = new javax.swing.JLabel();
        totalRetTable = new javax.swing.JLabel();
        totalHtTable = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        backgroundMain = new javax.swing.JLabel();
        addFacturePanel = new javax.swing.JPanel();
        tvaNewFacture = new javax.swing.JLabel();
        backNewFacture1 = new javax.swing.JLabel();
        saveNewFacture = new javax.swing.JLabel();
        montantNewTTC = new javax.swing.JLabel();
        retenuNewFacture = new javax.swing.JLabel();
        backNewFacture = new javax.swing.JLabel();
        numFactureNew = new javax.swing.JLabel();
        companyNewContr = new javax.swing.JComboBox<>();
        companyNewAvn = new javax.swing.JComboBox<>();
        companyNewFacture = new javax.swing.JComboBox<>();
        montantNewHT = new javax.swing.JTextField();
        dateNewDenvoi = new com.toedter.calendar.JDateChooser();
        dateNewFacturation = new com.toedter.calendar.JDateChooser();
        copyNumFacture = new javax.swing.JLabel();
        supRetenu = new javax.swing.JCheckBox();
        backgroundAdd = new javax.swing.JLabel();
        updateFacturePanel = new javax.swing.JPanel();
        saveUpdateFacture = new javax.swing.JLabel();
        bisCheck = new javax.swing.JCheckBox();
        backUpdateFacture = new javax.swing.JLabel();
        searchFactureBtnUpdate = new javax.swing.JLabel();
        searchAnneUpdateFactureInput = new javax.swing.JTextField();
        searchUpdateFactureInput = new javax.swing.JTextField();
        tvaUpdateFacture = new javax.swing.JLabel();
        retenuUpdateFacture = new javax.swing.JLabel();
        montantUpdateTTC = new javax.swing.JLabel();
        causeUpdateFacture = new javax.swing.JTextField();
        montantHTUpdate = new javax.swing.JTextField();
        dateUpdateFacturation = new com.toedter.calendar.JDateChooser();
        dateUpdateDenvoi = new com.toedter.calendar.JDateChooser();
        autreFactureUpdate = new javax.swing.JLabel();
        companyUpdateFacture1 = new javax.swing.JComboBox<>();
        companyUpdateContr1 = new javax.swing.JComboBox<>();
        companyUpdateAvn1 = new javax.swing.JComboBox<>();
        etatTheFacture1 = new javax.swing.JComboBox<>();
        annulerCheck = new javax.swing.JCheckBox();
        declareUpdate = new javax.swing.JLabel();
        supRetenuUpdate = new javax.swing.JCheckBox();
        backgroundUpdate = new javax.swing.JLabel();
        etatFactureUpdate = new javax.swing.JLabel();
        avnantUpdateFacture = new javax.swing.JLabel();
        contractUpdateFacture = new javax.swing.JLabel();
        companyUpdateFacture = new javax.swing.JLabel();
        oldFacturePanel = new javax.swing.JPanel();
        tvaOldFacture = new javax.swing.JLabel();
        saveOldFacture = new javax.swing.JLabel();
        backOldFacture = new javax.swing.JLabel();
        anneOldFacture = new javax.swing.JTextField();
        causeOldFacture = new javax.swing.JTextField();
        numOldFacture = new javax.swing.JTextField();
        etatOldFacture = new javax.swing.JComboBox<>();
        companyOldFacture = new javax.swing.JComboBox<>();
        companyOldContr = new javax.swing.JComboBox<>();
        companyOldAvn = new javax.swing.JComboBox<>();
        montantHTOld = new javax.swing.JTextField();
        montantOldTTC = new javax.swing.JLabel();
        retenuOldFacture = new javax.swing.JLabel();
        dateOldFacturation = new com.toedter.calendar.JDateChooser();
        dateOldDenvoi = new com.toedter.calendar.JDateChooser();
        backNewFacture4 = new javax.swing.JLabel();
        bissFacture = new javax.swing.JCheckBox();
        supRetenu1 = new javax.swing.JCheckBox();
        backgroundOld = new javax.swing.JLabel();
        notificationPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        notificationTable = new javax.swing.JTable();
        backNotification = new javax.swing.JLabel();
        backgroundAdd1 = new javax.swing.JLabel();

        changePasswordDialog.setBackground(new java.awt.Color(255, 255, 255));
        changePasswordDialog.setMinimumSize(new java.awt.Dimension(560, 470));
        changePasswordDialog.setUndecorated(true);
        changePasswordDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                changePasswordDialogWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(null);

        close1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Group 6.png"))); // NOI18N
        close1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        close1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                close1MouseClicked(evt);
            }
        });
        jPanel1.add(close1);
        close1.setBounds(150, 305, 130, 40);

        passwordChanged.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        passwordChanged.setBorder(null);
        jPanel1.add(passwordChanged);
        passwordChanged.setBounds(150, 157, 260, 27);

        confirmPassword.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        confirmPassword.setBorder(null);
        jPanel1.add(confirmPassword);
        confirmPassword.setBounds(150, 240, 260, 27);

        saveConfirmPassword.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/saveChangePassword.png"))); // NOI18N
        saveConfirmPassword.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        saveConfirmPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveConfirmPasswordMouseClicked(evt);
            }
        });
        jPanel1.add(saveConfirmPassword);
        saveConfirmPassword.setBounds(290, 300, 130, 50);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/changePasswordPanel.png"))); // NOI18N
        jPanel1.add(jLabel1);
        jLabel1.setBounds(0, 0, 560, 470);

        javax.swing.GroupLayout changePasswordDialogLayout = new javax.swing.GroupLayout(changePasswordDialog.getContentPane());
        changePasswordDialog.getContentPane().setLayout(changePasswordDialogLayout);
        changePasswordDialogLayout.setHorizontalGroup(
            changePasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        changePasswordDialogLayout.setVerticalGroup(
            changePasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        totalRetenuDialog.setMinimumSize(new java.awt.Dimension(560, 470));
        totalRetenuDialog.setUndecorated(true);
        totalRetenuDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                totalRetenuDialogWindowOpened(evt);
            }
        });

        jPanel2.setLayout(null);

        backTotalRetenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Group 6.png"))); // NOI18N
        backTotalRetenu.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        backTotalRetenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backTotalRetenuMouseClicked(evt);
            }
        });
        jPanel2.add(backTotalRetenu);
        backTotalRetenu.setBounds(230, 380, 130, 40);

        totalRetenuTable.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        totalRetenuTable.setModel(new javax.swing.table.DefaultTableModel(
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
        totalRetenuTable.setRowHeight(30);
        jScrollPane3.setViewportView(totalRetenuTable);

        jPanel2.add(jScrollPane3);
        jScrollPane3.setBounds(50, 80, 470, 230);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/retenuTotalPanel.png"))); // NOI18N
        jPanel2.add(jLabel2);
        jLabel2.setBounds(0, 0, 560, 470);

        javax.swing.GroupLayout totalRetenuDialogLayout = new javax.swing.GroupLayout(totalRetenuDialog.getContentPane());
        totalRetenuDialog.getContentPane().setLayout(totalRetenuDialogLayout);
        totalRetenuDialogLayout.setHorizontalGroup(
            totalRetenuDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
        );
        totalRetenuDialogLayout.setVerticalGroup(
            totalRetenuDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1200, 720));
        setMinimumSize(new java.awt.Dimension(1200, 720));
        setPreferredSize(new java.awt.Dimension(1200, 720));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(null);

        box.setMaximumSize(new java.awt.Dimension(1200, 720));
        box.setPreferredSize(new java.awt.Dimension(1200, 720));
        box.setLayout(new java.awt.CardLayout());

        mainFacturePanel.setMaximumSize(new java.awt.Dimension(1200, 720));
        mainFacturePanel.setMinimumSize(new java.awt.Dimension(1200, 720));
        mainFacturePanel.setPreferredSize(new java.awt.Dimension(1200, 720));
        mainFacturePanel.setLayout(null);

        menuPanel.setBackground(new java.awt.Color(255, 255, 255));
        menuPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuPanelMouseExited(evt);
            }
        });
        menuPanel.setLayout(null);

        totalRetenuBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/retenuTotalBtn.png"))); // NOI18N
        totalRetenuBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        totalRetenuBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                totalRetenuBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                totalRetenuBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                totalRetenuBtnMouseExited(evt);
            }
        });
        menuPanel.add(totalRetenuBtn);
        totalRetenuBtn.setBounds(8, 0, 306, 54);

        changePasswordBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/changePasswordBtn.png"))); // NOI18N
        changePasswordBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        changePasswordBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                changePasswordBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                changePasswordBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                changePasswordBtnMouseExited(evt);
            }
        });
        menuPanel.add(changePasswordBtn);
        changePasswordBtn.setBounds(8, 55, 306, 54);

        logOutBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logOutBtn.png"))); // NOI18N
        logOutBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        logOutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logOutBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logOutBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logOutBtnMouseExited(evt);
            }
        });
        menuPanel.add(logOutBtn);
        logOutBtn.setBounds(8, 110, 306, 54);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Rectangle 1.png"))); // NOI18N
        menuPanel.add(jLabel3);
        jLabel3.setBounds(0, 0, 332, 186);

        mainFacturePanel.add(menuPanel);
        menuPanel.setBounds(820, 120, 332, 0);

        notificationCount.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        notificationCount.setForeground(new java.awt.Color(255, 255, 255));
        notificationCount.setText("+9");
        mainFacturePanel.add(notificationCount);
        notificationCount.setBounds(253, 65, 20, 20);

        userNameChange.setFont(new java.awt.Font("Arial", 3, 16)); // NOI18N
        userNameChange.setForeground(new java.awt.Color(255, 255, 255));
        mainFacturePanel.add(userNameChange);
        userNameChange.setBounds(920, 70, 140, 30);

        newFacture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add facture.jpg"))); // NOI18N
        newFacture.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        newFacture.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                newFactureMouseClicked(evt);
            }
        });
        mainFacturePanel.add(newFacture);
        newFacture.setBounds(50, 170, 210, 58);

        deleteFactureBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete.png"))); // NOI18N
        deleteFactureBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        deleteFactureBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteFactureBtnMouseClicked(evt);
            }
        });
        mainFacturePanel.add(deleteFactureBtn);
        deleteFactureBtn.setBounds(495, 165, 200, 70);

        updateFacture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/update facture.png"))); // NOI18N
        updateFacture.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        updateFacture.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                updateFactureMouseClicked(evt);
            }
        });
        mainFacturePanel.add(updateFacture);
        updateFacture.setBounds(260, 165, 230, 70);

        oldFacture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/old facture.png"))); // NOI18N
        oldFacture.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        oldFacture.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                oldFactureMouseClicked(evt);
            }
        });
        mainFacturePanel.add(oldFacture);
        oldFacture.setBounds(940, 170, 220, 60);

        numFactureSearch.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        numFactureSearch.setBorder(null);
        numFactureSearch.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                numFactureSearchCaretUpdate(evt);
            }
        });
        numFactureSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                numFactureSearchKeyPressed(evt);
            }
        });
        mainFacturePanel.add(numFactureSearch);
        numFactureSearch.setBounds(820, 300, 130, 28);

        yearSearch.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                yearSearchCaretPositionChanged(evt);
            }
        });
        yearSearch.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                yearSearchPropertyChange(evt);
            }
        });
        mainFacturePanel.add(yearSearch);
        yearSearch.setBounds(990, 295, 120, 33);

        listFactureTable.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        listFactureTable.setModel(new javax.swing.table.DefaultTableModel(
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
        listFactureTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        listFactureTable.setGridColor(new java.awt.Color(0, 0, 0));
        listFactureTable.setRowHeight(30);
        listFactureTable.setRowMargin(5);
        listFactureTable.setSelectionBackground(new java.awt.Color(0, 0, 255));
        listFactureTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listFactureTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(listFactureTable);

        mainFacturePanel.add(jScrollPane1);
        jScrollPane1.setBounds(50, 350, 1100, 270);

        notificationBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bell btn.png"))); // NOI18N
        notificationBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        notificationBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                notificationBtnMouseClicked(evt);
            }
        });
        mainFacturePanel.add(notificationBtn);
        notificationBtn.setBounds(220, 60, 60, 50);

        userMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/userMenuBtn.png"))); // NOI18N
        userMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        userMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                userMenuMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                userMenuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                userMenuMouseExited(evt);
            }
        });
        mainFacturePanel.add(userMenu);
        userMenu.setBounds(1070, 40, 90, 100);

        annulerFacture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/annulerFacture.png"))); // NOI18N
        annulerFacture.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                annulerFactureMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                annulerFactureMouseEntered(evt);
            }
        });
        mainFacturePanel.add(annulerFacture);
        annulerFacture.setBounds(700, 165, 220, 70);

        etat.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        etat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Payée", "Non Payée" }));
        etat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                etatActionPerformed(evt);
            }
        });
        mainFacturePanel.add(etat);
        etat.setBounds(610, 300, 150, 35);

        contractSearch.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        contractSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                contractSearchFocusGained(evt);
            }
        });
        contractSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contractSearchActionPerformed(evt);
            }
        });
        mainFacturePanel.add(contractSearch);
        contractSearch.setBounds(310, 300, 280, 35);

        totalTtcTable.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        mainFacturePanel.add(totalTtcTable);
        totalTtcTable.setBounds(950, 640, 190, 20);

        totalTvaTable.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        mainFacturePanel.add(totalTvaTable);
        totalTvaTable.setBounds(690, 640, 150, 20);

        totalRetTable.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        mainFacturePanel.add(totalRetTable);
        totalRetTable.setBounds(430, 640, 150, 20);

        totalHtTable.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        mainFacturePanel.add(totalHtTable);
        totalHtTable.setBounds(130, 640, 200, 20);

        jLabel7.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel7.setText("Total TTC :");
        mainFacturePanel.add(jLabel7);
        jLabel7.setBounds(860, 640, 100, 19);
        jLabel7.getAccessibleContext().setAccessibleName("");

        jLabel6.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel6.setText("Total TVA :");
        mainFacturePanel.add(jLabel6);
        jLabel6.setBounds(600, 640, 90, 19);
        jLabel6.getAccessibleContext().setAccessibleName("");

        jLabel5.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel5.setText("Total Tarif :");
        mainFacturePanel.add(jLabel5);
        jLabel5.setBounds(340, 640, 90, 19);
        jLabel5.getAccessibleContext().setAccessibleName("");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel4.setText("Total HT :");
        mainFacturePanel.add(jLabel4);
        jLabel4.setBounds(50, 640, 160, 19);
        jLabel4.getAccessibleContext().setAccessibleName("");

        backgroundMain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/mainFacture.png"))); // NOI18N
        mainFacturePanel.add(backgroundMain);
        backgroundMain.setBounds(0, 0, 1200, 720);

        box.add(mainFacturePanel, "card5");

        addFacturePanel.setMaximumSize(new java.awt.Dimension(1200, 720));
        addFacturePanel.setMinimumSize(new java.awt.Dimension(1200, 720));
        addFacturePanel.setPreferredSize(new java.awt.Dimension(1200, 720));
        addFacturePanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                addFacturePanelKeyTyped(evt);
            }
        });
        addFacturePanel.setLayout(null);

        tvaNewFacture.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        addFacturePanel.add(tvaNewFacture);
        tvaNewFacture.setBounds(110, 360, 150, 30);

        backNewFacture1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/autre.png"))); // NOI18N
        backNewFacture1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        backNewFacture1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                backNewFacture1FocusGained(evt);
            }
        });
        backNewFacture1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backNewFacture1MouseClicked(evt);
            }
        });
        addFacturePanel.add(backNewFacture1);
        backNewFacture1.setBounds(200, 580, 220, 56);

        saveNewFacture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.jpg"))); // NOI18N
        saveNewFacture.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        saveNewFacture.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveNewFactureMouseClicked(evt);
            }
        });
        addFacturePanel.add(saveNewFacture);
        saveNewFacture.setBounds(430, 575, 210, 60);

        montantNewTTC.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        addFacturePanel.add(montantNewTTC);
        montantNewTTC.setBounds(790, 360, 220, 30);

        retenuNewFacture.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        addFacturePanel.add(retenuNewFacture);
        retenuNewFacture.setBounds(400, 360, 170, 30);

        backNewFacture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/back.jpg"))); // NOI18N
        backNewFacture.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        backNewFacture.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backNewFactureMouseClicked(evt);
            }
        });
        addFacturePanel.add(backNewFacture);
        backNewFacture.setBounds(640, 580, 220, 56);

        numFactureNew.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        numFactureNew.setForeground(new java.awt.Color(255, 0, 0));
        addFacturePanel.add(numFactureNew);
        numFactureNew.setBounds(160, 200, 160, 30);

        companyNewContr.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        companyNewContr.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                companyNewContrFocusGained(evt);
            }
        });
        companyNewContr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                companyNewContrActionPerformed(evt);
            }
        });
        addFacturePanel.add(companyNewContr);
        companyNewContr.setBounds(890, 200, 280, 35);

        companyNewAvn.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        companyNewAvn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                companyNewAvnFocusGained(evt);
            }
        });
        companyNewAvn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                companyNewAvnActionPerformed(evt);
            }
        });
        addFacturePanel.add(companyNewAvn);
        companyNewAvn.setBounds(140, 270, 280, 35);

        companyNewFacture.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        companyNewFacture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                companyNewFactureActionPerformed(evt);
            }
        });
        addFacturePanel.add(companyNewFacture);
        companyNewFacture.setBounds(480, 200, 280, 35);

        montantNewHT.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        montantNewHT.setBorder(null);
        montantNewHT.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                montantNewHTCaretUpdate(evt);
            }
        });
        montantNewHT.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                montantNewHTKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                montantNewHTKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                montantNewHTKeyTyped(evt);
            }
        });
        addFacturePanel.add(montantNewHT);
        montantNewHT.setBounds(660, 284, 240, 28);

        dateNewDenvoi.setBackground(new java.awt.Color(255, 255, 255));
        dateNewDenvoi.setDateFormatString("dd/MM/yyyy");
        dateNewDenvoi.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        addFacturePanel.add(dateNewDenvoi);
        dateNewDenvoi.setBounds(690, 450, 210, 35);

        dateNewFacturation.setBackground(new java.awt.Color(255, 255, 255));
        dateNewFacturation.setDateFormatString("dd/MM/yyyy");
        dateNewFacturation.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        addFacturePanel.add(dateNewFacturation);
        dateNewFacturation.setBounds(250, 450, 210, 35);

        copyNumFacture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/copy.png"))); // NOI18N
        copyNumFacture.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        copyNumFacture.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                copyNumFactureMouseClicked(evt);
            }
        });
        addFacturePanel.add(copyNumFacture);
        copyNumFacture.setBounds(325, 195, 30, 40);

        supRetenu.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        supRetenu.setForeground(new java.awt.Color(102, 102, 102));
        supRetenu.setText("Annuler Retenue");
        supRetenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supRetenuActionPerformed(evt);
            }
        });
        addFacturePanel.add(supRetenu);
        supRetenu.setBounds(1010, 280, 150, 30);

        backgroundAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/addFacture.png"))); // NOI18N
        addFacturePanel.add(backgroundAdd);
        backgroundAdd.setBounds(0, 0, 1200, 720);

        box.add(addFacturePanel, "card2");

        updateFacturePanel.setMaximumSize(new java.awt.Dimension(1200, 720));
        updateFacturePanel.setMinimumSize(new java.awt.Dimension(1200, 720));
        updateFacturePanel.setPreferredSize(new java.awt.Dimension(1200, 720));
        updateFacturePanel.setLayout(null);

        saveUpdateFacture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.jpg"))); // NOI18N
        saveUpdateFacture.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        saveUpdateFacture.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveUpdateFactureMouseClicked(evt);
            }
        });
        updateFacturePanel.add(saveUpdateFacture);
        saveUpdateFacture.setBounds(380, 600, 210, 60);

        bisCheck.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        bisCheck.setText("Bis");
        updateFacturePanel.add(bisCheck);
        bisCheck.setBounds(850, 160, 100, 30);

        backUpdateFacture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/back.jpg"))); // NOI18N
        backUpdateFacture.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        backUpdateFacture.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backUpdateFactureMouseClicked(evt);
            }
        });
        updateFacturePanel.add(backUpdateFacture);
        backUpdateFacture.setBounds(600, 603, 220, 56);

        searchFactureBtnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search.jpg"))); // NOI18N
        searchFactureBtnUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        searchFactureBtnUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchFactureBtnUpdateMouseClicked(evt);
            }
        });
        updateFacturePanel.add(searchFactureBtnUpdate);
        searchFactureBtnUpdate.setBounds(750, 160, 60, 50);

        searchAnneUpdateFactureInput.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        searchAnneUpdateFactureInput.setBorder(null);
        searchAnneUpdateFactureInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchAnneUpdateFactureInputKeyPressed(evt);
            }
        });
        updateFacturePanel.add(searchAnneUpdateFactureInput);
        searchAnneUpdateFactureInput.setBounds(655, 168, 75, 30);

        searchUpdateFactureInput.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        searchUpdateFactureInput.setBorder(null);
        searchUpdateFactureInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchUpdateFactureInputKeyPressed(evt);
            }
        });
        updateFacturePanel.add(searchUpdateFactureInput);
        searchUpdateFactureInput.setBounds(545, 168, 70, 30);

        tvaUpdateFacture.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        updateFacturePanel.add(tvaUpdateFacture);
        tvaUpdateFacture.setBounds(100, 390, 170, 30);

        retenuUpdateFacture.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        updateFacturePanel.add(retenuUpdateFacture);
        retenuUpdateFacture.setBounds(390, 390, 170, 30);

        montantUpdateTTC.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        updateFacturePanel.add(montantUpdateTTC);
        montantUpdateTTC.setBounds(780, 390, 215, 30);

        causeUpdateFacture.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        causeUpdateFacture.setBorder(null);
        updateFacturePanel.add(causeUpdateFacture);
        causeUpdateFacture.setBounds(510, 535, 490, 28);

        montantHTUpdate.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        montantHTUpdate.setBorder(null);
        montantHTUpdate.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                montantHTUpdateCaretUpdate(evt);
            }
        });
        montantHTUpdate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                montantHTUpdateFocusGained(evt);
            }
        });
        montantHTUpdate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                montantHTUpdateKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                montantHTUpdateKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                montantHTUpdateKeyTyped(evt);
            }
        });
        updateFacturePanel.add(montantHTUpdate);
        montantHTUpdate.setBounds(183, 330, 240, 28);

        dateUpdateFacturation.setBackground(new java.awt.Color(255, 255, 255));
        dateUpdateFacturation.setDateFormatString("dd/MM/yyyy");
        dateUpdateFacturation.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        updateFacturePanel.add(dateUpdateFacturation);
        dateUpdateFacturation.setBounds(240, 460, 210, 35);

        dateUpdateDenvoi.setBackground(new java.awt.Color(255, 255, 255));
        dateUpdateDenvoi.setDateFormatString("dd/MM/yyyy");
        dateUpdateDenvoi.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        updateFacturePanel.add(dateUpdateDenvoi);
        dateUpdateDenvoi.setBounds(680, 460, 210, 35);

        autreFactureUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/autre.png"))); // NOI18N
        autreFactureUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        autreFactureUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autreFactureUpdateMouseClicked(evt);
            }
        });
        updateFacturePanel.add(autreFactureUpdate);
        autreFactureUpdate.setBounds(150, 608, 228, 56);

        companyUpdateFacture1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        companyUpdateFacture1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                companyUpdateFacture1ActionPerformed(evt);
            }
        });
        updateFacturePanel.add(companyUpdateFacture1);
        companyUpdateFacture1.setBounds(160, 250, 260, 35);

        companyUpdateContr1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        companyUpdateContr1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                companyUpdateContr1FocusGained(evt);
            }
        });
        companyUpdateContr1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                companyUpdateContr1ActionPerformed(evt);
            }
        });
        updateFacturePanel.add(companyUpdateContr1);
        companyUpdateContr1.setBounds(520, 250, 270, 35);

        companyUpdateAvn1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        companyUpdateAvn1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                companyUpdateAvn1FocusGained(evt);
            }
        });
        updateFacturePanel.add(companyUpdateAvn1);
        companyUpdateAvn1.setBounds(900, 250, 270, 35);

        etatTheFacture1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        etatTheFacture1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Envoyée", "Payée", "Rejetée" }));
        etatTheFacture1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                etatTheFacture1ActionPerformed(evt);
            }
        });
        updateFacturePanel.add(etatTheFacture1);
        etatTheFacture1.setBounds(120, 530, 280, 35);

        annulerCheck.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        annulerCheck.setText("Annuler");
        updateFacturePanel.add(annulerCheck);
        annulerCheck.setBounds(1060, 520, 100, 40);

        declareUpdate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        declareUpdate.setForeground(new java.awt.Color(255, 0, 0));
        updateFacturePanel.add(declareUpdate);
        declareUpdate.setBounds(440, 210, 420, 20);

        supRetenuUpdate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        supRetenuUpdate.setForeground(new java.awt.Color(102, 102, 102));
        supRetenuUpdate.setText("Annuler Retenue");
        supRetenuUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supRetenuUpdateActionPerformed(evt);
            }
        });
        updateFacturePanel.add(supRetenuUpdate);
        supRetenuUpdate.setBounds(520, 330, 150, 25);

        backgroundUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/updateFacture.png"))); // NOI18N
        updateFacturePanel.add(backgroundUpdate);
        backgroundUpdate.setBounds(0, 0, 1200, 720);

        etatFactureUpdate.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        updateFacturePanel.add(etatFactureUpdate);
        etatFactureUpdate.setBounds(115, 520, 220, 30);

        avnantUpdateFacture.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        updateFacturePanel.add(avnantUpdateFacture);
        avnantUpdateFacture.setBounds(920, 230, 240, 30);

        contractUpdateFacture.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        updateFacturePanel.add(contractUpdateFacture);
        contractUpdateFacture.setBounds(550, 230, 240, 30);

        companyUpdateFacture.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        updateFacturePanel.add(companyUpdateFacture);
        companyUpdateFacture.setBounds(160, 230, 240, 30);

        box.add(updateFacturePanel, "card3");

        oldFacturePanel.setMaximumSize(new java.awt.Dimension(1200, 720));
        oldFacturePanel.setMinimumSize(new java.awt.Dimension(1200, 720));
        oldFacturePanel.setPreferredSize(new java.awt.Dimension(1200, 720));
        oldFacturePanel.setLayout(null);

        tvaOldFacture.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        oldFacturePanel.add(tvaOldFacture);
        tvaOldFacture.setBounds(100, 390, 170, 30);

        saveOldFacture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.jpg"))); // NOI18N
        saveOldFacture.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        saveOldFacture.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveOldFactureMouseClicked(evt);
            }
        });
        oldFacturePanel.add(saveOldFacture);
        saveOldFacture.setBounds(425, 600, 210, 60);

        backOldFacture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/back.jpg"))); // NOI18N
        backOldFacture.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        backOldFacture.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backOldFactureMouseClicked(evt);
            }
        });
        oldFacturePanel.add(backOldFacture);
        backOldFacture.setBounds(650, 605, 220, 56);

        anneOldFacture.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        anneOldFacture.setBorder(null);
        anneOldFacture.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                anneOldFactureKeyPressed(evt);
            }
        });
        oldFacturePanel.add(anneOldFacture);
        anneOldFacture.setBounds(655, 168, 70, 28);

        causeOldFacture.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        causeOldFacture.setBorder(null);
        oldFacturePanel.add(causeOldFacture);
        causeOldFacture.setBounds(510, 535, 490, 28);

        numOldFacture.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        numOldFacture.setBorder(null);
        numOldFacture.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                numOldFactureKeyPressed(evt);
            }
        });
        oldFacturePanel.add(numOldFacture);
        numOldFacture.setBounds(545, 168, 70, 28);

        etatOldFacture.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        etatOldFacture.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Envoyée", "Payée", "Rejetée" }));
        etatOldFacture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                etatOldFactureActionPerformed(evt);
            }
        });
        oldFacturePanel.add(etatOldFacture);
        etatOldFacture.setBounds(120, 520, 280, 35);

        companyOldFacture.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        companyOldFacture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                companyOldFactureActionPerformed(evt);
            }
        });
        oldFacturePanel.add(companyOldFacture);
        companyOldFacture.setBounds(160, 250, 250, 35);

        companyOldContr.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        companyOldContr.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                companyOldContrFocusGained(evt);
            }
        });
        companyOldContr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                companyOldContrActionPerformed(evt);
            }
        });
        oldFacturePanel.add(companyOldContr);
        companyOldContr.setBounds(520, 250, 270, 35);

        companyOldAvn.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        companyOldAvn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                companyOldAvnFocusGained(evt);
            }
        });
        oldFacturePanel.add(companyOldAvn);
        companyOldAvn.setBounds(900, 250, 270, 35);

        montantHTOld.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        montantHTOld.setBorder(null);
        montantHTOld.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                montantHTOldCaretUpdate(evt);
            }
        });
        montantHTOld.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                montantHTOldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                montantHTOldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                montantHTOldKeyTyped(evt);
            }
        });
        oldFacturePanel.add(montantHTOld);
        montantHTOld.setBounds(183, 328, 245, 28);

        montantOldTTC.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        oldFacturePanel.add(montantOldTTC);
        montantOldTTC.setBounds(780, 390, 220, 30);

        retenuOldFacture.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        oldFacturePanel.add(retenuOldFacture);
        retenuOldFacture.setBounds(390, 390, 170, 30);

        dateOldFacturation.setBackground(new java.awt.Color(255, 255, 255));
        dateOldFacturation.setDateFormatString("dd/MM/yyyy");
        dateOldFacturation.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        oldFacturePanel.add(dateOldFacturation);
        dateOldFacturation.setBounds(240, 460, 210, 35);

        dateOldDenvoi.setBackground(new java.awt.Color(255, 255, 255));
        dateOldDenvoi.setDateFormatString("dd/MM/yyyy");
        dateOldDenvoi.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        oldFacturePanel.add(dateOldDenvoi);
        dateOldDenvoi.setBounds(680, 460, 210, 35);

        backNewFacture4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/autre.png"))); // NOI18N
        backNewFacture4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        backNewFacture4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backNewFacture4MouseClicked(evt);
            }
        });
        oldFacturePanel.add(backNewFacture4);
        backNewFacture4.setBounds(190, 607, 220, 56);

        bissFacture.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        bissFacture.setText("BIS");
        oldFacturePanel.add(bissFacture);
        bissFacture.setBounds(580, 210, 90, 31);

        supRetenu1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        supRetenu1.setForeground(new java.awt.Color(102, 102, 102));
        supRetenu1.setText("Annuler Retenue");
        supRetenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supRetenu1ActionPerformed(evt);
            }
        });
        oldFacturePanel.add(supRetenu1);
        supRetenu1.setBounds(520, 330, 150, 25);

        backgroundOld.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/oldFacture.png"))); // NOI18N
        oldFacturePanel.add(backgroundOld);
        backgroundOld.setBounds(0, 0, 1200, 720);

        box.add(oldFacturePanel, "card6");

        notificationPanel.setLayout(null);

        notificationTable.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        notificationTable.setModel(new javax.swing.table.DefaultTableModel(
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
        notificationTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        notificationTable.setGridColor(new java.awt.Color(255, 255, 255));
        notificationTable.setRowHeight(35);
        notificationTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                notificationTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(notificationTable);

        notificationPanel.add(jScrollPane2);
        jScrollPane2.setBounds(50, 140, 1100, 370);

        backNotification.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/back.jpg"))); // NOI18N
        backNotification.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        backNotification.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backNotificationMouseClicked(evt);
            }
        });
        notificationPanel.add(backNotification);
        backNotification.setBounds(590, 610, 220, 56);

        backgroundAdd1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/notification page.png"))); // NOI18N
        notificationPanel.add(backgroundAdd1);
        backgroundAdd1.setBounds(0, 0, 1200, 720);

        box.add(notificationPanel, "card7");

        getContentPane().add(box);
        box.setBounds(0, 0, 1200, 720);

        pack();
    }// </editor-fold>//GEN-END:initComponents
private void comboBoxCompany(JComboBox combo) { 
        try {
            Connecter conn=new Connecter();
           Connection con=conn.ObteneurConction();
            PreparedStatement pst = con.prepareStatement("SELECT DISTINCT enterprises.name FROM user,role,contracts,enterprises WHERE contracts.id_cont = role.id_contract and contracts.id_ent = enterprises.id and user.id = role.id_user and role.id_user =?");
            pst.setString(1, getIdUser);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                combo.addItem(name);               
            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
private void comboBoxContract(JComboBox combo) { 
        try {
            Connecter conn=new Connecter();
           Connection con=conn.ObteneurConction();
            PreparedStatement pst = con.prepareStatement("SELECT DISTINCT num_contract FROM all_factures WHERE idUser =?");
            pst.setString(1, getIdUser);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString("num_contract");
                combo.addItem(name);               
            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
private String getUsername() { 
        try {
            Connecter conn=new Connecter();
           Connection con=conn.ObteneurConction();
            PreparedStatement pst = con.prepareStatement("SELECT username from user where id =?");
            pst.setString(1, getIdUser);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                return rs.getString("username");
                
            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return null;
    }

private int getMaxNumFacture(){
    try {
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            PreparedStatement pst = con.prepareStatement("SELECT MAX(CAST(all_factures.num_facture AS INT)) AS Max FROM all_factures WHERE all_factures.anne = YEAR(CURRENT_DATE)");
            //pst.setString(1,T);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String maxNumeroFacture;
                maxNumeroFacture =  rs.getString("max");
                if(maxNumeroFacture ==null){
                    maxNumeroFacture = "0";
                }
                else if(maxNumeroFacture.contains("Bis")){
                    maxNumeroFacture = maxNumeroFacture.substring(0,maxNumeroFacture.indexOf("Bis"));
                }
                return Integer.parseInt(maxNumeroFacture);
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return 0;

}

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        this.setLocationRelativeTo(null);
        affichage(getIdUser);
        affichageNotification(getIdUser);
        getCountNotification();
        userNameChange.setText(getUsername());
        contractSearch.removeAllItems();
        contractSearch.addItem("Tous");
        comboBoxContract(contractSearch);
        //fix cloumn width
        listFactureTable.getColumnModel().getColumn(0).setPreferredWidth(170);
        listFactureTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        listFactureTable.getColumnModel().getColumn(2).setPreferredWidth(300);
        listFactureTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        listFactureTable.getColumnModel().getColumn(4).setPreferredWidth(190);
        listFactureTable.getColumnModel().getColumn(5).setPreferredWidth(150);
        listFactureTable.getColumnModel().getColumn(6).setPreferredWidth(150);
        listFactureTable.getColumnModel().getColumn(7).setPreferredWidth(190);
        listFactureTable.getColumnModel().getColumn(8).setPreferredWidth(100);
        listFactureTable.getColumnModel().getColumn(9).setPreferredWidth(100);
        listFactureTable.getColumnModel().getColumn(10).setPreferredWidth(80);
        listFactureTable.getColumnModel().getColumn(11).setPreferredWidth(150);
        listFactureTable.getColumnModel().getColumn(12).setPreferredWidth(70);
        //
        notificationTable.getColumnModel().getColumn(0).setPreferredWidth(170);
        notificationTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        notificationTable.getColumnModel().getColumn(2).setPreferredWidth(300);
        notificationTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        notificationTable.getColumnModel().getColumn(4).setPreferredWidth(190);
        notificationTable.getColumnModel().getColumn(5).setPreferredWidth(150);
        notificationTable.getColumnModel().getColumn(6).setPreferredWidth(150);
        notificationTable.getColumnModel().getColumn(7).setPreferredWidth(190);
        notificationTable.getColumnModel().getColumn(8).setPreferredWidth(100);
        notificationTable.getColumnModel().getColumn(9).setPreferredWidth(100);
        notificationTable.getColumnModel().getColumn(10).setPreferredWidth(200);
        
        //style header jtable 
        listFactureTable.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,14));
        listFactureTable.getTableHeader().setBackground(new Color(0,0,0));
        listFactureTable.getTableHeader().setForeground(new Color(0,0,0));
        notificationTable.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,14));
        notificationTable.getTableHeader().setBackground(new Color(0,0,0));
        notificationTable.getTableHeader().setForeground(new Color(0,0,0));
        //montant alignment
        DefaultTableCellRenderer renderRight = new DefaultTableCellRenderer();
        renderRight.setHorizontalAlignment(JLabel.RIGHT);
        listFactureTable.getColumnModel().getColumn(4).setCellRenderer(renderRight);
        listFactureTable.getColumnModel().getColumn(5).setCellRenderer(renderRight);
        listFactureTable.getColumnModel().getColumn(6).setCellRenderer(renderRight);
        listFactureTable.getColumnModel().getColumn(7).setCellRenderer(renderRight);
        
        notificationTable.getColumnModel().getColumn(4).setCellRenderer(renderRight);
        notificationTable.getColumnModel().getColumn(5).setCellRenderer(renderRight);
        notificationTable.getColumnModel().getColumn(6).setCellRenderer(renderRight);
        notificationTable.getColumnModel().getColumn(7).setCellRenderer(renderRight);
        
        getSumHt();
        
    }//GEN-LAST:event_formWindowOpened

    private void backNewFactureMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backNewFactureMouseClicked
        // TODO add your handling code here:
        mainFacturePanel.setVisible(true);
        addFacturePanel.setVisible(false);
        updateFacturePanel.setVisible(false);
        oldFacturePanel.setVisible(false);
        numFactureNew.setText("");
        montantNewHT.setText("");
        retenuNewFacture.setText("");
        montantNewTTC.setText("");
        dateNewFacturation.setCalendar(null);
        dateNewDenvoi.setCalendar(null);
        rechercheFacture();
    }//GEN-LAST:event_backNewFactureMouseClicked

    private void backUpdateFactureMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backUpdateFactureMouseClicked
        // TODO add your handling code here:
        mainFacturePanel.setVisible(true);
        addFacturePanel.setVisible(false);
        updateFacturePanel.setVisible(false);
        oldFacturePanel.setVisible(false);
        searchUpdateFactureInput.setText("");
        searchAnneUpdateFactureInput.setText("");
        montantHTUpdate.setText("");
        retenuUpdateFacture.setText("");
        montantUpdateTTC.setText("");
        companyUpdateFacture.setText("");
        contractUpdateFacture.setText("");
        avnantUpdateFacture.setText("");
        etatFactureUpdate.setText("");
        causeUpdateFacture.setText("");
        dateUpdateFacturation.setCalendar(null);
        dateUpdateDenvoi.setCalendar(null);
        annulerCheck.setSelected(false);
        rechercheFacture();
    }//GEN-LAST:event_backUpdateFactureMouseClicked

    private void backOldFactureMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backOldFactureMouseClicked
        // TODO add your handling code here:
        mainFacturePanel.setVisible(true);
        addFacturePanel.setVisible(false);
        updateFacturePanel.setVisible(false);
        oldFacturePanel.setVisible(false);
        numOldFacture.setText("");
        anneOldFacture.setText("");
        montantHTOld.setText("");
        retenuOldFacture.setText("");
        montantOldTTC.setText("");
        causeOldFacture.setText("");
        dateOldFacturation.setCalendar(null);
        dateOldDenvoi.setCalendar(null);
        rechercheFacture();
    }//GEN-LAST:event_backOldFactureMouseClicked

    private void newFactureMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newFactureMouseClicked
        // TODO add your handling code here:
        mainFacturePanel.setVisible(false);
        addFacturePanel.setVisible(true);
        updateFacturePanel.setVisible(false);
        oldFacturePanel.setVisible(false);
        companyNewFacture.removeAllItems();
        comboBoxCompany(companyNewFacture);
    }//GEN-LAST:event_newFactureMouseClicked

    private void updateFactureMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_updateFactureMouseClicked
        // TODO add your handling code here:
        mainFacturePanel.setVisible(false);
        addFacturePanel.setVisible(false);
        updateFacturePanel.setVisible(true);
        oldFacturePanel.setVisible(false);
        //causeUpdateFacture.setEditable(false);
         comboBoxCompany(companyUpdateFacture1);

    }//GEN-LAST:event_updateFactureMouseClicked

    private void oldFactureMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_oldFactureMouseClicked
        // TODO add your handling code here:
        mainFacturePanel.setVisible(false);
        addFacturePanel.setVisible(false);
        updateFacturePanel.setVisible(false);
        oldFacturePanel.setVisible(true);
        companyOldFacture.removeAllItems();
        comboBoxCompany(companyOldFacture);
    }//GEN-LAST:event_oldFactureMouseClicked

    private void companyOldFactureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_companyOldFactureActionPerformed
        // TODO add your handling code here:
          companyOldContr.removeAllItems();
        companyOldContr.addItem("choisir contract");
        try {
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            //PreparedStatement pst = con.prepareStatement("SELECT DISTINCT contracts.num_contract FROM contracts,enterprises WHERE contracts.id_ent = enterprises.id and enterprises.name =? ");
            PreparedStatement pst = con.prepareStatement("SELECT DISTINCT contracts.num_contract FROM contracts,enterprises,role,user WHERE contracts.id_ent = enterprises.id and user.id = role.id_user and role.id_contract = contracts.id_cont and enterprises.name =? and user.id = ?");
            if(companyOldFacture.getSelectedItem() !=null){
            String selected = companyOldFacture.getSelectedItem().toString();         
                pst.setString(1,selected);
            }
            pst.setString(2,getIdUser);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString("num_contract");
                companyOldContr.addItem(name);
            }
        } catch (HeadlessException | SQLException e) {
           // JOptionPane.showMessageDialog(null, e);
           
        }
    }//GEN-LAST:event_companyOldFactureActionPerformed

    private void companyOldContrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_companyOldContrActionPerformed
        // TODO add your handling code here:
        companyOldAvn.removeAllItems();
        companyOldAvn.addItem("choisir Avenant");
        try {
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            PreparedStatement pst = con.prepareStatement("SELECT avenant.avenant_num,contracts.retenu FROM avenant,contracts WHERE avenant.ref_cont = contracts.id_cont and contracts.num_contract =?");
            if(companyOldContr.getSelectedItem() !=null){
            String selected = companyOldContr.getSelectedItem().toString();   
                pst.setString(1,selected);
            }  
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString("avenant_num");
                companyOldAvn.addItem(name);
            }
           
        } catch (HeadlessException | SQLException e) {
           // JOptionPane.showMessageDialog(null, e);
           
        }
        // get TVA and Retenu from cotract
        try {
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            PreparedStatement pst1 = con.prepareStatement("SELECT contracts.retenu,contracts.tva,contracts.date_debut FROM contracts WHERE contracts.num_contract =?");
             if(companyOldContr.getSelectedItem() !=null){
                String selected = companyOldContr.getSelectedItem().toString();   
                pst1.setString(1,selected);
             }
             
            ResultSet rs1 = pst1.executeQuery();
            while (rs1.next()) {
                retenuInOldFac = rs1.getString("retenu");
                retenuInOldFac1 = rs1.getString("retenu");
                tvaInOldFac = rs1.getString("tva");
                dateDebutOldFacture = rs1.getDate("date_debut");
                
            }
           
        } catch (HeadlessException | SQLException e) {
           // JOptionPane.showMessageDialog(null, e);
           
        }
    }//GEN-LAST:event_companyOldContrActionPerformed

    private void companyNewFactureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_companyNewFactureActionPerformed
       
          companyNewContr.removeAllItems();
        companyNewContr.addItem("choisir contract");
        try {
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            //PreparedStatement pst = con.prepareStatement("SELECT DISTINCT contracts.num_contract FROM contracts,enterprises WHERE contracts.id_ent = enterprises.id and enterprises.name =? ");
            PreparedStatement pst = con.prepareStatement("SELECT DISTINCT contracts.num_contract FROM contracts,enterprises,role,user WHERE contracts.id_ent = enterprises.id and user.id = role.id_user and role.id_contract = contracts.id_cont and enterprises.name =? and user.id = ?");
            if(companyNewFacture.getSelectedItem() !=null){
            String selected = companyNewFacture.getSelectedItem().toString();
            
                pst.setString(1,selected);
            }
            pst.setString(2,getIdUser);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString("num_contract");
                companyNewContr.addItem(name);
            }
        } catch (HeadlessException | SQLException e) {
           // JOptionPane.showMessageDialog(null, e);
           
        }

    }//GEN-LAST:event_companyNewFactureActionPerformed

    private void companyNewContrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_companyNewContrActionPerformed
       
        companyNewAvn.removeAllItems();
        companyNewAvn.addItem("choisir Avenant");
        try {
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            PreparedStatement pst = con.prepareStatement("SELECT avenant.avenant_num,contracts.retenu FROM avenant,contracts WHERE avenant.ref_cont = contracts.id_cont and contracts.num_contract =?");
            if(companyNewContr.getSelectedItem() !=null){
            String selected = companyNewContr.getSelectedItem().toString();   
                pst.setString(1,selected);
            }  
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString("avenant_num");
                companyNewAvn.addItem(name);
            }
           
        } catch (HeadlessException | SQLException e) {
           // JOptionPane.showMessageDialog(null, e);
           
        }
        // get TVA and Retenu from cotract
        try {
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            PreparedStatement pst1 = con.prepareStatement("SELECT contracts.retenu,contracts.tva,contracts.date_debut FROM contracts WHERE contracts.num_contract =?");
             if(companyNewContr.getSelectedItem() !=null){
                String selected = companyNewContr.getSelectedItem().toString();   
                pst1.setString(1,selected);
             }
             
            ResultSet rs1 = pst1.executeQuery();
            while (rs1.next()) {
                getretenuNewFacture = rs1.getString("retenu");
                getRetenuNewFacture1 = rs1.getString("retenu");
                getTvaNewFacture = rs1.getString("tva");
                getDateDebutNewFacture = rs1.getDate("date_debut");
            }
           
        } catch (HeadlessException | SQLException e) {
           // JOptionPane.showMessageDialog(null, e);
           
        }
    }//GEN-LAST:event_companyNewContrActionPerformed

    private void numOldFactureKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numOldFactureKeyPressed
        // TODO add your handling code here:
        text_long(numOldFacture,10,evt);
    }//GEN-LAST:event_numOldFactureKeyPressed

    private void montantHTOldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_montantHTOldCaretUpdate
        // TODO add your handling code here:
        //old calcule with ttc to ht
        /*double montantTTC = 0;
        String contact = companyOldContr.getSelectedItem().toString();
        if(retenuInOldFac != null && contact != "choisir contract"){
            String montantHt = montantHTOld.getText().replaceAll(" ", "");
            
            if(montantHTOld.getText().length()>0 ){
             montantTTC = Double.parseDouble(montantHt);
            }      
            double retenuPer = Double.parseDouble(retenuInOldFac)/100;
            double ht = montantTTC/(1-retenuPer);
            String montantTtcTotal = String.format("%.2f",ht) ;
            String montantRetenu = String.format("%.2f",ht*retenuPer);        
            montantTtcTotal = montantTtcTotal.replace(",", ".");
            montantRetenu = montantRetenu.replace(",", ".");
            montantOldTTC.setText(montantTtcTotal);
            retenuOldFacture.setText(montantRetenu);
        }else{
            JOptionPane.showMessageDialog(null, "Choisse Contract");
        }*/
        //new calcule with ht to ttc
        double montantHTCalc = 0;
        String contact = companyOldFacture.getSelectedItem().toString();
        if(retenuInOldFac != null && contact != "choisir contract"){          
            String montantHtInput = montantHTOld.getText();
            if(montantHTOld.getText().length()>0){
                montantHtInput = montantHtInput.replaceAll(" ","");
                montantHTCalc = Double.parseDouble(montantHtInput);          
            }
            double retenuPer =(double) Double.parseDouble(retenuInOldFac)/100; 
            double tvaPer =(double) Double.parseDouble(tvaInOldFac)/100; 
            String montantTTCTotal = String.format("%.2f", montantHTCalc - ( montantHTCalc*retenuPer) +((montantHTCalc -(montantHTCalc*retenuPer))*tvaPer) );
            String montantRetenu = String.format("%.2f", montantHTCalc*retenuPer);
            String montantTva = String.format("%.2f", (montantHTCalc -(montantHTCalc*retenuPer))*tvaPer);
            montantTTCTotal = montantTTCTotal.replace(",", ".");
            montantRetenu = montantRetenu.replace(",", ".");
            montantTva = montantTva.replace(",", ".");
            montantOldTTC.setText(montantTTCTotal);
            retenuOldFacture.setText(montantRetenu);
            tvaOldFacture.setText(montantTva);
        }else{
            JOptionPane.showMessageDialog(null, "Choisse Contract");
        }
    }//GEN-LAST:event_montantHTOldCaretUpdate

    private void montantHTOldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_montantHTOldKeyPressed
        // TODO add your handling code here:
        text_long(montantHTOld,15,evt);
    }//GEN-LAST:event_montantHTOldKeyPressed

    private void saveOldFactureMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveOldFactureMouseClicked
        // TODO add your handling code here:
        String numOldFac = numOldFacture.getText();
        String anneFacture = anneOldFacture.getText();
        String numAvn = companyOldAvn.getSelectedItem().toString();
        SimpleDateFormat Rt = new SimpleDateFormat("yyyy-MM-dd");
        String etat = etatOldFacture.getSelectedItem().toString();
        String cause = causeOldFacture.getText();
        if ((!"".equals(montantHTOld.getText()))& dateOldFacturation.getDate() != null & dateOldDenvoi.getDate() !=null & (!"".equals(numOldFacture.getText())) & (!"".equals(anneOldFacture.getText()))){
            //if(dateOldFacturation.getDate().after(dateDebutOldFacture) && dateOldDenvoi.getDate().after(dateDebutOldFacture)){
            if(montantHTOld.getText().replaceAll(" ", "").matches("\\d+(.\\d+)?")){    
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            PreparedStatement pst;
            try {
            pst = con.prepareStatement("INSERT INTO encient_facture (num_facture,montant_ht,montant_retenu,montant_ttc,date_fact,date_env,etat,cause,id_cont,ref_avn,idUser,annuler) VALUE(?,?,?,?,?,?,?,?,?,?,?,?)");
            getIdContract(companyOldContr.getSelectedItem().toString());
            double montantHt = Double.parseDouble(montantHTOld.getText().replaceAll(" ", ""));
            double montantTTC = Double.parseDouble(montantOldTTC.getText());
            double retenu = Double.parseDouble(retenuOldFacture.getText());
            String dateFacturation = Rt.format(dateOldFacturation.getDate());
            String dateEnvoi = Rt.format(dateOldDenvoi.getDate());
            if(bissFacture.isSelected()){
                pst.setString(1,numOldFac+"Bis"+"/"+anneFacture);
            }else{
                pst.setString(1,numOldFac+"/"+anneFacture);
            }         
            pst.setDouble(4,montantTTC);
            pst.setDouble(3,retenu);
            pst.setDouble(2,montantHt);
            pst.setString(5,dateFacturation);
            pst.setString(6,dateEnvoi);
            pst.setString(7,etat);
            pst.setString(8,cause);
            pst.setString(9,idOldContract);
            if("choisir Avenant".equals(numAvn)){
                pst.setString(10,null);
            }else{
                pst.setInt(10,getIdAv(companyOldAvn.getSelectedItem().toString() ));
            }
            pst.setString(11,getIdUser);
            pst.setString(12,"");
            pst.executeUpdate();         
            JOptionPane.showMessageDialog(null, "Ajouté Succés");
            affichage(getIdUser);
            affichageNotification(getIdUser);
            getCountNotification();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Numero Facture est deja existe");
            } 
            }else{
                JOptionPane.showMessageDialog(null, "Montant incorrect");
            }    
            //}else{
              //  JOptionPane.showMessageDialog(null, "La date du facture a été envoyée avant la date de début du contrat");
           // }
             
        }else{
           JOptionPane.showMessageDialog(null, "champ vide");  
         }
        contractSearch.removeAllItems();
        contractSearch.addItem("Tous");
        comboBoxContract(contractSearch);
    }//GEN-LAST:event_saveOldFactureMouseClicked

    private void etatOldFactureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_etatOldFactureActionPerformed
        // TODO add your handling code here:
        if(etatOldFacture.getSelectedItem().toString() =="Rejeté"){
            causeOldFacture.setEnabled(true);
        }else{
            causeOldFacture.setEnabled(false);
        }
    }//GEN-LAST:event_etatOldFactureActionPerformed

    private void numFactureSearchCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_numFactureSearchCaretUpdate
        // TODO add your handling code here:
        rechercheFacture();
        getSumHt();
        getSumRetenu();
        getSumTtc();
        getSumTva();
    }//GEN-LAST:event_numFactureSearchCaretUpdate

    private void yearSearchPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_yearSearchPropertyChange
        // TODO add your handling code here:
        
        rechercheFacture();
       
    }//GEN-LAST:event_yearSearchPropertyChange

    private void searchFactureBtnUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchFactureBtnUpdateMouseClicked
        if(checkNumberSearch(searchUpdateFactureInput.getText(), searchAnneUpdateFactureInput.getText())){
        rechercheFactureUpdate("SELECT montant_ht,montant_retenu,(montant_ht - montant_retenu)*(SELECT contracts.tva /100 from contracts where contracts.num_contract = all_factures.num_contract) AS TVA,montant_ttc,date_fact,date_env,etat,cause,name,num_contract,ref_avn,annuler FROM all_factures WHERE idUser =? and num_facture =? and anne = ?");
        getFullNumero = searchUpdateFactureInput.getText()+"/"+searchAnneUpdateFactureInput.getText();
        try {
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            PreparedStatement pst1 = con.prepareStatement("SELECT contracts.retenu,contracts.tva,contracts.date_debut FROM contracts WHERE contracts.num_contract =?");  
            pst1.setString(1,contractUpdateFacture.getText());             
            ResultSet rs1 = pst1.executeQuery();
            while (rs1.next()) {
                getDateDebutUpdateFacture = rs1.getDate("date_debut");
                getRetenuUpdateFacture = rs1.getString("retenu");
                getRetenuUpdateFacture1 = rs1.getString("retenu");
                getTvaUpdateFacture = rs1.getString("tva");  
            }
           
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "contract error");
           
        }
        if(checkNumberInTable(searchUpdateFactureInput.getText(),searchAnneUpdateFactureInput.getText()) ==false){
                searchUpdateFactureInput.setEnabled(true);
                searchAnneUpdateFactureInput.setEnabled(true);
                declareUpdate.setText("Vous pouvez modifier le numéro de facture");
        }else{
                searchUpdateFactureInput.setEnabled(false);
                searchAnneUpdateFactureInput.setEnabled(false);
                declareUpdate.setText("Vous ne pouvez pas modifier le numéro de facture");
        }
        }else{
            JOptionPane.showMessageDialog(null, "cette facture néxiste pas");
        }
    }//GEN-LAST:event_searchFactureBtnUpdateMouseClicked

    private void searchUpdateFactureInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchUpdateFactureInputKeyPressed
        // TODO add your handling code here:
        text_long(searchUpdateFactureInput,10,evt);
    }//GEN-LAST:event_searchUpdateFactureInputKeyPressed

    private void numFactureSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numFactureSearchKeyPressed
        // TODO add your handling code here:
        text_long(numFactureSearch,4,evt);
    }//GEN-LAST:event_numFactureSearchKeyPressed

    private void anneOldFactureKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_anneOldFactureKeyPressed
        // TODO add your handling code here:
        text_long(anneOldFacture,4, evt);
    }//GEN-LAST:event_anneOldFactureKeyPressed

    private void searchAnneUpdateFactureInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchAnneUpdateFactureInputKeyPressed
        // TODO add your handling code here:
        text_long(searchAnneUpdateFactureInput,4, evt);
    }//GEN-LAST:event_searchAnneUpdateFactureInputKeyPressed

    private void saveUpdateFactureMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveUpdateFactureMouseClicked
        String numeroFact [] = getFullNumero.split("/",2);
        if ((!"".equals(montantHTUpdate.getText()))& dateUpdateFacturation.getDate() != null & dateUpdateDenvoi.getDate() !=null){
            //if(dateUpdateFacturation.getDate().after(getDateDebutUpdateFacture) && dateUpdateDenvoi.getDate().after(getDateDebutUpdateFacture)){
            if(montantHTUpdate.getText().replaceAll(" ","").matches("\\d+(.\\d+)?")){
            if(checkNumberInTable(numeroFact[0],numeroFact[1]) ==false){
            if(checkNumberInTable(searchUpdateFactureInput.getText(),searchAnneUpdateFactureInput.getText()) ==false){
            try {
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();           
            int getIdEncFact = getIdEncFacture(getFullNumero);
            String sql = "update encient_facture set montant_ht =? , montant_retenu = ?, montant_ttc = ?, date_fact = ?, date_env = ?,id_cont = ?,ref_avn = ?,etat = ? ,cause =?,num_facture=?,annuler = ?  where id_enc_fact = "+ getIdEncFact ;            
            PreparedStatement preparedStatement =con.prepareStatement(sql);
            SimpleDateFormat Rt = new SimpleDateFormat("yyyy-MM-dd");
            String date_fact = Rt.format(dateUpdateFacturation.getDate());
            String date_env = Rt.format(dateUpdateDenvoi.getDate());
            preparedStatement.setString(3,montantUpdateTTC.getText().replaceAll(" ", ""));
            preparedStatement.setString(1,montantHTUpdate.getText());
            preparedStatement.setString(2,retenuUpdateFacture.getText());
            preparedStatement.setString(4,date_fact);
            preparedStatement.setString(5,date_env);
            getIdContract(companyUpdateContr1.getSelectedItem().toString());
            preparedStatement.setString(6,idUpdateContract);
            if("choisir Avenant".equals(companyUpdateAvn1.getSelectedItem().toString())){
                preparedStatement.setString(7,null);
            }else{
                preparedStatement.setInt(7,getIdAv(companyUpdateAvn1.getSelectedItem().toString()));
            }          
            preparedStatement.setString(8,etatTheFacture1.getSelectedItem().toString());
            preparedStatement.setString(9,causeUpdateFacture.getText());
            String fullNumber;
            if(bisCheck.isSelected()){
                 fullNumber = searchUpdateFactureInput.getText()+"Bis"+"/"+searchAnneUpdateFactureInput.getText();    
            }else{
                 fullNumber = searchUpdateFactureInput.getText()+"/"+searchAnneUpdateFactureInput.getText();    
            }
            
            preparedStatement.setString(10,fullNumber);
            if(annulerCheck.isSelected()){
                preparedStatement.setString(11,"Annuler");
            }else{
                preparedStatement.setString(11,"");
            }
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Modification est succés");           
            affichage(getIdUser);
        } catch (SQLException ex) {
            //Logger.getLogger(facturation.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Le numero est déja exister");  
        }   
            }else{
            JOptionPane.showMessageDialog(null, "Le numero est déja exister"); 
            
            }    
            }else{
                
            try {
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            String sql = "update facture set montant_ht =? , montant_retenu = ?, montant_ttc = ?, date_fact = ?, date_env = ?,id_cont = ?,ref_avn = ?,etat = ? ,cause =?,annuler = ?  where num_facture = ? and anne = ?" ;            
            PreparedStatement preparedStatement =con.prepareStatement(sql);
            SimpleDateFormat Rt = new SimpleDateFormat("yyyy-MM-dd");
            String date_fact = Rt.format(dateUpdateFacturation.getDate());
            String date_env = Rt.format(dateUpdateDenvoi.getDate());
            preparedStatement.setString(3,montantUpdateTTC.getText().replaceAll(" ", ""));
            preparedStatement.setString(1,montantHTUpdate.getText());
            preparedStatement.setString(2,retenuUpdateFacture.getText());
            preparedStatement.setString(4,date_fact);
            preparedStatement.setString(5,date_env);
            getIdContract(companyUpdateContr1.getSelectedItem().toString());
            preparedStatement.setString(6,idUpdateContract);
            if("choisir Avenant".equals(companyUpdateAvn1.getSelectedItem().toString()) || "".equals(companyUpdateAvn1.getSelectedItem().toString())){
                preparedStatement.setString(7,null);
            }else{
                preparedStatement.setString(7,companyUpdateAvn1.getSelectedItem().toString());
            }          
            preparedStatement.setString(8,etatTheFacture1.getSelectedItem().toString());
            preparedStatement.setString(9,causeUpdateFacture.getText());
            preparedStatement.setString(11,searchUpdateFactureInput.getText());
            preparedStatement.setString(12,searchAnneUpdateFactureInput.getText());
            if(annulerCheck.isSelected()){
                preparedStatement.setString(10,"Annuler");
            }else{
                preparedStatement.setString(10,"");
            }
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Modification est succés");
            
            affichage(getIdUser);
        } catch (SQLException ex) {
            Logger.getLogger(facturation.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            }
            
         }else{
                JOptionPane.showMessageDialog(null, "Montant incorrect");
            }
            //}else{
            //JOptionPane.showMessageDialog(null, "La date du facture a été envoyée avant la date de début du contrat");  
            //}   
        }else{
            JOptionPane.showMessageDialog(null, "Champ vide"); 
        
        }
        contractSearch.removeAllItems();
        contractSearch.addItem("Tous");
        comboBoxContract(contractSearch);
    }//GEN-LAST:event_saveUpdateFactureMouseClicked

    private void montantHTUpdateCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_montantHTUpdateCaretUpdate
        // TODO add your handling code here:
        //old calcule with ttc to ht
        /*double montantTTC = 0;
            String montantHt = montantHTUpdate.getText().replaceAll(" ","");
            if(montantHTUpdate.getText().length()>0){ 
             montantTTC = Double.parseDouble(montantHt);
            }
            if( getRetenuUpdateFacture !=null){
                double retenuPer = Double.parseDouble(getRetenuUpdateFacture)/100;
                double ht = montantTTC/(1-retenuPer);
                String montantTtcTotal = String.format("%.2f",ht) ;
                String montantRetenu = String.format("%.2f", ht*retenuPer);
                montantTtcTotal = montantTtcTotal.replace(",", ".");
                montantRetenu = montantRetenu.replace(",", ".");
                montantUpdateTTC.setText(montantTtcTotal);
                retenuUpdateFacture.setText(montantRetenu);
            }
        */
        //new calcule with ht to ttc
        double montantHTCalc = 0;
        //String contact = companyUpdateFacture1.getSelectedItem().toString();
        if(getRetenuUpdateFacture != null){          
            String montantHtInput = montantHTUpdate.getText();
            if(montantHTUpdate.getText().length()>0){
                montantHtInput = montantHtInput.replaceAll(" ","");
                montantHTCalc = Double.parseDouble(montantHtInput);          
            }
            double retenuPer =(double) Double.parseDouble(getRetenuUpdateFacture)/100; 
            double tvaPer =(double) Double.parseDouble(getTvaUpdateFacture)/100; 
            String montantTTCTotal = String.format("%.2f", montantHTCalc - ( montantHTCalc*retenuPer) +((montantHTCalc -(montantHTCalc*retenuPer))*tvaPer) );
            String montantRetenu = String.format("%.2f", montantHTCalc*retenuPer);
            String montantTva = String.format("%.2f", (montantHTCalc -(montantHTCalc*retenuPer))*tvaPer);
            montantTTCTotal = montantTTCTotal.replace(",", ".");
            montantRetenu = montantRetenu.replace(",", ".");
            montantTva = montantTva.replace(",", ".");
            montantUpdateTTC.setText(montantTTCTotal);
            retenuUpdateFacture.setText(montantRetenu);
            tvaUpdateFacture.setText(montantTva);
        }
    }//GEN-LAST:event_montantHTUpdateCaretUpdate

    private void montantHTUpdateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_montantHTUpdateKeyPressed
        // TODO add your handling code here:
        //text_long(montantHTUpdate, 10, evt);
    }//GEN-LAST:event_montantHTUpdateKeyPressed

    private void saveNewFactureMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveNewFactureMouseClicked
        // TODO add your handling code here:
        int maxNumeroFacture = getMaxNumFacture();
        
        maxNumeroFacture++;
        int yearNow = Year.now().getValue();
        ///////////////////////////
        String numAvn = companyNewAvn.getSelectedItem().toString();
        
        String etat = "Envoyé";
        String cause = "";
        if ((!"".equals(montantNewHT.getText()))& dateNewFacturation.getDate() != null & dateNewDenvoi.getDate() !=null){
            //if(dateNewFacturation.getDate().after(getDateDebutNewFacture) && dateNewDenvoi.getDate().after(getDateDebutNewFacture)){
            if(montantNewHT.getText().replaceAll(" ","").matches("\\d+(.\\d+)?")){
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            PreparedStatement pst;
            try {
                double montantHt = Double.parseDouble(montantNewHT.getText().replaceAll(" ",""));
                double montantTTC = Double.parseDouble(montantNewTTC.getText());
                double retenu = Double.parseDouble(retenuNewFacture.getText());
                SimpleDateFormat Rt = new SimpleDateFormat("yyyy-MM-dd");
                String dateFacturation = Rt.format(dateNewFacturation.getDate());
                String dateEnvoi = Rt.format(dateNewDenvoi.getDate());
            pst = con.prepareStatement("INSERT INTO facture (num_facture,anne,montant_ht,montant_retenu,montant_ttc,date_fact,date_env,etat,cause,id_cont,ref_avn,idUser,annuler) VALUE(?,?,?,?,?,?,?,?,?,?,?,?,?)");
            getIdContract(companyNewContr.getSelectedItem().toString());
            pst.setInt(1,maxNumeroFacture);
            pst.setInt(2,yearNow);
            pst.setDouble(5,montantTTC);
            pst.setDouble(4,retenu);
            pst.setDouble(3,montantHt);
            pst.setString(6,dateFacturation);
            pst.setString(7,dateEnvoi);
            pst.setString(8,etat);
            pst.setString(9,cause);
            pst.setString(10,idNewContract);
            if("choisir Avenant".equals(numAvn)){
                pst.setString(11,null);
            }else{
                pst.setInt(11,getIdAv(companyNewAvn.getSelectedItem().toString()));
            }
            pst.setString(12,getIdUser);
            pst.setString(13,"");
            pst.executeUpdate();         
            JOptionPane.showMessageDialog(null, "Ajouté Succés et votre numero du facture est : "+maxNumeroFacture + "/" + yearNow );
            
            numFactureNew.setText(maxNumeroFacture + "/" + yearNow);
            affichage(getIdUser);
            }   catch (SQLException ex) { 
                    Logger.getLogger(facturation.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }else{
                JOptionPane.showMessageDialog(null, "Montant incorrect");
            }
            //}else{
            //JOptionPane.showMessageDialog(null, "La date du facture a été envoyée avant la date de début du contrat");  
            //}
            
            
        }else{
           JOptionPane.showMessageDialog(null, "champ vide");  
         }
        contractSearch.removeAllItems();
        contractSearch.addItem("Tous");
        comboBoxContract(contractSearch);
    }//GEN-LAST:event_saveNewFactureMouseClicked

    private void montantNewHTKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_montantNewHTKeyPressed
        // TODO add your handling code here:
        
        text_long(montantNewHT,15,evt);
    }//GEN-LAST:event_montantNewHTKeyPressed

    private void montantNewHTCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_montantNewHTCaretUpdate
        // TODO add your handling code here:
        //old calcule with montant ttc to ht------------
        /*double montantTTC = 0;
        String contact = companyNewContr.getSelectedItem().toString();
        if(getretenuNewFacture != null && contact != "choisir contract"){          
            String montantHt = montantNewHT.getText();
            if(montantNewHT.getText().length()>0){
                montantHt = montantHt.replaceAll(" ","");
                montantTTC = Double.parseDouble(montantHt);
               
            }
            double retenuPer =(double) Double.parseDouble(getretenuNewFacture)/100; 
            double ht = montantTTC/(1-retenuPer);
            
            String montantHTTotal = String.format("%.2f", ht);
            String montantRetenu = String.format("%.2f", ht*retenuPer);
            montantHTTotal = montantHTTotal.replace(",", ".");
            montantRetenu = montantRetenu.replace(",", ".");
            montantNewTTC.setText(montantHTTotal);
            retenuNewFacture.setText(montantRetenu);
        }else{
            JOptionPane.showMessageDialog(null, "Choisse Contract");
        }
        */
        //new calcule with ht to ttc
        double montantHTCalc = 0;
        String contact = companyNewContr.getSelectedItem().toString();
        if(getretenuNewFacture != null && contact != "choisir contract"){          
            String montantHtInput = montantNewHT.getText();
            if(montantNewHT.getText().length()>0){
                montantHtInput = montantHtInput.replaceAll(" ","");
                montantHTCalc = Double.parseDouble(montantHtInput);          
            }
            
            double retenuPer =(double) Double.parseDouble(getretenuNewFacture)/100; 
            double tvaPer =(double) Double.parseDouble(getTvaNewFacture)/100; 
            
            String montantTTCTotal = String.format("%.2f", montantHTCalc - ( montantHTCalc*retenuPer) +((montantHTCalc -(montantHTCalc*retenuPer))*tvaPer) );
            String montantRetenu = String.format("%.2f", montantHTCalc*retenuPer);
            String montantTva = String.format("%.2f", (montantHTCalc -(montantHTCalc*retenuPer))*tvaPer);
            montantTTCTotal = montantTTCTotal.replace(",", ".");
            montantRetenu = montantRetenu.replace(",", ".");
            montantTva = montantTva.replace(",", ".");
            montantNewTTC.setText(montantTTCTotal);
            retenuNewFacture.setText(montantRetenu);
            tvaNewFacture.setText(montantTva);
        }else{
            JOptionPane.showMessageDialog(null, "Choisse Contract");
        }
    }//GEN-LAST:event_montantNewHTCaretUpdate

    private void copyNumFactureMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_copyNumFactureMouseClicked
        // TODO add your handling code here:
        String str = numFactureNew.getText();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
	Clipboard clipboard = toolkit.getSystemClipboard();
	StringSelection strSel = new StringSelection(str);
	clipboard.setContents(strSel, null);
    }//GEN-LAST:event_copyNumFactureMouseClicked

    private void notificationBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_notificationBtnMouseClicked
        // TODO add your handling code here:
         mainFacturePanel.setVisible(false);
        addFacturePanel.setVisible(false);
        updateFacturePanel.setVisible(false);
        oldFacturePanel.setVisible(false);
        notificationPanel.setVisible(true);
        affichageNotification(getIdUser);
        getCountNotification();
    }//GEN-LAST:event_notificationBtnMouseClicked

    private void listFactureTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listFactureTableMouseClicked
        // TODO add your handling code here:
        clickTableFacture = true;
        montantHTUpdate.requestFocus();
        int i = listFactureTable.getSelectedRow();
        TableModel m = listFactureTable.getModel();
        getFullNumero = m.getValueAt(i, 3).toString();
        ///////
        String numeroFact [] = getFullNumero.split("/",2);
        if(numeroFact[0].contains("Bis")){
            searchUpdateFactureInput.setText(numeroFact[0].substring(0,numeroFact[0].indexOf("Bis") ));
            bisCheck.setSelected(true);
        }else{
            searchUpdateFactureInput.setText(numeroFact[0]);    
            bisCheck.setSelected(false);
        }
        
        searchAnneUpdateFactureInput.setText(numeroFact[1]);
        companyUpdateFacture1.setSelectedItem(m.getValueAt(i, 0).toString());
        companyUpdateContr1.setSelectedItem(m.getValueAt(i, 1).toString());
        contractUpdateFacture.setText(m.getValueAt(i, 1).toString());
        getIdContract(m.getValueAt(i, 1).toString());
        if(m.getValueAt(i, 2) !=null){   
            companyUpdateAvn1.setSelectedItem(m.getValueAt(i, 2).toString());
        }
        montantHTUpdate.setText(m.getValueAt(i, 4).toString().replaceAll(",",""));
        tvaUpdateFacture.setText(m.getValueAt(i, 6).toString().replaceAll(",",""));
        montantUpdateTTC.setText(m.getValueAt(i, 7).toString().replaceAll(",",""));
        retenuUpdateFacture.setText(m.getValueAt(i, 5).toString().replaceAll(",",""));
        etatTheFacture1.setSelectedItem(m.getValueAt(i, 10).toString());
        causeUpdateFacture.setText(m.getValueAt(i, 11).toString());
        if(m.getValueAt(i, 12) !=null){
            annulerCheck.setSelected(true);
        }else{
            annulerCheck.setSelected(false);
        }
        java.util.Date datefact,dateenv;
        try {
            datefact = new SimpleDateFormat("yyyy-MM-dd").parse(m.getValueAt(i, 8).toString());
            dateUpdateFacturation.setDate(datefact);
        } catch (ParseException ex) {
            Logger.getLogger(facturation.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            dateenv = new SimpleDateFormat("yyyy-MM-dd").parse(m.getValueAt(i, 9).toString());
            dateUpdateDenvoi.setDate(dateenv);
        } catch (ParseException ex) {
            Logger.getLogger(facturation.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(checkNumberInTable(numeroFact[0],numeroFact[1]) ==false){
                searchUpdateFactureInput.setEnabled(true);
                searchAnneUpdateFactureInput.setEnabled(true);
                declareUpdate.setText("Vous pouvez modifier le numéro de facture");
        }else{
                searchUpdateFactureInput.setEnabled(false);
                searchAnneUpdateFactureInput.setEnabled(false);
                declareUpdate.setText("Vous ne pouvez pas modifier le numéro de facture");
        }
        ///////////////////
        try {
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            PreparedStatement pst1 = con.prepareStatement("SELECT contracts.retenu,contracts.tva,contracts.date_debut FROM contracts WHERE contracts.num_contract =?");  
            pst1.setString(1,m.getValueAt(i, 1).toString());             
            ResultSet rs1 = pst1.executeQuery();
            while (rs1.next()) {
                getRetenuUpdateFacture = rs1.getString("retenu");
                getRetenuUpdateFacture1 = rs1.getString("retenu");
                getTvaUpdateFacture = rs1.getString("tva");
                getDateDebutUpdateFacture = rs1.getDate("date_debut");
            }
           
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
           
        }
    }//GEN-LAST:event_listFactureTableMouseClicked

    private void backNotificationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backNotificationMouseClicked
        // TODO add your handling code here:
        mainFacturePanel.setVisible(true);
        addFacturePanel.setVisible(false);
        updateFacturePanel.setVisible(false);
        oldFacturePanel.setVisible(false);
        notificationPanel.setVisible(false);
    }//GEN-LAST:event_backNotificationMouseClicked

    private void backNewFacture1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backNewFacture1MouseClicked
        // TODO add your handling code here:
        numFactureNew.setText("");
        montantNewHT.setText("");
        retenuNewFacture.setText("");
        montantNewTTC.setText("");
        dateNewFacturation.setCalendar(null);
        dateNewDenvoi.setCalendar(null);
    }//GEN-LAST:event_backNewFacture1MouseClicked

    private void autreFactureUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autreFactureUpdateMouseClicked
        // TODO add your handling code here:
        searchUpdateFactureInput.setText("");
        searchAnneUpdateFactureInput.setText("");
        montantHTUpdate.setText("");
        retenuUpdateFacture.setText("");
        montantUpdateTTC.setText("");
        companyUpdateFacture.setText("");
        contractUpdateFacture.setText("");
        avnantUpdateFacture.setText("");
        etatFactureUpdate.setText("");
        causeUpdateFacture.setText("");
        dateUpdateFacturation.setCalendar(null);
        dateUpdateDenvoi.setCalendar(null);
        searchUpdateFactureInput.setEnabled(true);
        searchAnneUpdateFactureInput.setEnabled(true);
        annulerCheck.setSelected(false);
    }//GEN-LAST:event_autreFactureUpdateMouseClicked

    private void backNewFacture4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backNewFacture4MouseClicked
        // TODO add your handling code here:
        numOldFacture.setText("");
        anneOldFacture.setText("");
        montantHTOld.setText("");
        retenuOldFacture.setText("");
        montantOldTTC.setText("");
        causeOldFacture.setText("");
        dateOldFacturation.setCalendar(null);
        dateOldDenvoi.setCalendar(null);

    }//GEN-LAST:event_backNewFacture4MouseClicked

    private void companyNewContrFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_companyNewContrFocusGained
        // TODO add your handling code here:
        companyNewContr.showPopup();
    }//GEN-LAST:event_companyNewContrFocusGained

    private void companyNewAvnFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_companyNewAvnFocusGained
        // TODO add your handling code here:
        companyNewAvn.showPopup();
    }//GEN-LAST:event_companyNewAvnFocusGained

    private void companyOldContrFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_companyOldContrFocusGained
        // TODO add your handling code here:
        companyOldContr.showPopup();
    }//GEN-LAST:event_companyOldContrFocusGained

    private void companyOldAvnFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_companyOldAvnFocusGained
        // TODO add your handling code here:
        companyOldAvn.showPopup();
    }//GEN-LAST:event_companyOldAvnFocusGained

    private void notificationTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_notificationTableMouseClicked
        // TODO add your handling code here:
        int i = notificationTable.getSelectedRow();
        TableModel m = notificationTable.getModel();
        String fullNumber = m.getValueAt(i, 3).toString();
        String numeroFact [] = fullNumber.split("/",2);
        int confirm = JOptionPane.showConfirmDialog(this,"voulez vous modife l'etat des facture a payée","confirme",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(confirm ==JOptionPane.YES_OPTION){
            if(checkNumberInTableNot() ==false){
                try {
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            String sql = "update encient_facture set etat ='Payée' where num_facture = ?" ;
            
            PreparedStatement preparedStatement =con.prepareStatement(sql);
            preparedStatement.setString(1,fullNumber);
            
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Modification est succés");
            
            affichage(getIdUser);
            affichageNotification(getIdUser);
            getCountNotification();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Errour"); 
        }
                
            }else{
            try {
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            String sql = "update facture set etat ='Payée' where num_facture = ? and anne = ?" ;
            
            PreparedStatement preparedStatement =con.prepareStatement(sql);
            preparedStatement.setString(1,numeroFact[0]);
            preparedStatement.setString(2,numeroFact[1]);
            
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Modification est succés");
            
            affichage(getIdUser);
            affichageNotification(getIdUser);
            getCountNotification();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Errour"); 
        }
            
            }
            
        }
                  
    }//GEN-LAST:event_notificationTableMouseClicked

    private void backNewFacture1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_backNewFacture1FocusGained
        // TODO add your handling code here:
        
    }//GEN-LAST:event_backNewFacture1FocusGained

    private void addFacturePanelKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_addFacturePanelKeyTyped
        // TODO add your handling code here:
          Action refreshAction = new AbstractAction("Refresh"){
            @Override
            public void actionPerformed(ActionEvent e) {
                numFactureNew.setText("");
                montantNewHT.setText("");
                retenuNewFacture.setText("");
                montantNewTTC.setText("");
                dateNewFacturation.setCalendar(null);
                dateNewDenvoi.setCalendar(null);
            }
        
        };
        String key = "Refresh";
        backNewFacture1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R,KeyEvent.CTRL_MASK), key);
        backNewFacture1.getActionMap().put(key, refreshAction);

        ///////////////////////////
            Action saveAction = new AbstractAction("Save"){
            @Override
            public void actionPerformed(ActionEvent e) {
                
                System.out.println("Save");
            }
        
        };
        String keyS = "Save";
        saveNewFacture.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,KeyEvent.CTRL_MASK), keyS);
        saveNewFacture.getActionMap().put(keyS, saveAction);

    }//GEN-LAST:event_addFacturePanelKeyTyped

    private void companyUpdateFacture1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_companyUpdateFacture1ActionPerformed
        // TODO add your handling code here:
        companyUpdateContr1.removeAllItems();
        companyUpdateContr1.addItem("choisir contract");
        try {
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            //PreparedStatement pst = con.prepareStatement("SELECT DISTINCT contracts.num_contract FROM contracts,enterprises WHERE contracts.id_ent = enterprises.id and enterprises.name =? ");
            PreparedStatement pst = con.prepareStatement("SELECT DISTINCT contracts.num_contract FROM contracts,enterprises,role,user WHERE contracts.id_ent = enterprises.id and user.id = role.id_user and role.id_contract = contracts.id_cont and enterprises.name =? and user.id = ?");
            if(companyUpdateFacture1.getSelectedItem() !=null){
            String selected = companyUpdateFacture1.getSelectedItem().toString();
            
                pst.setString(1,selected);
            }           
            pst.setString(2,getIdUser);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString("num_contract");
                companyUpdateContr1.addItem(name);
            }
        } catch (HeadlessException | SQLException e) {
           // JOptionPane.showMessageDialog(null, e);
           
        }

    }//GEN-LAST:event_companyUpdateFacture1ActionPerformed

    private void companyUpdateContr1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_companyUpdateContr1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_companyUpdateContr1FocusGained

    private void companyUpdateContr1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_companyUpdateContr1ActionPerformed
        // TODO add your handling code here:
               
        companyUpdateAvn1.removeAllItems();
        companyUpdateAvn1.addItem("choisir Avenant");
        try {
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            PreparedStatement pst = con.prepareStatement("SELECT avenant.avenant_num,contracts.retenu FROM avenant,contracts WHERE avenant.ref_cont = contracts.id_cont and contracts.num_contract =?");
            if(companyUpdateContr1.getSelectedItem() !=null){
            String selected = companyUpdateContr1.getSelectedItem().toString();   
                pst.setString(1,selected);
            }  
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString("avenant_num");
                companyUpdateAvn1.addItem(name);
            }
           
        } catch (HeadlessException | SQLException e) {
           // JOptionPane.showMessageDialog(null, e);
           
        }
        // get TVA and Retenu from cotract
        try {
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            PreparedStatement pst1 = con.prepareStatement("SELECT contracts.retenu,contracts.tva,contracts.date_debut FROM contracts WHERE contracts.num_contract =?");
             if(companyUpdateContr1.getSelectedItem() !=null){
                String selected = companyUpdateContr1.getSelectedItem().toString();   
                pst1.setString(1,selected);
             }
             
            ResultSet rs1 = pst1.executeQuery();
            while (rs1.next()) {
                getRetenuUpdateFacture = rs1.getString("retenu");
                getRetenuUpdateFacture1 = rs1.getString("retenu");
                getTvaUpdateFacture = rs1.getString("tva");
                getDateDebutUpdateFacture = rs1.getDate("date_debut");
            }
           
        } catch (HeadlessException | SQLException e) {
           // JOptionPane.showMessageDialog(null, e);
           
        }

    }//GEN-LAST:event_companyUpdateContr1ActionPerformed

    private void companyUpdateAvn1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_companyUpdateAvn1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_companyUpdateAvn1FocusGained

    private void etatTheFacture1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_etatTheFacture1ActionPerformed
        // TODO add your handling code here:
                String etat = etatTheFacture1.getSelectedItem().toString();
        if(etat =="Rejeté"){
            causeUpdateFacture.setEnabled(true);
        }else{
            causeUpdateFacture.setEnabled(false);
        }

    }//GEN-LAST:event_etatTheFacture1ActionPerformed

    private void userMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userMenuMouseClicked
        // TODO add your handling code here:
        menuPanel.setSize(332,250);
    }//GEN-LAST:event_userMenuMouseClicked

    private void userMenuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userMenuMouseEntered
        // TODO add your handling code here:
        menuPanel.setSize(332,250);
    }//GEN-LAST:event_userMenuMouseEntered

    private void userMenuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userMenuMouseExited
        // TODO add your handling code here:
        menuPanel.setSize(332,0);
    }//GEN-LAST:event_userMenuMouseExited

    private void menuPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuPanelMouseEntered
        // TODO add your handling code here:
        menuPanel.setSize(332,250);
    }//GEN-LAST:event_menuPanelMouseEntered

    private void menuPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuPanelMouseExited
        // TODO add your handling code here:
        menuPanel.setSize(0,0);
    }//GEN-LAST:event_menuPanelMouseExited

    private void logOutBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logOutBtnMouseClicked
        // TODO add your handling code here:
        this.dispose();
        Login login = new Login();
        login.setVisible(true);
    }//GEN-LAST:event_logOutBtnMouseClicked

    private void totalRetenuBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_totalRetenuBtnMouseClicked
        // TODO add your handling code here:
        totalRetenuDialog.setVisible(true);
        affichageTotalRetenu(getIdUser);
    }//GEN-LAST:event_totalRetenuBtnMouseClicked

    private void changePasswordBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_changePasswordBtnMouseClicked
        // TODO add your handling code here:
        changePasswordDialog.setVisible(true);
       
    }//GEN-LAST:event_changePasswordBtnMouseClicked

    private void changePasswordDialogWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_changePasswordDialogWindowOpened
        // TODO add your handling code here:
        changePasswordDialog.setLocationRelativeTo(null);
    }//GEN-LAST:event_changePasswordDialogWindowOpened

    private void close1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_close1MouseClicked
        // TODO add your handling code here:
        //System.exit(0);
        changePasswordDialog.dispose();
    }//GEN-LAST:event_close1MouseClicked

    private void annulerFactureMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_annulerFactureMouseClicked

        if(clickTableFacture ==true){
            String numeroFact [] = getFullNumero.split("/",2);
        int confirm = JOptionPane.showConfirmDialog(this,"voulez vous modife l'etat des facture a annuler","confirme",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(confirm ==JOptionPane.YES_OPTION){    
        if(checkNumberInTable(numeroFact[0],numeroFact[1])==false){
                try {
                Connecter conn=new Connecter();
                Connection con=conn.ObteneurConction();
                String sql = "update encient_facture set annuler = 'Annuler' where  num_facture = ?";           
                PreparedStatement preparedStatement = con.prepareStatement(sql);           
                preparedStatement.setString(1,getFullNumero);
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Annuler avec succès");
                //affichage(getIdUser);
                rechercheFacture();
                clickTableFacture = false;
            } catch (SQLException ex) {
                Logger.getLogger(facturation.class.getName()).log(Level.SEVERE, null, ex);
            }         
            }else{
                try {
                Connecter conn=new Connecter();
                Connection con=conn.ObteneurConction();
                String sql = "update facture set annuler = 'Annuler' where  num_facture = ? AND anne =?";
                PreparedStatement preparedStatement = con.prepareStatement(sql);           
                preparedStatement.setString(1, numeroFact[0]);
                preparedStatement.setString(2, numeroFact[1]);
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Annuler avec succès");
                //affichage(getIdUser);
                rechercheFacture();
                clickTableFacture = false;
            } catch (SQLException ex) {
                Logger.getLogger(facturation.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            }
        }
        }else{
            JOptionPane.showMessageDialog(null, "Choisissez une facture à Annuler");             
        }

    }//GEN-LAST:event_annulerFactureMouseClicked

    private void totalRetenuBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_totalRetenuBtnMouseEntered
        // TODO add your handling code here:
        menuPanel.setSize(332,250);
    }//GEN-LAST:event_totalRetenuBtnMouseEntered

    private void totalRetenuBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_totalRetenuBtnMouseExited
        // TODO add your handling code here:
        menuPanel.setSize(0,250);
    }//GEN-LAST:event_totalRetenuBtnMouseExited

    private void changePasswordBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_changePasswordBtnMouseEntered
        // TODO add your handling code here:
        menuPanel.setSize(332,250);
    }//GEN-LAST:event_changePasswordBtnMouseEntered

    private void changePasswordBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_changePasswordBtnMouseExited
        // TODO add your handling code here:
        menuPanel.setSize(0,250);
    }//GEN-LAST:event_changePasswordBtnMouseExited

    private void logOutBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logOutBtnMouseEntered
        // TODO add your handling code here:
        menuPanel.setSize(332,250);
    }//GEN-LAST:event_logOutBtnMouseEntered

    private void logOutBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logOutBtnMouseExited
        // TODO add your handling code here:
        menuPanel.setSize(0,250);
    }//GEN-LAST:event_logOutBtnMouseExited

    private void saveConfirmPasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveConfirmPasswordMouseClicked
        // TODO add your handling code here:
        if(passwordChanged.getText().equals(confirmPassword.getText())){
            try {
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            String sql = "update user set password =? where id =" +getIdUser ;
            PreparedStatement preparedStatement =con.prepareStatement(sql);
            //preparedStatement.setString(1,username.getText());
            preparedStatement.setString(1,passwordChanged.getText());
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Modification est succés");
            passwordChanged.setText("");
            confirmPassword.setText("");
        } catch (SQLException ex) {
            Logger.getLogger(admin.class.getName()).log(Level.SEVERE, null, ex);
        }

        }else{
            JOptionPane.showMessageDialog(null, "le mot de passe ne correspond pas");
        }
    }//GEN-LAST:event_saveConfirmPasswordMouseClicked

    private void totalRetenuDialogWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_totalRetenuDialogWindowOpened
        // TODO add your handling code here:
        totalRetenuDialog.setLocationRelativeTo(null);
    }//GEN-LAST:event_totalRetenuDialogWindowOpened

    private void backTotalRetenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backTotalRetenuMouseClicked
        // TODO add your handling code here:
        totalRetenuDialog.dispose();
    }//GEN-LAST:event_backTotalRetenuMouseClicked

    private void annulerFactureMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_annulerFactureMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_annulerFactureMouseEntered

    private void montantNewHTKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_montantNewHTKeyReleased
        // TODO add your handling code here:
        /*double montantTTC = 0;
        String contact = companyNewContr.getSelectedItem().toString();
        if(getretenuNewFacture != null && contact != "chosse contract"){          
            String montantHt = montantNewHT.getText();
            if(montantNewHT.getText().length()>0){
                montantTTC = Double.parseDouble(montantHt);
            }
            double retenuPer =(double) Double.parseDouble(getretenuNewFacture)/100; 
            double ht = montantTTC/(1-retenuPer);
            String montantHTTotal = String.format("%.2f", ht);
            String montantRetenu = String.format("%.2f", ht*retenuPer);
            montantHTTotal = montantHTTotal.replace(",", ".");
            montantRetenu = montantRetenu.replace(",", ".");
            montantNewTTC.setText(montantHTTotal);
            retenuNewFacture.setText(montantRetenu);
        }else{
            JOptionPane.showMessageDialog(null, "Choisse Contract");
        } */     
    }//GEN-LAST:event_montantNewHTKeyReleased

    private void montantHTOldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_montantHTOldKeyReleased
        // TODO add your handling code here:
        /*double montantTTC = 0;
        String contact = companyOldContr.getSelectedItem().toString();
        if(retenuInOldFac != null && contact != "chosse contract"){
            String montantHt = montantHTOld.getText();
            if(montantHTOld.getText().length()>0){
             montantTTC = Double.parseDouble(montantHt);
            }           
            double retenuPer = Double.parseDouble(retenuInOldFac)/100;
            double ht = montantTTC/(1-retenuPer);
            String montantTtcTotal = String.format("%.2f",ht) ;
            String montantRetenu = String.format("%.2f",ht*retenuPer);        
            montantTtcTotal = montantTtcTotal.replace(",", ".");
            montantRetenu = montantRetenu.replace(",", ".");
            montantOldTTC.setText(montantTtcTotal);
            retenuOldFacture.setText(montantRetenu);
        }else{
            JOptionPane.showMessageDialog(null, "Choisse Contract");
        }*/
    }//GEN-LAST:event_montantHTOldKeyReleased

    private void montantHTUpdateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_montantHTUpdateKeyReleased
        // TODO add your handling code here:
        /*double montantTTC = 0;
            String montantHt = montantHTUpdate.getText();
            if(montantHTUpdate.getText().length()>0){
             montantTTC = Double.parseDouble(montantHt);
            }
            if( getRetenuUpdateFacture !=null){
                double retenuPer = Double.parseDouble(getRetenuUpdateFacture)/100;
                double ht = montantTTC/(1-retenuPer);
                String montantTtcTotal = String.format("%.2f",ht) ;
                String montantRetenu = String.format("%.2f", ht*retenuPer);
                montantTtcTotal = montantTtcTotal.replace(",", ".");
                montantRetenu = montantRetenu.replace(",", ".");
                montantUpdateTTC.setText(montantTtcTotal);
                retenuUpdateFacture.setText(montantRetenu);
            }*/
    }//GEN-LAST:event_montantHTUpdateKeyReleased

    private void etatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_etatActionPerformed
        // TODO add your handling code here:
        rechercheFacture();
        getSumHt();  
        getSumRetenu();
        getSumTtc();
        getSumTva();
    }//GEN-LAST:event_etatActionPerformed

    private void contractSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contractSearchFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_contractSearchFocusGained

    private void contractSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contractSearchActionPerformed
        // TODO add your handling code here:
        rechercheFacture();
        getSumHt();
        getSumRetenu();
        getSumTtc();
        getSumTva();
    }//GEN-LAST:event_contractSearchActionPerformed

    private void companyNewAvnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_companyNewAvnActionPerformed
        // TODO add your handling code here:
/*        try {
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            PreparedStatement pst = con.prepareStatement("SELECT avenant.ref_av FROM avenant,contracts WHERE contracts.id_cont = avenant.ref_cont and avenant.avenant_num =? AND avenant.ref_cont = ?");
            if(companyNewAvn.getSelectedItem() !=null){
            String selected = companyNewAvn.getSelectedItem().toString();   
                pst.setString(1,selected);
                
                pst.setString(2,idNewContract);
            }  
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("ref_av"));
            }
           
        } catch (HeadlessException | SQLException e) {
           // JOptionPane.showMessageDialog(null, e);
           
        }
*/
    }//GEN-LAST:event_companyNewAvnActionPerformed

    private void montantNewHTKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_montantNewHTKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if(Character.isLetter(c)){
            evt.consume();
        }else{
            try{
                Double.parseDouble(montantNewHT.getText()+ evt.getKeyChar());
            }catch(NumberFormatException e){
                evt.consume();
            }
        }
    }//GEN-LAST:event_montantNewHTKeyTyped

    private void montantHTUpdateKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_montantHTUpdateKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if(Character.isLetter(c)){
            evt.consume();
        }else{
            try{
                Double.parseDouble(montantHTUpdate.getText()+ evt.getKeyChar());
            }catch(NumberFormatException e){
                evt.consume();
            }
        }
    }//GEN-LAST:event_montantHTUpdateKeyTyped

    private void montantHTOldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_montantHTOldKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if(Character.isLetter(c)){
            evt.consume();
        }else{
            try{
                Double.parseDouble(montantHTOld.getText()+ evt.getKeyChar());
            }catch(NumberFormatException e){
                evt.consume();
            }
        }
    }//GEN-LAST:event_montantHTOldKeyTyped

    private void yearSearchCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_yearSearchCaretPositionChanged
        // TODO add your handling code here:
        getSumHt();
        getSumRetenu();
        getSumTtc();
        getSumTva();
    }//GEN-LAST:event_yearSearchCaretPositionChanged

    private void montantHTUpdateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_montantHTUpdateFocusGained
        // TODO add your handling code here:
        montantHTUpdate.setEditable(true);
    }//GEN-LAST:event_montantHTUpdateFocusGained

    private void deleteFactureBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteFactureBtnMouseClicked
        // TODO add your handling code here:
        if(clickTableFacture ==true){
            String numeroFact [] = getFullNumero.split("/",2);
        int confirm = JOptionPane.showConfirmDialog(this,"Voulez-vous supprimer cette facture ?","confirme",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(confirm ==JOptionPane.YES_OPTION){    
        if(checkNumberInTable(numeroFact[0],numeroFact[1])==false){
                try {
                Connecter conn=new Connecter();
                Connection con=conn.ObteneurConction();
                String sql = "delete from  encient_facture where  num_facture = ?";           
                PreparedStatement preparedStatement = con.prepareStatement(sql);           
                preparedStatement.setString(1,getFullNumero);
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "supprimer avec succès");
                //affichage(getIdUser);
                rechercheFacture();
                clickTableFacture = false;
            } catch (SQLException ex) {
                Logger.getLogger(facturation.class.getName()).log(Level.SEVERE, null, ex);
            }         
            }else{
                try {
                Connecter conn=new Connecter();
                Connection con=conn.ObteneurConction();
                String sql = "delete from  facture  where  num_facture = ? AND anne =?";
                PreparedStatement preparedStatement = con.prepareStatement(sql);           
                preparedStatement.setString(1, numeroFact[0]);
                preparedStatement.setString(2, numeroFact[1]);
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "supprimer avec succès");
                //affichage(getIdUser);
                rechercheFacture();
                clickTableFacture = false;
            } catch (SQLException ex) {
                Logger.getLogger(facturation.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            }
        }
        }else{
            JOptionPane.showMessageDialog(null, "Choisissez une facture à supprimer");             
        }
    }//GEN-LAST:event_deleteFactureBtnMouseClicked

    private void supRetenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supRetenuActionPerformed
        // TODO add your handling code here:
            if(supRetenu.isSelected()){
                getretenuNewFacture = "0";
            }else{
                getretenuNewFacture = getRetenuNewFacture1;
            }
    }//GEN-LAST:event_supRetenuActionPerformed

    private void supRetenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supRetenu1ActionPerformed
        // TODO add your handling code here:
        if(supRetenu1.isSelected()){
                retenuInOldFac = "0";
            }else{
                retenuInOldFac = retenuInOldFac1;
            }
    }//GEN-LAST:event_supRetenu1ActionPerformed

    private void supRetenuUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supRetenuUpdateActionPerformed
        // TODO add your handling code here:
        if(supRetenuUpdate.isSelected()){
                getRetenuUpdateFacture = "0";
            }else{
                getRetenuUpdateFacture = getRetenuUpdateFacture1;
            }
    }//GEN-LAST:event_supRetenuUpdateActionPerformed
private boolean checkNumberInTableNot(){
        int i = notificationTable.getSelectedRow();
        TableModel m = notificationTable.getModel();
        String fullNumber = m.getValueAt(i, 3).toString();
        String numeroFact [] = fullNumber.split("/",2);
      Connecter conn=new Connecter();
      Connection con=conn.ObteneurConction();
     try {
            //PreparedStatement pst = con.prepareStatement("SELECT CONCAT(facture.num_facture,\"/\",facture.anne) AS \"numero facture\",facture.montant_ht,facture.montant_tva,facture.montant_retenu,facture.montant_ttc,facture.date_fact,facture.date_env,facture.etat,facture.cause,enterprises.name,contracts.num_contract,facture.ref_avn FROM enterprises,contracts,facture,user WHERE facture.id_cont = contracts.id_cont and contracts.id_ent = enterprises.id and user.id = facture.idUser and facture.idUser =? and facture.etat != \"Payée\" and contracts.id_cont IN (SELECT facture.id_cont from facture GROUP BY `id_cont` HAVING ADDDATE(facture.date_env,60) < CURRENT_DATE())");
            PreparedStatement pst = con.prepareStatement("SELECT num_facture,anne FROM facture WHERE num_facture = ? and anne = ?");
            pst.setString(1, numeroFact[0]);
            pst.setString(2, numeroFact[1]);
            ResultSet rs=pst.executeQuery();
            while (rs.next()){
              return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(facturation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    
}
private boolean checkNumberInTable(String numero,String anne){       
     Connecter conn=new Connecter();
     Connection con=conn.ObteneurConction();
     try {
            //PreparedStatement pst = con.prepareStatement("SELECT CONCAT(facture.num_facture,\"/\",facture.anne) AS \"numero facture\",facture.montant_ht,facture.montant_tva,facture.montant_retenu,facture.montant_ttc,facture.date_fact,facture.date_env,facture.etat,facture.cause,enterprises.name,contracts.num_contract,facture.ref_avn FROM enterprises,contracts,facture,user WHERE facture.id_cont = contracts.id_cont and contracts.id_ent = enterprises.id and user.id = facture.idUser and facture.idUser =? and facture.etat != \"Payée\" and contracts.id_cont IN (SELECT facture.id_cont from facture GROUP BY `id_cont` HAVING ADDDATE(facture.date_env,60) < CURRENT_DATE())");
            PreparedStatement pst = con.prepareStatement("SELECT num_facture,anne FROM facture WHERE num_facture = ? and anne = ?");
            pst.setString(1,numero);
            pst.setString(2,anne);
            ResultSet rs=pst.executeQuery();
            while (rs.next()){
              return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(facturation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;  
}
private boolean checkNumberSearch(String numero,String anne){       
     Connecter conn=new Connecter();
     Connection con=conn.ObteneurConction();
     try {
            //PreparedStatement pst = con.prepareStatement("SELECT CONCAT(facture.num_facture,\"/\",facture.anne) AS \"numero facture\",facture.montant_ht,facture.montant_tva,facture.montant_retenu,facture.montant_ttc,facture.date_fact,facture.date_env,facture.etat,facture.cause,enterprises.name,contracts.num_contract,facture.ref_avn FROM enterprises,contracts,facture,user WHERE facture.id_cont = contracts.id_cont and contracts.id_ent = enterprises.id and user.id = facture.idUser and facture.idUser =? and facture.etat != \"Payée\" and contracts.id_cont IN (SELECT facture.id_cont from facture GROUP BY `id_cont` HAVING ADDDATE(facture.date_env,60) < CURRENT_DATE())");
            PreparedStatement pst = con.prepareStatement("SELECT num_facture,anne FROM all_factures WHERE num_facture = ? and anne = ?");
            pst.setString(1,numero);
            pst.setString(2,anne);
            ResultSet rs=pst.executeQuery();
            while (rs.next()){
              return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(facturation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;  
}
private void rechercheFacture(){    
  try{
    modelTableFacturation.setRowCount(0);
    Connecter conn=new Connecter();
    Connection con=conn.ObteneurConction();
    String etatStatus="and etat = 'Payée'";
    String contractStatus="";
    if(etat.getSelectedItem() !="Payée"){
                etatStatus = " and etat != 'Payée'";
    }
    if(contractSearch.getSelectedItem() !="Tous" & contractSearch.getSelectedItem() !=null){
        contractStatus = " and num_contract = '"+contractSearch.getSelectedItem().toString()+"'";
    }
    //PreparedStatement preparedStatement = con.prepareStatement("SELECT CONCAT(facture.num_facture,\"/\",facture.anne) AS \"numero facture\",facture.montant_ht,facture.montant_retenu,facture.montant_ttc,facture.date_fact,facture.date_env,facture.etat,facture.cause,enterprises.name,contracts.num_contract,facture.ref_avn FROM enterprises,contracts,facture,user WHERE facture.id_cont = contracts.id_cont and contracts.id_ent = enterprises.id and user.id = facture.idUser and facture.idUser =? and facture.num_facture like ? and facture.anne = ?");
    PreparedStatement preparedStatement = con.prepareStatement("SELECT CONCAT(num_facture,\"/\",anne) AS \"numero facture\",FORMAT(montant_ht,2) AS montant_ht,FORMAT(montant_retenu,2) AS montant_retenu,FORMAT((montant_ht - montant_retenu)*(SELECT contracts.tva /100 from contracts where contracts.num_contract = all_factures.num_contract),2) AS TVA,FORMAT(montant_ttc,2) AS montant_ttc,date_fact,date_env,etat,cause,name,num_contract,ref_avn,annuler FROM all_factures WHERE idUser =? and num_facture like ? and anne = ? " + etatStatus + contractStatus);
    preparedStatement.setString(1,getIdUser);
    preparedStatement.setString(2,"%"+numFactureSearch.getText()+"%");
    preparedStatement.setInt(3,yearSearch.getYear());
    ResultSet rs=preparedStatement.executeQuery();
    while (rs.next()){
      modelTableFacturation.addRow(new Object[]{rs.getString("name"),rs.getString("num_contract"),rs.getString("ref_avn"),rs.getString("numero facture"),rs.getString("montant_ht"),rs.getString("montant_retenu"),rs.getString("TVA"),rs.getString("montant_ttc"),rs.getString("date_fact"),rs.getString("date_env"),rs.getString("etat"),rs.getString("cause"),rs.getString("annuler")});
  }
  }catch(SQLException e){System.err.println("erreur");  }
    
    }
private void rechercheFactureUpdate(String sql){   
  try{
    Connecter conn=new Connecter();
    Connection con=conn.ObteneurConction();
    PreparedStatement preparedStatement = con.prepareStatement(sql);
    preparedStatement.setString(1,getIdUser);
    preparedStatement.setString(2,searchUpdateFactureInput.getText());
    preparedStatement.setString(3,searchAnneUpdateFactureInput.getText());
    ResultSet rs=preparedStatement.executeQuery();
        while (rs.next()){
          companyUpdateFacture.setText(rs.getString("name"));
          contractUpdateFacture.setText(rs.getString("num_contract"));
          avnantUpdateFacture.setText(rs.getString("ref_avn"));
          if(avnantUpdateFacture.getText() ==""){
              avnantUpdateFacture.setText("choisir Avenant");
          }
          companyUpdateFacture1.setSelectedItem(rs.getString("name"));
          companyUpdateContr1.setSelectedItem(rs.getString("num_contract"));
          
          if("".equals(avnantUpdateFacture.getText())){             
              companyUpdateAvn1.setSelectedItem(rs.getString("ref_avn"));
          }else{
              companyUpdateAvn1.setSelectedItem("choisir Avenant");
          }
          montantHTUpdate.setText(rs.getString("montant_ht"));
          montantUpdateTTC.setText(rs.getString("montant_ttc"));
          retenuUpdateFacture.setText(rs.getString("montant_retenu"));
          tvaUpdateFacture.setText(rs.getString("tva"));
          java.util.Date datefact = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("date_fact"));
          dateUpdateFacturation.setDate(datefact);
          java.util.Date dateenv = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("date_env"));
          dateUpdateDenvoi.setDate(dateenv);
          etatFactureUpdate.setText(rs.getString("etat"));
          causeUpdateFacture.setText(rs.getString("cause"));
          if("Annuler".equals(rs.getString("annuler"))){
              annulerCheck.setSelected(true);
          }else{
              annulerCheck.setSelected(false);
          }
        }
  }catch(SQLException e){System.err.println("erreur");  } catch (ParseException ex) {
            //Logger.getLogger(facturation.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "cette facture néxiste pas");
        }  
    }

public void text_long(JTextField  T,int k, java.awt.event.KeyEvent evt){
        int i=T.getText().length();
        //if(evt.getKeyChar()>='0' && evt.getKeyChar() <='9'){
            if (i<k) {
              T.setEditable(true);
            }
            else{
              T.setEditable(false);
            }
        //}else{
          //  if(evt.getKeyChar() == KeyEvent.VK_BACK_SPACE || evt.getKeyChar() == KeyEvent.VK_DELETE || evt.getKeyChar() == KeyEvent.VK_SLASH || evt.getKeyChar() == KeyEvent.VK_PERIOD){
            //    T.setEditable(true);
            //}
            //else{
              //   T.setEditable(false);

            //}
        //}
    }
public void getIdContract(String T){
        try {
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            PreparedStatement pst = con.prepareStatement("SELECT id_cont FROM contracts WHERE num_contract=?");
            pst.setString(1,T);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                idOldContract =  rs.getString("id_cont");
                idNewContract =  rs.getString("id_cont");
                idUpdateContract = rs.getString("id_cont");
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
}
public int getIdAv(String numAv){
        try {
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            PreparedStatement pst = con.prepareStatement("SELECT avenant.ref_av FROM avenant WHERE avenant.avenant_num =?");
            pst.setString(1,numAv);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("ref_av");
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return 0;
}

public int getIdEncFacture(String T){
        try {
            Connecter conn=new Connecter();
            Connection con=conn.ObteneurConction();
            PreparedStatement pst = con.prepareStatement("SELECT id_enc_fact FROM encient_facture WHERE num_facture=?");
            pst.setString(1,T);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_enc_fact");
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return 0;
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
            java.util.logging.Logger.getLogger(facturation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(facturation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(facturation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(facturation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new facturation().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel addFacturePanel;
    private javax.swing.JTextField anneOldFacture;
    private javax.swing.JCheckBox annulerCheck;
    private javax.swing.JLabel annulerFacture;
    private javax.swing.JLabel autreFactureUpdate;
    private javax.swing.JLabel avnantUpdateFacture;
    private javax.swing.JLabel backNewFacture;
    private javax.swing.JLabel backNewFacture1;
    private javax.swing.JLabel backNewFacture4;
    private javax.swing.JLabel backNotification;
    private javax.swing.JLabel backOldFacture;
    private javax.swing.JLabel backTotalRetenu;
    private javax.swing.JLabel backUpdateFacture;
    private javax.swing.JLabel backgroundAdd;
    private javax.swing.JLabel backgroundAdd1;
    private javax.swing.JLabel backgroundMain;
    private javax.swing.JLabel backgroundOld;
    private javax.swing.JLabel backgroundUpdate;
    private javax.swing.JCheckBox bisCheck;
    private javax.swing.JCheckBox bissFacture;
    private javax.swing.JPanel box;
    private javax.swing.JTextField causeOldFacture;
    private javax.swing.JTextField causeUpdateFacture;
    private javax.swing.JLabel changePasswordBtn;
    private javax.swing.JDialog changePasswordDialog;
    private javax.swing.JLabel close1;
    private javax.swing.JComboBox<String> companyNewAvn;
    private javax.swing.JComboBox<String> companyNewContr;
    private javax.swing.JComboBox<String> companyNewFacture;
    private javax.swing.JComboBox<String> companyOldAvn;
    private javax.swing.JComboBox<String> companyOldContr;
    private javax.swing.JComboBox<String> companyOldFacture;
    private javax.swing.JComboBox<String> companyUpdateAvn1;
    private javax.swing.JComboBox<String> companyUpdateContr1;
    private javax.swing.JLabel companyUpdateFacture;
    private javax.swing.JComboBox<String> companyUpdateFacture1;
    private javax.swing.JTextField confirmPassword;
    private javax.swing.JComboBox<String> contractSearch;
    private javax.swing.JLabel contractUpdateFacture;
    private javax.swing.JLabel copyNumFacture;
    private com.toedter.calendar.JDateChooser dateNewDenvoi;
    private com.toedter.calendar.JDateChooser dateNewFacturation;
    private com.toedter.calendar.JDateChooser dateOldDenvoi;
    private com.toedter.calendar.JDateChooser dateOldFacturation;
    private com.toedter.calendar.JDateChooser dateUpdateDenvoi;
    private com.toedter.calendar.JDateChooser dateUpdateFacturation;
    private javax.swing.JLabel declareUpdate;
    private javax.swing.JLabel deleteFactureBtn;
    private javax.swing.JComboBox<String> etat;
    private javax.swing.JLabel etatFactureUpdate;
    private javax.swing.JComboBox<String> etatOldFacture;
    private javax.swing.JComboBox<String> etatTheFacture1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable listFactureTable;
    private javax.swing.JLabel logOutBtn;
    private javax.swing.JPanel mainFacturePanel;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JTextField montantHTOld;
    private javax.swing.JTextField montantHTUpdate;
    private javax.swing.JTextField montantNewHT;
    private javax.swing.JLabel montantNewTTC;
    private javax.swing.JLabel montantOldTTC;
    private javax.swing.JLabel montantUpdateTTC;
    private javax.swing.JLabel newFacture;
    private javax.swing.JLabel notificationBtn;
    private javax.swing.JLabel notificationCount;
    private javax.swing.JPanel notificationPanel;
    private javax.swing.JTable notificationTable;
    private javax.swing.JLabel numFactureNew;
    private javax.swing.JTextField numFactureSearch;
    private javax.swing.JTextField numOldFacture;
    private javax.swing.JLabel oldFacture;
    private javax.swing.JPanel oldFacturePanel;
    private javax.swing.JTextField passwordChanged;
    private javax.swing.JLabel retenuNewFacture;
    private javax.swing.JLabel retenuOldFacture;
    private javax.swing.JLabel retenuUpdateFacture;
    private javax.swing.JLabel saveConfirmPassword;
    private javax.swing.JLabel saveNewFacture;
    private javax.swing.JLabel saveOldFacture;
    private javax.swing.JLabel saveUpdateFacture;
    private javax.swing.JTextField searchAnneUpdateFactureInput;
    private javax.swing.JLabel searchFactureBtnUpdate;
    private javax.swing.JTextField searchUpdateFactureInput;
    private javax.swing.JCheckBox supRetenu;
    private javax.swing.JCheckBox supRetenu1;
    private javax.swing.JCheckBox supRetenuUpdate;
    private javax.swing.JLabel totalHtTable;
    private javax.swing.JLabel totalRetTable;
    private javax.swing.JLabel totalRetenuBtn;
    private javax.swing.JDialog totalRetenuDialog;
    private javax.swing.JTable totalRetenuTable;
    private javax.swing.JLabel totalTtcTable;
    private javax.swing.JLabel totalTvaTable;
    private javax.swing.JLabel tvaNewFacture;
    private javax.swing.JLabel tvaOldFacture;
    private javax.swing.JLabel tvaUpdateFacture;
    private javax.swing.JLabel updateFacture;
    private javax.swing.JPanel updateFacturePanel;
    private javax.swing.JLabel userMenu;
    private javax.swing.JLabel userNameChange;
    private com.toedter.calendar.JYearChooser yearSearch;
    // End of variables declaration//GEN-END:variables
}
