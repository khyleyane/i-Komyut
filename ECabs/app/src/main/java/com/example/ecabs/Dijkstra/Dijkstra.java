package com.example.ecabs.Dijkstra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.ecabs.Utils.SQLHelper;

public class Dijkstra {

    SQLHelper dbHelper;
    Cursor cursor;

    String[][] graph = new String[100][100];
    public String shortestPath1 = "";
    public String status = "none";

    public void shortestPath(String[][] arg_graph, int startNode, int endNode) {

        if (startNode == endNode) {
            status = "die";
            return;
        }

        graph = arg_graph;
        int start_node = startNode;
        int current_node = startNode;
        int end_node = endNode;

        if (current_node != end_node) {
            // CALCULATE THE NUMBER OF NODES
            int node_count = 0;
            for (String[] array : graph) {
                if (array[0] != null) {
                    node_count += 1;
                }
            }

            // MARK THE NODES TO BE PROCESSED
            List<Integer> nodesToProcess = new ArrayList<Integer>();

            // TO STORE THE MARKED * VALUES
            List<Integer> markedNodeValues_below = new ArrayList<Integer>();

            double markedNodeValue = 0;
            double markedNodeValueFixed = 0;

            // LOOP HANDLE
            for (int iteration = 0; iteration < 1; iteration++) {
                // TO GET THE MINIMUM WEIGHT FROM EACH NODE
                List<Double> comparisonAllWeights = new ArrayList<Double>();

                // REGISTER THE FIRST NODE TO BE PROCESSED INTO THE ARRAY
                if (!nodesToProcess.contains(current_node)) {
                    nodesToProcess.add(current_node);
                }

                // LOOP THROUGH MARKED NODES
                for (int iterationNode = 0; iterationNode < nodesToProcess.size(); iterationNode++) {
                    // CALCULATE THE NUMBER OF ROWS PER COLUMN OF THE NODE
                    int row_count_fixed = 0;
                    for (int min_row_limit = 0; min_row_limit < 100; min_row_limit++) {
                        if (graph[nodesToProcess.get(iterationNode)][min_row_limit] != null) {
                            row_count_fixed += 1;
                        }
                    }

                    // FIND THE MINIMUM WEIGHT in 1 node based on rows sequentially [0][0],[0][1], etc.
                    List<Double> weights = new ArrayList<Double>();
                    int row_status = 0;

                    // Loop to find weights in 1 NODE
                    for (int min_row_limit_fixed = 0; min_row_limit_fixed < row_count_fixed; min_row_limit_fixed++) {
                        String weight_and_edges = graph[nodesToProcess.get(iterationNode)][min_row_limit_fixed];
                        String[] explode;
                        explode = weight_and_edges.split("->");

                        // Find weights that haven't been processed (that don't have ->y)
                        if (explode.length == 2) {
                            row_status += 1; // there are still weights not processed ->y

                            // Check if the node has been marked or not, if it has been marked, the * value is not added again / 0
                            // if it is not marked, then the * value is the value markedNodeValueFixed
                            if (!markedNodeValues_below.isEmpty()) {
                                if (markedNodeValues_below.contains(nodesToProcess.get(iterationNode))) {
                                    markedNodeValue = 0;
                                } else {
                                    markedNodeValue = markedNodeValueFixed;
                                }
                            }
                            // Split the string into latitude and longitude and parse them separately
                            String[] weightParts = explode[1].split(", ");
                            double latitudeValue = Double.parseDouble(weightParts[0]);
                            //double longitudeValue = Double.parseDouble(weightParts[1]);

                            weights.add((latitudeValue + markedNodeValue));
                            graph[nodesToProcess.get(iterationNode)][min_row_limit_fixed] = String.valueOf(
                                    explode[0] + "->" + (latitudeValue + markedNodeValue));
                        }
                    }

                    // if the row in the column is not all ->y, then do the if below:
                    if (row_status > 0) {
                        // GET THE MINIMUM WEIGHT
                        for (int weight_index = 0; weight_index < weights.size(); weight_index++) {
                            if (weights.get(weight_index) <= weights.get(0)) {
                                weights.set(0, weights.get(weight_index));
                            }
                        }

                        comparisonAllWeights.add(weights.get(0));
                    } else {
                        // If all rows in the column are already ->y, then do the else below:
                    }

                    // REGISTER THE NEWLY FINISHED NODES
                    if (!markedNodeValues_below.contains(nodesToProcess.get(iterationNode))) {
                        markedNodeValues_below.add(nodesToProcess.get(iterationNode));
                    }
                }

                // GET THE MINIMUM WEIGHT FROM THE MARKED NODES
                for (int min_indexBetweenMarkedWeights = 0; min_indexBetweenMarkedWeights < comparisonAllWeights
                        .size(); min_indexBetweenMarkedWeights++) {
                    if (comparisonAllWeights.get(min_indexBetweenMarkedWeights) <= comparisonAllWeights.get(0)) {
                        comparisonAllWeights.set(0, comparisonAllWeights.get(min_indexBetweenMarkedWeights));
                    }
                }

                // GET THE ORIGINAL NODE + WEIGHT INDEX MARKED FROM THE MARKED NODES
                int originalIndex = 0; // node index
                int row_status1 = 0;
                int can_getOriginalIndexWeight = 0;
                int old_node = 0;
                for (Integer originalIndex_weight : nodesToProcess) {
                    for (int row1 = 0; row1 < 100; row1++) {
                        if (graph[nodesToProcess.get(originalIndex)][row1] != null) {
                            String weight_and_edges1 = graph[nodesToProcess.get(originalIndex)][row1];
                            String[] explode1;
                            explode1 = weight_and_edges1.split("->");
                            if (explode1.length == 2) {
                                if (comparisonAllWeights.get(0) == Double.parseDouble(explode1[1])) {
                                    can_getOriginalIndexWeight = row1;
                                    old_node = nodesToProcess.get(originalIndex);
                                    current_node = Integer.parseInt(explode1[0]);
                                    row_status1 += 1;
                                }
                            }
                        }
                    }
                    originalIndex++; // node index +1
                }

                // BULLETIN THE MINIMUM WEIGHT OBTAINED AND REMOVE THE CONNECTED EDGES
                if (row_status1 > 0) {
                    graph[old_node][can_getOriginalIndexWeight] = graph[old_node][can_getOriginalIndexWeight] + "->y";

                    // REMOVE OTHER EDGES
                    for (int min_column = 0; min_column < node_count; min_column++) {
                        for (int min_row = 0; min_row < 100; min_row++) {

                            if (graph[min_column][min_row] != null) {
                                String edgeToBeRemoved = graph[min_column][min_row];
                                String[] explode3 = edgeToBeRemoved.split("->");
                                if (explode3.length == 2) {
                                    if (explode3[0].equals(String.valueOf(current_node))) {
                                        graph[min_column][min_row] = graph[min_column][min_row] + "->t";
                                    }
                                } // end if check ->y or ->t
                            } // end if check row != null
                        } // end for rows
                    } // end for columns
                } // end if check if all rows have been ->y or ->t


                if (!comparisonAllWeights.isEmpty()) {
                    // Marked * Value
                    markedNodeValueFixed = comparisonAllWeights.get(0);
                    if (current_node != end_node) {
                        --iteration;
                    } else {
                        break; // end the loop
                    }
                }else {
                    break;
                }
            }//end for handling the loop
            // System.out.println("--DONE--");

            // put the combined nodes into an array; for example: nodes 6-10
            List<String> mergeNodeOptions = new ArrayList<String>();
            for (int h = 0; h < node_count; h++) {
                for (int n = 0; n < 100; n++) {
                    if (graph[h][n] != null) {
                        String str_graph = graph[h][n];
                        if (str_graph.substring(str_graph.length() - 1, str_graph.length()).equals("y")) {
                            String[] explode4 = graph[h][n].split("->");
                            String mergedNode = h + "-" + explode4[0];

                            mergeNodeOptions.add(mergedNode);
                        }
                    } // end if check graph content != null
                } // end for looping rows
            } // end looping columns (nodes)


            // insert the nodes that have been sorted (from the destination node to the source node). (later reverse the array)
            List<Integer> sortedNodesFinish = new ArrayList<Integer>();
            // insert the destination node (last node) into the array with index 0. (later reverse the array)
            sortedNodesFinish.add(end_node);
            System.out.println("Start Node: "+ start_node);
            System.out.println("End Node: "+end_node);
            System.out.println(mergeNodeOptions);
            int node_explode = end_node;

            for (int v = 0; v < 1; v++) {
                for (int w = 0; w < mergeNodeOptions.size(); w++) {
                    String explodedNode = mergeNodeOptions.get(w);
                    String[] explode5 = explodedNode.split("-");
                    System.out.println("node_explode: " + node_explode + " explode5[0]: " +explode5[0] + " explode5[1]: " + explode5[1]);
                    if (node_explode == Integer.parseInt(explode5[1])) {
                        sortedNodesFinish.add(Integer.parseInt(explode5[0]));
                        node_explode = Integer.parseInt(explode5[0]);
                    }
                    if (node_explode == start_node) {
                        break;
                    }
                }

                if (start_node != node_explode) {
                    --v;
                } else {
                    break;
                }
            } // end to find the nodes marked and then compare with the destination node

            System.out.println("Sorted Node: " + sortedNodesFinish);
            // reverse the array index; so that the destination node is moved to the end of the array index
            Collections.reverse(sortedNodesFinish);
            String shortest_path = "";
            for (int x = 0; x < sortedNodesFinish.size(); x++) {
                if (x == sortedNodesFinish.size() - 1) {
                    shortest_path += sortedNodesFinish.get(x);
                } else {
                    shortest_path += sortedNodesFinish.get(x) + "->";
                }
            }

            // System.out.println("... " + shortest_path);
            // Toast.makeText(getBaseContext(), "... " + shortest_path, Toast.LENGTH_LONG).show();
            shortestPath1 = shortest_path;
        }//end if start != finish
    }
}

