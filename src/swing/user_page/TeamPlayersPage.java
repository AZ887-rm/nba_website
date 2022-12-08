package swing.user_page;

import dao.DBConn;
import service.user_modules.SearchGameModule;
import service.user_modules.SearchPlayerModule;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

public class TeamPlayersPage extends AbstractSearchPage {
    private JPanel searchBarPanel;
    private JTextField searchField;
    private JButton searchBtn;
    private JPanel menuPanel;
    private JLabel globalPositionLabel;
    private JPanel tablePanel;
    private JScrollPane scrollPanel;
    private JTable table;
    private JPanel bottomPanel;
    private JButton backBtn;
    private JPanel mainPanel;
    private int teamId;

    public TeamPlayersPage(Connection conn, String username, int teamId) {
        super(conn, username);

        this.teamId = teamId;
        setContentPane(mainPanel);
        setVisible(true);
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new TeamInfoPage(conn, username, teamId);
            }
        });
    }

    private void createUIComponents() {
        menuPanel = super.menuPanel;
        searchField = super.searchField;
        searchBtn = super.searchBtn;

        String[] header = {"abbreviation", "player name", "match count", "scores", "assists", "rebound"};
        List<List<String>> listData = new SearchPlayerModule(conn).getTeamPlayers(teamId);
        int m = listData.size(), n = listData.get(0).size();
        Object[][] data = new Object[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                data[i][j] = listData.get(i).get(j);
            }
        }
        DefaultTableModel defaultModel = new DefaultTableModel(data, header);
        table = new JTable(defaultModel);
    }

    public static void main(String[] args) {
        new TeamPlayersPage(DBConn.getConn("root", "12345678"), "123", 1610612737);
    }
}
