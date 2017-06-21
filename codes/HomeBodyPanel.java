package fr.sert.body;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import fr.sert.body.jfx.AlertePanelController;
import fr.sert.body.project.AdminStatProjectPanel;
import fr.sert.body.project.ProgramsPanel;
import fr.sert.body.project.ProjectsPanel;
import fr.sert.body.project.jfx.AdminListeProjetPanelController;
import fr.sert.body.project.jfx.ListeProjetPanelController;
import fr.sert.body.project.jfx.RPBasListController;
import fr.sert.body.project.jfx.RPHautListController;
import fr.sert.body.project.jfx.RRProgramListViewController;
import fr.sert.dao.AlerteDAO;
import fr.sert.main.MainSERT;
import fr.sert.sm.bean.AbstractProject;
import fr.sert.sm.services.ProjectSM;
import fr.sert.util.javafx.SERTJFXPanel;
import fr.sert.util.swing.TitlePanel;

/**
 * The Class HomeBodyPanel.
 */
public final class HomeBodyPanel extends JPanel {

    private static final Logger LOGGER = Logger.getLogger(HomeBodyPanel.class);

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 547203131685755754L;

    /** The north panel. */
    private final JPanel northPanel;

    /** The center panel. */
    private final JPanel centerPanel;

    /** nombre d'alertes */
    private int nombreAlertes = 0;
    /** message d'arlerte */
    private String messageAlerte = "Pas d'alertes!";
    /** AlerteDAO */
    private AlerteDAO alerteDAO = new AlerteDAO();

    /**
     * Instantiates a new home body panel.
     */
    public HomeBodyPanel() {
        super();

        // Fermer les éventuelles fenetres flottantes
        closeToolsBars();

        northPanel = new JPanel(new BorderLayout());
        centerPanel = new JPanel();

        initBodyPanel();
    }

    /**
     * Inits the body panel.
     */
    private void initBodyPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        setOpaque(false);

        northPanel.setOpaque(false);
        northPanel.add(new TitlePanel("Tableau de bord"));

        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        RoleEnum role = MainSERT.getCurrentUser().getProfil();
        // En mode nomade le tableau de bord contient seulement les projets
        // importées.
        // En nomade ne pas afficher le panel de la liste des projets.

        // Afficher la listes des études sauf dans le cas du tableau de bord
        // admin.
        // Dans le cas du tableau de bord admin afficher un panneau de stat.
        if (role == RoleEnum.ADMINISTRATEUR) {
            if (!MainSERT.isNomade()) {
                centerPanel.add(new ProjectsPanel("PROJETS/PROGRAMMES", new AdminListeProjetPanelController()));
                centerPanel.add(Box.createVerticalStrut(20));
            }
            AdminStatProjectPanel statPanel = new AdminStatProjectPanel();
            statPanel.setPreferredSize(new Dimension(2000, 200));
            statPanel.setMaximumSize(new Dimension(2000, 200));
            centerPanel.add(statPanel);
        } else if (role == RoleEnum.REDACTEUR_DE_REGIME) {

            if (!MainSERT.isNomade()) {
                nombreAlertes = alerteDAO.getNombreAlerteCourant(); 
                if(nombreAlertes>0) messageAlerte = "Attention "+nombreAlertes+" alerte(s) en attente !";
				
				JPanel centerPanel = new JPanel();
                AlertePanelController alerteController = new AlertePanelController(messageAlerte);
                SERTJFXPanel alertefxPanel = new SERTJFXPanel("/javafx/alertePanel.fxml", alerteController);
                centerPanel.add(alertefxPanel);
				
                Dimension dimension = new Dimension();
                dimension.height = 20;
                dimension.width = centerPanel.getMaximumSize().width;
                alertefxPanel.setSize(dimension);
                alertefxPanel.setMinimumSize(dimension);
                alertefxPanel.setMaximumSize(dimension);
                alertefxPanel.revalidate();
            } else {
                nombreAlertes = 0;
            }

            this.centerPanel.add(new ProjectsPanel("MES PROJETS", new ListeProjetPanelController()));
            this.centerPanel.add(Box.createVerticalStrut(20));
            this.centerPanel.add(new ProgramsPanel("PROGRAMMES TIR", new RRProgramListViewController()));
        } else if (role == RoleEnum.REDACTEUR_DE_PROGRAMME) {
            ProjectSM projectSM = new ProjectSM();
            List<AbstractProject> programmes = new ArrayList<>();
            try {
                programmes = projectSM.getProgrammes();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
            List<AbstractProject> programmesEnCours = projectSM.getProgrammesEnCours(programmes);
            List<AbstractProject> programmesAutre = projectSM.getProgrammesAutre(programmes);

            ProgramsPanel programmesEnCoursController = new ProgramsPanel("MES PROGRAMMES TIR", new RPHautListController(programmesEnCours));
            ProgramsPanel programmesAutreController = new ProgramsPanel("PROGRAMMES TIR TRANSMIS", new RPBasListController(programmesAutre));

            if (!MainSERT.isNomade()) {
                nombreAlertes = alerteDAO.getNombreAlerteCourant(); 
                if(nombreAlertes>0) messageAlerte = "Attention "+nombreAlertes+" alerte(s) en attente !";
                AlertePanelController alerteController = new AlertePanelController(messageAlerte);
                SERTJFXPanel alertefxPanel = new SERTJFXPanel("/javafx/alertePanel.fxml", alerteController);
                if(nombreAlertes>0) this.centerPanel.add(alertefxPanel);
                Dimension dimension = new Dimension();
                dimension.height = 20;
                dimension.width = centerPanel.getMaximumSize().width;
                alertefxPanel.setSize(dimension);
                alertefxPanel.setMinimumSize(dimension);
                alertefxPanel.setMaximumSize(dimension);
                alertefxPanel.revalidate();
            } else {
                nombreAlertes = 0;
            }

            this.centerPanel.add(programmesEnCoursController);
            this.centerPanel.add(Box.createVerticalStrut(20));
            this.centerPanel.add(programmesAutreController);
        }
        centerPanel.add(Box.createVerticalStrut(20));

        this.add(northPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);

    }

    /**
     * Fermeture des des fenetres floattantes.
     */
    private void closeToolsBars() {
        ProjectBodyPanel.closeFormAndTools();
    }

    public void showAlertPopup() {
        if (nombreAlertes > 0) {
            RoleEnum role = MainSERT.getCurrentUser().getProfil();
            if (role == RoleEnum.REDACTEUR_DE_PROGRAMME || role == RoleEnum.REDACTEUR_DE_REGIME) {
                // Popup message d'alerte au demarrage de l'application
                JOptionPane.showMessageDialog(null, messageAlerte, "Popup Alerte", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
