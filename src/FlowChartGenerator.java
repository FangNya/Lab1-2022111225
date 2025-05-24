import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FlowChartGenerator {
    public static void main(String[] args) {
        generateCalcShortestPathFlowChart("shortest_path_flowchart.dot");
        System.out.println("流程图DOT文件已生成：shortest_path_flowchart.dot");
        System.out.println("请使用Graphviz命令生成图像：");
        System.out.println("dot -Tpng shortest_path_flowchart.dot -o shortest_path_flowchart.png");
    }

    public static void generateCalcShortestPathFlowChart(String outputFile) {
        try (PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {
            // 流程图的DOT语言描述
            out.println("digraph CalcShortestPath {");
            out.println("  rankdir=TB;");
            out.println("  node [shape=box, style=filled, fillcolor=lightblue];");
            
            // 开始节点
            out.println("  start [shape=oval, label=\"开始\"];");
            
            // 输入参数
            out.println("  input [label=\"输入: word1, word2\"];");
            
            // 转换为小写
            out.println("  lowercase1 [label=\"word1 = word1.toLowerCase()\"];");
            
            // 检查起始单词
            out.println("  check_word1 [shape=diamond, label=\"word1 在图中?\"];");
            out.println("  error1 [shape=box, style=filled, fillcolor=pink, label=\"返回错误：起始单词不在图中\"];");
            
            // 检查第二个单词
            out.println("  check_word2 [shape=diamond, label=\"word2为null或空?\"];");
            out.println("  any_word [label=\"调用calcShortestPathToAnyWord(word1)\"];");
            
            // 第二个单词转小写
            out.println("  lowercase2 [label=\"word2 = word2.toLowerCase()\"];");
            
            // 检查两个单词
            out.println("  check_both [shape=diamond, label=\"word1和word2都不在图中?\"];");
            out.println("  error_both [shape=box, style=filled, fillcolor=pink, label=\"返回错误：两个单词都不在图中\"];");
            
            out.println("  check_word1_again [shape=diamond, label=\"word1不在图中?\"];");
            out.println("  error_word1 [shape=box, style=filled, fillcolor=pink, label=\"返回错误：word1不在图中\"];");
            
            out.println("  check_word2_again [shape=diamond, label=\"word2不在图中?\"];");
            out.println("  error_word2 [shape=box, style=filled, fillcolor=pink, label=\"返回错误：word2不在图中\"];");
            
            // 获取索引
            out.println("  get_indices [label=\"获取单词索引\\nstartIndex = wordToIndex.get(word1)\\nendIndex = wordToIndex.get(word2)\"];");
            
            // 检查是否相同单词
            out.println("  check_same [shape=diamond, label=\"startIndex == endIndex?\"];");
            out.println("  same_word [shape=box, label=\"返回：路径为单词本身\"];");
            
            // 初始化Dijkstra
            out.println("  init_dijkstra [label=\"初始化Dijkstra\\n- 创建距离数组\\n- 创建前驱映射\\n- 创建访问数组\\n- 设置起点距离为0\"];");
            
            // Dijkstra主循环
            out.println("  dijkstra_loop [label=\"Dijkstra主循环\\nfor i = 0 to n-1\"];");
            
            // 找最小距离节点
            out.println("  find_min [label=\"找到距离最小的未访问节点\"];");
            
            // 检查是否找到最小节点
            out.println("  check_min [shape=diamond, label=\"找到最小距离节点?\"];");
            out.println("  break_loop [label=\"跳出循环\"];");
            
            // 标记已访问
            out.println("  mark_visited [label=\"标记节点为已访问\"];");
            
            // 更新邻居
            out.println("  update_neighbors [label=\"更新邻居节点距离\\nfor j = 0 to n-1\"];");
            
            // 检查是否可达终点
            out.println("  check_reachable [shape=diamond, label=\"终点不可达?\"];");
            out.println("  no_path [shape=box, style=filled, fillcolor=pink, label=\"返回错误：无法到达终点\"];");
            
            // 查找所有路径
            out.println("  find_all_paths [label=\"使用DFS找出所有最短路径\"];");
            
            // 构建结果
            out.println("  build_result [label=\"构建结果字符串\"];");
            
            // 返回结果
            out.println("  return_result [label=\"返回最短路径结果\"];");
            
            // 结束节点
            out.println("  end [shape=oval, label=\"结束\"];");
            
            // 连接节点
            out.println("  start -> input;");
            out.println("  input -> lowercase1;");
            out.println("  lowercase1 -> check_word1;");
            out.println("  check_word1 -> error1 [label=\"否\"];");
            out.println("  check_word1 -> check_word2 [label=\"是\"];");
            out.println("  check_word2 -> any_word [label=\"是\"];");
            out.println("  any_word -> end;");
            out.println("  check_word2 -> lowercase2 [label=\"否\"];");
            out.println("  lowercase2 -> check_both;");
            out.println("  check_both -> error_both [label=\"是\"];");
            out.println("  check_both -> check_word1_again [label=\"否\"];");
            out.println("  check_word1_again -> error_word1 [label=\"是\"];");
            out.println("  check_word1_again -> check_word2_again [label=\"否\"];");
            out.println("  check_word2_again -> error_word2 [label=\"是\"];");
            out.println("  check_word2_again -> get_indices [label=\"否\"];");
            out.println("  get_indices -> check_same;");
            out.println("  check_same -> same_word [label=\"是\"];");
            out.println("  same_word -> end;");
            out.println("  check_same -> init_dijkstra [label=\"否\"];");
            out.println("  init_dijkstra -> dijkstra_loop;");
            out.println("  dijkstra_loop -> find_min;");
            out.println("  find_min -> check_min;");
            out.println("  check_min -> break_loop [label=\"否\"];");
            out.println("  check_min -> mark_visited [label=\"是\"];");
            out.println("  mark_visited -> update_neighbors;");
            out.println("  update_neighbors -> dijkstra_loop [label=\"继续循环\"];");
            out.println("  break_loop -> check_reachable;");
            out.println("  check_reachable -> no_path [label=\"是\"];");
            out.println("  no_path -> end;");
            out.println("  check_reachable -> find_all_paths [label=\"否\"];");
            out.println("  find_all_paths -> build_result;");
            out.println("  build_result -> return_result;");
            out.println("  return_result -> end;");
            out.println("  error1 -> end;");
            out.println("  error_both -> end;");
            out.println("  error_word1 -> end;");
            out.println("  error_word2 -> end;");
            
            out.println("}");
        } catch (IOException e) {
            System.err.println("生成流程图时出错: " + e.getMessage());
        }
    }
} 