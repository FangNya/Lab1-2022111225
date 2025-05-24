import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.io.File;

public class ControlFlowGraphGenerator {
    public static void main(String[] args) {
        String dotFile = "shortest_path_cfg.dot";
        String pngFile = "shortest_path_cfg.png";
        
        generateCalcShortestPathCFG(dotFile);
        System.out.println("控制流图DOT文件已生成：" + dotFile);
        System.out.println("请使用Graphviz生成图像：");
        System.out.println("dot -Tpng -Gcharset=utf8 " + dotFile + " -o " + pngFile);
    }

    public static void generateCalcShortestPathCFG(String outputFile) {
        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(
                new java.io.FileOutputStream(outputFile), StandardCharsets.UTF_8))) {
            
            // 控制流图的DOT语言描述
            out.println("digraph ControlFlowGraph {");
            out.println("  rankdir=TB;");
            out.println("  graph [fontname=\"Microsoft YaHei\" fontsize=12];");
            out.println("  node [fontname=\"Microsoft YaHei\" fontsize=12 shape=box style=filled fillcolor=lightblue];");
            out.println("  edge [fontname=\"Microsoft YaHei\" fontsize=10];");
            
            // 节点定义 - 基本块
            out.println("  entry [shape=oval, label=\"入口: calcShortestPath\"];");
            
            // 基本块1: 初始化和第一个检查
            out.println("  block1 [label=\"word1 = word1.toLowerCase();\\n检查word1是否在图中\"];");
            
            // 基本块2: word1不在图中的处理
            out.println("  block2 [shape=box, style=filled, fillcolor=pink, label=\"返回错误：起始单词不在图中\"];");
            
            // 基本块3: 检查word2是否为null或空
            out.println("  block3 [label=\"检查word2是否为null或空\"];");
            
            // 基本块4: 处理word2为空的情况
            out.println("  block4 [label=\"调用calcShortestPathToAnyWord(word1)\"];");
            
            // 基本块5: 转换word2为小写
            out.println("  block5 [label=\"word2 = word2.toLowerCase()\"];");
            
            // 基本块6: 检查两个单词是否都不在图中
            out.println("  block6 [label=\"检查word1和word2是否都不在图中\"];");
            
            // 基本块7: 两个单词都不在图中的处理
            out.println("  block7 [shape=box, style=filled, fillcolor=pink, label=\"返回错误：两个单词都不在图中\"];");
            
            // 基本块8: 再次检查word1是否不在图中
            out.println("  block8 [label=\"检查word1是否不在图中\"];");
            
            // 基本块9: word1不在图中的处理
            out.println("  block9 [shape=box, style=filled, fillcolor=pink, label=\"返回错误：word1不在图中\"];");
            
            // 基本块10: 检查word2是否不在图中
            out.println("  block10 [label=\"检查word2是否不在图中\"];");
            
            // 基本块11: word2不在图中的处理
            out.println("  block11 [shape=box, style=filled, fillcolor=pink, label=\"返回错误：word2不在图中\"];");
            
            // 基本块12: 获取索引
            out.println("  block12 [label=\"获取索引:\\nstartIndex = wordToIndex.get(word1)\\nendIndex = wordToIndex.get(word2)\"];");
            
            // 基本块13: 检查是否是相同的单词
            out.println("  block13 [label=\"检查startIndex == endIndex\"];");
            
            // 基本块14: 相同单词的处理
            out.println("  block14 [label=\"返回：路径为单词本身\"];");
            
            // 基本块15: 初始化Dijkstra算法
            out.println("  block15 [label=\"初始化Dijkstra:\\n- 创建距离数组\\n- 创建前驱映射\\n- 创建访问数组\\n- 设置起点距离为0\"];");
            
            // 基本块16: Dijkstra主循环开始
            out.println("  block16 [label=\"进入Dijkstra主循环\\nfor i = 0 to n-1\"];");
            
            // 基本块17: 查找最小距离节点
            out.println("  block17 [label=\"查找距离最小的未访问节点\"];");
            
            // 基本块18: 检查是否找到最小距离节点
            out.println("  block18 [label=\"检查是否找到最小距离节点\"];");
            
            // 基本块19: 没有找到最小节点，跳出循环
            out.println("  block19 [label=\"跳出循环\"];");
            
            // 基本块20: 标记访问和更新邻居
            out.println("  block20 [label=\"标记节点为已访问\\n更新邻居节点距离\"];");
            
            // 基本块21: 检查是否可达终点
            out.println("  block21 [label=\"检查终点是否可达\"];");
            
            // 基本块22: 终点不可达的处理
            out.println("  block22 [shape=box, style=filled, fillcolor=pink, label=\"返回错误：无法到达终点\"];");
            
            // 基本块23: 查找所有最短路径并构建结果
            out.println("  block23 [label=\"使用DFS查找所有最短路径\\n构建结果字符串\"];");
            
            // 基本块24: 返回结果
            out.println("  block24 [label=\"返回最短路径结果\"];");
            
            // 出口节点
            out.println("  exit [shape=oval, label=\"出口\"];");
            
            // 控制流边 - 表示可能的执行路径
            out.println("  entry -> block1;");
            out.println("  block1 -> block2 [label=\"否\"];");
            out.println("  block2 -> exit;");
            out.println("  block1 -> block3 [label=\"是\"];");
            out.println("  block3 -> block4 [label=\"是\"];");
            out.println("  block4 -> exit;");
            out.println("  block3 -> block5 [label=\"否\"];");
            out.println("  block5 -> block6;");
            out.println("  block6 -> block7 [label=\"是\"];");
            out.println("  block7 -> exit;");
            out.println("  block6 -> block8 [label=\"否\"];");
            out.println("  block8 -> block9 [label=\"是\"];");
            out.println("  block9 -> exit;");
            out.println("  block8 -> block10 [label=\"否\"];");
            out.println("  block10 -> block11 [label=\"是\"];");
            out.println("  block11 -> exit;");
            out.println("  block10 -> block12 [label=\"否\"];");
            out.println("  block12 -> block13;");
            out.println("  block13 -> block14 [label=\"是\"];");
            out.println("  block14 -> exit;");
            out.println("  block13 -> block15 [label=\"否\"];");
            out.println("  block15 -> block16;");
            out.println("  block16 -> block17 [label=\"i < n\"];");
            out.println("  block17 -> block18;");
            out.println("  block18 -> block19 [label=\"否\"];");
            out.println("  block18 -> block20 [label=\"是\"];");
            out.println("  block20 -> block16 [label=\"继续循环\"];");
            out.println("  block19 -> block21;");
            out.println("  block21 -> block22 [label=\"是\"];");
            out.println("  block22 -> exit;");
            out.println("  block21 -> block23 [label=\"否\"];");
            out.println("  block23 -> block24;");
            out.println("  block24 -> exit;");
            out.println("  block16 -> block21 [label=\"i >= n\"];");
            
            // 添加循环的回边
            out.println("  {rank=same; block16 block19}"); // 将循环头和循环出口放在同一级别

            out.println("}");
        } catch (IOException e) {
            System.err.println("生成控制流图时出错: " + e.getMessage());
        }
    }
} 