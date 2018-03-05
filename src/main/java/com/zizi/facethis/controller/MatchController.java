package com.zizi.facethis.controller;

import com.zizi.facethis.json.UrlJson;
import com.zizi.facethis.repository.DatabaseConnexion;
import com.zizi.facethis.util.PasswordGenerator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/**
 * Created by Vince on 03-03-18.
 */
@RestController
@RequestMapping("/api")
public class MatchController {

    @RequestMapping(value = "/create", method = RequestMethod.GET, produces = "application/json")
    public UrlJson createMatch() throws Exception {
        String password = PasswordGenerator.getSaltString();
        String ip = "5.135.153.179:27015";
        String joinUrl = "steam://connect/" + ip + "/" + password;
        Connection conn = DatabaseConnexion.getInstance().getConn();
        Statement st = conn.createStatement();
        st.executeUpdate("INSERT INTO matchs (ip,server_id,team_a_flag,team_a_name,team_b_flag,team_b_name,status,score_a,score_b,max_round,rules,overtime_startmoney,overtime_max_round,config_full_score,config_ot,config_streamer,config_knife_round,config_password,enable,map_selection_mode,startdate,config_authkey,auto_start, created_at,updated_at)" +
                " VALUES ('" + ip + "', 2, 'FR', 'Team A', 'BE', 'Team B', 0, 0, 0, 15, 'esl5on5', 10000, 3, 0, 1, 0, 1, '" + password + "', 0, 'normal', NOW(), '" + new Date().getTime() + "', 0, NOW(), NOW());");
        int matchId = 0;
        ResultSet rs = st.executeQuery("SELECT max(id) FROM matchs");
        while(rs.next()) {
            matchId = rs.getInt(1);
        }
        if (matchId == 0) throw new Exception();
        st.executeUpdate("INSERT INTO maps (match_id,map_name,score_1,score_2,current_side,status,created_at,updated_at)" +
                " VALUES (" + matchId + ", 'de_inferno', 0, 0, 'ct', 0, NOW(), NOW());");
        int mapId = 0;
        rs = st.executeQuery("SELECT max(id) FROM maps");
        while(rs.next()) {
            mapId = rs.getInt(1);
        }
        if (matchId == 0) throw new Exception();
        st.executeUpdate("UPDATE matchs SET current_map = " + mapId + " WHERE id = " + matchId + ";");
        Thread.sleep(3000);
        st.executeUpdate("UPDATE matchs SET status = 1 AND enable = 1 WHERE id = " + matchId + ";");

        return new UrlJson(joinUrl);
    }
}
