import java.io.*;

/**
 * 为DirectedGraph程序生成算法流程图的工具类
 * 使用DOT语言和Graphviz工具绘制流程图
 */
public class AlgorithmFlowcharts {
    private static final String OUTPUT_DIR = "flowcharts/";
    
    /**
     * 程序入口
     */
    public static void main(String[] args) {
        // 创建输出目录
        File dir = new File(OUTPUT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        // 生成各个模块的流程图
        generateBuildGraphFlowchart();
        generateShowGraphFlowchart();
        generateQueryBridgeWordsFlowchart();
        generateNewTextFlowchart();
        generateShortestPathFlowchart();
        generatePageRankFlowchart();
        generateRandomWalkFlowchart();
        generateMainFlowchart();
        
        System.out.println("所有流程图已生成到 " + OUTPUT_DIR + " 目录");
    }
    
    /**
     * 生成根据文本构建图的流程图
     */
    private static void generateBuildGraphFlowchart() {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph BuildGraph {\n");
        dot.append("    node [shape=box, style=filled, fillcolor=lightblue];\n");
        dot.append("    edge [color=blue];\n");
        dot.append("    rankdir=TB;\n\n");
        
        // 添加节点
        dot.append("    start [shape=oval, fillcolor=lightgreen, label=\"开始\"];\n");
        dot.append("    readFile [label=\"读取文本文件\"];\n");
        dot.append("    init [label=\"初始化数据结构\\n(wordToIndex, indexToWord, adjacencyList)\"];\n");
        dot.append("    processLine [label=\"处理文本行\"];\n");
        dot.append("    extractWords [label=\"提取单词\"];\n");
        dot.append("    prevWordNull [shape=diamond, fillcolor=lightyellow, label=\"previousWord\\n是否为null?\"];\n");
        dot.append("    addNode [label=\"将单词添加到图中\"];\n");
        dot.append("    addEdge [label=\"添加边(previousWord → currentWord)\"];\n");
        dot.append("    updatePrevWord [label=\"previousWord = currentWord\"];\n");
        dot.append("    moreWords [shape=diamond, fillcolor=lightyellow, label=\"还有更多单词?\"];\n");
        dot.append("    moreLines [shape=diamond, fillcolor=lightyellow, label=\"还有更多行?\"];\n");
        dot.append("    createMatrix [label=\"创建邻接矩阵\"];\n");
        dot.append("    end [shape=oval, fillcolor=lightgreen, label=\"结束\"];\n");
        
        // 添加边
        dot.append("    start -> readFile;\n");
        dot.append("    readFile -> init;\n");
        dot.append("    init -> processLine;\n");
        dot.append("    processLine -> extractWords;\n");
        dot.append("    extractWords -> prevWordNull;\n");
        dot.append("    prevWordNull -> addNode;\n");
        dot.append("    prevWordNull -> addEdge [label=\"否\"];\n");
        dot.append("    addEdge -> addNode;\n");
        dot.append("    addNode -> updatePrevWord;\n");
        dot.append("    updatePrevWord -> moreWords;\n");
        dot.append("    moreWords -> extractWords [label=\"是\"];\n");
        dot.append("    moreWords -> moreLines [label=\"否\"];\n");
        dot.append("    moreLines -> processLine [label=\"是\"];\n");
        dot.append("    moreLines -> createMatrix [label=\"否\"];\n");
        dot.append("    createMatrix -> end;\n");
        
        dot.append("}");
        
        saveFlowchart(dot.toString(), "build_graph_flowchart.svg");
    }
    
    /**
     * 生成展示图的流程图
     */
    private static void generateShowGraphFlowchart() {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph ShowGraph {\n");
        dot.append("    node [shape=box, style=filled, fillcolor=lightblue];\n");
        dot.append("    edge [color=blue];\n");
        dot.append("    rankdir=TB;\n\n");
        
        // 添加节点
        dot.append("    start [shape=oval, fillcolor=lightgreen, label=\"开始\"];\n");
        dot.append("    checkEmpty [shape=diamond, fillcolor=lightyellow, label=\"图是否为空?\"];\n");
        dot.append("    showError [label=\"显示错误信息\"];\n");
        dot.append("    showNodes [label=\"显示图中节点\"];\n");
        dot.append("    showEdges [label=\"显示图中边\"];\n");
        dot.append("    generateDot [label=\"生成DOT文件\"];\n");
        dot.append("    runGraphviz [label=\"运行Graphviz\"];\n");
        dot.append("    checkSuccess [shape=diamond, fillcolor=lightyellow, label=\"Graphviz\\n执行成功?\"];\n");
        dot.append("    showImage [label=\"显示生成的SVG图像\"];\n");
        dot.append("    showErrorInfo [label=\"显示错误信息\"];\n");
        dot.append("    end [shape=oval, fillcolor=lightgreen, label=\"结束\"];\n");
        
        // 添加边
        dot.append("    start -> checkEmpty;\n");
        dot.append("    checkEmpty -> showError [label=\"是\"];\n");
        dot.append("    checkEmpty -> showNodes [label=\"否\"];\n");
        dot.append("    showNodes -> showEdges;\n");
        dot.append("    showEdges -> generateDot;\n");
        dot.append("    generateDot -> runGraphviz;\n");
        dot.append("    runGraphviz -> checkSuccess;\n");
        dot.append("    checkSuccess -> showImage [label=\"是\"];\n");
        dot.append("    checkSuccess -> showErrorInfo [label=\"否\"];\n");
        dot.append("    showImage -> end;\n");
        dot.append("    showErrorInfo -> end;\n");
        dot.append("    showError -> end;\n");
        
        dot.append("}");
        
        saveFlowchart(dot.toString(), "show_graph_flowchart.svg");
    }
    
    /**
     * 生成查询桥接词的流程图
     */
    private static void generateQueryBridgeWordsFlowchart() {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph QueryBridgeWords {\n");
        dot.append("    node [shape=box, style=filled, fillcolor=lightblue];\n");
        dot.append("    edge [color=blue];\n");
        dot.append("    rankdir=TB;\n\n");
        
        // 添加节点
        dot.append("    start [shape=oval, fillcolor=lightgreen, label=\"开始\"];\n");
        dot.append("    convertCase [label=\"转换单词为小写\"];\n");
        dot.append("    checkWord1 [shape=diamond, fillcolor=lightyellow, label=\"word1是否在图中?\"];\n");
        dot.append("    checkWord2 [shape=diamond, fillcolor=lightyellow, label=\"word2是否在图中?\"];\n");
        dot.append("    errorBoth [label=\"返回：两个单词都不在图中\"];\n");
        dot.append("    errorWord1 [label=\"返回：word1不在图中\"];\n");
        dot.append("    errorWord2 [label=\"返回：word2不在图中\"];\n");
        dot.append("    findBridges [label=\"寻找桥接词(从word1可达且可达word2)\"];\n");
        dot.append("    checkEmpty [shape=diamond, fillcolor=lightyellow, label=\"桥接词列表为空?\"];\n");
        dot.append("    returnNone [label=\"返回：没有桥接词\"];\n");
        dot.append("    checkOne [shape=diamond, fillcolor=lightyellow, label=\"只有一个桥接词?\"];\n");
        dot.append("    returnOne [label=\"返回：唯一的桥接词\"];\n");
        dot.append("    returnMultiple [label=\"返回：多个桥接词\"];\n");
        dot.append("    end [shape=oval, fillcolor=lightgreen, label=\"结束\"];\n");
        
        // 添加边
        dot.append("    start -> convertCase;\n");
        dot.append("    convertCase -> checkWord1;\n");
        dot.append("    checkWord1 -> checkWord2 [label=\"是\"];\n");
        dot.append("    checkWord1 -> errorWord1 [label=\"否\"];\n");
        dot.append("    checkWord2 -> findBridges [label=\"是\"];\n");
        dot.append("    checkWord2 -> errorWord2 [label=\"否\"];\n");
        dot.append("    findBridges -> checkEmpty;\n");
        dot.append("    checkEmpty -> returnNone [label=\"是\"];\n");
        dot.append("    checkEmpty -> checkOne [label=\"否\"];\n");
        dot.append("    checkOne -> returnOne [label=\"是\"];\n");
        dot.append("    checkOne -> returnMultiple [label=\"否\"];\n");
        dot.append("    errorBoth -> end;\n");
        dot.append("    errorWord1 -> end;\n");
        dot.append("    errorWord2 -> end;\n");
        dot.append("    returnNone -> end;\n");
        dot.append("    returnOne -> end;\n");
        dot.append("    returnMultiple -> end;\n");
        
        dot.append("}");
        
        saveFlowchart(dot.toString(), "query_bridge_words_flowchart.svg");
    }
    
    /**
     * 生成根据桥接词生成新文本的流程图
     */
    private static void generateNewTextFlowchart() {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph GenerateNewText {\n");
        dot.append("    node [shape=box, style=filled, fillcolor=lightblue];\n");
        dot.append("    edge [color=blue];\n");
        dot.append("    rankdir=TB;\n\n");
        
        // 添加节点
        dot.append("    start [shape=oval, fillcolor=lightgreen, label=\"开始\"];\n");
        dot.append("    checkEmpty [shape=diamond, fillcolor=lightyellow, label=\"输入文本是否为空?\"];\n");
        dot.append("    returnEmpty [label=\"返回空字符串\"];\n");
        dot.append("    extractWords [label=\"提取文本中的单词\"];\n");
        dot.append("    checkWordCount [shape=diamond, fillcolor=lightyellow, label=\"单词数量<=1?\"];\n");
        dot.append("    returnOriginal [label=\"返回原文本\"];\n");
        dot.append("    processFirstWord [label=\"处理第一个单词\"];\n");
        dot.append("    initForLoop [label=\"初始化循环i=1\"];\n");
        dot.append("    getPrevWord [label=\"获取前一个单词(i-1)\"];\n");
        dot.append("    getCurrWord [label=\"获取当前单词(i)\"];\n");
        dot.append("    queryBridge [label=\"查询桥接词\"];\n");
        dot.append("    checkBridge [shape=diamond, fillcolor=lightyellow, label=\"找到桥接词?\"];\n");
        dot.append("    extractBridges [label=\"提取桥接词列表\"];\n");
        dot.append("    selectRandomBridge [label=\"随机选择一个桥接词\"];\n");
        dot.append("    addBridge [label=\"添加桥接词到新文本\"];\n");
        dot.append("    addCurrentWord [label=\"添加当前单词到新文本\"];\n");
        dot.append("    incrementLoop [label=\"i++\"];\n");
        dot.append("    checkMoreWords [shape=diamond, fillcolor=lightyellow, label=\"i < words.size()?\"];\n");
        dot.append("    returnNewText [label=\"返回新文本\"];\n");
        dot.append("    end [shape=oval, fillcolor=lightgreen, label=\"结束\"];\n");
        
        // 添加边
        dot.append("    start -> checkEmpty;\n");
        dot.append("    checkEmpty -> returnEmpty [label=\"是\"];\n");
        dot.append("    checkEmpty -> extractWords [label=\"否\"];\n");
        dot.append("    extractWords -> checkWordCount;\n");
        dot.append("    checkWordCount -> returnOriginal [label=\"是\"];\n");
        dot.append("    checkWordCount -> processFirstWord [label=\"否\"];\n");
        dot.append("    processFirstWord -> initForLoop;\n");
        dot.append("    initForLoop -> getPrevWord;\n");
        dot.append("    getPrevWord -> getCurrWord;\n");
        dot.append("    getCurrWord -> queryBridge;\n");
        dot.append("    queryBridge -> checkBridge;\n");
        dot.append("    checkBridge -> extractBridges [label=\"是\"];\n");
        dot.append("    checkBridge -> addCurrentWord [label=\"否\"];\n");
        dot.append("    extractBridges -> selectRandomBridge;\n");
        dot.append("    selectRandomBridge -> addBridge;\n");
        dot.append("    addBridge -> addCurrentWord;\n");
        dot.append("    addCurrentWord -> incrementLoop;\n");
        dot.append("    incrementLoop -> checkMoreWords;\n");
        dot.append("    checkMoreWords -> getPrevWord [label=\"是\"];\n");
        dot.append("    checkMoreWords -> returnNewText [label=\"否\"];\n");
        dot.append("    returnEmpty -> end;\n");
        dot.append("    returnOriginal -> end;\n");
        dot.append("    returnNewText -> end;\n");
        
        dot.append("}");
        
        saveFlowchart(dot.toString(), "generate_new_text_flowchart.svg");
    }
    
    /**
     * 生成计算最短路径的流程图
     */
    private static void generateShortestPathFlowchart() {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph ShortestPath {\n");
        dot.append("    node [shape=box, style=filled, fillcolor=lightblue];\n");
        dot.append("    edge [color=blue];\n");
        dot.append("    rankdir=TB;\n\n");
        
        // 添加节点
        dot.append("    start [shape=oval, fillcolor=lightgreen, label=\"开始\"];\n");
        dot.append("    convertCase [label=\"转换单词为小写\"];\n");
        dot.append("    checkWord1 [shape=diamond, fillcolor=lightyellow, label=\"word1是否在图中?\"];\n");
        dot.append("    errorWord1 [label=\"返回：起始单词不在图中\"];\n");
        dot.append("    checkWord2Null [shape=diamond, fillcolor=lightyellow, label=\"word2是否为null?\"];\n");
        dot.append("    calcToAny [label=\"计算到任意单词的最短路径\"];\n");
        dot.append("    checkWord2 [shape=diamond, fillcolor=lightyellow, label=\"word2是否在图中?\"];\n");
        dot.append("    errorWord2 [label=\"返回：目标单词不在图中\"];\n");
        dot.append("    checkSame [shape=diamond, fillcolor=lightyellow, label=\"word1和word2\\n是否相同?\"];\n");
        dot.append("    returnSame [label=\"返回：相同单词的路径\"];\n");
        dot.append("    initDijkstra [label=\"初始化Dijkstra算法\\n(距离、前驱列表、访问状态)\"];\n");
        dot.append("    dijkstraLoop [label=\"Dijkstra主循环\"];\n");
        dot.append("    findMinNode [label=\"找到距离最小的未访问节点\"];\n");
        dot.append("    markVisited [label=\"标记节点为已访问\"];\n");
        dot.append("    updateNeighbors [label=\"更新相邻节点的距离\\n记录多条等长路径\"];\n");
        dot.append("    checkNoPath [shape=diamond, fillcolor=lightyellow, label=\"是否无法到达终点?\"];\n");
        dot.append("    returnNoPath [label=\"返回：无路径\"];\n");
        dot.append("    findAllPaths [label=\"使用DFS找出\\n所有最短路径\"];\n");
        dot.append("    formatResult [label=\"格式化结果字符串\"];\n");
        dot.append("    end [shape=oval, fillcolor=lightgreen, label=\"结束\"];\n");
        
        // 添加边
        dot.append("    start -> convertCase;\n");
        dot.append("    convertCase -> checkWord1;\n");
        dot.append("    checkWord1 -> errorWord1 [label=\"否\"];\n");
        dot.append("    checkWord1 -> checkWord2Null [label=\"是\"];\n");
        dot.append("    checkWord2Null -> calcToAny [label=\"是\"];\n");
        dot.append("    checkWord2Null -> checkWord2 [label=\"否\"];\n");
        dot.append("    checkWord2 -> errorWord2 [label=\"否\"];\n");
        dot.append("    checkWord2 -> checkSame [label=\"是\"];\n");
        dot.append("    checkSame -> returnSame [label=\"是\"];\n");
        dot.append("    checkSame -> initDijkstra [label=\"否\"];\n");
        dot.append("    initDijkstra -> dijkstraLoop;\n");
        dot.append("    dijkstraLoop -> findMinNode;\n");
        dot.append("    findMinNode -> markVisited;\n");
        dot.append("    markVisited -> updateNeighbors;\n");
        dot.append("    updateNeighbors -> dijkstraLoop [label=\"继续\"];\n");
        dot.append("    dijkstraLoop -> checkNoPath [label=\"完成\"];\n");
        dot.append("    checkNoPath -> returnNoPath [label=\"是\"];\n");
        dot.append("    checkNoPath -> findAllPaths [label=\"否\"];\n");
        dot.append("    findAllPaths -> formatResult;\n");
        dot.append("    formatResult -> end;\n");
        dot.append("    errorWord1 -> end;\n");
        dot.append("    errorWord2 -> end;\n");
        dot.append("    returnSame -> end;\n");
        dot.append("    returnNoPath -> end;\n");
        dot.append("    calcToAny -> end;\n");
        
        dot.append("}");
        
        saveFlowchart(dot.toString(), "shortest_path_flowchart.svg");
    }
    
    /**
     * 生成计算PageRank值的流程图
     */
    private static void generatePageRankFlowchart() {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph PageRank {\n");
        dot.append("    node [shape=box, style=filled, fillcolor=lightblue];\n");
        dot.append("    edge [color=blue];\n");
        dot.append("    rankdir=TB;\n\n");
        
        // 添加节点
        dot.append("    start [shape=oval, fillcolor=lightgreen, label=\"开始\"];\n");
        dot.append("    convertCase [label=\"转换单词为小写\"];\n");
        dot.append("    checkWord [shape=diamond, fillcolor=lightyellow, label=\"单词是否在图中?\"];\n");
        dot.append("    returnError [label=\"返回-1.0（单词不在图中）\"];\n");
        dot.append("    initPR [label=\"初始化PageRank值\\n(每个节点初始值为1/n)\"];\n");
        dot.append("    initLoop [label=\"初始化迭代（iter=0）\"];\n");
        dot.append("    checkIter [shape=diamond, fillcolor=lightyellow, label=\"iter < maxIterations?\"];\n");
        dot.append("    calcNewPR [label=\"计算新的PageRank值\"];\n");
        dot.append("    initNodeLoop [label=\"初始化节点循环（i=0）\"];\n");
        dot.append("    checkNodeIndex [shape=diamond, fillcolor=lightyellow, label=\"i < n?\"];\n");
        dot.append("    initBasePR [label=\"初始化基础PR值\\n(1-d)/n\"];\n");
        dot.append("    initIncomingLoop [label=\"初始化入边循环（j=0）\"];\n");
        dot.append("    checkIncomingIndex [shape=diamond, fillcolor=lightyellow, label=\"j < n?\"];\n");
        dot.append("    checkEdge [shape=diamond, fillcolor=lightyellow, label=\"adjacencyMatrix[j][i] > 0?\"];\n");
        dot.append("    calcOutDegree [label=\"计算j的出度\"];\n");
        dot.append("    checkOutDegree [shape=diamond, fillcolor=lightyellow, label=\"outDegree > 0?\"];\n");
        dot.append("    updatePR [label=\"更新PR值\\nPR[i] += d * PR[j] * 权重 / 出度\"];\n");
        dot.append("    incrementJ [label=\"j++\"];\n");
        dot.append("    incrementI [label=\"i++\"];\n");
        dot.append("    calcDiff [label=\"计算新旧PR值的差异\"];\n");
        dot.append("    updatePRArray [label=\"更新PR数组\"];\n");
        dot.append("    checkConverge [shape=diamond, fillcolor=lightyellow, label=\"diff < 阈值?\"];\n");
        dot.append("    incrementIter [label=\"iter++\"];\n");
        dot.append("    returnPR [label=\"返回指定单词的PageRank值\"];\n");
        dot.append("    end [shape=oval, fillcolor=lightgreen, label=\"结束\"];\n");
        
        // 添加边
        dot.append("    start -> convertCase;\n");
        dot.append("    convertCase -> checkWord;\n");
        dot.append("    checkWord -> returnError [label=\"否\"];\n");
        dot.append("    checkWord -> initPR [label=\"是\"];\n");
        dot.append("    initPR -> initLoop;\n");
        dot.append("    initLoop -> checkIter;\n");
        dot.append("    checkIter -> calcNewPR [label=\"是\"];\n");
        dot.append("    checkIter -> returnPR [label=\"否\"];\n");
        dot.append("    calcNewPR -> initNodeLoop;\n");
        dot.append("    initNodeLoop -> checkNodeIndex;\n");
        dot.append("    checkNodeIndex -> initBasePR [label=\"是\"];\n");
        dot.append("    checkNodeIndex -> calcDiff [label=\"否\"];\n");
        dot.append("    initBasePR -> initIncomingLoop;\n");
        dot.append("    initIncomingLoop -> checkIncomingIndex;\n");
        dot.append("    checkIncomingIndex -> checkEdge [label=\"是\"];\n");
        dot.append("    checkIncomingIndex -> incrementI [label=\"否\"];\n");
        dot.append("    checkEdge -> calcOutDegree [label=\"是\"];\n");
        dot.append("    checkEdge -> incrementJ [label=\"否\"];\n");
        dot.append("    calcOutDegree -> checkOutDegree;\n");
        dot.append("    checkOutDegree -> updatePR [label=\"是\"];\n");
        dot.append("    checkOutDegree -> incrementJ [label=\"否\"];\n");
        dot.append("    updatePR -> incrementJ;\n");
        dot.append("    incrementJ -> checkIncomingIndex;\n");
        dot.append("    incrementI -> checkNodeIndex;\n");
        dot.append("    calcDiff -> updatePRArray;\n");
        dot.append("    updatePRArray -> checkConverge;\n");
        dot.append("    checkConverge -> returnPR [label=\"是\"];\n");
        dot.append("    checkConverge -> incrementIter [label=\"否\"];\n");
        dot.append("    incrementIter -> checkIter;\n");
        dot.append("    returnPR -> end;\n");
        dot.append("    returnError -> end;\n");
        
        dot.append("}");
        
        saveFlowchart(dot.toString(), "page_rank_flowchart.svg");
    }
    
    /**
     * 生成随机游走的流程图
     */
    private static void generateRandomWalkFlowchart() {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph RandomWalk {\n");
        dot.append("    node [shape=box, style=filled, fillcolor=lightblue];\n");
        dot.append("    edge [color=blue];\n");
        dot.append("    rankdir=TB;\n\n");
        
        // 添加节点
        dot.append("    start [shape=oval, fillcolor=lightgreen, label=\"开始\"];\n");
        dot.append("    checkEmpty [shape=diamond, fillcolor=lightyellow, label=\"图是否为空?\"];\n");
        dot.append("    returnEmpty [label=\"返回：图为空，无法随机游走\"];\n");
        dot.append("    initVisited [label=\"初始化已访问边集合\"];\n");
        dot.append("    initPath [label=\"初始化路径\"];\n");
        dot.append("    selectStart [label=\"随机选择起始节点\"];\n");
        dot.append("    addToPath [label=\"将当前节点添加到路径\"];\n");
        dot.append("    findNeighbors [label=\"获取当前节点的所有出边\"];\n");
        dot.append("    checkNeighbors [shape=diamond, fillcolor=lightyellow, label=\"是否有出边?\"];\n");
        dot.append("    selectNext [label=\"随机选择下一个节点\"];\n");
        dot.append("    checkVisited [shape=diamond, fillcolor=lightyellow, label=\"边是否已访问?\"];\n");
        dot.append("    markVisited [label=\"标记边为已访问\"];\n");
        dot.append("    updateCurrent [label=\"更新当前节点\"];\n");
        dot.append("    saveToFile [label=\"将路径保存到文件\"];\n");
        dot.append("    returnPath [label=\"返回随机游走路径\"];\n");
        dot.append("    end [shape=oval, fillcolor=lightgreen, label=\"结束\"];\n");
        
        // 添加边
        dot.append("    start -> checkEmpty;\n");
        dot.append("    checkEmpty -> returnEmpty [label=\"是\"];\n");
        dot.append("    checkEmpty -> initVisited [label=\"否\"];\n");
        dot.append("    initVisited -> initPath;\n");
        dot.append("    initPath -> selectStart;\n");
        dot.append("    selectStart -> addToPath;\n");
        dot.append("    addToPath -> findNeighbors;\n");
        dot.append("    findNeighbors -> checkNeighbors;\n");
        dot.append("    checkNeighbors -> saveToFile [label=\"否\"];\n");
        dot.append("    checkNeighbors -> selectNext [label=\"是\"];\n");
        dot.append("    selectNext -> checkVisited;\n");
        dot.append("    checkVisited -> saveToFile [label=\"是\"];\n");
        dot.append("    checkVisited -> markVisited [label=\"否\"];\n");
        dot.append("    markVisited -> updateCurrent;\n");
        dot.append("    updateCurrent -> addToPath;\n");
        dot.append("    saveToFile -> returnPath;\n");
        dot.append("    returnPath -> end;\n");
        dot.append("    returnEmpty -> end;\n");
        
        dot.append("}");
        
        saveFlowchart(dot.toString(), "random_walk_flowchart.svg");
    }
    
    /**
     * 生成主流程图
     */
    private static void generateMainFlowchart() {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph MainFlow {\n");
        dot.append("    node [shape=box, style=filled, fillcolor=lightblue];\n");
        dot.append("    edge [color=blue];\n");
        dot.append("    rankdir=TB;\n\n");
        
        // 添加节点
        dot.append("    start [shape=oval, fillcolor=lightgreen, label=\"开始\"];\n");
        dot.append("    showMenu [label=\"显示功能菜单\"];\n");
        dot.append("    getChoice [label=\"获取用户选择\"];\n");
        dot.append("    checkExit [shape=diamond, fillcolor=lightyellow, label=\"选择是否为0(退出)?\"];\n");
        dot.append("    exit [label=\"显示退出信息并关闭Scanner\"];\n");
        dot.append("    checkChoice [shape=diamond, fillcolor=lightyellow, label=\"选择是哪个功能?\", width=2];\n");
        dot.append("    buildGraph [label=\"1. 从文件构建有向图\"];\n");
        dot.append("    showGraph [label=\"2. 显示有向图\"];\n");
        dot.append("    queryBridge [label=\"3. 查询桥接词\"];\n");
        dot.append("    generateText [label=\"4. 根据桥接词生成新文本\"];\n");
        dot.append("    calcPath [label=\"5. 计算两个单词之间的最短路径\"];\n");
        dot.append("    calcPR [label=\"6. 计算单词的PageRank值\"];\n");
        dot.append("    randomWalk [label=\"7. 随机游走\"];\n");
        dot.append("    invalidChoice [label=\"显示无效选择信息\"];\n");
        dot.append("    end [shape=oval, fillcolor=lightgreen, label=\"结束\"];\n");
        
        // 添加边
        dot.append("    start -> showMenu;\n");
        dot.append("    showMenu -> getChoice;\n");
        dot.append("    getChoice -> checkExit;\n");
        dot.append("    checkExit -> exit [label=\"是\"];\n");
        dot.append("    checkExit -> checkChoice [label=\"否\"];\n");
        dot.append("    checkChoice -> buildGraph [label=\"1\"];\n");
        dot.append("    checkChoice -> showGraph [label=\"2\"];\n");
        dot.append("    checkChoice -> queryBridge [label=\"3\"];\n");
        dot.append("    checkChoice -> generateText [label=\"4\"];\n");
        dot.append("    checkChoice -> calcPath [label=\"5\"];\n");
        dot.append("    checkChoice -> calcPR [label=\"6\"];\n");
        dot.append("    checkChoice -> randomWalk [label=\"7\"];\n");
        dot.append("    checkChoice -> invalidChoice [label=\"其他\"];\n");
        dot.append("    buildGraph -> showMenu;\n");
        dot.append("    showGraph -> showMenu;\n");
        dot.append("    queryBridge -> showMenu;\n");
        dot.append("    generateText -> showMenu;\n");
        dot.append("    calcPath -> showMenu;\n");
        dot.append("    calcPR -> showMenu;\n");
        dot.append("    randomWalk -> showMenu;\n");
        dot.append("    invalidChoice -> showMenu;\n");
        dot.append("    exit -> end;\n");
        
        dot.append("}");
        
        saveFlowchart(dot.toString(), "main_flowchart.svg");
    }
    
    /**
     * 保存流程图到SVG文件
     * @param dotContent DOT语言内容
     * @param outputFileName 输出文件名
     */
    private static void saveFlowchart(String dotContent, String outputFileName) {
        String dotFilePath = OUTPUT_DIR + outputFileName.replace(".svg", ".dot");
        String svgFilePath = OUTPUT_DIR + outputFileName;
        
        // 保存DOT文件
        try (PrintWriter writer = new PrintWriter(new FileWriter(dotFilePath))) {
            writer.print(dotContent);
        } catch (IOException e) {
            System.err.println("保存DOT文件时出错: " + e.getMessage());
            return;
        }
        
        // 使用Graphviz生成SVG
        try {
            ProcessBuilder pb = new ProcessBuilder();
            // Graphviz可执行文件路径
            String graphvizPath = "D:\\Apps\\Tools\\Graphviz-12.2.1-win64\\bin\\dot.exe"; // 默认为空，表示从PATH中查找
            
            // 直接使用dot.exe
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                pb.command("cmd.exe", "/c", graphvizPath, "-Tsvg", dotFilePath, "-o", svgFilePath);
            } else {
                // 对于Unix/Linux/Mac系统
                pb.command(graphvizPath, "-Tsvg", dotFilePath, "-o", svgFilePath);
            }
            
            Process process = pb.start();
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                System.out.println("已生成流程图: " + svgFilePath);
            } else {
                System.err.println("生成" + outputFileName + "时出错，错误代码: " + exitCode);
                System.err.println("请确保已安装Graphviz并将其添加到系统PATH中");
                System.err.println("DOT文件已保存至: " + dotFilePath);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("执行Graphviz命令时出错: " + e.getMessage());
            System.err.println("请确保已安装Graphviz并将其添加到系统PATH中");
            System.err.println("DOT文件已保存至: " + dotFilePath);
        }
    }
} 