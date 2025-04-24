import java.util.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DirectedGraph {
    // 图的数据结构定义
    public final Map<String, Integer> wordToIndex; // 单词到索引的映射
    public final java.util.List<String> indexToWord; // 索引到单词的映射
    public int[][] adjacencyMatrix; // 邻接矩阵
    public final java.util.List<java.util.List<Integer>> adjacencyList; // 邻接表
    public final Random random = new Random();

    /**
     * 构造函数
     */
    public DirectedGraph() {
        wordToIndex = new HashMap<>();
        indexToWord = new ArrayList<>();
        adjacencyList = new ArrayList<>();
    }

    /**
     * 展示有向图
     * @param G 有向图对象
     */
    public static void showDirectedGraph(DirectedGraph G) {
        if (G == null || G.indexToWord.isEmpty()) {
            System.out.println("图为空，无法显示。");
            return;
        }
        
        System.out.println("有向图中的节点：");
        for (String word : G.indexToWord) {
            System.out.print(word + " ");
        }
        System.out.println("\n");
        
        System.out.println("有向图中的边：");
        for (int i = 0; i < G.indexToWord.size(); i++) {
            String fromWord = G.indexToWord.get(i);
            for (int j = 0; j < G.adjacencyMatrix[i].length; j++) {
                if (G.adjacencyMatrix[i][j] > 0) {
                    String toWord = G.indexToWord.get(j);
                    System.out.println(fromWord + " -> " + toWord + " (权重: " + G.adjacencyMatrix[i][j] + ")");
                }
            }
        }
        
        // 使用Graphviz生成图像
        generateGraphvizImage(G, "directed_graph.png");
    }
    
    /**
     * 生成DOT语言描述文件
     * @param G 有向图对象
     * @param dotFilePath 输出的DOT文件路径
     */
    private static void generateDotFile(DirectedGraph G, String dotFilePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(dotFilePath))) {
            writer.println("digraph G {");
            
            // // 添加节点定义
            // for (String word : G.indexToWord) {
            //     writer.println("  \"" + word + "\" [label=\"" + word + "\"];");
            // }
            
            // 添加边定义
            for (int i = 0; i < G.indexToWord.size(); i++) {
                String fromWord = G.indexToWord.get(i);
                for (int j = 0; j < G.adjacencyMatrix[i].length; j++) {
                    if (G.adjacencyMatrix[i][j] > 0) {
                        String toWord = G.indexToWord.get(j);
                        writer.println("  \"" + fromWord + "\" -> \"" + toWord + "\" [label=\"" + G.adjacencyMatrix[i][j] + "\"];");
                    }
                }
            }
            
            writer.println("}");
        } catch (IOException e) {
            System.err.println("生成DOT文件时出错: " + e.getMessage());
        }
    }
    
    /**
     * 使用Graphviz生成图像文件
     * @param G 有向图对象
     * @param outputImagePath 输出的图像文件路径
     */
    private static void generateGraphvizImage(DirectedGraph G, String outputImagePath) {
        // 先生成DOT文件
        String dotFilePath = "graph.dot";
        generateDotFile(G, dotFilePath);
        
        // Graphviz可执行文件路径
        String graphvizPath = "D:\\Apps\\Tools\\Graphviz-12.2.1-win64\\bin\\dot.exe"; // 默认为空，表示从PATH中查找
        
        try {
            // 执行Graphviz命令
            ProcessBuilder pb = new ProcessBuilder();
            
            // 根据操作系统设置命令
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                // 如果提供了Graphviz路径，则使用，否则从PATH中查找
                pb.command("cmd.exe", "/c", graphvizPath, "-Tpng", dotFilePath, "-o", outputImagePath);
            } else {
                // 对于Unix/Linux/Mac系统
                String dotCmd = graphvizPath + "/dot";
                pb.command(dotCmd, "-Tpng", dotFilePath, "-o", outputImagePath);
            }
            
            // 启动进程
            final Process process = pb.start();
            System.out.println("Graphviz进程已启动");
            
            // 创建一个线程监控进程执行时间
            final boolean[] completed = {false};
            Thread watchdog = new Thread(() -> {
                try {
                    // 等待最多2秒
                    if (process.waitFor(2, java.util.concurrent.TimeUnit.SECONDS)) {
                        completed[0] = true;
                    } else {
                        // 超时，需要终止dot.exe进程
                        System.err.println("\n⚠️ 警告: 图形绘制超时（超过2秒）");
                        
                        // 首先尝试终止当前进程
                        process.destroyForcibly();
                        
                        // 在Windows系统上，专门查找和终止所有dot.exe进程
                        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                            try {
                                // 使用taskkill命令终止所有dot.exe进程
                                ProcessBuilder killPb = new ProcessBuilder("taskkill", "/F", "/IM", "dot.exe");
                                Process killProcess = killPb.start();
                                
                                // 获取命令输出
                                StringBuilder output = new StringBuilder();
                                try (BufferedReader reader = new BufferedReader(new InputStreamReader(killProcess.getInputStream()))) {
                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        output.append(line).append("\n");
                                    }
                                }
                                
                                // 获取命令错误
                                StringBuilder error = new StringBuilder();
                                try (BufferedReader reader = new BufferedReader(new InputStreamReader(killProcess.getErrorStream()))) {
                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        error.append(line).append("\n");
                                    }
                                }
                                
                                // 等待taskkill命令执行完成
                                killProcess.waitFor();
                                
                                // 输出终止结果
                                if (output.length() > 0) {
                                    System.err.println("终止dot.exe进程结果: " + output.toString().trim());
                                }
                                if (error.length() > 0) {
                                    System.err.println("终止dot.exe进程错误: " + error.toString().trim());
                                }
                                
                                System.err.println("已尝试终止所有dot.exe进程");
                            } catch (Exception e) {
                                System.err.println("终止dot.exe进程失败: " + e.getMessage());
                            }
                        }
                        
                        System.err.println("图形可能过于复杂，无法绘制。");
                        System.err.println("DOT文件已保存至: " + new File(dotFilePath).getAbsolutePath());
                        System.err.println("您可以尝试使用其他Graphviz布局引擎（如neato或fdp）或增加超时时间来绘制此图。");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            
            watchdog.start();
            
            try {
                watchdog.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            if (completed[0]) {
                // 获取命令执行的错误信息
                StringBuilder errorInfo = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        errorInfo.append(line).append("\n");
                    }
                }
                
                // 等待进程执行完成
                int exitCode = process.exitValue();
                
                if (exitCode == 0) {
                    System.out.println("有向图已生成并保存为PNG文件: " + outputImagePath);
                    System.out.println("DOT文件已保留: " + new File(dotFilePath).getAbsolutePath());
                } else {
                    System.err.println("Graphviz执行失败，错误代码: " + exitCode);
                    System.err.println("错误信息: " + errorInfo.toString());
                    System.err.println("请确保已安装Graphviz并将其添加到系统PATH中");
                    System.err.println("或在程序中设置graphvizPath变量为Graphviz的安装路径");
                    System.err.println("可从https://graphviz.org/download/下载Graphviz");
                    
                    // 尝试使用备选方法 - 直接打开DOT文件并请求用户使用Graphviz处理
                    System.err.println("\n您可以手动使用Graphviz处理生成的DOT文件: " + new File(dotFilePath).getAbsolutePath());
                    System.err.println("命令示例: dot -Tpng " + dotFilePath + " -o " + outputImagePath);
                }
            }
        } catch (IOException e) {
            System.err.println("执行Graphviz命令时出错: " + e.getMessage());
            System.err.println("请确保已安装Graphviz并将其添加到系统PATH中");
            System.err.println("或在程序中设置graphvizPath变量为Graphviz的安装路径");
            System.err.println("可从https://graphviz.org/download/下载Graphviz");
            
            // 保留DOT文件以便用户手动处理
            System.err.println("\n已保留DOT文件: " + new File(dotFilePath).getAbsolutePath());
            System.err.println("您可以手动使用Graphviz处理它");
            System.err.println("命令示例: dot -Tpng " + dotFilePath + " -o " + outputImagePath);
        }
    }

    /**
     * 查询桥接词
     * @param word1 第一个单词
     * @param word2 第二个单词
     * @return 包含查询结果的字符串
     */
    public String queryBridgeWords(String word1, String word2) {
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();
        
        // 检查单词是否存在于图中
        if (!wordToIndex.containsKey(word1) && !wordToIndex.containsKey(word2)) {
            return "No \"" + word1 + "\" and \"" + word2 + "\" in the graph!";
        } else if (!wordToIndex.containsKey(word1)) {
            return "No \"" + word1 + "\" in the graph!";
        } else if (!wordToIndex.containsKey(word2)) {
            return "No \"" + word2 + "\" in the graph!";
        }
        
        int index1 = wordToIndex.get(word1);
        int index2 = wordToIndex.get(word2);
        
        java.util.List<String> bridgeWords = new ArrayList<>();
        
        // 寻找桥接词
        for (int i = 0; i < indexToWord.size(); i++) {
            if (adjacencyMatrix[index1][i] > 0 && adjacencyMatrix[i][index2] > 0) {
                bridgeWords.add(indexToWord.get(i));
            }
        }
        
        if (bridgeWords.isEmpty()) {
            return "No bridge words from \"" + word1 + "\" to \"" + word2 + "\"!";
        } else if (bridgeWords.size() == 1) {
            return "The bridge word from \"" + word1 + "\" to \"" + word2 + "\" is: " +"\"" + bridgeWords.get(0)+"\"";
        } else {
            StringBuilder result = new StringBuilder("The bridge words from \"" + word1 + "\" to \"" + word2 + "\" are: ");
            
            for (int i = 0; i < bridgeWords.size() - 1; i++) {
                result.append("\"" + bridgeWords.get(i)+ "\"");
                if (i < bridgeWords.size() - 2) {
                    result.append(", ");
                } else {
                    result.append(" and ");
                }
            }
            result.append(bridgeWords.get(bridgeWords.size() - 1)).append(".");
            
            return result.toString();
        }
    }

    /**
     * 根据bridge word生成新文本
     * @param inputText 输入文本
     * @return 生成的新文本
     */
    public String generateNewText(String inputText) {
        if (inputText == null || inputText.trim().isEmpty()) {
            return "";
        }
        
        // 提取输入文本中的单词，同时保留原始形式用于输出
        Pattern pattern = Pattern.compile("\\b[a-zA-Z]+\\b");
        Matcher matcher = pattern.matcher(inputText);
        java.util.List<String> words = new ArrayList<>(); // 小写形式，用于匹配
        java.util.List<String> originalWords = new ArrayList<>(); // 原始形式，用于输出
        
        while (matcher.find()) {
            String originalWord = matcher.group();
            originalWords.add(originalWord);
            words.add(originalWord.toLowerCase());
        }
        
        if (words.size() <= 1) {
            return inputText;
        }
        
        StringBuilder newText = new StringBuilder();
        
        // 处理第一个单词
        newText.append(originalWords.get(0));
        
        // 处理后续单词，检查是否有桥接词并插入
        for (int i = 1; i < words.size(); i++) {
            String prevWord = words.get(i-1);
            String currWord = words.get(i);
            
            // 检查是否有桥接词
            String bridgeWordResult = queryBridgeWords(prevWord, currWord);
            
            // 如果有桥接词，添加到结果中
            if (!bridgeWordResult.startsWith("No")) {
                // 提取桥接词
                java.util.List<String> bridgeWords = new ArrayList<>();
                if (bridgeWordResult.contains("is: ")) {
                    // 处理形如 "is: "xxx"" 的情况
                    String bridgeWordSection = bridgeWordResult.substring(bridgeWordResult.indexOf("is: ") + 4).trim();
                    // 移除引号
                    String bridgeWord = bridgeWordSection.replaceAll("^\"|\"$", "");
                    bridgeWords.add(bridgeWord);
                } else if (bridgeWordResult.contains("are: ")) {
                    String bridgeWordsSection = bridgeWordResult.substring(bridgeWordResult.indexOf("are: ") + 5, 
                                                             bridgeWordResult.length() - 1); // 去掉末尾的点号
                    
                    if (bridgeWordsSection.contains(" and ")) {
                        // 处理形如 "x, y, and z" 的情况
                        String beforeAnd = bridgeWordsSection.substring(0, bridgeWordsSection.lastIndexOf(" and "));
                        String afterAnd = bridgeWordsSection.substring(bridgeWordsSection.lastIndexOf(" and ") + 5);
                        
                        // 移除引号
                        afterAnd = afterAnd.replaceAll("^\"|\"$", "");
                        
                        if (beforeAnd.contains(",")) {
                            String[] beforeParts = beforeAnd.split(", ");
                            for (String part : beforeParts) {
                                // 移除引号
                                part = part.replaceAll("^\"|\"$", "");
                                bridgeWords.add(part);
                            }
                        } else if (!beforeAnd.isEmpty()) {
                            // 移除引号
                            beforeAnd = beforeAnd.replaceAll("^\"|\"$", "");
                            bridgeWords.add(beforeAnd);
                        }
                        
                        bridgeWords.add(afterAnd);
                    } else {
                        // 只有一个桥接词的情况
                        // 移除引号
                        String bridgeWord = bridgeWordsSection.replaceAll("^\"|\"$", "");
                        bridgeWords.add(bridgeWord);
                    }
                }
                
                // 随机选择一个桥接词并添加
                if (!bridgeWords.isEmpty()) {
                    String selectedBridge = bridgeWords.get(random.nextInt(bridgeWords.size()));
                    newText.append(" ").append(selectedBridge);
                }
            }
            
            // 添加当前单词
            newText.append(" ").append(originalWords.get(i));
        }
        
        return newText.toString();
    }

    /**
     * 计算两个单词之间的最短路径
     * @param word1 起始单词
     * @param word2 目标单词
     * @return 最短路径的描述字符串
     */
    public String calcShortestPath(String word1, String word2) {
        word1 = word1.toLowerCase();
        
        // 检查起始单词是否存在
        if (!wordToIndex.containsKey(word1)) {
            return "起始单词 \"" + word1 + "\" 不在图中！";
        }
        
        // 如果第二个单词为空或null，计算到任意一个其他单词的最短路径
        if (word2 == null || word2.isEmpty()) {
            return calcShortestPathToAnyWord(word1);
        }
        
        word2 = word2.toLowerCase();
        
        // 检查单词是否存在
        if (!wordToIndex.containsKey(word1) && !wordToIndex.containsKey(word2)) {
            return "No \"" + word1 + "\" and \"" + word2 + "\" in the graph!";
        } else if (!wordToIndex.containsKey(word1)) {
            return "No \"" + word1 + "\" in the graph!";
        } else if (!wordToIndex.containsKey(word2)) {
            return "No \"" + word2 + "\" in the graph!";
        }
        
        int startIndex = wordToIndex.get(word1);
        int endIndex = wordToIndex.get(word2);
        
        if (startIndex == endIndex) {
            return "The shortest path from \"" + word1 + "\" to \"" + word2 + "\" is: " + word1;
        }
        
        // 使用Dijkstra算法找出所有最短路径
        int n = indexToWord.size();
        int[] distance = new int[n];
        Map<Integer, List<Integer>> predecessors = new HashMap<>(); // 存储所有可能的前驱
        boolean[] visited = new boolean[n];
        
        // 初始化距离和前驱列表
        for (int i = 0; i < n; i++) {
            distance[i] = Integer.MAX_VALUE;
            predecessors.put(i, new ArrayList<>());
        }
        
        distance[startIndex] = 0;
        
        // Dijkstra算法找出最短距离
        for (int i = 0; i < n; i++) {
            // 找到距离最小的未访问节点
            int minIndex = -1;
            int minDistance = Integer.MAX_VALUE;
            
            for (int j = 0; j < n; j++) {
                if (!visited[j] && distance[j] < minDistance) {
                    minIndex = j;
                    minDistance = distance[j];
                }
            }
            
            if (minIndex == -1) {
                break;
            }
            
            visited[minIndex] = true;
            
            // 更新相邻节点的距离
            for (int j = 0; j < n; j++) {
                if (adjacencyMatrix[minIndex][j] > 0 && !visited[j]) {
                    int newDistance = distance[minIndex] + adjacencyMatrix[minIndex][j];
                    
                    if (newDistance < distance[j]) {
                        // 如果找到更短的路径，更新距离并清除之前的前驱
                        distance[j] = newDistance;
                        predecessors.get(j).clear();
                        predecessors.get(j).add(minIndex);
                    } else if (newDistance == distance[j]) {
                        // 如果找到相同长度的路径，添加这个前驱，多条路径及对应多条前驱
                        predecessors.get(j).add(minIndex);
                    }
                }
            }
        }
        
        // 如果无法到达终点
        if (distance[endIndex] == Integer.MAX_VALUE) {
            return "No path from \"" + word1 + "\" to \"" + word2 + "\"!";
        }
        
        // 使用深度优先搜索找出所有最短路径
        List<List<Integer>> allPaths = new ArrayList<>();
        findAllPaths(startIndex, endIndex, predecessors, new ArrayList<>(), allPaths);
        
        // 构建结果字符串
        StringBuilder result = new StringBuilder();
        result.append("从 \"").append(word1).append("\" 到 \"").append(word2).append("\" 的最短路径长度为 ")
              .append(distance[endIndex]).append(":\n");
        
        for (int i = 0; i < allPaths.size(); i++) {
            List<Integer> path = allPaths.get(i);
            result.append("路径 ").append(i + 1).append(": ");
            
            for (int j = 0; j < path.size(); j++) {
                result.append(indexToWord.get(path.get(j)));
                if (j < path.size() - 1) {
                    result.append(" -> ");
                }
            }
            
            if (i < allPaths.size() - 1) {
                result.append("\n");
            }
        }
        
        return result.toString();
    }
    
    /**
     * 深度优先搜索找出所有从start到end的最短路径
     * @param start 起始节点索引
     * @param end 终点节点索引
     * @param predecessors 前驱节点列表
     * @param currentPath 当前路径
     * @param allPaths 所有路径的列表
     */
    private void findAllPaths(int start, int end, Map<Integer, List<Integer>> predecessors, 
                              List<Integer> currentPath, List<List<Integer>> allPaths) {
        // 将当前节点添加到路径开头
        currentPath.add(0, end);
        
        // 如果到达起始节点，保存该路径
        if (end == start) {
            allPaths.add(new ArrayList<>(currentPath));
        } else {
            // 遍历所有前驱
            for (int pred : predecessors.get(end)) {
                findAllPaths(start, pred, predecessors, currentPath, allPaths);
            }
        }
        
        // 回溯，移除当前节点
        currentPath.remove(0);
    }
    
    /**
     * 计算从指定单词到任意一个其他单词的最短路径
     * @param startWord 起始单词
     * @return 最短路径的描述字符串
     */
    private String calcShortestPathToAnyWord(String startWord) {
        int startIndex = wordToIndex.get(startWord);
        int n = indexToWord.size();
        
        // 使用Dijkstra算法
        int[] distance = new int[n];
        int[] predecessor = new int[n];
        boolean[] visited = new boolean[n];
        
        Arrays.fill(distance, Integer.MAX_VALUE);
        Arrays.fill(predecessor, -1);
        
        distance[startIndex] = 0;
        
        for (int i = 0; i < n; i++) {
            // 找到距离最小的未访问节点
            int minIndex = -1;
            int minDistance = Integer.MAX_VALUE;
            
            for (int j = 0; j < n; j++) {
                if (!visited[j] && distance[j] < minDistance) {
                    minIndex = j;
                    minDistance = distance[j];
                }
            }
            
            if (minIndex == -1) {
                break;
            }
            
            visited[minIndex] = true;
            
            // 更新相邻节点的距离
            for (int j = 0; j < n; j++) {
                if (adjacencyMatrix[minIndex][j] > 0 && !visited[j]) {
                    int newDistance = distance[minIndex] + adjacencyMatrix[minIndex][j];
                    
                    if (newDistance < distance[j]) {
                        distance[j] = newDistance;
                        predecessor[j] = minIndex;
                    }
                }
            }
        }
        
        // 收集所有可达的非起始节点
        List<Integer> reachableNodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (i != startIndex && distance[i] != Integer.MAX_VALUE) {
                reachableNodes.add(i);
            }
        }
        
        // 如果没有找到任何可达的其他单词
        if (reachableNodes.isEmpty()) {
            return "从 \"" + startWord + "\" 无法到达任何其他单词!";
        }
        
        // 随机选择一个可达节点
        int randomIndex = random.nextInt(reachableNodes.size());
        int targetIndex = reachableNodes.get(randomIndex);
        
        // 构建路径
        String endWord = indexToWord.get(targetIndex);
        java.util.List<Integer> path = new ArrayList<>();
        int current = targetIndex;
        
        while (current != -1) {
            path.add(current);
            current = predecessor[current];
        }
        
        Collections.reverse(path);
        
        StringBuilder result = new StringBuilder("从 \"" + startWord + "\" 到 随机选择的单词 \"" + endWord + "\" 的最短路径是: ");
        
        for (int i = 0; i < path.size(); i++) {
            result.append(indexToWord.get(path.get(i)));
            if (i < path.size() - 1) {
                result.append(" -> ");
            }
        }
        
        return result.toString();
    }

    /**
     * 计算单词的PageRank值
     * @param word 单词
     * @return PageRank值
     */
    public Double calPageRank(String word) {
        word = word.toLowerCase();
        
        if (!wordToIndex.containsKey(word)) {
            return -1.0; // 单词不在图中
        }
        
        int n = indexToWord.size();
        double d = 0.85; // 按要求设定为0.85
        double[] PR = new double[n];
        double[] newPR = new double[n];
        
        // 初始化PageRank值
        Arrays.fill(PR, 1.0 / n);
        
        // 迭代计算PageRank，直到收敛
        double diff;
        int maxIterations = 100;
        
        for (int iter = 0; iter < maxIterations; iter++) {
            // 计算新的PageRank值
            for (int i = 0; i < n; i++) {
                newPR[i] = (1 - d) / n;
                
                for (int j = 0; j < n; j++) {
                    if (adjacencyMatrix[j][i] > 0) {
                        // 计算j的出度
                        int outDegree = 0;
                        for (int k = 0; k < n; k++) {
                            outDegree += adjacencyMatrix[j][k];
                        }
                        
                        if (outDegree > 0) {
                            newPR[i] += d * PR[j] * adjacencyMatrix[j][i] / outDegree;
                        }
                    }
                }
            }
            
            // 检查收敛
            diff = 0;
            for (int i = 0; i < n; i++) {
                diff += Math.abs(newPR[i] - PR[i]);
            }
            
            // 更新PageRank值
            System.arraycopy(newPR, 0, PR, 0, n);
            
            if (diff < 1e-6) {
                break;
            }
        }
        
        return PR[wordToIndex.get(word)];
    }

    /**
     * 在图上进行随机游走
     * @return 随机游走路径的字符串描述
     */
    public String randomWalk() {
        if (indexToWord.isEmpty()) {
            return "图为空，无法进行随机游走。";
        }
        
        // 已访问的边集合，使用"from-to"格式的字符串表示
        Set<String> visitedEdges = new HashSet<>();
        StringBuilder path = new StringBuilder();
        
        // 随机选择起始节点
        int currentIndex = random.nextInt(indexToWord.size());
        String currentWord = indexToWord.get(currentIndex);
        System.out.println("随机游走开始，起始单词：" + currentWord);
        path.append(currentWord);
        
        // 创建一个标志，用于用户中断游走
        final boolean[] shouldStop = {false};
        
        // 创建一个线程监听用户输入
        Thread inputThread = new Thread(() -> {
            try {
                System.out.println("游走过程中，随时按回车键停止游走...");
                System.in.read();
                shouldStop[0] = true;
                // 清空输入缓冲区
                while (System.in.available() > 0) {
                    System.in.read();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        inputThread.setDaemon(true); // 设置为守护线程，主线程结束时自动结束
        inputThread.start();
        
        // 记录总步数
        int steps = 0;
        
        // 显示起始单词
        System.out.print("游走路径: " + currentWord);
        
        try {
            while (!shouldStop[0]) {  // 检查用户是否要求停止
                steps++;
                
                // 获取当前节点的所有出边
                java.util.List<Integer> neighbors = new ArrayList<>();
                for (int i = 0; i < indexToWord.size(); i++) {
                    if (adjacencyMatrix[currentIndex][i] > 0) {
                        neighbors.add(i);
                    }
                }
                
                // 如果没有出边，结束游走
                if (neighbors.isEmpty()) {
                    System.out.println("\n当前节点没有出边，游走结束。");
                    break;
                }
                
                // 随机选择一条边
                int nextIndex = neighbors.get(random.nextInt(neighbors.size()));
                String nextWord = indexToWord.get(nextIndex);
                
                // 检查边是否已访问过
                String edge = currentIndex + "-" + nextIndex;
                if (visitedEdges.contains(edge)) {
                    System.out.println("\n发现重复边：" + currentWord + " -> " + nextWord + "，游走结束。");
                    break;
                }
                
                // 标记边为已访问
                visitedEdges.add(edge);
                
                // 更新路径
                path.append(" -> ").append(nextWord);
                
                // 在同一行显示当前步骤
                System.out.print(" -> " + nextWord);
                
                // 更新当前节点
                currentIndex = nextIndex;
                currentWord = nextWord;
                
                // 添加延迟，使游走不会太快
                Thread.sleep(1000);  // 1秒延迟
            }
        } catch (InterruptedException e) {
            System.out.println("\n游走被中断。");
        }
        
        if (shouldStop[0]) {
            System.out.println("\n用户手动停止了游走。");
        }
        
        System.out.println("\n随机游走完成，共" + steps + "步。");
        System.out.println("完整路径：" + path.toString());
        
        // 将游走路径写入文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("random_walk_path.txt"))) {
            writer.write(path.toString());
            System.out.println("随机游走路径已保存到文件 'random_walk_path.txt'");
        } catch (IOException e) {
            System.err.println("保存随机游走路径文件时出错: " + e.getMessage());
        }
        
        return path.toString();
    }

    /**
     * 从文本文件构建有向图
     * @param filePath 文件路径
     * @return 构建好的有向图
     */
    public static DirectedGraph buildGraphFromFile(String filePath) {
        DirectedGraph graph = new DirectedGraph();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String previousWord = null;
            
            while ((line = reader.readLine()) != null) {
                // 使用正则表达式提取单词
                Pattern pattern = Pattern.compile("\\b[a-zA-Z]+\\b");
                Matcher matcher = pattern.matcher(line.toLowerCase());
                
                while (matcher.find()) {
                    String currentWord = matcher.group();
                    
                    // 将单词添加到图中
                    if (!graph.wordToIndex.containsKey(currentWord)) {
                        int index = graph.indexToWord.size();
                        graph.wordToIndex.put(currentWord, index);
                        graph.indexToWord.add(currentWord);
                        graph.adjacencyList.add(new ArrayList<>());
                    }
                    
                    // 添加边
                    if (previousWord != null) {
                        int fromIndex = graph.wordToIndex.get(previousWord);
                        int toIndex = graph.wordToIndex.get(currentWord);
                        graph.adjacencyList.get(fromIndex).add(toIndex);
                    }
                    
                    previousWord = currentWord;
                }
            }
            
            // 创建邻接矩阵
            int size = graph.indexToWord.size();
            graph.adjacencyMatrix = new int[size][size];
            
            for (int i = 0; i < size; i++) {
                for (Integer j : graph.adjacencyList.get(i)) {
                    graph.adjacencyMatrix[i][j]++;
                }
            }
            
        } catch (IOException e) {
            System.err.println("读取文件错误: " + e.getMessage());
        }
        
        return graph;
    }

    /**
     * 主程序入口
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DirectedGraph graph = null;
        
        System.out.println("欢迎使用有向图处理系统！");
        
        while (true) {
            System.out.println("\n请选择功能：");
            System.out.println("1. 从文件构建有向图");
            System.out.println("2. 显示有向图");
            System.out.println("3. 查询桥接词");
            System.out.println("4. 根据桥接词生成新文本");
            System.out.println("5. 计算两个单词之间的最短路径");
            System.out.println("6. 计算单词的PageRank值");
            System.out.println("7. 随机游走");
            System.out.println("0. 退出");
            
            System.out.print("请输入功能编号：");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("无效输入，请输入数字。");
                continue;
            }
            
            switch (choice) {
                case 0:
                    System.out.println("感谢使用！再见！");
                    scanner.close();
                    return;
                    
                case 1:
                    System.out.print("请输入文本文件路径：");
                    String filePath = scanner.nextLine();
                    graph = buildGraphFromFile(filePath);
                    System.out.println("有向图构建完成！");
                    break;
                    
                case 2:
                    if (graph == null) {
                        System.out.println("请先构建有向图！");
                    } else {
                        showDirectedGraph(graph);
                    }
                    break;
                    
                case 3:
                    if (graph == null) {
                        System.out.println("请先构建有向图！");
                    } else {
                        System.out.print("请输入第一个单词：");
                        String word1 = scanner.nextLine();
                        System.out.print("请输入第二个单词：");
                        String word2 = scanner.nextLine();
                        
                        String result = graph.queryBridgeWords(word1, word2);
                        System.out.println(result);
                    }
                    break;
                    
                case 4:
                    if (graph == null) {
                        System.out.println("请先构建有向图！");
                    } else {
                        System.out.print("请输入文本：");
                        String inputText = scanner.nextLine();
                        
                        String newText = graph.generateNewText(inputText);
                        System.out.println("生成的新文本：" + newText);
                    }
                    break;
                    
                case 5:
                    if (graph == null) {
                        System.out.println("请先构建有向图！");
                    } else {
                        System.out.print("请输入起始单词：");
                        String startWord = scanner.nextLine().trim();
                        
                        if (startWord.isEmpty()) {
                            System.out.println("起始单词不能为空！");
                            break;
                        }
                        
                        System.out.print("请输入目标单词（直接回车可计算到任意单词的最短路径）：");
                        String endWord = scanner.nextLine().trim();
                        
                        String path = graph.calcShortestPath(startWord, endWord.isEmpty() ? null : endWord);
                        System.out.println(path);
                    }
                    break;
                    
                case 6:
                    if (graph == null) {
                        System.out.println("请先构建有向图！");
                    } else {
                        System.out.print("请输入单词：");
                        String word = scanner.nextLine();
                        
                        Double pr = graph.calPageRank(word);
                        if (pr < 0) {
                            System.out.println("单词 \"" + word + "\" 不在图中！");
                        } else {
                            System.out.println("单词 \"" + word + "\" 的PageRank值为：" + pr);
                        }
                    }
                    break;
                    
                case 7:
                    if (graph == null) {
                        System.out.println("请先构建有向图！");
                    } else {
                        String walkPath = graph.randomWalk();
                        System.out.println("随机游走路径：" + walkPath);
                    }
                    break;
                    
                default:
                    System.out.println("无效的功能编号，请重新输入。");
            }
        }
    }
}
