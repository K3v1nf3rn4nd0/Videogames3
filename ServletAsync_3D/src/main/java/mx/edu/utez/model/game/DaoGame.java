package mx.edu.utez.model.game;

import mx.edu.utez.model.category.BeanCategory;
import mx.edu.utez.service.ConectionMySQL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.ConnectionEvent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoGame {
    private Connection con;
    private CallableStatement cstm;
    private ResultSet rs;
    final private Logger CONSOLE = LoggerFactory.getLogger(DaoGame.class);

    public List<BeanGame> findAll() {
        List<BeanGame> listGame = new ArrayList<>();
        try {
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("{call sp_findAll}");
            rs = cstm.executeQuery();

            while (rs.next()) {
                BeanGame game = new BeanGame();
                BeanCategory category2 = new BeanCategory();


                game.setIdGame(rs.getInt("idGame"));
                game.setNameGame(rs.getString("nameGame"));
                game.setImgGame(rs.getFile("img_game"));
                game.setCategory_idCategory(category2);
                game.setDatePremiere(rs.getString("date_premire"));
                game.setStatus(rs.getInt("STATUS"));

                category2.setIdCategory(rs.getInt("idCategory"));
                category2.setNameCategory(rs.getString("nameCategory"));
                category2.setStatus(rs.getInt("STATUS"));


                listGame.add(game);
            }
        } catch (SQLException e) {
            Logger logger = LoggerFactory.getLogger(DaoGame.class);
            logger.error("Ha ocurrido un error: " + e.getMessage());
        } finally {
            ConnectionMySQL.closeConnections(con, cstm, rs);
        }
        return listGame;

    }

    //------------------------------------------------------------
    public BeanGame findById(int idGame) {
        BeanGame game = null;
        try {
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("SELECT * FROM game AS U INNER JOIN category AS P ON U.idGame = P.idCategory WHERE U.idGame = ?");
            cstm.setInt(1, idGame);
            rs = cstm.executeQuery();

            if (rs.next()) {
                BeanCategory category2 = new BeanCategory();
                game = new BeanGame();

                game.setIdGame(rs.getInt("idGame"));
                game.setNameGame(rs.getString("nameGame"));
                //game.setImgGame(rs.getFile("img_game"));
                game.setCategory_idCategory(category2);
                game.setDatePremiere(rs.getString("date_premire"));
                game.setStatus(rs.getInt("STATUS"));

                category2.setIdCategory(rs.getInt("idCategory"));
                category2.setNameCategory(rs.getString("nameCategory"));
                category2.setStatus(rs.getInt("STATUS"));


            }
        } catch (SQLException e) {
            Logger logger = LoggerFactory.getLogger(DaoGame.class);
            logger.error("Ha ocurrido un error: " + e.getMessage());
        } finally {
            ConnectionMySQL.closeConnections(con, cstm, rs);
        }
        return game;
    }
    public boolean create(BeanGame game){
        boolean flag = false;
        try{
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("{call sp_create(?,?,?,?,?,?)}");
            cstm.setInt(1, game.getIdGame());
            cstm.setString(2, game.getNameGame());
            cstm.setInt(3, game.getCategory_idCategory().getIdCategory());
            cstm.setString(4, game.getCategory_idCategory().getNameCategory());
            cstm.setString(5, game.getDatePremiere());
            cstm.setInt(6, game.getStatus());
            cstm.execute();
            flag = true;
        }catch(SQLException e){
            Logger logger = LoggerFactory.getLogger(DaoGame.class);
            logger.error("Ha ocurrido un error: " + e.getMessage());
        } finally {
            ConnectionMySQL.closeConnections(con, cstm);
        }
        return flag;
    }
    public boolean update(BeanGame game){
        boolean flag = false;
        try{
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("{call sp_update(?,?,?,?,?,?,?)}");
            cstm.setInt(1, game.getIdGame());
            cstm.setString(2, game.getNameGame());
            cstm.setInt(3, game.getCategory_idCategory().getIdCategory());
            cstm.setString(4, game.getCategory_idCategory().getNameCategory());
            cstm.setString(5, game.getDatePremiere());
            cstm.setInt(6, game.getStatus());

            flag = cstm.execute();
        }catch(SQLException e){
            Logger logger = LoggerFactory.getLogger(DaoGame.class);
            logger.error("Ha ocurrido un error: " + e.getMessage());
        }finally{
           ConnectionMySQL.closeConnections(con, cstm);
        }
        return flag;
    }
    public boolean delete(int idGame){
        boolean flag = false;
        try{
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("{call sp_delete2(?)}");
            cstm.setLong(1, idGame);

            flag = cstm.execute();
        }catch(SQLException e){
            Logger logger = LoggerFactory.getLogger(DaoGame.class);
            logger.error("Ha ocurrido un error: " + e.getMessage());
        }finally{
            ConnectionMySQL.closeConnections(con, cstm);
        }
        return flag;
    }
}
