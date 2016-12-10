/*
 * FinalProjectView.java
 */

package tugasakhir.form;

import java.awt.Color;
import java.awt.ComponentOrientation;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import tugasakhir.process.DatabaseIndexer;
import tugasakhir.process.Searcher;
import java.io.IOException;
import org.apache.lucene.queryParser.ParseException;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import tugasakhir.process.Bobot;
import tugasakhir.process.IdxReader;
import tugasakhir.process.Indexing;

/**
 * The application's main frame.
 */
public class View extends FrameView {

    public View(SingleFrameApplication app) {
        super(app);

        this.initialize();
        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = App.getApplication().getMainFrame();
            aboutBox = new AboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        App.getApplication().show(aboutBox);
    }
    @Action
    public void showPetunjuk() {
        if (guide == null) {
            JFrame mainFrame = App.getApplication().getMainFrame();
            guide = new Guide(mainFrame);
            guide.setLocationRelativeTo(mainFrame);
        }
        App.getApplication().show(guide);
    }
    
    @Action
    public void previewDokumen() {

    }
    
    @Action
    public void showDocumentList() {
        if (documentList == null) {
            JFrame mainFrame = App.getApplication().getMainFrame();
            documentList = new ListDocument(mainFrame);
            documentList.setLocationRelativeTo(mainFrame);
        }
        App.getApplication().show(documentList);
    }

    @Action
    public void showTermDocumentList() {
        if (termDocumentList == null) {
            JFrame mainFrame = App.getApplication().getMainFrame();
            termDocumentList = new TambahDokumen(mainFrame);
            termDocumentList.setLocationRelativeTo(mainFrame);
        }
        App.getApplication().show(termDocumentList);
    }
    
    @Action
    public void indexing(){
        if (indexing == null) {
            indexing = new Indexing();
            String[] args = null;
            try {
                indexing.main(args);
            } catch (IOException ex) {
            }
            
            
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        cariPreviewTextField = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane3 = new javax.swing.JScrollPane();
        previewHasilTextArea1 = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        previewHasilTextArea2 = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        previewHasilTextArea3 = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        previewHasilTextArea4 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        cariSplitPane1 = new javax.swing.JSplitPane();
        cariButton1 = new javax.swing.JButton();
        cariTextField1 = new javax.swing.JTextField();
        hasilSplitPane1 = new javax.swing.JSplitPane();
        hasilScrollPane1 = new javax.swing.JScrollPane();
        hasilTable1 = new javax.swing.JTable();
        nassScrollPane1 = new javax.swing.JScrollPane();
        nassTextArea1 = new javax.swing.JTextArea();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        docListMenu = new javax.swing.JMenuItem();
        termDocListMenu = new javax.swing.JMenuItem();
        Indexing = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        Petunjuk = new javax.swing.JMenuItem();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setMaximumSize(new java.awt.Dimension(1024, 768));
        mainPanel.setMinimumSize(new java.awt.Dimension(700, 500));
        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(730, 568));
        mainPanel.setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N
        jTabbedPane1.setPreferredSize(mainPanel.getPreferredSize());

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new java.awt.BorderLayout());

        jSplitPane1.setDividerLocation(3);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setName("jSplitPane1"); // NOI18N
        jSplitPane1.setPreferredSize(new java.awt.Dimension(645, 450));

        jPanel3.setName("jPanel3"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 697, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );

        jSplitPane1.setLeftComponent(jPanel3);

        jPanel4.setName("jPanel4"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(tugasakhir.form.App.class).getContext().getResourceMap(View.class);
        cariPreviewTextField.setFont(resourceMap.getFont("cariPreviewTextField.font")); // NOI18N
        cariPreviewTextField.setText(resourceMap.getString("cariPreviewTextField.text")); // NOI18N
        cariPreviewTextField.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        cariPreviewTextField.setName("cariPreviewTextField"); // NOI18N

        jSeparator1.setName("jSeparator1"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        previewHasilTextArea1.setColumns(20);
        previewHasilTextArea1.setEditable(false);
        previewHasilTextArea1.setFont(resourceMap.getFont("previewHasilTextArea1.font")); // NOI18N
        previewHasilTextArea1.setLineWrap(true);
        previewHasilTextArea1.setRows(5);
        previewHasilTextArea1.setWrapStyleWord(true);
        previewHasilTextArea1.setName("previewHasilTextArea1"); // NOI18N
        previewHasilTextArea1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        previewHasilTextArea1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                previewHasilTextArea1MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(previewHasilTextArea1);

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        previewHasilTextArea2.setColumns(20);
        previewHasilTextArea2.setEditable(false);
        previewHasilTextArea2.setFont(resourceMap.getFont("previewHasilTextArea2.font")); // NOI18N
        previewHasilTextArea2.setLineWrap(true);
        previewHasilTextArea2.setRows(5);
        previewHasilTextArea2.setWrapStyleWord(true);
        previewHasilTextArea2.setName("previewHasilTextArea2"); // NOI18N
        previewHasilTextArea2.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        previewHasilTextArea2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                previewHasilTextArea2MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(previewHasilTextArea2);

        jScrollPane5.setName("jScrollPane5"); // NOI18N

        previewHasilTextArea3.setColumns(20);
        previewHasilTextArea3.setEditable(false);
        previewHasilTextArea3.setFont(resourceMap.getFont("previewHasilTextArea3.font")); // NOI18N
        previewHasilTextArea3.setLineWrap(true);
        previewHasilTextArea3.setRows(5);
        previewHasilTextArea3.setWrapStyleWord(true);
        previewHasilTextArea3.setName("previewHasilTextArea3"); // NOI18N
        previewHasilTextArea3.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        previewHasilTextArea3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                previewHasilTextArea3MouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(previewHasilTextArea3);

        jScrollPane6.setName("jScrollPane6"); // NOI18N

        previewHasilTextArea4.setColumns(20);
        previewHasilTextArea4.setEditable(false);
        previewHasilTextArea4.setFont(resourceMap.getFont("previewHasilTextArea4.font")); // NOI18N
        previewHasilTextArea4.setLineWrap(true);
        previewHasilTextArea4.setRows(5);
        previewHasilTextArea4.setWrapStyleWord(true);
        previewHasilTextArea4.setName("previewHasilTextArea4"); // NOI18N
        previewHasilTextArea4.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        previewHasilTextArea4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                previewHasilTextArea4MouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(previewHasilTextArea4);

        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(cariPreviewTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addGap(11, 11, 11))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cariPreviewTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel4);

        jPanel1.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jPanel5.setName("jPanel5"); // NOI18N
        jPanel5.setPreferredSize(new java.awt.Dimension(695, 90));

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setFont(resourceMap.getFont("jLabel3.font")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(374, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(275, 275, 275))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(267, 267, 267))))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addContainerGap())
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(0, 31, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addGap(0, 30, Short.MAX_VALUE)))
        );

        jPanel1.add(jPanel5, java.awt.BorderLayout.NORTH);

        jTabbedPane1.addTab(resourceMap.getString("jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setLayout(new java.awt.BorderLayout());

        cariSplitPane1.setDividerLocation(600);
        cariSplitPane1.setName("cariSplitPane1"); // NOI18N

        cariButton1.setText(resourceMap.getString("cariButton1.text")); // NOI18N
        cariButton1.setName("cariButton1"); // NOI18N
        cariButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cariButton1ActionPerformed(evt);
            }
        });
        cariSplitPane1.setRightComponent(cariButton1);

        cariTextField1.setFont(resourceMap.getFont("cariTextField1.font")); // NOI18N
        cariTextField1.setName("cariTextField1"); // NOI18N
        cariTextField1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        cariSplitPane1.setLeftComponent(cariTextField1);

        jPanel2.add(cariSplitPane1, java.awt.BorderLayout.PAGE_START);

        hasilSplitPane1.setDividerLocation(300);
        hasilSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        hasilSplitPane1.setName("hasilSplitPane1"); // NOI18N

        hasilScrollPane1.setName("hasilScrollPane1"); // NOI18N

        hasilTable1.setFont(resourceMap.getFont("hasilTable1.font")); // NOI18N
        hasilTable1.setModel(this.hasilCariTableModel);
        hasilTable1.setName("hasilTable1"); // NOI18N
        hasilTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                hasilTable1MouseClicked(evt);
            }
        });
        hasilScrollPane1.setViewportView(hasilTable1);

        hasilSplitPane1.setBottomComponent(hasilScrollPane1);

        nassScrollPane1.setName("nassScrollPane1"); // NOI18N

        nassTextArea1.setColumns(20);
        nassTextArea1.setFont(resourceMap.getFont("nassTextArea1.font")); // NOI18N
        nassTextArea1.setLineWrap(true);
        nassTextArea1.setRows(5);
        nassTextArea1.setWrapStyleWord(true);
        nassTextArea1.setAutoscrolls(false);
        nassTextArea1.setName("nassTextArea1"); // NOI18N
        nassTextArea1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        nassScrollPane1.setViewportView(nassTextArea1);

        hasilSplitPane1.setLeftComponent(nassScrollPane1);

        jPanel2.add(hasilSplitPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(resourceMap.getString("jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

        mainPanel.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(tugasakhir.form.App.class).getContext().getActionMap(View.class, this);
        docListMenu.setAction(actionMap.get("showDocumentList")); // NOI18N
        docListMenu.setText(resourceMap.getString("docListMenu.text")); // NOI18N
        docListMenu.setToolTipText(resourceMap.getString("docListMenu.toolTipText")); // NOI18N
        docListMenu.setName("docListMenu"); // NOI18N
        fileMenu.add(docListMenu);

        termDocListMenu.setAction(actionMap.get("showTermDocumentList")); // NOI18N
        termDocListMenu.setText(resourceMap.getString("termDocListMenu.text")); // NOI18N
        termDocListMenu.setName("termDocListMenu"); // NOI18N
        fileMenu.add(termDocListMenu);

        Indexing.setAction(actionMap.get("indexing")); // NOI18N
        Indexing.setText(resourceMap.getString("Indexing.text")); // NOI18N
        Indexing.setName("Indexing"); // NOI18N
        fileMenu.add(Indexing);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setAction(actionMap.get("showPetunjuk")); // NOI18N
        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        Petunjuk.setAction(actionMap.get("showPetunjuk")); // NOI18N
        Petunjuk.setText(resourceMap.getString("Petunjuk.text")); // NOI18N
        Petunjuk.setName("Petunjuk"); // NOI18N
        helpMenu.add(Petunjuk);

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 534, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents
    IdxReader indexReader ;
    private void cariButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cariButton1ActionPerformed
       
        List<Bobot> bobotList;
        String index = "FileIndex";
        Searcher searcher = null;
        try {
            searcher = new Searcher(index);
        } catch (IOException ex) {
            System.out.println("Tidak bisa membaca" + index);
        }
        
        String[][] hasil = null;
        
        try {
            hasil = searcher.search(this.cariTextField1.getText());
            //
            //!!!
            indexReader = new IdxReader(index);
            String searchText = this.cariTextField1.getText().replaceAll("\"", "");
            bobotList = indexReader.getThesaurus(this.cariTextField1.getText());
            //indexReader.getTermPos(searchText);
            //indexReader.getTerms();
        } catch (IOException | ParseException ex) {
        }
        this.hasilCariTableModel.populateList(hasil);
        
        
    }//GEN-LAST:event_cariButton1ActionPerformed

    private void hasilTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hasilTable1MouseClicked
        String nass = this.hasilTable1.getValueAt(this.hasilTable1.getSelectedRow(), 3).toString();
        
        
        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter( Color.GREEN );

        String searchWordUN = this.cariTextField1.getText().replaceAll("\"", "");
        
        
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < nass.length(); i++){
            char c = nass.charAt(i);
//            if (CharUnicodeInfo.GetUnicodeCategory(c) != UnicodeCategory.NonSpacingMark)
//              stringBuilder.Append(c);
        }
                                    
            String normalizedNass = nass.replaceAll("ٌ", "");
            normalizedNass = normalizedNass.replaceAll("ٍ", "");
            normalizedNass = normalizedNass.replaceAll("ً", "");
            normalizedNass = normalizedNass.replaceAll("ِ", "");
            normalizedNass = normalizedNass.replaceAll("َ", "");
            normalizedNass = normalizedNass.replaceAll("ُ", "");
            normalizedNass = normalizedNass.replaceAll("ْ", "");

            String searchWord = searchWordUN.replaceAll("ٌ", "");
            searchWord = searchWord.replaceAll("ٍ", "");
            searchWord = searchWord.replaceAll("ً", "");
            searchWord = searchWord.replaceAll("ِ", "");
            searchWord = searchWord.replaceAll("َ", "");
            searchWord = searchWord.replaceAll("ُ", "");
            searchWord = searchWord.replaceAll("ْ", "");

  
            this.nassTextArea1.setText(normalizedNass);
        
        int offset = normalizedNass.indexOf(searchWord);
        int length = searchWord.length();

        while ( offset != -1)
        {
            try
            {
                this.nassTextArea1.getHighlighter().addHighlight(offset, offset + length, painter);
                offset = searchWord.indexOf(searchWord, offset+1);
            }
            catch(BadLocationException ble) { System.out.println(ble); }
        }
        
    }//GEN-LAST:event_hasilTable1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

         String queryPencarian = this.cariPreviewTextField.getText();         String[][] hasil = null;         String index = "FileIndex";         Searcher searcher = null;         try {              searcher = new Searcher(index);         } catch (IOException ex) {             System.out.println("Tidak bisa membaca" + index);         }          try {             hasil = searcher.search(queryPencarian);         } catch (ParseException | IOException ex) {             System.out.println("Tidak bisa mencari " + queryPencarian);         }          for (int i = 0; i < 4; i++) {             this.preview[i][0] = hasil[i][1];             this.preview[i][1] = hasil[i][2];             this.preview[i][2] = hasil[i][3];         }          this.previewHasilTextArea1.setText(hasil[0][3]);         this.previewHasilTextArea2.setText(hasil[1][3]);         this.previewHasilTextArea3.setText(hasil[2][3]);         this.previewHasilTextArea4.setText(hasil[3][3]);     }//GEN-LAST:event_jButton1ActionPerformed

    private void previewHasilTextArea4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_previewHasilTextArea4MouseClicked

         if (previewDokumen == null) {             JFrame mainFrame = App.getApplication().getMainFrame();             previewDokumen = new PreviewDokumen(mainFrame);             previewDokumen.setLocationRelativeTo(mainFrame);             previewDokumen.clearContent();             previewDokumen.setContent(this.preview[3]);         }         App.getApplication().show(previewDokumen);     }//GEN-LAST:event_previewHasilTextArea4MouseClicked

    private void previewHasilTextArea3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_previewHasilTextArea3MouseClicked

         if (previewDokumen == null) {             JFrame mainFrame = App.getApplication().getMainFrame();             previewDokumen = new PreviewDokumen(mainFrame);             previewDokumen.setLocationRelativeTo(mainFrame);             previewDokumen.clearContent();             previewDokumen.setContent(this.preview[2]);         }         App.getApplication().show(previewDokumen);     }//GEN-LAST:event_previewHasilTextArea3MouseClicked

    private void previewHasilTextArea2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_previewHasilTextArea2MouseClicked

         if (previewDokumen == null) {             JFrame mainFrame = App.getApplication().getMainFrame();             previewDokumen = new PreviewDokumen(mainFrame);             previewDokumen.setLocationRelativeTo(mainFrame);             previewDokumen.clearContent();             previewDokumen.setContent(this.preview[1]);         }         App.getApplication().show(previewDokumen);     }//GEN-LAST:event_previewHasilTextArea2MouseClicked

    private void previewHasilTextArea1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_previewHasilTextArea1MouseClicked

         if (previewDokumen == null) {             JFrame mainFrame = App.getApplication().getMainFrame();             previewDokumen = new PreviewDokumen(mainFrame);             previewDokumen.setLocationRelativeTo(mainFrame);             previewDokumen.clearContent();             previewDokumen.setContent(this.preview[0]);         }         App.getApplication().show(previewDokumen);     }//GEN-LAST:event_previewHasilTextArea1MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Indexing;
    private javax.swing.JMenuItem Petunjuk;
    private javax.swing.JButton cariButton1;
    private javax.swing.JTextField cariPreviewTextField;
    private javax.swing.JSplitPane cariSplitPane1;
    private javax.swing.JTextField cariTextField1;
    private javax.swing.JMenuItem docListMenu;
    private javax.swing.JScrollPane hasilScrollPane1;
    private javax.swing.JSplitPane hasilSplitPane1;
    private javax.swing.JTable hasilTable1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JScrollPane nassScrollPane1;
    private javax.swing.JTextArea nassTextArea1;
    private javax.swing.JTextArea previewHasilTextArea1;
    private javax.swing.JTextArea previewHasilTextArea2;
    private javax.swing.JTextArea previewHasilTextArea3;
    private javax.swing.JTextArea previewHasilTextArea4;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JMenuItem termDocListMenu;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private HasilTableModel hasilCariTableModel;
    private DatabaseIndexer indexer;
    
    private JDialog aboutBox;
    private JDialog guide;
    private JFrame documentList;
    private JFrame termDocumentList;
    private PreviewDokumen previewDokumen;
    private String[][] preview = new String[4][3];
    private Indexing indexing;
    
    private void initialize(){
        this.hasilCariTableModel = new HasilTableModel();
        String[] columnName = {"No","Judul Kitab","Halaman","Preview","Score"};
        this.hasilCariTableModel.setColumnName(columnName);
        

        
    }
            public HashMap<Integer,String> getJudul(){
    
String[][] judul = {
    {"1","نهاية المطلب في دراية المذهب"},
{"2","المجموع شرح المهذب"},
{"3","التذكرة في الفقه الشافعي لابن الملقن"},
{"4","كفاية الأخيار في حل غاية الإختصار"},
{"5","نهاية المحتاج إلى شرح المنهاج"},
{"6"," الوسيط في المذهب"},
{"7"," البيان في مذهب الإمام الشافعي"},
{"8","فتح العزيز بشرح الوجيز = الشرح الكبير"},
{"9","الغرر البهية في شرح البهجة الوردية"},
{"10","غاية البيان شرح زبد ابن رسلان"},
{"11","فتح المعين بشرح قرة العين بمهمات الدين"},
{"12","إعانة الطالبين على حل ألفاظ فتح المعين"},
{"13","المقدمة الحضرمية"},
{"14","أسنى المطالب في شرح روض الطالب"},
{"15"," المنهاج القويم"},
{"16","تحفة المحتاج في شرح المنهاج"},
{"17","الإقناع في حل ألفاظ أبي شجاع"},
{"18","حاشيتا قليوبي وعميرة"},
{"19","فتح الوهاب بشرح منهج الطلاب"},
{"20","حاشية البجيرمي على الخطيب"}

};
     
        HashMap<Integer,String> judulMap = new HashMap<>();
        
        for (int i = 0; i < judul.length; i++) {

            judulMap.put(Integer.parseInt(judul[i][0]), judul[i][1]);
            
        }
        
        return judulMap;
    } 
}
