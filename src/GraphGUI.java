import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

public class GraphGUI extends JFrame {
    private DirectedGraph graph;
    private JTextArea outputArea;
    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private AtomicBoolean walkInProgress = new AtomicBoolean(false);
    private Thread walkThread;
    
    // 卡片名称常量
    private static final String CARD_MAIN = "主页";
    private static final String CARD_BUILD_GRAPH = "构建有向图";
    private static final String CARD_QUERY_BRIDGE = "查询桥接词";
    private static final String CARD_GENERATE_TEXT = "生成新文本";
    private static final String CARD_SHORTEST_PATH = "计算最短路径";
    private static final String CARD_PAGE_RANK = "计算PageRank";
    private static final String CARD_RANDOM_WALK = "随机游走";
    private static final String CARD_SHOW_GRAPH = "显示有向图";

    public GraphGUI() {
        // 设置窗口标题和大小
        super("有向图处理系统");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // 创建菜单栏
        createMenuBar();
        
        // 创建主面板和卡片布局
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        
        // 创建输出区域 - 减小高度
        outputArea = new JTextArea(6, 50);  // 从10行减少到6行
        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("输出信息"));
        scrollPane.setPreferredSize(new Dimension(800, 150)); // 设置固定高度
        
        // 创建各功能卡片
        createMainCard();
        createBuildGraphCard();
        createQueryBridgeCard();
        createGenerateTextCard();
        createShortestPathCard();
        createPageRankCard();
        createRandomWalkCard();
        
        // 显示主卡片
        cardLayout.show(cardsPanel, CARD_MAIN);
        
        // 设置布局
        mainPanel.add(cardsPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);
        
        // 将主面板添加到窗口
        add(mainPanel);
        
        // 显示窗口
        setLocationRelativeTo(null);
        setVisible(true);
        
        // 输出欢迎信息
        appendOutput("欢迎使用有向图处理系统 GUI 版本！\n");
        appendOutput("请使用上方菜单选择功能。\n");
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // 文件菜单
        JMenu fileMenu = new JMenu("文件");
        JMenuItem buildGraphItem = new JMenuItem("从文件构建有向图");
        JMenuItem exitItem = new JMenuItem("退出");
        
        buildGraphItem.addActionListener(e -> cardLayout.show(cardsPanel, CARD_BUILD_GRAPH));
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(buildGraphItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // 功能菜单
        JMenu functionMenu = new JMenu("功能");
        JMenuItem showGraphItem = new JMenuItem("显示有向图");
        JMenuItem queryBridgeItem = new JMenuItem("查询桥接词");
        JMenuItem generateTextItem = new JMenuItem("根据桥接词生成新文本");
        JMenuItem shortestPathItem = new JMenuItem("计算最短路径");
        JMenuItem pageRankItem = new JMenuItem("计算PageRank值");
        JMenuItem randomWalkItem = new JMenuItem("随机游走");
        
        showGraphItem.addActionListener(e -> showGraph());
        queryBridgeItem.addActionListener(e -> cardLayout.show(cardsPanel, CARD_QUERY_BRIDGE));
        generateTextItem.addActionListener(e -> cardLayout.show(cardsPanel, CARD_GENERATE_TEXT));
        shortestPathItem.addActionListener(e -> cardLayout.show(cardsPanel, CARD_SHORTEST_PATH));
        pageRankItem.addActionListener(e -> cardLayout.show(cardsPanel, CARD_PAGE_RANK));
        randomWalkItem.addActionListener(e -> cardLayout.show(cardsPanel, CARD_RANDOM_WALK));
        
        functionMenu.add(showGraphItem);
        functionMenu.add(queryBridgeItem);
        functionMenu.add(generateTextItem);
        functionMenu.add(shortestPathItem);
        functionMenu.add(pageRankItem);
        functionMenu.add(randomWalkItem);
        
        // 帮助菜单
        JMenu helpMenu = new JMenu("帮助");
        JMenuItem aboutItem = new JMenuItem("关于");
        
        aboutItem.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, 
                "有向图处理系统 - GUI版本\n版本: 1.0\n作者: 软件工程实验1", 
                "关于", JOptionPane.INFORMATION_MESSAGE)
        );
        
        helpMenu.add(aboutItem);
        
        // 添加菜单到菜单栏
        menuBar.add(fileMenu);
        menuBar.add(functionMenu);
        menuBar.add(helpMenu);
        
        // 设置菜单栏
        setJMenuBar(menuBar);
    }
    
    private void createMainCard() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("有向图处理系统", JLabel.CENTER);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        
        // 修改为4行2列以适应7个按钮
        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 20, 20));
        
        String[] buttonLabels = {
            "从文件构建有向图", "显示有向图", 
            "查询桥接词", "根据桥接词生成新文本", 
            "计算最短路径", "计算PageRank值",
            "随机游走"
        };
        
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Dialog", Font.PLAIN, 14));
            
            button.addActionListener(e -> {
                switch (label) {
                    case "从文件构建有向图":
                        cardLayout.show(cardsPanel, CARD_BUILD_GRAPH);
                        break;
                    case "显示有向图":
                        showGraph();
                        break;
                    case "查询桥接词":
                        cardLayout.show(cardsPanel, CARD_QUERY_BRIDGE);
                        break;
                    case "根据桥接词生成新文本":
                        cardLayout.show(cardsPanel, CARD_GENERATE_TEXT);
                        break;
                    case "计算最短路径":
                        cardLayout.show(cardsPanel, CARD_SHORTEST_PATH);
                        break;
                    case "计算PageRank值":
                        cardLayout.show(cardsPanel, CARD_PAGE_RANK);
                        break;
                    case "随机游走":
                        cardLayout.show(cardsPanel, CARD_RANDOM_WALK);
                        break;
                }
            });
            
            buttonPanel.add(button);
        }
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        
        cardsPanel.add(panel, CARD_MAIN);
    }
    
    private void createBuildGraphCard() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("从文件构建有向图", JLabel.CENTER);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        
        // 创建中央内容面板，使用盒式布局让内容居中且不会拉伸
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        
        // 文件路径输入区
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setMaximumSize(new Dimension(500, 100));
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel pathLabel = new JLabel("文件路径:");
        pathLabel.setPreferredSize(new Dimension(80, 25));
        
        JTextField filePathField = new JTextField();
        filePathField.setPreferredSize(new Dimension(300, 25));
        
        JButton browseButton = new JButton("浏览...");
        
        // 按钮面板
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton buildButton = new JButton("构建有向图");
        JButton backButton = new JButton("返回主页");
        
        // 添加文件选择对话框功能
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("文本文件", "txt"));
            int result = fileChooser.showOpenDialog(GraphGUI.this);
            
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                filePathField.setText(selectedFile.getAbsolutePath());
            }
        });
        
        // 添加构建图功能
        buildButton.addActionListener(e -> {
            String filePath = filePathField.getText().trim();
            if (!filePath.isEmpty()) {
                buildGraphFromFile(filePath);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "请输入文件路径或选择文件", 
                    "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        backButton.addActionListener(e -> cardLayout.show(cardsPanel, CARD_MAIN));
        
        // 组装文件路径输入区
        JPanel fileSelectionPanel = new JPanel(new BorderLayout(5, 0));
        fileSelectionPanel.add(filePathField, BorderLayout.CENTER);
        fileSelectionPanel.add(browseButton, BorderLayout.EAST);
        
        inputPanel.add(pathLabel, BorderLayout.WEST);
        inputPanel.add(fileSelectionPanel, BorderLayout.CENTER);
        
        // 组装按钮面板
        buttonsPanel.add(buildButton);
        buttonsPanel.add(backButton);
        
        // 添加到内容面板
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(inputPanel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(buttonsPanel);
        contentPanel.add(Box.createVerticalGlue());
        
        // 组装主面板
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        
        cardsPanel.add(panel, CARD_BUILD_GRAPH);
    }
    
    private void createQueryBridgeCard() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("查询桥接词", JLabel.CENTER);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        
        // 创建中央内容面板，使用盒式布局让内容居中且不会拉伸
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        
        // 第一个单词输入区
        JPanel word1Panel = new JPanel(new BorderLayout(5, 0));
        word1Panel.setMaximumSize(new Dimension(400, 30));
        word1Panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel word1Label = new JLabel("第一个单词:");
        word1Label.setPreferredSize(new Dimension(80, 25));
        JTextField word1Field = new JTextField();
        
        word1Panel.add(word1Label, BorderLayout.WEST);
        word1Panel.add(word1Field, BorderLayout.CENTER);
        
        // 第二个单词输入区
        JPanel word2Panel = new JPanel(new BorderLayout(5, 0));
        word2Panel.setMaximumSize(new Dimension(400, 30));
        word2Panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel word2Label = new JLabel("第二个单词:");
        word2Label.setPreferredSize(new Dimension(80, 25));
        JTextField word2Field = new JTextField();
        
        word2Panel.add(word2Label, BorderLayout.WEST);
        word2Panel.add(word2Field, BorderLayout.CENTER);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton queryButton = new JButton("查询");
        JButton backButton = new JButton("返回主页");
        
        queryButton.addActionListener(e -> {
            if (checkGraphExists()) {
                String word1 = word1Field.getText().trim();
                String word2 = word2Field.getText().trim();
                
                if (!word1.isEmpty() && !word2.isEmpty()) {
                    String result = graph.queryBridgeWords(word1, word2);
                    appendOutput("查询结果：\n" + result + "\n");
                } else {
                    appendOutput("错误：请输入两个单词进行查询\n");
                }
            }
        });
        
        backButton.addActionListener(e -> cardLayout.show(cardsPanel, CARD_MAIN));
        
        buttonPanel.add(queryButton);
        buttonPanel.add(backButton);
        
        // 添加到内容面板
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(word1Panel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(word2Panel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(buttonPanel);
        contentPanel.add(Box.createVerticalGlue());
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        
        cardsPanel.add(panel, CARD_QUERY_BRIDGE);
    }
    
    private void createGenerateTextCard() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("根据桥接词生成新文本", JLabel.CENTER);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        
        JLabel textLabel = new JLabel("请输入文本:");
        JTextArea textArea = new JTextArea(5, 40);
        JScrollPane textScrollPane = new JScrollPane(textArea);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton generateButton = new JButton("生成");
        JButton backButton = new JButton("返回主页");
        buttonPanel.add(generateButton);
        buttonPanel.add(backButton);
        
        generateButton.addActionListener(e -> {
            if (checkGraphExists()) {
                String inputText = textArea.getText().trim();
                
                if (!inputText.isEmpty()) {
                    String newText = graph.generateNewText(inputText);
                    appendOutput("原始文本：" + inputText + "\n");
                    appendOutput("生成的新文本：" + newText + "\n");
                } else {
                    appendOutput("错误：请输入文本进行生成\n");
                }
            }
        });
        
        backButton.addActionListener(e -> cardLayout.show(cardsPanel, CARD_MAIN));
        
        inputPanel.add(textLabel, BorderLayout.NORTH);
        inputPanel.add(textScrollPane, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(inputPanel, BorderLayout.CENTER);
        
        cardsPanel.add(panel, CARD_GENERATE_TEXT);
    }
    
    private void createShortestPathCard() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("计算最短路径", JLabel.CENTER);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        
        // 创建中央内容面板，使用盒式布局让内容居中且不会拉伸
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        
        // 起始单词输入区
        JPanel startPanel = new JPanel(new BorderLayout(5, 0));
        startPanel.setMaximumSize(new Dimension(350, 30));
        startPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel startLabel = new JLabel("起始单词:");
        startLabel.setPreferredSize(new Dimension(100, 25));
        JTextField startField = new JTextField();
        
        startPanel.add(startLabel, BorderLayout.WEST);
        startPanel.add(startField, BorderLayout.CENTER);
        
        // 目标单词输入区
        JPanel endPanel = new JPanel(new BorderLayout(5, 0));
        endPanel.setMaximumSize(new Dimension(350, 30));
        endPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel endLabel = new JLabel("目标单词(可选):");
        endLabel.setPreferredSize(new Dimension(100, 25));
        JTextField endField = new JTextField();
        
        endPanel.add(endLabel, BorderLayout.WEST);
        endPanel.add(endField, BorderLayout.CENTER);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton calcButton = new JButton("计算");
        JButton backButton = new JButton("返回主页");
        
        calcButton.addActionListener(e -> {
            if (checkGraphExists()) {
                String startWord = startField.getText().trim();
                String endWord = endField.getText().trim();
                
                if (!startWord.isEmpty()) {
                    String result = graph.calcShortestPath(startWord, endWord.isEmpty() ? null : endWord);
                    appendOutput("最短路径结果：\n" + result + "\n");
                } else {
                    appendOutput("错误：请输入起始单词\n");
                }
            }
        });
        
        backButton.addActionListener(e -> cardLayout.show(cardsPanel, CARD_MAIN));
        
        buttonPanel.add(calcButton);
        buttonPanel.add(backButton);
        
        // 添加到内容面板
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(startPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(endPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(buttonPanel);
        contentPanel.add(Box.createVerticalGlue());
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        
        cardsPanel.add(panel, CARD_SHORTEST_PATH);
    }
    
    private void createPageRankCard() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("计算PageRank值", JLabel.CENTER);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        
        // 创建中央内容面板，使用盒式布局让内容居中且不会拉伸
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        
        // 单词输入区
        JPanel wordPanel = new JPanel(new BorderLayout(5, 0));
        wordPanel.setMaximumSize(new Dimension(300, 30));
        wordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel wordLabel = new JLabel("单词:");
        wordLabel.setPreferredSize(new Dimension(60, 25));
        JTextField wordField = new JTextField();
        
        wordPanel.add(wordLabel, BorderLayout.WEST);
        wordPanel.add(wordField, BorderLayout.CENTER);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton calcButton = new JButton("计算");
        JButton backButton = new JButton("返回主页");
        
        calcButton.addActionListener(e -> {
            if (checkGraphExists()) {
                String word = wordField.getText().trim();
                
                if (!word.isEmpty()) {
                    Double pr = graph.calPageRank(word);
                    if (pr < 0) {
                        appendOutput("单词 \"" + word + "\" 不在图中！\n");
                    } else {
                        appendOutput("单词 \"" + word + "\" 的PageRank值为：" + pr + "\n");
                    }
                } else {
                    appendOutput("错误：请输入单词进行计算\n");
                }
            }
        });
        
        backButton.addActionListener(e -> cardLayout.show(cardsPanel, CARD_MAIN));
        
        buttonPanel.add(calcButton);
        buttonPanel.add(backButton);
        
        // 添加到内容面板
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(wordPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(buttonPanel);
        contentPanel.add(Box.createVerticalGlue());
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        
        cardsPanel.add(panel, CARD_PAGE_RANK);
    }
    
    private void createRandomWalkCard() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("随机游走", JLabel.CENTER);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton startButton = new JButton("开始游走");
        JButton stopButton = new JButton("停止游走");
        JButton backButton = new JButton("返回主页");
        
        stopButton.setEnabled(false);
        
        startButton.addActionListener(e -> {
            if (checkGraphExists() && !walkInProgress.get()) {
                startRandomWalk();
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
            }
        });
        
        stopButton.addActionListener(e -> {
            stopRandomWalk();
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        });
        
        backButton.addActionListener(e -> {
            if (walkInProgress.get() && walkThread != null) {
                stopRandomWalk();
            }
            cardLayout.show(cardsPanel, CARD_MAIN);
        });
        
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(backButton);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        
        cardsPanel.add(panel, CARD_RANDOM_WALK);
    }
    
    private void buildGraphFromFile(String filePath) {
        appendOutput("正在从文件构建有向图: " + filePath + "\n");
        
        // 在后台线程中构建图，避免GUI卡顿
        new Thread(() -> {
            graph = DirectedGraph.buildGraphFromFile(filePath);
            SwingUtilities.invokeLater(() -> {
                appendOutput("有向图构建完成！\n");
                cardLayout.show(cardsPanel, CARD_MAIN);
            });
        }).start();
    }
    
    private void showGraph() {
        if (checkGraphExists()) {
            appendOutput("正在生成并显示有向图...\n");
            
            // 在后台线程中显示图，避免GUI卡顿
            new Thread(() -> {
                DirectedGraph.showDirectedGraph(graph);
                SwingUtilities.invokeLater(() -> {
                    appendOutput("有向图已生成。\n");
                    displayGraphInGUI();
                });
            }).start();
        }
    }
    
    private void displayGraphInGUI() {
        try {
            // 创建图像显示卡片
            JPanel graphPanel = new JPanel(new BorderLayout());
            graphPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // 创建标题
            JLabel titleLabel = new JLabel("有向图可视化", JLabel.CENTER);
            titleLabel.setFont(new Font("Dialog", Font.BOLD, 18));
            
            // 使用JLabel显示PNG图像
            String pngFilePath = new File("directed_graph.png").getAbsolutePath();
            File pngFile = new File(pngFilePath);
            
            if (pngFile.exists()) {
                try {
                    // 加载图像
                    ImageIcon imageIcon = new ImageIcon(pngFilePath);
                    
                    // 创建一个可滚动的图像显示面板
                    JLabel imageLabel = new JLabel(imageIcon);
                    imageLabel.setHorizontalAlignment(JLabel.CENTER);
                    
                    JScrollPane scrollPane = new JScrollPane(imageLabel);
                    scrollPane.setPreferredSize(new Dimension(700, 500));
                    
                    graphPanel.add(scrollPane, BorderLayout.CENTER);
                } catch (Exception e) {
                    JLabel errorLabel = new JLabel("加载图像时出错: " + e.getMessage(), JLabel.CENTER);
                    errorLabel.setForeground(Color.RED);
                    graphPanel.add(errorLabel, BorderLayout.CENTER);
                }
            } else {
                JLabel errorLabel = new JLabel("无法找到图像文件: " + pngFilePath, JLabel.CENTER);
                errorLabel.setForeground(Color.RED);
                graphPanel.add(errorLabel, BorderLayout.CENTER);
            }
            
            // 添加返回按钮
            JPanel buttonPanel = new JPanel();
            JButton backButton = new JButton("返回主页");
            backButton.addActionListener(e -> cardLayout.show(cardsPanel, CARD_MAIN));
            buttonPanel.add(backButton);
            
            graphPanel.add(titleLabel, BorderLayout.NORTH);
            graphPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            // 添加到卡片面板
            cardsPanel.add(graphPanel, CARD_SHOW_GRAPH);
            
            // 显示图形卡片
            cardLayout.show(cardsPanel, CARD_SHOW_GRAPH);
        } catch (Exception e) {
            appendOutput("显示图像时出错: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }
    
    private void startRandomWalk() {
        appendOutput("开始随机游走...\n");
        walkInProgress.set(true);
        
        walkThread = new Thread(() -> {
            StringBuilder walkPath = new StringBuilder();
            
            // 获取图的节点
            if (graph.indexToWord.isEmpty()) {
                SwingUtilities.invokeLater(() -> {
                    appendOutput("图为空，无法进行随机游走。\n");
                    walkInProgress.set(false);
                    enableStartButton();
                });
                return;
            }
            
            // 随机选择起始节点
            int currentIndex = graph.random.nextInt(graph.indexToWord.size());
            final String initialWord = graph.indexToWord.get(currentIndex);
            SwingUtilities.invokeLater(() -> appendOutput("游走路径: " + initialWord));
            walkPath.append(initialWord);
            
            // 已访问的边集合，使用"from-to"格式的字符串表示
            Set<String> visitedEdges = new HashSet<>();
            
            int steps = 0;
            String currentWord = initialWord; // 当前单词，可以修改
            while (!Thread.currentThread().isInterrupted() && walkInProgress.get()) {
                steps++;
                
                // 获取当前节点的所有出边
                java.util.List<Integer> neighbors = new java.util.ArrayList<>();
                for (int i = 0; i < graph.indexToWord.size(); i++) {
                    if (graph.adjacencyMatrix[currentIndex][i] > 0) {
                        neighbors.add(i);
                    }
                }
                
                // 如果没有出边，结束游走
                if (neighbors.isEmpty()) {
                    final String msg = "\n当前节点没有出边，游走结束。";
                    SwingUtilities.invokeLater(() -> appendOutput(msg));
                    break;
                }
                
                // 随机选择一条边
                int nextIndex = neighbors.get(graph.random.nextInt(neighbors.size()));
                String nextWord = graph.indexToWord.get(nextIndex);
                
                // 检查边是否已访问过
                String edge = currentIndex + "-" + nextIndex;
                if (visitedEdges.contains(edge)) {
                    final String edgeRepeatMsg = "\n发现重复边：" + currentWord + " -> " + nextWord + "，游走结束。";
                    SwingUtilities.invokeLater(() -> appendOutput(edgeRepeatMsg));
                    break;
                }
                
                // 标记边为已访问
                visitedEdges.add(edge);
                
                final String stepOutput = " -> " + nextWord;
                SwingUtilities.invokeLater(() -> appendOutput(stepOutput));
                
                // 更新路径
                walkPath.append(" -> ").append(nextWord);
                
                // 更新当前节点
                currentIndex = nextIndex;
                currentWord = nextWord;
                
                try {
                    Thread.sleep(500);  // 0.5秒延迟
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            
            final int finalSteps = steps;
            final String finalPath = walkPath.toString();
            
            SwingUtilities.invokeLater(() -> {
                appendOutput("\n随机游走完成，共" + finalSteps + "步。\n");
                appendOutput("完整路径：" + finalPath + "\n");
                walkInProgress.set(false);
                enableStartButton();
            });
        });
        
        walkThread.start();
    }
    
    // 辅助方法，用于启用"开始游走"按钮，禁用"停止游走"按钮
    private void enableStartButton() {
        for (Component component : cardsPanel.getComponents()) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                if (panel.getComponentCount() > 0 && panel.getComponent(0) instanceof JLabel) {
                    JLabel label = (JLabel) panel.getComponent(0);
                    if ("随机游走".equals(label.getText())) {
                        // 找到随机游走面板
                        for (Component c : panel.getComponents()) {
                            if (c instanceof JPanel) {
                                // 遍历按钮面板中的按钮
                                for (Component btn : ((JPanel) c).getComponents()) {
                                    if (btn instanceof JButton) {
                                        JButton button = (JButton) btn;
                                        if ("开始游走".equals(button.getText())) {
                                            button.setEnabled(true);
                                        } else if ("停止游走".equals(button.getText())) {
                                            button.setEnabled(false);
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
    }
    
    private void stopRandomWalk() {
        if (walkInProgress.get() && walkThread != null) {
            walkInProgress.set(false);
            walkThread.interrupt();
            appendOutput("\n用户手动停止了游走。\n");
            
            // 等待线程实际结束
            try {
                walkThread.join(1000); // 等待最多1秒钟
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // 此处不需要手动启用"开始游走"按钮，因为中断的线程会在退出时通过SwingUtilities调用enableStartButton
        }
    }
    
    private boolean checkGraphExists() {
        if (graph == null) {
            JOptionPane.showMessageDialog(this, 
                    "请先构建有向图！", 
                    "提示", 
                    JOptionPane.WARNING_MESSAGE);
            cardLayout.show(cardsPanel, CARD_BUILD_GRAPH);
            return false;
        }
        return true;
    }
    
    private void appendOutput(String text) {
        outputArea.append(text);
        // 自动滚动到底部
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }
    
    public static void main(String[] args) {
        try {
            // 使用系统外观
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 在EDT线程中创建GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GraphGUI();
            }
        });
    }
} 