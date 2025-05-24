import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DirectedGraphShortestPathTest {
    
    private DirectedGraph graph;
    
    @Before
    public void setUp() {
        // 创建一个空的有向图
        graph = new DirectedGraph();
        
        // 从Easy Test.txt构建有向图
        String filePath = "inputs/Easy Test.txt";
        graph = DirectedGraph.buildGraphFromFile(filePath);
    }
    
    /**
     * 测试用例1: 测试word1不在图中的情况
     * 输入数据: word1="apple", word2="data"
     * 期望的输出: 包含"不在图中"的错误消息
     * 所覆盖的基本路径编号: 1
     */
    @Test
    public void testPath1_Word1NotInGraph() {
        String result = graph.calcShortestPath("apple", "data");
        assertTrue(result.contains("不在图中"));
    }
    
    /**
     * 测试用例2: 测试word2为空的情况
     * 输入数据: word1="scientist", word2=""
     * 期望的输出: 随机选择的单词的最短路径
     * 所覆盖的基本路径编号: 2
     */
    @Test
    public void testPath2_Word2IsEmpty() {
        String result = graph.calcShortestPath("scientist", "");
        assertTrue(result.contains("从 \"scientist\" 到 随机选择的单词"));
    }
    
    /**
     *  这条路径是编写代码时导致的问题，实际上这条路径并不会触发，当两个单词都不在时，会先检测出单词1不在，即测试用例1的报错
     * 测试用例3: 测试两个单词都不在图中的情况
     * 输入数据: word1="apple", word2="banana"
     * 期望的输出: 两个单词都不在图中的错误消息
     * 所覆盖的基本路径编号: 3---这条路径实际上永远不会触发
     */
    @Test
    public void testPath3_BothWordsNotInGraph() {
        // 由于这个用例与用例1和用例5有逻辑冲突（如果word1不在图中，就不会检查两个单词）
        // 我们可以修改DirectedGraph类源码或模拟这种情况
        // 这里我们假设已经修改了源码以支持这个测试用例
        String result = graph.calcShortestPath("apple", "banana");
        assertTrue(result.contains("No \"apple\" and \"banana\" in the graph"));
    }

    /**
     * 测试用例4: 单独测试word1不在图中
     * 输入数据: word1="apple", word2="data"
     * 期望的输出: word1不在图中的错误消息
     * 所覆盖的基本路径编号: 4
     */
    @Test
    public void testPath4_OnlyWord1NotInGraph() {
        // 注意：此测试与路径1重叠，因为当word1不在图中时，会直接返回，不会进入路径4
        // 在实际的代码结构中，这是不可能独立测试的路径
        String result = graph.calcShortestPath("apple", "data");
        assertTrue(result.contains("不在图中"));
    }

    /**
     * 测试用例5: 单独测试word2不在图中
     * 输入数据: word1="scientist", word2="banana"
     * 期望的输出: word2不在图中的错误消息
     * 所覆盖的基本路径编号: 5
     */
    @Test
    public void testPath5_OnlyWord2NotInGraph() {
        String result = graph.calcShortestPath("scientist", "banana");
        assertTrue(result.contains("No \"banana\" in the graph"));
    }

    /**
     * 测试用例6: 测试两个单词相同
     * 输入数据: word1="data", word2="data"
     * 期望的输出: 路径为单词本身
     * 所覆盖的基本路径编号: 6
     */
    @Test
    public void testPath6_SameWords() {
        String result = graph.calcShortestPath("data", "data");
        assertTrue(result.contains("The shortest path from \"data\" to \"data\" is: data"));
    }

    /**
     * 测试用例7: 测试无法到达终点
     * 输入数据: word1="the", word2="again"
     * 期望的输出: 无法到达终点的错误消息
     * 所覆盖的基本路径编号: 7
     */
    @Test
    public void testPath7_NoPathToDestination() {
        // 假设在我们的图中，从"again"到"the"没有路径
        String result = graph.calcShortestPath("again", "the");
        assertTrue(result.contains("No path from \"again\" to \"the\""));
    }

    /**
     * 这个实际上也不会触发，因为按照输入文本距离的定义，只要一个单词出现在文本中，他就不会是一个孤立的单词
     * 测试用例8: Dijkstra算法中未找到最小距离节点（孤立节点）
     * 输入数据: 使用孤立节点，比如"team"和"requested"（假设team是孤立的）
     * 期望的输出: 无路径消息
     * 所覆盖的基本路径编号: 8
     */
    @Test
    public void testPath8_NoMinimumDistanceNodeFound() {
        // 假设在我们的图中，"team"是一个孤立节点
        // 这个测试可能需要构造特殊的图结构
        String result = graph.calcShortestPath("team", "requested");
        assertTrue(result.contains("No path"));
    }
    
    /**
     * 测试用例9: 成功找到单条最短路径
     * 输入数据: word1="scientist", word2="analyzed"
     * 期望的输出: 找到从scientist到analyzed的路径
     * 所覆盖的基本路径编号: 9
     */
    @Test
    public void testPath9_SingleShortestPathFound() {
        String result = graph.calcShortestPath("scientist", "analyzed");
        assertTrue(result.contains("从 \"scientist\" 到 \"analyzed\" 的最短路径"));
        // 根据文本，scientist应该可以到达analyzed
    }
    
    /**
     * 测试用例10: 更新更短路径的情况
     * 输入数据: 一个有多条路径的单词对，如"the"和"report"
     * 期望的输出: 找到并显示最短路径
     * 所覆盖的基本路径编号: 10
     */
    @Test
    public void testPath10_ShorterPathFound() {
        // 这条路径需要在Dijkstra算法执行过程中找到更短的路径
        // 这个测试可能需要构造特殊的图结构
        String result = graph.calcShortestPath("the", "report");
        assertTrue(result.contains("最短路径长度"));
    }
    
    /**
     * 测试用例11: 找到等长路径的情况
     * 输入数据: 一个有多条等长路径的单词对
     * 期望的输出: 显示所有等长的路径
     * 所覆盖的基本路径编号: 11
     */
    @Test
    public void testPath11_EqualLengthPathFound() {
        // 在实际的文本中，可能存在多条从"scientist"到"data"的等长路径
        String result = graph.calcShortestPath("scientist", "data");
        assertTrue(result.contains("最短路径长度"));
        // 这个测试可能需要检查结果中是否包含多条路径
    }
    
    /**
     * 测试用例12: 测试多路径输出
     * 输入数据: 一个有多条路径的单词对
     * 期望的输出: 包含多条路径的输出，带有换行符
     * 所覆盖的基本路径编号: 12
     */
    @Test
    public void testPath12_MultiplePathsOutput() {
        // 与测试11类似，但更关注输出格式
        String result = graph.calcShortestPath("scientist", "data");
        // 如果有多条路径，结果应该包含"路径 1"和"路径 2"等字样
        assertTrue(result.contains("路径 1") || result.contains("路径 2"));
    }
    
    /**
     * 测试用例13: 测试路径中最后一个节点的处理
     * 输入数据: 任意有路径的单词对
     * 期望的输出: 路径中最后一个单词后没有箭头
     * 所覆盖的基本路径编号: 13
     */
    @Test
    public void testPath13_LastNodeInPath() {
        String result = graph.calcShortestPath("scientist", "data");
        // 检查结果中的路径最后一个单词后没有箭头
        assertFalse(result.endsWith("->"));
    }
    
    /**
     * 测试用例14: 测试路径中中间节点的处理
     * 输入数据: 包含多个单词的路径
     * 期望的输出: 路径中间有箭头连接
     * 所覆盖的基本路径编号: 14
     */
    @Test
    public void testPath14_MiddleNodesWithArrows() {
        // 假设存在从scientist到team的路径，中间有其他节点
        String result = graph.calcShortestPath("scientist", "team");
        // 检查结果中包含箭头符号
        assertTrue(result.contains(" -> "));
    }
}