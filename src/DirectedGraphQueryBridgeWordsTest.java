import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.File;

public class DirectedGraphQueryBridgeWordsTest {
    
    private DirectedGraph graph;
    
    @Before
    public void setUp() {
        // 在每个测试方法执行前构建图
        String filePath = "inputs/Cursed Be The Treasure.txt";
        File file = new File(filePath);
        if (!file.exists()) {
            fail("测试文件不存在: " + filePath);
        }
        graph = DirectedGraph.buildGraphFromFile(filePath);
    }
    
    @Test
    public void testCase1_OneBridgeWord() {
        // 测试用例1：有一个桥接词
        String word1 = "serious";
        String word2 = "me";
        String expected = "The bridge word from \"serious\" to \"me\" is: \"took\"";
        String actual = graph.queryBridgeWords(word1, word2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCase2_BothWordsNotInGraph() {
        // 测试用例2：两个单词都不在图中
        String word1 = "nonexistent1";
        String word2 = "nonexistent2";
        String expected = "No \"nonexistent1\" and \"nonexistent2\" in the graph!";
        String actual = graph.queryBridgeWords(word1, word2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCase3_SecondWordNotInGraph() {
        // 测试用例3：第二个单词不在图中
        String word1 = "father";
        String word2 = "nonexistent";
        String expected = "No \"nonexistent\" in the graph!";
        String actual = graph.queryBridgeWords(word1, word2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCase4_FirstWordNotInGraph() {
        // 测试用例4：第一个单词不在图中
        String word1 = "nonexistent";
        String word2 = "father";
        String expected = "No \"nonexistent\" in the graph!";
        String actual = graph.queryBridgeWords(word1, word2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCase5_MultipleBridgeWords() {
        // 测试用例5：有多个桥接词
        String word1 = "and";
        String word2 = "the";
        String actual = graph.queryBridgeWords(word1, word2);
        assertTrue(actual.startsWith("The bridge words from \"and\" to \"the\" are:"));
        // 注意：因为可能有多个桥接词且顺序不确定，所以使用startsWith判断
    }
    
    @Test
    public void testCase6_NoBridgeWords() {
        // 测试用例6：没有桥接词
        String word1 = "rattling";
        String word2 = "talk";
        String expected = "No bridge words from \"rattling\" to \"talk\"!";
        String actual = graph.queryBridgeWords(word1, word2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCase7_EmptySecondWord() {
        // 测试用例7：第二个单词为空
        String word1 = "the";
        String word2 = "";
        String expected = "No \"\" in the graph!";
        String actual = graph.queryBridgeWords(word1, word2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCase8_FirstWordNotInTextButSecondWordIn() {
        // 测试用例8：第一个单词不在文本中，第二个在
        String word1 = "uniqueword";
        String word2 = "the";
        String expected = "No \"uniqueword\" in the graph!";
        String actual = graph.queryBridgeWords(word1, word2);
        assertEquals(expected, actual);
    }

}