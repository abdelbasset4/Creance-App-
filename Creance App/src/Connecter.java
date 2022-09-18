/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.*;
import javax.swing.JOptionPane;

public class Connecter {
    
    
    Connection con;
    public Connecter(){
        try{
             Class.forName("com.mysql.jdbc.Driver");
        
        }catch(ClassNotFoundException e){System.err.println(e);}
         try{
             
            //con = DriverManager.getConnection("jdbc:mysql://169.254.97.109:3306/h_bdd","hamdi", "hamdi2022");
           
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/h_bdd","root", "");
        }catch(SQLException e){
            //System.err.println(e);
            JOptionPane.showMessageDialog(null, "errour connection"); 
        }
    }
    Connection ObteneurConction(){return con;}

    PreparedStatement prepareStatement(String select__from_lieu_travail_WHERE_daira_and) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
